package tiles;

public class Main {
	
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
	
	public void printTilingDotDrawing() {//work in progress
		for (int i=0;i<=board.xlen;i++) {
			System.out.print(". ");
		}
		System.out.println();
		for (int j=0;j<board.ylen;j++) {
			System.out.print(". ");
			for (int i=1;i<board.xlen;i++) {
				if(board.grid[i][j]!=board.grid[i-1][j]) {
					System.out.print(". ");
				}else if(i==board.xlen-1) {//small issue
					System.out.print("  .");
				}else if(j==board.ylen-1 || board.grid[i][j]!=board.grid[i][j+1]) {
					System.out.print(". ");
				}else {
					System.out.print("  ");
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
