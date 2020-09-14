package lifegame.game;

import java.util.ArrayList;

public class Coordinates {
	private int x;
	private int y;
	private int cost=0;
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

	public int getCost() {
		return cost;
	}

	public void open(int count) {
		cost = count+getVirtualCost();
	}

	public void resetCost() {
		cost = 0;
	}

	private int getVirtualCost() {
		return getDistance(this,Window.japan.getGoal());
	}

	//現在の地点でのコストを取得
	public int getCost(Coordinates now,Coordinates goal){
		return getDistance(now,goal);
	}
	//最適探索範囲を取得
	public int getMaxCost(Coordinates start,Coordinates goal){
		return getDistance(this,start) + getDistance(this,goal);
	}
	//4方向連結の場合マンハッタン距離がパーフェクトヒューリスティックとなる。
	//2点間の距離(マンハッタン距離)
	public int getDistance(Coordinates coor1,Coordinates coor2){
		return Math.abs(coor1.getX()-coor2.getX())+Math.abs(coor1.getY()-coor2.getY());
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

	//自分の方が大きいときtrue
	public boolean containsCost(Coordinates coor) {
		return getCost()>=coor.getCost();
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
