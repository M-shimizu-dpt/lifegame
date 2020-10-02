package lifegame.game.event;

import lifegame.game.main.App;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.animation.DiceAnimationThread;

public abstract class DiceEvent {
	//サイコロ操作
	public static void shuffleDice() {
		DiceAnimationThread.end();
		Player.player.setMove(Dice.shuffle(Player.player));
		Searcher.searchCanMoveMass(Player.player);
		if(Player.player.getMove()==0) {//DiceFrameかもっと上位のレベルで書くべき？
			App.turnEnd();
			//MassEvent.massEvent(FrameEvent.getNowMassName());
		}else {
			MoveEvent.addTrajectory(FrameEvent.getNowMassName());
			if(Player.player.isPlayer()) {
				FrameEvent.printMoveButton();
			}
		}
		FrameEvent.closeMenu();
		Dice.clear();
		CardEvent.resetUsedCard();
	}
}
