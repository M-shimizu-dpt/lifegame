package lifegame.game.search;

import lifegame.game.Window;
import lifegame.game.player.Player;


public class OnlyDistanceSearchThread extends SearchThread{
	private Player player;

	//start
	public OnlyDistanceSearchThread(Window window,Player player) {
		super.setWindow(window);
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Window.time = System.currentTimeMillis();
		Window.count=500;
		OnlyDistanceSearchThread.initSearchTime();
	}

	//start
	public OnlyDistanceSearchThread(Window window,Player player,int searchTime) {
		super.setWindow(window);
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Window.time = System.currentTimeMillis();
		Window.count=500;
		OnlyDistanceSearchThread.initSearchTime(searchTime);
	}

	//continue
	public OnlyDistanceSearchThread(OnlyDistanceSearchThread original) {
		threadCopy(original);
	}

	protected void threadCopy(OnlyDistanceSearchThread original) {
		//this.setWindow(original.window);
		super.setCount(original.count);
		super.setStart(original.start);
		this.player=original.player;
		super.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(OnlyDistanceSearchThread.lock3) {
			if(player.containsGoalDistance(super.count)) {
				player.setGoalDistance(super.count);
			}
		}
	}
}