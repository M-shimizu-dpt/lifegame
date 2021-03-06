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
import javax.swing.JLayeredPane;

import lifegame.game.event.CardEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.DiceEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.event.Searcher;
import lifegame.game.event.WaitThread;
import lifegame.game.event.search.NearestSearchThread;
import lifegame.game.main.App;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Ginga;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.print.frames.map.PlayFrame;
import lifegame.game.object.map.print.frames.property.SellPropertyFrame;

public class Player {
	public static Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報
	public static Player player;//操作中のプレイヤー

	private static boolean stopFlag;//一時停止用フラグ

	private Buff buff;//一定期間の持続効果
	private ArrayList<Card> cards;//所持カード一覧(持てるカードは8枚まで)
	private JLabel colt=new JLabel();//プレイヤーの駒
	private boolean cpuflag;
	private int goaldistance;
	private int id;//識別番号
	private String name;//名前
	private Coordinates nowMass;//現在地
	private long money;//所持金
	private int move;//進めるマス
	private ArrayList<Property> propertys;//プレイヤーが保有している物件情報
	private int mapID;

	public Player(String name,int money,int id,boolean cpuflag) {
		this.money=0;
		this.move=0;
		this.buff=new Buff();
		this.cards = new ArrayList<Card>();
		this.propertys = new ArrayList<Property>();
		this.id=id;
		this.mapID=0;
		this.cpuflag=cpuflag;
		setName(name);
		addMoney(money);
		this.nowMass = new Coordinates();
		this.nowMass.setValue(6, 9);//大阪
		clearMove();
		initGoalDistance();
	}

	public static Player getPlayer(int index) {
		return Player.players.get(index);
	}

	public static Player getNextPlayer() {
		if(Player.player.getID()==3) {
			return Player.getPlayer(0);
		}else {
			return Player.getPlayer(Player.player.getID()+1);
		}
	}

	public static Player getNormalPlayer() {
		int id=Player.player.getID();
		for(int i=id;i>-1;i--) {

			if(ContainsEvent.isNormalMap(Player.getPlayer(i)))return Player.getPlayer(i);
			if(i==0) {
				i=4;
			}
		}
		return null;
	}

	public static void initPlayers(PlayFrame playFrame,int playerCount) {
		for(int i=0;i<4;i++) {
			if(FrameEvent.getOrder()==0) {//順番入れ替え
				if(playerCount>i) {//player
					Player.players.put(FrameEvent.getPlayerOrder(i),new Player(FrameEvent.getName(i),1000,FrameEvent.getPlayerOrder(i),true));
				}else {	//CPU
					Player.players.put(FrameEvent.getPlayerOrder(i),new Player(FrameEvent.getName(i),1000,FrameEvent.getPlayerOrder(i),false));
				}
			}else {//順番初期値
				if(playerCount>i) {//player
					Player.players.put(i,new Player(FrameEvent.getName(i),1000,FrameEvent.getPlayerOrder(i),true));
				}else {//CPU
					Player.players.put(i,new Player(FrameEvent.getName(i),1000,FrameEvent.getPlayerOrder(i),false));
				}
			}
			Player.players.get(FrameEvent.getPlayerOrder(i)).setColt(playFrame.createText(401+400,301+900,20,20,10,Player.players.get(FrameEvent.getPlayerOrder(i)).getName()));
  	  		Player.players.get(FrameEvent.getPlayerOrder(i)).getColt().setBackground(Color.BLACK);
  	  		Player.players.get(FrameEvent.getPlayerOrder(i)).getColt().setName(Player.players.get(FrameEvent.getPlayerOrder(i)).getName());
  	  		playFrame.getLayeredPane().add(Player.players.get(FrameEvent.getPlayerOrder(i)).getColt(),JLayeredPane.PALETTE_LAYER,0);
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

	public void addMoney(long money) {
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

	//CPUの所持カードが最大を超えた場合、捨てるカードを選択
	public void cardFullCPU() {
		do{
			this.removeCard(this.getCard(0));
			priceSort(this.getCards());
		}while(this.getCardSize()>8);
	}

	public static void priceSort(ArrayList<Card> cards){
        Collections.sort(cards,new Comparator<Card>() {
        	public int compare(Card card1, Card card2) {
				return Integer.compare(card1.getBuyPrice(), card2.getBuyPrice());
			}
        });
    }

	public void clearMove() {
		this.move=0;
	}

	//CPU操作
	public void cpu() throws InterruptedException{
		FrameEvent.reloadMain();
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
				Card playercard = this.getCard(rand);
				CardEvent.useAbilitys(playercard);
				if(playercard.getID()==2) {
					FrameEvent.moveMaps();//移動した人を一番真ん中に表示する。(カードの使用者がどこに移動したか分かるように)
					Thread.sleep(2000);
				}
				if(ContainsEvent.isUsedRandomCard()) {
					FrameEvent.ableMenu();
					diceFlag=false;
					App.turnEnd();
				}
			}
		}
		if(diceFlag) {
			//処理を待たないと一瞬表示されるだけになる
			//FrameEvent.openDice();
			DiceEvent.movePlayer();
			Thread.sleep(500);
			//FrameEvent.closeDice();

			/*
			WaitThread waitthread = new WaitThread(4);//行くことが出来るマスの探索待ち
			waitthread.start();
			waitthread.join();
			*/

			if(Player.player.getGoalDistance()>=this.getMove() || !ContainsEvent.isNormalMap()) {//出目が目的地に届かないもしくは、目的地に着く場合
				cpuaddTrajectory();
			}else {//目的地を超えてしまう場合
				Searcher.searchShortestRoute(Player.player);
				Searcher.searchCanMoveMass(Player.player);
				//ゴールから最も近い移動可能マスを選出し、移動する
				boolean flag=false;
				//行くことが出来るマス取得
				NearestSearchThread searchthread = new NearestSearchThread();
				searchthread.setMass(Japan.getGoalCoor());//探索開始位置をゴールに設定
				for(Coordinates coor : Searcher.canMoveTrajectoryList.keySet()) {
					if(ContainsEvent.isGoal(coor)) {//目的地に行ける場合
						cpuMoveMaps(Searcher.canMoveTrajectoryList.get(coor).get(0));
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
					cpuMoveMaps(Searcher.canMoveTrajectoryList.get(Searcher.nearestMassToGoalList.get(0)).get(0));
				}
			}

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
	public void cpuaddTrajectory() throws InterruptedException{
		for(int i=0;i<Player.player.getMove();) {
			if(Player.isStop()) {
				WaitThread wait = new WaitThread(7);
				wait.start();
				wait.join();
			}
			ArrayList<Coordinates> list;
			Coordinates next=new Coordinates();
			if(ContainsEvent.isNormalMap()) {
				list=Japan.getLinks();
				for(Coordinates coor:list) {
					if(Japan.getGoalDistance(this.getNowMass())>Japan.getGoalDistance(coor)) {
						next=coor;
					}
				}
			}else if(ContainsEvent.isGingaMap()) {
				list=Ginga.getLinks();
				for(Coordinates coor:list) {
					if(Ginga.getGoalDistance(this.getNowMass())>Ginga.getGoalDistance(coor)) {
						next=coor;
					}
				}
			}else if(ContainsEvent.isBonbirasMap()) {
				//bonbiras
			}

			int x = this.getNowMass().getX()-next.getX();
			int y = this.getNowMass().getY()-next.getY();
			if(x==0) {
				if(y<0) {//下
					FrameEvent.moveMaps(0,-130);
				}else if(y>0) {//上
					FrameEvent.moveMaps(0,130);
				}
			}else if(y==0) {
				if(x>0) {//左
					FrameEvent.moveMaps(130,0);
				}else if(x<0) {//右
					FrameEvent.moveMaps(-130,0);
				}
			}
			FrameEvent.reloadInfo();
			Thread.sleep(300);
		}
	}

	//cpuの移動操作(指定された経路で移動)
	public void cpuMoveMaps(ArrayList<Coordinates> list) throws InterruptedException{
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
					FrameEvent.moveMaps(0,-130);
				}else if(y>0) {//上
					FrameEvent.moveMaps(0,130);
				}
			}else if(y==0) {
				if(x>0) {//左
					FrameEvent.moveMaps(130,0);
				}else if(x<0) {//右
					FrameEvent.moveMaps(-130,0);
				}
			}
			FrameEvent.reloadInfo();
			Thread.sleep(300);
			if(this.getMove()<=0)break;
		}
	}

	public Player getAnotherPlayer() {
		int rand;
		do {
			rand = new Random().nextInt(4);
		}while(rand != this.id);
		return players.get(rand);
	}
	
	public Player getAnotherPlayers() {
		int rand;
		do {
			rand = new Random().nextInt(4);
		}while(rand == this.id);
		return players.get(rand);
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
		return this.cards.size();
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

	public long getMoney() {
		return this.money;
	}

	public int getMove() {
		return this.move;
	}

	public String getName() {
		return this.name;
	}

	public String getCardName(int index) {
		return getCard(index).getName();
	}

	public String getCardText(int index) {
		return getCard(index).getText();
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
		this.goaldistance=500;
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

	public void sellPropertyCPU(SellPropertyFrame sellPropertyFrame) {
		Player.sortProperty(this.getPropertys());
		sellPropertyFrame.sellPropertys(this.getProperty(0));
	}

	public void setBonbirasMap() {
		setMapID(2);
	}

	public void setColt(JLabel colt) {
		this.colt = colt;
	}

	public void setGingaMap() {
		setMapID(1);
	}

	public void setGoalDistance(int distance) {//最短距離をセット
		this.goaldistance = distance;
	}

	public void setGoalDistance() {//最短距離をセット
		if(ContainsEvent.isNormalMap()) {
			this.goaldistance = Japan.getCoordinates(nowMass).getGoalDistance();
		}else if(ContainsEvent.isGingaMap()) {
			this.goaldistance = Ginga.getCoordinates(nowMass).getGoalDistance();
		}else if(ContainsEvent.isBonbirasMap()) {
			//bonbiras
		}
	}

	private void setMapID(int id) {
		this.mapID=id;
	}

	public int getMapID() {
		return this.mapID;
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x, y);
	}
	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	public void setMove(int move) {
		this.move=move;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNormalMap() {
		setMapID(0);
	}

	public void addBuff(int ability,int period) {
		getBuff().addBuff(ability, period);
	}
}


