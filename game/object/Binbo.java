/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */


package lifegame.game.object;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import lifegame.game.WaitThread;
import lifegame.game.map.print.Window;
import lifegame.game.search.Searcher;

public  class Binbo{
	private String name;
	private Player player;
	private ArrayList<Player> together;
	private ArrayList<Player> before;
	//private Player after;
	//private int playerId;
	private int month = 4;//??

	public Binbo() {
		name = "ボンビー";
		this.together = new ArrayList<Player>();
		this.before = new ArrayList<Player>();
	}


	public int getBmonth() {//??
		return this.month;
	}

	public void binboTurn() {
		//スタブメソッド
		System.out.println(name + "のターン");
		System.out.println(this.player.getName());
		randomBinboEvent();
		System.out.println(name +"のターン終了");
	}
	public void sutabBinboFinishTurn() {
		//スタブメソッド
		System.out.print("ボンビーターン終了");
	}

	public void setBinboPlayer(Player player) {
		this.player = player;
	}

	public Player getBinboPlayer() {
		return this.player;
	}

	public void addSameMossPlayer(Player player) {//だれと一緒にいるか変更
		this.together.add(player);
	}

	public ArrayList<Player> getSameMossPlayers() {//だれと一緒にいるか取得
		return this.together;
	}
	public void sameMossPlayersClear() {//だれと一緒にいるかクリア

		if(this.together!=null) {
			this.together.clear();
		}
	}

	public void setBonbyBefore(Player player) {//ボンビーだれからもらったかか変更
		this.before.add(player);
	}
	public ArrayList<Player> getBonbyBefore() {//ボンビーだれからもらったか取得
		return this.before;
	}
	public Player getBonbyLastBefore() {//ボンビーだれからもらったか直近取得
		if(this.before!=null) {
			return this.before.get(this.before.size()-1);
		}else {
			return null;
		}
	}
	public void clearBonbyLastBefore() {//ボンビーだれからもらったか初期化
		this.before.remove(this.before.size()-1);
	}

	public void clearBonbyBefore() {//ボンビーだれからもらったか初期化
		this.before.clear();
	}

	public void binboPossessPlayer(Window window,Map<Integer,Player> players) {//一番遠い人にボンビーが付く
		ArrayList<Integer> whobonbylist = new ArrayList<Integer>();
		Random rand = new Random();
		int maxdistance = 0;//最長距離比較
		int whobonby = 0;
		Searcher.searchShortestRouteAllPlayers(window,players);
   		WaitThread waitthred  = new WaitThread(11);
		waitthred.start();
		try {
			waitthred.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<players.size();i++) {
			//System.out.println(players.get(i).getGoalDistance()+"最長距離"+i);
			if(players.get(i).containsGoalDistance(maxdistance)) {
				if( ! (players.get(i).equalsGoalDistance(maxdistance))) {
					if(whobonbylist!=null) {
						whobonbylist.clear();
					}
					maxdistance = players.get(i).getGoalDistance();
				}
				whobonbylist.add(i);
			}
		}
		whobonby = whobonbylist.get(rand.nextInt(1000)%whobonbylist.size());//同じ距離にいた場合ランダム
		if(this.getBinboPlayer()!=null) {
			this.player.changeBonby();
		}
		players.get(whobonby).changeBonby();
		this.setBinboPlayer(players.get(whobonby));
	}



public void passingBonby(boolean tf,Map<Integer,Player> players,int turn) {//ながくなったため、分けた(ボンビー擦り付けメソッド)
		if(tf == true) {//残り移動マスが減るとき(進むとき)
			//boolean onceflag = false;//同じマスに複数人存在している際に一度だけしか交換しないように
			Player nullflag = this.getBinboPlayer();
			if(nullflag!=null) {
				this.samePlacePlayer(turn,players);
				if(this.player.containsID(turn)) {//ボンビーと一緒に移動していたら
					if(this.getSameMossPlayers()!=null) {
						for (Player whowith : this.getSameMossPlayers()) {
							this.setBonbyBefore(this.player);
							this.changeBonby(whowith);//ボンビーつく人とplayerのボンビーフラグTFを変えるメソッド
							break;
						}
					}
				}else {
					if(this.player.getNowMass().contains(players.get(turn).getNowMass())) {
						this.setBonbyBefore(this.player);
						this.changeBonby(players.get(turn));
					}
				}
			}
		}else {////残り移動マスが増えるとき(戻るとき)
			if(this.getBonbyBefore().size()!=0) {
				if(this.getSameMossPlayers().size()!=0) {
					if(this.getBonbyLastBefore().containsID(players.get(turn))) {
						this.clearBonbyLastBefore();//リスト一番上消す
						this.changeBonby(players.get(turn));
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
			this.samePlacePlayer(turn,players);
		}
		/*
		for(int i = 0;i<4;i++) {
			//System.out.println(players.get(i).isBonby());
		}
		*/
	}

	private void changeBonby(Player who) {//ボンビー入れ替えメソッド
		this.player.changeBonby();
		this.setBinboPlayer(who);
		this.player.changeBonby();
	}

	private void samePlacePlayer(int turn,Map<Integer,Player> players) {//動いている人が進んだマスにだれがいるかを保持するリスト(進んでいる人以外)
		this.sameMossPlayersClear();
		int i = turn;
		while(true) {
			i++;
			if(i==players.size()) {
				i=0;
			}
			if(i==turn) {
				break;
			}
			if(this.player.getNowMass().contains(players.get(i).getNowMass())){
				this.addSameMossPlayer(players.get(i));
			}
		}
	}







	private void randomBinboEvent() {
		Random rand = new Random();
		int result = rand.nextInt(100);
		if(result<10) {
			Makeover();
		}else {
			Makeover();
		}
	}

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

}
