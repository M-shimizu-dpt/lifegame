package lifegame.game.event.search;

import java.util.ArrayList;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.Searcher;
import lifegame.game.event.search.model.SearchThreadModel;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;

public class ShopSearchThread extends SearchThreadModel{
	//start
	public ShopSearchThread() {
		Searcher.time = System.currentTimeMillis();
		Searcher.count=100;
		super.initSearchTime();
	}
	//start
	public ShopSearchThread(int searchtime) {
		Searcher.time = System.currentTimeMillis();
		Searcher.count=100;
		super.initSearchTime(searchtime);
	}
	//continue
	public ShopSearchThread(ShopSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		while(count<=Searcher.count && count<=20 && System.currentTimeMillis()-Searcher.time<ShopSearchThread.searchTime) {
			next.setValue(0, 0);
			first=true;
			synchronized(ShopSearchThread.lock2) {
				if(ContainsEvent.isShop(this.nowMass)) {
					goal();
					break;
				}
			}

			count++;
			synchronized(ShopSearchThread.lock1) {
				list = Japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));

			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					if(ContainsEvent.coor(moveTrajectory.get(j), coor)) {//来た道の場合
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
					//Threadを立ち上げる
					ShopSearchThread thread = new ShopSearchThread(this);
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

	private void threadCopy(ShopSearchThread original) {
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(ShopSearchThread.lock2) {
			Searcher.setNearestShopResult(count, nowMass);
		}
	}

}