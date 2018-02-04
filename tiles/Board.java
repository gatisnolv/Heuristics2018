package tiles;

public class Board extends Rectangle {
	
	public static final int FREE=-1;

	int[][] grid;
	
	Board(int xlen, int ylen) {
		super(xlen, ylen);
		grid = new int [xlen][ylen]; 
		for(int i=0;i<xlen;i++) {
			for(int j=0;j<ylen;j++) {
				grid[i][j]=FREE;
			}
		}
	}
	
	public void writeTile(Tile tile, int xloc, int yloc, int tileID) {
		for(int i=0;i<tile.ylen;i++) {
			for(int j=0;j<tile.xlen;j++) {
				grid[xloc+j][yloc+i]=tileID;
			}
		}
	}

}