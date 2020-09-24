package lifegame.game.event;

import java.util.ArrayList;

import lifegame.game.main.App;
import lifegame.game.map.information.Coordinates;
import lifegame.game.object.Player;

public abstract class MoveEvent {
	private static ArrayList<String> moveTrajectory = new ArrayList<String>();//プレイヤーの移動の軌跡

	//移動履歴を保持
	public static void addTrajectory(String component) {
		moveTrajectory.add(component);
	}

	public static void clearTrajectory() {
		moveTrajectory.clear();
	}

	public static void updateTrajectory(String component) {
		//移動先が1つ前と同じか
		if(moveTrajectory.size()>1) {
			if(component.equals(moveTrajectory.get(moveTrajectory.size()-2))) {//同じ場合、1つ前のmoveTrajectoryを削除
				moveTrajectory.remove(moveTrajectory.size()-1);
				Player.player.setMove(Player.player.getMove()+1);
				App.poorgod.passingBackBonby();
			}else {//違う場合、移動した先の座標をmoveTrajectoryに格納
				moveTrajectory.add(component);
				Player.player.setMove(Player.player.getMove()-1);
				App.poorgod.passingGoBonby();
			}
		}else {
			moveTrajectory.add(component);
			Player.player.setMove(Player.player.getMove()-1);
			App.poorgod.passingGoBonby();
		}
	}

	public static void movePlayer(int x,int y) {
		do {
			//移動
			if(x<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()+1,Player.player.getNowMass().getY());
			}else if(x>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()-1,Player.player.getNowMass().getY());
			}
			if(y<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()+1);
			}else if(y>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()-1);
			}
			if(!App.japan.contains(Player.player.getNowMass().getX(),Player.player.getNowMass().getY())) {//2マス開いている場合
				x*=2;
				y*=2;
			}
		}while(!App.japan.contains(Player.player.getNowMass()));
	}

	public static void moveTo(int player,Coordinates to) {
		Player.players.get(player).getNowMass().setValue(to);
	}

}
