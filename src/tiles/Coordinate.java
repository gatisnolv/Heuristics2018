package tiles;

public class Coordinate {
	private static final int NOT_SET=-1;
	
	private int x, y;
	
	Coordinate(){
		clear();
	}

	Coordinate(int x, int y){
		set(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isSet() {
		return x!=NOT_SET && y!=NOT_SET;
	}
	
	public void set(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void clear() {
		x=NOT_SET;
		y=NOT_SET;
	}
	
}
