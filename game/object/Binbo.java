/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */


package lifegame.game.object;

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.event.WaitThread;
import lifegame.game.event.search.Searcher;
import lifegame.game.main.App;
import lifegame.game.map.information.Coordinates;
import lifegame.game.map.print.Window;

public abstract class Binbo{
	private static boolean bonbyTurnEndFlag = false;//ボンビー終了フラグ
	private static String name;
	private static Player binboplayer;
	private static ArrayList<Player> together;
	private static ArrayList<Player> before;
	//private ArrayList<Coordinates> allplayernowMass;//将来的に全員にbufさせるために全員の位置を取得する
	//動いている人が重なったタイミングでメソッド実行されるようにするためのList
	private static ArrayList<Coordinates> stopplayernowMass;

	public static void initBinbo() {
		name = "ボンビー";
		together = new ArrayList<Player>();
		before = new ArrayList<Player>();
		stopplayernowMass = new ArrayList<Coordinates>();
		//allplayernowMass = new ArrayList<Coordinates>();
	}
	public static String getName() {
		return name;
	}

	//binboのターンメソッド
	public static void start(Window window) {

		//スタブメソッド
		//System.out.println(name + "のターン");
		//System.out.println(this.binboplayer.getName());
		window.bonbyPlayer();
		randomBinboEvent();
		//System.out.println(name +"のターン終了");
	}

	//ボンビー終了メソッド
	public static void turnFinish() {
		//スタブメソッド
		//System.out.print("ボンビーターン終了");
	}

	//ボンビーが憑くプレイヤーをセット
	public static void setPlayerBinbo(Player player) {
		binboplayer = player;
	}

	//ボンビーが憑くプレイヤーを取得
	public static Player getBinboPlayer() {
		return binboplayer;
	}

	//ボンビーが憑いているプレイヤーが現在動いているPlayerかを判断
	public static boolean containsBinbo() {
		return binboplayer==Player.player;
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

	//現在動いていないプレイヤーの現在地set
	public static void setStopPlayersNowMass() {
		for(int player=0;player<Player.players.size();player++) {
			if(player!=App.turn) {
				stopplayernowMass.add(Player.players.get(player).getNowMass());
			}
		}
	}

	//現在動いていないプレイヤーの現在地取得
	public static ArrayList<Coordinates> getStopPlayersNowMass() {
		return	stopplayernowMass;
	}

	//現在動いていないプレイヤーの現在地clear
	public static void clearStopPlayersNowMass() {
		stopplayernowMass.clear();
	}

	//現在動いているプレイヤーと動いていないプレイヤーが重なったか判定
	public static boolean stopPlayersNowMassContains() {
		for(Coordinates playernowmass : stopplayernowMass) {
			if(playernowmass.contains(Player.player.getNowMass())) {
				return true;
			}
		}
		return false;
	}

	//だれと一緒にいるかListをset
	public static void addSameMossPlayer(Player samemassplayer) {
		together.add(samemassplayer);
	}

	//だれと一緒にいるかListを取得
	public static ArrayList<Player> getSameMossPlayers() {
		return together;
	}

	//だれと一緒にいるかListをclear
	public static void sameMossPlayersClear() {
		if(together!=null) {
			together.clear();
		}
	}

	//ボンビーが前回憑いていた人を記憶
	public static void setBonbyBefore(Player beforeplayer) {
		before.add(beforeplayer);
	}

	//ボンビーが前回憑いていた人を取得
	public static ArrayList<Player> getBonbyBefore() {
		return before;
	}

	//ボンビーが前回憑いていた人を直近でだれからもらったか取得
	public static Player getBonbyLastBefore() {
		if(before!=null) {
			return before.get(before.size()-1);
		}else {
			return null;
		}
	}

	//ボンビーが前回憑いていた人を直近のプレイヤーをListから削除
	public static void clearBonbyLastBefore() {
		before.remove(before.size()-1);
	}

	//ボンビーが前回憑いていた人を初期化
	public static void clearBonbyBefore() {
		before.clear();
	}

	//一番遠い人にボンビーが付くメソッド
	public static void binboPossessPlayer(Window window) {
		ArrayList<Integer> whobonbylist = new ArrayList<Integer>();
		Random rand = new Random();
		int maxdistance = 0;//最長距離比較
		int whobonby = 0;
		Searcher.searchShortestRouteAllPlayers(window,Player.players);
   		WaitThread waitthred  = new WaitThread(11);
		waitthred.start();
		try {
			waitthred.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<Player.players.size();i++) {
			//System.out.println(players.get(i).getGoalDistance()+"最長距離"+i);
			if(Player.players.get(i).containsGoalDistance(maxdistance)==1) {
				if(Player.players.get(i).containsGoalDistance(maxdistance)!=0) {
					if(whobonbylist!=null) {
						whobonbylist.clear();
					}
					maxdistance = Player.players.get(i).getGoalDistance();
				}
				whobonbylist.add(i);
			}
		}
		whobonby = whobonbylist.get(rand.nextInt(1000)%whobonbylist.size());//同じ距離にいた場合ランダム
		if(getBinboPlayer()!=null) {
			binboplayer.changeBonby();
		}
		Player.players.get(whobonby).changeBonby();
		setPlayerBinbo(Player.players.get(whobonby));
	}

	//ボンビー擦り付けメソッド--進んだ際
	public static void passingGoBonby() {
		Player nullflag = getBinboPlayer();
		if(nullflag!=null) {
			samePlacePlayer();
			if(binboplayer.containsID(App.turn)) {//ボンビーと一緒に移動していたら
				if(getSameMossPlayers()!=null) {
					for (Player whowith : getSameMossPlayers()) {
						setBonbyBefore(binboplayer);//だれについていたかlist
						changeBonby(whowith);//ボンビーつく人
						break;//同じマスに複数人存在している際に一度だけしか交換しないように
					}
				}
			}else {
				if(binboplayer.getNowMass().contains(Player.player.getNowMass())) {
					setBonbyBefore(binboplayer);
					changeBonby(Player.player);
				}
			}
		}
	}

	//ボンビー擦り付けメソッド--戻った際
	public static void passingBackBonby() {
		Player nullflag = getBinboPlayer();
		if(nullflag!=null) {
			if(getBonbyBefore().size()!=0) {
				if(getSameMossPlayers().size()!=0) {
					if(getBonbyLastBefore().containsID(Player.player)) {
						clearBonbyLastBefore();//リスト一番上消す
						changeBonby(Player.player);
					}else {
						for (Player whowith : getSameMossPlayers()) {//動いている人が止まったマスに一緒にいる人一覧
							if(getBonbyLastBefore().containsID(whowith)) {
								clearBonbyLastBefore();//リスト一番上消す
								changeBonby(whowith);
								break;
							}
						}
					}
				}
			}
			samePlacePlayer();
		}
	}

	//ボンビー入れ替えメソッド
	private static void changeBonby(Player who) {
		binboplayer.changeBonby();
		setPlayerBinbo(who);
		binboplayer.changeBonby();
		/*
		for(int i = 0;i<4;i++) {//debug
			System.out.println(Player.players.get(i).isBonby());
		}
		*/
	}

	//動いている人が進んだマスにだれがいるかを保持するリスト(進んでいる人以外)
	private static void samePlacePlayer() {
		sameMossPlayersClear();
		int i = App.turn;
		while(true) {
			i++;
			if(i==Player.players.size()) {
				i=0;
			}
			if(i==App.turn) {
				break;
			}
			if(binboplayer.getNowMass().contains(Player.players.get(i).getNowMass())){
				addSameMossPlayer(Player.players.get(i));
			}
		}
	}

	//ランダムイベント
	private static void randomBinboEvent() {
		Random rand = new Random();
		int result = rand.nextInt(100);
		if(result<10) {
			Makeover();
		}else {
			Makeover();
		}
	}

	//ボンビーが成るメソッド
	private static void Makeover() {
		Random rand = new Random();
		double result = rand.nextDouble();
		if(result<0.3) {
			name = "赤ちゃんボンビー";
		}else if(result<0.5) {
			name = "タイフーンボンビー";
		}else if(result<0.7) {
			name = "幸せボンビー";
		}else{
			name = "キングボンビー";
		}
	}

	public static void initBinboFlag() {
		Binbo.bonbyTurnEndFlag=false;
	}

	public static void binboFinish() {
		Binbo.bonbyTurnEndFlag=true;
	}

	public static boolean isBinboTurn() {
		return Binbo.bonbyTurnEndFlag;
	}

}
