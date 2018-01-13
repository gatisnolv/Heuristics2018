package tiles;

public class Main {
	
	private static final String DRAW_DOT = ". ";
	private static final String DRAW_SPACE = "  ";
	
	Tile[] tileArray;
	Board board;
	
	Main(){
		exampleTilesAndBoard();
	}
	
	public void exampleTilesAndBoard() {
		board=new Board(6,5);
		tileArray=new Tile[4];
		tileArray[0]=new Tile(2,3);
		tileArray[1]=new Tile(4,4);
		tileArray[2]=new Tile(2,2);
		tileArray[3]=new Tile(4,1);
	}
	
	public void exampleTilePlacement() {
		board.placeTile(tileArray[0], 0, 0, 0);
		board.placeTile(tileArray[1], 1, 2, 0);
		board.placeTile(tileArray[2], 2, 0, 3);
		board.placeTile(tileArray[3], 3, 2, 4);
	}
	
	public void printTilingDotDrawing() {
		for (int i=0;i<=board.xlen;i++) {//upper border
			System.out.print(DRAW_DOT);
		}
		System.out.println();
		for (int j=0;j<board.ylen;j++) {
			System.out.print(DRAW_DOT);//left border
			for (int i=0;i<board.xlen;i++) {
				if(i==board.xlen-1 || board.grid[i][j]!=board.grid[i+1][j]) {//border or different tile. order crucial
					System.out.print(DRAW_DOT);
				}else if(j==board.ylen-1 || board.grid[i][j]!=board.grid[i][j+1]) {
					System.out.print(DRAW_DOT);
				}else {
					System.out.print(DRAW_SPACE);//same tile, so print blank space
				}
			}
			System.out.println();
		}
	}
	
	public void printTilingId() {
		for(int i=0;i<board.ylen;i++) {
			for(int j=0;j<board.xlen;j++) {
				System.out.print(board.grid[j][i]+" ");
			}
			System.out.println();
		}
	}
	
	public void start() {
		exampleTilePlacement();		
		printTilingId();
		printTilingDotDrawing();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
	
}
