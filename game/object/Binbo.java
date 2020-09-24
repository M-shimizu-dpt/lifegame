/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */


package lifegame.game.object;

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.event.WaitThread;
import lifegame.game.main.App;
import lifegame.game.map.information.Coordinates;
import lifegame.game.map.print.Window;
import lifegame.game.search.Searcher;

public class Binbo{

	private static boolean bonbyTurnEndFlag = false;//ボンビー終了フラグ
	private String name;
	private Player binboplayer;
	private ArrayList<Player> together;
	private ArrayList<Player> before;
	//private ArrayList<Coordinates> allplayernowMass;//将来的に全員にbufさせるために全員の位置を取得する
	//動いている人が重なったタイミングでメソッド実行されるようにするためのList
	private ArrayList<Coordinates> stopplayernowMass;

	public Binbo() {
		name = "ボンビー";
		this.together = new ArrayList<Player>();
		this.before = new ArrayList<Player>();
		this.stopplayernowMass = new ArrayList<Coordinates>();
		//this.allplayernowMass = new ArrayList<Coordinates>();
	}


	//binboのターンメソッド
	public void start(Window window) {

		//スタブメソッド
		System.out.println(name + "のターン");
		System.out.println(this.binboplayer.getName());
		window.bonbyplayer();
		randomBinboEvent();
		System.out.println(name +"のターン終了");
	}

	//ボンビー終了メソッド
	public void finishTurn() {
		//スタブメソッド
		System.out.print("ボンビーターン終了");
	}

	//ボンビーが憑くプレイヤーをセット
	public void setPlayerBinbo(Player player) {
		this.binboplayer = player;
	}

	//ボンビーが憑くプレイヤーを取得
	public Player getBinboPlayer() {
		return this.binboplayer;
	}

	//ボンビーが憑いているプレイヤーが現在動いているPlayerかを判断
	public boolean containsBinbo(Player player) {
		return this.binboplayer==player;
	}

/*将来必要
 	//全てのプレイヤーの現在地set
	public void setAllPlayersNowMass() {
		for(int player=0;player<Player.players.size();player++) {
				this.allplayernowMass.add(Player.players.get(player).getNowMass());
		}
	}

	//全てのプレイヤーの現在地取得
	public ArrayList<Coordinates> getAllPlayersNowMass() {
		return	this.allplayernowMass;
	}

	//全てのプレイヤーの現在地clear
	public void clearAllPlayersNowMass() {
		this.allplayernowMass.clear();
	}
*/

	//現在動いていないプレイヤーの現在地set
	public void setStopPlayersNowMass() {
		for(int player=0;player<Player.players.size();player++) {
			if(player!=App.turn) {
				this.stopplayernowMass.add(Player.players.get(player).getNowMass());
			}
		}
	}

	//現在動いていないプレイヤーの現在地取得
	public ArrayList<Coordinates> getStopPlayersNowMass() {
		return	this.stopplayernowMass;
	}

	//現在動いていないプレイヤーの現在地clear
	public void clearStopPlayersNowMass() {
		this.stopplayernowMass.clear();
	}

	//現在動いているプレイヤーと動いていないプレイヤーが重なったか判定
	public boolean stopPlayersNowMassContains() {
		for(Coordinates playernowmass : this.stopplayernowMass) {
			if(playernowmass.contains(Player.player.getNowMass())) {
				return true;
			}
		}
		return false;
	}

	//だれと一緒にいるかListをset
	public void addSameMossPlayer(Player samemassplayer) {
		this.together.add(samemassplayer);
	}

	//だれと一緒にいるかListを取得
	public ArrayList<Player> getSameMossPlayers() {
		return this.together;
	}

	//だれと一緒にいるかListをclear
	public void sameMossPlayersClear() {
		if(this.together!=null) {
			this.together.clear();
		}
	}

	//ボンビーが前回憑いていた人を記憶
	public void setBonbyBefore(Player beforeplayer) {
		this.before.add(beforeplayer);
	}

	//ボンビーが前回憑いていた人を取得
	public ArrayList<Player> getBonbyBefore() {
		return this.before;
	}

	//ボンビーが前回憑いていた人を直近でだれからもらったか取得
	public Player getBonbyLastBefore() {
		if(this.before!=null) {
			return this.before.get(this.before.size()-1);
		}else {
			return null;
		}
	}

	//ボンビーが前回憑いていた人を直近のプレイヤーをListから削除
	public void clearBonbyLastBefore() {
		this.before.remove(this.before.size()-1);
	}

	//ボンビーが前回憑いていた人を初期化
	public void clearBonbyBefore() {
		this.before.clear();
	}

	//一番遠い人にボンビーが付くメソッド
	public void binboPossessPlayer(Window window) {
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
		if(this.getBinboPlayer()!=null) {
			this.binboplayer.changeBonby();
		}
		Player.players.get(whobonby).changeBonby();
		this.setPlayerBinbo(Player.players.get(whobonby));
	}

	//ボンビー擦り付けメソッド--進んだ際
	public void passingGoBonby() {
		Player nullflag = this.getBinboPlayer();
		if(nullflag!=null) {
			this.samePlacePlayer();
			if(this.binboplayer.containsID(App.turn)) {//ボンビーと一緒に移動していたら
				if(this.getSameMossPlayers()!=null) {
					for (Player whowith : this.getSameMossPlayers()) {
						this.setBonbyBefore(this.binboplayer);//だれについていたかlist
						this.changeBonby(whowith);//ボンビーつく人
						break;//同じマスに複数人存在している際に一度だけしか交換しないように
					}
				}
			}else {
				if(this.binboplayer.getNowMass().contains(Player.player.getNowMass())) {
					this.setBonbyBefore(this.binboplayer);
					this.changeBonby(Player.player);
				}
			}
		}
	}

	//ボンビー擦り付けメソッド--戻った際
	public void passingBackBonby() {//ながくなったため、分けた(ボンビー擦り付けメソッド)
		if(this.getBonbyBefore().size()!=0) {
			if(this.getSameMossPlayers().size()!=0) {
				if(this.getBonbyLastBefore().containsID(Player.player)) {
					this.clearBonbyLastBefore();//リスト一番上消す
					this.changeBonby(Player.player);
				}else {
					for (Player whowith : this.getSameMossPlayers()) {//動いている人が止まったマスに一緒にいる人一覧
						if(this.getBonbyLastBefore().containsID(whowith)) {
							this.clearBonbyLastBefore();//リスト一番上消す
							this.changeBonby(whowith);
							break;
						}
					}
				}
			}
		}
		this.samePlacePlayer();
	}

	//ボンビー入れ替えメソッド
	private void changeBonby(Player who) {
		this.binboplayer.changeBonby();
		this.setPlayerBinbo(who);
		this.binboplayer.changeBonby();
	}

	//動いている人が進んだマスにだれがいるかを保持するリスト(進んでいる人以外)
	private void samePlacePlayer() {
		this.sameMossPlayersClear();
		int i = App.turn;
		while(true) {
			i++;
			if(i==Player.players.size()) {
				i=0;
			}
			if(i==App.turn) {
				break;
			}
			if(this.binboplayer.getNowMass().contains(Player.players.get(i).getNowMass())){
				this.addSameMossPlayer(Player.players.get(i));
			}
		}
	}

	//ランダムイベント
	private void randomBinboEvent() {
		Random rand = new Random();
		int result = rand.nextInt(100);
		if(result<10) {
			Makeover();
		}else {
			Makeover();
		}
	}

	//ボンビーが成るメソッド
	private void Makeover() {
		Random rand = new Random();
		double result = rand.nextDouble();
		if(result<0.3) {
			this.name = "赤ちゃんボンビー";
		}else if(result<0.5) {
			this.name = "タイフーンボンビー";
		}else if(result<0.7) {
			this.name = "幸せボンビー";
		}else{
			this.name = "キングボンビー";
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
