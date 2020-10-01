package lifegame.game.event;

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.main.App;
import lifegame.game.object.Binbo;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Property;

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
	public static void start() {
		String action = randomBinboEvent();
		//String action = binboCardLost();//debug
		if(action=="変身") {
			Binbo.clearTurnCount();
			FrameEvent.openBinbo();//Binbo.getBinboPlayer().getName(),Binbo.getName(),"に変化した",Binbo.getName());
		}else {
			//String pre[] = action.split(",");
			FrameEvent.openBinbo();//Binbo.getBinboPlayer().getName(),pre[0],pre[1],Binbo.getName());
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
		//Searcher.searchShortestRouteAllPlayers();//うまく動くときと動かないときがある。
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
		if(nextbonbylist.size()>1) {
			nextbonbyplayer = nextbonbylist.get(rand.nextInt(nextbonbylist.size()-1));//同じ距離にいた場合ランダム
		}else {
			nextbonbyplayer = nextbonbylist.get(0);
		}
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
		if(!ContainsEvent.binboNameNormal()) {
			if(ContainsEvent.binboNameBaby()) {
				result += Binbo.getTurnCount();
				if(result<8) {
					//"お金とる";
					event= babyMoney();
				}else {
					makeOver();
					event= "変身";
				}
			}else if(ContainsEvent.binboNameHappy()){
				result += Binbo.getTurnCount();
				if(result<8) {
					//"お金もらう";
					event=happyMoney();
				}else {
					makeOver();
					event= "変身";
				}
			}else if(ContainsEvent.binboNameTyphoon()){
				result += Binbo.getTurnCount();
				if(result<9) {
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
				//入らない
				event = "まだなにするかきめてないにょろ~~,案があればほしいにょろ~~。「"+result+"」";
			}
			Binbo.addTurnCount();
		}else {
			int changeresult = result + Binbo.getTurnCount();
			if(changeresult<10) {
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
					event=binboCardLost();
				}else if(result==4) {
					//"プレイヤー移動系";
					event= binboMovePlayer();
				}else if(result == 5){
					event = "まだなにするかきめてないにょろ~~,案があればほしいにょろ~~。「"+result+"」";
				}else if(result == 6){
					event = "まだなにするかきめてないにょろ~~,案があればほしいにょろ~~。「"+result+"」";
				}else if(result == 7){
					event = "まだなにするかきめてないにょろ~~,案があればほしいにょろ~~。「"+result+"」";
				}else {
					event = "まだなにするかきめてないにょろ~~,案があればほしいにょろ~~。「"+result+"」";
				}
			}else {
				makeOver();
				event= "変身";
			}
			Binbo.addTurnCount();
		}
		//event = binboCardbuy();
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
				return "二倍のお金で"+cardname+"をかってきたのねん"+","+Player.player.getName()+"は金額:"+card.getBuyPrice()*2+"万円を支払った。";
			}
		}
		return "予期しない,リターン";

	}
	public static String binboProperty() {
		if(ContainsEvent.propertySize()) {
			Random rand = new Random();
			ArrayList<Property> playersproperty = Player.player.getPropertys();
			int result = rand.nextInt(playersproperty.size()-1);
			Property property = playersproperty.get(result);
			SaleEvent.sellPropertys(property);
			return "お金にこまってそうなのねん。だから物件売ってきたのねん,"+Player.player.getName()+"の"+property+"が買った半分の"+property.getAmount()/2+"の値段で売られてしまった。";
		}else {
			return "お金にこまってそうなのねん。だから物件売ろうと思うのねん。でも売れる物件ないにょろ～,"+Player.player.getName()+"は物件を売られなくて済んだ。";
		}
	}
	public static String binboDice() {
		return "さいころふるゲームをじっそうしたいにょろ。,でもまだ実装できてないにょろ~~";
	}
	public static String binboCardLost() {
		if(ContainsEvent.isHaveCard()) {
			Random rand = new Random();
			int result=rand.nextInt(Player.player.getCardSize()-1);
			Player.player.removeCard(Player.player.getCard(result));
			return "やっぱり最強のプレイヤーはカードいらないと思うのねん,"+Player.player.getName()+"の"+Player.player.getCard(result)+"が処分されてしまった。";
		}else {
			return "やっぱり最強のプレイヤーはカードいらないと思うのねん,捨てるカードないにょろ!?もう最強のプレイヤーにょろ!。"+Player.player.getName()+"はカードを捨てられなくて済んだ。";
		}
	}
	public static String binboMovePlayer() {
		return "プレイヤーをどかしたいのねん。,でもまだ実装できてないにょろ~~";
	}
	public static String kingCardbuy() {
		return "キングボンビーでデビルカードたちを呼びたい。,でもまだ実装できてないにょろ~~";
	}
	public static String kingProperty() {
		return "キングボンビーで物件を処分したい。,でもまだ実装できてないにょろ~~";
	}
	public static String kingDice() {
		return "キングボンビーでさいころ回してお金とりたい。,でもまだ実装できてないにょろ~~";
	}
	public static String kingCardSell() {
		return "キングボンビーでカードを処分したい。,でもまだ実装できてないにょろ~~";
	}
	public static String kingMovePlayer() {
		return "キングボンビーでプレイヤーをどかしたい。,でもまだ実装できてないにょろ~~";
	}
	public static String babyMoney() {
		Random rand = new Random();
		int result=0;
		result = rand.nextInt(25);
		result += result+(App.year/10)+50;
		Player.player.addMoney(-result);
		return "お小遣いほちいのねん"+","+Player.player.getName()+"は金額:"+result+"万円を支払った。";

	}
	public static String happyMoney() {
		Random rand = new Random();
		int result=0;
		while(result<500) {
			result =rand.nextInt(2000);
		}
		result += result*(App.year/10);
		result -= result%100;
		System.out.println(result);
		Player.player.addMoney(result);
		return "貧乏なあなたにさしあげましょう"+","+Player.player.getName()+"は金額:"+result+"万円をもらった。";

	}
	public static String typhoon() {
		if(ContainsEvent.propertySize()) {
			Random rand = new Random();
			ArrayList<Property> playersproperty = Player.player.getPropertys();
			String keepsellproperty = "";
			int result = rand.nextInt(playersproperty.size()-1);
			int i = 0;
			if(result<2) {
				i = 0;
				if(playersproperty.size()<2) {
					result = playersproperty.size()-1;
				}
			}else {
				i = result-2;
			}

			for(;i<result;i++) {
				System.out.println(i);
				Property property = playersproperty.get(i);
				SaleEvent.lostPropertys(property);
				keepsellproperty = keepsellproperty +property.getName()+ ":";
				System.out.println(property.getName());
			}
			System.out.println(keepsellproperty);
			return "タイフーンで吹っ飛ばす。"+Player.player.getName()+"の物件が吹っ飛ばされた。,"+keepsellproperty+"が吹っ飛ばされた。";
		}else {
			return "吹っ飛ばす物件がなかった。,助かった~~~。";
		}
	}
}
