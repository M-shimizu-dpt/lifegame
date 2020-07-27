package lifegame.game;

public class Coordinates {
	public int x;
	public int y;

	public Coordinates(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public Coordinates() {

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
}
