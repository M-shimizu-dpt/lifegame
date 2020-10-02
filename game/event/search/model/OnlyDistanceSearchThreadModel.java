package lifegame.game.event.search.model;

import java.util.ArrayList;

import lifegame.game.event.ContainsEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.PrivateJapan;


public abstract class OnlyDistanceSearchThreadModel extends Thread{
	public static int searchTime;
	public static long time;
	public static int allCount;
	protected ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	protected static final Object lock1 = new Object();
	protected static final Object lock2 = new Object();
	protected static final Object lock3 = new Object();
	protected static final Object lock4 = new Object();
	protected int count=0;
	protected Coordinates nowMass=new Coordinates();
	protected ArrayList<Coordinates> goals = new ArrayList<Coordinates>();
	protected Coordinates start = new Coordinates();
	protected Player player;
	protected PrivateJapan japan;

	protected OnlyDistanceSearchThreadModel() {
		japan = new PrivateJapan();
		this.japan.init();
	}

	public static void initSearchTime() {
		searchTime=800;
	}

	public static void initSearchTime(int searchTime) {
		OnlyDistanceSearchThreadModel.searchTime=searchTime;
	}

	protected void setCount(int count) {
		this.count=count;
	}

	protected Coordinates getStart() {
		return start;
	}

	protected void setStart(Coordinates coor) {
		this.start=coor;
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	protected void threadCopy(OnlyDistanceSearchThreadModel original) {
		this.setCount(original.count);
		this.setStart(original.start);
		this.player=original.player;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	protected void goal() {
		synchronized(OnlyDistanceSearchThreadModel.lock3) {
			if(ContainsEvent.goalDistance(this.player, this.count)==1) {
				this.player.setGoalDistance(this.count);
			}
		}
	}
}
