package lifegame.game.object.map.print.frames.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.event.MoveEvent;
import lifegame.game.main.App;
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
    	this.setTitle("挑大郎電鉄");

		JLayeredPane play = this.getLayeredPane();

		int distance=130;
		for(int i=1;i<=17;i++) {
			for(int j=1;j<=17;j++) {
				if(!ContainsEvent.isMassInJapan(j, i))continue;
				if(ContainsEvent.isStation(j,i)) {
					JLabel pre = createText(j*distance-20,i*distance-5,80,60,15,Japan.getStationName(j, i));
					pre.setBackground(Color.WHITE);
					play.add(pre,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
				}else {
					play.add(createMassInJapan(j,i,distance),JLayeredPane.DEFAULT_LAYER,0);
				}
				drawLineInJapan(this.getLayeredPane(),j,i,distance,20);
			}
		}

		Player.initPlayers(this, playerCount);
		Player.initNowPlayer();

  	    Japan.initGoal();
  	    setGoalColor();

  	    try {
			Thread.sleep(1000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}

  	    for(Player player:Player.players.values()) {
  	    	player.setGoalDistance();
  	    }
  	    initMaps();
  	    initMenu();
  	    enableMenu();

  	    moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Player.player.getGoalDistance());
    	moveLabel.setName("moves");
    	play.setBackground(Color.ORANGE);
    	closeMoveButton();
	}

    //CPU操作時のmenuを無効化したいが1ターン目のmenuが普通に表示され、その後は無効化ではなく非表示になっている
    public void open() {
    	if(ContainsEvent.isPlayer()) {
			ableMenu();
		}else {
			enableMenu();
		}
    	this.setVisible(true);
    }

    @Override
    public void close() {
    	this.setVisible(false);
    }

	//メイン画面でのメニューボタンを非表示
	public void closeMenu() {
		back.setVisible(false);
	}

	//メイン画面でのメニューボタンを無効
	public void enableMenu() {
		saikoro.setEnabled(false);
	    cardB.setEnabled(false);
	    company.setEnabled(false);
	    minimap.setEnabled(false);
	    allmap.setEnabled(false);
	}

	public void ableMenu() {
		if(ContainsEvent.isPlayer()) {
			able();
		}else {
			enableMenu();
		}
		if(ContainsEvent.isUsedCard()) {
			cardB.setEnabled(false);
		}
	}

	private void able() {
		saikoro.setEnabled(true);
	    cardB.setEnabled(true);
	    company.setEnabled(true);
	    minimap.setEnabled(true);
	    allmap.setEnabled(true);
	}

	private void massEvent() {
		closeMoveButton();
		App.turnEnd();
		//MassEvent.massEvent(this.getLayeredPane().getComponentAt(400, 300).getName());
		ableMenu();
	}

	//メイン画面での移動ボタンを非表示
	public void closeMoveButton() {
		playRight.setBackground(Color.WHITE);
		playLeft.setBackground(Color.WHITE);
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
			if(Player.player.getEffect()==-3) {
				mainInfo.setText("自社情報 "+"名前："+Player.player.getName()+" 持ち金："+FrameEvent.convertMoney(Player.player.getMoney())+" "+App.year+"年目 "+App.month+"月 "+Japan.getGoalName()+"まで"+Player.player.getGoalDistance()+"マス 牛歩ｶｰﾄﾞ効果発動中");
			}else {
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+FrameEvent.convertMoney(Player.player.getMoney())+"　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Player.player.getGoalDistance()+"マス　効果発動中("+Player.player.getEffect()+")");
			}
		}else {
			mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+FrameEvent.convertMoney(Player.player.getMoney())+"　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Player.player.getGoalDistance()+"マス");
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

	public void addPlayer(Player player) {
		this.getLayeredPane().add(player.getColt(),JLayeredPane.PALETTE_LAYER);
	}
	public void removePlayer(Player player) {
		this.getLayeredPane().remove(player.getColt());
	}

	public Component getGoalComponent() {
		for(Component comp:this.getLayeredPane().getComponents()) {
			if(ContainsEvent.isGoal(comp.getName())) {
				return comp;
			}
		}
		return null;
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
		mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+Player.player.getName()+"　持ち金："+FrameEvent.convertMoney(Player.player.getMoney())+"　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"までの最短距離:"+Player.player.getGoalDistance()+"マス");
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(Player.player.getName()+Player.player.getMoney());
		play.add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
		playRight = createButton(730,250,50,40,10,"右");//プレイマップでの移動ボタン
  		playLeft = createButton(10,250,50,40,10,"左");//プレイマップでの移動ボタン
  		playTop = createButton(380,40,50,40,10,"上");//プレイマップでの移動ボタン
  		playBottom = createButton(380,510,50,40,10,"下");//プレイマップでの移動ボタン
		playRight.setText("→");
		playLeft.setText("←");
		playTop.setText("↑");
		playBottom.setText("↓");
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

	//指定プレイヤーのcoltを画面の真ん中に位置させる
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

		Coordinates coor = MoveEvent.movePlayerInJapan(x, y);

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

		Player.player.setGoalDistance();

		if(Player.player.getMove()<=0) {
			MoveEvent.clearTrajectory();
			Dice.clear();
			BinboEvent.clearPredecessor();
			if(!ContainsEvent.isUsedRandomCard()) {
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

	//別マップから帰還時の画面遷移処理
	public void moveMaps(Player player,Coordinates to,String toName) {//駒の位置の配置方法を考える
		JLayeredPane play = this.getLayeredPane();
		for(int i=0;i<play.getComponentCount();i++) {
			if(play.getComponent(i).getName()!=null && play.getComponent(i).getName().equals(toName)) {
				player.getColt().setLocation(play.getComponent(i).getLocation().x+41,play.getComponent(i).getLocation().y+11);
			}
		}
		MoveEvent.moveTo(player, to);
	}

	//プレイマップの画面遷移処理//カード処理後MassEvent
	public void moveMapsEvent() {
		//CardEvent.resetFlags();
		massEvent();
	}

	//メイン画面でのメニューボタンを表示
	public void printMenu() {
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
		for(Coordinates coor:Japan.getLinks(Player.player.getNowMass())) {
			if(ContainsEvent.goalDistance(Player.player, Japan.getGoalDistance(coor))!=1)continue;
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
		playLeft.setVisible(vector.get(0));
		playRight.setVisible(vector.get(1));
		playTop.setVisible(vector.get(2));
		playBottom.setVisible(vector.get(3));
		if(Player.player.getMove() <= 0) {
			closeMoveButton();
		}else {
			moveLabel.setText("残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Player.player.getGoalDistance());
			moveLabel.setVisible(true);
			this.getLayeredPane().add(moveLabel,JLayeredPane.PALETTE_LAYER,0);
		}
	}

	public void waitButtonUpdate() {
  		if(ContainsEvent.isPlayer()) {
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
