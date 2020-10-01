/*
 * Searcherクラスで使われる探索用Threadのクラス
 * 探索する際の処理を記述
 */

package lifegame.game.event.search.model;

import java.util.ArrayList;

import lifegame.game.object.map.information.Coordinates;

public abstract class SearchThreadModel extends Thread{
	protected ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	protected static final Object lock1 = new Object();
	protected static final Object lock2 = new Object();
	protected static final Object lock3 = new Object();
	protected static final Object lock4 = new Object();
	public static int searchTime;
	protected int count=0;
	protected Coordinates nowMass=new Coordinates();
	protected ArrayList<Coordinates> goals = new ArrayList<Coordinates>();

	public static void initSearchTime() {
		searchTime=500;
	}

	public static void initSearchTime(int searchtime) {
		searchTime=searchtime;
	}

	public synchronized void addGoal(Coordinates coor) {
		goals.add(coor);
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	public void setCount(int count) {
		this.count = count;
	}

	protected void goal() {}
}