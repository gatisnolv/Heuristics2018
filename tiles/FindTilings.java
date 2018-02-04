package tiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import tiles.TilingPlus.Skyline.Node;

public class FindTilings {

	private class TileDescription{
		int count, xlen, ylen;
		TileDescription(int count, int xlen, int ylen){
			this.count=count;
			this.xlen=xlen;
			this.ylen=ylen;
			
		}
	}
	
	private static final String DRAW_DOT = ". ";
	private static final String DRAW_SPACE = "  ";
	private static final String DIM_DELIMITER ="x";
	private static final double BILLION=1000000000.0;
	
	int boardWidth;
	int boardHeight;
	Tile[] tiles;
	Tiling tiling;
	TilingPlus tilingPlus;
	int solutioncount=0;
	int algorithm=0;
	long startTime;
	long endTime;
	ArrayList<Solution> solutions;
	int timeout;
	boolean printTilings;
	
	FindTilings(){
	}

	public void exampleTilesAndBoard() {
		tiling=new Tiling(6,5);
		tilingPlus=new TilingPlus(6, 5);
		tiles=new Tile[4];
		tiles[0]=new Tile(2,3);
		tiles[1]=new Tile(4,4);
		tiles[2]=new Tile(2,2);
		tiles[3]=new Tile(4,1);
		// printSkyline();
		addNextTile2();
//		printSolutions();
		// addNextTile1();
	}
	
	public void tstAdd2(int tileNr, int x, int y){
		System.out.println(tilingPlus.possileToPlace(tiles, tileNr, x, y));
		tilingPlus.placeTile(tiles[tileNr], x, y);
		printTilingLineDrawingRev(getBoardFromCurrentPlacement(tiles));
		printSkyline();
	}

	public void tstRem2(int tileNr){
		tilingPlus.removeTile(tiles[tileNr]);
		printTilingLineDrawingRev(getBoardFromCurrentPlacement(tiles));
		printSkyline();
	}

	public void printSkyline(){
		Node node=tilingPlus.skyline.leftSide.getNext();
		int i=0;
		while(i<10 && node.getNext()!=null && node.getCoordinate().getX()!=Integer.MAX_VALUE) {
			System.out.printf("c: "+node.getCoordinate().getX()+","+ node.getCoordinate().getY()+ " , l: "+node.getLength() + " |");
			node=node.getNext();
			i++;
		}
		System.out.println();
	}

	public void tstAdd(int tileNr, int x, int y){
		System.out.println(tiling.possileToPlace(tiles, tileNr, x, y));
		tiling.placeTile(tiles[tileNr], x, y);
		printTilingLineDrawingRev(getBoardFromCurrentPlacement(tiles));
		printTopline();
	}

	public void tstRem(int tileNr){
		tiling.removeTile(tiles[tileNr]);
		printTilingLineDrawingRev(getBoardFromCurrentPlacement(tiles));
		printTopline();
	}

	public void printPolledTopline(){
		int qsize=tiling.getTopline().size();
		for(int i=0;i<qsize;i++){
			Coordinate c=tiling.getTopline().poll();
			System.out.println(c.getX()+","+c.getY()+", ");
		}
		System.out.println();
	}

	public void printTopline(){
		PriorityQueue<Coordinate> topline=tiling.getTopline();
		System.out.println("Topline length:"+tiling.getTopline().size());
		for(Coordinate c:topline){
			System.out.printf(c.getX()+","+c.getY()+", ");
		}
		System.out.println();
	}

	public void printSolutions(){
		for(Solution s:solutions){
			tiles=s.getTiles();
			printTilingLineDrawingRev(getBoardFromCurrentPlacement(tiles));
		}
	}

	public Board getBoardFromCurrentPlacement(Tile[] tiles){
		Board current=new Board(boardWidth, boardHeight);
		for (int i=0;i<tiles.length;i++){
			if(tiles[i].isPlaced()){
				Tile tile=tiles[i];
				current.writeTile(tile, tile.getLocation().getX(), tile.getLocation().getY(), i);
			}
		}
		return current;
	}

	public boolean timeOut(){
		if(timeout==0){
			return false;
		}else{
			endTime=System.nanoTime();
			if((endTime-startTime)/BILLION>timeout){
				// System.out.printf("Timeout (%d s) exceeded\n", timeout);
				return true;
			}
			return false;
		}
	}

	public boolean pruneAreaFill(int valleyWidth, int valleyArea){
		for(Tile tile:tiles){
			if(!tile.isPlaced() && tile.getShortestDim()<=valleyWidth){
				valleyArea-=tile.getArea();
				if(valleyArea<=0){
					break;
				}
			}
			
		}
		if (valleyArea>0){// tiles that fit dont add up to the area to fill
			return true;
		}else{
			return false;
		}
	}
	
	public boolean pruneSymmetryBreak(){
		Tile smallest=tiles[tiles.length-1];
		if(smallest.isPlaced()){
			int xcoord=smallest.getLocation().getX();
			int ycoord=smallest.getLocation().getY();
			int xlen=smallest.getXLen();
			int ylen=smallest.getYLen();
			double xmid=xcoord+(double)xlen/2.0;
			double ymid=ycoord+(double)ylen/2.0;

			double boardXMid=(double)boardWidth/2.0;
			double boardYMid=(double)boardHeight/2.0;
			if(xmid<boardXMid || ymid<boardYMid){//not in upper right
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public boolean prune2(int valleyWidth, int valleyArea){
		if (pruneSymmetryBreak()){
			return true;
		}
		if(pruneAreaFill(valleyWidth, valleyArea)){
			return true;
		}
		return false;
	}

	public void addNextTile2(){
		int[] loc=new int[2];
		int x, y;
		int[] valleyParams=new int[2];
		int valleyWidth, valleyArea;

		
		if(!timeOut()){
			if (!tilingPlus.findNextPlace(loc/*, valleyParams*/)){
				solutioncount++;
				if(printTilings){
					solutions.add(new Solution(tiles));
				}
			}
			x=loc[0];
			y=loc[1];
			
			for(int i=0;i<tiles.length;i++) {
				if(!tiles[i].isPlaced()) {
					if(tilingPlus.possileToPlace(tiles, i, x, y)){
						tilingPlus.placeTile(tiles[i], x, y);
						tilingPlus.getSmallestValleyParams(valleyParams);
						valleyWidth=valleyParams[0];
						valleyArea=valleyParams[1];
						if(!prune2(valleyWidth, valleyArea)){
							addNextTile2();
						}
						tilingPlus.removeTile(tiles[i]);
					}
					if(!(tiles[i].isSquare() || i==0 && tilingPlus.isSquare())){//dont check rotations if tile is a square or if tile is first and board is square (due to symmetry)
						tiles[i].rotate90();
						if(tilingPlus.possileToPlace(tiles, i, x, y)){
							tilingPlus.placeTile(tiles[i], x, y);
							tilingPlus.getSmallestValleyParams(valleyParams);
							valleyWidth=valleyParams[0];
							valleyArea=valleyParams[1];
							if(!prune2(valleyWidth, valleyArea)){
								addNextTile2();
							}
							tilingPlus.removeTile(tiles[i]);
						}
						tiles[i].rotate90();
					}
					while(true){//skip over identical tiles for the selection of next
						if(i==tiles.length-1){// at the end, so stop
							break;
						}else if (tiles[i].equals(tiles[i+1])) {
							i++;
						}else {//found different tile, so stop skipping
							break;
						}
					}
				}
			}
		}
	}

	public boolean prune1(){
		if (pruneSymmetryBreak()){
			return true;
		}

		return false;
	}

	public void addNextTile1(){
		int[] loc=new int[2];
		int x, y;

		if(!timeOut()){
			if (!tiling.findNextPlace(loc)){
				solutioncount++;
				if(printTilings){
					solutions.add(new Solution(tiles));
				}
			}
			x=loc[0];
			y=loc[1];
			for(int i=0;i<tiles.length;i++) {
				if(!tiles[i].isPlaced()) {
					if(tiling.possileToPlace(tiles, i, x, y)){
						tiling.placeTile(tiles[i], x, y);
						if(!prune1()){
							addNextTile1();
						}
						tiling.removeTile(tiles[i]);
					}
					if(!(tiles[i].isSquare() || i==0 && tiling.isSquare())){//dont check rotations if tile is a square or if tile is first and board is square (due to symmetry)
						tiles[i].rotate90();
						if(tiling.possileToPlace(tiles, i, x, y)){
							tiling.placeTile(tiles[i], x, y);
							if(!prune1()){
								addNextTile1();
							}
							tiling.removeTile(tiles[i]);
						}
						tiles[i].rotate90();
					}
					while(true){//skip over identical tiles for the selection of next
						if(i==tiles.length-1){// at the end, so stop
							break;
						}else if (tiles[i].equals(tiles[i+1])) {
							i++;
						}else {//found different tile, so stop skipping
							break;
						}
					}
				}
			}
		}
	}

	public void initializeBoardAndTiles(int width, int height, ArrayList<TileDescription> tileDescriptionList){
		if(algorithm==1){
			System.out.println("Using basic (Bottom Left) algorithm, timeout: "+timeout+" s");
		}else{
			System.out.println("Using advanced (Smallest valley + pruning) algorithm, timeout: "+timeout+" s");
		}
		
		boardWidth=width;
		boardHeight=height;
		solutions=new ArrayList<Solution>();
		if(algorithm==1){
			tiling=new Tiling(width, height);
		}else{
			tilingPlus=new TilingPlus(width, height);
		}
		int tileCount=0;
		for(TileDescription description:tileDescriptionList){
			tileCount+=description.count;
		}
		tiles=new Tile[tileCount];
		int index=0;
		for(TileDescription description:tileDescriptionList){
			for(int i=0;i<description.count;i++){
				tiles[index]=new Tile(description.xlen,description.ylen);
				index++;
			}
		}


		System.out.println("Board dimensions: "+width+"*"+height+", number of tiles: "+tileCount);
		
		startTime=System.nanoTime();
		if(algorithm==1){
			addNextTile1();			
		}else{
			addNextTile2();
		}
		
		if((endTime-startTime)/BILLION>timeout){
			// System.out.printf("Timeout (%d s) exceeded\n", timeout);
			System.out.printf("TIMEOUT (%d s) EXCEEDED! Solutions found within the timeout: %d\n", timeout, solutioncount);
		}else{
			System.out.println("Total solution count: "+solutioncount);
			endTime=System.nanoTime();
		}
		System.out.println("Elapsed time: "+ (double)((endTime-startTime)/BILLION)+ " s\n");
		
		solutioncount=0;
		if(printTilings){
			printSolutions();
		}
	}

	public void readFile(String filename){
		// String filePath = System.getProperty("user.dir")+"\\src\\tiles\\tilings\\"+filename;
		String filePath = System.getProperty("user.dir")+"\\tilings\\"+filename;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			Scanner inputFile = new Scanner(br);
			String boardDimentions = inputFile.nextLine();
			Scanner boardScanner = new Scanner(boardDimentions);
			boardScanner.next(); 
			int width = boardScanner.nextInt();
			boardScanner.next();
			int height = boardScanner.nextInt();
			boardScanner.close();
			ArrayList<TileDescription> tileDescriptionList = new ArrayList<TileDescription>();
			
			while(inputFile.hasNext()){
				String nextRectangle =  inputFile.nextLine();
				Scanner tileScanner = new Scanner(nextRectangle);
				int count = tileScanner.nextInt();
				tileScanner.next();
				String dimensionDescription = tileScanner.next(); 
				String[] dimensions = dimensionDescription.split(DIM_DELIMITER);
				tileScanner.close();
				tileDescriptionList.add(new TileDescription(count, Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1])));
			}

			inputFile.close();
			System.out.println("Finding tilings for input: "+filename);
			initializeBoardAndTiles(width,height,tileDescriptionList);
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found in current directory");
			System.exit(1);
		}
	}	

	public void printTilingDotDrawing(Board board) {
		for (int i=0;i<=board.xlen;i++) {//upper border
			System.out.print(DRAW_DOT);
		}
		System.out.println();
		for (int y=0;y<board.ylen;y++) {
			System.out.print(DRAW_DOT);//left border
			for (int x=0;x<board.xlen;x++) {
				if(x==board.xlen-1 || board.grid[x][y]!=board.grid[x+1][y]) {//border or different tile. order crucial
					System.out.print(DRAW_DOT);
				}else if(y==board.ylen-1 || board.grid[x][y]!=board.grid[x][y+1]) {
					System.out.print(DRAW_DOT);
				}else if(board.grid[x][y]!=board.grid[x+1][y+1]) {
					System.out.print(DRAW_DOT);
				}else {
					System.out.print(DRAW_SPACE);//same tile, so print blank space
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printTilingDotDrawingRev(Board board) {
		for (int y=board.ylen-1;y>=0;y--) {
			System.out.print(DRAW_DOT);//left border
			for (int x=0;x<board.xlen;x++) {
				if(x==board.xlen-1 || board.grid[x][y]!=board.grid[x+1][y]) {//border or different tile. order crucial
					System.out.print(DRAW_DOT);
				}else if(y==board.ylen-1 || board.grid[x][y]!=board.grid[x][y+1]) {
					System.out.print(DRAW_DOT);
				}else if(board.grid[x][y]!=board.grid[x+1][y+1]) {
					System.out.print(DRAW_DOT);
				}else {
					System.out.print(DRAW_SPACE);//same tile, so print blank space
				}
			}
			System.out.println();
		}
		for (int i=0;i<=board.xlen;i++) {//lower border
			System.out.print(DRAW_DOT);
		}
		System.out.println();
	}
	
	public void printTilingLineDrawing(Board board) {
		for (int i=0;i<=board.xlen-1;i++) {//upper border
			System.out.print(" __");
		}
		System.out.println();
		for (int y=0;y<board.ylen;y++) {
			System.out.print("|");//left border
			for (int x=0;x<board.xlen;x++) {
				if(x==board.xlen-1 && y==board.ylen-1) {
					System.out.print("__|");
				}else if(x==board.xlen-1 && board.grid[x][y]!=board.grid[x][y+1]) {
					System.out.print("__|");
				}else if(x==board.xlen-1) {
					System.out.print("  |");
				}else if(y==board.ylen-1 && board.grid[x][y]!=board.grid[x+1][y]) {
					System.out.print("__|");
				}else if(y==board.ylen-1) {
					System.out.print("__ ");
				}else if(board.grid[x][y]!=board.grid[x+1][y] && board.grid[x][y]!=board.grid[x][y+1]) {
					System.out.print("__|");
				}else if(board.grid[x][y]!=board.grid[x+1][y]) {//border or different tile. order crucial
					System.out.print("  |");
				}else if(board.grid[x][y]!=board.grid[x][y+1]) {
					System.out.print("__ ");
				}else {
					System.out.print("   ");//same tile, so print blank space
				}
			}
			System.out.println();
		}
	}

	public void printTilingLineDrawingRev(Board board) {
		for (int i=0;i<=board.xlen-1;i++) {//upper border
			System.out.print(" __");
		}
		System.out.println();
		
		for (int y=board.ylen-1;y>=0;y--) {
			System.out.print("|");//left border
			for (int x=0;x<board.xlen;x++) {
				if(x==board.xlen-1 && y==0) {
					System.out.print("__|");
				}
				else if(x==board.xlen-1 && board.grid[x][y]!=board.grid[x][y-1]) {
					System.out.print("__|");
				}
				else if(x==board.xlen-1) {
					System.out.print("  |");
				}
				else if(y==0 && board.grid[x][y]!=board.grid[x+1][y]) {
					System.out.print("__|");
				}
				else if(y==0) {
					System.out.print("__ ");
				}
				else if(board.grid[x][y]!=board.grid[x+1][y] && board.grid[x][y]!=board.grid[x][y-1]) {
					System.out.print("__|");
				}
				else if(board.grid[x][y]!=board.grid[x+1][y]) {//border or different tile. order crucial
					System.out.print("  |");
				}
				else if(board.grid[x][y]!=board.grid[x][y-1]) {
					System.out.print("__ ");
				}
				else {
					System.out.print("   ");//same tile, so print blank space
				}
			}
			System.out.println();
		}
	}
	
	public void printTilingId(Board board) {
		for(int y=0;y<board.ylen;y++) {
			for(int x=0;x<board.xlen;x++) {
				System.out.print(board.grid[x][y]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printTilingIdRev(Board board) {
		for(int y=board.ylen-1;y>=0;y--) {
			for(int x=0;x<board.xlen;x++) {
				System.out.print(board.grid[x][y]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void runAll(){
		readFile("1.tiles");
		readFile("2.tiles");
		readFile("3.tiles");
		for(int i=15;i<=55;i+=10){
		for(int j=0;j<=4;j++){
			for(int k=0;k<=4;k++){
				String filename2=Integer.toString(i)+"-"+Integer.toString(j)+"-"+Integer.toString(k)+".tiles";
				readFile(filename2);
			}
		}
		}
	}

	public void start(String filename, int algorithmChoice, int timeLimit, boolean print) {	
		this.algorithm=algorithmChoice;
		timeout=timeLimit;
		printTilings=print;
		if (filename.equals("all")){
			System.out.println("Finding solutions to all tilesets with both algorithms with the specified timeout\n");
			algorithm=1;
			runAll();
			algorithm=2;
			runAll();
		}else{
			filename=filename+".tiles";
			readFile(filename);
//			exampleTilesAndBoard();
		}

		
	}

	public static void printUsageAndExit(){
		System.out.println("Usage: 'java tiles.FindTilings algorithm_to_use(1=basic or 2=advanced) input_filename(without '.tiles' extension)-OR-'all' timeout(seconds, 0=no timeout) print/silent'");
		System.out.println("Example1: 'java tiles.FindTilings 2 15-0-0 60 print'");
		System.out.println("Example1: 'java tiles.FindTilings 1 all 20 silent'");
		System.exit(1);
	}
	
	public static void main(String[] args){
		if (args.length!=4) {
			printUsageAndExit();
		}
		int algorithmChoice=Integer.parseInt(args[0]);
		String filename=args[1];
		int timeLimit=Integer.parseInt(args[2]);
		String modeString=args[3];
		boolean print=false;

		if(modeString.equals("silent")){
			print=false;
		}else if(modeString.equals("print")){
			print=true;
		}else{
			printUsageAndExit();
		}

		
		if (algorithmChoice!=1 && algorithmChoice!=2){
			printUsageAndExit();
		}

		
		new FindTilings().start(filename, algorithmChoice, timeLimit, print);
	}
	
}