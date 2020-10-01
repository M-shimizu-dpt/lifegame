/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */

package lifegame.game.object;
import java.util.ArrayList;

public abstract class Binbo{
	private static boolean bonbyTurnEndFlag = false;//ボンビー終了フラグ
	private static boolean bonbyFlag = false;//ボンビーかそうじゃないか
	private static String name;
	private static Player binboplayer;
	private static int turncount = 0;
	private static ArrayList<Player> together = new ArrayList<Player>();
	private static ArrayList<Player> before = new ArrayList<Player>();
	//private ArrayList<Coordinates> allplayernowMass = new ArrayList<Coordinates>();//将来的に全員にbufさせるために全員の位置を取得する

	//動いている人が進んだマスにだれがいるかを保持するリスト(進んでいる人以外)
	public static void addSameMassPlayer(Player player) {
		together.add(player);
	}

	//binboターン終了
	public static void binboFinish() {
		Binbo.bonbyTurnEndFlag=true;
	}
	public static void binboMakeover() {
		if(Binbo.isMakeBinbo()) {
			Binbo.bonbyFlag=false;
		}else{
			Binbo.bonbyFlag=true;
		}
	}

	public static void clearTurnCount() {
		turncount = 0;
	}
	public static void addTurnCount() {
		turncount +=1;
	}
	public static int getTurnCount() {
		return turncount;
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
	public static ArrayList<Player> getSameMassPlayers() {
		return together;
	}

	//だれと一緒にいるかListの一番最初を返す
	public static Player getSameMassPlayer() {
		return together.get(0);
	}

	//ボンビーが前回憑いていた人を取得
	public static ArrayList<Player> getBonbyBefore() {
		return before;
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
	public static void sameMassPlayersClear() {
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

	public static void initBinboMakeFlag() {
		Binbo.bonbyFlag=false;
	}

	//bonbyTurnEndFlagを返す
	public static boolean isMakeBinbo() {
		return Binbo.bonbyFlag;
	}
}
