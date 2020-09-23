package lifegame.game.search;

import java.util.ArrayList;

import lifegame.game.map.information.Coordinates;
import lifegame.game.map.print.Window;
import lifegame.game.search.model.SearchThreadModel;

public class NearestSearchThread extends SearchThreadModel{
	public static int nearestCount = 100;//目的地から移動可能マスまでの最短距離
	public NearestSearchThread() {

	}

	//start
	public NearestSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		NearestSearchThread.nearestCount=100;
		this.setWindow(window);
		NearestSearchThread.initSearchTime();
	}

	//start
	public NearestSearchThread(Window window,int searchTime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		NearestSearchThread.nearestCount=100;
		this.setWindow(window);
		NearestSearchThread.initSearchTime(searchTime);
	}

	//continue
	public NearestSearchThread(NearestSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		while(count<=NearestSearchThread.nearestCount && count<=35 && System.currentTimeMillis()-Window.time<searchTime) {
			next.setValue(0, 0);
			first=true;

			count++;
			synchronized(NearestSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));
			for(Coordinates goal:goals) {
				if(goal.contains(nowMass)){
					goal();
					break;
				}
			}
			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					if(moveTrajectory.get(j).contains(coor)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					NearestSearchThread thread = new NearestSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				this.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	@Override
	protected void goal() {
		synchronized(NearestSearchThread.lock2) {
			Searcher.setNearestMass(nowMass,count);
		}
	}

	private void threadCopy(NearestSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
		this.goals.addAll(original.goals);
	}
}
