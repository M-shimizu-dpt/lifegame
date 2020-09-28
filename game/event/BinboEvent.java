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
	public static void sameMassPlayerEvent(){
		if(ContainsEvent.isTogether()) {
			Binbo.sameMassPlayersClear();
		}
		Binbo.addSameMassPlayer();
	}

	//ボンビー入れ替えメソッド
	public static void changeBonby(Player who) {
		Binbo.setPlayerBinbo(who);
		/*
		for(int i = 0;i<4;i++) {//debug
			System.out.println(Player.players.get(i).isBonby());
		}
		*/
	}

	public static void clearBefore() {
		if(ContainsEvent.isBonbyBefore()) {
			Binbo.clearBonbyBefore();
		}
	}



	//ボンビー擦り付けメソッド--進んだ際
	public static void passingGoBonby() {
		if(ContainsEvent.isBinboPlayer()) {
			Player binboplayer = Binbo.getBinboPlayer();
			 sameMassPlayerEvent();
			if(ContainsEvent.binboPlayer()) {//ボンビーと一緒に移動していたら
				if(ContainsEvent.isTogether()) {
					Binbo.setBonbyBefore(binboplayer);//だれについていたかlist
					BinboEvent.changeBonby(Binbo.getSameMassPlayer());//ボンビーつく人
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
		if(ContainsEvent.isBinboPlayer()){
			if(ContainsEvent.isBonbyBefore()) {
				Player bonbylastplayer = Binbo.getBonbyLastBefore();
				if(ContainsEvent.isTogether()) {
					if(ContainsEvent.id(bonbylastplayer,Player.player)) {//動いている人が前回のbinbo所持者だったら
						Binbo.clearBonbyLastBefore();//リスト一番上消す
						BinboEvent.changeBonby(Player.player);
					}else {//前回binbo所持者が止まっていたら
						if(ContainsEvent.isBonbyLastBefore()) {
							Binbo.clearBonbyLastBefore();//リスト一番上消す
							BinboEvent.changeBonby(bonbylastplayer);
						}
					}
				}
			}
			sameMassPlayerEvent();
		}
	}

	//一番遠い人にボンビーが付くメソッド
	public static void binboPossessPlayer() {
		ArrayList<Integer> nextbonbylist = new ArrayList<Integer>();
		Random rand = new Random();
		int maxdistance = 0;//最長距離比較
		int nextbonbyplayer;
		Searcher.searchShortestRouteAllPlayers();//うまく動くときと動かないときがある。
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
		Binbo.setPlayerBinbo(Player.players.get(nextbonbyplayer));
	}

	//ランダムイベント
	private static void randomBinboEvent() {
		Random rand = new Random();
		int result = rand.nextInt(100);
		if(result<10) {//debug用なのだとしたらメモを書いておくように！！！
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
