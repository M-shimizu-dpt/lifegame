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
	public static void initBinbo(boolean debug) {
		if(debug) {
			Binbo.setName("ボンビー");
			Binbo.setPlayerBinbo(Player.players.get(0));//初期でボンビーを憑けさせるなら
		}else {
			Binbo.setName("ボンビー");
		}
	}
	public static void setDice(int result) {
		Binbo.setdiceresult(result);
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
	public static void clearDiceFlag() {
		Binbo.clearDiceFlag();
	}

	//binboのターンメソッド
	public static void start() {
		System.out.println("binbo");
		int result = randomBinbo();
		if(ContainsEvent.isFrameDiceBinbo(result)) {
			if(Binbo.getBinboPlayer().isPlayer()) {
				randomBinboEvent();
			}else {
				Random rand = new Random();
				int  diceresult = rand.nextInt(100)%6+1;
				addDiceResult(diceresult);
			}
		}else {
			String action = randomBinboEvent(result);
			binboFrameEvent(action);
		}
	}

	public static void binboFrameEvent(String action) {
		if(action=="変身") {
			Binbo.clearTurnCount();
			FrameEvent.openBinbo(Binbo.getName()+"に変化した");
		}else {
			FrameEvent.openBinbo(action);
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

	public static void clearPredecessor() {
		if(ContainsEvent.isBonbyPredecessor()) {
			Binbo.clearBonbyPredecessor();
		}
	}

	public static void addDiceResult(int result) {
		String text = "";
		if(ContainsEvent.binboNameKing()) {
			Player.player.addMoney(-result*10000);
			text = "さいころ振ったたくさんお金もらってやるぞ。"+ result*1000 +"円取られてしまった";
		}else {
			Player.player.addMoney(-result*1000);
			text = "さいころ振った値だけお金もらうねん。" + result*1000 +"円取られてしまった";
		}
		clearDiceFlag();
		binboFrameEvent(text);
	}

	//ボンビー擦り付けメソッド--進んだ際
	public static void passingGoBonby() {
		if(ContainsEvent.isBinboPlayer()) {
			Player binboplayer = Binbo.getBinboPlayer();
			 sameMassPlayerEvent();
			if(ContainsEvent.binboPlayer()) {//ボンビーと一緒に移動していたら
				if(ContainsEvent.isTogether()) {
					Binbo.setBonbyPredecessor(binboplayer);//だれについていたかlist
					BinboEvent.changeBonby(Binbo.getSameMassPlayer());//ボンビーつく人
				}
			}else {
				if(ContainsEvent.coor(binboplayer,Player.player)) {
					Binbo.setBonbyPredecessor(binboplayer);
					BinboEvent.changeBonby(Player.player);
				}
			}
		}
	}

	//ボンビー擦り付けメソッド--戻った際
	public static void passingBackBonby() {
		if(ContainsEvent.isBinboPlayer()){
			if(ContainsEvent.isBonbyPredecessor()) {
				Player bonbylastplayer = Binbo.getBonbyLastPredecessor();
				if(ContainsEvent.isTogether()) {
					if(ContainsEvent.id(bonbylastplayer,Player.player)) {//動いている人が前回のbinbo所持者だったら
						Binbo.clearBonbyLastPredecessor();//リスト一番上消す
						BinboEvent.changeBonby(Player.player);
					}else {//前回binbo所持者が止まっていたら
						if(ContainsEvent.isBonbyLastPredecessor()) {
							Binbo.clearBonbyLastPredecessor();//リスト一番上消す
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
		//ArrayList<Player> gingaplayer = new ArrayList<Player>();//銀河の人リスト
		/*
		for(int i=0;i<Player.players.size();i++) {//銀河にいるひと取得
			if(ContainsEvent.isGingaMap(Player.players.get(i))){
				gingaplayer.add(Player.players.get(i));
			}
		}
		*/
		//if(gingaplayer.isEmpty()) {
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
		nextbonbyplayer = nextbonbylist.get(rand.nextInt(100)%nextbonbylist.size());//同じ距離にいた場合ランダム
			/*
		}else {//銀河鉄道のいるひと
			for(Player player : gingaplayer) {
				System.out.println(player.getGoalDistance()+"最長距離"+player.getName());
				if(ContainsEvent.goalDistance(player,maxdistance)==1) {
					if(ContainsEvent.goalDistance(player,maxdistance)!=0) {
						if(nextbonbylist!=null) {
							nextbonbylist.clear();
						}
						maxdistance = player.getGoalDistance();
					}
					nextbonbylist.add(player.getID());
				}
			}
			nextbonbyplayer = nextbonbylist.get(rand.nextInt(100)%nextbonbylist.size());//同じ距離にいた場合ランダム
		}*/

		Binbo.setPlayerBinbo(Player.players.get(nextbonbyplayer));
	}

	public static int randomBinbo() {
		Random rand = new Random();
		int result = rand.nextInt(8);
		return result;
	}

	//ランダムイベント
	private static String randomBinboEvent(int result) {
		String event;
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
			if(result==0||result==1) {
				//"カード増やす";
				event= kingCardbuy();
			}else if(result==2) {
				//"物件";
				event= kingProperty();
				//}else if(result==3||result==4){
				//	Binboさいころの目だけお金とる
				//(containsEventに記述)仕様のため、最初の条件分岐で
					//ランダムの値で3,4がでた場合はそのEventに行く
			}else if(result==5) {
				//"カードなくす";
				event= kingCardSell();
			}else if(result==6) {
				//"プレイヤー移動系";
				event= kingMovePlayer();
			}else{
				//"ボンビラス";
				event= kingMoveBonbiras();
			}
			if(result + Binbo.getTurnCount()>8) {
				makeOver();
				event= "変身";
			}
		}else {
			int changeresult = result + Binbo.getTurnCount();
			if(changeresult<10) {
				if(result==0||result==1) {
					//"カード増やす";
					event= cardBuy();
				}else if(result==2) {
					// "物件";d
					event=property();
				//}else if(result==3||result==4){
				//	Binboさいころの目だけお金とる
				//(containsEventに記述)仕様のため、最初の条件分岐で
					//ランダムの値で3,4がでた場合はそのEventに行く
				}else if(result==5) {
					//"カードなくす";
					event=cardLost();
				}else{
					//"プレイヤー移動系";
					event= movePlayer();
				}
			}else {
				makeOver();
				event= "変身";
			}
			Binbo.addTurnCount();
		}
		//event = dice();//debug
		return event;
	}
	public static void randomBinboEvent() {
		dice();
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
	public static String cardBuy() {
		String cardname;
		cardname = "徳政令カード";
		for(Card card : Card.getCardList()) {
			if(ContainsEvent.name(card,cardname)) {
				Player.player.addCard(card);
				Player.player.addMoney(-card.getBuyPrice()*2);
				return "二倍のお金で"+cardname+"をかってきたのねん"+","+Player.player.getName()+"は金額:"+FrameEvent.convertMoney(card.getBuyPrice()*2)+"を支払った。";
			}
		}
		return "予期しない,リターン";
	}
	public static String property() {
		if(ContainsEvent.isHaveProperty()) {
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
	public static void dice() {
		Binbo.setDiceFlag();
		FrameEvent.openDiceBinbo();
	}

	public static String cardLost() {
		Card card;
		if(ContainsEvent.isHaveCard()) {
			Random rand = new Random();
			int result=rand.nextInt(100)%Player.player.getCardSize();
			card = Player.player.getCard(result);
			Player.player.removeCard(Player.player.getCard(result));
			return "やっぱり最強のプレイヤーはカードいらないと思うのねん,"+Player.player.getName()+"の"+card.getName()+"が処分されてしまった。";
		}else {
			return "やっぱり最強のプレイヤーはカードいらないと思うのねん,捨てるカードないにょろ!?もう最強のプレイヤーにょろ!。"+Player.player.getName()+"はカードを捨てられなくて済んだ。";
		}
	}



	public static String movePlayer() {
		return "プレイヤーをどかしたいのねん。,でもまだ実装できてないにょろ~~";
	}
	public static String kingCardbuy() {
		return "キングボンビーでデビルカードたちを呼びたい。,でもまだ実装できてないにょろ~~";//(カード自体がないにょろ)
	}
	public static String kingProperty() {
		return "キングボンビーで物件を処分したい。,でもまだ実装できてないにょろ~~";
	}
	public static String kingDice() {
		return "キングボンビーでさいころ回してお金とりたい。,でもまだ実装できてないにょろ~~";
	}




	public static String kingCardSell() {
		if(ContainsEvent.isHaveProperty()) {
			ArrayList<Property> playersproperty = Player.player.getPropertys();
			for(Property property : playersproperty) {
				SaleEvent.lostPropertys(property);
				System.out.print(property.getName());
			}
			return "キ~ングボンビー!!物件なんぞ集めてなにになる！,"+Player.player.getName()+"の全ての物件が処分された。";
		}else {
			return "キ~ングボンビー!!物件なんぞ集めてなにになる,なに!?処分する物件がないだと!?処分されずに済んだ";
		}
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
		return "お小遣いほちいのねん"+","+Player.player.getName()+"は金額:"+FrameEvent.convertMoney(result)+"を支払った。";
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
		return "貧乏なあなたにさしあげましょう"+","+Player.player.getName()+"は金額:"+FrameEvent.convertMoney(result)+"をもらった。";
	}

	public static String typhoon() {
		if(ContainsEvent.isHaveProperty()) {
			Random rand = new Random();
			ArrayList<Property> playersproperty = Player.player.getPropertys();
			String keepsellproperty = "";
			int result = rand.nextInt(100)%playersproperty.size();
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




	public static String kingMoveBonbiras() {
		return  "マップが出来ていないのでまだボンビラス星には飛ばせない";
	}
}
