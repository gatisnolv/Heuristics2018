package tiles;

import tiles.TilingPlus.Skyline.Node;

public class TilingPlus extends Rectangle {

	public class Skyline{

		public class Node{
			private Coordinate coordinate;
			private Node next;
			private Node prev;
			private int length;

			Node(Coordinate coord, int length){
				coordinate=coord;
				this.length=length;
			}

			public int getLength(){
				return length;
			}

			public void setLength(int length){
				this.length=length;
			}

			public Node getNext(){
				return next;
			}
			
			public Node getPrev(){
				return prev;
			}

			public void setNext(Node node){
				next=node;
			}

			public void setPrev(Node node){
				prev=node;
			}

			public Coordinate getCoordinate(){
				return coordinate;
			}

			public int getX(){
				return coordinate.getX();
			}

			public int getY(){
				return coordinate.getY();
			}

			public void setX(int x){
				coordinate.setX(x);
			}

			public void setY(int y){
				coordinate.setY(y);
			}


		}

		public Node leftSide;
		public Node rightSide;
		public Node[] nodePool;//pool of reusable Nodes for Skyline - allows to avoid creating objects at search time

		Skyline(int xlen){
			leftSide=new Node(new Coordinate(Integer.MIN_VALUE, Integer.MAX_VALUE),0);
			rightSide=new Node(new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE),0);
			nodePool=new Node[xlen];
			for(int i=0;i<nodePool.length;i++){
				nodePool[i]=new Node(new Coordinate(i,0),0);
			}
			nodePool[0].setLength(xlen);//initial segment representing entire width of board
			nodePool[0].setPrev(leftSide);
			nodePool[0].setNext(rightSide);
			leftSide.setNext(nodePool[0]);
			rightSide.setPrev(leftSide.getNext());
		}

		public void add(Coordinate coord, int length, int height){
			Node current=skyline.leftSide.getNext();
			while(current.getNext().getX()<=coord.getX()){//find segment with smaller or equal x coordinate
				current=current.getNext();
			}
			Node prev=current.getPrev();//retrieve neigbor segments
			Node next=current.getNext();
			int currentLength, prevLength, nextLength, lengthDifference, currentHeight, prevHeight, nextHeight, newHeight, currentX;
			currentLength=current.getLength();
			prevLength=prev.getLength();
			nextLength=next.getLength();
			lengthDifference=currentLength-length;
			currentHeight=current.getY();
			prevHeight=prev.getY();
			nextHeight=next.getY();
			newHeight=currentHeight+height;
			currentX=current.getX();

			if(prevHeight==newHeight && nextHeight==newHeight){//prev and next are of equal height with new height of curr
				if(lengthDifference>0){// there is a gap between the updateable segment and the old next segment, so a new segment to represent it has to be inserted
					Node newNextNode=nodePool[currentX+length];
					newNextNode.setLength(lengthDifference);
					newNextNode.setY(currentHeight);
					prev.setLength(prevLength+length);

					newNextNode.setNext(next);
					newNextNode.setPrev(prev);
					prev.setNext(newNextNode);
					next.setPrev(newNextNode);
					
				}else{
					prev.setLength(prevLength+length+nextLength);
					prev.setNext(next.getNext());
					next.getNext().setPrev(prev);
				}
			}else if(prevHeight==newHeight){//prev is of equal height with new height
				prev.setLength(prevLength+length);

				if (lengthDifference>0){//there is a gap between the updateable segment and the old next segment, so a new segment to represent it has to be inserted
					Node newNextNode=nodePool[currentX+length];
					newNextNode.setLength(lengthDifference);
					newNextNode.setY(currentHeight);

					newNextNode.setNext(next);
					newNextNode.setPrev(prev);
					next.setPrev(newNextNode);
					prev.setNext(newNextNode);
				}else{
					prev.setNext(next);
					next.setPrev(prev);
				}
			}else if(nextHeight==newHeight){//next is of equal height with new height
				if(lengthDifference>0){//there is a gap between the updateable segment and the old next segment, so a new segment to represent it has to be inserted
					Node newNextNode=nodePool[currentX+length];
					newNextNode.setY(currentHeight);
					current.setY(newHeight);
					current.setLength(length);
					newNextNode.setLength(lengthDifference);

					newNextNode.setNext(next);
					newNextNode.setPrev(current);
					next.setPrev(newNextNode);
					current.setNext(newNextNode);

				}else{
					current.setLength(length+nextLength);
					current.setY(nextHeight);
					next.getNext().setPrev(current);
					current.setNext(next.getNext());
				}
			}
			else{ //neither next nor prev is of equal height with new height
				if(lengthDifference>0){ //there is a gap between the updateable segment and the old next segment, so a new segment to represent it has to be inserted
					Node newNextNode=nodePool[currentX+length];
					newNextNode.setLength(lengthDifference);
					newNextNode.setY(currentHeight);

					newNextNode.setPrev(current);
					newNextNode.setNext(current.next);
					next.setPrev(newNextNode);
					current.setNext(newNextNode);
				}
				current.setLength(length);
				current.setY(newHeight);
			}
		}

		public void remove(Coordinate coord, int length, int height){
			Node current=skyline.leftSide.getNext();
			while(current.getNext().getX()<=coord.getX()){//find segment with smaller or equal x coordinate
				current=current.getNext();
			}
			Node next=current.getNext();//retrieve next segment
			int currentLength, newHeight, currentHeight, nextHeight, nextLength, currentX, newX, currentXEnd, newXEnd;
			currentLength=current.getLength();
			currentHeight=current.getY();
			newHeight=currentHeight-height;
			nextHeight=next.getY();
			nextLength=next.getLength();
			currentX=current.getX();
			newX=coord.getX();
			currentXEnd=currentX+currentLength;
			newXEnd=newX+length;
			
			if(currentX<newX && currentXEnd>newXEnd){//new segment is in the middle, a part of a larger segment
				Node newNext=nodePool[newX];//the new segment
				Node afterNewNext=nodePool[newXEnd];//the segment after the new
				current.setLength(newX-currentX);
				newNext.setLength(length);
				afterNewNext.setLength(currentXEnd-newXEnd);
				newNext.setY(newHeight);
				afterNewNext.setY(currentHeight);

				newNext.setNext(afterNewNext);
				newNext.setPrev(current);
				afterNewNext.setPrev(newNext);
				afterNewNext.setNext(next);
				next.setPrev(afterNewNext);
				current.setNext(newNext);
			}else if(currentX<newX){//new segment is the right part of a larger segment
				current.setLength(newX-currentX);
				Node newNext=nodePool[newX];

				if(nextHeight==newHeight){//height of the new segment is equal to that of the next, so these will be joined
					newNext.setY(nextHeight);
					newNext.setLength(nextLength+length);
					current.setLength(currentLength-length);

					newNext.setPrev(current);
					newNext.setNext(next.getNext());
					next.getNext().setPrev(newNext);
					current.setNext(newNext);
				}else{
					newNext.setLength(length);
					newNext.setY(newHeight);
	
					newNext.setNext(next);
					newNext.setPrev(current);
					next.setPrev(newNext);
					current.setNext(newNext);
				}
			}else if(currentXEnd>newXEnd){//new segment is the left part of a larger segment
				Node newNext=nodePool[newXEnd];
				newNext.setLength(currentLength-length);
				newNext.setY(currentHeight);
				current.setLength(length);
				current.setY(newHeight);

				newNext.setNext(next);
				newNext.setPrev(current);
				next.setPrev(newNext);
				current.setNext(newNext);
			}else{
				if(nextHeight==newHeight){//height of the new segment is equal to that of the next, so these will be joined
					current.setLength(nextLength+length);
					next.getNext().setPrev(current);
					current.setNext(next.getNext());
				}
				current.setY(newHeight);
			}
		}
	}
	
	int boardWidth, boardHeight;
	Skyline skyline;
	
	TilingPlus(int xlen, int ylen) {
        super(xlen, ylen);
        boardWidth=this.getXLen();
        boardHeight=this.getYLen();
		skyline=new Skyline(xlen);
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
		skyline.add(tile.getLocation(), tileWidth, tileHeight);
	}

	public void removeTile(Tile tile){
		Coordinate location=tile.getLocation();
		int tileWidth=tile.getXLen();
		int tileHeight=tile.getYLen();
		skyline.remove(location, tileWidth, tileHeight);
		tile.remove();
    }
    
    public Node findSmallestValley(){
		Node smallestValley=skyline.leftSide;
		int smallestValleyWidth=Integer.MAX_VALUE;
		Node segment=skyline.leftSide;

		while(segment.getNext().getX()<Integer.MAX_VALUE){
			segment=segment.getNext();
			Node leftNeighbor=segment.getPrev();
			Node rigthNeighbor=segment.getNext();
			int leftHeight=leftNeighbor.getY();
			int rightHeight=rigthNeighbor.getY();
			int segmentHeight=segment.getY();
			if(leftHeight>segmentHeight && rightHeight>segmentHeight){
				if(segment.getLength()<smallestValleyWidth){
					smallestValleyWidth=segment.getLength();
					smallestValley=segment;
				}
			}
		}
		return smallestValley;
	}

	public void getSmallestValleyParams(int[] valleyParams){
		Node valley=findSmallestValley();
		int valleyWidth, valleyHeight, valleyArea, leftHight, rightHeight, valleyLevel;
		valleyLevel=valley.getY();
		leftHight=valley.getPrev().getY();
		rightHeight=valley.getNext().getY();
		if(leftHight==Integer.MAX_VALUE && rightHeight==Integer.MAX_VALUE){
			valleyHeight=boardHeight-valleyLevel;
		}else{
			valleyHeight=Math.min(leftHight, rightHeight)-valleyLevel;
		}
		valleyWidth=valley.getLength();
		valleyArea=valleyWidth*valleyHeight;
		valleyParams[0]=valleyWidth;
		valleyParams[1]=valleyArea;
	}
	
	 public boolean findNextPlace(int[] loc){
		Node valley=findSmallestValley();
		Coordinate next=valley.getCoordinate();
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

}