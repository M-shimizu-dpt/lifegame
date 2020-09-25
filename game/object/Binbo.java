/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */

package lifegame.game.object;
import java.util.ArrayList;

public abstract class Binbo{
	private static boolean bonbyTurnEndFlag = false;//ボンビー終了フラグ
	private static String name;
	private static Player binboplayer;
	private static ArrayList<Player> together = new ArrayList<Player>();
	private static ArrayList<Player> before = new ArrayList<Player>();
	//private ArrayList<Coordinates> allplayernowMass = new ArrayList<Coordinates>();//将来的に全員にbufさせるために全員の位置を取得する

	//動いている人が進んだマスにだれがいるかを保持するリスト(進んでいる人以外)
	public static void addSameMossPlayer() {
		int turn = Player.player.getID();
		while(true) {
			turn++;
			if(turn==Player.players.size()) {
				turn=0;
			}
			if(turn==Player.player.getID()) {
				break;
			}
			if(binboplayer.getNowMass().contains(Player.players.get(turn).getNowMass())){
				together.add(Player.players.get(turn));
			}
		}
	}

	//binboターン終了
	public static void binboFinish() {
		Binbo.bonbyTurnEndFlag=true;
	}

	//ボンビーが前回憑いていた人を初期化
	public static void clearBonbyBefore() {
		before.clear();
	}

	//ボンビーが前回憑いていた人を直近のプレイヤーをListから削除
	public static void clearBonbyLastBefore() {
		before.remove(before.size()-1);
	}

	//ボンビーの名前取得メソッド
	public static String getName() {
		return name;
	}

	//ボンビーの名前変更メソッド
	public static void setName(String nextname) {
		name = nextname;
	}

	//ボンビーが憑くプレイヤーを取得
	public static Player getBinboPlayer() {
		return binboplayer;
	}

	//だれと一緒にいるかListを取得
	public static ArrayList<Player> getSameMossPlayers() {
		return together;
	}

	//だれと一緒にいるかListの一番最初を返す
	public static Player getSameMossPlayer() {
		return together.get(0);
	}

	//だれと一緒にいるかListがあるかどうかTF
	public static boolean isSameMossPlayers() {
		return together!=null;
	}

	//binboplayerがいるかどうかTF
	public static boolean isBinboPlayer() {
		return binboplayer!=null;
	}

	//ボンビーが前回憑いていた人を取得
	public static ArrayList<Player> getBonbyBefore() {
		return before;
	}

	//ボンビーが前回憑いていた人がいるかどうか取得
	public static boolean isBonbyBefore() {
		return before!=null;
	}

	//ボンビーが前回憑いていた人を直近でだれからもらったか取得
	public static Player getBonbyLastBefore() {
		return before.get(before.size()-1);
	}

	//ボンビーが憑くプレイヤーをセット
	public static void setPlayerBinbo(Player player) {
		binboplayer = player;
	}

	//だれと一緒にいるかListをclear
	public static void sameMossPlayersClear() {
		together.clear();
	}

	//ボンビーが前回憑いていた人を記憶
	public static void setBonbyBefore(Player beforeplayer) {
		before.add(beforeplayer);
	}

	//binboフラグ初期化
	public static void initBinboFlag() {
		Binbo.bonbyTurnEndFlag=false;
	}

	//bonbyTurnEndFlagを返す
	public static boolean isBinboTurn() {
		return Binbo.bonbyTurnEndFlag;
	}

/*将来必要
 	//全てのプレイヤーの現在地set
	public static void setAllPlayersNowMass() {
		for(int player=0;player<Player.players.size();player++) {
				allplayernowMass.add(Player.players.get(player).getNowMass());
		}
	}

	//全てのプレイヤーの現在地取得
	public static ArrayList<Coordinates> getAllPlayersNowMass() {
		return	allplayernowMass;
	}

	//全てのプレイヤーの現在地clear
	public static void clearAllPlayersNowMass() {
		allplayernowMass.clear();
	}
*/
}
