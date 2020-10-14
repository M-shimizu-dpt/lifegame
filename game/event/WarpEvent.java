package lifegame.game.event;

import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;

public abstract class WarpEvent {

	public static void warp(String to) {
		Player.player.addMoney(-Player.player.getMoney());
		FrameEvent.moveMaps(Player.player, Japan.getWarpCoor(to));
		MoveEvent.moveTo(Player.player, Japan.getWarpCoor(to));
		FrameEvent.closeSelectWarp();
		FrameEvent.closeWarp();
	}
}
