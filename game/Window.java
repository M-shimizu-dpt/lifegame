/*
 *
 * <todo>
 * CPUの実装
 * イベント画面のひな型
 * 目的地に到着した時の処理
 * カード処理
 * カード購買処理
 * カード一覧
 * ボンビー処理
 *
 *設定でマップをrandomに変更できる
 *
 *メインウィンドウ以外の×ボタンを消す（消せたら）
 *
 *カード情報の登録
 *
 *マップ表示のところでjapan.contains()を使って行数を減らす
 *
 *全てのカードが同じ確率で出る為、レア度に応じた出現率の操作も必要かも？
 *
 *お金がマイナスになった時、物件を持っていれば持っている物件の中から売却する
 *
 *移動中に残り移動可能距離と目的地までの最短距離を表示する
 *始めは正しく表示することが出来るが、移動すると最短ルートの先導や最短距離を算出できなくなる問題
 *
 *独占効果の実装
 *
 *特定の場所で最短距離の計算ミスが起きる問題
 *
 *稀によくmoveButtonが無くならない問題
 *
 *マルチスレッドでは参照型の変数に注意
 *
 *ぶっとびカードを使った後少し画面を停止したい。(どこに移動したか分かるようにしたい)
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Window implements ActionListener{
	private JFrame playFrame = new JFrame("桃大郎電鉄");//メインフレーム
	private JLayeredPane play = playFrame.getLayeredPane();
	private JLayeredPane text = playFrame.getLayeredPane();
	private JLayeredPane moveButton = playFrame.getLayeredPane();
	private JFrame diceFrame = new JFrame("サイコロ");//サイコロ用フレーム
	private JLayeredPane diceP = diceFrame.getLayeredPane();
	private JFrame mapFrame = new JFrame("マップ");//マップ確認用フレーム
	private JLayeredPane maps = mapFrame.getLayeredPane();
	private JFrame infoFrame = new JFrame("会社情報");//会社情報用フレーム
	private Container info = infoFrame.getContentPane();
	private JFrame propertyFrame;//物件情報確認用フレーム
	private JFrame cardFrame = new JFrame("カード");
	private JLayeredPane card = cardFrame.getLayeredPane();
	private JButton playRight = createButton(730,250,50,40,10,"→");
	private JButton playLeft = createButton(10,250,50,40,10,"←");
	private JButton playTop = createButton(380,40,50,40,10,"↑");
	private JButton playBottom = createButton(380,510,50,40,10,"↓");
	private JButton saikoro = createButton(650, 360, 90, 30,10, "サイコロ");
    private JButton cardB = createButton(650, 400, 90, 30,10, "カード");
    private JButton company = createButton(650, 440, 90, 30,10, "会社情報");
    private JButton minimap = createButton(650, 480, 90, 30,10, "詳細マップ");
    private JButton allmap = createButton(650, 520, 90, 30,10, "全体マップ");
    private JLabel mainInfo;
    private JPanel back = new JPanel();
    private JFrame goalFrame = new JFrame("ゴール");
    private JLayeredPane goal = goalFrame.getLayeredPane();
    private JFrame errorFrame = new JFrame("カードが満タン");
    private JLabel moveLabel;

	private Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報
	private Boolean turnEndFlag=false,closingEndFlag=false;//ターンを交代するためのフラグ
	private int turn=0;//現在のターン
	private Dice dice = new Dice();//サイコロ処理
	public Japan japan = new Japan();//物件やマス情報
	private ArrayList<String> moveTrajectory = new ArrayList<String>();//プレイヤーの移動の軌跡
	private ArrayList<Integer[]> allProfitList = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> allAssetsList = new ArrayList<Integer[]>();
	private Map<String,ArrayList<Integer>> moneyTrajectory = new HashMap<String,ArrayList<Integer>>();//プレイヤーのお金の増減の軌跡
	private int year=0;
	private int month=4;
	private int maxProfit=100;
	private int minProfit=0;
	private int maxAssets=100;
	private int minAssets=0;
	private ArrayList<String> alreadys = new ArrayList<String>();
	private int saveGoal;
	public static int count;//最短経路
	public static long time;//経過時間
	private Map<Integer,ArrayList<ArrayList<Coordinates>>> trajectoryList = new HashMap<Integer,ArrayList<ArrayList<Coordinates>>>();//移動の軌跡


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

        play(endYear);
	}

	//マップの駒やタブの基本情報を更新
	public void reload(String name,int money, int month, int year) {
		mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+name+"　持ち金："+money+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count+"マス");
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(name+money);
		text.add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
	}

  	//プレイ中の動作
	private void play(int endYear) {
    	Boolean flag=true;
    	text.add(new JLabel());
    	reload(players.get(turn).name,players.get(turn).money,month,year);
    	moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+players.get(turn).move+"　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count);
    	moveLabel.setName("moves");
    	playFrame.setBackground(Color.ORANGE);
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
    		//debug
    		/*
    		searchShortestRoute();
    		while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 500) {
    			try {
    				Thread.sleep(100);
    			}catch(InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		*/
    		saveGoal=japan.goal;
    		returnMaps();//画面遷移が少し遅い
    		closeMoveButton();
    		yellowEvent();//debug
    		mainInfo.setVisible(false);
    		mainInfo.setText("自社情報　"+"名前："+players.get(turn).name+"　持ち金："+players.get(turn).money+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"まで"+Window.count+"マス");
    		mainInfo.setVisible(true);
    		while(!turnEndFlag) {//プレイヤーのターン中の処理が終わるとループを抜ける
    			try {
    				Thread.sleep(100);
    			}catch(InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		turnEndFlag=false;
    		alreadys.clear();
    		printMenu();
    		if(turn==3) {
    			ArrayList<Integer> moneyList = new ArrayList<Integer>();
    			for(int i=0;i<4;i++) {
    				moneyList.add(players.get(i).money);
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
		MultiThread.savecount=0;
		Thread t = new Thread();
		trajectoryList.clear();
		ArrayList<Coordinates> vList = new ArrayList<Coordinates>();
		for(int i=0;i<4;i++) {
			vList.add(new Coordinates());
		}
		vList.get(0).x=0;vList.get(0).y=-1;
		vList.get(1).x=0;vList.get(1).y=1;
		vList.get(2).x=-1;vList.get(2).y=0;
		vList.get(3).x=1;vList.get(3).y=0;
		int i=0;

		//探索すべき方角の数を数える
		ArrayList<Boolean> list = japan.getVector(players.get(turn).nowMass.x,players.get(turn).nowMass.y,1);
		for(Boolean bool:list) {
			if(bool) {
				//Threadを立ち上げる
				MultiThread thread = new MultiThread(this);
				thread.moveTrajectory.add(new Coordinates(players.get(turn).nowMass.x,players.get(turn).nowMass.y));
				synchronized(MultiThread.lock3) {
					thread.setMass(players.get(turn).nowMass.x+vList.get(i).x, players.get(turn).nowMass.y+vList.get(i).y);
				}
				t = new Thread(thread);
				t.start();
			}
			i++;
		}

		System.out.println("OK");
	}


	//目的地までの最短ルートを探索し、最短距離を算出
	private void searchShortestRoute() {//計算が全く間に合っていない
		Window.time = System.currentTimeMillis();
		Window.count=100;
		StationSearchThread.savecount=0;
		Thread t = new Thread();
		trajectoryList.clear();
		ArrayList<Coordinates> vList = new ArrayList<Coordinates>();
		for(int i=0;i<4;i++) {
			vList.add(new Coordinates());
		}
		vList.get(0).x=0;vList.get(0).y=-1;
		vList.get(1).x=0;vList.get(1).y=1;
		vList.get(2).x=-1;vList.get(2).y=0;
		vList.get(3).x=1;vList.get(3).y=0;
		int i=0;

		//探索すべき方角の数を数える
		ArrayList<Boolean> list = japan.getVector(players.get(turn).nowMass.x,players.get(turn).nowMass.y,1);
		for(Boolean bool:list) {
			if(bool) {
				//Threadを立ち上げる
				StationSearchThread thread = new StationSearchThread(this);
				thread.moveTrajectory.add(new Coordinates(players.get(turn).nowMass.x,players.get(turn).nowMass.y));
				synchronized(StationSearchThread.lock3) {
					thread.setMass(players.get(turn).nowMass.x+vList.get(i).x, players.get(turn).nowMass.y+vList.get(i).y);
				}
				t = new Thread(thread);
				t.start();
			}
			i++;
		}

	}

	//探索結果を格納
	public synchronized void setSearchResult(int count, ArrayList<Coordinates> trajectory) {
		if(Window.count>=count) {
			Window.count=count;
			if(!this.trajectoryList.containsKey(count)) {
				this.trajectoryList.put(count,new ArrayList<ArrayList<Coordinates>>());
			}
			this.trajectoryList.get(count).add(trajectory);
		}
	}

	//マスのイベント処理
	private void massEvent() {
		closeMoveButton();
		String massName = play.getComponentAt(400, 300).getName();
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

	private void blueEvent() {
		System.out.println("blueEvent");
		int result=0;
		while(result<500) {
			result = (int)(Math.random()*2000);
		}
		result += result*(year/10);
		result -= result%100;
		System.out.println(result);
		players.get(turn).addMoney(result);
		turnEndFlag=true;
	}

	private void redEvent() {
		System.out.println("redEvent");
		int result=0;
		while(result<500) {
			result = (int)(Math.random()*2000);
		}
		result += result*(year/10);
		result -= result%100;
		System.out.println(-result);
		players.get(turn).addMoney(-result);
		turnEndFlag=true;
	}

	private void yellowEvent() {
		System.out.println("yellowEvent");
		int rand = (int)(Math.random()*10000.0)%Card.cardList.size();
		players.get(turn).addCard(Card.cardList.get(rand));
		if(players.get(turn).cards.size()>8) {
			cardFull();
		}
		System.out.println(Card.cardList.get(rand).name);
		//turnEndFlag=true;
	}

	private void cardFull() {
		errorFrame.setSize(400,500);
		errorFrame.setLocationRelativeTo(null);
		errorFrame.setLayout(null);
		JLayeredPane error = errorFrame.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<players.get(turn).cards.size();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,players.get(turn).cards.get(i).name);
        	label.setBackground(Color.LIGHT_GRAY);
        	error.add(label);
        	throwButton.setActionCommand(players.get(turn).cards.get(i).name+"t");
        	error.add(throwButton);
        }
		error.add(titleName);

		playFrame.setVisible(false);
		errorFrame.setVisible(true);
	}

	private void shopEvent() {
		System.out.println("shopEvent");
		turnEndFlag=true;
	}

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
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).name);
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
		while(!closingEndFlag) {//プレイヤーのターン中の処理が終わるとループを抜ける
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {

			}
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
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,players.get(i).name);
			playerNameLabel.setBackground(Color.BLUE);
			closing.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerAssetsLabel = createText(400,110+(100*i),100,40,10,String.valueOf(allAssetsList.get(allAssetsList.size()-1)[i]));
			playerAssetsLabel.setBackground(Color.BLUE);
			closing.add(playerAssetsLabel,JLayeredPane.DEFAULT_LAYER,0);
		}

		closing.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		closingFrame.setVisible(true);
		while(!closingEndFlag) {//プレイヤーのターン中の処理が終わるとループを抜ける
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {

			}
		}
		closingEndFlag=false;
		closingFrame.setVisible(false);
		//ここまで総資産

	}

	//収益を加算
	private void addProfit() {
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			for(ArrayList<Property> list : japan.prefectureInfo.values()) {
				for(Property property:list) {
					if(players.get(i).name.equals(property.owner)) {
						players.get(i).addMoney(property.getProfit());
					}
				}
			}
		}
	}

	//収益を集計
	private void aggregateProfit() {
		Integer profitList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			for(ArrayList<Property> list : japan.prefectureInfo.values()) {
				for(Property property:list) {
					if(players.get(i).name.equals(property.owner)) {
						profitList[i]+=property.getProfit();
					}
				}
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
			assetsList[i]+=players.get(i).money;
			for(ArrayList<Property> list : japan.prefectureInfo.values()) {
				for(Property property:list) {
					if(players.get(i).name.equals(property.owner)) {
						assetsList[i]+=property.money;
					}
				}
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

	//青マスを作成
 	private JPanel createPlusMass(int x,int y,int size) {
		JPanel p = new JPanel();
		p.setBounds(x, y, size, size);
		p.setBackground(Color.BLUE);
		return p;
	}

	//赤マスを作成
	private JPanel createMinusMass(int x,int y,int size) {
		JPanel p = new JPanel();
		p.setBounds(x, y, size, size);
		p.setBackground(Color.RED);
		return p;
	}

	//カードマスを作成
	private JPanel createCardMass(int x,int y,int size) {
		JPanel p = new JPanel();
		p.setBounds(x, y, size, size);
		p.setBackground(Color.YELLOW);
		return p;
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

	//サイコロを回した後に移動するボタンを作成
	private void createMoveButton() {
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

	//サイコロを回した後に移動するボタンを非表示
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

	//サイコロを回した後に移動するボタンを表示
	private void printMoveButton() {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();
		vector = japan.getVector(players.get(turn).nowMass.x,players.get(turn).nowMass.y,1);
		closeMoveButton();
		if(trajectoryList.containsKey(Window.count)) {
			for(ArrayList<Coordinates> list:trajectoryList.get(Window.count)) {
				for(Coordinates coor:list) {
					for(int i=0;i<4;i++) {
						if(coor.x==players.get(turn).nowMass.x-1 && coor.y==players.get(turn).nowMass.y) {
							playLeft.setBackground(Color.MAGENTA);
						}else if(coor.x==players.get(turn).nowMass.x+1 && coor.y==players.get(turn).nowMass.y) {
							playRight.setBackground(Color.MAGENTA);
						}else if(coor.x==players.get(turn).nowMass.x && coor.y==players.get(turn).nowMass.y-1) {
							playTop.setBackground(Color.MAGENTA);
						}else if(coor.x==players.get(turn).nowMass.x && coor.y==players.get(turn).nowMass.y+1) {
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
		moveLabel.setText("残り移動可能マス数:"+players.get(turn).move+"　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count);
		moveLabel.setVisible(true);
		play.add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
	}

	private void closeMenu() {
		saikoro.setVisible(false);
		company.setVisible(false);
		cardB.setVisible(false);
		minimap.setVisible(false);
		allmap.setVisible(false);
		back.setVisible(false);
	}

	private void enableMenu() {
		saikoro.setEnabled(false);
		company.setEnabled(false);
		cardB.setEnabled(false);
		minimap.setEnabled(false);
		allmap.setEnabled(false);
	}

	private void ableMenu() {
		saikoro.setEnabled(true);
		company.setEnabled(true);
		if(!Card.usedCard) {
			cardB.setEnabled(true);
		}
		minimap.setEnabled(true);
		allmap.setEnabled(true);
	}

	private void printMenu() {
		saikoro.setVisible(true);
		company.setVisible(true);
		cardB.setVisible(true);
		minimap.setVisible(true);
		allmap.setVisible(true);
		back.setVisible(true);
	}

	//サイコロ画面表示
	private void printDice() {
		//サイコロ処理
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
		cardFrame.setSize(700, 500);
		cardFrame.setLayout(null);
        JButton closeButton = createButton(570,400,100,40,10,"戻る");
        closeButton.setActionCommand("所持カード一覧を閉じる");
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int i=0;i<players.get(turn).cards.size();i++) {
        	JButton useButton = createButton(10,35*(i+1)+30,70,30,10,"使用");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(i+1)+30,180,30,10,players.get(turn).cards.get(i).name);
        	JLabel labelText = createText(300,35*(i+1)+30,350,30,10,players.get(turn).cards.get(i).cardText);
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(players.get(turn).cards.get(i).name);
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
		card.removeAll();
	}

	//会社情報を表示
	private void info() {
		//会社情報の表示
		infoFrame.setSize(800, 600);
		infoFrame.setLayout(null);
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		JLabel titleName = createText(20,20,100,100,20,"名前");
		JLabel titleMoney = createText(120,20,100,100,20,"所持金");
		JLabel player1Name = createText(20,120,100,100,20,players.get(0).name);
		JLabel player2Name = createText(20,220,100,100,20,players.get(1).name);
		JLabel player3Name = createText(20,320,100,100,20,players.get(2).name);
		JLabel player4Name = createText(20,420,100,100,20,players.get(3).name);
		JLabel player1Money;
		JLabel player2Money;
		JLabel player3Money;
		JLabel player4Money;
		closeButton.setActionCommand("会社情報を閉じる");
		if(players.get(0).money<10000) {
			player1Money = createText(120,120,100,100,20,players.get(0).money + "万円");
		}else if(players.get(0).money%10000==0){
			player1Money = createText(120,120,100,100,20,players.get(0).money + "億円");
		}else {
			player1Money = createText(120,120,100,100,20,players.get(0).money/10000 + "億円" + players.get(0).money%10000 + "万円");
		}
		if(players.get(1).money<10000) {
			player2Money = createText(120,220,100,100,20,players.get(1).money + "万円");
		}else if(players.get(1).money%10000==0){
			player2Money = createText(120,220,100,100,20,players.get(1).money + "億円");
		}else {
			player2Money = createText(120,220,100,100,20,players.get(1).money/10000 + "億円" + players.get(1).money%10000 + "万円");
		}
		if(players.get(2).money<10000) {
			player3Money = createText(120,320,100,100,20,players.get(2).money + "万円");
		}else if(players.get(2).money%10000==0){
			player3Money = createText(120,320,100,100,20,players.get(2).money + "億円");
		}else {
			player3Money = createText(120,320,100,100,20,players.get(2).money/10000 + "億円" + players.get(2).money%10000 + "万円");
		}
		if(players.get(3).money<10000) {
			player4Money = createText(120,420,100,100,20,players.get(3).money + "万円");
		}else if(players.get(3).money%10000==0){
			player4Money = createText(120,420,100,100,20,players.get(3).money + "億円");
		}else {
			player4Money = createText(120,420,100,100,20,players.get(3).money/10000 + "億円" + players.get(3).money%10000 + "万円");
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
		info.removeAll();
	}

	//マップを動かす
	//詳細マップの画面遷移処理
	private void moveMaps(String cmd) {//今はボタンを入力できない状態にできないので、状態遷移できない状態にした。(ComponentからJButtomに変換できれば可能)
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
		do {
			//移動
			if(x<0) {
				players.get(turn).nowMass.x++;
			}else if(x>0) {
				players.get(turn).nowMass.x--;
			}
			if(y<0) {
				players.get(turn).nowMass.y++;
			}else if(y>0) {
				players.get(turn).nowMass.y--;
			}
			if(!japan.contains(players.get(turn).nowMass.x,players.get(turn).nowMass.y)) {//2マス開いている場合
				x*=2;
				y*=2;
			}
		}while(!japan.contains(players.get(turn).nowMass.x,players.get(turn).nowMass.y));
		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}else if(!(name.equals("右") || name.equals("左") || name.equals("下") || name.equals("上") ||
					name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()) || name.equals(players.get(turn).name))) {//移動・閉じるボタン以外を動かす
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}

		//移動先が1つ前と同じか
		if(moveTrajectory.size()>1) {
			if(play.getComponentAt(400, 300).getName().equals(moveTrajectory.get(moveTrajectory.size()-2))) {//同じ場合、1つ前のmoveTrajectoryを削除
				moveTrajectory.remove(moveTrajectory.size()-1);
				players.get(turn).move++;
			}else {//違う場合、移動した先の座標をmoveTrajectoryに格納
				moveTrajectory.add(play.getComponentAt(400, 300).getName());
				players.get(turn).move--;
			}
		}else {
			moveTrajectory.add(play.getComponentAt(400, 300).getName());
			players.get(turn).move--;
		}
		if(players.get(turn).move<=0) {
			moveTrajectory.clear();
			dice.clearResult();
			dice.clearNum();
			if(!Card.usedRandomCard) {
				massEvent();
			}
		}
		mainInfo.setText("自社情報　"+"名前："+players.get(turn).name+"　持ち金："+players.get(turn).money+"万円　"+year+"年目　"+month+"月　"+japan.prefectureMapping.get(japan.prefectures.get(japan.goal))+"までの最短距離:"+Window.count+"マス");
	}

	//プレイマップの画面遷移処理
		private void moveMaps(int player,Coordinates to) {//今はボタンを入力できない状態にできないので、状態遷移できない状態にした。(ComponentからJButtomに変換できれば可能)
			System.out.println("random move  x:"+to.x+"  y:"+to.y);
			int x=(to.x-players.get(player).nowMass.x)*130;
			int y=(to.y-players.get(player).nowMass.y)*130;
			for(int i=0;i<play.getComponentCount();i++) {
				if(play.getComponent(i).getName()==null) {

				}else if(play.getComponent(i).getName().equals(players.get(player).name)) {
					play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
				}
			}
			players.get(player).nowMass.setValue(to);
		}

	private void moveMaps() {
		moveTrajectory.add(play.getComponentAt(400, 300).getName());
	}


	private void returnMaps() {
		int x = 401 - players.get(turn).colt.getX();
		int y = 301 - players.get(turn).colt.getY();
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
		int distance=130;
		int list;
		boolean check;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				check=false;
				if(japan.prefectureContains(j,i)) {
					list=japan.getIndexOfPrefecture(j, i);
					JLabel pre = createText(j*distance-20,i*distance-5,80,60,15,japan.prefectureMapping.get(japan.prefectures.get(list)));
					pre.setBackground(Color.WHITE);
					play.add(pre,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
					check=true;
				}else if(japan.blueContains(j,i)) {
					list=japan.getIndexOfBlue(j, i);
					JPanel blue = createPlusMass(j*distance,i*distance,distance/3);
					blue.setName("青"+japan.getIndexOfBlue(j, i));
					play.add(blue,JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.redContains(j,i)) {
					list=japan.getIndexOfRed(j, i);
					JPanel red = createMinusMass(j*distance,i*distance,distance/3);
					red.setName("赤"+japan.getIndexOfRed(j, i));
					play.add(red,JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.yellowContains(j,i)) {
					list=japan.getIndexOfYellow(j, i);
					JPanel yellow = createCardMass(j*distance,i*distance,distance/3);
					yellow.setName("黄"+japan.getIndexOfYellow(j, i));
					play.add(yellow,JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}
				if(check) {
					drawLine(playFrame.getLayeredPane(),j*distance+20,i*distance+20,distance);
				}
			}
		}
	}


	//詳細マップを表示
	private void miniMap() {
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
		Container panel = mapFrame.getContentPane();
		panel.setBackground(Color.ORANGE);
		mapFrame.setVisible(true);
		maps.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		maps.add(right,JLayeredPane.PALETTE_LAYER,0);
		maps.add(left,JLayeredPane.PALETTE_LAYER,0);
		maps.add(top,JLayeredPane.PALETTE_LAYER,0);
		maps.add(bottom,JLayeredPane.PALETTE_LAYER,0);
		int list;
		Boolean check;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				check=false;
				if(japan.prefectureContains(j,i)) {//駅の座標が来たら
					list=japan.getIndexOfPrefecture(j, i);
					JButton button = createButton(j*distance-20,i*distance-5,60,30,8,japan.prefectureMapping.get(japan.prefectures.get(list)));
					if(list==japan.goal) {
						button.setBackground(Color.MAGENTA);
					}
					maps.add(button,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
					check=true;
				}else if(japan.blueContains(j,i)) {
					maps.add(createPlusMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.redContains(j,i)) {
					maps.add(createMinusMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.yellowContains(j,i)) {
					maps.add(createCardMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}
				if(check) {
					drawLine(mapFrame.getLayeredPane(),j*distance+10,i*distance+10,distance);
				}
			}
		}
	}


	//全体マップを表示
	private void allMap() {
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		boolean check;
		int distance=30;
		mapFrame.setSize(800, 600);
		mapFrame.setLayout(null);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.getContentPane().setBackground(Color.ORANGE);
		mapFrame.setVisible(true);
		closeButton.setActionCommand("マップを閉じる");
		maps.add(closeButton);
		int list;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				check=false;
				if(japan.prefectureContains(j,i)) {//駅の座標が来たら
					list=japan.getIndexOfPrefecture(j, i);
					JButton button=createButton(j*distance,i*distance,distance/3,distance/3,6,japan.prefectureMapping.get(japan.prefectures.get(list)));
					if(list==japan.goal) {
						button.setBackground(Color.MAGENTA);
					}
					maps.add(button,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
					check=true;
				}else if(japan.blueContains(j,i)) {
					maps.add(createPlusMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.redContains(j,i)) {
					maps.add(createMinusMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}else if(japan.yellowContains(j,i)) {
					maps.add(createCardMass(j*distance,i*distance,distance/3),JLayeredPane.DEFAULT_LAYER,0);
					check=true;
				}
				if(check) {
					drawLine(mapFrame.getLayeredPane(),j*distance+5,i*distance+5,distance);
				}
			}
		}
	}

	//マップ画面を閉じる
	private void closeMaps() {
		mapFrame.setVisible(false);
		maps.removeAll();
	}

	//指定した位置のマスの移動可能方向を取得
	//出力すべき方角を取得


	//線を引く	//線路を引く(Boxで代用)

	private void drawLine(Container lines,int x,int y,int size) {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();//線を引くべき方角
		//Container lines=mapFrame.getLayeredPane();//線をまとめたコンテナ
		vector = japan.getVector(x,y,size);
		if(vector!=null) {
			if(vector.get(0)) {
				JPanel line = new JPanel();
				line.setBackground(Color.BLACK);
				line.setLocation(x-size, y);
				line.setSize(size,2);
				lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
			}
			if(vector.get(1)) {
				JPanel line = new JPanel();
				line.setBackground(Color.BLACK);
				line.setLocation(x, y);
				line.setSize(size,2);
				lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
			}
			if(vector.get(2)) {
				JPanel line = new JPanel();
				line.setBackground(Color.BLACK);
				line.setLocation(x, y-size);
				line.setSize(2,size);
				lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
			}
			if(vector.get(3)) {
				JPanel line = new JPanel();
				line.setBackground(Color.BLACK);
				line.setLocation(x, y);
				line.setSize(2,size);
				lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
			}
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


	private void goal() {
		int goalMoney;
		playFrame.setVisible(false);
		goalFrame.setSize(500, 300);
		goalFrame.setLayout(null);
		goalFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(380,180,100,50,10,"閉じる");
		closeButton.setActionCommand("ゴール画面を閉じる");
		goalMoney=10000*year;
		goalMoney+=Math.random()*10000.0;
		goalMoney-=goalMoney%100;
		System.out.println(goalMoney);
		players.get(turn).addMoney(goalMoney);
		JLabel label = createText(10,30,400,100,10,players.get(turn).name+"さんには地元民から援助金として"+System.lineSeparator()+goalMoney/10000+"億"+goalMoney%10000+"万円が寄付されます。");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		goalFrame.setVisible(true);

		play.getComponentAt(400, 300).setBackground(Color.WHITE);

		japan.changeGoal();

		setGoalColor();
	}

	private void setGoalColor() {
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()==null)continue;
			if(play.getComponent(i).getName().equals(japan.prefectureMapping.get(japan.prefectures.get(japan.goal)))) {
				play.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}


	private void closeGoal() {
		goalFrame.setVisible(false);
		printPropertys(japan.prefectureMapping.get(japan.prefectures.get(saveGoal)));
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
		System.out.println(japan.prefectureInfo.get(name).size());//debug
		propertys.add(createText(150,10,200,40,20,"物件名"));
		propertys.add(createText(400,10,150,40,20,"値段"));
		propertys.add(createText(550,10,100,40,20,"利益率"));
		propertys.add(createText(650,10,100,40,20,"所有者"));
		for(int i=0;i<japan.prefectureInfo.get(name).size();i++) {
			String property = japan.prefectureInfo.get(name).get(i).name;//名前
			String owner = japan.prefectureInfo.get(name).get(i).owner;//管理者
			int money = japan.prefectureInfo.get(name).get(i).money;//購入金額
			int level = japan.prefectureInfo.get(name).get(i).level;//利益率の段階
			JButton buyButton = createButton(10,15+(i+1)*35,60,30,10,"購入");
			JButton sellButton = createButton(80,15+(i+1)*35,60,30,10,"売却");
			if(mapFrame.isShowing() || japan.prefectureInfo.get(name).get(i).level>=2
					|| (!owner.equals("") && !owner.equals(players.get(turn).name)) || players.get(turn).money<japan.prefectureInfo.get(name).get(i).money) {
				buyButton.setEnabled(false);
			}
			if(mapFrame.isShowing() || !owner.equals(players.get(turn).name)) {
				sellButton.setEnabled(false);
			}
			for(String already:alreadys) {
				if(already.equals(japan.prefectureInfo.get(name).get(i).name+i)) {
					buyButton.setEnabled(false);
					sellButton.setEnabled(false);
					break;
				}
			}
			buyButton.setActionCommand(name+"b:"+i);
			sellButton.setActionCommand(name+"s:"+i);
			propertys.add(buyButton);
			propertys.add(sellButton);
			int rate = (int)((double)japan.prefectureInfo.get(name).get(i).rate.get(level) * 100);//利益率(3段階)
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
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		if(!mapFrame.isShowing()) {
			playFrame.setVisible(true);
			turnEndFlag=true;
		}
	}


	//物件購入・増築処理
	private void buyPropertys(String name, int index) {

		if(japan.prefectureInfo.get(name).get(index).owner.equals("")) {
			japan.prefectureInfo.get(name).get(index).buy(players.get(turn).name,0);
		}else {
			japan.prefectureInfo.get(name).get(index).buy(players.get(turn).name,japan.prefectureInfo.get(name).get(index).level+1);
		}
		players.get(turn).addMoney(-japan.prefectureInfo.get(name).get(index).money);
		alreadys.add(japan.prefectureInfo.get(name).get(index).name+index);

		System.out.println(japan.prefectureInfo.get(name).get(index).name+"を購入"+"("+index+")");
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		printPropertys(name);
	}


	private void sellPropertys(String name, int index) {
		japan.prefectureInfo.get(name).get(index).sell();
		players.get(turn).addMoney(japan.prefectureInfo.get(name).get(index).money/2);
		alreadys.add(japan.prefectureInfo.get(name).get(index).name+index);

		System.out.println(japan.prefectureInfo.get(name).get(index).name+"を売却"+"("+index+")");
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		printPropertys(name);
	}


	//プレイマップの大阪を真ん中に設定する
	//マップの位置を初期位置(大阪)に設定
	private void initMaps() {
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
  			players.get(i).colt = createText(401,301,20,20,10,players.get(i).name);
  	  		players.get(i).colt.setBackground(Color.BLACK);
  	  		players.get(i).colt.setName(players.get(i).name);
  			text.add(players.get(i).colt,JLayeredPane.DEFAULT_LAYER,0);
  		}
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
			for(int i=0;i<dice.num;i++) {//サイコロの数だけサイコロを回わす；
				if(Card.usedFixedCard)break;//初めからresultが入力されていれば
				dice.shuffle();
				System.out.println("result"+i+":"+dice.result);
			}
			System.out.println("allResult:"+dice.result+"  num:"+dice.num);
			players.get(turn).move = dice.result;
			if(players.get(turn).move==0) {
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
		}else if(cmd.equals("右")) {
			moveMaps(-130,0);
			/*
			searchShortestRoute();
			while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			printMoveButton();
		}else if(cmd.equals("左")) {
			moveMaps(130,0);
			/*
			searchShortestRoute();
			while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			printMoveButton();
		}else if(cmd.equals("上")) {
			moveMaps(0,130);
			/*
			searchShortestRoute();
			while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			printMoveButton();
		}else if(cmd.equals("下")) {
			moveMaps(0,-130);
			/*
			searchShortestRoute();
			while(MultiThread.savecount<=1000 && System.currentTimeMillis()-Window.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
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
		for(int i=0;i<Card.cardList.size();i++) {
			if(cmd.equals(Card.cardList.get(i).name)) {//カードを使う
				//カードの能力を使用
				if(Card.cardList.get(i).moveAbility!=0) {
					dice.num = Card.cardList.get(i).useAbility();
				}else if(Card.cardList.get(i).fixedMoveAbility!=-1){
					dice.result = Card.cardList.get(i).useAbility();
				}else if(Card.cardList.get(i).randomMoveAbility!=0){
					Card.usedRandomCard();
					Coordinates coor = new Coordinates();
					//誰に影響を与えるのか
					if(cmd.equals("サミットカード")) {
						coor.setValue(players.get(turn).nowMass);
						for(int roop=0;roop<4;roop++) {
							if(roop==turn)continue;
							moveMaps(roop,coor);
						}
					}else {
						if(cmd.equals("北へ！カード")) {
							do {
								coor = Card.cardList.get(i).useRandomAbility();
							}while(players.get(turn).nowMass.y<coor.y);
						}else if(cmd.equals("ピッタリカード")){
							int rand;
							do {
								rand=(int)(Math.random()*100.0)%4;
							}while(rand==turn);
							coor.setValue(players.get(rand).nowMass);
						}else if(cmd.equals("最寄り駅カード")){
							searchNearestStation();///////////////////////////////////////////////////////
						}else {
							coor = Card.cardList.get(i).useRandomAbility();
						}
						moveMaps(turn,coor);
						try {
							Thread.sleep(100);
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
						players.get(turn).nowMass.setValue(coor);
					}
				}else if(Card.cardList.get(i).othersAbility!=0){
					//誰に影響どんなを与えるのか
					players.get(turn).money = Card.cardList.get(i).useAbility();
				}
				//周遊カードの場合は確率でカードを破壊
				if(excursion.length==2) {
					double rand=Math.random();
					Card.cardList.get(i).count++;
					if(rand<0.3 || Card.cardList.get(i).count>5) {
						players.get(turn).cards.remove(Card.cardList.get(i));
					}
				}else {
					players.get(turn).cards.remove(Card.cardList.get(i));
				}

				if(cmd.equals("足踏みカード") || cmd.equals("1進めるカード") || cmd.equals("2進めるカード")
						|| cmd.equals("3進めるカード") || cmd.equals("4進めるカード") || cmd.equals("5進めるカード") || cmd.equals("6進めるカード")) {
					Card.usedFixedCard();
				}
				if(!cmd.equals("徳政令??")) {
					Card.usedCard();
				}
				ableMenu();
				closeCard();
				break;
			}else if(cmd.equals(Card.cardList.get(i).name+"t")) {//カードを捨てる
				players.get(turn).cards.remove(Card.cardList.get(i));
				errorFrame.setVisible(false);
				playFrame.setVisible(true);
				break;
			}
		}
		if(Card.usedRandomCard) {
			Card.resetUsedCard();
			Card.resetUsedFixedCard();
			Card.resetUsedRandomCard();
			ableMenu();
			turnEndFlag=true;
		}
		String pre[] = cmd.split(":");
		if(pre.length==2) {
			for(int i=0;i<japan.prefectures.size();i++) {
				if(pre[0].equals(japan.prefectureMapping.get(japan.prefectures.get(i))+"b")) {//物件を購入
					buyPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
					break;
				}else if(pre[0].equals(japan.prefectureMapping.get(japan.prefectures.get(i))+"s")) {//物件を売却
					sellPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
					break;
				}
			}

		}
	}
}
