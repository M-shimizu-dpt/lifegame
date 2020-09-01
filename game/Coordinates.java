package lifegame.game;

import java.util.ArrayList;

public class Coordinates {
	public int x;
	public int y;
	public ArrayList<Coordinates> links = new ArrayList<Coordinates>();

	public Coordinates(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public Coordinates() {

	}

	public void addLinks(Coordinates coor) {
		links.add(coor);
	}

	public ArrayList<Coordinates> getLinks(){
		return links;
	}
	public void setValue(int x,int y) {
		this.x=x;
		this.y=y;
	}

	public void setValue(Coordinates coor) {
		this.x=coor.x;
		this.y=coor.y;
	}

	public boolean contains(Coordinates coor) {
		if(this.x==coor.x && this.y==coor.y) {
			return true;
		}
		return false;
	}
	public boolean contains(int x, int y) {
		if(this.x==x && this.y==y) {
			return true;
		}
		return false;
	}
}
