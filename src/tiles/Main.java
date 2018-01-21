package tiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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
	
	Tile[] tileArray;
	Board board;
	ArrayList<Coordinate> locationsForFirstTile;
	
	Main(){
		// exampleTilesAndBoard();
	}

	public void exampleTilesAndBoard() {
		board=new Board(5,4);
		tileArray=new Tile[4];
		tileArray[0]=new Tile(2,3);
		tileArray[1]=new Tile(2,1);
		tileArray[2]=new Tile(3,1);
		tileArray[3]=new Tile(3,3);

		locationsForFirstTile=getListOfLocationsForFirstTile();
		addNextTile();
		// for(Coordinate c:locationsForFirstTile){
		// 	System.out.println("x="+c.getX()+" y="+c.getY());			
			
		// }
//		System.out.println("x="+(int)Math.round(((double)1/2)));			
		
	}
	
	public boolean isPossibleToPlaceFirstTile(){
		for(Coordinate c: locationsForFirstTile){
			if(board.possileToPlace(tileArray[0], c.getX(), c.getY())){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Coordinate> getListOfLocationsForFirstTile(){
		ArrayList<Coordinate> locations=new ArrayList<Coordinate>();
		for(int y=0;y<(int)Math.round((double)board.ylen/2);y++){
			for(int x=0;x<(int)Math.round((double)board.xlen/2);x++){
				System.out.println("h");
				if(x+((float)tileArray[0].xlen)/2 <= ((float)board.xlen)/2 && y+((float)tileArray[0].xlen)/2 <= ((float)board.xlen)/2){
					if(board.isSquare() && y>x){
						continue;
					}
					locations.add(new Coordinate(x,y));
				}
			}
		}
		return locations;
	}
	
	public boolean findNextPlace(int[] loc){
		for (int y=0;y<board.grid[0].length;y++){
			for(int x=0;x<board.grid.length;x++){
				if(board.grid[x][y]==board.FREE){
					loc[0]=x;
					loc[1]=y;
					return true;
				}
			}
		}
		return false;
	}
	
	public void addNextTile(){
		int[] loc=new int[2];
		int x, y;
		if (!findNextPlace(loc)){
			System.out.println("tiling found");
//			printTilingLineDrawingRev();
			// printTilingIdRev();
		}
		x=loc[0];
		y=loc[1];
		for(int i=0;i<tileArray.length;i++) {
			if(!tileArray[i].isPlaced()) {
				if(board.possileToPlace(tileArray[i], x, y)){
					board.placeTile(tileArray[i], x, y, i);
					if(tileArray[0].isPlaced() || isPossibleToPlaceFirstTile()){
						addNextTile();
					}
					board.removeTile(tileArray[i], x, y, i);
				}
				if(!(tileArray[i].isSquare() || i==0 && board.isSquare())){//dont check rotations if tile is a square or if tile is first and board is square (due to symmetry)
					tileArray[i].rotate90();
					if(board.possileToPlace(tileArray[i], x, y)){
						board.placeTile(tileArray[i], x, y, i);
							if(tileArray[0].isPlaced() || isPossibleToPlaceFirstTile()){
								addNextTile();
							}
						board.removeTile(tileArray[i], x, y, i);
					}
					tileArray[i].rotate90();
				}
				while(true){//skip over identical tiles for the selection of next
					if(i==tileArray.length-1){// at the end, so stop
						break;
					}else if (tileArray[i].equals(tileArray[i+1])) {
						i++;
					}else {//found different tile, so stop skipping
						break;
					}
				}
			}
		}
	}

	public void initializeBoardAndTiles(int width, int height, ArrayList<TileDescription> tileDescriptionList){
		int tileCount=0;
		for(TileDescription description:tileDescriptionList){
			tileCount+=description.count;
		}
		board = new Board(width, height);
		tileArray=new Tile[tileCount];
		int index=0;
		for(TileDescription description:tileDescriptionList){
			for(int i=0;i<description.count;i++){
				tileArray[index]=new Tile(description.xlen,description.ylen);
				index++;
			}
		}
		System.out.println("Board and tiles initialized");
		
		addNextTile();
	}

	public void readFile(String filename){
		String filePath = System.getProperty("user.dir")+"\\src\\tiles\\tilings\\"+filename;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			Scanner inputFile = new Scanner(br);
			String boardDimentions = inputFile.nextLine();
			Scanner boardScanner = new Scanner(boardDimentions);
			boardScanner.next(); 
			int boardwidth = boardScanner.nextInt();
			boardScanner.next();
			int boardheight = boardScanner.nextInt();
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
			System.out.println("File parsed");
			initializeBoardAndTiles(boardwidth,boardheight,tileDescriptionList);
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found in current directory");
			System.exit(1);
		}
	}	
	
	public void printTilingDotDrawing() {
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

	public void printTilingDotDrawingRev() {
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
		// System.out.println();
	}
	
	public void printTilingLineDrawing() {
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
		// System.out.println();		
	}

	public void printTilingLineDrawingRev() {
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
		// System.out.println();		
	}
	
	public void printTilingId() {
		for(int y=0;y<board.ylen;y++) {
			for(int x=0;x<board.xlen;x++) {
				System.out.print(board.grid[x][y]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printTilingIdRev() {
		for(int y=board.ylen-1;y>=0;y--) {
			for(int x=0;x<board.xlen;x++) {
				System.out.print(board.grid[x][y]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void start(String filename) {	
		readFile(filename);
		// exampleTilesAndBoard();
		// System.out.println(System.getProperty("user.dir"));
	}
	
	public static void main(String[] args){
		if (args.length!=1) {
			System.out.println("Usage: 'java findtilings input_filename_without_extension'");
			System.exit(1);
		}
		String filename=args[0]+".tiles";
		System.out.println("Finding tilings for input: "+filename);
		new Main().start(filename);
	}
	
}
