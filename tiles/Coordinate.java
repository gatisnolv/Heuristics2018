package tiles;

public class Coordinate implements Comparable<Coordinate>{
	private static final int NOT_SET=-1;
	
	private int x, y;
	
	Coordinate(){
		clear();
	}

	Coordinate(int x, int y){
		set(x, y);
	}

	Coordinate(Coordinate input){
		set(input.getX(), input.getY());
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

	public void setX(int x){
		this.x=x;
	}

	public void setY(int y){
		this.y=y;
	}
	
	public void clear() {
		x=NOT_SET;
		y=NOT_SET;
	}

	@Override
	public int compareTo(Coordinate input) {
		if(this.y<input.y) {
			return -1;
		}else if (this.y>input.y) {
			return 1;
		}else {//y component equal
			if (this.x<input.x) {
				return -1;
			}else if (this.x>input.x) {
				return 1;
			}else {
				return 0;
			}
		}
	}
	
	public boolean equals(Object obj){
		Coordinate input=(Coordinate) obj;
		if(input.x==x && input.y==y) {
			return true;
		}else {
			return false;
		}
	}
	
}
