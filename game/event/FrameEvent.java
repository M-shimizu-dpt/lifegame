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
import lifegame.game.object.map.print.frames.card.CardFrame;
import lifegame.game.object.map.print.frames.card.DubbingFrame;
import lifegame.game.object.map.print.frames.card.ErrorFrame;
import lifegame.game.object.map.print.frames.card.ShopFrame;
import lifegame.game.object.map.print.frames.card.ShopFrontFrame;
import lifegame.game.object.map.print.frames.closing.AssetsFrame;
import lifegame.game.object.map.print.frames.closing.RevenueFrame;
import lifegame.game.object.map.print.frames.map.AllMapFrame;
import lifegame.game.object.map.print.frames.map.MiniMapFrame;
import lifegame.game.object.map.print.frames.map.PlayFrame;
import lifegame.game.object.map.print.frames.property.BuyPropertyFrame;
import lifegame.game.object.map.print.frames.property.SellPropertyFrame;

//FrameEventにする。
public abstract class FrameEvent{
	private static AllMapFrame allMap = new AllMapFrame();
	private static AssetsFrame assets = new AssetsFrame();
	private static BinboFrame binbo = new BinboFrame();
	private static CardFrame card = new CardFrame();
	private static ConfirmationFrame confirmation = new ConfirmationFrame();
	private static DiceFrame dice = new DiceFrame();//サイコロ用フレーム
	private static DubbingFrame dubbing = new DubbingFrame();
	private static ErrorFrame error = new ErrorFrame();
	private static GoalFrame goal = new GoalFrame();
	private static InfoFrame info = new InfoFrame();
	private static MiniMapFrame miniMap = new MiniMapFrame();
	private static PlayFrame play = new PlayFrame();//メインフレーム
	private static BuyPropertyFrame property = new BuyPropertyFrame();
	private static RandomFrame random = new RandomFrame();
	private static RevenueFrame revenue = new RevenueFrame();
	private static SellPropertyFrame sellStation = new SellPropertyFrame();
	private static ShopFrame shop = new ShopFrame();
	private static ShopFrontFrame shopFront = new ShopFrontFrame();
	private static StartFrame start = new StartFrame();

	public static void openClosing() {
		play.close("closing");
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
		play.open();//すぐに非表示にする為、必要ない。
	}

	public static void createPopUp(String title,String article) {
		confirmation.open(title,article);
	}

	public static void createPopUp(String title,String article,int time) {
		confirmation.open(title, article, time);
	}

	public static void closePopUp() {
		confirmation.close();
		play.open();
	}

	public static void setGoalColor() {
		play.setGoalColor();
	}

	public static void resetGoalColor() {
		play.resetGoalColor();
	}

	public static void init(int playerCount){
		play.init(playerCount);
        play.open();
	}

	public static void openMiniMap() {
		play.close("minimap");
		miniMap.open();
	}

	public static void closeMiniMap() {
		miniMap.close();
		play.open();
	}

	public static void openAllMap() {
		play.close("allMap");
		allMap.open();
	}

	public static void closeAllMap() {
		allMap.close();
		play.open();
	}

	public static void openDice() {
		play.close("dice");
		dice.open();
	}

	public static void closeDice() {
		dice.close();
		play.open();
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

	public static void openBinbo(String playerName, String action, String binboName) {
		play.close("binbo");
		binbo.open(playerName,action,binboName);
	}

	public static void closeBinbo() {
		binbo.close();
		play.open();
	}

	public static void openGoal() {
		play.close("goal");
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

	public static void openError() {//errorをcardFullに改名
		play.close("error");
		error.open();
	}

	public static void closeError() {
		error.close();
		play.open();
	}

	public static boolean isThrowed() {
		return error.isThrowed();
	}

	public static void initThrowFlag() {
		error.initThrowFlag();
	}

	public static void openDubbing() {
		play.close("dubbing");
		dubbing.open();
	}

	public static void closeDubbing() {
		dubbing.close();
		if(new Random().nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			App.turnEnd();
		}
		play.open();
	}

	public static void openInfo() {
		play.close("info");
		info.open();
	}

	public static void closeInfo() {
		info.close();
		play.open();
	}

	public static void openCard() {
		play.close("card");
		card.open();
	}

	public static void closeCard() {
		card.close();
		play.open();
	}

	public static void openSellProperty() {
		play.close("sell property");
		sellStation.open();
	}

	public static void closeSellProperty() {
		sellStation.close();
		if(new Random().nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			App.turnEnd();
		}
		play.open();
	}

	public static void waitButtonUpdate() {
  		play.waitButtonUpdate();
	}

	public static void moveMaps() {
		play.moveMaps();
	}

	public static void moveMaps(int x,int y) {
		play.moveMaps(x,y);
	}

	public static void moveMaps(Player player,Coordinates to) {
		play.moveMaps(player, to);
	}


	public static void reloadInfo() {
		play.reloadInfo();
	}

	public static void printMenu() {
		play.printMenu();
	}

	public static int[] openStartFrame() {
		return start.open();
	}

	public static void openShopFront() {
		ArrayList<Card> cardList = CardEvent.getElectedCard();
		play.close("shop");
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
			App.turnEnd();
		}
		play.open();
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
	public static void printMonthFrame() {//ConfirmationFrameにする
		play.close("month");
		confirmation.open(App.month + "月", App.month + "月",100, 3000);
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		confirmation.close();
		play.open();

	}

	public static void openRandom1(double rand) {
		play.close("random");
		random.open(1,rand);
	}

	public static void openRandom2() {
		if(ContainsEvent.isOwners()) {
			int rndnum = new Random().nextInt(11)+1;
			if(App.month==rndnum) {
				play.close("random2");
				random.open(2,rndnum);
			}
		}
	}

	public static void closeRandom() {
		random.close();
		App.turnEnd();
		play.open();
	}
	public static void closeRandom2() {
		random.close();
		play.open();
	}

	public static void printPropertys(String massName,int id) {
		property.open(massName,id);
		if(id==0) {
			miniMap.close();
		}else if(id==1) {
			allMap.close();
		}else if(id==2) {
			play.close("station");
		}
	}

	public static void closePropertys() {
		property.close();
		if(property.getID()==0) {
			miniMap.open();
		}else if(property.getID()==1) {
			allMap.open();
		}else if(property.getID()==2) {
			if(new Random().nextInt(100) < 3) {
				RandomEvent.randomEvent();
			}else {
				App.turnEnd();
			}
			play.open();
		}
	}

	public static void closeMoveButton() {
		play.closeMoveButton();
	}

	public static void ableMenu() {
		play.ableMenu();
	}

	public static void throwEnd() {
		error.throwEnd();
	}

	public static void printMoveButton() {
		play.printMoveButton();
	}

	public static String getNowMassName() {
		return play.getNowMassName();
	}

	public static void closeMenu() {
		play.closeMenu();
	}

	//最終結果表示
	public static void finish() {//ConfirmationFrame
		openClosing();
		String name = ClosingEvent.finish();
		if(!Player.player.isPlayer()) {
			createPopUp("最終結果","優勝は"+name+"です！\nおめでとうございます！",3000);
		}else {
			createPopUp("最終結果","優勝は"+name+"です！\nおめでとうございます！",5000);
		}
	}

	public static boolean isPlayShowing() {
		return play.isShowing();
	}
}
