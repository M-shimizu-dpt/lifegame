/*
 * 画面表示に関する処理を管理するクラス
 * 画面表示をする際にこのクラスのメソッドを使用
*/
package lifegame.game.map.print;




import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lifegame.game.WaitThread;
import lifegame.game.event.ClosingEvent;
import lifegame.game.map.information.Coordinates;
import lifegame.game.map.information.Japan;
import lifegame.game.map.information.Property;
import lifegame.game.map.information.Station;
import lifegame.game.object.Binbo;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.search.Searcher;

public class Window implements ActionListener{
	private JFrame playFrame = new JFrame("桃大郎電鉄");//メインフレーム
	private JButton playRight;//プレイマップでの移動ボタン
	private JButton playLeft;//プレイマップでの移動ボタン
	private JButton playTop;//プレイマップでの移動ボタン
	private JButton playBottom;//プレイマップでの移動ボタン
	private JButton saikoro;//プレイマップでのサイコロボタン
    private JButton cardB;//プレイマップでのカード一覧表示ボタン
    private JButton company;//プレイマップでのプレイヤー情報一覧表示ボタン
    private JButton minimap;//プレイマップでの詳細マップ表示ボタン
    private JButton allmap;//プレイマップでの全体マップ表示ボタン
    private JButton waitButton;//CPU操作中にプレーヤーが一時停止するためのボタン
    private JLabel mainInfo;//プレイマップで上に表示されるプレイヤー情報を表示するラベル
    private JPanel back = new JPanel();//メニューボタンの背景
    private JLabel moveLabel;//後何マス移動できるか、目的地までの最短距離を表示するラベル
	private JFrame diceFrame = new JFrame("サイコロ");//サイコロ用フレーム
	private JFrame mapFrame = new JFrame("マップ");//マップ確認用フレーム
	private JFrame infoFrame = new JFrame("会社情報");//会社情報用フレーム
	private JFrame propertyFrame;//物件情報確認用フレーム
	private JFrame cardFrame = new JFrame("カード");//所持カード一覧表示用フレーム
    private JFrame goalFrame = new JFrame("ゴール");//ゴール画面用フレーム
    private JFrame errorFrame = new JFrame("カードが満タン");//カード削除用フレーム
    private JFrame dubbingCardFrame = new JFrame("ダビング");//カード複製用フレーム
	private JFrame sellStationFrame;//物件売却用フレーム
	private JFrame randomFrame;//randomイベント用フレーム
	private JFrame shopFrontFrame;//カードshopイベント用フレーム
	private JFrame shopFrame;//カードshop購買イベント用フレーム
	private JFrame confirmationFrame;//ポップアップ用フレーム

	private JFrame binboFrame;//貧乏神イベント用フレーム＊＊＊＊

	private Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報
	private Player player;//操作中のプレイヤー
	public static Boolean turnEndFlag=false,closingEndFlag=false,shoppingEndFlag=false,throwFlag=false,random2EndFlag=false;//ターンを交代するためのフラグ
	public static Boolean bonbyTurnEndFlag = false;//ボンビー終了フラグ
	private int turn=0;//現在のターン
	private Dice dice = new Dice();//サイコロ処理
	public static Japan japan = new Japan();//物件やマス情報
	private ArrayList<String> moveTrajectory = new ArrayList<String>();//プレイヤーの移動の軌跡
	private Map<String,ArrayList<Integer>> moneyTrajectory = new HashMap<String,ArrayList<Integer>>();//プレイヤーのお金の増減の軌跡
	private int year=1;//今の年
	private int month=4;//今の月


	public static int count;//目的のマスまでの最短距離
	public static long time;//マルチスレッド開始からの経過時間

	private ArrayList<Card> canBuyCardlist = new ArrayList<Card>();//店の購入可能カードリスト
	public Binbo poorgod = new Binbo();

	public Window(int endYear,int playerCount){
		int w = 800, h = 600;
		playFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
        // ウィンドウのサイズ・初期位置
        playFrame.setSize(w, h);
        playFrame.setLocationRelativeTo(null);//windowsの画面の中央に表示
        playFrame.setLayout(null);

        // 背景色追加
        playFrame.getContentPane().setBackground(Color.ORANGE);

        playMap();
    	init(playerCount);

        JLayeredPane button = playFrame.getLayeredPane();//ボタンが前に出ない

        back.setBackground(Color.CYAN);
        back.setBounds(640,350,110,210);
        back.setName("ボタン背景");
        button.add(back,JLayeredPane.PALETTE_LAYER,-1);
        button.add(company,JLayeredPane.PALETTE_LAYER,0);
        button.add(saikoro,JLayeredPane.PALETTE_LAYER,0);
    	button.add(cardB,JLayeredPane.PALETTE_LAYER,0);
    	button.add(minimap,JLayeredPane.PALETTE_LAYER,0);
    	button.add(allmap,JLayeredPane.PALETTE_LAYER,0);

    	// ウィンドウを表示
        playFrame.setVisible(true);

        try {
        	play(endYear,playerCount);
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
	}

	//メイン画面の上に書いてあるプレイヤーの情報を更新
	public void reloadInfo() {
		mainInfo.setVisible(false);
		if(player.isEffect()){
			if(player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス　効果発動中("+player.getEffect()+")");
			}else if(player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()/10000+"億円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス　効果発動中("+player.getEffect()+")");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()/10000+"億　"+player.getMoney()%10000+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス　効果発動中("+player.getEffect()+")");
			}
		}else {
			if(player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス");
			}else if(player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()/10000+"億円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()/10000+"億　"+player.getMoney()%10000+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"まで"+Window.count+"マス");
			}
		}
		mainInfo.setVisible(true);
	}

	private void initMenu() {
		if(player.isEffect()){
			mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"までの最短距離:"+Window.count+"マス　効果発動中("+player.getEffect()+")");
		}else {
			mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+player.getName()+"　持ち金："+player.getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.getGoalName()+"までの最短距離:"+Window.count+"マス");
		}
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(player.getName()+player.getMoney());
		playFrame.getLayeredPane().add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
	}

  	//プレイ中の動作
	private void play(int endYear, int playerCount) throws InterruptedException{
    	Boolean first=true;
    	moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+player.getMove()+"　"+japan.getGoalName()+"までの最短距離:"+Window.count);
    	moveLabel.setName("moves");
    	playFrame.setBackground(Color.ORANGE);
    	closeMoveButton();
    	playFrame.getLayeredPane().add(waitButton,JLayeredPane.PALETTE_LAYER);

		//ボンビー初期設定***
		player.changeBonby();
		poorgod.setBinboPlayer(player);
		Player.setStopFlag(false);

    	while(true) {
    		if(first) {
    			printMonthFrame(month);
    		}else {
	    		if(turn==3) {
	    			ArrayList<Integer> moneyList = new ArrayList<Integer>();
	    			for(int i=0;i<4;i++) {
	    				moneyList.add(players.get(i).getMoney());
	    			}
	    			moneyTrajectory.put(year+"年"+month+"月", moneyList);//この月のプレイヤーの所持金を保存
	    			if(month==3) {
		    			closing();
		    			year++;
					}
	    			month++;
	    			if(month==13) {
	    				month=1;
	    			}
	    			printMonthFrame(month);
	    			turn=0;
	    		}else {
	    			turn++;
	    		}
    		}
    		first=false;
    		if(year>endYear)break;
    		player=players.get(turn);//このターンのプレイヤーを選定

    		if(player.isPlayer()) {
    			waitButton.setVisible(false);
    		}else {
    			waitButton.setVisible(true);
    		}
    		Searcher.searchShortestRoute(this,player);//目的地までの最短経路を探索
    		WaitThread waitthred  = new WaitThread(2);//再探索に対応していない為、3回程再探索を行っていた場合reloadInfoで正しく更新されない可能性がある。
    		waitthred.start();
    		waitthred.join();
    		japan.saveGoal();
    		System.out.println("1  colt coor:"+player.getColt().getX()+","+player.getColt().getY());
    		moveMaps();//画面遷移が少し遅い
    		System.out.println("2  colt coor:"+player.getColt().getX()+","+player.getColt().getY());
    		reloadInfo();//画面上部に表示している情報を更新
    		Card.priceSort(player.getCards());//プレイヤーが持つカードを価格順にソート
    		if(!player.isPlayer()) {//cpu操作
    			player.cpu(this,players,dice,turn);
    		}else {
    			printMenu();
    		}

    		WaitThread turnEnd  = new WaitThread(0);//ターン終了まで待機
			turnEnd.start();
			turnEnd.join();
    		bonbyplayer();
    		WaitThread bonbyTurnEnd = new WaitThread(5);//ボンビーターン終了まで待機
     		bonbyTurnEnd.start();
     		try {
    			bonbyTurnEnd.join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
     		bonbyTurnEndFlag=false;

    		Thread.sleep(1000);
    		turnEndFlag=false;

    		japan.alreadys.clear();//このターンに購入した物件リストを初期化
    	}
    	System.out.println("終わり");
    }

	//指定のFrameを1秒後に閉じる
	private void setCloseFrame(int id) {
		if(!player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			TimerTask task = new CPUTimerTask(this,id);
			timer.schedule(task, 1000);
		}
	}

	//マスに到着した時のマスのイベント処理
	private void massEvent() {
		closeMoveButton();
		String massName = playFrame.getLayeredPane().getComponentAt(400, 300).getName();
		if(Player.isStop()) {
			WaitThread wait = new WaitThread(7);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(massName.substring(0, 1).equals("B")) {
			blueEvent();
		}else if(massName.substring(0, 1).equals("R")) {
			redEvent();
		}else if(massName.substring(0, 1).equals("Y")) {
			yellowEvent();
		}else if(massName.substring(0, 1).equals("S")) {
			shopEvent();
		}else{
			if(japan.getGoalName().equals(massName)) {
				goal();
			}else {
				printPropertys(massName);
			}
		}
		ableMenu();
	}

	//青マスイベント
	private void blueEvent() {
		Random rand = new Random();
		int result=0;
		while(result<500) {
			result =rand.nextInt(2000);
		}
		result += result*(year/10);
		result -= result%100;
		System.out.println(result);
		player.addMoney(result);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//赤マスイベント
	private void redEvent() {
		Random rand = new Random();
		int result=0;
		while(result<500) {
			result = rand.nextInt(2000);
		}
		result += result*(year/10);
		result -= result%100;
		System.out.println(-result);
		player.addMoney(-result);
		if(player.getMoney() < 0 && player.getPropertys().size() > 0) {
			printTakeStations();
		}else{
			if(rand.nextInt(100) < 3) {
				randomEvent();
			}else {
				turnEndFlag=true;
			}
		}
	}

	//黄マスイベント
	private void yellowEvent() {
		Random rand = new Random();
		boolean get=false;
		int index=0;
		while(true) {
			get=false;
			index = rand.nextInt(Card.cardList.size());
			int i=0;
			//System.out.println("candidate card, name:"+Card.cardList.get(index).getName()+"  rarity"+Card.cardList.get(index).getRarity());
			do {
				if(rand.nextInt(100)<30) {
					get=true;
				}
				i++;
			}while(i<Card.cardList.get(index).getRarity());
			if(!get) {
				break;
			}
		}
		player.addCard(Card.cardList.get(index));
		if(player.getCards().size()>8) {
			cardFull();
			WaitThread wait = new WaitThread(9);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Card Get! name:"+Card.cardList.get(index).getName()+"  rarity"+Card.cardList.get(index).getRarity());
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//所持カードが最大を超えた場合、捨てるカードを選択する為所持カード一覧を表示
	public void cardFull() {
		errorFrame.setSize(400,500);
		errorFrame.setLocationRelativeTo(null);
		errorFrame.setLayout(null);
		JLayeredPane error = errorFrame.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<player.getCards().size();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,player.getCard(i).getName());
        	label.setBackground(Color.LIGHT_GRAY);
        	error.add(label);
        	throwButton.setActionCommand(player.getCard(i).getName()+"t");
        	error.add(throwButton);
        }
		error.add(titleName);

		playFrame.setVisible(false);
		errorFrame.setVisible(true);

		if(!player.isPlayer()) {
			player.cardFullCPU();
			errorFrame.setVisible(false);
			playFrame.setVisible(true);
		}
	}



	//店イベント
	private void shopEvent() {
		playFrame.setVisible(false);
		shopFrontFrame = new JFrame("カードショップ");
		JLayeredPane shop = shopFrontFrame.getLayeredPane();
		shopFrontFrame.setSize(300,400);
		shopFrontFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(100,210,100,50,10,"出る");
		closeButton.setActionCommand("店を出る");
		shop.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JButton buyButton = createButton(100,10,100,50,10,"買う");
		buyButton.setActionCommand("カードを買う");
		shop.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
		JButton sellButton = createButton(100,110,100,50,10,"売る");
		sellButton.setActionCommand("カードを売る");
		if(player.getCards().size()==0) {
			sellButton.setEnabled(false);
		}
		if(!player.isPlayer()) {
			closeButton.setEnabled(false);
			buyButton.setEnabled(false);
			sellButton.setEnabled(false);
		}
		Random rand = new Random();
		boolean get=false;
		int index=0;
		for(int i = 0;i<8;i++) {//表示するカード8枚を選出
			do {
				get=false;
				boolean flag;
				do {//今まで選出したカードと今回選出したカードが被った場合は再選
					flag=true;
					index = rand.nextInt(Card.cardList.size());
					for(Card card : canBuyCardlist) {
						if(card.contains(Card.cardList.get(index))) {
							flag=false;
						}
					}
				}while(!flag);
				int rarity=0;
				do {
					if(rand.nextInt(100)<30) {
						get=true;
					}
					rarity++;
				}while(rarity<Card.cardList.get(index).getRarity());
			}while(!get);
			canBuyCardlist.add(Card.cardList.get(index));
		}
		shop.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
		shopFrontFrame.setVisible(true);
		setCloseFrame(1);
	}

	//店用フレームを閉じる
	public void closeShop() {
		Random rand = new Random();
		shopFrontFrame.setVisible(false);
		shopFrontFrame.removeAll();
		canBuyCardlist.clear();
		Window.shoppingEndFlag=true;
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//カードショップの購入画面
	private void printBuyShop() {
		shopFrontFrame.setVisible(false);
		shopFrame = new JFrame("購入");
		JLayeredPane shopBuy = shopFrame.getLayeredPane();
		shopFrame.setSize(600, 600);
		shopFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(500,500,70,50,10,"戻る");
		closeButton.setActionCommand("カード購入を終える");
		shopBuy.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JLabel myMoney = createText(10,5,400,40,10,"所持金"+player.getMoney());
		shopBuy.add(myMoney);

		for(int i=1; i<=canBuyCardlist.size(); i++) {
			JButton buyButton = createButton(500,i*50,70,50,10,"購入");
			buyButton.setActionCommand(canBuyCardlist.get(i-1).getName()+":b");
			if(canBuyCardlist.get(i-1).getBuyPrice() > player.getMoney() || player.getCards().size() > 7) {
				buyButton.setEnabled(false);
			}
			shopBuy.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,canBuyCardlist.get(i-1).getName());
			shopBuy.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(canBuyCardlist.get(i-1).getBuyPrice()));
			shopBuy.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}

		if(player.getCards().size() > 7) {
			JLabel cardFull = createText(450,5,130,40,10,"カードがいっぱいです");
			shopBuy.add(cardFull);
		}
		shopFrame.setVisible(true);
	}

	private void buyCard(Card card) {
		player.buyCard(card);
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		printBuyShop();
	}

	//カードショップの売却画面
	private void printSellShop() {
		shopFrontFrame.setVisible(false);
		shopFrame = new JFrame("売却");
		JLayeredPane shopSell = shopFrame.getLayeredPane();
		shopFrame.setSize(600, 600);
		shopFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(500,500,70,50,10,"戻る");
		closeButton.setActionCommand("カード売却を終える");
		shopSell.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		for(int i=1; i<=player.getCards().size(); i++) {
			JButton sellButton = createButton(500,i*50,70,50,10,"売却");
			sellButton.setActionCommand(player.getCard(i-1).getName()+":s");
			shopSell.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,player.getCard(i-1).getName());
			shopSell.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(player.getCard(i-1).getSellPrice()));
			shopSell.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}
		shopFrame.setVisible(true);
	}

	private void sellCard(Card card) {
		player.sellCard(card);
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		printSellShop();
	}

	//カードショップの売買画面を閉じる
	private void backShop() {
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		if(player.getCards().size()>0) {
			shopFrontFrame.getLayeredPane().getComponentAt(100,110).setEnabled(true);
		}else {
			shopFrontFrame.getLayeredPane().getComponentAt(100,110).setEnabled(false);
		}
		shopFrontFrame.setVisible(true);
	}

	//randomイベント
	private void randomEvent() {
		Random rand = new Random();
		playFrame.setVisible(false);
		randomFrame = new JFrame();
		JLayeredPane random = randomFrame.getLayeredPane();
		randomFrame.setSize(800,600);
		randomFrame.setLocationRelativeTo(null);
		JLabel text1=new JLabel();
		JLabel text2=new JLabel();
		JLabel text3=new JLabel();
		JButton closeButton = createButton(580,500,180,50,10,"閉じる");
		closeButton.setActionCommand("randomイベントを閉じる");
		if(!player.isPlayer()) {
			closeButton.setEnabled(false);
		}
		random.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		double randomNum = rand.nextDouble();
		if(randomNum < 0.1) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を1/4失った");
			player.addMoney(-player.getMoney()/4);
		}else if(randomNum < 0.2) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を半分失った");
			player.addMoney(-player.getMoney()/2);
		}else if(randomNum < 0.3) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を全て失った");
			player.addMoney(-player.getMoney());
		}else if(randomNum < 0.4) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"1億円もらった");
			player.addMoney(10000);
		}else if(randomNum < 0.5) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"2億円もらった");
			player.addMoney(20000);
		}else if(randomNum < 0.6) {
			randomFrame.setName("しあわせの小鳥");
			text1 = createText(10,10,600,100,20,"ちゅんちゅんちゅん");
			text2 = createText(10,110,600,100,20,"ちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅん");
			text3 = createText(10,210,600,100,20,"ちゅんちゅんちゅん(一頭地を抜くカードをもらった)");
			for(int i=0;i<Card.cardList.size();i++) {
				if(Card.cardList.get(i).contains("一頭地を抜くカード")) {
					player.addCard(Card.cardList.get(i));
				}
			}
		}else if(randomNum < 0.7 && japan.isOwners()) {
			randomFrame.setName("鋼鉄の人");
			text1 = createText(10,10,600,100,20,"全身赤い装甲に身を包んだアメリカの空飛ぶ天才発明家が現れた！");
			text2 = createText(10,110,600,100,20,"Destroying properties at random！Don't hold a grudge.");
			//誰かの物件の所有権を初期化
			for(Property property : japan.getPropertys()) {
				if(property.isOwner()) {
					text3 = createText(10,210,600,100,20,"誰かの物件が破壊された(" + property.getOwner() + "の" + property.getName() + ")");
					property.setOwner("");
					if(japan.getStation(property).isMono()) {
						japan.monopoly(property);
					}
					break;
				}
			}
		}else if(randomNum < 0.8) {
			randomFrame.setName("偉い人");
			text1 = createText(10,10,600,100,20,"偉い人が現れた！");
			text2 = createText(10,110,600,100,20,"山形の開発工事を行いたいを思っているので所持金全部投資してください");
			if(rand.nextInt(100) < 50) {
				text3 = createText(10,210,600,100,20,"事業が成功し、所持金が倍になります");
				player.addMoney(player.getMoney());
			}else {
				text3 = createText(10,210,600,100,20,"事業が失敗し、所持金が無くなります");
				player.addMoney(-player.getMoney());
			}
		}else if(randomNum < 0.9) {
			randomFrame.setName("スキャンダル");
			text1 = createText(10,10,600,100,20,"若者とキャッキャウフフしていたのがばれた");
			text2 = createText(10,110,600,100,20,"世間体を気にして移動を自粛することにした");
			if(player.isEffect()) {
				text3 = createText(10,210,600,100,20,"移動距離が-3される");
			}else {
				text3 = createText(10,210,600,100,20,"3カ月の間、移動距離が-3される");
			}
			//3か月間移動距離を制限する
			player.getBuff().addBuff(-3, 3);
		}else {
			randomFrame.setName("富士山");
			text1 = createText(10,10,600,100,20,player.getName()+"「富士山に行きたい！！！」");
			text2 = createText(10,110,600,100,20,"富士山の登頂に成功し、気分が良くなった");
			text3 = createText(10,210,600,100,20,"登山費用として5000万円失った");
			player.addMoney(-5000);
		}

		text1.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text2);
		text3.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text3);
		randomFrame.setVisible(true);
		setCloseFrame(0);
	}

	public void closeRandomEvent() {
		randomFrame.setVisible(false);
		randomFrame.removeAll();
		playFrame.setVisible(true);
		turnEndFlag = true;
	}

	//月が替わった時に何月か表示
	private void printMonthFrame(int month) {
		if(!playFrame.isShowing()) {
			while(!playFrame.isShowing()) {
				try {
    				Thread.sleep(100);
    			}catch(InterruptedException e) {

    			}
			}
		}
		playFrame.setVisible(false);
		JFrame monthFrame = new JFrame(month + "月");
		monthFrame.setSize(800,600);
		monthFrame.setLocationRelativeTo(null);
		monthFrame.getContentPane().add(createText(10,10,300,200,100,month+"月"));
		if(month>=3 && month<=5) {
			monthFrame.getContentPane().setBackground(Color.PINK);
		}else if(month>=6 && month<=8) {
			monthFrame.getContentPane().setBackground(Color.RED);
		}else if(month>=9 && month<=11) {
			monthFrame.getContentPane().setBackground(Color.ORANGE);
		}else{
			monthFrame.getContentPane().setBackground(Color.BLUE);
		}
		monthFrame.setVisible(true);
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {

		}
		monthFrame.setVisible(false);
		playFrame.setVisible(true);

		if(japan.isOwners()) {
			random2Event(month,year);
		}
	}

	/*
	 * 収益表示
	 */
	private void revenue() {
		JFrame revenueFrame = new JFrame("収益");
		JLayeredPane revenue = revenueFrame.getLayeredPane();
		revenueFrame.setSize(800, 600);
		revenueFrame.setLocationRelativeTo(null);
		revenueFrame.setVisible(true);

		JLabel profitLabel = createText(10,10,370,40,15,"今までの収益の推移");
		profitLabel.setBackground(Color.BLUE);
		revenue.add(profitLabel);
		//グラフ作成(左半分)
		for(float i=0;i<Player.getProfitListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x=(int)(300*((i+1)/Player.getProfitListSize()));								//x座標を算出
				int y=(int)(500-(400*Player.getProfitList((int)i)[j]/(Player.maxProfit-Player.minProfit)));	//y座標を算出
				graph.setBounds(x,y,5,5);													//（x,y）をプロット
				graph.setBackground(Color.YELLOW);
				revenue.add(graph);
				//System.out.println("x:" + String.valueOf(x) + "\ty:" + String.valueOf(y));
				if(i==Player.getProfitListSize()-1) {//ぬるぽ回避
					continue;
				}
				//グラフ線分
				/*
				 * (x1,y1):線分の始点
				 * (x2,y2):線分の終点
				 * a:x座標の増加分　b:y座標の増加分
				 * c:aを2倍し絶対値を取った値
				 * d:bを2倍し絶対値を取った値
				 * dx,dy:増減を格納用
				 * fraction:
				 *
				 */
				int x1=(int)x;
				int y1=(int)y;
				int x2=(int)(300*((i+2)/Player.getProfitListSize()));									//翌年のx座標を算出
				int y2=(int)(500-(400*Player.getProfitList((int)i+1)[j]/(Player.maxProfit-Player.minProfit)));		//翌年のy座標を算出

				//System.out.println("x1:"+x+"\ty1:"+y1+"\tx2:"+x2+"\ty2:"+y2);

				int a = x2 - x1;
				int b = y2 - y1;
				int dx,dy,fraction;

				if(a < 0) {
					dx = -1;
				}else {
					dx = 1;
				}
				if(b < 0) {
					dy = -1;
				}else {
					dy = 1;
				}

				int c = Math.abs(a * 2);
				int d = Math.abs(b * 2);

				if(c > d) {
					fraction = d - c/2;
					while(x1 != x2) {
						if(fraction >= 0) {
							y1 += dy;
							fraction -= c;
						}
						x1 += dx;
						fraction += d;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						revenue.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}else {
					fraction = c - d/2;
					while(y1 != y2) {
						if(fraction >= 0) {
							x1 += dx;
							fraction -= d;
						}
						y1 += dy;
						fraction += c;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						revenue.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}

		//グラフの具体的な数値(右半部)
		JLabel profitThisYearLabel = createText(400,10,370,40,15,"今年の収益");
		profitThisYearLabel.setBackground(Color.BLUE);
		revenue.add(profitThisYearLabel);
		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			revenue.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerProfitLabel = createText(500,110+(100*i),100,40,10,String.valueOf(Player.getProfitList(Player.getProfitListSize()-1)[i]));
			playerProfitLabel.setBackground(Color.white);
			revenue.add(playerProfitLabel,JLayeredPane.DEFAULT_LAYER,0);
		}

		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		closeButton.setActionCommand("決算画面を閉じる");
		revenue.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		revenueFrame.setVisible(true);
		WaitThread thread = new WaitThread(3);
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		closingEndFlag=false;
		revenueFrame.setVisible(false);
		revenue.removeAll();

		ClosingEvent.aggregateAssets(players);
		Assets();
	}

	/*
	 * 総資産表示
	 */
	private void Assets() {
		JFrame AssetsFrame = new JFrame("総資産");
		JLayeredPane Assets = AssetsFrame.getLayeredPane();
		AssetsFrame.setSize(800, 600);
		AssetsFrame.setLocationRelativeTo(null);
		AssetsFrame.setVisible(true);

		//System.out.println("x="+getProfitList(Size());
		JLabel assetsLabel = createText(10,10,370,40,15,"今までの総資産の推移");
		assetsLabel.setBackground(Color.BLUE);
		Assets.add(assetsLabel);

		for(float i=0;i<Player.getAssetsListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x = (int)(300*((i+1)/Player.getAssetsListSize()));
				int y = (int)(500-(400*Player.getAssetsList((int)i)[j]/(Player.maxAssets-Player.minAssets)));
				graph.setBounds(x,y,5,5);
				graph.setBackground(Color.YELLOW);
				Assets.add(graph);
				//System.out.println("x:"+x+"\ty:"+y);
				if(i==Player.getAssetsListSize()-1) {//ぬるぽ回避
					continue;
				}
				//グラフ線分
				/*
				 * (x1,y1):線分の始点
				 * (x2,y2):線分の終点
				 * a:x座標の増加分　b:y座標の増加分
				 * c:aを2倍し絶対値を取った値
				 * d:bを2倍し絶対値を取った値
				 * dx,dy:増減を格納用
				 * fraction:
				 *
				 */
				int x1=(int)x;
				int y1=(int)y;
				int x2=(int)(300*((i+2)/Player.getAssetsListSize()));									//翌年のx座標
				int y2=(int)(500-(400*Player.getAssetsList((int)i+1)[j]/(Player.maxAssets-Player.minAssets)));		//翌年のy座標

				//System.out.println("x1:"+x+"\ty1:"+y1+"\tx2:"+x2+"\ty2:"+y2);

				int a = x2 - x1;
				int b = y2 - y1;
				int dx,dy,fraction;
					if(a<0) {
					dx = -1;
				}else {
					dx = 1;
				}
				if(b<0) {
					dy = -1;
				}else {
					dy = 1;
				}

				int c=Math.abs(a*2);
				int d=Math.abs(b*2);

				if(c > d) {
					fraction = d - c/2;
					while(x1 != x2) {
						if(fraction >= 0) {
							y1 += dy;
							fraction -= c;
						}
						x1 += dx;
						fraction += d;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						Assets.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}else {
					fraction = c - d/2;
					while(y1 != y2) {
						if(fraction >= 0) {
							x1 += dx;
							fraction -= d;
						}
						y1 += dy;
						fraction += c;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						Assets.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}

		//グラフの具体的な数値(右半部)
		JLabel assetsThisYearLabel = createText(400,10,370,40,15,"今年の総資産");
		assetsThisYearLabel.setBackground(Color.BLUE);
		Assets.add(assetsThisYearLabel);
		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			Assets.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerAssetsLabel = createText(500,110+(100*i),100,40,10,String.valueOf(Player.getAssetsList(Player.getAssetsListSize()-1)[i]));
			playerAssetsLabel.setBackground(Color.white);
			Assets.add(playerAssetsLabel,JLayeredPane.DEFAULT_LAYER,0);
		}
		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		closeButton.setActionCommand("決算画面を閉じる");
		Assets.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		AssetsFrame.setVisible(true);
		WaitThread thread = new WaitThread(3);
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		closingEndFlag=false;
		AssetsFrame.setVisible(false);
		Assets.removeAll();
		//ここまで総資産
	}


	/*
	 * ランダムイベント
	 * ①臨時収入
	 * playerが所有する物件の中から1件選び臨時収入が入る
	 *
	 * propertyに指定したスパン毎の部門属性を付与
	 * ownerがいるproeprty全てを取得するメソッドを用意
	 * その中から指定の部門を抽出
	 * 抽出したpropertyの臨時収入をownerに付与
	 *
	 * ・見た目
	 * 専用フレームを作る
	 * 必要な情報を記載
	 * 流れに合う箇所に記述する
	 *
	 */
	private void random2Event(int month,int year) {

		int rndnum;
		rndnum = new Random().nextInt(11)+1;
		System.out.println("year:"+year+"\tmonth:"+month+"\trndnum:"+rndnum);

		if(month==rndnum) {
    		JFrame RandomEvent2 = new JFrame("トピックス");
    		JLayeredPane Random2 = RandomEvent2.getLayeredPane();
    		RandomEvent2.setSize(800, 600);
    		RandomEvent2.setLocationRelativeTo(null);
    		RandomEvent2.setVisible(true);

    		JLabel text1=new JLabel();
    		JLabel text2=new JLabel();
    		JLabel text3=new JLabel();
    		JLabel text4=new JLabel();

    		text1 = createText(10,10,600,100,20,"トピックスです");
        	text2 = createText(10,110,600,100,20,"全国の放送局で特集が放送されました！");
    		text3 = createText(10,210,600,100,20,"テレビの影響はすごく,大きな収入が出ています。");
    		System.out.println("所持金");
    		for(int i=0;i<4;i++) {
    			System.out.println(players.get(i).getName()+":"+players.get(i).getMoney());
    		}

    		//物件の情報を取得
    		for(Property property : japan.getPropertys()) {
    			//オーナーの有無の判断
    			if(property.isOwner()) {
    				//物件が1～3かどうか判断
    				if(property.getGroup()==1||property.getGroup()==2||property.getGroup()==3) {
    					//物件の選出
    					text4 = createText(10,310,600,100,20,"臨時収入が入ります(" + property.getOwner() + "の" + property.getName() + ")");
    					System.out.println("臨時収入:"+property.getOwner()+"の"+ property.getName());
    					//臨時収入を追加

    					int s;
    		    		s=(int)(year/rndnum*5000);

    					if(property.getOwner() == players.get(0).getName()) {
    						players.get(0).addMoney(s);
    					}else if(property.getOwner() == players.get(1).getName()) {
    						players.get(1).addMoney(s);
    					}else if(property.getOwner() == players.get(2).getName()) {
    						players.get(2).addMoney(s);
    					}else if(property.getOwner() == players.get(3).getName()) {
    						players.get(3).addMoney(s);
    					}else {
    						break;
    					}
    					break;
    				}
    			}
        	}
    		System.out.println("所持金");
    		for(int i=0;i<4;i++) {
    			System.out.println(players.get(i).getName()+":"+players.get(i).getMoney());
    		}

    		text1.setHorizontalTextPosition(SwingConstants.LEFT);//左に寄せたいができない
    		Random2.add(text1);
    		text2.setHorizontalTextPosition(SwingConstants.LEFT);
    		Random2.add(text2);
    		text3.setHorizontalTextPosition(SwingConstants.LEFT);
    		Random2.add(text3);
    		text4.setHorizontalTextPosition(SwingConstants.LEFT);
    		Random2.add(text4);

    		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
    		closeButton.setActionCommand("臨時収入画面を閉じる");
    		Random2.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

    		if(player.isPlayer()) {
	    		Thread thread = new Thread(new WaitThread(8));
	    		thread.start();
	    		try {
	    			thread.join();
	    		}catch(InterruptedException e) {
	    			e.printStackTrace();
	    		}
    		}else {
    			try {
    				Thread.sleep(3000);
    			}catch(InterruptedException e) {
    				e.printStackTrace();
    			}
    		}

    		RandomEvent2.setVisible(false);
    		Random2.removeAll();
		}
	}

	private void closing() {
		JFrame closingFrame = new JFrame("決算");
		JLayeredPane closing = closingFrame.getLayeredPane();
		closingFrame.setSize(800, 600);
		closingFrame.setLocationRelativeTo(null);
		closingFrame.add(createText(10,10,300,200,100,"決算"));
		closingFrame.setVisible(true);
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {

		}

		closingFrame.setVisible(false);
		closing.removeAll();
		closingFrame.setLayout(null);

		ClosingEvent.closing(players);

		revenue();
	}

	//駅以外のマスを作成
	private JPanel createMass(int j,int i,int distance) {
		JPanel mass = new JPanel();
		if(japan.containsBlue(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.BLUE);
			mass.setName("B"+japan.getIndexOfBlue(j, i));
		}else if(japan.containsRed(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.RED);
			mass.setName("R"+japan.getIndexOfRed(j, i));
		}else if(japan.containsYellow(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+japan.getIndexOfYellow(j, i));
		}else if(japan.containsShop(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.GRAY);
			mass.setName("S"+japan.getIndexOfShop(j, i));
		}
		return mass;
	}

	//ボタンを作成
	private JButton createButton(int x,int y,int w,int h,int size,String name) {
		JButton button = new JButton(name);
		button.setFont(new Font("SansSerif", Font.ITALIC, size));
		button.setBounds(x,y,w,h);
		button.addActionListener(this);
		button.setActionCommand(name);
		button.setName(name);
		if(!player.isPlayer())button.setEnabled(false);
		return button;
	}

	//textを作成
	private JLabel createText(int x,int y,int w,int h,int size,String name) {
		JLabel text = new JLabel(name,SwingConstants.CENTER);
		text.setOpaque(true);
		text.setBounds(x, y, w, h);
		text.setFont(new Font("SansSerif", Font.ITALIC, size));
		text.setName(name);
		return text;
	}

	public void createPopUp(String title,String article) {
		playFrame.setVisible(false);
		confirmationFrame = new JFrame(title);
		confirmationFrame.setSize(800,600);
		confirmationFrame.setLocationRelativeTo(null);
		JLayeredPane confirmation = confirmationFrame.getLayeredPane();
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
		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		JButton closeButton =createButton(700,500,70,50,10,"閉じる");
		closeButton.setActionCommand("ポップアップを閉じる");
		confirmation.add(closeButton,JLayeredPane.PALETTE_LAYER);
		confirmationFrame.setVisible(true);
	}

	public void createPopUp(String title,String article,int time) {
		playFrame.setVisible(false);
		confirmationFrame = new JFrame(title);
		confirmationFrame.setSize(800,600);
		confirmationFrame.setLocationRelativeTo(null);
		JLayeredPane confirmation = confirmationFrame.getLayeredPane();
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
		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		confirmationFrame.setVisible(true);

		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}

		confirmationFrame.setVisible(false);
		playFrame.setVisible(true);
	}

	public void createPopUpOnPlay(String title,String article) {
		confirmationFrame = new JFrame(title);
		confirmationFrame.setSize(800,400);
		confirmationFrame.setLocationRelativeTo(null);
		JLayeredPane confirmation = confirmationFrame.getLayeredPane();
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
		JLabel art = createText(0,0,800,400,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		JButton closeButton =createButton(700,300,70,50,10,"閉じる");
		closeButton.setActionCommand("ポップアップを閉じる");
		confirmation.add(closeButton,JLayeredPane.PALETTE_LAYER);
		confirmationFrame.setVisible(true);
	}

	public void createPopUpOnPlay(String title,String article,int time) {
		confirmationFrame = new JFrame(title);
		confirmationFrame.setSize(800,400);
		confirmationFrame.setLocationRelativeTo(null);
		JLayeredPane confirmation = confirmationFrame.getLayeredPane();
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
		JLabel art = createText(0,0,800,400,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		confirmationFrame.setVisible(true);

		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}

		confirmationFrame.setVisible(false);
	}

	private void closePopUp() {
		confirmationFrame.setVisible(false);
		confirmationFrame.removeAll();
		playFrame.setVisible(true);
	}

	//メイン画面での移動ボタンを作成
	private void createMoveButton() {
		JLayeredPane moveButton = playFrame.getLayeredPane();
		playRight.setActionCommand("右");
		playLeft.setActionCommand("左");
		playTop.setActionCommand("上");
		playBottom.setActionCommand("下");
		playRight.setName("右");
		playLeft.setName("左");
		playTop.setName("上");
		playBottom.setName("下");
		playRight.setVisible(false);
		playLeft.setVisible(false);
		playTop.setVisible(false);
		playBottom.setVisible(false);
		moveButton.add(playRight,JLayeredPane.PALETTE_LAYER,0);
		moveButton.add(playLeft,JLayeredPane.PALETTE_LAYER,0);
		moveButton.add(playTop,JLayeredPane.PALETTE_LAYER,0);
		moveButton.add(playBottom,JLayeredPane.PALETTE_LAYER,0);
	}

	//メイン画面での移動ボタンを非表示
	public void closeMoveButton() {
		playLeft.setBackground(Color.WHITE);
		playRight.setBackground(Color.WHITE);
		playTop.setBackground(Color.WHITE);
		playBottom.setBackground(Color.WHITE);
		playRight.setVisible(false);
		playLeft.setVisible(false);
		playTop.setVisible(false);
		playBottom.setVisible(false);
		moveLabel.setVisible(false);
	}

	//メイン画面での移動ボタンを表示
	private void printMoveButton() {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();
		vector = japan.getVector(player.getNowMass(),1);
		closeMoveButton();
		if(Searcher.nearestTrajectoryList.containsKey(Window.count)) {
			for(ArrayList<Coordinates> list:Searcher.nearestTrajectoryList.get(Window.count)) {
				for(Coordinates coor:list) {
					for(int i=0;i<4;i++) {
						if(coor.contains(player.getNowMass().getX()-1,player.getNowMass().getY())) {
							playLeft.setBackground(Color.MAGENTA);
						}else if(coor.contains(player.getNowMass().getX()+1,player.getNowMass().getY())) {
							playRight.setBackground(Color.MAGENTA);
						}else if(coor.contains(player.getNowMass().getX(),player.getNowMass().getY()-1)) {
							playTop.setBackground(Color.MAGENTA);
						}else if(coor.contains(player.getNowMass().getX(),player.getNowMass().getY()+1)) {
							playBottom.setBackground(Color.MAGENTA);
						}
					}
				}
			}
		}
		playLeft.setVisible(vector.get(0));
		playRight.setVisible(vector.get(1));
		playTop.setVisible(vector.get(2));
		playBottom.setVisible(vector.get(3));
		if(player.getMove() <= 0) {
			closeMoveButton();
		}else {
			Searcher.searchShortestRoute(this,player);
			WaitThread thread = new WaitThread(2);
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		moveLabel.setText("残り移動可能マス数:"+player.getMove()+"　"+japan.getGoalName()+"までの最短距離:"+Window.count);
		moveLabel.setVisible(true);
		playFrame.getLayeredPane().add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
	}

	//メイン画面でのメニューボタンを表示
	private void printMenu() {
		saikoro.setVisible(true);
		company.setVisible(true);
		cardB.setVisible(true);
		minimap.setVisible(true);
		allmap.setVisible(true);
		back.setVisible(true);
	}

	//メイン画面でのメニューボタンを非表示
	private void closeMenu() {
		saikoro.setVisible(false);
		company.setVisible(false);
		cardB.setVisible(false);
		minimap.setVisible(false);
		allmap.setVisible(false);
		back.setVisible(false);
	}

	//メイン画面でのメニューボタンを無効
	public void enableMenu() {
		saikoro.setEnabled(false);
		company.setEnabled(false);
		cardB.setEnabled(false);
		minimap.setEnabled(false);
		allmap.setEnabled(false);
	}

	//メイン画面でのメニューボタンを有効
	public void ableMenu() {
		saikoro.setEnabled(true);
		company.setEnabled(true);
		if(!Card.usedCard) {
			cardB.setEnabled(true);
		}
		minimap.setEnabled(true);
		allmap.setEnabled(true);
	}

	//サイコロ画面表示
	private void printDice() {
		JLayeredPane diceP = diceFrame.getLayeredPane();
		diceFrame.setSize(200, 250);
		diceFrame.setLayout(null);
		JButton button =createButton(50,50,100,50,10,"回す");
		JButton closeButton =createButton(100,150,70,50,10,"戻る");
		closeButton.setActionCommand("サイコロを閉じる");
		diceP.add(button);
		diceP.add(closeButton);
		// ウィンドウを表示
        diceFrame.setVisible(true);
	}

	//サイコロ画面を閉じる
	private void closeDice() {
		diceFrame.setVisible(false);
	}

	//サイコロ操作
	public void diceShuffle() {
		player.setMove(dice.shuffle(player));
		Searcher.searchCanMoveMass(this,player);
		if(player.getMove()==0) {
			massEvent();
		}else {
			addTrajectory();
			if(player.isPlayer()) {
				printMoveButton();
			}
		}
		closeMenu();
		dice.clear();
		Card.resetUsedCard();
		Card.resetUsedFixedCard();
		closeDice();
	}

	//所持カード一覧を表示
	private void printCard() {
		JLayeredPane card = cardFrame.getLayeredPane();
		cardFrame.setSize(700, 500);
		cardFrame.setLayout(null);
        JButton closeButton = createButton(570,400,100,40,10,"戻る");
        closeButton.setActionCommand("所持カード一覧を閉じる");
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int i=0;i<player.getCards().size();i++) {
        	JButton useButton = createButton(10,35*(i+1)+30,70,30,10,"使用");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(i+1)+30,180,30,10,player.getCards().get(i).getName());
        	JLabel labelText = createText(300,35*(i+1)+30,350,30,10,player.getCards().get(i).getText());
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(player.getCard(i).getName());
        	if(player.getCard(i).contains("ダビングカード") && player.getCards().size()<2) {
        		useButton.setEnabled(false);
        	}
        	card.add(labelName);
        	card.add(labelText);
        	card.add(useButton);
        }
        card.add(titleName);
        card.add(titleText);
        card.add(closeButton);

        cardFrame.setVisible(true);
	}

	//所持カード一覧を閉じる
	private void closeCard() {
		cardFrame.setVisible(false);
		cardFrame.getLayeredPane().removeAll();
	}

	//カードの複製を行う画面を表示
	public void printDubbing() {
		JLayeredPane dubbing = dubbingCardFrame.getLayeredPane();
		dubbingCardFrame.setSize(700,500);
		dubbingCardFrame.setLayout(null);
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int roop=0;roop<player.getCards().size();roop++) {
        	JButton useButton = createButton(10,35*(roop+1)+30,70,30,10,"複製");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(roop+1)+30,180,30,10,player.getCards().get(roop).getName());
        	JLabel labelText = createText(300,35*(roop+1)+30,350,30,10,player.getCards().get(roop).getText());
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(player.getCards().get(roop).getName()+"d");
        	dubbing.add(labelName);
        	dubbing.add(labelText);
        	dubbing.add(useButton);
        }
        dubbing.add(titleName);
        dubbing.add(titleText);

        dubbingCardFrame.setVisible(true);
        playFrame.setVisible(false);

        if(!player.isPlayer()) {
	        player.addCard(player.getCard(0));//無いも考えず一番上のカードを複製
	        setCloseFrame(4);
        }
	}

	//カードの複製を行う画面を非表示
	public void closeDubbing() {
		Random rand = new Random();
		dubbingCardFrame.setVisible(false);
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//会社情報を表示
	private void info() {
		//会社情報の表示
		infoFrame.setSize(800, 600);
		infoFrame.setLayout(null);
		Container info = infoFrame.getContentPane();
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		JLabel titleName = createText(20,20,100,100,20,"名前");
		JLabel titleMoney = createText(120,20,100,100,20,"所持金");
		JLabel player1Name = createText(20,120,100,100,20,players.get(0).getName());
		JLabel player2Name = createText(20,220,100,100,20,players.get(1).getName());
		JLabel player3Name = createText(20,320,100,100,20,players.get(2).getName());
		JLabel player4Name = createText(20,420,100,100,20,players.get(3).getName());
		JLabel player1Money;
		JLabel player2Money;
		JLabel player3Money;
		JLabel player4Money;
		closeButton.setActionCommand("会社情報を閉じる");
		if(players.get(0).getMoney()<10000) {
			player1Money = createText(120,120,100,100,20,players.get(0).getMoney() + "万円");
		}else if(players.get(0).getMoney()%10000==0){
			player1Money = createText(120,120,100,100,20,players.get(0).getMoney() + "億円");
		}else {
			player1Money = createText(120,120,100,100,20,players.get(0).getMoney()/10000 + "億円" + players.get(0).getMoney()%10000 + "万円");
		}
		if(players.get(1).getMoney()<10000) {
			player2Money = createText(120,220,100,100,20,players.get(1).getMoney() + "万円");
		}else if(players.get(1).getMoney()%10000==0){
			player2Money = createText(120,220,100,100,20,players.get(1).getMoney() + "億円");
		}else {
			player2Money = createText(120,220,100,100,20,players.get(1).getMoney()/10000 + "億円" + players.get(1).getMoney()%10000 + "万円");
		}
		if(players.get(2).getMoney()<10000) {
			player3Money = createText(120,320,100,100,20,players.get(2).getMoney() + "万円");
		}else if(players.get(2).getMoney()%10000==0){
			player3Money = createText(120,320,100,100,20,players.get(2).getMoney() + "億円");
		}else {
			player3Money = createText(120,320,100,100,20,players.get(2).getMoney()/10000 + "億円" + players.get(2).getMoney()%10000 + "万円");
		}
		if(players.get(3).getMoney()<10000) {
			player4Money = createText(120,420,100,100,20,players.get(3).getMoney() + "万円");
		}else if(players.get(3).getMoney()%10000==0){
			player4Money = createText(120,420,100,100,20,players.get(3).getMoney() + "億円");
		}else {
			player4Money = createText(120,420,100,100,20,players.get(3).getMoney()/10000 + "億円" + players.get(3).getMoney()%10000 + "万円");
		}
		info.add(titleName);
		info.add(titleMoney);
		info.add(player1Name);
		info.add(player1Money);
		info.add(player2Name);
		info.add(player2Money);
		info.add(player3Name);
		info.add(player3Money);
		info.add(player4Name);
		info.add(player4Money);
		info.add(closeButton);
		infoFrame.setVisible(true);
	}

	//会社情報を閉じる
	private void closeInfo() {
		infoFrame.setVisible(false);
		infoFrame.getContentPane().removeAll();
	}

	//詳細マップの画面遷移処理
	private void moveMaps(String cmd) {
		JLayeredPane maps = mapFrame.getLayeredPane();
		int x=0,y=0;
		for(int i=0;i<maps.getComponentCount();i++) {
			if(i>=0&&i<5) {//標準装備のコンポーネント以外
				continue;
			}
			if(maps.getComponent(i).getX() < 0 && cmd.equals("←")) {//左にフレームアウトしているコンポーネントが存在しない場合それ以上左に行けないようにする
				x=50;
				break;
			}
			if(maps.getComponent(i).getX() > 670 && cmd.equals("→")) {//右にフレームアウトしているコンポーネントが存在しない場合それ以上右に行けないようにする
				x=-50;
				break;
			}
			if(maps.getComponent(i).getY() < 0 && cmd.equals("↑")) {//上にフレームアウトしているコンポーネントが存在しない場合それ以上上に行けないようにする
				y=50;
				break;
			}
			if(maps.getComponent(i).getY() > 470 && cmd.equals("↓")) {//下にフレームアウトしているコンポーネントが存在しない場合それ以上下に行けないようにする
				y=-50;
				break;
			}
		}
		for(int i=0;i<maps.getComponentCount();i++) {
			if(!(i>=0&&i<5)) {//移動・閉じるボタン以外を動かす
				maps.getComponent(i).setLocation(maps.getComponent(i).getX()+x,maps.getComponent(i).getY()+y);
			}
		}
	}

	//プレイマップの画面遷移処理
	public void moveMaps(int x,int y) {
		String name;//if文が長すぎる為
		JLayeredPane play = playFrame.getLayeredPane();
		boolean tf;//進むか戻るか
		do {
			//移動
			if(x<0) {
				player.getNowMass().setValue(player.getNowMass().getX()+1,player.getNowMass().getY());
			}else if(x>0) {
				player.getNowMass().setValue(player.getNowMass().getX()-1,player.getNowMass().getY());
			}
			if(y<0) {
				player.getNowMass().setValue(player.getNowMass().getX(),player.getNowMass().getY()+1);
			}else if(y>0) {
				player.getNowMass().setValue(player.getNowMass().getX(),player.getNowMass().getY()-1);
			}
			if(!japan.contains(player.getNowMass().getX(),player.getNowMass().getY())) {//2マス開いている場合
				x*=2;
				y*=2;
			}
		}while(!japan.contains(player.getNowMass()));
		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}else if(!(name.equals("stop") || name.equals("start") || name.equals("右") || name.equals("左") || name.equals("下") || name.equals("上") ||
					name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()) || name.equals(player.getName()))) {//移動・閉じるボタン以外を動かす
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}


		//移動先が1つ前と同じか
		if(moveTrajectory.size()>1) {
			if(play.getComponentAt(400, 300).getName().equals(moveTrajectory.get(moveTrajectory.size()-2))) {//同じ場合、1つ前のmoveTrajectoryを削除
				moveTrajectory.remove(moveTrajectory.size()-1);
				player.setMove(player.getMove()+1);
				tf = false;
			}else {//違う場合、移動した先の座標をmoveTrajectoryに格納
				moveTrajectory.add(play.getComponentAt(400, 300).getName());
				player.setMove(player.getMove()-1);
				tf = true;
			}
		}else {
			moveTrajectory.add(play.getComponentAt(400, 300).getName());
			player.setMove(player.getMove()-1);
			tf = true;
		}
		poorgod.passingBonby(tf,players,turn);
		if(player.getMove()<=0) {
			moveTrajectory.clear();
			dice.clear();
			poorgod.clearBonbyBefore();
			if(!Card.usedRandomCard) {
				massEvent();
			}
		}
	}

	//プレイマップの画面遷移処理
	public void moveMaps(int player,Coordinates to) {
		JLayeredPane play = playFrame.getLayeredPane();
		int x=(to.getX()-players.get(player).getNowMass().getX())*130;
		int y=(to.getY()-players.get(player).getNowMass().getY())*130;
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()!=null && play.getComponent(i).getName().equals(players.get(player).getName())) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}
		players.get(player).getNowMass().setValue(to);
	}

	//移動履歴を保持
	private void addTrajectory() {
		moveTrajectory.add(playFrame.getLayeredPane().getComponentAt(400, 300).getName());
	}
////////////////////
	//次のプレイヤーをプレイ画面の真ん中に位置させる
	public void moveMaps() {
		JLayeredPane play = playFrame.getLayeredPane();
		System.out.println("colt coor:"+player.getColt().getX()+","+player.getColt().getY());
		int x = 401 - player.getColt().getX();
		int y = 301 - player.getColt().getY();
		String name;//if文が長すぎる為
		//移動
		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}else if(!(name.equals("start") || name.equals("stop") || name.equals("右") || name.equals("左") || name.equals("下") ||
					name.equals("上") ||name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()))) {//移動・閉じるボタン以外を動かす
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}
	}

	//プレイマップを表示
	private void playMap() {
		JLayeredPane play = playFrame.getLayeredPane();
		int distance=130;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!japan.contains(j, i))continue;
				if(japan.containsStation(j,i)) {
					JLabel pre = createText(j*distance-20,i*distance-5,80,60,15,japan.getStationName(j, i));
					pre.setBackground(Color.WHITE);
					play.add(pre,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
				}else {
					play.add(createMass(j,i,distance),JLayeredPane.DEFAULT_LAYER,0);
				}
				drawLine(playFrame.getLayeredPane(),j,i,distance,20);
			}
		}
	}

	//詳細マップを表示
	private void miniMap() {
		JLayeredPane maps = mapFrame.getLayeredPane();
		int distance=70;
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		JButton right = createButton(730,250,50,40,10,"→");
		JButton left = createButton(10,250,50,40,10,"←");
		JButton top = createButton(380,10,50,40,10,"↑");
		JButton bottom = createButton(380,510,50,40,10,"↓");
		closeButton.setActionCommand("マップを閉じる");
		right.setBackground(Color.WHITE);
		left.setBackground(Color.WHITE);
		top.setBackground(Color.WHITE);
		bottom.setBackground(Color.WHITE);
		mapFrame.setSize(800, 600);
		mapFrame.setLayout(null);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.setBackground(Color.ORANGE);
		mapFrame.getContentPane().setBackground(Color.ORANGE);
		maps.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		maps.add(right,JLayeredPane.PALETTE_LAYER,0);
		maps.add(left,JLayeredPane.PALETTE_LAYER,0);
		maps.add(top,JLayeredPane.PALETTE_LAYER,0);
		maps.add(bottom,JLayeredPane.PALETTE_LAYER,0);
		JLabel p1 = createText(players.get(0).getNowMass().getX()*distance-15, players.get(0).getNowMass().getY()*distance-5, 20, 10, 10, "1");
		p1.setBackground(Color.BLACK);
		JLabel p2 = createText(players.get(1).getNowMass().getX()*distance+15, players.get(1).getNowMass().getY()*distance-5, 20, 10, 10, "2");
		p2.setBackground(Color.BLACK);
		JLabel p3 = createText(players.get(2).getNowMass().getX()*distance-15, players.get(2).getNowMass().getY()*distance+15, 20, 10, 10, "3");
		p3.setBackground(Color.BLACK);
		JLabel p4 = createText(players.get(3).getNowMass().getX()*distance+15, players.get(3).getNowMass().getY()*distance+15, 20, 10, 10, "4");
		p4.setBackground(Color.BLACK);
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!japan.contains(j, i))continue;
				if(japan.containsStation(j,i)) {//駅の座標が来たら
					int list=japan.getIndexOfStation(j, i);
					JButton button = createButton(j*distance-20,i*distance-5,60,30,8,japan.getStationName(japan.getStationCoor(list)));
					if(list==japan.getGoalIndex()) {
						button.setBackground(Color.MAGENTA);
					}
					maps.add(button,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
				}else {
					maps.add(createMass(j,i,distance),JLayeredPane.DEFAULT_LAYER,0);
				}
				drawLine(maps,j,i,distance,10);
			}
		}
		mapFrame.setVisible(true);
	}

	//全体マップを表示
	private void allMap() {
		JLayeredPane maps = mapFrame.getLayeredPane();
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		int distance=30;
		mapFrame.setSize(800, 600);
		mapFrame.setLayout(null);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.getContentPane().setBackground(Color.ORANGE);
		closeButton.setActionCommand("マップを閉じる");
		maps.add(closeButton);
		JLabel p1 = createText(players.get(0).getNowMass().getX()*distance-5, players.get(0).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "1");
		p1.setBackground(Color.BLACK);
		JLabel p2 = createText(players.get(1).getNowMass().getX()*distance+5, players.get(1).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "2");
		p2.setBackground(Color.BLACK);
		JLabel p3 = createText(players.get(2).getNowMass().getX()*distance-5, players.get(2).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "3");
		p3.setBackground(Color.BLACK);
		JLabel p4 = createText(players.get(3).getNowMass().getX()*distance+5, players.get(3).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "4");
		p4.setBackground(Color.BLACK);
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!japan.contains(j, i))continue;
				if(japan.containsStation(j,i)) {//駅の座標が来たら
					int list=japan.getIndexOfStation(j, i);
					JButton button=createButton(j*distance,i*distance,distance/3,distance/3,6,japan.getStationName(japan.getStationCoor(list)));
					if(list==japan.getGoalIndex()) {
						button.setBackground(Color.MAGENTA);
					}
					maps.add(button,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
				}else {
					maps.add(createMass(j,i,distance),JLayeredPane.DEFAULT_LAYER,0);
				}
				drawLine(mapFrame.getLayeredPane(),j,i,distance,5);
			}
		}
		mapFrame.setVisible(true);
	}

	//マップ画面を閉じる
	private void closeMaps() {
		mapFrame.setVisible(false);
		mapFrame.getLayeredPane().removeAll();
	}

	//線路を引く(Boxで代用)
	private void drawLine(JLayeredPane lines,int x,int y,int size,int somethig) {
		ArrayList<Coordinates> list = japan.getMovePossibles(x, y);
		for(Coordinates coor : list) {
			JPanel line = new JPanel();
			line.setBackground(Color.BLACK);
			if(x>coor.getX()) {
				line.setLocation(x*size+somethig-size, y*size+somethig);
				line.setSize(size,2);
			}else if(x<coor.getX()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(size,2);
			}else if(y>coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig-size);
				line.setSize(2,size);
			}else if(y<coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(2,size);
			}
			lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
		}
	}

	private void bonbyplayer() {
		//moveTrajectoryと全プレイヤーの位置が重なるごとにどっちかがbonbyフラグがONなら交代
		for(int i = 0;i<4;i++) {
			//System.out.println(players.get(i).isBonby());//bonbyフラグTEST用
		}
		if(player.isBonby()) {//＊＊＊＊
			poorgod.binboTurn();
			playFrame.setVisible(false);
			binboFrame = new JFrame();
			JLayeredPane binbo = binboFrame.getLayeredPane();
			binboFrame.setSize(800,600);
			binboFrame.setLocationRelativeTo(null);
			//ImageIcon icon =  new ImageIcon("./img/days_res.png");
			//icon.createImageIcon("./img/days_res.png",");
			JLabel text1=new JLabel();
			JLabel text2=new JLabel();
			//JLabel text3=new JLabel(icon);
			JButton closeButton = createButton(580,500,180,50,10,"閉じる");
			closeButton.setActionCommand("貧乏神イベントを閉じる");
			if(!player.isPlayer()) {
				closeButton.setEnabled(false);
			}
			binbo.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
			binboFrame.setName("ボンビーのターン");

			text1 = createText(10,10,600,100,20,"テストボンビー1");
			text2 = createText(10,110,600,100,20,"テストボンビー２");
			//text3 = createText(10,210,600,100,20,"テストボンビー3");

			text1.setHorizontalTextPosition(SwingConstants.LEFT);
			binbo.add(text1);
			text2.setHorizontalTextPosition(SwingConstants.LEFT);
			binbo.add(text2);
			//text3.setHorizontalTextPosition(SwingConstants.LEFT);
			//binbo.add(text3);
			//System.out.println(icon.getIconWidth());
			//System.out.println(icon.getIconHeight());
			binboFrame.setVisible(true);

			setCloseFrame(5);
		}else {
			bonbyTurnEndFlag = true;
		}

	}

	public void closeBinboEvent() {//＊＊＊＊
		binboFrame.setVisible(false);
		binboFrame.removeAll();
		playFrame.setVisible(true);
		poorgod.sutabBinboFinishTurn();
		bonbyTurnEndFlag = true;
	}

	//ゴール画面を表示
	private void goal() {
		JLayeredPane goal = goalFrame.getLayeredPane();
		int goalMoney;
		Random rand = new Random();
		playFrame.setVisible(false);
		goalFrame.setSize(500, 300);
		goalFrame.setLayout(null);
		goalFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(380,180,100,50,10,"閉じる");
		closeButton.setActionCommand("ゴール画面を閉じる");
		goalMoney=10000*year;
		goalMoney+=rand.nextInt(10000);
		goalMoney-=goalMoney%100;
		player.addMoney(goalMoney);
		JLabel label = createText(10,30,400,100,10,player.getName()+"さんには地元民から援助金として"+System.lineSeparator()+goalMoney/10000+"億"+goalMoney%10000+"万円が寄付されます。");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		goalFrame.setVisible(true);

		setCloseFrame(3);

		playFrame.getLayeredPane().getComponentAt(400, 300).setBackground(Color.WHITE);

		japan.changeGoal();

		setGoalColor();

		poorgod.binboPossessPlayer(this,players);
	}

	//ゴール画面を閉じる
	public void closeGoal() {
		goalFrame.setVisible(false);
		printPropertys(japan.getStationName(japan.getSaveGoal()));
	}

	//目的地の色付け
	private void setGoalColor() {
		JLayeredPane play = playFrame.getLayeredPane();
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()==null)continue;
			if(play.getComponent(i).getName().equals(japan.getGoalName())) {
				play.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}

	//自分の持ち物件一覧を閉じる
	private void closeTakeStations() {
		Random rand = new Random();
		sellStationFrame.setVisible(false);
		sellStationFrame.removeAll();
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//持ち物件を売却するための画面を表示
	private void printTakeStations() {
		playFrame.setVisible(false);
		int takeProCount=0;
		int i=0;
		sellStationFrame = new JFrame("売却");
		JLayeredPane sellStation = sellStationFrame.getLayeredPane();
		sellStation.add(createText(150,10,200,40,20,"物件名"));
		sellStation.add(createText(400,10,150,40,20,"値段"));
		sellStation.add(createText(550,10,100,40,20,"利益率"));
		sellStation.add(createText(650,10,100,40,20,"所有者"));
		for(Property property:player.getPropertys()) {
			takeProCount++;
			JButton sellButton = createButton(80,15+(takeProCount+1)*35,60,30,10,"売却");
			sellButton.setActionCommand(property.getName()+"s:"+i);

			sellStation.add(sellButton);
			int rate = property.getRate();//利益率(3段階)
			int pMoney = property.getAmount()/2;
			sellStation.add(createText(150,10+(i+1)*35,200,40,15,property.getName()));
			if(pMoney<10000) {
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney+"万円"));
			}else if(pMoney%10000==0){
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億円"));
			}else {//今登録している物件では呼ばれないかも
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億"+pMoney%10000+"万円"));
			}
			sellStation.add(createText(550,10+(i+1)*35,100,40,15,rate + "%"));
			sellStation.add(createText(650,10+(i+1)*35,100,40,15,property.getOwner()));
			i++;
		}
		sellStationFrame.setSize(800, 35*player.getPropertys().size()+150);

		sellStationFrame.setVisible(true);

		if(!player.isPlayer()) {
			player.sellPropertyCPU(this);
		}
	}

	//駅の物件情報を表示
	private void printPropertys(String name) {
		playFrame.setVisible(false);
		propertyFrame = new JFrame(name + "の物件情報");
		propertyFrame.setSize(800, 35*japan.getStaInPropertySize(name)+150);
		propertyFrame.setLayout(null);
		propertyFrame.setLocationRelativeTo(null);
		propertyFrame.setVisible(false);
		Container propertys = propertyFrame.getContentPane();
		JButton closeButton = createButton(580,35*japan.getStaInPropertySize(name)+50,180,50,10,"閉じる");
		closeButton.setActionCommand("物件情報を閉じる");

		propertys.add(createText(150,10,200,40,20,"物件名"));
		propertys.add(createText(400,10,150,40,20,"値段"));
		propertys.add(createText(550,10,100,40,20,"利益率"));
		propertys.add(createText(650,10,100,40,20,"所有者"));
		if(japan.getStation(name).isMono()) {
			JLabel label = createText(750,10,30,40,20,"独");
			label.setBackground(Color.RED);
			propertys.add(label);
		}
		for(int i=0;i<japan.getStaInPropertySize(name);i++) {
			String property = japan.getStaInProperty(name,i).getName();//名前
			String owner = japan.getStaInProperty(name,i).getOwner();//管理者
			int money = japan.getStaInProperty(name,i).getAmount();//購入金額
			JButton buyButton = createButton(20,15+(i+1)*35,80,30,10,"購入");
			if(mapFrame.isShowing() || japan.getStaInProperty(name,i).getLevel()>=2
					|| (!owner.equals("") && !owner.equals(player.getName())) || player.getMoney()<japan.getStaInProperty(name,i).getAmount()) {
				buyButton.setEnabled(false);
			}
			for(String already:japan.alreadys) {
				if(already.equals(japan.getStaInProperty(name,i).getName()+i)) {
					buyButton.setEnabled(false);
					//sellButton.setEnabled(false);
					break;
				}
			}
			buyButton.setActionCommand(name+"b:"+i);
			propertys.add(buyButton);
			int rate = japan.getStaInProperty(name,i).getRate();//利益率(3段階)
			propertys.add(createText(150,10+(i+1)*35,200,40,15,property));
			if(money<10000) {
				propertys.add(createText(400,10+(i+1)*35,150,40,15,money+"万円"));
			}else if(money%10000==0){
				propertys.add(createText(400,10+(i+1)*35,150,40,15,money/10000+"億円"));
			}else {//今登録している物件では呼ばれないかも
				propertys.add(createText(400,10+(i+1)*35,150,40,15,money/10000+"億"+money%10000+"万円"));
			}
			propertys.add(createText(550,10+(i+1)*35,100,40,15,rate + "%"));
			propertys.add(createText(650,10+(i+1)*35,100,40,15,owner));
		}
		propertys.add(closeButton);
		propertyFrame.setVisible(true);

		if(!player.isPlayer()) {
			player.buyPropertysCPU(name);
		}
		setCloseFrame(2);
	}

	//駅の物件情報を閉じる
	public void closePropertys() {
		Random rand = new Random();
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		if(!mapFrame.isShowing()) {
			playFrame.setVisible(true);
			if(rand.nextInt(100) < 3) {
				randomEvent();
			}else {
				turnEndFlag=true;
			}
		}
	}

	//物件購入・増築処理
	private void buyPropertys(String name, int index) {
		japan.buyPropertys(name, index, player);

		System.out.println(japan.getStaInProperty(name,index).getName()+"を購入"+"("+index+")");
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		printPropertys(name);
	}

	//物件売却処理
	public void sellPropertys(Property property) {
		japan.sellPropertys(property,player);

		System.out.println(property.getName()+"を売却");
		sellStationFrame.setVisible(false);
		sellStationFrame.removeAll();
		if(player.getPropertys().size()>0) {
			printTakeStations();
		}else {
			closeTakeStations();
		}
	}

	//プレイマップの中央位置を初期位置(大阪)に設定
	private void initMaps() {
		JLayeredPane maps = mapFrame.getLayeredPane();
		JLayeredPane play = playFrame.getLayeredPane();
		int x=-400;
		int y=-900;
		for(int i=0;i<maps.getComponentCount();i++) {
			if(!(i>=0&&i<6)) {//移動・閉じるボタン以外を動かす
				play=maps;
			}
		}
		for(int i=0;i<play.getComponentCount();i++) {
			if(!(i>=0&&i<6)) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}
	}

	//初期化
  	private void init(int playerCount) {
  		Card.init(this);
  		for(int i=0;i<4;i++) {
  			if(playerCount>i) {//プレイヤー
	  			players.put(i,new Player("player"+(i+1),1000,i,true));
	  		}else {//CPU
  				players.put(i,new Player("CPU"+(i+1-playerCount),1000,i,false));
  			}
  			players.get(i).setColt(createText(401,301,20,20,10,players.get(i).getName()));
  	  		players.get(i).getColt().setBackground(Color.BLACK);
  	  		players.get(i).getColt().setName(players.get(i).getName());
  	  		playFrame.getLayeredPane().add(players.get(i).getColt(),JLayeredPane.DEFAULT_LAYER,0);
  		}
  		player=players.get(0);
  		playRight = createButton(730,250,50,40,10,"→");//プレイマップでの移動ボタン
  		playLeft = createButton(10,250,50,40,10,"←");//プレイマップでの移動ボタン
  		playTop = createButton(380,40,50,40,10,"↑");//プレイマップでの移動ボタン
  		playBottom = createButton(380,510,50,40,10,"↓");//プレイマップでの移動ボタン
  		saikoro = createButton(650, 360, 90, 30,10, "サイコロ");//プレイマップでのサイコロボタン
  	    cardB = createButton(650, 400, 90, 30,10, "カード");//プレイマップでのカード一覧表示ボタン
  	    company = createButton(650, 440, 90, 30,10, "会社情報");//プレイマップでのプレイヤー情報一覧表示ボタン
  	    minimap = createButton(650, 480, 90, 30,10, "詳細マップ");//プレイマップでの詳細マップ表示ボタン
  	    allmap = createButton(650, 520, 90, 30,10, "全体マップ");//プレイマップでの全体マップ表示ボタン
  	    waitButton = createButton(10,520,60,30,10,"stop");
  	    waitButton.setEnabled(true);
  		initMaps();
  		createMoveButton();
  		japan.initGoal();
  		dice.init();
  		setGoalColor();

  		initMenu();
  	}

	//ボタンを押した時の操作
 	public void actionPerformed(ActionEvent act){
		String cmd = act.getActionCommand();
		//System.out.println(cmd);
		if(cmd.equals("start")) {
			waitButton.setText("stop");
			waitButton.setActionCommand("stop");
			Player.setStopFlag(false);
		}else if(cmd.equals("stop")) {
			waitButton.setText("start");
			waitButton.setActionCommand("start");
			Player.setStopFlag(true);
		}else if(cmd.equals("サイコロ")) {
			enableMenu();
			printDice();
		}else if(cmd.equals("カード")) {
			enableMenu();
			printCard();
		}else if(cmd.equals("会社情報")) {
			enableMenu();
			info();
		}else if(cmd.equals("詳細マップ")) {
			playFrame.setVisible(false);
			miniMap();
		}else if(cmd.equals("全体マップ")) {
			playFrame.setVisible(false);
			allMap();
		}else if(cmd.equals("回す")) {
			diceShuffle();
		}else if(cmd.equals("サイコロを閉じる")) {
			ableMenu();
			closeDice();
		}else if(cmd.equals("マップを閉じる")) {
			playFrame.setVisible(true);
			closeMaps();
		}else if(cmd.equals("物件情報を閉じる")) {
			ableMenu();
			closePropertys();
		}else if(cmd.equals("会社情報を閉じる")) {
			ableMenu();
			closeInfo();
		}else if(cmd.equals("所持カード一覧を閉じる")) {
			ableMenu();
			closeCard();
		}else if(cmd.equals("ポップアップを閉じる")) {
			closePopUp();
		}else if(cmd.equals("決算画面を閉じる")) {
			closingEndFlag=true;//closeメソッドを用意した方がCPUでの操作がしやすくなる
		}else if(cmd.equals("ゴール画面を閉じる")) {
			closeGoal();
		}else if(cmd.equals("randomイベントを閉じる")) {
			closeRandomEvent();
		}else if(cmd.equals("臨時収入画面を閉じる")) {
			random2EndFlag=true;//closeメソッドを用意した方がCPUでの操作がしやすくなる
		}else if(cmd.equals("店を出る")) {
			closeShop();
		}else if(cmd.equals("カード購入を終える") || cmd.equals("カード売却を終える")) {
			backShop();
		}else if(cmd.equals("カードを買う")) {
			printBuyShop();
		}else if(cmd.equals("カードを売る")) {
			printSellShop();
		}else if(cmd.equals("右") || cmd.equals("左") || cmd.equals("上")  || cmd.equals("下")) {
			if(cmd.equals("右")) {
				moveMaps(-130,0);
			}else if(cmd.equals("左")) {
				moveMaps(130,0);
			}else if(cmd.equals("上")) {
				moveMaps(0,130);
			}else if(cmd.equals("下")) {
				moveMaps(0,-130);
			}
			printMoveButton();
			reloadInfo();
		}else if(cmd.equals("→") || cmd.equals("←") || cmd.equals("↑") || cmd.equals("↓")) {
			moveMaps(cmd);
		}else if(cmd.equals("貧乏神イベントを閉じる")) {
			closeBinboEvent();
		}
		for(Station sta:japan.getStations()) {
			if(cmd.equals(sta.getName())) {
				printPropertys(cmd);
			}
		}

		for(int i=0;i<Card.cardList.size();i++) {
			if(cmd.equals(Card.cardList.get(i).getName())) {//カードを使う
				Card.cardList.get(i).useAbility(this,dice,players,turn);
				if(Card.cardList.get(i).getID()==2){
					moveMaps();
					try {
						Thread.sleep(2000);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				ableMenu();
				closeCard();
				break;
			}else if(cmd.equals(Card.cardList.get(i).getName()+"t")) {//カードを捨てる
				player.getCards().remove(Card.cardList.get(i));
				Window.throwFlag=true;
				errorFrame.setVisible(false);
				playFrame.setVisible(true);
				break;
			}else if(cmd.equals(Card.cardList.get(i).getName()+"d")) {//カードを複製
				player.getCards().add(Card.cardList.get(i));
				closeDubbing();
				break;
			}
		}

		if(Card.usedRandomCard || Card.usedOthersCard) {
			Card.resetUsedCard();
			Card.resetUsedFixedCard();
			Card.resetUsedRandomCard();
			Card.resetUsedOthersCard();
			ableMenu();
			if(playFrame.isVisible()) {
				turnEndFlag=true;
			}
		}

		String pre[] = cmd.split(":");
		if(pre.length==2) {
			for(Card card:player.getCards()) {
				if(pre[0].equals(card.getName()) && pre[1].equals("s")) {//カード売却
					sellCard(card);
					break;
				}
			}
			for(Card card:Card.cardList) {
				if(pre[0].equals(card.getName()) && pre[1].equals("b")) {//カード購入
					buyCard(card);
					break;
				}
			}
			for(int i=0;i<japan.stationSize();i++) {
				if(pre[0].equals(japan.getStationName(japan.getStationCoor(i))+"b")) {//物件を購入
					buyPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
					break;
				}
			}
			for(Property property:player.getPropertys()) {
				if(pre[0].equals(property.getName()+"s")) {//物件を売却
					sellPropertys(property);
					break;
				}
			}
		}
	}
}

//id=0→randomイベントを閉じる,id=1→店フレームを閉じる,id=2→物件購入を閉じる,id=3→ゴール画面を閉じる
class CPUTimerTask extends TimerTask{
	private Window window;
	private int id;
	public CPUTimerTask(Window window,int id) {
		this.id=id;
		this.window=window;
	}
	@Override
	public void run() {
		switch(id) {
		case 0:
			window.closeRandomEvent();
			break;
		case 1:
			window.closeShop();
			break;
		case 2:
			window.closePropertys();
			break;
		case 3:
			window.closeGoal();
			break;
		case 4:
			window.closeDubbing();
			break;
		case 5:
			window.closeBinboEvent();
			break;
		default:
			break;
		}
	}

	public int getID() {
		return this.id;
	}
}

