/*
 * <todo>
 * CPUの実装
 * ボンビー処理
 * 決算処理(恐らく未完成)
 *
 *設定でマップをrandomに変更できる
 *	→双方向連結に実装したマップであればスレッドを用いてマップをランダムに構築することが可能
 *	→ランダムに駅を選定、選んだ駅の結合する方向の数をランダムに決め、その方向に繋げる駅をrandomに選定！を繰り返す
 *
 *メインウィンドウ以外の×ボタンを消す（消せたら）
 *
 *ぶっとびカードを使った後少し画面を停止したい。(どこに移動したか分かるようにしたい)
 *
 *web開発を行いたいと思っている人がいるのであれば、このゲームをweb上で動かせるような環境構築を行う
 *
 *現在、randomイベントはマスにとまった後に発生するものだが、"どこかの飲食店の売り上げが上がったのでお金がもらえる"等の
 *イベントがターンの初めに起きるrandomイベントを追加したい
 *
 *フォルダ構成を改善する
 *
 *グローバル変数を減らす
 *
 *カードが最大所持数を超えた場合ターン終了の関係で正しいプレイヤーのカードが表示されないかもしれない
 *
 *確認ポップアップを作る
 *
 *最寄り駅探索で双方向連結マップを使用する
 *
 *最寄りのカードショップの探索機構を作成する。
 *
 *独占解除処理を実装する
 *
 *・CPUがすること
 *1)サイコロを回す
 *		→カードを使わない場合サイコロを回す
 *2)適切なタイミングでカードを使う
 *		→移動系のカードは目的地までの距離で決める
 *3)CPUのレベルに応じて確率を操作する？
 *4)借金時に適切に物件売却をする
 *		→増築していない物件の内、利益率の低い物件で借金返済に最小限の金額の物件から順に売る
 *5)店でカードを買う(売るはいらないかな？)
 *		→目的地まで遠い時は移動系を買うとか？
 *6)駅で物件を買う
 *		→安い物件から順番に買う
 *
 */

package lifegame.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Window implements ActionListener{
	private JFrame playFrame = new JFrame("桃大郎電鉄");//メインフレーム
	private JButton playRight = createButton(730,250,50,40,10,"→");//プレイマップでの移動ボタン
	private JButton playLeft = createButton(10,250,50,40,10,"←");//プレイマップでの移動ボタン
	private JButton playTop = createButton(380,40,50,40,10,"↑");//プレイマップでの移動ボタン
	private JButton playBottom = createButton(380,510,50,40,10,"↓");//プレイマップでの移動ボタン
	private JButton saikoro = createButton(650, 360, 90, 30,10, "サイコロ");//プレイマップでのサイコロボタン
    private JButton cardB = createButton(650, 400, 90, 30,10, "カード");//プレイマップでのカード一覧表示ボタン
    private JButton company = createButton(650, 440, 90, 30,10, "会社情報");//プレイマップでのプレイヤー情報一覧表示ボタン
    private JButton minimap = createButton(650, 480, 90, 30,10, "詳細マップ");//プレイマップでの詳細マップ表示ボタン
    private JButton allmap = createButton(650, 520, 90, 30,10, "全体マップ");//プレイマップでの全体マップ表示ボタン
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
	private JFrame sellPrefectureFrame;//物件売却用フレーム
	private JFrame randomFrame;//randomイベント用フレーム
	private JFrame shopFrontFrame;//カードshopイベント用フレーム
	private JFrame shopFrame;//カードshop購買イベント用フレーム

	private Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報
	public static Boolean turnEndFlag=false,closingEndFlag=false,shoppingEndFlag=false;//ターンを交代するためのフラグ
	private int turn=0;//現在のターン
	private Dice dice = new Dice();//サイコロ処理
	public Japan japan = new Japan();//物件やマス情報
	private ArrayList<String> moveTrajectory = new ArrayList<String>();//プレイヤーの移動の軌跡
	private ArrayList<Integer[]> allProfitList = new ArrayList<Integer[]>();//各プレイヤーの総収益(過去も含む)
	private ArrayList<Integer[]> allAssetsList = new ArrayList<Integer[]>();//各プレイヤーの総資産(過去も含む)
	private Map<String,ArrayList<Integer>> moneyTrajectory = new HashMap<String,ArrayList<Integer>>();//プレイヤーのお金の増減の軌跡
	private int year=0;//今の年
	private int month=4;//今の月
	private int maxProfit=100;//最高収益(グラフ作成用)
	private int minProfit=0;//最低収益(グラフ作成用)
	private int maxAssets=100;//最高資産(グラフ作成用)
	private int minAssets=0;//最低資産(グラフ作成用)
	private ArrayList<String> alreadys = new ArrayList<String>();//そのターンに購入した物件リスト(連続購入を防ぐため)
	private int saveGoal;//ゴール保存用
	public static int count;//目的のマスまでの最短距離
	public static long time;//マルチスレッド開始からの経過時間
	private Map<Integer,ArrayList<ArrayList<Coordinates>>> trajectoryList = new HashMap<Integer,ArrayList<ArrayList<Coordinates>>>();//移動の軌跡
	private ArrayList<Coordinates> nearestStationList = new ArrayList<Coordinates>();//最寄り駅のリスト(複数存在する場合、その中からランダムに選択)
	private ArrayList<Card> canBuyCardlist = new ArrayList<Card>();//店の購入可能カードリスト

	public Window(int endYear){
		int w = 800, h = 600;
		playFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
        // ウィンドウのサイズ・初期位置
        playFrame.setSize(w, h);
        playFrame.setLocationRelativeTo(null);//windowsの画面の中央に表示
        playFrame.setLayout(null);

        // 背景色追加
        playFrame.getContentPane().setBackground(Color.ORANGE);

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

    	playMap();
    	init();

    	// ウィンドウを表示
        playFrame.setVisible(true);

        try {
        	play(endYear);
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
	}

	//メイン画面の上に書いてあるプレイヤーの情報を更新
	public void reload() {
		mainInfo.setVisible(false);
		if(players.get(turn).isEffect()){
			if(players.get(turn).getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス　効果発動中("+players.get(turn).getBuff().effect+")");
			}else if(players.get(turn).getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()/10000+"億円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス　効果発動中("+players.get(turn).getBuff().effect+")");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()/10000+"億　"+players.get(turn).getMoney()%10000+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス　効果発動中("+players.get(turn).getBuff().effect+")");
			}
		}else {
			if(players.get(turn).getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス");
			}else if(players.get(turn).getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()/10000+"億円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()/10000+"億　"+players.get(turn).getMoney()%10000+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス");
			}
		}
		mainInfo.setVisible(true);
	}

	private void initMenu() {
		if(players.get(turn).isEffect()){
			mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count+"マス　効果発動中("+players.get(turn).getBuff().effect+")");
		}else {
			mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+players.get(turn).getName()+"　持ち金："+players.get(turn).getMoney()+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count+"マス");
		}
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(players.get(turn).getName()+players.get(turn).getMoney());
		playFrame.getLayeredPane().add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
	}

  	//プレイ中の動作
	private void play(int endYear) throws InterruptedException{
    	Boolean flag=true;
    	playFrame.getLayeredPane().add(new JLabel());
    	moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+players.get(turn).getMove()+"　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count);
    	moveLabel.setName("moves");
    	playFrame.setBackground(Color.ORANGE);
    	closeMoveButton();
    	while(true) {
    		if(flag)printMonthFrame(month);
    		if(month==4 && turn==0) {
    			if(!flag) {
    				closing();
    			}
    			flag=false;
				year++;
			}
    		if(year>endYear)break;
    		searchShortestRoute();
    		Thread search = new Thread(new WaitThread(2));
    		search.start();
    		search.join();
    		saveGoal=japan.goal;
    		returnMaps();//画面遷移が少し遅い
    		reload();
    		Thread turnEnd = new Thread(new WaitThread(0));
    		turnEnd.start();
    		turnEnd.join();
    		Thread.sleep(1000);
    		turnEndFlag=false;
    		alreadys.clear();
    		printMenu();
    		if(turn==3) {
    			ArrayList<Integer> moneyList = new ArrayList<Integer>();
    			for(int i=0;i<4;i++) {
    				moneyList.add(players.get(i).getMoney());
    			}
    			moneyTrajectory.put(year+"年"+month+"月", moneyList);
    			month++;
    			printMonthFrame(month);
    			if(month==13) {
    				month=1;
    			}
    			turn=0;
    		}else {
    			turn++;
    		}
    	}
    	System.out.println("終わり");
		System.exit(0);
    }

	//最寄り駅を探索
	private void searchNearestStation() {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		StationSearchThread.savecount=0;
		Thread t = new Thread();
		trajectoryList.clear();
		if(japan.prefectureContains(players.get(turn).getNowMass())){
			StationSearchThread thread = new StationSearchThread(this);
			thread.moveTrajectory.add(new Coordinates(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()));
			synchronized(StationSearchThread.lock3) {
				thread.setMass(players.get(turn).getNowMass().getX(), players.get(turn).getNowMass().getY());
			}
			t = new Thread(thread);
			t.start();
		}else {
			//探索すべき方角の数を数える
			ArrayList<Coordinates> list = japan.getMovePossibles(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY());
			for(Coordinates coor:list) {
				//Threadを立ち上げる
				StationSearchThread thread = new StationSearchThread(this);
				thread.moveTrajectory.add(new Coordinates(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()));
				synchronized(StationSearchThread.lock3) {
					thread.setMass(coor.getX(), coor.getY());
				}
				t = new Thread(thread);
				t.start();
			}
		}

		System.out.println("OK");
	}

	//最寄り駅の探索結果を格納
	public synchronized void setNearestStationResult(int count, Coordinates nearestStation) {
		if(Window.count>=count) {
			System.out.println("name:"+japan.prefectureMapping.get(japan.prefectures.get(japan.getIndexOfPrefecture(nearestStation.getX(), nearestStation.getY())))+"  x:"+nearestStation.getX()+"   y:"+nearestStation.getY());
			Window.count=count;
			boolean flag=true;
			for(Coordinates coor:nearestStationList) {
				if(coor.contains(nearestStation)) {
					flag=false;
				}
			}
			if(flag) {
				this.nearestStationList.add(nearestStation);
			}
		}
	}

	//目的地までの最短距離を計算し、最短ルートを取得
	private void searchShortestRoute() {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		MultiThread.savecount=0;
		Thread t = new Thread();
		trajectoryList.clear();
		//探索すべき方角の数を数える
		ArrayList<Coordinates> list = japan.getMovePossibles(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY());
		for(Coordinates coor:list) {
			//Threadを立ち上げる
			MultiThread thread = new MultiThread(this);
			thread.moveTrajectory.add(new Coordinates(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()));
			synchronized(MultiThread.lock3) {
				thread.setMass(coor.getX(), coor.getY());
			}
			t = new Thread(thread);
			t.start();
		}

	}

	//目的地までの最短距離と最短ルートを格納
	public synchronized void setSearchResult(int count, ArrayList<Coordinates> trajectory) {
		if(Window.count>=count) {
			Window.count=count;
			if(!this.trajectoryList.containsKey(count)) {
				this.trajectoryList.put(count,new ArrayList<ArrayList<Coordinates>>());
			}
			this.trajectoryList.get(count).add(trajectory);
		}
	}

	//マスに到着した時のマスのイベント処理
	private void massEvent() {
		closeMoveButton();
		String massName = playFrame.getLayeredPane().getComponentAt(400, 300).getName();
		if(massName.substring(0, 1).equals("青")) {
			blueEvent();
		}else if(massName.substring(0, 1).equals("赤")) {
			redEvent();
		}else if(massName.substring(0, 1).equals("黄")) {
			yellowEvent();
		}else if(massName.substring(0, 1).equals("店")) {
			shopEvent();
		}else{
			if(japan.prefectureMapping.get(japan.prefectures.get(japan.goal)).equals(massName)) {
				//ゴール処理
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
		players.get(turn).addMoney(result);
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
		players.get(turn).addMoney(-result);
		if(players.get(turn).getMoney() < 0 && players.get(turn).getPropertys().size() > 0) {
			printTakePrefectures();
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
			System.out.println("candidate card, name:"+Card.cardList.get(index).getName()+"  rarity"+Card.cardList.get(index).getRarity());
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
		players.get(turn).addCard(Card.cardList.get(index));
		if(players.get(turn).getCards().size()>8) {
			cardFull();
		}
		System.out.println("Card Get! name:"+Card.cardList.get(index).getName()+"  rarity"+Card.cardList.get(index).getRarity());
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//所持カードが最大を超えた場合、捨てるカードを選択
	private void cardFull() {
		errorFrame.setSize(400,500);
		errorFrame.setLocationRelativeTo(null);
		errorFrame.setLayout(null);
		JLayeredPane error = errorFrame.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<players.get(turn).getCards().size();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,players.get(turn).getCards().get(i).getName());
        	label.setBackground(Color.LIGHT_GRAY);
        	error.add(label);
        	throwButton.setActionCommand(players.get(turn).getCards().get(i).getName()+"t");
        	error.add(throwButton);
        }
		error.add(titleName);

		playFrame.setVisible(false);
		errorFrame.setVisible(true);
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
		if(players.get(turn).getCards().size()==0) {
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
	}

	//店用フレームを閉じる
	private void closeShop() {
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
		JLabel myMoney = createText(10,5,400,40,10,"所持金"+players.get(turn).getMoney());
		shopBuy.add(myMoney);

		for(int i=1; i<=canBuyCardlist.size(); i++) {
			JButton buyButton = createButton(500,i*50,70,50,10,"購入");
			buyButton.setActionCommand(canBuyCardlist.get(i-1).getName()+":b");
			if(canBuyCardlist.get(i-1).getBuyPrice() > players.get(turn).getMoney() || players.get(turn).getCards().size() > 7) {
				buyButton.setEnabled(false);
			}
			shopBuy.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,canBuyCardlist.get(i-1).getName());
			shopBuy.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(canBuyCardlist.get(i-1).getBuyPrice()));
			shopBuy.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}

		if(players.get(turn).getCards().size() > 7) {
			JLabel cardFull = createText(450,5,130,40,10,"カードがいっぱいです");
			shopBuy.add(cardFull);
		}

		shopFrame.setVisible(true);
	}

	private void buyCard(Card card) {
		players.get(turn).buyCard(card);
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
		for(int i=1; i<=players.get(turn).getCards().size(); i++) {
			JButton sellButton = createButton(500,i*50,70,50,10,"売却");
			sellButton.setActionCommand(players.get(turn).getCards().get(i-1).getName()+":s");
			shopSell.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,players.get(turn).getCards().get(i-1).getName());
			shopSell.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(players.get(turn).getCards().get(i-1).getSellPrice()));
			shopSell.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}
		shopFrame.setVisible(true);
	}

	private void sellCard(Card card) {
		players.get(turn).sellCard(card);
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		printSellShop();
	}

	//カードショップの売買画面を閉じる
	private void backShop() {
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		if(players.get(turn).getCards().size()>0) {
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
		random.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		double randomNum = rand.nextDouble();
		if(randomNum < 0.1) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を1/4失った");
			players.get(turn).addMoney(-players.get(turn).getMoney()/4);
		}else if(randomNum < 0.2) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を半分失った");
			players.get(turn).addMoney(-players.get(turn).getMoney()/2);
		}else if(randomNum < 0.3) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を全て失った");
			players.get(turn).addMoney(-players.get(turn).getMoney());
		}else if(randomNum < 0.4) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"1億円もらった");
			players.get(turn).addMoney(10000);
		}else if(randomNum < 0.5) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"2億円もらった");
			players.get(turn).addMoney(20000);
		}else if(randomNum < 0.6) {
			randomFrame.setName("しあわせの小鳥");
			text1 = createText(10,10,600,100,20,"ちゅんちゅんちゅん");
			text2 = createText(10,110,600,100,20,"ちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅん");
			text3 = createText(10,210,600,100,20,"ちゅんちゅんちゅん(一頭地を抜くカードをもらった)");
			for(int i=0;i<Card.cardList.size();i++) {
				if(Card.cardList.get(i).contains("一頭地を抜くカード")) {
					players.get(turn).addCard(Card.cardList.get(i));
				}
			}
		}else if(randomNum < 0.7) {
			randomFrame.setName("鋼鉄の人");
			text1 = createText(10,10,600,100,20,"全身赤い装甲に身を包んだアメリカの空飛ぶ天才発明家が現れた！");
			text2 = createText(10,110,600,100,20,"Destroying properties at random！Don't hold a grudge.");
			//誰かの物件の所有権を初期化
			for(int i=0;i<japan.property.size();i++) {
				if(japan.property.get(i).getOwner() != null && !japan.property.get(i).getOwner().equals("")) {
					text3 = createText(10,210,600,100,20,"誰かの物件が破壊された(" + japan.property.get(i).getOwner() + "の" + japan.property.get(i).getName() + ")");
					japan.property.get(i).setOwner("");
					if(isMonopoly(japan.property.get(i))) {
						for(String preName:japan.prefectureNameList) {
							if(japan.prefectureInfo.get(preName).contains(japan.property.get(i))) {
								monopoly(japan.prefectureInfo.get(preName));
							}
						}
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
				players.get(turn).addMoney(players.get(turn).getMoney());
			}else {
				text3 = createText(10,210,600,100,20,"事業が失敗し、所持金が無くなります");
				players.get(turn).addMoney(-players.get(turn).getMoney());
			}
		}else if(randomNum < 0.9) {
			randomFrame.setName("スキャンダル");
			text1 = createText(10,10,600,100,20,"若者とキャッキャウフフしていたのがばれた");
			text2 = createText(10,110,600,100,20,"世間体を気にして移動を自粛することにした");
			if(players.get(turn).isEffect()) {
				text3 = createText(10,210,600,100,20,"移動距離が-3される");
			}else {
				text3 = createText(10,210,600,100,20,"3カ月の間、移動距離が-3される");
			}
			//3か月間移動距離を制限する
			players.get(turn).getBuff().addBuff(-3, 3);
		}else {
			randomFrame.setName("富士山");
			text1 = createText(10,10,600,100,20,players.get(turn).getName()+"「富士山に行きたい！！！」");
			text2 = createText(10,110,600,100,20,"富士山の登頂に成功し、気分が良くなった");
			text3 = createText(10,210,600,100,20,"登山費用として5000万円失った");
			players.get(turn).addMoney(-5000);
		}
		text1.setHorizontalTextPosition(SwingConstants.LEFT);//左に寄せたいができない
		random.add(text1);
		text2.setHorizontalTextPosition(SwingConstants.LEFT);
		random.add(text2);
		text3.setHorizontalTextPosition(SwingConstants.LEFT);
		random.add(text3);

		randomFrame.setVisible(true);
	}

	private void closeRandomEvent() {
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
	}

	//決算処理
	private void closing() {
		JFrame closingFrame = new JFrame("決算");
		JLayeredPane closing = closingFrame.getLayeredPane();
		closingFrame.setSize(800, 600);
		closingFrame.setLocationRelativeTo(null);
		closing.add(createText(10,10,300,200,100,"決算"));
		closingFrame.setVisible(true);
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {

		}
		closingFrame.setVisible(false);
		closing.removeAll();
		closingFrame.setLayout(null);
		addProfit();
		aggregateProfit();

		//ここから収益
		JLabel profitLabel = createText(10,10,370,40,15,"今までの収益の推移");
		profitLabel.setBackground(Color.BLUE);
		closing.add(profitLabel);
		//グラフ作成(左半分)
		for(int i=0;i<allProfitList.size();i++) {
			for(int j=0;j<4;j++) {
				//各プレイヤーの収益の値をグラフに出力
				JPanel graph = new JPanel();
				graph.setBounds(400*((i+1)/allProfitList.size()),600-(500*allProfitList.get(i)[j]/(maxProfit-minProfit)),5,5);
				graph.setBackground(Color.YELLOW);
				closing.add(graph);
				System.out.println("x:" + String.valueOf(400*((i+1)/allProfitList.size())) + "   y:" + String.valueOf(600-(500*allProfitList.get(i)[j]/(maxProfit-minProfit))));
				if(i==allProfitList.size()-1) {//ぬるぽ回避
					continue;
				}
				//次の年の自分の収益までの線を引く
				for(int y=600-(500*allProfitList.get(i)[j]/(maxProfit-minProfit));y<600-(500*allProfitList.get(i+1)[j]/(maxProfit-minProfit));y+=4) {
					for(int x=400*((i+1)/allProfitList.size());x<400*((i+2)/allProfitList.size());x+=4) {
						JPanel line = new JPanel();
						line.setBackground(Color.YELLOW);
						line.setLocation(x, y);
						line.setSize(2,2);
						closing.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}

		//グラフの具体的な数値(右半部)
		JLabel profitThisYearLabel = createText(400,10,370,40,15,"今年の収益");
		profitThisYearLabel.setBackground(Color.BLUE);
		closing.add(profitThisYearLabel);

		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).getName());
			playerNameLabel.setBackground(Color.BLUE);
			closing.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerProfitLabel = createText(400,110+(100*i),100,40,10,String.valueOf(allProfitList.get(allProfitList.size()-1)[i]));
			playerProfitLabel.setBackground(Color.BLUE);
			closing.add(playerProfitLabel,JLayeredPane.DEFAULT_LAYER,0);
		}

		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		closeButton.setActionCommand("決算画面を閉じる");
		closing.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		closingFrame.setVisible(true);
		Thread thread = new Thread(new WaitThread(3));
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		closingEndFlag=false;
		closingFrame.setVisible(false);
		closing.removeAll();
		//ここまで収益

		//ここから総資産
		JLabel assetsLabel = createText(10,10,370,40,15,"今までの総資産の推移");
		assetsLabel.setBackground(Color.BLUE);
		closing.add(assetsLabel);
		aggregateAssets();

		//グラフ作成(左半分)
		for(int i=0;i<allAssetsList.size();i++) {
			for(int j=0;j<4;j++) {
				//各プレイヤーの総資産の値をグラフに出力
				JPanel graph = new JPanel();
				graph.setBounds(400*((i+1)/allAssetsList.size()),600-(500*allAssetsList.get(i)[j]/(maxAssets-minAssets)),5,5);
				graph.setBackground(Color.YELLOW);
				closing.add(graph);
				System.out.println("x:" + String.valueOf(400*((i+1)/allAssetsList.size())) + "   y:" + String.valueOf(600-(500*allAssetsList.get(i)[j]/(maxAssets-minAssets))));
				if(i==allAssetsList.size()-1) {//ぬるぽ回避
					continue;
				}
				//次の年の自分の総資産までの線を引く
				for(int y=600-(500*allAssetsList.get(i)[j]/(maxAssets-minAssets));y<600-(500*allAssetsList.get(i+1)[j]/(maxAssets-minAssets));y+=4) {
					for(int x=400*((i+1)/allAssetsList.size());x<400*((i+2)/allAssetsList.size());x+=4) {
						JPanel line = new JPanel();
						line.setBackground(Color.YELLOW);
						line.setLocation(x, y);
						line.setSize(2,2);
						closing.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}

		//グラフの具体的な数値(右半部)
		JLabel assetsThisYearLabel = createText(400,10,370,40,15,"今年の総資産");
		assetsThisYearLabel.setBackground(Color.BLUE);
		closing.add(assetsThisYearLabel);

		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).getName());
			playerNameLabel.setBackground(Color.BLUE);
			closing.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerAssetsLabel = createText(400,110+(100*i),100,40,10,String.valueOf(allAssetsList.get(allAssetsList.size()-1)[i]));
			playerAssetsLabel.setBackground(Color.BLUE);
			closing.add(playerAssetsLabel,JLayeredPane.DEFAULT_LAYER,0);
		}

		closing.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		closingFrame.setVisible(true);
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		closingEndFlag=false;
		closingFrame.setVisible(false);
		//ここまで総資産

	}

	//収益を加算
	private void addProfit() {
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			players.get(i).addProfit();
		}
	}

	//収益を集計
	private void aggregateProfit() {
		Integer profitList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			for(Property property:players.get(i).getPropertys()) {
				profitList[i]+=property.getProfit();
			}
			if(maxProfit<profitList[i]) {
				maxProfit=profitList[i];
			}
			if(minProfit>profitList[i]) {
				minProfit=profitList[i];
			}
		}
		allProfitList.add(profitList);
	}

	//総資産を集計
	private void aggregateAssets() {
		Integer assetsList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			assetsList[i]+=players.get(i).getMoney();
			for(Property property : players.get(i).getPropertys()) {
				assetsList[i]+=property.getAmount();
			}
			if(maxAssets<assetsList[i]) {
				maxAssets=assetsList[i];
			}
			if(minAssets>assetsList[i]) {
				minAssets=assetsList[i];
			}
		}
		allAssetsList.add(assetsList);
	}

	//駅以外のマスを作成
	private JPanel createMass(int j,int i,int distance) {
		JPanel mass = new JPanel();
		if(japan.blueContains(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.BLUE);
			mass.setName("青"+japan.getIndexOfBlue(j, i));
		}else if(japan.redContains(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.RED);
			mass.setName("赤"+japan.getIndexOfRed(j, i));
		}else if(japan.yellowContains(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.YELLOW);
			mass.setName("黄"+japan.getIndexOfYellow(j, i));
		}else if(japan.shopContains(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.GRAY);
			mass.setName("店"+japan.getIndexOfShop(j, i));
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
		return button;
	}

	//textを作成
	private JLabel createText(int x,int y,int w,int h,int size,String name) {
		JLabel text = new JLabel(name,SwingConstants.CENTER);
		text.setOpaque(true);
		//text.setBackground(Color.BLUE);
		text.setBounds(x, y, w, h);
		text.setFont(new Font("SansSerif", Font.ITALIC, size));
		text.setName(name);
		return text;
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
	private void closeMoveButton() {
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
		vector = japan.getVector(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY(),1);
		closeMoveButton();
		if(trajectoryList.containsKey(Window.count)) {
			for(ArrayList<Coordinates> list:trajectoryList.get(Window.count)) {
				for(Coordinates coor:list) {
					for(int i=0;i<4;i++) {
						if(coor.contains(players.get(turn).getNowMass().getX()-1,players.get(turn).getNowMass().getY())) {
							playLeft.setBackground(Color.MAGENTA);
						}else if(coor.contains(players.get(turn).getNowMass().getX()+1,players.get(turn).getNowMass().getY())) {
							playRight.setBackground(Color.MAGENTA);
						}else if(coor.contains(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()-1)) {
							playTop.setBackground(Color.MAGENTA);
						}else if(coor.contains(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()+1)) {
							playBottom.setBackground(Color.MAGENTA);
						}
					}
				}
			}
		}
		if(vector.get(0)) {
			playLeft.setVisible(true);
		}
		if(vector.get(1)) {
			playRight.setVisible(true);
		}
		if(vector.get(2)) {
			playTop.setVisible(true);
		}
		if(vector.get(3)) {
			playBottom.setVisible(true);
		}
		moveLabel.setText("残り移動可能マス数:"+players.get(turn).getMove()+"　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count);
		moveLabel.setVisible(true);
		playFrame.getLayeredPane().add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
		if(players.get(turn).getMove() <= 0) {
			closeMoveButton();
		}
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
	private void enableMenu() {
		saikoro.setEnabled(false);
		company.setEnabled(false);
		cardB.setEnabled(false);
		minimap.setEnabled(false);
		allmap.setEnabled(false);
	}

	//メイン画面でのメニューボタンを有効
	private void ableMenu() {
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

	//所持カード一覧を表示
	private void printCard() {
		JLayeredPane card = cardFrame.getLayeredPane();
		cardFrame.setSize(700, 500);
		cardFrame.setLayout(null);
        JButton closeButton = createButton(570,400,100,40,10,"戻る");
        closeButton.setActionCommand("所持カード一覧を閉じる");
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int i=0;i<players.get(turn).getCards().size();i++) {
        	JButton useButton = createButton(10,35*(i+1)+30,70,30,10,"使用");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(i+1)+30,180,30,10,players.get(turn).getCards().get(i).getName());
        	JLabel labelText = createText(300,35*(i+1)+30,350,30,10,players.get(turn).getCards().get(i).getText());
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(players.get(turn).getCard(i).getName());
        	if(players.get(turn).getCard(i).contains("ダビングカード") && players.get(turn).getCards().size()<2) {
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
	private void printDubbing() {
		JLayeredPane dubbing = dubbingCardFrame.getLayeredPane();
		dubbingCardFrame.setSize(700,500);
		dubbingCardFrame.setLayout(null);
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int roop=0;roop<players.get(turn).getCards().size();roop++) {
        	JButton useButton = createButton(10,35*(roop+1)+30,70,30,10,"複製");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(roop+1)+30,180,30,10,players.get(turn).getCards().get(roop).getName());
        	JLabel labelText = createText(300,35*(roop+1)+30,350,30,10,players.get(turn).getCards().get(roop).getText());
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(players.get(turn).getCards().get(roop).getName()+"d");
        	dubbing.add(labelName);
        	dubbing.add(labelText);
        	dubbing.add(useButton);
        }
        dubbing.add(titleName);
        dubbing.add(titleText);

        dubbingCardFrame.setVisible(true);
        playFrame.setVisible(false);
	}

	//カードの複製を行う画面を非表示
	private void closeDubbing() {
		dubbingCardFrame.setVisible(false);
		playFrame.setVisible(true);
		turnEndFlag=true;
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
	private void moveMaps(String cmd) {//今はボタンを入力できない状態にできないので、状態遷移できない状態にした。(ComponentからJButtomに変換できれば可能)
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
	private void moveMaps(int x,int y) {//今はボタンを入力できない状態にできないので、状態遷移できない状態にした。(ComponentからJButtomに変換できれば可能)
		String name;//if文が長すぎる為
		JLayeredPane play = playFrame.getLayeredPane();
		do {
			//移動
			if(x<0) {
				players.get(turn).getNowMass().setValue(players.get(turn).getNowMass().getX()+1,players.get(turn).getNowMass().getY());
			}else if(x>0) {
				players.get(turn).getNowMass().setValue(players.get(turn).getNowMass().getX()-1,players.get(turn).getNowMass().getY());
			}
			if(y<0) {
				players.get(turn).getNowMass().setValue(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()+1);
			}else if(y>0) {
				players.get(turn).getNowMass().setValue(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()-1);
			}
			if(!japan.contains(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY())) {//2マス開いている場合
				x*=2;
				y*=2;
			}
		}while(!japan.contains(players.get(turn).getNowMass().getX(),players.get(turn).getNowMass().getY()));
		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}else if(!(name.equals("右") || name.equals("左") || name.equals("下") || name.equals("上") ||
					name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()) || name.equals(players.get(turn).getName()))) {//移動・閉じるボタン以外を動かす
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}

		//移動先が1つ前と同じか
		if(moveTrajectory.size()>1) {
			if(play.getComponentAt(400, 300).getName().equals(moveTrajectory.get(moveTrajectory.size()-2))) {//同じ場合、1つ前のmoveTrajectoryを削除
				moveTrajectory.remove(moveTrajectory.size()-1);
				players.get(turn).setMove(players.get(turn).getMove()+1);
			}else {//違う場合、移動した先の座標をmoveTrajectoryに格納
				moveTrajectory.add(play.getComponentAt(400, 300).getName());
				players.get(turn).setMove(players.get(turn).getMove()-1);
			}
		}else {
			moveTrajectory.add(play.getComponentAt(400, 300).getName());
			players.get(turn).setMove(players.get(turn).getMove()-1);
		}
		if(players.get(turn).getMove()<=0) {
			moveTrajectory.clear();
			dice.clearResult();
			dice.clearNum();
			if(!Card.usedRandomCard) {
				massEvent();
			}
		}
	}

	//プレイマップの画面遷移処理
	private void moveMaps(int player,Coordinates to) {
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
	private void moveMaps() {
		moveTrajectory.add(playFrame.getLayeredPane().getComponentAt(400, 300).getName());
	}

	//次のプレイヤーをプレイ画面の真ん中に位置させる
	private void returnMaps() {
		JLayeredPane play = playFrame.getLayeredPane();
		int x = 401 - players.get(turn).getColt().getX();
		int y = 301 - players.get(turn).getColt().getY();
		String name;//if文が長すぎる為
		//移動
		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}else if(!(name.equals("右") || name.equals("左") || name.equals("下") || name.equals("上") ||
					name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
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
				if(japan.prefectureContains(j,i)) {
					JLabel pre = createText(j*distance-20,i*distance-5,80,60,15,japan.prefectureMapping.get(japan.prefectures.get(japan.getIndexOfPrefecture(j, i))));
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
		JLabel p1 = createText(players.get(0).getNowMass().getX()*distance-15, players.get(0).getNowMass().getY()*distance-5, 20, 10, 10, "p"+1);
		p1.setBackground(Color.BLACK);
		JLabel p2 = createText(players.get(1).getNowMass().getX()*distance+15, players.get(1).getNowMass().getY()*distance-5, 20, 10, 10, "p"+2);
		p2.setBackground(Color.BLACK);
		JLabel p3 = createText(players.get(2).getNowMass().getX()*distance-15, players.get(2).getNowMass().getY()*distance+15, 20, 10, 10, "p"+3);
		p3.setBackground(Color.BLACK);
		JLabel p4 = createText(players.get(3).getNowMass().getX()*distance+15, players.get(3).getNowMass().getY()*distance+15, 20, 10, 10, "p"+4);
		p4.setBackground(Color.BLACK);
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!japan.contains(j, i))continue;
				if(japan.prefectureContains(j,i)) {//駅の座標が来たら
					int list=japan.getIndexOfPrefecture(j, i);
					JButton button = createButton(j*distance-20,i*distance-5,60,30,8,japan.prefectureMapping.get(japan.prefectures.get(list)));
					if(list==japan.goal) {
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
				if(japan.prefectureContains(j,i)) {//駅の座標が来たら
					int list=japan.getIndexOfPrefecture(j, i);
					JButton button=createButton(j*distance,i*distance,distance/3,distance/3,6,japan.prefectureMapping.get(japan.prefectures.get(list)));
					if(list==japan.goal) {
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


	/*
	//線路を引く
	private void drawLine(int x,int y,int size) {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();
		PaintCanvas canvas = new PaintCanvas();
		if(maps.contains(x, y)) {//指定された位置にコンポーネントが存在した場合
			vector = japan.getVector(x,y,size);
			if(vector!=null) {
				canvas.set(x, y);
				if(vector.get(0)) {
					System.out.println("0");
					canvas.clearSize();
					canvas.setSizeY(size);
					System.out.println("x: "+canvas.x+" y: "+canvas.y+" sizex: "+canvas.sizeX+" sizey: "+canvas.sizeY);

				}
				if(vector.get(1)) {
					System.out.println("1");
					canvas.clearSize();
					canvas.setSizeY(-size);
					System.out.println("x: "+canvas.x+" y: "+canvas.y+" sizex: "+canvas.sizeX+" sizey: "+canvas.sizeY);
				}
				if(vector.get(2)) {
					System.out.println("2");
					canvas.clearSize();
					canvas.setSizeX(-size);
					System.out.println("x: "+canvas.x+" y: "+canvas.y+" sizex: "+canvas.sizeX+" sizey: "+canvas.sizeY);
					canvas.repaint();
				}
				if(vector.get(3)) {
					System.out.println("3");
					canvas.clearSize();
					canvas.setSizeX(size);
					System.out.println("x: "+canvas.x+" y: "+canvas.y+" sizex: "+canvas.sizeX+" sizey: "+canvas.sizeY);
				}
			}
		}
		m.add(canvas);
	}
	*/

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
		System.out.println(goalMoney);
		players.get(turn).addMoney(goalMoney);
		JLabel label = createText(10,30,400,100,10,players.get(turn).getName()+"さんには地元民から援助金として"+System.lineSeparator()+goalMoney/10000+"億"+goalMoney%10000+"万円が寄付されます。");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		goalFrame.setVisible(true);

		playFrame.getLayeredPane().getComponentAt(400, 300).setBackground(Color.WHITE);

		japan.changeGoal();

		setGoalColor();
	}

	//ゴール画面を閉じる
	private void closeGoal() {
		goalFrame.setVisible(false);
		printPropertys(japan.prefectureMapping.get(japan.prefectures.get(saveGoal)));
	}

	//目的地の色付け
	private void setGoalColor() {
		JLayeredPane play = playFrame.getLayeredPane();
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()==null)continue;
			if(play.getComponent(i).getName().equals(japan.prefectureMapping.get(japan.prefectures.get(japan.goal)))) {
				play.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}

	//自分の持ち物件一覧を閉じる
	private void closeTakePrefectures() {
		Random rand = new Random();
		sellPrefectureFrame.setVisible(false);
		sellPrefectureFrame.removeAll();
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			turnEndFlag=true;
		}
	}

	//持ち物件を売却するための画面を表示
	private void printTakePrefectures() {
		playFrame.setVisible(false);
		int takeProCount=0;
		int i=0;
		sellPrefectureFrame = new JFrame("売却");
		JLayeredPane sellPrefecture = sellPrefectureFrame.getLayeredPane();
		sellPrefecture.add(createText(150,10,200,40,20,"物件名"));
		sellPrefecture.add(createText(400,10,150,40,20,"値段"));
		sellPrefecture.add(createText(550,10,100,40,20,"利益率"));
		sellPrefecture.add(createText(650,10,100,40,20,"所有者"));
		for(Property property:players.get(turn).getPropertys()) {
			takeProCount++;
			JButton sellButton = createButton(80,15+(takeProCount+1)*35,60,30,10,"売却");
			sellButton.setActionCommand(property.getName()+"s:"+i);

			sellPrefecture.add(sellButton);
			int rate = property.getRate();//利益率(3段階)
			int pMoney = property.getAmount()/2;
			sellPrefecture.add(createText(150,10+(i+1)*35,200,40,15,property.getName()));
			if(pMoney<10000) {
				sellPrefecture.add(createText(400,10+(i+1)*35,150,40,15,pMoney+"万円"));
			}else if(pMoney%10000==0){
				sellPrefecture.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億円"));
			}else {//今登録している物件では呼ばれないかも
				sellPrefecture.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億"+pMoney%10000+"万円"));
			}
			sellPrefecture.add(createText(550,10+(i+1)*35,100,40,15,rate + "%"));
			sellPrefecture.add(createText(650,10+(i+1)*35,100,40,15,property.getOwner()));
			i++;
		}
		sellPrefectureFrame.setSize(800, 35*players.get(turn).getPropertys().size()+150);

		sellPrefectureFrame.setVisible(true);
	}

	//駅の物件情報を表示
	private void printPropertys(String name) {
		playFrame.setVisible(false);
		propertyFrame = new JFrame(name + "の物件情報");
		propertyFrame.setSize(800, 35*japan.prefectureInfo.get(name).size()+150);
		propertyFrame.setLayout(null);
		propertyFrame.setLocationRelativeTo(null);
		propertyFrame.setVisible(false);
		Container propertys = propertyFrame.getContentPane();
		JButton closeButton = createButton(580,35*japan.prefectureInfo.get(name).size()+50,180,50,10,"閉じる");
		closeButton.setActionCommand("物件情報を閉じる");

		//System.out.println(japan.prefectureInfo.get(name).size());//debug
		propertys.add(createText(150,10,200,40,20,"物件名"));
		propertys.add(createText(400,10,150,40,20,"値段"));
		propertys.add(createText(550,10,100,40,20,"利益率"));
		propertys.add(createText(650,10,100,40,20,"所有者"));
		if(isMonopoly(japan.prefectureInfo.get(name))) {
			JLabel label = createText(750,10,30,40,20,"独");
			label.setBackground(Color.RED);
			propertys.add(label);
		}
		for(int i=0;i<japan.prefectureInfo.get(name).size();i++) {
			String property = japan.prefectureInfo.get(name).get(i).getName();//名前
			String owner = japan.prefectureInfo.get(name).get(i).getOwner();//管理者
			int money = japan.prefectureInfo.get(name).get(i).getAmount();//購入金額
			JButton buyButton = createButton(20,15+(i+1)*35,80,30,10,"購入");
			//JButton sellButton = createButton(80,15+(i+1)*35,60,30,10,"売却");
			if(mapFrame.isShowing() || japan.prefectureInfo.get(name).get(i).getLevel()>=2
					|| (!owner.equals("") && !owner.equals(players.get(turn).getName())) || players.get(turn).getMoney()<japan.prefectureInfo.get(name).get(i).getAmount()) {
				buyButton.setEnabled(false);
			}
			for(String already:alreadys) {
				if(already.equals(japan.prefectureInfo.get(name).get(i).getName()+i)) {
					buyButton.setEnabled(false);
					//sellButton.setEnabled(false);
					break;
				}
			}
			buyButton.setActionCommand(name+"b:"+i);
			//sellButton.setActionCommand(name+"s:"+i);
			propertys.add(buyButton);
			//propertys.add(sellButton);
			int rate = japan.prefectureInfo.get(name).get(i).getRate();//利益率(3段階)
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
	}

	//駅の物件情報を閉じる
	private void closePropertys() {
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

	//独占判定処理(引数：駅)
	private boolean isMonopoly(ArrayList<Property> list) {
		for(Property property:list) {
			if(!list.get(0).containsOwner(property) || !property.isOwner()){
				return false;
			}
		}
		return true;
	}

	//独占判定処理(引数：物件)
	private boolean isMonopoly(Property property) {
		for(String preName:japan.prefectureNameList) {
			if(japan.prefectureInfo.get(preName).contains(property)) {
				for(Property pro:japan.prefectureInfo.get(preName)) {//propertyが属する駅の物件一覧
					if(!property.containsOwner(pro)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	//独占処理
	private void monopoly(ArrayList<Property> list) {
		for(int i=0;i<list.size();i++) {
			list.get(i).monoChange();
		}
	}

	//物件購入・増築処理
	private void buyPropertys(String name, int index) {
		if(!japan.prefectureInfo.get(name).get(index).isOwner()) {
			japan.prefectureInfo.get(name).get(index).buy(players.get(turn),0);
			if(isMonopoly(japan.prefectureInfo.get(name))) {
				monopoly(japan.prefectureInfo.get(name));
			}
		}else {
			japan.prefectureInfo.get(name).get(index).buy(players.get(turn),japan.prefectureInfo.get(name).get(index).getLevel()+1);
		}
		alreadys.add(japan.prefectureInfo.get(name).get(index).getName()+index);

		System.out.println(japan.prefectureInfo.get(name).get(index).getName()+"を購入"+"("+index+")");
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		printPropertys(name);
	}

	//物件売却処理
	private void sellPropertys(Property property) {
		property.sell(players.get(turn));
		if(isMonopoly(property)) {
			for(String preName:japan.prefectureNameList) {
				if(japan.prefectureInfo.get(preName).contains(property)) {
					monopoly(japan.prefectureInfo.get(preName));
				}
			}
		}
		System.out.println(property.getName()+"を売却");
		sellPrefectureFrame.setVisible(false);
		sellPrefectureFrame.removeAll();
		if(players.get(turn).getPropertys().size()>0) {
			printTakePrefectures();
		}else {
			closeTakePrefectures();
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
  	private void init() {
  		initMaps();
  		createMoveButton();
  		japan.initGoal();
  		dice.init();
  		setGoalColor();
  		Card.init(this);
  		for(int i=0;i<4;i++) {
  			players.put(i,new Player("player"+(i+1),1000));
  			players.get(i).setColt(createText(401,301,20,20,10,players.get(i).getName()));
  	  		players.get(i).getColt().setBackground(Color.BLACK);
  	  		players.get(i).getColt().setName(players.get(i).getName());
  	  	playFrame.getLayeredPane().add(players.get(i).getColt(),JLayeredPane.DEFAULT_LAYER,0);
  		}
  		initMenu();
  	}

	//ボタンを押した時の操作
 	public void actionPerformed(ActionEvent act){
		String cmd = act.getActionCommand();
		System.out.println(cmd);
		if(cmd.equals("サイコロ")) {
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
			players.get(turn).setMove(dice.shuffle(players.get(turn)));
			if(players.get(turn).getMove()==0) {
				massEvent();
			}else {
				moveMaps();
				printMoveButton();
			}
			closeMenu();
			dice.clearResult();
			dice.clearNum();
			Card.resetUsedCard();
			Card.resetUsedFixedCard();
			//dice画面を閉じる
			closeDice();
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
		}else if(cmd.equals("決算画面を閉じる")) {
			closingEndFlag=true;
		}else if(cmd.equals("ゴール画面を閉じる")) {
			closeGoal();
		}else if(cmd.equals("randomイベントを閉じる")) {
			closeRandomEvent();
		}else if(cmd.equals("店を出る")) {
			closeShop();
		}else if(cmd.equals("カード購入を終える") || cmd.equals("カード売却を終える")) {
			backShop();
		}else if(cmd.equals("カードを買う")) {
			printBuyShop();
		}else if(cmd.equals("カードを売る")) {
			printSellShop();
		}else if(cmd.equals("右")) {
			moveMaps(-130,0);
			searchShortestRoute();
			Thread thread = new Thread(new WaitThread(2));
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			reload();
			printMoveButton();
		}else if(cmd.equals("左")) {
			moveMaps(130,0);
			searchShortestRoute();
			Thread thread = new Thread(new WaitThread(2));
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			reload();
			printMoveButton();
		}else if(cmd.equals("上")) {
			moveMaps(0,130);
			searchShortestRoute();
			Thread thread = new Thread(new WaitThread(2));
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			reload();
			printMoveButton();
		}else if(cmd.equals("下")) {
			moveMaps(0,-130);
			searchShortestRoute();
			Thread thread = new Thread(new WaitThread(2));
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			reload();
			printMoveButton();
		}else if(cmd.equals("→") || cmd.equals("←") || cmd.equals("↑") || cmd.equals("↓")) {
			moveMaps(cmd);
		}
		for(int i=0;i<japan.prefectures.size();i++) {
			if(cmd.equals(japan.prefectureMapping.get(japan.prefectures.get(i)))) {
				printPropertys(cmd);
			}
		}
		String excursion[] = cmd.split("周遊");
		Random rand = new Random();
		for(int i=0;i<Card.cardList.size();i++) {
			if(cmd.equals(Card.cardList.get(i).getName())) {//カードを使う
				//カードの能力を使用
				if(Card.cardList.get(i).getMoveAbility()!=0) {
					dice.setNum(Card.cardList.get(i).useAbility());
				}else if(Card.cardList.get(i).getFixedMoveAbility()!=-1){
					if(cmd.equals("牛歩カード")) {
						int enemy;
						int period;
						do {
							enemy = rand.nextInt(4);
							period = rand.nextInt(5);
						}while(enemy == turn || period <= 1);
						players.get(enemy).getBuff().addBuff(Card.cardList.get(i).useAbility(), period);
						System.out.println(players.get(enemy).getName());
					}else {
						dice.setResult(Card.cardList.get(i).useAbility());
					}
				}else if(Card.cardList.get(i).getOthersAbility()!=0){
					Card.usedRandomCard();
					//誰に影響どんなを与えるのか
					if(cmd.equals("一頭地を抜くカード")) {
						int maxMoney=0;
						for(int player=0;player<4;player++) {
							if(players.get(player).containsMoney(maxMoney)>0) {
								maxMoney=players.get(player).getMoney();
							}
						}
						players.get(turn).addMoney(maxMoney);
					}else if(cmd.equals("起死回生カード")) {
						if(players.get(turn).containsMoney(0)<0) {
							players.get(turn).addMoney(-players.get(turn).getMoney());
						}
					}else if(cmd.equals("福袋カード")) {
						int count=0;
						do {
							int randcard = rand.nextInt(Card.cardList.size());
							players.get(turn).addCard(Card.cardList.get(randcard));
							if(players.get(turn).getCards().size()>8) {
								cardFull();
							}
							count++;
						}while(rand.nextInt(100)<50 && count<5);
					}else if(cmd.equals("ダビングカード")) {
						printDubbing();
					}else if(cmd.equals("徳政令カード")) {
						for(int player=0;player<4;player++) {
							if(players.get(player).containsMoney(0)<0) {
								players.get(player).addMoney(-players.get(player).getMoney());
							}
						}
					}
					//players.get(turn).getMoney() += Card.cardList.get(i).useAbility();
				}else if(Card.cardList.get(i).getRandomMoveAbility()!=0){
					Card.usedRandomCard();
					Coordinates coor = new Coordinates();
					//誰に影響を与えるのか
					if(cmd.equals("サミットカード")) {
						coor.setValue(players.get(turn).getNowMass());
						for(int roop=0;roop<4;roop++) {
							if(roop==turn)continue;
							moveMaps(roop,coor);
						}
					}else if(cmd.equals("北へ！カード")) {
						do {
							coor = Card.cardList.get(i).useRandomAbility();
						}while(players.get(turn).getNowMass().getY()<coor.getY());
					}else if(cmd.equals("ピッタリカード")){
						coor.setValue(players.get(rand.nextInt(4)).getNowMass());
					}else if(cmd.equals("最寄り駅カード")){
						searchNearestStation();
						Thread thread = new Thread(new WaitThread(2));
						thread.start();
						try {
							thread.join();
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
						coor.setValue(nearestStationList.get(rand.nextInt(nearestStationList.size())));
					}else {
						coor = Card.cardList.get(i).useRandomAbility();
					}
					moveMaps(turn,coor);
					try {
						Thread.sleep(100);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
					players.get(turn).getNowMass().setValue(coor);
				}
				//周遊カードの場合は確率でカードを破壊
				if(excursion.length==2) {
					Card.cardList.get(i).setCount(Card.cardList.get(i).getCount()+1);
					if(rand.nextInt(100)<30 || Card.cardList.get(i).getCount()>5) {
						players.get(turn).getCards().remove(Card.cardList.get(i));
					}
				}else {
					players.get(turn).getCards().remove(Card.cardList.get(i));
				}

				if(cmd.equals("足踏みカード") || cmd.equals("1進めるカード") || cmd.equals("2進めるカード")
						|| cmd.equals("3進めるカード") || cmd.equals("4進めるカード") || cmd.equals("5進めるカード") || cmd.equals("6進めるカード")) {
					Card.usedFixedCard();
				}
				if(!cmd.equals("徳政令カード")) {
					Card.usedCard();//カードを使ったことにする
				}
				ableMenu();
				closeCard();
				break;
			}else if(cmd.equals(Card.cardList.get(i).getName()+"t")) {//カードを捨てる
				players.get(turn).getCards().remove(Card.cardList.get(i));
				errorFrame.setVisible(false);
				playFrame.setVisible(true);
				break;
			}else if(cmd.equals(Card.cardList.get(i).getName()+"d")) {//カードを複製
				players.get(turn).getCards().add(Card.cardList.get(i));
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
			for(Card card:players.get(turn).getCards()) {
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
			for(int i=0;i<japan.prefectures.size();i++) {
				if(pre[0].equals(japan.prefectureMapping.get(japan.prefectures.get(i))+"b")) {//物件を購入
					buyPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
					break;
				}
			}
			for(Property property:players.get(turn).getPropertys()) {
				System.out.println("owner:"+property.getOwner()+"  sellproperty.name:"+property.getName()+"player:  "+players.get(turn));
				if(pre[0].equals(property.getName()+"s")) {//物件を売却
					sellPropertys(property);
					break;
				}
			}
		}
	}
}

//id=0→ターンエンド待ち,id=1→借金返済待ち,id=2→探索待ち,id=3→決算待ち
class WaitThread implements Runnable{
	private int id;
	private int money;
	private int size;
	public WaitThread(int id) {
		this.id=id;
	}

	public WaitThread(int id,int money,int size) {
		this.id=id;
		this.money=money;
		this.size=size;
	}

	public void run() {
		switch(id) {
		case 0:
			while(!Window.turnEndFlag) {//プレイヤーのターン中の処理が終わるとループを抜ける
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		case 1:
			while(this.money < 0 && this.size > 0) {
				try {
		            Thread.sleep(100);
		        } catch (InterruptedException e) {
		        	e.printStackTrace();
		        }
			}
			break;
		case 2:
			while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 200) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		case 3:
			while(!Window.closingEndFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			Window.closingEndFlag=false;
			break;
		case 4:
			while(!Window.shoppingEndFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			Window.shoppingEndFlag=false;
			break;
		default:
			break;
		}
	}
}

