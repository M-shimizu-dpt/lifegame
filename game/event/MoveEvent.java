package lifegame.game.event;

import java.util.ArrayList;

import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;

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
			if(ContainsEvent.name(component,moveTrajectory.get(moveTrajectory.size()-2))) {//同じ場合、1つ前のmoveTrajectoryを削除
				moveTrajectory.remove(moveTrajectory.size()-1);
				Player.player.setMove(Player.player.getMove()+1);
				BinboEvent.passingBackBonby();
			}else {//違う場合、移動した先の座標をmoveTrajectoryに格納
				moveTrajectory.add(component);
				Player.player.setMove(Player.player.getMove()-1);
				if(	ContainsEvent.stopPlayersNowMass()){
					BinboEvent.passingGoBonby();
				}
			}
		}else {
			moveTrajectory.add(component);
			Player.player.setMove(Player.player.getMove()-1);
			if(	ContainsEvent.stopPlayersNowMass()){
				BinboEvent.passingGoBonby();
			}
		}
	}

	public static Coordinates movePlayerInJapan(int x,int y) {
		Coordinates coor = new Coordinates(x,y);
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
			if(!ContainsEvent.isMassInJapan(Player.player.getNowMass())) {//2マス開いている場合
				coor.setValue(coor.getX()*2, coor.getY()*2);
			}
		}while(!ContainsEvent.isMassInJapan(Player.player.getNowMass()));
		return coor;
	}
	public static Coordinates movePlayerInJapan(Coordinates coordinates) {
		Coordinates coor = new Coordinates(coordinates);
		do {
			//移動
			if(coordinates.getX()<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()+1,Player.player.getNowMass().getY());
			}else if(coordinates.getX()>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()-1,Player.player.getNowMass().getY());
			}
			if(coordinates.getY()<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()+1);
			}else if(coordinates.getY()>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()-1);
			}
			if(!ContainsEvent.isMassInJapan(Player.player.getNowMass())) {//2マス開いている場合
				coor.setValue(coor.getX()*2, coor.getY()*2);
			}
		}while(!ContainsEvent.isMassInJapan(Player.player.getNowMass()));
		return coor;
	}
	public static Coordinates movePlayerInGinga(int x,int y) {
		Coordinates coor = new Coordinates(x,y);
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
			if(!ContainsEvent.isMassInGinga(Player.player.getNowMass())) {//2マス開いている場合
				coor.setValue(coor.getX()*2, coor.getY()*2);
			}
		}while(!ContainsEvent.isMassInGinga(Player.player.getNowMass()));
		return coor;
	}
	public static Coordinates movePlayerInGinga(Coordinates coordinates) {
		Coordinates coor = new Coordinates(coordinates);
		do {
			//移動
			if(coordinates.getX()<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()+1,Player.player.getNowMass().getY());
			}else if(coordinates.getX()>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX()-1,Player.player.getNowMass().getY());
			}
			if(coordinates.getY()<0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()+1);
			}else if(coordinates.getY()>0) {
				Player.player.getNowMass().setValue(Player.player.getNowMass().getX(),Player.player.getNowMass().getY()-1);
			}
			if(!ContainsEvent.isMassInGinga(Player.player.getNowMass())) {//2マス開いている場合
				coor.setValue(coor.getX()*2, coor.getY()*2);
			}
		}while(!ContainsEvent.isMassInGinga(Player.player.getNowMass()));
		return coor;
	}

	public static void moveTo(Player player,Coordinates to) {
		player.getNowMass().setValue(to);
	}

}
