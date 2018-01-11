package tiles;

public abstract class Rectangle {
	protected int xlen, ylen;
	
	Rectangle(int xlen, int ylen){
		this.xlen=xlen;
		this.ylen=ylen;
	}
	
	public boolean isSquare() {
		return xlen==ylen;
	}
	
	public int getXLen() {
		return xlen;
	}
	
	public int getYLen() {
		return ylen;
	}
	
	public int getShortestDim() {
		return xlen <= ylen ? xlen : ylen; 
	}
	
	public int getLongestDim() {
		return xlen >= ylen ? xlen : ylen;
	}
}