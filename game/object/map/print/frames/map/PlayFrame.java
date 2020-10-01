package lifegame.game.object.map.print.frames.map;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.event.MassEvent;
import lifegame.game.event.MoveEvent;
import lifegame.game.event.Searcher;
import lifegame.game.event.WaitThread;
import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class PlayFrame extends FrameModel{
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
    private JPanel back = new JPanel();//メニューボタンの背景
    private JLabel mainInfo;//プレイマップで上に表示されるプレイヤー情報を表示するラベル
    private JLabel moveLabel;//後何マス移動できるか、目的地までの最短距離を表示するラベル

    public PlayFrame() {

    }
    public void init(int playerCount) {
    	this.setTitle("桃大郎電鉄");

		JLayeredPane play = this.getLayeredPane();

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
				drawLine(this.getLayeredPane(),j,i,distance,20);
			}
		}

		Player.initPlayers(this, playerCount);
		Player.initNowPlayer();



  	    Japan.initGoal();
  	    setGoalColor();

  	    initMaps();
  	    initMenu();

  	    moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count);
    	moveLabel.setName("moves");
    	play.setBackground(Color.ORANGE);
    	closeMoveButton();


	}

	//メイン画面でのメニューボタンを非表示
	public void closeMenu() {
		back.setVisible(false);
	}

	//メイン画面でのメニューボタンを無効
	public void enableMenu() {
		back.setEnabled(false);
	}

	public void ableMenu() {
		back.setEnabled(true);
	}

	private void massEvent() {
		closeMoveButton();
		MassEvent.massEvent(this.getLayeredPane().getComponentAt(400, 300).getName());
		ableMenu();
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

	//目的地の色付け
	public void setGoalColor() {
		JLayeredPane play = this.getLayeredPane();
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()==null)continue;
			if(play.getComponent(i).getName().equals(Japan.getGoalName())) {
				play.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}

	public void resetGoalColor() {
		this.getLayeredPane().getComponentAt(400, 300).setBackground(Color.WHITE);
	}


	//プレイマップの中央位置を初期位置(大阪)に設定
	private void initMaps() {
		JLayeredPane play = this.getLayeredPane();
		int x=-400;
		int y=-900;
		for(int i=0;i<play.getComponentCount();i++) {
			play.getComponent(i).setLocation(play.getComponent(i).getX()+x,play.getComponent(i).getY()+y);
		}
	}

	private void initMenu() {
		JLayeredPane play = this.getLayeredPane();
		mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count+"マス");
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(Player.player.getName()+Player.player.getMoney());
		play.add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
		playRight = createButton(730,250,50,40,10,"→");//プレイマップでの移動ボタン
  		playLeft = createButton(10,250,50,40,10,"←");//プレイマップでの移動ボタン
  		playTop = createButton(380,40,50,40,10,"↑");//プレイマップでの移動ボタン
  		playBottom = createButton(380,510,50,40,10,"↓");//プレイマップでの移動ボタン
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
		play.add(playRight,JLayeredPane.PALETTE_LAYER,0);
		play.add(playLeft,JLayeredPane.PALETTE_LAYER,0);
		play.add(playTop,JLayeredPane.PALETTE_LAYER,0);
		play.add(playBottom,JLayeredPane.PALETTE_LAYER,0);
		saikoro = createButton(650, 360, 90, 30,10, "サイコロ");//プレイマップでのサイコロボタン
  	    cardB = createButton(650, 400, 90, 30,10, "カード");//プレイマップでのカード一覧表示ボタン
  	    company = createButton(650, 440, 90, 30,10, "会社情報");//プレイマップでのプレイヤー情報一覧表示ボタン
  	    minimap = createButton(650, 480, 90, 30,10, "詳細マップ");//プレイマップでの詳細マップ表示ボタン
  	    allmap = createButton(650, 520, 90, 30,10, "全体マップ");//プレイマップでの全体マップ表示ボタン
  	    back.add(saikoro);//プレイマップでのサイコロボタン
  		back.add(cardB);//プレイマップでのカード一覧表示ボタン
  		back.add(company);//プレイマップでのプレイヤー情報一覧表示ボタン
  		back.add(minimap);//プレイマップでの詳細マップ表示ボタン
  		back.add(allmap);//プレイマップでの全体マップ表示ボタン
  		back.setBackground(Color.CYAN);
  		back.setBounds(640,400,110,150);
  		back.setName("ボタン背景");
  	    play.add(back,JLayeredPane.PALETTE_LAYER,0);
  		waitButton = createButton(10,520,60,30,10,"stop");
  	    waitButton.setEnabled(true);
  	    play.add(waitButton,JLayeredPane.PALETTE_LAYER,0);
	}

	//次のプレイヤーをプレイ画面の真ん中に位置させる
	public void moveMaps() {
		JLayeredPane play = this.getLayeredPane();
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

	//プレイマップの画面遷移処理
	public void moveMaps(int x,int y) {
		String name;//if文が長すぎる為
		JLayeredPane play = this.getLayeredPane();

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
		JLayeredPane play = this.getLayeredPane();
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

	public String getNowMassName() {
		return this.getLayeredPane().getComponentAt(400, 300).getName();
	}

	//メイン画面での移動ボタンを表示
	public void printMoveButton() {
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
			Searcher.searchShortestRoute(Player.player);
			WaitThread thread = new WaitThread(2);
			thread.setDaemon(true);
			thread.start();
			try {
				thread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		moveLabel.setText("残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count);
		moveLabel.setVisible(true);
		this.getLayeredPane().add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
	}

	public void waitButtonUpdate() {
  		if(Player.player.isPlayer()) {
  			waitButton.setVisible(false);
  		}else {
  			waitButton.setVisible(true);
  		}
  	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("start")) {
			waitButton.setText("stop");
			waitButton.setActionCommand("stop");
			Player.setStopFlag(false);
		}else if(cmd.equals("stop")) {
			waitButton.setText("start");
			waitButton.setActionCommand("start");
			Player.setStopFlag(true);
		}else if(cmd.equals("サイコロ")) {
			FrameEvent.openDice();
		}else if(cmd.equals("カード")) {
			FrameEvent.openCard();
		}else if(cmd.equals("会社情報")) {
			FrameEvent.openInfo();
		}else if(cmd.equals("詳細マップ")) {
			FrameEvent.openMiniMap();
		}else if(cmd.equals("全体マップ")) {
			FrameEvent.openAllMap();
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
		}
	}
}
