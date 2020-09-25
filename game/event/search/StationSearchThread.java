package lifegame.game.event.search;

import java.util.ArrayList;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.search.model.SearchThreadModel;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.Window;

public class StationSearchThread extends SearchThreadModel{
	//start
	public StationSearchThread(Window window) {
		Searcher.time = System.currentTimeMillis();
		Searcher.count=100;
		super.setWindow(window);
		super.initSearchTime();
	}

	//start
	public StationSearchThread(Window window,int searchtime) {
		Searcher.time = System.currentTimeMillis();
		Searcher.count=100;
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
		while(count<=Searcher.count && count<=10 && System.currentTimeMillis()-Searcher.time<StationSearchThread.searchTime) {
			//初期化
			next.setValue(0, 0);
			first=true;

			//終了
			synchronized(StationSearchThread.lock2) {
				if(ContainsEvent.isStation(this.nowMass)) {
					goal();
					break;
				}
			}

			//情報の取得・更新
			super.count++;
			synchronized(StationSearchThread.lock1) {
				list = Japan.getMovePossibles(this.nowMass);
			}
			super.moveTrajectory.add(new Coordinates(this.nowMass));

			//移動
			for(Coordinates coor:list) {
				boolean conti=false;
				for(Coordinates trajectory:super.moveTrajectory) {//既に通った場所を省く
					if(ContainsEvent.coor(trajectory, coor)) {//来た道の場合
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
