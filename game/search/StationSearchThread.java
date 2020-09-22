package lifegame.game.search;

import java.util.ArrayList;

import lifegame.game.Window;
import lifegame.game.map.Coordinates;
import lifegame.game.search.model.SearchThreadModel;

public class StationSearchThread extends SearchThreadModel{
	//start
	public StationSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime();
	}

	//start
	public StationSearchThread(Window window,int searchtime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime(searchtime);
	}

	//continue
	public StationSearchThread(StationSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;//現在地の近傍マス

		boolean first;//分岐検知用フラグ
		Coordinates next = new Coordinates();//次の移動マス格納用
		while(count<=Window.count && count<=10 && System.currentTimeMillis()-Window.time<StationSearchThread.searchTime) {
			//初期化
			next.setValue(0, 0);
			first=true;

			//終了
			synchronized(StationSearchThread.lock2) {
				if(Window.japan.containsStation(this.nowMass)) {
					goal();
					break;
				}
			}

			//情報の取得・更新
			super.count++;
			synchronized(StationSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			super.moveTrajectory.add(new Coordinates(this.nowMass));

			//移動
			for(Coordinates coor:list) {
				boolean conti=false;
				for(Coordinates trajectory:super.moveTrajectory) {//既に通った場所を省く
					if(trajectory.contains(coor)) {//来た道の場合
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
					StationSearchThread thread = new StationSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				super.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	private void threadCopy(StationSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(StationSearchThread.lock2) {
			Searcher.setNearestStationResult(this.count, this.nowMass);
		}
	}

}
