/*
 * 画面表示に関する処理を管理するクラス
 * 画面表示をする際にこのクラスのメソッドを使用
*/
package lifegame.game.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.frames.BinboFrame;
import lifegame.game.object.map.print.frames.ConfirmationFrame;
import lifegame.game.object.map.print.frames.DiceFrame;
import lifegame.game.object.map.print.frames.GoalFrame;
import lifegame.game.object.map.print.frames.InfoFrame;
import lifegame.game.object.map.print.frames.RandomFrame;
import lifegame.game.object.map.print.frames.StartFrame;
import lifegame.game.object.map.print.frames.Title;
import lifegame.game.object.map.print.frames.card.CardFrame;
import lifegame.game.object.map.print.frames.card.DubbingFrame;
import lifegame.game.object.map.print.frames.card.FullCardFrame;
import lifegame.game.object.map.print.frames.card.ShopFrame;
import lifegame.game.object.map.print.frames.card.ShopFrontFrame;
import lifegame.game.object.map.print.frames.closing.AssetsFrame;
import lifegame.game.object.map.print.frames.closing.RevenueFrame;
import lifegame.game.object.map.print.frames.map.AllGingaMapFrame;
import lifegame.game.object.map.print.frames.map.AllJapanMapFrame;
import lifegame.game.object.map.print.frames.map.GingaFrame;
import lifegame.game.object.map.print.frames.map.MiniGingaMapFrame;
import lifegame.game.object.map.print.frames.map.MiniJapanMapFrame;
import lifegame.game.object.map.print.frames.map.PlayFrame;
import lifegame.game.object.map.print.frames.property.BuyPropertyFrame;
import lifegame.game.object.map.print.frames.property.SellPropertyFrame;
import lifegame.game.object.map.print.frames.setting.settingPlayer;
import lifegame.game.object.map.print.frames.setting.settingYear;

//FrameEventにする。
public abstract class FrameEvent{
	private static AllJapanMapFrame allJapanMap = new AllJapanMapFrame();
	private static AllGingaMapFrame allGingaMap = new AllGingaMapFrame();
	private static AssetsFrame assets = new AssetsFrame();
	private static BinboFrame binbo = new BinboFrame();
	private static CardFrame card = new CardFrame();
	private static ConfirmationFrame confirmation = new ConfirmationFrame();
	private static DiceFrame dice = new DiceFrame();//サイコロ用フレーム
	private static DubbingFrame dubbing = new DubbingFrame();
	private static FullCardFrame cardFull = new FullCardFrame();
	private static GoalFrame goal = new GoalFrame();
	private static InfoFrame info = new InfoFrame();
	private static MiniJapanMapFrame miniJapanMap = new MiniJapanMapFrame();
	private static MiniGingaMapFrame miniGingaMap = new MiniGingaMapFrame();
	private static PlayFrame play = new PlayFrame();//メインフレーム
	private static BuyPropertyFrame property = new BuyPropertyFrame();
	private static RandomFrame random = new RandomFrame();
	private static RevenueFrame revenue = new RevenueFrame();
	private static SellPropertyFrame sellStation = new SellPropertyFrame();
	private static ShopFrame shop = new ShopFrame();
	private static ShopFrontFrame shopFront = new ShopFrontFrame();
	private static StartFrame start = new StartFrame();
	private static GingaFrame ginga = new GingaFrame();
	private static Title title = new Title();
	private static settingPlayer settingPlayer = new settingPlayer();
	private static settingYear settingYear = new settingYear();

	public static void openClosing() {
		FrameEvent.closeMain();
		confirmation.open("決算","決算",100,3000);
		ClosingEvent.closing();
		try {//スムーズに決算処理に移れない可能性がある為、書き変える必要がある
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void openRevenue() {
		revenue.open();
	}

	public static void closeRevenue() {
		revenue.close();
		assets.open();
	}

	public static void closeAssets() {
		assets.close();
		FrameEvent.openMain();//すぐに非表示にする為、必要ない。
	}

	public static void createPopUp(String title,String article) {
		confirmation.open(title,article);
	}

	public static void createPopUp(String title,String article,int time) {
		confirmation.open(title, article, time);
	}

	public static void createPopUp(String title,String article,int size,int time) {
		confirmation.open(title, article, size, time);
	}

	public static void closePopUp() {
		confirmation.close();
	}

	public static void openMiniMap() {
		FrameEvent.closeMain();
		if(ContainsEvent.isNormalMap()) {
			miniJapanMap.open();
		}else if(ContainsEvent.isGingaMap()) {
			miniGingaMap.open();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void closeMiniMap() {
		if(ContainsEvent.isNormalMap()) {
			miniJapanMap.close();
		}else if(ContainsEvent.isGingaMap()) {
			miniGingaMap.close();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
		FrameEvent.openMain();
	}

	public static void openAllMap() {
		FrameEvent.closeMain();
		if(ContainsEvent.isNormalMap()) {
			allJapanMap.open();
		}else if(ContainsEvent.isGingaMap()) {
			allGingaMap.open();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void closeAllMap() {
		if(ContainsEvent.isNormalMap()) {
			allJapanMap.close();
		}else if(ContainsEvent.isGingaMap()) {
			allGingaMap.close();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
		FrameEvent.openMain();
	}

	public static void openDice() {
		FrameEvent.closeMain();
		dice.open();
	}

	public static void closeDice() {
		dice.close();
		FrameEvent.openMain();
	}

	public static void openBinbo(String playerName, String action, String binboName) {
		FrameEvent.closeMain();
		binbo.open(playerName,action,binboName);
	}

	public static void closeBinbo() {
		binbo.close();
		//FrameEvent.openMain();
	}

	public static void openGoal() {
		FrameEvent.closeMain();
		goal.open();
	}

	public static void closeGoal() {
		goal.close();
		goal.openNextGoal();
	}

	public static void closeNextGoal() {
		goal.close();
		property.open(Japan.getSaveGoalName(),2);
	}

	public static void openFullCardFromPlay() {
		FrameEvent.closeMain();
		cardFull.open(0);
	}

	public static void closeFullCardFromPlay() {
		cardFull.close();
		FrameEvent.openMain();
	}

	public static void openFullCardFromRandom() {
		random.closeSave();
		cardFull.open(1);
	}

	public static void closeFullCardFromRandom() {
		cardFull.close();
		random.openSave();
	}

	public static void openDubbing() {
		FrameEvent.closeMain();
		dubbing.open();
	}

	public static void closeDubbing() {
		dubbing.close();
		if(new Random().nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			App.turnEnd();
		}
		FrameEvent.openMain();
	}

	public static void openInfo() {
		FrameEvent.closeMain();
		info.open();
	}

	public static void closeInfo() {
		info.close();
		FrameEvent.openMain();
	}

	public static void openCard() {
		FrameEvent.closeMain();
		card.open();
	}

	public static void closeCard() {
		card.close();
		FrameEvent.openMain();
	}

	public static void openSellProperty() {
		FrameEvent.closeMain();
		sellStation.open();
	}

	public static void closeSellProperty() {
		sellStation.close();
		if(new Random().nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			MassEvent.massEventEnd();
		}
		FrameEvent.openMain();
	}

	public static int[] openStartFrame() {
		return start.open();
	}

	public static void openShopFront() {
		ArrayList<Card> cardList = CardEvent.getElectedCard();
		FrameEvent.closeMain();
		shopFront.setCardList(cardList);
		shop.setCardList(cardList);
		shopFront.open();
	}

	public static void closeShopFront() {
		shopFront.close();
		shopFront.clearCardList();
		shop.clearCardList();
		if(new Random().nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			MassEvent.massEventEnd();
		}
		FrameEvent.openMain();
	}

	public static void openBuyShop() {
		shopFront.close();
		shop.open(0);
	}

	public static void openSellShop() {
		shopFront.close();
		shop.open(1);
	}

	public static void closeShop() {
		shop.close();
		shopFront.open();
	}

	//月が替わった時に何月か表示
	public static void openMonthFrame() {//ConfirmationFrameにする
		FrameEvent.closeMain();
		confirmation.open(App.month + "月", App.month + "月",100, 3000);
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		confirmation.close();
	}

	public static void openRandom1(double rand) {
		FrameEvent.closeMain();
		random.open(1,rand);
	}

	public static void openRandom2() {
		if(ContainsEvent.isOwners()) {
			int rndnum = new Random().nextInt(11)+1;
			if(App.month==rndnum) {
				random.open(2,rndnum);
			}
		}
	}

	public static void closeRandom() {
		random.close();
		MassEvent.massEventEnd();
		FrameEvent.openMain();
	}

	public static void closeRandom2() {
		random.close();
	}

	public static void openPropertys(String massName,int id) {
		property.open(massName,id);
		if(id==0) {
			miniJapanMap.close();
		}else if(id==1) {
			allJapanMap.close();
		}else if(id==2) {
			FrameEvent.closeMain();
		}
	}

	public static void closePropertys() {
		property.close();
		if(property.getID()==0) {
			miniJapanMap.open();
		}else if(property.getID()==1) {
			allJapanMap.open();
		}else if(property.getID()==2) {
			if(new Random().nextInt(100) < 3) {
				RandomEvent.randomEvent();
			}else {
				MassEvent.massEventEnd();
				FrameEvent.openMain();
			}
		}
	}

	public static void closeMoveButton() {
		if(ContainsEvent.isNormalMap()) {
			play.closeMoveButton();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.closeMoveButton();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
	}

	public static void printMenu() {
		if(ContainsEvent.isNormalMap()) {
			play.printMenu();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.printMenu();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
	}

	public static void ableMenu() {
		if(ContainsEvent.isNormalMap()) {
			play.ableMenu();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.ableMenu();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
	}

	public static void throwEnd() {
		cardFull.throwEnd();
	}

	public static void printMoveButton() {
		if(ContainsEvent.isNormalMap()) {
			play.printMoveButton();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.printMoveButton();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
	}

	public static String getNowMassName() {
		if(ContainsEvent.isNormalMap()) {
			return play.getNowMassName();
		}else if(ContainsEvent.isGingaMap()) {
			return ginga.getNowMassName();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
		return null;
	}

	public static void closeMenu() {
		if(ContainsEvent.isNormalMap()) {
			play.closeMenu();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.closeMenu();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void setGoalColor() {
		play.setGoalColor();
	}

	public static void resetGoalColor() {
		play.resetGoalColor();
	}

	public static void init(int playerCount){
		play.init(playerCount);
		ginga.init();
	}

	public static void waitButtonUpdate() {
  		if(ContainsEvent.isNormalMap()) {
			play.waitButtonUpdate();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.waitButtonUpdate();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	//銀河から帰ってきた後しっかりとplayFrameの中心に目的地があるか確認する
	public static void moveMaps() {
		if(ContainsEvent.isNormalMap()) {
			play.moveMaps();
			reloadMain();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.moveMaps();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void moveMaps(int x,int y) {
		if(ContainsEvent.isNormalMap()) {
			play.moveMaps(x,y);
		}else if(ContainsEvent.isGingaMap()) {
			ginga.moveMaps(x,y);
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void moveMaps(Player player,Coordinates to) {
		if(ContainsEvent.isNormalMap()) {
			play.moveMaps(player, to);
		}else if(ContainsEvent.isGingaMap()) {
			ginga.moveMaps(player, to);
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void moveMapsEvent() {
		if(ContainsEvent.isNormalMap()) {
			play.moveMapsEvent();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.moveMapsEvent();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void reloadInfo() {
		if(ContainsEvent.isNormalMap()) {
			play.reloadInfo();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.reloadInfo();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	public static void reloadMain() {
		FrameEvent.closeMain();
		FrameEvent.reloadInfo();
		FrameEvent.printMenu();
		FrameEvent.openMain();
	}

	//最終結果表示
	public static void finish() {//ConfirmationFrame
		openClosing();
		String name = ClosingEvent.finish();
		if(!ContainsEvent.isPlayer()) {
			createPopUp("最終結果","優勝は"+name+"です！\nおめでとうございます！",3000);
		}else {
			createPopUp("最終結果","優勝は"+name+"です！\nおめでとうございます！",5000);
		}
	}

	public static boolean isPlayShowing() {
		if(ContainsEvent.isNormalMap()) {
			return play.isShowing();
		}else if(ContainsEvent.isGingaMap()) {
			return ginga.isShowing();
		}else if(ContainsEvent.isBonbirasMap()) {
			//return bonbiras
		}
		return false;
	}

	public static boolean isThrowed() {
		return cardFull.isThrowed();
	}

	public static void initThrowFlag() {
		cardFull.initThrowFlag();
	}

	public static String adjustText(String article){
		List<String> articles = new ArrayList<String>();

		if(article.length()>35) {
			List<String> list = new ArrayList<String>();
			if(article.contains("\n")) {//改行文字毎に改行
				list.addAll(Arrays.asList(article.split("\n")));
			}else {
				list.add(article);
			}
			for(String longart:list) {//改行しても35文字を超える場合は超えたところで改行
				Matcher m = Pattern.compile("[\\s\\S]{1,35}").matcher(longart);
				while (m.find()) {
					articles.add(m.group());
				}
			}
		}else {
			articles.add(article);
		}
		if(articles.size()>13) System.out.println("はみ出ています");
		String artresult="<html><body>";
		for(String art : articles) {
			artresult = artresult + art + "<br />";
		}
		artresult=artresult+"</body></html>";
		return artresult;
	}

	public static int[] openTitle() {
		return title.open();
	}

	public static int openSettingPlayer() {
		return settingPlayer.open();
	}

	public static int getCount() {
		return settingPlayer.count;
	}

	public static String getName(int index) {
		return settingPlayer.getName(index);
	}

	public static int getPlayerOrder(int index) {
		return settingPlayer.getPlayerOrder(index);
	}

	public static int getOrder() {
		return settingPlayer.playerorder;
	}

	public static int openSettingYear() {
		return settingYear.open();
	}

	public static void openMain() {
		if(ContainsEvent.isNormalMap()) {
			play.open();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.open();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras.open();??
		}
	}
	public static void closeMain() {
		if(ContainsEvent.isNormalMap()) {
			play.close();
		}else if(ContainsEvent.isGingaMap()) {
			ginga.close();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras.close();??
		}
	}

	public static void resetMapGoalColor() {
		allJapanMap.resetGoalColor();
		miniJapanMap.resetGoalColor();
	}

	public static void setMapGoalColor() {
		allJapanMap.setGoalColor();
		miniJapanMap.setGoalColor();
	}

	public static void transferMap(int toMapID) {
		/*
		 * フラグ切り替え
		 * 移動元のフレームから消す
		 * 移動先のフレームに追加
		 * 正しい位置に配置
		 */
		if(ContainsEvent.isNormalMap()) {
			if(toMapID==1) {//norm to ginga
				Player.player.setGingaMap();
				play.removePlayer(Player.player);
				ginga.addPlayer(Player.player);
			}else if(toMapID==2) {//norm to bonbiras

			}
		}else if(ContainsEvent.isGingaMap()) {
			if(toMapID==0) {//ginga to norm
				Player.player.setNormalMap();
				ginga.removePlayer(Player.player);
				play.addPlayer(Player.player);
				FrameEvent.moveMaps(Player.player,Japan.getGoalCoor());
			}else if(toMapID==2) {//ginga to bonbiras

			}
		}else if(ContainsEvent.isBonbirasMap()) {
			if(toMapID==0) {//bonbiras to norm

			}else if(toMapID==1) {//bonbiras to ginga

			}
		}
		FrameEvent.moveMaps();
	}

	public static int getYear() {
		return settingYear.year;
	}

}
