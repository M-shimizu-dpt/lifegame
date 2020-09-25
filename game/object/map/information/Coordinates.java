/*
 * 座標を管理するクラス
 * マスの座標に関する処理を記述
 */

package lifegame.game.object.map.information;

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

	public void close() {
		cost = 0;
	}

	private int getVirtualCost() {
		return getDistance(this,Japan.getGoal());
	}
	//最適探索範囲を取得
	public int getMaxCost(Coordinates start,Coordinates goal){
		int Tolerances = 6;
		return getDistance(this,start) + getDistance(this,goal) + Tolerances;
	}
	//4方向連結のため、マンハッタン距離(perfect heuristic)を採用
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
}
