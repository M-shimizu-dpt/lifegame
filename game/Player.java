/*
 * プレイヤー(CPU含む)の情報を管理するクラス
 * プレイヤーが行う処理を記述
 */

package lifegame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.swing.JLabel;

public class Player {
	private String name;//名前
	private int money;//所持金
	private int move;//進めるマス
	private Buff buff;//一定期間の持続効果
	private ArrayList<Card> cards;//所持カード一覧(持てるカードは8枚まで)
	private Coordinates nowMass;//現在地
	private JLabel colt;//プレイヤーの駒
	private ArrayList<Property> propertys;//プレイヤーが保有している物件情報
	private int id;//識別番号
	private boolean cpuflag;

	private int goaldistance;
	private boolean bonby;//ボンビー識別
	private ArrayList<Integer> whowith;
	private int givebonby;
	private int getbonby;
	//private int givebonby;

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

		this.whowith = new ArrayList<Integer>();
		initGoalDistance();
		bonby = false;//ボンビー識別
		givebonby = 0;
		getbonby = 0;
	}

	public int getCardSize() {
		return cards.size();
	}

	public int getAnotherPlayer() {
		int rand;
		do {
			rand = new Random().nextInt(4);
		}while(rand != this.id);
		return rand;
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

	public boolean isPlayer() {
		return cpuflag;
	}

	public boolean containsID(int id) {
		return this.id==id;
	}

	public boolean containsID(Player player) {
		return this.id==player.id;
	}

	public void addProfit() {
		for(Property property:this.propertys) {
			this.addMoney(property.getProfit());
		}
	}

	public void addProperty(Property property) {
		this.propertys.add(property);
	}

	public void removeProperty(Property proerty) {
		this.propertys.remove(proerty);
	}

	public void addCard(Card card) {
		cards.add(card);
	}

	public void sellCard(Card card) {
		this.removeCard(card);
		this.addMoney(card.getSellPrice());
	}

	public void buyCard(Card card) {
		this.addCard(card);
		this.addMoney(-card.getBuyPrice());
	}

	public void removeCard(Card card) {
		cards.remove(card);
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x, y);
	}

	public void setMove(int move) {
		this.move=move;
	}

	public void clearMove() {
		this.move=0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public String getName() {
		return this.name;
	}

	public int getMoney() {
		return this.money;
	}

	public int getMove() {
		return this.move;
	}

	public Buff getBuff() {
		return this.buff;
	}

	public ArrayList<Card> getCards(){
		return this.cards;
	}

	public Card getCard(int index) {
		return this.cards.get(index);
	}

	public Coordinates getNowMass() {
		return this.nowMass;
	}

	public int getRandomEnemy() {
		Random rand = new Random();
		int enemy;
		do {
			enemy = rand.nextInt(4);
		}while(enemy==this.id);
		return enemy;
	}

	public int getID() {
		return this.id;
	}

	public void setColt(JLabel colt) {
		this.colt = colt;
	}

	public JLabel getColt() {
		return this.colt;
	}

	public ArrayList<Property> getPropertys() {
		return this.propertys;
	}

	public Property getProperty(int index) {
		return this.propertys.get(index);
	}

	public boolean isEffect() {
		if(getBuff().effect != 0) {
			return true;
		}else {
			return false;
		}
	}

	public static void sortProperty(ArrayList<Property> propertys) {
		Collections.sort(propertys, new Comparator<Property>() {
			public int compare(Property property1, Property property2) {
				return Integer.compare(property1.getAmount(), property2.getAmount());
			}
		});
	}

	public void setBonby(boolean bonbyTF) {//ボンビーついたら変更
		this.bonby = bonbyTF;
	}

	public boolean isBonby() {//ボンビーついているか取得
		return this.bonby;
	}


	public void setGoalDistance(int distance) {//最短距離をセット
		this.goaldistance = distance;
	}

	public boolean containsGoalDistance(int distance) {
		return this.goaldistance>distance;
	}

	public int getGoalDistance() {//最短距離を取得
		return this.goaldistance;
	}

	public void initGoalDistance() {
		this.goaldistance=100;
	}

	public void addSameMossPlayer(int who) {//だれと一緒にいるか変更
		this.whowith.add(who);
	}

	public ArrayList<Integer> getSameMossPlayers() {//だれと一緒にいるか取得
		return this.whowith;
	}
	public void sameMossPlayersClear() {//だれと一緒にいるかクリア

		if(this.whowith!=null) {
			this.whowith.clear();
		}
	}

	public void setBonbyAfter(int id) {//ボンビーだれにあげたか変更
		this.givebonby = id;
	}

	public int getBonbyAfter() {//ボンビーだれにあげたか取得
		return this.givebonby;
	}
	public void clearBonbyAfter() {//ボンビーだれにあげたか初期化
		this.givebonby = -1;
	}
	public void setBonbyBefore(int id) {//ボンビーだれからもらったかか変更
		this.getbonby = id;
	}
	public int getBonbyBefore() {//ボンビーだれからもらったか取得
		return this.getbonby;
	}
	public void clearBonbyBefore() {//ボンビーだれからもらったか初期化
		this.getbonby = -1;
	}
}

class Buff{
	int effect;
	int period;
	public Buff() {
		this.effect=0;
		this.period=0;
	}
	//新規effect
	public void addBuff(int effect,int period) {
		this.period=period;
		this.effect=effect;
		System.out.println("effect:"+effect+"     period:"+period);
	}
	public void elapsed() {
		if(period > 0) {
			this.period--;
		}
		if(period == 0) {
			clearEffect();
		}else if(period < 0) {
			System.out.println("periodの値が不適切です。");
		}
	}
	public void clearEffect() {
		this.effect=0;
	}

}
