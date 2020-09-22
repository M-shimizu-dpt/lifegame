package lifegame.game.search;

import java.util.ArrayList;

import lifegame.game.Window;
import lifegame.game.map.Coordinates;
import lifegame.game.search.model.SearchThreadModel;

public class MassSearchThread extends SearchThreadModel{
	//start
	public MassSearchThread(Window window,int count) {
		Window.time = System.currentTimeMillis();
		this.setWindow(window);
		this.setCount(count);
	}

	//continue
	public MassSearchThread(MassSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		//while(true) {
		while(System.currentTimeMillis()-Window.time<MassSearchThread.searchTime) {
			next.setValue(0, 0);
			first=true;
			count--;
			synchronized(MassSearchThread.lock2) {
				moveTrajectory.add(Window.japan.getCoordinates(nowMass));
			}
			if(count < 0) {
				goal();
				break;
			}
			synchronized(MassSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}

			for(Coordinates coor:list) {
				//既に通った場所を省く
				if(moveTrajectory.size()>1) {
					if(moveTrajectory.get(moveTrajectory.size()-2).contains(coor)) {//来た道の場合
						continue;
					}
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					MassSearchThread thread = new MassSearchThread(this);
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

	private void threadCopy(MassSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(MassSearchThread.lock2) {
			Searcher.setCanMoveMassResult(nowMass, moveTrajectory);
		}
	}

}
