package lifegame.game.event.search;

import java.util.ArrayList;

import lifegame.game.event.ContainsEvent;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;


public class OnlyDistanceSearchThread extends Thread{
	private ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	private static final Object lock1 = new Object();
	private static final Object lock2 = new Object();
	private static final Object lock3 = new Object();
	private static final Object lock4 = new Object();
	private int count=0;
	private Coordinates nowMass=new Coordinates();

	//start
	public OnlyDistanceSearchThread() {
		setMass(Japan.getGoalCoor());
		setDaemon(true);
	}

	//start
	public OnlyDistanceSearchThread(int searchTime) {
		setMass(Japan.getGoalCoor());
		setDaemon(true);
	}

	//continue
	public OnlyDistanceSearchThread(OnlyDistanceSearchThread original) {
		threadCopy(original);
		setDaemon(true);
	}

	@Override
	public void run() {
		while(count <= 50) {
			Thread.yield();

			synchronized(OnlyDistanceSearchThread.lock1) {
				Japan.setGoalDistance(nowMass,count);
			}
			ArrayList<Coordinates> list = new ArrayList<Coordinates>();
			moveTrajectory.add(new Coordinates(nowMass));//移動履歴を追加

			count++;
			ArrayList<Coordinates> can = new ArrayList<Coordinates>();
			synchronized(OnlyDistanceSearchThread.lock2) {
				can.addAll(Japan.getMovePossibles(nowMass));
			}
			for(Coordinates possibles : can) {//移動可能マスを取得
				synchronized(OnlyDistanceSearchThread.lock3) {
					if(Japan.getCoordinates(possibles).getGoalDistance()<count) {
						continue;
					}
				}
				boolean conti=false;
				for(Coordinates trajectory : moveTrajectory) {//既に通った場所を省く
					synchronized(OnlyDistanceSearchThread.lock4) {
						if(ContainsEvent.coor(trajectory, possibles)) {//来た道の場合
							conti=true;
							break;
						}
					}
				}
				if(conti) {
					continue;
				}
				list.add(possibles);
			}

			boolean me=true;
			for(Coordinates coor:list) {//移動処理
				if(me) {
					setMass(coor);
					me=false;
				}else {
					//Threadを立ち上げ、移動する
					OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(me)break;
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	private void setCount(int count) {
		this.count=count;
	}

	public int getCount() {
		return this.count;
	}

	public ArrayList<Coordinates> getTrajectory() {
		return this.moveTrajectory;
	}

	private void threadCopy(OnlyDistanceSearchThread original) {
		this.setCount(original.getCount());
		this.moveTrajectory.addAll(original.moveTrajectory);
	}
}
