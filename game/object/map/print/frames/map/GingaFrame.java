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
import lifegame.game.event.MoveEvent;
import lifegame.game.event.Searcher;
import lifegame.game.main.App;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Ginga;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class GingaFrame extends FrameModel{
	private JButton gingaRight;//プレイマップでの移動ボタン
	private JButton gingaLeft;//プレイマップでの移動ボタン
	private JButton gingaTop;//プレイマップでの移動ボタン
	private JButton gingaBottom;//プレイマップでの移動ボタン
	private JButton saikoro;//プレイマップでのサイコロボタン
    private JButton cardB;//プレイマップでのカード一覧表示ボタン
    private JButton company;//プレイマップでのプレイヤー情報一覧表示ボタン
    private JButton minimap;//プレイマップでの詳細マップ表示ボタン
    private JButton allmap;//プレイマップでの全体マップ表示ボタン
    private JButton waitButton;//CPU操作中にプレーヤーが一時停止するためのボタン
    private JPanel back = new JPanel();//メニューボタンの背景
    private JLabel mainInfo;//プレイマップで上に表示されるプレイヤー情報を表示するラベル
    private JLabel moveLabel;//後何マス移動できるか、目的地までの最短距離を表示するラベル
    private boolean goalflag=false;

	public GingaFrame() {

	}
	public void init() {
		super.setTitle("挑大郎電鉄");
		int distance=130;
		this.getContentPane().setBackground(Color.DARK_GRAY);
		for(Coordinates coor : Ginga.getAllCoordinates()) {
			this.getLayeredPane().add(createMassInGinga(coor,distance),JLayeredPane.DEFAULT_LAYER,0);
			drawLineInGinga(this.getLayeredPane(),coor,distance,20);
		}

  	    initMaps();
  	    initMenu();
  	    enableMenu();

  	    moveLabel = createText(500,100,250,50,10,"残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Player.player.getGoalDistance());
    	moveLabel.setName("moves");
    	this.getLayeredPane().setBackground(Color.ORANGE);
    	closeMoveButton();
	}
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

	//メイン画面での移動ボタンを非表示
	public void closeMoveButton() {
		gingaLeft.setBackground(Color.WHITE);
		gingaRight.setBackground(Color.WHITE);
		gingaTop.setBackground(Color.WHITE);
		gingaBottom.setBackground(Color.WHITE);
		gingaLeft.setVisible(false);
		gingaRight.setVisible(false);
		gingaTop.setVisible(false);
		gingaBottom.setVisible(false);
		moveLabel.setVisible(false);
	}

	//メイン画面の上に書いてあるプレイヤーの情報を更新
	public void reloadInfo() {
		mainInfo.setVisible(false);
		if(ContainsEvent.isEffect()){
			if(Player.player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス　効果発動中("+Player.player.getEffect()+")");
			}else if(Player.player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス　効果発動中("+Player.player.getEffect()+")");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億　"+Player.player.getMoney()%10000+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス　効果発動中("+Player.player.getEffect()+")");
			}
		}else {
			if(Player.player.getMoney()<10000) {
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス");
			}else if(Player.player.getMoney()%10000==0){
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス");
			}else {//今登録している物件では呼ばれないかも
				mainInfo.setText("自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()/10000+"億　"+Player.player.getMoney()%10000+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"まで"+Ginga.getGoalDistance()+"マス");
			}
		}
		mainInfo.setVisible(true);
	}

	private void massEvent() {
		closeMoveButton();
		App.turnEnd();
		ableMenu();
	}

	public void moveMapsEvent() {
		massEvent();
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

	public void printMenu() {
		back.setVisible(true);
	}

	public String getNowMassName() {
		return this.getLayeredPane().getComponentAt(400, 300).getName();
	}

	public void printMoveButton() {
		ArrayList<Boolean> vector = new ArrayList<Boolean>();
		vector = Ginga.getVector(Ginga.getCoordinates(Player.player.getNowMass()),1);
		closeMoveButton();
		for(Coordinates coor:Ginga.getLinks(Player.player.getNowMass())) {
			if(Ginga.getGoalDistance(coor)>Ginga.getGoalDistance())continue;
			if(ContainsEvent.coor(coor, Player.player.getNowMass().getX()-1,Player.player.getNowMass().getY())) {
				gingaLeft.setBackground(Color.MAGENTA);
			}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX()+1,Player.player.getNowMass().getY())) {
				gingaRight.setBackground(Color.MAGENTA);
			}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX(),Player.player.getNowMass().getY()-1)) {
				gingaTop.setBackground(Color.MAGENTA);
			}else if(ContainsEvent.coor(coor, Player.player.getNowMass().getX(),Player.player.getNowMass().getY()+1)) {
				gingaBottom.setBackground(Color.MAGENTA);
			}
		}
		gingaLeft.setVisible(vector.get(0));
		gingaRight.setVisible(vector.get(1));
		gingaTop.setVisible(vector.get(2));
		gingaBottom.setVisible(vector.get(3));
		if(Player.player.getMove() <= 0) {
			closeMoveButton();
		}else {
			moveLabel.setText("残り移動可能マス数:"+Player.player.getMove()+"　"+Japan.getGoalName()+"までの最短距離:"+Ginga.getGoalDistance());
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

	public void addPlayer(Player player) {
		moveMapsStart(player);
		this.getLayeredPane().add(player.getColt(),JLayeredPane.PALETTE_LAYER);
	}
	public void removePlayer(Player player) {
		this.getLayeredPane().remove(player.getColt());
	}

	public void moveMapsStart(Player player) {
		JLayeredPane ginga = this.getLayeredPane();
		for(int i=0;i<ginga.getComponentCount();i++) {
			if(ginga.getComponent(i).getName()!=null && ginga.getComponent(i).getName().equals("S0")) {
				player.getColt().setLocation(ginga.getComponent(i).getX()+21,ginga.getComponent(i).getY()+21);
			}
		}
		player.setMass(Ginga.getStartCoor());
	}

	//次のプレイヤーをプレイ画面の真ん中に位置させる
	public void moveMaps() {
		JLayeredPane ginga = this.getLayeredPane();
		int x = 401 - Player.player.getColt().getX();
		int y = 301 - Player.player.getColt().getY();
		String name;//if文が長すぎる為
		//移動
		for(int i=0;i<ginga.getComponentCount();i++) {
			name=ginga.getComponent(i).getName();
			if(name==null) {
				ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+x,ginga.getComponent(i).getY()+y);
			}else if(!(name.equals("start") || name.equals("stop") || name.equals("右") || name.equals("左") || name.equals("下") ||
					name.equals("上") ||name.equals("サイコロ") || name.equals("会社情報") || name.equals("カード") ||
					name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()))) {//移動・閉じるボタン以外を動かす
				ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+x,ginga.getComponent(i).getY()+y);
			}
		}
	}

	public void moveMaps(int x,int y) {
		String name;//if文が長すぎる為
		JLayeredPane ginga = this.getLayeredPane();

		Coordinates coor = MoveEvent.movePlayerInGinga(x, y);

		for(int i=0;i<ginga.getComponentCount();i++) {
			name=ginga.getComponent(i).getName();
			if(name==null) {
				ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+coor.getX(),ginga.getComponent(i).getY()+coor.getY());
			}else if(!(name.equals("stop") || name.equals("start") || name.equals("右") || name.equals("左") ||
					name.equals("下") || name.equals("上") || name.equals("サイコロ") || name.equals("会社情報") ||
					name.equals("カード") || name.equals("詳細マップ") || name.equals("全体マップ") || name.equals("ボタン背景") ||
					name.equals(mainInfo.getName()) || name.equals(moveLabel.getName()) || name.equals(Player.player.getName()))) {//移動・閉じるボタン以外を動かす
				ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+coor.getX(),ginga.getComponent(i).getY()+coor.getY());
			}
		}

		MoveEvent.updateTrajectory(ginga.getComponentAt(400, 300).getName());

		if(ContainsEvent.isGoalInGinga(Player.player.getNowMass())) {
			//目的地に到着
			goal();
		}else if(Player.player.getMove()<=0) {
			MoveEvent.clearTrajectory();
			Dice.clear();
			BinboEvent.clearPredecessor();
			if(!ContainsEvent.isUsedRandomCard()) {
				massEvent();
			}
		}
	}

	public void moveMaps(Player player,Coordinates to) {
		JLayeredPane ginga = this.getLayeredPane();
		int x=(to.getX()-player.getNowMass().getX())*130;
		int y=(to.getY()-player.getNowMass().getY())*130;
		for(int i=0;i<ginga.getComponentCount();i++) {
			if(ginga.getComponent(i).getName()!=null && ginga.getComponent(i).getName().equals(player.getName())) {
				ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+x,ginga.getComponent(i).getY()+y);
			}
		}
		MoveEvent.moveTo(player, to);
	}

	private void goal() {
		Player.player.clearMove();
		MoveEvent.clearTrajectory();
		Dice.clear();
		BinboEvent.clearPredecessor();
		FrameEvent.closeMain();
		FrameEvent.transferMap(0);
		FrameEvent.reloadMain();
		massEvent();
		goalflag=true;
	}

	private void initMaps() {
		JLayeredPane ginga = this.getLayeredPane();
		int x=250;
		int y=150;
		for(int i=0;i<ginga.getComponentCount();i++) {
			ginga.getComponent(i).setLocation(ginga.getComponent(i).getX()+x,ginga.getComponent(i).getY()+y);
		}
	}

	private void initMenu() {
		JLayeredPane ginga = this.getLayeredPane();
		mainInfo = createText(10,10,770,30,17,"自社情報　"+"名前："+Player.player.getName()+"　持ち金："+Player.player.getMoney()+"万円　"+App.year+"年目　"+App.month+"月　"+Japan.getGoalName()+"までの最短距離:"+Searcher.count+"マス");
		mainInfo.setBackground(Color.BLUE);
		mainInfo.setName(Player.player.getName()+Player.player.getMoney());
		ginga.add(mainInfo,JLayeredPane.PALETTE_LAYER,0);
		gingaRight = createButton(730,250,50,40,10,"右");//プレイマップでの移動ボタン
  		gingaLeft = createButton(10,250,50,40,10,"左");//プレイマップでの移動ボタン
  		gingaTop = createButton(380,40,50,40,10,"上");//プレイマップでの移動ボタン
  		gingaBottom = createButton(380,510,50,40,10,"下");//プレイマップでの移動ボタン
		gingaRight.setText("→");
		gingaLeft.setText("←");
		gingaTop.setText("↑");
		gingaBottom.setText("↓");
		gingaRight.setVisible(false);
		gingaLeft.setVisible(false);
		gingaTop.setVisible(false);
		gingaBottom.setVisible(false);
		ginga.add(gingaRight,JLayeredPane.PALETTE_LAYER,0);
		ginga.add(gingaLeft,JLayeredPane.PALETTE_LAYER,0);
		ginga.add(gingaTop,JLayeredPane.PALETTE_LAYER,0);
		ginga.add(gingaBottom,JLayeredPane.PALETTE_LAYER,0);
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
  	    ginga.add(back,JLayeredPane.PALETTE_LAYER,0);
  		waitButton = createButton(10,520,60,30,10,"stop");
  	    waitButton.setEnabled(true);
  	    ginga.add(waitButton,JLayeredPane.PALETTE_LAYER,0);
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
			if(!goalflag) {
				printMoveButton();
				reloadInfo();
			}else {
				goalflag=false;
			}
		}
	}
}
