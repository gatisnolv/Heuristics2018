package tiles;

import java.util.PriorityQueue;

public class Tiling extends Rectangle {
	
    Coordinate[][] coordGrid;//grid of reusable coordinates for topline only - allows to avoid creating objects at search time
    PriorityQueue<Coordinate> topline;
    int boardWidth, boardHeight;
	
	Tiling(int xlen, int ylen) {
        super(xlen, ylen);
        boardWidth=this.getXLen();
        boardHeight=this.getYLen();
		coordGrid = new Coordinate [boardWidth][boardHeight+1]; // +1 to allow the topline to be the upper part of completeley filled border
		for(int x=0;x<boardWidth;x++) {
			for(int y=0;y<=boardHeight;y++) {// <= - to allow the topline to be the upper part of completeley filled border
				coordGrid[x][y]=new Coordinate(x,y);
			}
        }
        topline=new PriorityQueue<Coordinate>(boardWidth);
        for(int x=0;x<boardWidth;x++){
			topline.add(new Coordinate(x, 0));
		}
	}
	
	public boolean possileToPlace(Tile[] tiles, int tileNr, int x, int y){
		Tile tile=tiles[tileNr];
		int tileWidth=tile.getXLen();
		int tileHeight=tile.getYLen();
		if(x+tileWidth>boardWidth || y+tileHeight>boardHeight){//check if doesn't go out of borders (we start at 0,0)
			return false;
		}
		int tileLeft=x;
		int tileBottom=y;
		int tileRight=x+tile.getXLen();
		int tileTop=y+tile.getYLen();
		int otherLeft, otherRight, otherTop, otherBottom;

		Coordinate coord;
		for(Tile other:tiles){//look for conflicting tiles
			if(other.isPlaced()){
				coord=other.getLocation();
				otherLeft=coord.getX();
				otherBottom=coord.getY();
				otherRight=otherLeft+other.getXLen();
				otherTop=otherBottom+other.getYLen();
				if(!(tileRight<=otherLeft || tileLeft>=otherRight || tileBottom>=otherTop || tileTop<=otherBottom)){
					return false;
				}
			}
		}
		return true;
	}
	
	public void placeTile(Tile tile, int x, int y){
		tile.setLocation(x, y);
		int tileWidth=tile.getXLen();
		int tileHeight=tile.getYLen();
		for (int i=0;i<tileWidth;i++){//adjust topline
            topline.remove(coordGrid[x+i][y]);            
			topline.add(coordGrid[x+i][y+tileHeight]);
		}
	}

	public void removeTile(Tile tile){
		Coordinate location=tile.getLocation();
		int x=location.getX();
		int y=location.getY();
		int tileWidth=tile.getXLen();
		int tileHeight=tile.getYLen();
		for (int i=0;i<tileWidth;i++){//adjust topline
            topline.remove(coordGrid[x+i][y+tileHeight]);            
			topline.add(coordGrid[x+i][y]);
		}
		tile.remove();
    }
    
    public boolean findNextPlace(int[] loc){
        Coordinate next=topline.peek();
        int x, y;
        x=next.getX();
        y=next.getY();
        if(y>=boardHeight){// - all topline coordinates have exceeded the boardHeight-1 limit (we start at 0,0) 
            return false;
        }else{
            loc[0]=x;
            loc[1]=y;
            return true;
        }
    }

    public PriorityQueue<Coordinate> getTopline(){
        return topline;
    }

}
