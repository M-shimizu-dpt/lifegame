package lifegame.game.search;

import java.util.ArrayList;

import lifegame.game.Window;
import lifegame.game.map.Coordinates;
import lifegame.game.search.model.SearchThreadModel;

public class ShopSearchThread extends SearchThreadModel{
	//start
	public ShopSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime();
	}
	//start
	public ShopSearchThread(Window window,int searchtime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
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
		while(count<=Window.count && count<=20 && System.currentTimeMillis()-Window.time<ShopSearchThread.searchTime) {
			next.setValue(0, 0);
			first=true;
			synchronized(ShopSearchThread.lock2) {
				if(Window.japan.containsShop(this.nowMass)) {
					goal();
					break;
				}
			}

			count++;
			synchronized(ShopSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));

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
		this.setWindow(original.window);
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