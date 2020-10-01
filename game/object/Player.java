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

	public static void initPlayers(PlayFrame playFrame,int playerCount) {
		for(int i=0;i<4;i++) {
  			if(playerCount>i) {//プレイヤー
	  			Player.players.put(i,new Player("player"+(i+1),1000,i,true));
	  		}else {//CPU
  				Player.players.put(i,new Player("CPU"+(i+1-playerCount),1000,i,false));
  			}
  			Player.players.get(i).setColt(playFrame.createText(401+400,301+900,20,20,10,Player.players.get(i).getName()));
  	  		Player.players.get(i).getColt().setBackground(Color.BLACK);
  	  		Player.players.get(i).getColt().setName(Player.players.get(i).getName());
  	  		playFrame.getLayeredPane().add(Player.players.get(i).getColt(),JLayeredPane.PALETTE_LAYER,0);
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

	//CPUの所持カードが最大を超えた場合、捨てるカードを選択
	public void cardFullCPU() {
		do{
			this.removeCard(this.getCard(0));
			//System.out.println("remove:"+this.getCard(0).getName());
			priceSort(this.getCards());
		}while(this.getCardSize()>8);
		FrameEvent.throwEnd();
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
		FrameEvent.closeMoveButton();
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
				CardEvent.useAbilitys(rand);
				if(movedflag) {
					FrameEvent.moveMaps();//移動した人を一番真ん中に表示する。(カードの使用者がどこに移動したか分かるように)
					Thread.sleep(2000);
				}
				if(ContainsEvent.isUsedRandomCard() || ContainsEvent.isUsedOthersCard()) {
					CardEvent.resetFlags();
					FrameEvent.ableMenu();
					diceFlag=false;
					App.turnEnd();
				}
			}
		}
		if(diceFlag) {
			//処理を待たないと一瞬表示されるだけになる
			FrameEvent.openDice();
			Thread.sleep(500);
			DiceEvent.shuffleDice();
			Thread.sleep(500);
			FrameEvent.closeDice();

			WaitThread waitthread = new WaitThread(4);//行くことが出来るマスの探索待ち
			waitthread.start();
			waitthread.join();

			if(Searcher.count>=this.getMove()) {//出目が目的地に届かないもしくは、目的地に着く場合
				cpuaddTrajectory();
			}else {//目的地を超えてしまう場合
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
	public void cpuaddTrajectory() throws InterruptedException{
		if(Searcher.nearestTrajectoryList.get(Searcher.count).size()>0) {
			ArrayList<Coordinates> list = Searcher.nearestTrajectoryList.get(Searcher.count).get(0);
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
				Thread.sleep(300);
				if(this.getMove()<=0)break;
			}
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

	public void addBuff(int ability,int period) {
		getBuff().addBuff(ability, period);
	}
}


