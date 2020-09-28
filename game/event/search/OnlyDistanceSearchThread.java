package lifegame.game.event.search;

import lifegame.game.event.ContainsEvent;
import lifegame.game.object.Player;


public class OnlyDistanceSearchThread extends SearchThread{
	private Player player;

	//start
	public OnlyDistanceSearchThread(Player player) {
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
		OnlyDistanceSearchThread.initSearchTime();
	}

	//start
	public OnlyDistanceSearchThread(Player player,int searchTime) {
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
		OnlyDistanceSearchThread.initSearchTime(searchTime);
	}

	//continue
	public OnlyDistanceSearchThread(OnlyDistanceSearchThread original) {
		threadCopy(original);
	}

	protected void threadCopy(OnlyDistanceSearchThread original) {
		super.setCount(original.count);
		super.setStart(original.start);
		this.player=original.player;
		super.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(OnlyDistanceSearchThread.lock3) {
			if(ContainsEvent.goalDistance(this.player, super.count)==1) {
				this.player.setGoalDistance(super.count);
			}
		}
	}
}
