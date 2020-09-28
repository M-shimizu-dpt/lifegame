/*
 * 画面表示に関する処理を管理するクラス
 * 画面表示をする際にこのクラスのメソッドを使用
*/
package lifegame.game.object.map.print;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.ClosingEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.MassEvent;
import lifegame.game.event.MoveEvent;
import lifegame.game.event.RandomEvent;
import lifegame.game.event.WaitThread;
import lifegame.game.event.search.Searcher;
import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.information.Station;

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

	private static boolean throwFlag=false;//カードを捨てるまで待つためのフラグ

	private ArrayList<Card> canBuyCardlist = new ArrayList<Card>();//店の購入可能カードリスト

	public Window() {

	}

	//メイン画面でのメニューボタンを有効
	public void ableMenu() {
		saikoro.setEnabled(true);
		company.setEnabled(true);
		if(!Card.isUsed()) {
			cardB.setEnabled(true);
		}
		minimap.setEnabled(true);
		allmap.setEnabled(true);
	}

	public void actionPerformed(ActionEvent act){
		String cmd = act.getActionCommand();
		//System.out.println(cmd);
		if(cmd.equals("始める")) {
    		App.start();
    	}else if(cmd.equals("start")) {
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
			ClosingEvent.closed();//closeメソッドを用意した方がCPUでの操作がしやすくなる
		}else if(cmd.equals("ゴール画面を閉じる")) {
			closeGoal();
		}else if(cmd.equals("randomイベントを閉じる")) {
			closeRandomEvent();
		}else if(cmd.equals("臨時収入画面を閉じる")) {
			RandomEvent.end();//closeメソッドを用意した方がCPUでの操作がしやすくなる
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
		for(Station sta:Japan.getStationList()) {
			if(cmd.equals(sta.getName())) {
				printPropertys(cmd);
			}
		}

		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName())) {//カードを使う
				Card.getCard(i).useAbilitys(this);
				if(ContainsEvent.id(Card.getCard(i),2)){
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
			}else if(cmd.equals(Card.getCard(i).getName()+"t")) {//カードを捨てる
				Player.player.removeCard(Card.getCard(i));
				closeErrorFrame();
				break;
			}else if(cmd.equals(Card.getCard(i).getName()+"d")) {//カードを複製
				Player.player.addCard(Card.getCard(i));
				closeDubbing();
				break;
			}
		}

		if(Card.isUsedRandom() || Card.isUsedOthers()) {
			Card.resetFlags();
			ableMenu();
			if(playFrame.isVisible()) {
				App.turnEnd();
			}
		}

		String pre[] = cmd.split(":");
		if(pre.length==2) {
			for(Card card:Player.player.getCards()) {
				if(pre[0].equals(card.getName()) && pre[1].equals("s")) {//カード売却
					sellCard(card);
					break;
				}
			}
			for(Card card:Card.getCardList()) {
				if(pre[0].equals(card.getName()) && pre[1].equals("b")) {//カード購入
					buyCard(card);
					break;
				}
			}
			for(int i=0;i<Japan.getStationSize();i++) {
				if(pre[0].equals(Japan.getStationName(Japan.getStationCoor(i))+"b")) {//物件を購入
					buyPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
					break;
				}
			}
			for(Property property:Player.player.getPropertys()) {
				if(pre[0].equals(property.getName()+"s")) {//物件を売却
					sellPropertys(property);
					break;
				}
			}
		}
	}

	public void addPlayFrame(JLabel label) {
		playFrame.getLayeredPane().add(label,JLayeredPane.DEFAULT_LAYER,0);
	}

	public void addPlayFrame(JButton button) {
		playFrame.getLayeredPane().add(button,JLayeredPane.PALETTE_LAYER,0);
	}

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
		JLabel p1 = createText(Player.players.get(0).getNowMass().getX()*distance-5, Player.players.get(0).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "1");
		p1.setBackground(Color.BLACK);
		JLabel p2 = createText(Player.players.get(1).getNowMass().getX()*distance+5, Player.players.get(1).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "2");
		p2.setBackground(Color.BLACK);
		JLabel p3 = createText(Player.players.get(2).getNowMass().getX()*distance-5, Player.players.get(2).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "3");
		p3.setBackground(Color.BLACK);
		JLabel p4 = createText(Player.players.get(3).getNowMass().getX()*distance+5, Player.players.get(3).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "4");
		p4.setBackground(Color.BLACK);
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!ContainsEvent.isMass(j, i))continue;
				if(ContainsEvent.isStation(j,i)) {//駅の座標が来たら
					Station list=Japan.getStation(j, i);
					JButton button=createButton(j*distance,i*distance,distance/3,distance/3,6,list.getName());
					if(ContainsEvent.isGoal(list)) {
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

		for(float i=0;i<ClosingEvent.getAssetsListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x = (int)(300*((i+1)/ClosingEvent.getAssetsListSize()));
				int y = (int)(500-(400*ClosingEvent.getAssetsList((int)i)[j]/(ClosingEvent.maxAssets-ClosingEvent.minAssets)));
				graph.setBounds(x,y,5,5);
				graph.setBackground(Color.YELLOW);
				Assets.add(graph);
				//System.out.println("x:"+x+"\ty:"+y);
				if(i==ClosingEvent.getAssetsListSize()-1) {//ぬるぽ回避
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
				int x2=(int)(300*((i+2)/ClosingEvent.getAssetsListSize()));									//翌年のx座標
				int y2=(int)(500-(400*ClosingEvent.getAssetsList((int)i+1)[j]/(ClosingEvent.maxAssets-ClosingEvent.minAssets)));		//翌年のy座標

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
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,Player.players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			Assets.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerAssetsLabel = createText(500,110+(100*i),100,40,10,String.valueOf(ClosingEvent.getAssetsList(ClosingEvent.getAssetsListSize()-1)[i]));
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
		AssetsFrame.setVisible(false);
		Assets.removeAll();
		//ここまで総資産
	}

	//カードショップの売買画面を閉じる
	private void backShop() {
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		if(Player.player.getCardSize()>0) {
			shopFrontFrame.getLayeredPane().getComponentAt(100,110).setEnabled(true);
		}else {
			shopFrontFrame.getLayeredPane().getComponentAt(100,110).setEnabled(false);
		}
		shopFrontFrame.setVisible(true);
	}

	public void bonbyPlayer() {
		/*
		for(int i = 0;i<4;i++) {
			System.out.println(Player.players.get(i).isBonby());//bonbyフラグTEST用
		}
		*/
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
		if(!Player.player.isPlayer()) {
			closeButton.setEnabled(false);
		}
		binbo.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		binboFrame.setName("ボンビーのターン");

		text1 = createText(10,10,600,100,20,"テストボンビー1");
		text2 = createText(10,110,600,100,20,"テストボンビー２");
		//text3 = createText(10,210,600,100,20,"テストボンビー3");

		text1.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text2);
		//text3.setHorizontalTextPosition(SwingConstants.LEFT);
		//binbo.add(text3);
		//System.out.println(icon.getIconWidth());
		//System.out.println(icon.getIconHeight());
		binboFrame.setVisible(true);

		setCloseFrame(5);

	}

	private void buyCard(Card card) {
		Player.player.buyCard(card);
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		printBuyShop();
	}

	//物件購入・増築処理
	private void buyPropertys(String name, int index) {
		Player.player.buyPropertys(name, index);

		//System.out.println(Japan.getStaInProperty(name,index).getName()+"を購入"+"("+index+")");
		propertyFrame.setVisible(false);
		propertyFrame.removeAll();
		printPropertys(name);
	}

	//所持カードが最大を超えた場合、捨てるカードを選択する為所持カード一覧を表示
	public void cardFull() {
		errorFrame.setSize(400,500);
		errorFrame.setLocationRelativeTo(null);
		errorFrame.setLayout(null);
		JLayeredPane error = errorFrame.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,Player.player.getCardName(i));
        	label.setBackground(Color.LIGHT_GRAY);
        	error.add(label);
        	throwButton.setActionCommand(Player.player.getCardName(i)+"t");
        	error.add(throwButton);
        }
		error.add(titleName);

		playFrame.setVisible(false);
		errorFrame.setVisible(true);

		if(!Player.player.isPlayer()) {
			Player.player.cardFullCPU();
			errorFrame.setVisible(false);
			playFrame.setVisible(true);
		}
	}

	public void closeBinboEvent() {
		binboFrame.setVisible(false);
		binboFrame.removeAll();
		playFrame.setVisible(true);
		BinboEvent.turnFinish();
	}

	//所持カード一覧を閉じる
	private void closeCard() {
		cardFrame.setVisible(false);
		cardFrame.getLayeredPane().removeAll();
	}

	//サイコロ画面を閉じる
	private void closeDice() {
		diceFrame.setVisible(false);
	}

	//カードの複製を行う画面を非表示
	public void closeDubbing() {
		Random rand = new Random();
		dubbingCardFrame.setVisible(false);
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			App.turnEnd();
		}
	}

	public void closeErrorFrame() {
		Window.throwFlag=true;
		errorFrame.setVisible(false);
		playFrame.setVisible(true);
	}

	public void closeGoal() {
		goalFrame.setVisible(false);
		printPropertys(Japan.getSaveGoalName());
	}

	//会社情報を閉じる
	private void closeInfo() {
		infoFrame.setVisible(false);
		infoFrame.getContentPane().removeAll();
	}

	private void closeMaps() {
		mapFrame.setVisible(false);
		mapFrame.getLayeredPane().removeAll();
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

	private void closePopUp() {
		confirmationFrame.setVisible(false);
		confirmationFrame.removeAll();
		playFrame.setVisible(true);
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
				App.turnEnd();
			}
		}
	}

	public void closeRandomEvent() {
		randomFrame.setVisible(false);
		randomFrame.removeAll();
		playFrame.setVisible(true);
		App.turnEnd();
	}

	//店用フレームを閉じる
	public void closeShop() {
		Random rand = new Random();
		shopFrontFrame.setVisible(false);
		shopFrontFrame.removeAll();
		canBuyCardlist.clear();
		playFrame.setVisible(true);
		if(rand.nextInt(100) < 3) {
			randomEvent();
		}else {
			App.turnEnd();
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
			App.turnEnd();
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

		ClosingEvent.closing(Player.players);

		revenue();
	}

	//ボタンを作成
	private JButton createButton(int x,int y,int w,int h,int size,String name) {
		JButton button = new JButton(name);
		button.setFont(new Font("SansSerif", Font.ITALIC, size));
		button.setBounds(x,y,w,h);
		button.addActionListener(this);
		button.setActionCommand(name);
		button.setName(name);
		button.setEnabled(Player.player.isPlayer());
		return button;
	}

	//駅以外のマスを作成
	private JPanel createMass(int j,int i,int distance) {
		JPanel mass = new JPanel();
		if(ContainsEvent.isBlue(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.BLUE);
			mass.setName("B"+Japan.getIndexOfBlue(j, i));
		}else if(ContainsEvent.isRed(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.RED);
			mass.setName("R"+Japan.getIndexOfRed(j, i));
		}else if(ContainsEvent.isYellow(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+Japan.getIndexOfYellow(j, i));
		}else if(ContainsEvent.isShop(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.GRAY);
			mass.setName("S"+Japan.getIndexOfShop(j, i));
		}
		return mass;
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

	//textを作成
	public JLabel createText(int x,int y,int w,int h,int size,String name) {
		JLabel text = new JLabel(name,SwingConstants.CENTER);
		text.setOpaque(true);
		text.setBounds(x, y, w, h);
		text.setFont(new Font("SansSerif", Font.ITALIC, size));
		text.setName(name);
		return text;
	}

	//textを作成
	public JLabel createImage(int x,int y,int w,int h,int size,String image) {
		ImageIcon icon = new ImageIcon(image);
		assert icon==null : "null";
		System.out.println("h:"+icon.getIconHeight()+"   w:"+icon.getIconWidth());
		JLabel images = new JLabel(icon);
		images.setOpaque(true);
		images.setBounds(x, y, w, h);
		return images;
	}

	//サイコロ操作
	public void diceShuffle() {
		Player.player.setMove(Dice.shuffle(Player.player));
		Searcher.searchCanMoveMass(this,Player.player);
		if(Player.player.getMove()==0) {
			massEvent();
		}else {
			MoveEvent.addTrajectory(playFrame.getLayeredPane().getComponentAt(400, 300).getName());
			if(Player.player.isPlayer()) {
				printMoveButton();
			}
		}
		closeMenu();
		Dice.clear();
		Card.resetUsed();
		Card.resetUsedFixed();
		closeDice();
	}

	//メイン画面でのメニューボタンを無効
	public void enableMenu() {
		saikoro.setEnabled(false);
		company.setEnabled(false);
		cardB.setEnabled(false);
		minimap.setEnabled(false);
		allmap.setEnabled(false);
	}


	private void drawLine(JLayeredPane lines,int x,int y,int size,int somethig) {
		ArrayList<Coordinates> list = Japan.getMovePossibles(x, y);
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

	//ゴール画面を表示
	public void goal() {
		JLayeredPane goal = goalFrame.getLayeredPane();
		int goalMoney;
		Random rand = new Random();
		playFrame.setVisible(false);
		goalFrame.setSize(500, 300);
		goalFrame.setLayout(null);
		goalFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(380,180,100,50,10,"閉じる");
		closeButton.setActionCommand("ゴール画面を閉じる");
		goalMoney=10000*App.year;
		goalMoney+=rand.nextInt(10000);
		goalMoney-=goalMoney%100;
		Player.player.addMoney(goalMoney);
		JLabel label = createText(10,30,400,100,10,Player.player.getName()+"さんには地元民から援助金として"+System.lineSeparator()+goalMoney/10000+"億"+goalMoney%10000+"万円が寄付されます。");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		goalFrame.setVisible(true);

		setCloseFrame(3);

		playFrame.getLayeredPane().getComponentAt(400, 300).setBackground(Color.WHITE);

		Japan.changeGoal();

		setGoalColor();

		BinboEvent.binboPossessPlayer();
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
		JLabel player1Name = createText(20,120,100,100,20,Player.players.get(0).getName());
		JLabel player2Name = createText(20,220,100,100,20,Player.players.get(1).getName());
		JLabel player3Name = createText(20,320,100,100,20,Player.players.get(2).getName());
		JLabel player4Name = createText(20,420,100,100,20,Player.players.get(3).getName());
		JLabel player1Money;
		JLabel player2Money;
		JLabel player3Money;
		JLabel player4Money;
		closeButton.setActionCommand("会社情報を閉じる");
		if(Player.players.get(0).getMoney()<10000) {
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney() + "万円");
		}else if(Player.players.get(0).getMoney()%10000==0){
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney() + "億円");
		}else {
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney()/10000 + "億円" + Player.players.get(0).getMoney()%10000 + "万円");
		}
		if(Player.players.get(1).getMoney()<10000) {
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney() + "万円");
		}else if(Player.players.get(1).getMoney()%10000==0){
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney() + "億円");
		}else {
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney()/10000 + "億円" + Player.players.get(1).getMoney()%10000 + "万円");
		}
		if(Player.players.get(2).getMoney()<10000) {
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney() + "万円");
		}else if(Player.players.get(2).getMoney()%10000==0){
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney() + "億円");
		}else {
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney()/10000 + "億円" + Player.players.get(2).getMoney()%10000 + "万円");
		}
		if(Player.players.get(3).getMoney()<10000) {
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney() + "万円");
		}else if(Player.players.get(3).getMoney()%10000==0){
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney() + "億円");
		}else {
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney()/10000 + "億円" + Player.players.get(3).getMoney()%10000 + "万円");
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

	//プレイマップの中央位置を初期位置(大阪)に設定
	private void initMaps() {
		JLayeredPane play = playFrame.getLayeredPane();
		int x=-400;
		int y=-900;
		for(int i=0;i<play.getComponentCount();i++) {
			if(!(i>=0&&i<6)) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}
	}

	private void initMenu() {
		mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count+"マス");
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(Player.player.getName()+Player.player.getMoney());
		playFrame.getLayeredPane().add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
	}

	public void initPlayFrame(int playerCount) {
		int w = 800, h = 600;
		playFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
        // ウィンドウのサイズ・初期位置
        playFrame.setSize(w, h);
        playFrame.setLocationRelativeTo(null);//windowsの画面の中央に表示
        playFrame.setLayout(null);

        // 背景色追加
        playFrame.getContentPane().setBackground(Color.ORANGE);

        JLayeredPane play = playFrame.getLayeredPane();
		int distance=130;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!ContainsEvent.isMass(j, i))continue;
				if(ContainsEvent.isStation(j,i)) {
					JLabel pre = createText(j*distance-20,i*distance-5,80,60,15,Japan.getStationName(j, i));
					pre.setBackground(Color.WHITE);
					play.add(pre,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
				}else {
					play.add(createMass(j,i,distance),JLayeredPane.DEFAULT_LAYER,0);
				}
				drawLine(playFrame.getLayeredPane(),j,i,distance,20);
			}
		}
		Player.initPlayers(this, playerCount);
		Player.initNowPlayer();
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
	}

	public static void initThrowFlag() {
		throwFlag=false;
	}

	public void initWindow(int endYear,int playerCount){
		initPlayFrame(playerCount);
		initMaps();
    	createMoveButton();
  		Japan.initGoal();

  		setGoalColor();

  		initMenu();

        JLayeredPane menu = playFrame.getLayeredPane();//ボタンが前に出ない

        back.setBackground(Color.CYAN);
        back.setBounds(640,350,110,210);
        back.setName("ボタン背景");
        menu.add(back,JLayeredPane.PALETTE_LAYER,-1);
        menu.add(company,JLayeredPane.PALETTE_LAYER,0);
        menu.add(saikoro,JLayeredPane.PALETTE_LAYER,0);
    	menu.add(cardB,JLayeredPane.PALETTE_LAYER,0);
    	menu.add(minimap,JLayeredPane.PALETTE_LAYER,0);
    	menu.add(allmap,JLayeredPane.PALETTE_LAYER,0);

    	// ウィンドウを表示
        playFrame.setVisible(true);

        moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count);
      	moveLabel.setName("moves");
      	playFrame.setBackground(Color.ORANGE);
      	closeMoveButton();
      	addPlayFrame(waitButton);
	}

	public static boolean isThrowed() {
		return throwFlag;
	}

	private void massEvent() {
		closeMoveButton();
		MassEvent.massEvent(this,playFrame.getLayeredPane().getComponentAt(400, 300).getName());
		ableMenu();
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
		JLabel p1 = createText(Player.players.get(0).getNowMass().getX()*distance-15, Player.players.get(0).getNowMass().getY()*distance-5, 20, 10, 10, "1");
		p1.setBackground(Color.BLACK);
		JLabel p2 = createText(Player.players.get(1).getNowMass().getX()*distance+15, Player.players.get(1).getNowMass().getY()*distance-5, 20, 10, 10, "2");
		p2.setBackground(Color.BLACK);
		JLabel p3 = createText(Player.players.get(2).getNowMass().getX()*distance-15, Player.players.get(2).getNowMass().getY()*distance+15, 20, 10, 10, "3");
		p3.setBackground(Color.BLACK);
		JLabel p4 = createText(Player.players.get(3).getNowMass().getX()*distance+15, Player.players.get(3).getNowMass().getY()*distance+15, 20, 10, 10, "4");
		p4.setBackground(Color.BLACK);
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!ContainsEvent.isMass(j, i))continue;
				if(ContainsEvent.isStation(j,i)) {//駅の座標が来たら
					Station list=Japan.getStation(j, i);
					JButton button = createButton(j*distance-20,i*distance-5,60,30,8,list.getName());
					if(ContainsEvent.isGoal(list)) {
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

	public void monthUpdate(boolean first) {
  		if(first) {
  			printMonthFrame();
  		}else {
	    		if(App.turn==3) {
	    			ArrayList<Integer> moneyList = new ArrayList<Integer>();
	    			for(Player player:Player.players.values()) {
	    				moneyList.add(player.getMoney());
	    			}
	    			if(App.month==3) {
		    			closing();
		    			App.year++;
					}
	    			App.month++;
	    			if(App.month==13) {
	    				App.month=1;
	    			}
	    			printMonthFrame();
	    			App.turn=0;
	    		}else {
	    			App.turn++;
	    		}
  		}
  	}

	//次のプレイヤーをプレイ画面の真ん中に位置させる
	public void moveMaps() {
		JLayeredPane play = playFrame.getLayeredPane();
		int x = 401 - Player.player.getColt().getX();
		int y = 301 - Player.player.getColt().getY();
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

		Coordinates coor = MoveEvent.movePlayer(x, y);

		for(int i=0;i<play.getComponentCount();i++) {
			name=play.getComponent(i).getName();
			if(name==null) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+coor.getX(),play.getComponent(i).getY()+coor.getY());
			}else if(!(name.equals("stop") || name.equals("start") || name.equals("右") || name.equals("左") || name.equals("下") || name.equals("上") ||
					name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()) || name.equals(Player.player.getName()))) {//移動・閉じるボタン以外を動かす
				play.getComponent(i).setLocation(play.getComponent(i).getX()+coor.getX(),play.getComponent(i).getY()+coor.getY());
			}
		}

		MoveEvent.updateTrajectory(play.getComponentAt(400, 300).getName());

		if(Player.player.getMove()<=0) {
			MoveEvent.clearTrajectory();
			Dice.clear();
			BinboEvent.clearBefore();
			if(!Card.isUsedRandom()) {
				massEvent();
			}
		}
	}

	//プレイマップの画面遷移処理
	public void moveMaps(Player player,Coordinates to) {
		JLayeredPane play = playFrame.getLayeredPane();
		int x=(to.getX()-player.getNowMass().getX())*130;
		int y=(to.getY()-player.getNowMass().getY())*130;
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()!=null && play.getComponent(i).getName().equals(player.getName())) {
				play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
			}
		}
		MoveEvent.moveTo(player, to);
	}

	//メイン画面でのメニューボタンを表示
	public void printMenu() {
		saikoro.setVisible(true);
		company.setVisible(true);
		cardB.setVisible(true);
		minimap.setVisible(true);
		allmap.setVisible(true);
		back.setVisible(true);
	}

	//メイン画面での移動ボタンを表示
	private void printMoveButton() {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();
		vector = Japan.getVector(Player.player.getNowMass(),1);
		closeMoveButton();
		if(Searcher.nearestTrajectoryList.containsKey(Searcher.count)) {
			for(ArrayList<Coordinates> list:Searcher.nearestTrajectoryList.get(Searcher.count)) {
				for(Coordinates coor:list) {
					for(int i=0;i<4;i++) {
						if(ContainsEvent.coor(coor, Player.player.getNowMass().getX()-1,Player.player.getNowMass().getY())) {
							playLeft.setBackground(Color.MAGENTA);
						}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX()+1,Player.player.getNowMass().getY())) {
							playRight.setBackground(Color.MAGENTA);
						}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX(),Player.player.getNowMass().getY()-1)) {
							playTop.setBackground(Color.MAGENTA);
						}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX(),Player.player.getNowMass().getY()+1)) {
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
		if(Player.player.getMove() <= 0) {
			closeMoveButton();
		}else {
			Searcher.searchShortestRoute(this,Player.player);
			WaitThread thread = new WaitThread(2);
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		moveLabel.setText("残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count);
		moveLabel.setVisible(true);
		playFrame.getLayeredPane().add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
	}

	//カードショップの購入画面
	public void printBuyShop() {
		shopFrontFrame.setVisible(false);
		shopFrame = new JFrame("購入");
		JLayeredPane shopBuy = shopFrame.getLayeredPane();
		shopFrame.setSize(600, 600);
		shopFrame.setLocationRelativeTo(null);
		JButton closeButton = createButton(500,500,70,50,10,"戻る");
		closeButton.setActionCommand("カード購入を終える");
		shopBuy.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JLabel myMoney = createText(10,5,400,40,10,"所持金"+Player.player.getMoney());
		shopBuy.add(myMoney);

		for(int i=1; i<=canBuyCardlist.size(); i++) {
			JButton buyButton = createButton(500,i*50,70,50,10,"購入");
			buyButton.setActionCommand(canBuyCardlist.get(i-1).getName()+":b");
			if(canBuyCardlist.get(i-1).getBuyPrice() > Player.player.getMoney() || Player.player.getCardSize() > 7) {
				buyButton.setEnabled(false);
			}
			shopBuy.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,canBuyCardlist.get(i-1).getName());
			shopBuy.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(canBuyCardlist.get(i-1).getBuyPrice()));
			shopBuy.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}

		if(Player.player.getCardSize() > 7) {
			JLabel cardFull = createText(450,5,130,40,10,"カードがいっぱいです");
			shopBuy.add(cardFull);
		}
		shopFrame.setVisible(true);
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
        for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton useButton = createButton(10,35*(i+1)+30,70,30,10,"使用");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(i+1)+30,180,30,10,Player.player.getCardName(i));
        	JLabel labelText = createText(300,35*(i+1)+30,350,30,10,Player.player.getCardText(i));
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(Player.player.getCardName(i));
        	if(ContainsEvent.name(Player.player.getCard(i), "ダビングカード") && Player.player.getCardSize()<2) {
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

	/*
	//サイコロ画面表示
	private void printDice() {
		JLayeredPane diceP = diceFrame.getLayeredPane();
		diceFrame.setSize(600, 600);
		diceFrame.setLayout(null);

		//iconの取得が出来ない
		ImageIcon icon = new ImageIcon("./dice1.gif","description");
		assert icon.getDescription() == null : "sample";
		JLabel d1 = createImage(10,10,300,300,50,"./dice1.gif");
		JPanel p1 = new JPanel();
		p1.add(d1);
		diceFrame.getContentPane().add(p1);


		JButton button =createButton(490,450,70,50,10,"回す");
		JButton closeButton =createButton(490,500,70,50,10,"戻る");
		closeButton.setActionCommand("サイコロを閉じる");
		diceP.add(button);
		diceP.add(closeButton);
		// ウィンドウを表示
        diceFrame.setVisible(true);
	}
	 */

	//カードの複製を行う画面を表示
	public void printDubbing() {
		JLayeredPane dubbing = dubbingCardFrame.getLayeredPane();
		dubbingCardFrame.setSize(700,500);
		dubbingCardFrame.setLayout(null);
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int roop=0;roop<Player.player.getCardSize();roop++) {
        	JButton useButton = createButton(10,35*(roop+1)+30,70,30,10,"複製");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(roop+1)+30,180,30,10,Player.player.getCardName(roop));
        	JLabel labelText = createText(300,35*(roop+1)+30,350,30,10,Player.player.getCardText(roop));
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(Player.player.getCardName(roop)+"d");
        	dubbing.add(labelName);
        	dubbing.add(labelText);
        	dubbing.add(useButton);
        }
        dubbing.add(titleName);
        dubbing.add(titleText);

        dubbingCardFrame.setVisible(true);
        playFrame.setVisible(false);

        if(!Player.player.isPlayer()) {
	        Player.player.addCard(Player.player.getCard(0));//無いも考えず一番上のカードを複製
	        setCloseFrame(4);
        }
	}

	//月が替わった時に何月か表示
	private void printMonthFrame() {
		if(!playFrame.isShowing()) {
			while(!playFrame.isShowing()) {
				try {
    				Thread.sleep(100);
    			}catch(InterruptedException e) {

    			}
			}
		}
		playFrame.setVisible(false);
		JFrame monthFrame = new JFrame(App.month + "月");
		monthFrame.setSize(800,600);
		monthFrame.setLocationRelativeTo(null);
		monthFrame.getContentPane().add(createText(10,10,300,200,100,App.month+"月"));
		if(App.month>=3 && App.month<=5) {
			monthFrame.getContentPane().setBackground(Color.PINK);
		}else if(App.month>=6 && App.month<=8) {
			monthFrame.getContentPane().setBackground(Color.RED);
		}else if(App.month>=9 && App.month<=11) {
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

		if(ContainsEvent.isOwners()) {
			random2Event();
		}
	}

	//駅の物件情報を表示
	public void printPropertys(String name) {
		playFrame.setVisible(false);
		propertyFrame = new JFrame(name + "の物件情報");
		propertyFrame.setSize(800, 35*Japan.getStaInPropertySize(name)+150);
		propertyFrame.setLayout(null);
		propertyFrame.setLocationRelativeTo(null);
		propertyFrame.setVisible(false);
		Container propertys = propertyFrame.getContentPane();
		JButton closeButton = createButton(580,35*Japan.getStaInPropertySize(name)+50,180,50,10,"閉じる");
		closeButton.setActionCommand("物件情報を閉じる");

		propertys.add(createText(150,10,200,40,20,"物件名"));
		propertys.add(createText(400,10,150,40,20,"値段"));
		propertys.add(createText(550,10,100,40,20,"利益率"));
		propertys.add(createText(650,10,100,40,20,"所有者"));
		if(Japan.getStation(name).isMono()) {
			JLabel label = createText(750,10,30,40,20,"独");
			label.setBackground(Color.RED);
			propertys.add(label);
		}
		for(int i=0;i<Japan.getStaInPropertySize(name);i++) {
			String property = Japan.getStaInProperty(name,i).getName();//名前
			String owner = Japan.getStaInProperty(name,i).getOwner();//管理者
			int money = Japan.getStaInProperty(name,i).getAmount();//購入金額
			JButton buyButton = createButton(20,15+(i+1)*35,80,30,10,"購入");
			if(mapFrame.isShowing() || Japan.getStaInProperty(name,i).getLevel()>=2
					|| (!owner.equals("") && !owner.equals(Player.player.getName())) || Player.player.getMoney()<Japan.getStaInProperty(name,i).getAmount()) {
				buyButton.setEnabled(false);
			}
			for(String already:Japan.alreadys) {
				if(already.equals(Japan.getStaInProperty(name,i).getName()+i)) {
					buyButton.setEnabled(false);
					//sellButton.setEnabled(false);
					break;
				}
			}
			buyButton.setActionCommand(name+"b:"+i);
			propertys.add(buyButton);
			int rate = Japan.getStaInProperty(name,i).getRate();//利益率(3段階)
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

		if(!Player.player.isPlayer()) {
			Player.player.buyPropertysCPU(name);
		}
		setCloseFrame(2);
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
		for(int i=1; i<=Player.player.getCardSize(); i++) {
			JButton sellButton = createButton(500,i*50,70,50,10,"売却");
			sellButton.setActionCommand(Player.player.getCardName(i-1)+":s");
			shopSell.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,Player.player.getCardName(i-1));
			shopSell.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(Player.player.getCard(i-1).getSellPrice()));
			shopSell.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}
		shopFrame.setVisible(true);
	}

	//店イベント
	public void printShop(ArrayList<Card> cardList) {
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
		if(Player.player.getCardSize()==0) {
			sellButton.setEnabled(false);
		}
		if(!Player.player.isPlayer()) {
			closeButton.setEnabled(false);
			buyButton.setEnabled(false);
			sellButton.setEnabled(false);
		}
		canBuyCardlist.addAll(cardList);
		shop.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
		shopFrontFrame.setVisible(true);
		setCloseFrame(1);
	}

	public int[] printStart() {
  		JFrame startFrame = new JFrame("桃大郎電鉄");
    	startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
    	startFrame.setSize(800,600);
    	startFrame.setLayout(null);
    	startFrame.setLocationRelativeTo(null);
    	JLayeredPane start = startFrame.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(280, 10, 400, 60);
    	JLabel labelYear = new JLabel("何年プレイしますか？");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 50, 200, 50);
    	JTextField textYear = new JTextField("3");
    	textYear.setBounds(20, 100, 200, 50);
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 150, 300, 50);
    	JTextField textPlayerCount = new JTextField("1");
    	textPlayerCount.setBounds(20, 200, 200, 50);
    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	start.add(labelTitle);
    	start.add(labelYear);
    	start.add(textYear);
    	start.add(labelPlayers);
    	start.add(textPlayerCount);
    	start.add(startButton);

    	startFrame.setVisible(true);

    	WaitThread wait = new WaitThread(10);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}

    	startFrame.setVisible(false);

    	int[] list= {0,0};
    	list[0] = Integer.parseInt(textPlayerCount.getText());
    	list[1] = Integer.parseInt(textYear.getText());
    	return list;
  	}

	//持ち物件を売却するための画面を表示
	public void printTakeStations() {
		playFrame.setVisible(false);
		int takeProCount=0;
		int i=0;
		sellStationFrame = new JFrame("売却");
		JLayeredPane sellStation = sellStationFrame.getLayeredPane();
		sellStation.add(createText(150,10,200,40,20,"物件名"));
		sellStation.add(createText(400,10,150,40,20,"値段"));
		sellStation.add(createText(550,10,100,40,20,"利益率"));
		sellStation.add(createText(650,10,100,40,20,"所有者"));
		for(Property property:Player.player.getPropertys()) {
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
		sellStationFrame.setSize(800, 35*Player.player.getPropertys().size()+150);

		sellStationFrame.setVisible(true);

		if(!Player.player.isPlayer()) {
			Player.player.sellPropertyCPU(this);
		}
	}

	//randomイベント
	public void randomEvent() {
		Random rndm = new Random();
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
		if(!Player.player.isPlayer()) {
			closeButton.setEnabled(false);
		}
		random.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		double rand = RandomEvent.randomEvent();

		if(rand < 0.1) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を1/4失った");
		}else if(rand < 0.2) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を半分失った");
		}else if(rand < 0.3) {
			randomFrame.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を全て失った");
		}else if(rand < 0.4) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"1億円もらった");
		}else if(rand < 0.5) {
			randomFrame.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"2億円もらった");
		}else if(rand < 0.6) {
			randomFrame.setName("しあわせの小鳥");
			text1 = createText(10,10,600,100,20,"ちゅんちゅんちゅん");
			text2 = createText(10,110,600,100,20,"ちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅん");
			text3 = createText(10,210,600,100,20,"ちゅんちゅんちゅん(一頭地を抜くカードをもらった)");
		}else if(rand < 0.7 && ContainsEvent.isOwners()) {
			randomFrame.setName("鋼鉄の人");
			text1 = createText(10,10,600,100,20,"全身赤い装甲に身を包んだアメリカの空飛ぶ天才発明家が現れた！");
			text2 = createText(10,110,600,100,20,"Destroying properties at random！Don't hold a grudge.");
			//誰かの物件の所有権を初期化
			for(Property property : Japan.getPropertys()) {
				if(ContainsEvent.isOwner(property)) {
					text3 = createText(10,210,600,100,20,"誰かの物件が破壊された(" + property.getOwner() + "の" + property.getName() + ")");
					break;
				}
			}
		}else if(rand < 0.8) {
			randomFrame.setName("偉い人");
			text1 = createText(10,10,600,100,20,"偉い人が現れた！");
			text2 = createText(10,110,600,100,20,"山形の開発工事を行いたいを思っているので所持金全部投資してください");
			if(rndm.nextInt(100) < 50) {
				text3 = createText(10,210,600,100,20,"事業が成功し、所持金が倍になります");
			}else {
				text3 = createText(10,210,600,100,20,"事業が失敗し、所持金が無くなります");
			}
		}else if(rand < 0.9) {
			randomFrame.setName("スキャンダル");
			text1 = createText(10,10,600,100,20,"若者とキャッキャウフフしていたのがばれた");
			text2 = createText(10,110,600,100,20,"世間体を気にして移動を自粛することにした");
			if(ContainsEvent.isEffect()) {
				text3 = createText(10,210,600,100,20,"移動距離が-3される");
			}else {
				text3 = createText(10,210,600,100,20,"3カ月の間、移動距離が-3される");
			}
		}else {
			randomFrame.setName("富士山");
			text1 = createText(10,10,600,100,20,Player.player.getName()+"「富士山に行きたい！！！」");
			text2 = createText(10,110,600,100,20,"富士山の登頂に成功し、気分が良くなった");
			text3 = createText(10,210,600,100,20,"登山費用として5000万円失った");
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

	/*
	 * ランダムイベント
	 * ①臨時収入
	 * Player.playerが所有する物件の中から1件選び臨時収入が入る
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
	private void random2Event() {
		int rndnum;
		rndnum = new Random().nextInt(11)+1;
		//System.out.println("year:"+App.year+"\tmonth:"+App.month+"\trndnum:"+rndnum);


		if(App.month==rndnum) {
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
    		//System.out.println("所持金");
    		/*
    		for(int i=0;i<4;i++) {
    			System.out.println(Player.players.get(i).getName()+":"+Player.players.get(i).getMoney());
    		}
    		*/

    		//物件の情報を取得
    		for(Property property : Japan.getPropertys()) {
    			//オーナーの有無の判断
    			if(ContainsEvent.isOwner(property)) {
    				//物件が1かどうか判断
    				if(property.getGroup()==1) {
    					//物件の選出
    					text4 = createText(10,310,600,100,20,"臨時収入が入ります(" + property.getOwner() + "の" + property.getName() + ")");
    					//System.out.println("臨時収入:"+property.getOwner()+"の"+ property.getName());
    					//臨時収入を追加
    					RandomEvent.random2Event(property,rndnum);
    					break;
    				}
    			}
        	}
    		//System.out.println("所持金");
    		/*
    		for(int i=0;i<4;i++) {
    			System.out.println(Player.players.get(i).getName()+":"+Player.players.get(i).getMoney());
    		}
    		*/

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

    		if(Player.player.isPlayer()) {
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

	//メイン画面の上に書いてあるプレイヤーの情報を更新
	public void reloadInfo() {
		mainInfo.setVisible(false);
		if(ContainsEvent.isEffect()){
			if(Player.player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス　効果発動中("+Player.player.getEffect()+")");
			}else if(Player.player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス　効果発動中("+Player.player.getEffect()+")");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億　"+Player.player.getMoney()%10000+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス　効果発動中("+Player.player.getEffect()+")");
			}
		}else {
			if(Player.player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス");
			}else if(Player.player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億　"+Player.player.getMoney()%10000+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Searcher.count+"マス");
			}
		}
		mainInfo.setVisible(true);
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
		for(float i=0;i<ClosingEvent.getProfitListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x=(int)(300*((i+1)/ClosingEvent.getProfitListSize()));								//x座標を算出
				int y=(int)(500-(400*ClosingEvent.getProfitList((int)i)[j]/(ClosingEvent.maxProfit-ClosingEvent.minProfit)));	//y座標を算出
				graph.setBounds(x,y,5,5);													//（x,y）をプロット
				graph.setBackground(Color.YELLOW);
				revenue.add(graph);
				//System.out.println("x:" + String.valueOf(x) + "\ty:" + String.valueOf(y));
				if(i==ClosingEvent.getProfitListSize()-1) {//ぬるぽ回避
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
				int x2=(int)(300*((i+2)/ClosingEvent.getProfitListSize()));									//翌年のx座標を算出
				int y2=(int)(500-(400*ClosingEvent.getProfitList((int)i+1)[j]/(ClosingEvent.maxProfit-ClosingEvent.minProfit)));		//翌年のy座標を算出

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
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,Player.players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			revenue.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerProfitLabel = createText(500,110+(100*i),100,40,10,String.valueOf(ClosingEvent.getProfitList(ClosingEvent.getProfitListSize()-1)[i]));
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
		revenueFrame.setVisible(false);
		revenue.removeAll();

		ClosingEvent.aggregateAssets(Player.players);
		Assets();
	}

	private void sellCard(Card card) {
		Player.player.sellCard(card);
		shopFrame.setVisible(false);
		shopFrame.removeAll();
		printSellShop();
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame(int id) {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			TimerTask task = new CPUTimerTask(this,id);
			timer.schedule(task, 1000);
		}
	}

	//目的地の色付け
	private void setGoalColor() {
		JLayeredPane play = playFrame.getLayeredPane();
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()==null)continue;
			if(play.getComponent(i).getName().equals(Japan.getGoalName())) {
				play.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}

	//物件売却処理
	public void sellPropertys(Property property) {
		Player.player.sellPropertys(property);

		//System.out.println(property.getName()+"を売却");
		sellStationFrame.setVisible(false);
		sellStationFrame.removeAll();
		if(Player.player.getPropertys().size()>0) {
			printTakeStations();
		}else {
			closeTakeStations();
		}
	}

	public static void throwEnd() {
		throwFlag=true;
	}

	public void waitButtonUpdate() {
  		if(Player.player.isPlayer()) {
  			waitButton.setVisible(false);
  		}else {
  			waitButton.setVisible(true);
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

