package tiles;

public class Board extends Rectangle {
	
	private static final int FREE=-1;

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
	
	public boolean possileToPlace(Tile tile, int xloc, int yloc) {
		if (grid[xloc][yloc]!=FREE || xloc+tile.xlen>xlen || yloc+tile.ylen>ylen) {
			return false;
		}else{
			for(int i=0;i<tile.ylen;i++) {
				for(int j=0;j<tile.xlen;j++) {
					if (grid[xloc+j][yloc+i]!=FREE) {
						return false;
					}
				}
			}
			return true;
		}
	}
	
	public void placeTile(Tile tile, int tileID, int xloc, int yloc) {
		for(int i=0;i<tile.ylen;i++) {
			for(int j=0;j<tile.xlen;j++) {
				grid[xloc+j][yloc+i]=tileID;
			}
		}
	}

}
