package lifegame.game;

import java.util.ArrayList;

public class Coordinates {
	private int x;
	private int y;
	private ArrayList<Coordinates> links = new ArrayList<Coordinates>();

	public Coordinates(int x,int y) {
		this.x=x;
		this.y=y;
	}

	public Coordinates(Coordinates coor) {
		this.x=coor.getX();
		this.y=coor.getY();
		this.links.addAll(coor.getLinks());
	}

	public Coordinates() {

	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
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
