package lifegame.game.event;

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.event.search.Searcher;
import lifegame.game.object.Binbo;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.Window;

public abstract class BinboEvent{
	//binboクラス初期化
	public static void initBinbo() {
		Binbo.setName("ボンビー");
		initBinbo(Player.players.get(0));//初期でボンビーを憑けさせるなら
	}

	private static void initBinbo(Player player) {
		Binbo.setPlayerBinbo(player);//初期でボンビーを憑けさせるなら
		Binbo.getBinboPlayer().changeBonby();//初期でボンビーを憑けさせるなら
	}

	//binboのターンメソッド
	public static void start(Window window) {
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
		Binbo.binboFinish();
	}

	//うごいている人と同じマスの人を保存
	public static void sameMossPlayerEvent(){
		if(Binbo.isSameMossPlayers()) {
			Binbo.sameMossPlayersClear();
		}
		Binbo.addSameMossPlayer();
	}

	//ボンビー入れ替えメソッド
	public static void changeBonby(Player who) {
		Binbo.getBinboPlayer().changeBonby();
		Binbo.setPlayerBinbo(who);
		Binbo.getBinboPlayer().changeBonby();
		/*
		for(int i = 0;i<4;i++) {//debug
			System.out.println(Player.players.get(i).isBonby());
		}
		*/
	}

	public static void clearBefore() {
		if(Binbo.isBonbyBefore()) {
			Binbo.clearBonbyBefore();
		}
	}

	//現在動いているプレイヤーと動いていないプレイヤーが重なったか判定
	public static boolean stopPlayersNowMassContains() {
		for(int stopplayer = 0;stopplayer<Player.players.size();stopplayer++) {
			if(Player.player.getID()!=stopplayer) {
				if(ContainsEvent.coor(Player.player,Player.players.get(stopplayer))) {
					return true;
				}
			}
		}
		return false;
	}


	//動いている人の前回止まっていたマスにボンビーが前回憑いていた人が存在するかTF
	public static boolean isBonbyLastBefore() {
		for (Player whowith : Binbo.getSameMossPlayers()) {//止まったマスに一緒にいる人一覧
			if(ContainsEvent.id(Binbo.getBonbyLastBefore(),whowith)) {
				return true;
			}
		}
		return false;
	}

	//ボンビー擦り付けメソッド--進んだ際
	public static void passingGoBonby() {
		if(Binbo.isBinboPlayer()) {
			Player binboplayer = Binbo.getBinboPlayer();
			 sameMossPlayerEvent();
			if(ContainsEvent.binboPlayer()) {//ボンビーと一緒に移動していたら
				if(Binbo.isSameMossPlayers()) {
					Binbo.setBonbyBefore(binboplayer);//だれについていたかlist
					BinboEvent.changeBonby(Binbo.getSameMossPlayer());//ボンビーつく人
				}
			}else {
				if(ContainsEvent.coor(binboplayer,Player.player)) {
					Binbo.setBonbyBefore(binboplayer);
					BinboEvent.changeBonby(Player.player);
				}
			}
		}
	}

	//ボンビー擦り付けメソッド--戻った際
	public static void passingBackBonby() {
		if(Binbo.isBinboPlayer()){
			if(Binbo.isBonbyBefore()) {
				Player bonbylastplayer = Binbo.getBonbyLastBefore();
				if(Binbo.isSameMossPlayers()) {
					if(ContainsEvent.id(bonbylastplayer,Player.player)) {//動いている人が前回のbinbo所持者だったら
						Binbo.clearBonbyLastBefore();//リスト一番上消す
						BinboEvent.changeBonby(Player.player);
					}else {//前回binbo所持者が止まっていたら
						if(BinboEvent.isBonbyLastBefore()) {
							Binbo.clearBonbyLastBefore();//リスト一番上消す
							BinboEvent.changeBonby(bonbylastplayer);
						}
					}
				}
			}
			sameMossPlayerEvent();
		}
	}

	//一番遠い人にボンビーが付くメソッド
	public static void binboPossessPlayer(Window window) {
		ArrayList<Integer> nextbonbylist = new ArrayList<Integer>();
		Random rand = new Random();
		int maxdistance = 0;//最長距離比較
		int nextbonbyplayer;
		Searcher.searchShortestRouteAllPlayers(window,Player.players);//うまく動くときと動かないときがある。
   		WaitThread waitthred  = new WaitThread(11);
		waitthred.start();
		try {
			waitthred.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<Player.players.size();i++) {
			System.out.println(Player.players.get(i).getGoalDistance()+"最長距離"+i);
			if(ContainsEvent.goalDistance(Player.players.get(i),maxdistance)==1) {
				if(ContainsEvent.goalDistance(Player.players.get(i),maxdistance)!=0) {
					if(nextbonbylist!=null) {
						nextbonbylist.clear();
					}
					maxdistance = Player.players.get(i).getGoalDistance();
				}
				nextbonbylist.add(i);
			}
		}
		nextbonbyplayer = nextbonbylist.get(rand.nextInt(1000)%nextbonbylist.size());//同じ距離にいた場合ランダム
		if(Binbo.isBinboPlayer()) {
			Binbo.getBinboPlayer().changeBonby();
		}
		Player.players.get(nextbonbyplayer).changeBonby();
		Binbo.setPlayerBinbo(Player.players.get(nextbonbyplayer));
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
		String name;
		if(result<0.3) {
			name = "赤ちゃんボンビー";
		}else if(result<0.5) {
			name = "タイフーンボンビー";
		}else if(result<0.7) {
			name = "幸せボンビー";
		}else{
			name = "キングボンビー";
		}
		Binbo.setName(name);
	}

}
