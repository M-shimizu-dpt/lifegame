package lifegame.game.event;

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.event.search.Searcher;
import lifegame.game.object.Binbo;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.Window;

public abstract class BinboEvent{
	//binboクラス初期
	public static void initBinbo() {
		Binbo.setName("ボンビー");
		//initBinbo(Player.players.get(0));
	}

	public static void initIsBinbo() {
		Binbo.setName("ボンビー");
		Binbo.setPlayerBinbo(Player.players.get(0));//初期でボンビーを憑けさせるなら
	}

	public static void addSameMassPlayer() {
		int turn = Player.player.getID();
		Player player;
		while(true) {
			turn++;
			if(turn==Player.players.size()) {
				turn=0;
			}
			if(ContainsEvent.id(turn)) {
				break;
			}
			player = Player.players.get(turn);
			if(ContainsEvent.coor(Player.player,player)){
				Binbo.addSameMassPlayer(player);
			}
		}
	}

	//binboのターンメソッド
	public static void start(Window window) {
		String action = randomBinboEvent();
		if(action=="変身") {
			window.bonbyPlayer(Binbo.getBinboPlayer().getName(),Binbo.getName(),"に変化した");
		}else {
			String pre[] = action.split(",");
			window.bonbyPlayer(Binbo.getBinboPlayer().getName(),pre[0],pre[1]);
		}
	}

	//ボンビー終了メソッド
	public static void turnFinish() {
		Binbo.binboFinish();
	}

	//うごいている人と同じマスの人を保存
	public static void sameMassPlayerEvent(){
		if(ContainsEvent.isTogether()) {
			Binbo.sameMassPlayersClear();
		}
		BinboEvent.addSameMassPlayer();
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

	public static int randomBinbo() {
		Random rand = new Random();
		int result = rand.nextInt(8);
		return result;
	}

	//ランダムイベント
	private static String randomBinboEvent() {
		int result = randomBinbo();
		String event;
		if(ContainsEvent.binboNameBaby()) {
			if(result<6) {
				//"お金とる";
				event= babyMoney();
			}else {
				makeOver();
				event= "変身";
			}
		}else if(ContainsEvent.binboNameHappy()){
			if(result<6) {
				//"お金もらう";
				event=happyMoney();
			}else {
				makeOver();
				event= "変身";
			}
		}else if(ContainsEvent.binboNameTyphoon()){
			if(result<6) {
				//"物件飛ばす";
				event= typhoon();
			}else {
				makeOver();
				event= "変身";
			}
		}else if(ContainsEvent.binboNameKing()){
			if(result==0) {
				//"カード増やす";
				event= kingCardbuy();
			}else if(result==1) {
				//"物件";
				event= kingProperty();
			}else if(result==2) {
				//"さいころ降らす";
				event= kingDice();
			}else if(result==3) {
				//"カードなくす";
				event= kingCardSell();
			}else if(result==4) {
				//"プレイヤー移動系";
				event= kingMovePlayer();
			}else{
				makeOver();
				event= "変身";
			}
		}else {
			if(result==0) {
				//"カード増やす";
				event= binboCardbuy();
			}else if(result==1) {
				// "物件";
				event=binboProperty();
			}else if(result==2) {
				//"さいころ降らす";
				event= binboDice();
			}else if(result==3) {
				//"カードなくす";
				event=binboCardSell();
			}else if(result==4) {
				//"プレイヤー移動系";
				event= binboMovePlayer();
			}else{
				makeOver();
				event= "変身";
			}
		}
		event = binboCardbuy();
		return event;
	}

	//ボンビーが成るメソッド
	public static void makeOver() {
		String name;
		if(Binbo.isMakeBinbo()) {
			name = "ボンビー";
		}else {
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
		Binbo.binboMakeover();
		Binbo.setName(name);
	}
	public static String binboCardbuy() {
		String cardname;
		cardname = "徳政令カード";
		for(Card card : Card.getCardList()) {
			if(ContainsEvent.name(card,cardname)) {
				Player.player.addCard(card);
				Player.player.addMoney(-card.getBuyPrice()*2);
				return "二倍のお金で"+cardname+"をかってきたのねん"+","+Player.player.getName()+"は金額:"+card.getBuyPrice()*2+"を支払った。";
			}
		}
		return "予期しないリターン";

	}
	public static String binboProperty() {
		return "";
	}
	public static String binboDice() {
		return "";
	}
	public static String binboCardSell() {
		return "";
	}
	public static String binboMovePlayer() {
		return "";
	}
	public static String kingCardbuy() {
		return "";
	}
	public static String kingProperty() {
		return "";
	}
	public static String kingDice() {
		return "";
	}
	public static String kingCardSell() {
		return "";
	}
	public static String kingMovePlayer() {
		return "";
	}
	public static String babyMoney() {
		return "";
	}
	public static String happyMoney() {
		return "";
	}
	public static String typhoon() {
		return "";
	}
}
