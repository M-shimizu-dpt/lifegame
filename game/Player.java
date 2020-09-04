package lifegame.game;

import java.util.ArrayList;

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

	public Player(String name,int money) {
		this.money=0;
		this.move=0;
		this.buff=new Buff();
		this.cards = new ArrayList<Card>();
		this.propertys = new ArrayList<Property>();
		setName(name);
		addMoney(money);
		this.nowMass = new Coordinates();
		this.nowMass.setValue(6, 9);
		clearMove();
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

	public void setColt(JLabel colt) {
		this.colt = colt;
	}

	public JLabel getColt() {
		return this.colt;
	}

	public ArrayList<Property> getPropertys() {
		return this.propertys;
	}

	public boolean isEffect() {
		if(getBuff().effect != 0) {
			return true;
		}else {
			return false;
		}
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
