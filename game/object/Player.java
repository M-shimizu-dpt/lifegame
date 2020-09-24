/*
 * プレイヤー(CPU含む)の情報を管理するクラス
 * プレイヤーが行う処理を記述
 */

package lifegame.game.object;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;

import lifegame.game.event.WaitThread;
import lifegame.game.main.App;
import lifegame.game.map.information.Coordinates;
import lifegame.game.map.information.Property;
import lifegame.game.map.print.Window;
import lifegame.game.search.NearestSearchThread;
import lifegame.game.search.Searcher;

public class Player {
	public static Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報
	public static Player player;//操作中のプレイヤー

	public static int maxProfit=100;//最高収益(グラフ作成用)
	public static int minProfit=0;//最低収益(グラフ作成用)
	public static int maxAssets=100;//最高資産(グラフ作成用)
	public static int minAssets=0;//最低資産(グラフ作成用)

	private static ArrayList<Integer[]> allProfitList = new ArrayList<Integer[]>();//各プレイヤーの総収益(過去も含む)
	private static ArrayList<Integer[]> allAssetsList = new ArrayList<Integer[]>();//各プレイヤーの総資産(過去も含む)

	private static boolean stopFlag;//一時停止用フラグ

	private boolean bonby;//ボンビー識別
	private Buff buff;//一定期間の持続効果
	private ArrayList<Card> cards;//所持カード一覧(持てるカードは8枚まで)
	private JLabel colt;//プレイヤーの駒
	private boolean cpuflag;
	private int goaldistance;
	private int id;//識別番号
	private String name;//名前
	private Coordinates nowMass;//現在地
	private int money;//所持金
	private int move;//進めるマス
	private ArrayList<Property> propertys;//プレイヤーが保有している物件情報


	public Player(String name,int money,int id,boolean cpuflag) {
		this.money=0;
		this.move=0;
		this.buff=new Buff();
		this.cards = new ArrayList<Card>();
		this.propertys = new ArrayList<Property>();
		this.id=id;
		this.bonby = false;
		this.cpuflag=cpuflag;
		this.bonby = false;
		setName(name);
		addMoney(money);
		this.nowMass = new Coordinates();
		this.nowMass.setValue(6, 9);//大阪
		clearMove();

		initGoalDistance();
		bonby = false;//ボンビー識別

		//givebonby = 0;
		//getbonby = 0;
		//this.whowith = new ArrayList<Integer>();
	}

	public static void addProfitList(Integer[] list){
		Player.allProfitList.add(list);
	}

	public static void addAssetsList(Integer[] list){
		Player.allAssetsList.add(list);
	}

	public static ArrayList<Integer[]> getAssetsList(){
		return Player.allAssetsList;
	}

	public static Integer[] getAssetsList(int index){
		return Player.allAssetsList.get(index);
	}

	public static int getAssetsListSize(){
		return Player.allAssetsList.size();
	}

	public static Player getPlayer(int index) {
		return Player.players.get(index);
	}

	public static ArrayList<Integer[]> getProfitList(){
		return Player.allProfitList;
	}

	public static Integer[] getProfitList(int index){
		return Player.allProfitList.get(index);
	}

	public static int getProfitListSize(){
		return Player.allProfitList.size();
	}

	public static void initPlayers(Window window,int playerCount) {
		for(int i=0;i<4;i++) {
  			if(playerCount>i) {//プレイヤー
	  			Player.players.put(i,new Player("player"+(i+1),1000,i,true));
	  		}else {//CPU
  				Player.players.put(i,new Player("CPU"+(i+1-playerCount),1000,i,false));
  			}
  			Player.players.get(i).setColt(window.createText(401,301,20,20,10,Player.players.get(i).getName()));
  	  		Player.players.get(i).getColt().setBackground(Color.BLACK);
  	  		Player.players.get(i).getColt().setName(Player.players.get(i).getName());
  	  		window.addPlayFrame(Player.players.get(i).getColt());
  		}
	}

	public static void initNowPlayer() {
		Player.player=Player.players.get(0);
	}

	public static boolean isStop() {
		return Player.stopFlag;
	}

	public static void setNowPlayer() {
		Player.player=Player.players.get(App.turn);
	}

	public static void setStopFlag(boolean tf) {
		Player.stopFlag = tf;
	}

	public static void sortProperty(ArrayList<Property> propertys) {
		Collections.sort(propertys, new Comparator<Property>() {
			public int compare(Property property1, Property property2) {
				return Integer.compare(property1.getAmount(), property2.getAmount());
			}
		});
	}


	public void addCard(Card card) {
		cards.add(card);
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public void addProfit() {
		for(Property property:this.propertys) {
			this.addMoney(property.getProfit());
		}
	}

	public void addProperty(Property property) {
		this.propertys.add(property);
	}

	public void buyCard(Card card) {
		this.addCard(card);
		this.addMoney(-card.getBuyPrice());
	}

	//物件購入・増築処理
	public void buyPropertysCPU(String name) {
		for(int index = 0;index<App.japan.getStaInPropertySize(name);index++) {
			if(App.japan.getStaInProperty(name,index).getAmount() > this.getMoney())break;
			if(!App.japan.getStaInProperty(name,index).isOwner()) {
				App.japan.getStaInProperty(name,index).buy(this,0);
				if(App.japan.getStation(name).isMono()) {
					App.japan.monopoly(name);
				}
			}else {
				App.japan.getStaInProperty(name,index).buy(this);
			}
			App.japan.alreadys.add(App.japan.getStaInProperty(name,index).getName()+index);

			//System.out.println(App.japan.getStaInProperty(name,index).getName()+"を購入"+"("+index+")");
		}
	}

	//CPUの所持カードが最大を超えた場合、捨てるカードを選択
	public void cardFullCPU() {
		do{
			this.getCards().remove(this.getCard(0));
			//System.out.println("remove:"+this.getCard(0).getName());
			Card.priceSort(this.getCards());
		}while(this.getCardSize()>8);
		Window.throwEnd();
	}

	public void changeBonby() {//ボンビーついたら変更
		if(this.isBonby()) {
			this.bonby = false;
		}else {
			this.bonby = true;
		}
	}

	public void clearMove() {
		this.move=0;
	}

	public int containsGoalDistance(int distance) {
		if(this.goaldistance>distance) {
			return 1;
		}else if(this.goaldistance<distance){
			return -1;
		}else {
			return 0;
		}
	}

	public boolean containsID(int id) {
		return this.id==id;
	}

	public boolean containsID(Player player) {
		return this.id==player.id;
	}

	public int containsMoney(int money) {
		if(this.money>money) {
			return 1;
		}else if(this.money<money) {
			return -1;
		}else {
			return 0;
		}
	}

	//CPU操作
	public void cpu(Window window,int turn) throws InterruptedException{
		window.closeMoveButton();
		Thread.sleep(500);
		if(Player.isStop()) {
			WaitThread wait = new WaitThread(7);
			wait.start();
			wait.join();
		}
		boolean diceFlag=true;//サイコロを回すかどうか
		if(this.getCardSize()>0) {//カードがある場合確率で使用する（今は効果関係なく1/2で使用）
			//確率でカードを使用
			int rand = new Random().nextInt(this.getCardSize()*2);
			if(rand < this.getCardSize()) {
				boolean movedflag = this.getCard(rand).getID()==2;
				this.getCard(rand).useAbilitys(window);
				if(movedflag) {
					window.moveMaps();//移動した人を一番真ん中に表示する。(カードの使用者がどこに移動したか分かるように)
					Thread.sleep(2000);
				}
				if(Card.usedRandomCard || Card.usedOthersCard) {
					Card.resetUsedCard();
					Card.resetUsedFixedCard();
					Card.resetUsedRandomCard();
					Card.resetUsedOthersCard();
					window.ableMenu();
					diceFlag=false;
					App.turnEnd();
				}
			}
		}
		if(diceFlag) {
			window.diceShuffle();//サイコロを回す
			WaitThread waitthread = new WaitThread(4);//行くことが出来るマスの探索待ち
			waitthread.start();
			waitthread.join();

			if(Searcher.count>=this.getMove()) {//出目が目的地に届かないもしくは、目的地に着く場合
				cpuaddTrajectory(window);
			}else {//目的地を超えてしまう場合
				//ゴールから最も近い移動可能マスを選出し、移動する
				boolean flag=false;
				//行くことが出来るマス取得
				NearestSearchThread searchthread = new NearestSearchThread(window);
				searchthread.setMass(App.japan.getGoal());//探索開始位置をゴールに設定
				for(Coordinates coor : Searcher.canMoveTrajectoryList.keySet()) {
					if(coor.contains(App.japan.getGoal())) {//目的地に行ける場合
						cpuMoveMaps(window,Searcher.canMoveTrajectoryList.get(coor).get(0));
						flag=true;
						break;
					}
					searchthread.addGoal(coor);
				}
				if(!flag) {
					Searcher.nearestMassToGoalList.clear();
					searchthread.start();
					WaitThread wt = new WaitThread(2);
					wt.start();
					wt.join();

					//ゴールから最短にある移動可能マスを格納
					cpuMoveMaps(window,Searcher.canMoveTrajectoryList.get(Searcher.nearestMassToGoalList.get(0)).get(0));
				}
			}
			//System.out.println("player.move:"+this.getMove()+"       終わりました");
			if(this.getMove()>0) {
				System.out.println("異常終了");
			}
		}
		if(Player.isStop()) {
			WaitThread wait = new WaitThread(7);
			wait.start();
			wait.join();
		}
	}

	//cpuの移動操作(目的地までの最短経路で移動)
	public void cpuaddTrajectory(Window window) throws InterruptedException{
		if(Searcher.nearestTrajectoryList.get(Searcher.count).size()>0) {
			ArrayList<Coordinates> list = Searcher.nearestTrajectoryList.get(Searcher.count).get(0);
			for(Coordinates coor : list) {
				if(Player.isStop()) {
					WaitThread wait = new WaitThread(7);
					wait.start();
					wait.join();
				}
				//if(coor.contains(list.get(0)))continue;
				int x = this.getNowMass().getX()-coor.getX();
				int y = this.getNowMass().getY()-coor.getY();
				if(x==0) {
					if(y<0) {//下
						window.moveMaps(0,-130);
					}else if(y>0) {//上
						window.moveMaps(0,130);
					}
				}else if(y==0) {
					if(x>0) {//左
						window.moveMaps(130,0);
					}else if(x<0) {//右
						window.moveMaps(-130,0);
					}
				}
				Thread.sleep(300);
				if(this.getMove()<=0)break;
			}
		}
	}

	//cpuの移動操作(指定された経路で移動)
	public void cpuMoveMaps(Window window, ArrayList<Coordinates> list) throws InterruptedException{
		for(Coordinates coor : list) {
			if(Player.isStop()) {
				WaitThread wait = new WaitThread(7);
				wait.start();
				wait.join();
			}
			int x = this.getNowMass().getX()-coor.getX();
			int y = this.getNowMass().getY()-coor.getY();
			if(x==0) {
				if(y<0) {//下
					window.moveMaps(0,-130);
				}else if(y>0) {//上
					window.moveMaps(0,130);
				}
			}else if(y==0) {
				if(x>0) {//左
					window.moveMaps(130,0);
				}else if(x<0) {//右
					window.moveMaps(-130,0);
				}
			}
			Thread.sleep(300);
			if(this.getMove()<=0)break;
		}
	}

	public int getAnotherPlayer() {
		int rand;
		do {
			rand = new Random().nextInt(4);
		}while(rand != this.id);
		return rand;
	}

	public Buff getBuff() {
		return this.buff;
	}

	public Card getCard(int index) {
		return this.cards.get(index);
	}

	public ArrayList<Card> getCards(){
		return this.cards;
	}

	public int getCardSize() {
		return cards.size();
	}

	public JLabel getColt() {
		return this.colt;
	}

	public int getEffect() {
		return getBuff().getEffect();
	}

	public int getGoalDistance() {//最短距離を取得
		return this.goaldistance;
	}

	public int getID() {
		return this.id;
	}

	public int getMoney() {
		return this.money;
	}

	public int getMove() {
		return this.move;
	}

	public String getName() {
		return this.name;
	}

	public Coordinates getNowMass() {
		return this.nowMass;
	}

	public int getPeriod() {
		return getBuff().getPeriod();
	}

	public Property getProperty(int index) {
		return this.propertys.get(index);
	}

	public ArrayList<Property> getPropertys() {
		return this.propertys;
	}

	public void initGoalDistance() {
		this.goaldistance=100;
	}

	public boolean isBonby() {//ボンビーついているか取得
		return this.bonby;
	}

	public boolean isEffect() {
		if(getEffect() != 0) {
			return true;
		}else {
			return false;
		}
	}

	public boolean isPlayer() {
		return cpuflag;
	}

	public void removeProperty(Property proerty) {
		this.propertys.remove(proerty);
	}

	public void removeCard(Card card) {
		cards.remove(card);
	}

	public void sellCard(Card card) {
		this.removeCard(card);
		this.addMoney(card.getSellPrice());
	}

	public void sellPropertyCPU(Window window) {
		Player.sortProperty(this.getPropertys());
		window.sellPropertys(this.getProperty(0));
	}

	public void setColt(JLabel colt) {
		this.colt = colt;
	}

	public void setGoalDistance(int distance) {//最短距離をセット
		this.goaldistance = distance;
		//System.out.println(this.getGoalDistance()+"最長距離   :   名前 : "+this.getName());
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x, y);
	}

	public void setMove(int move) {
		this.move=move;
	}

	public void setName(String name) {
		this.name = name;
	}

}


