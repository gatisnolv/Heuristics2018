package tiles;

public class Tile extends Rectangle {
	
	private Coordinate location;
	private int area;
	
	Tile(int xlen, int ylen) {
		super(xlen, ylen);
		location = new Coordinate();
		area = xlen*ylen;
	}
	
	Tile(Tile input){
		super(input.getXLen(), input.getYLen());
		location=new Coordinate(input.getLocation());
		area=input.getArea();
	}
	
	public void setLocation(int x, int y) {
		location.set(x, y);
	}

	public int getArea() {
		return area;
	}

	public Coordinate getLocation() {
		return location;
	}
	
	public void remove() {
		location.clear();
	}
	
	public boolean isPlaced() {
		return location.isSet();
	}
	
	public void rotate90() {
		int temp=xlen;
		xlen=ylen;
		ylen=temp;
	}
	
	public boolean equals(Tile tile) {
		return this.xlen==tile.xlen && this.ylen==tile.ylen || this.xlen==tile.ylen && this.ylen==tile.xlen;
	}
	

}
