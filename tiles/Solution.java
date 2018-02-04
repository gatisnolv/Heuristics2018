package tiles;

public class Solution {

	Tile[] tiles;
	
	Solution(Tile[] input) {
		tiles=new Tile[input.length];
		for(int i=0;i<input.length;i++){
			tiles[i]=new Tile(input[i]);
		}
	}

	public Tile[] getTiles(){
		return tiles;
	}	
}
