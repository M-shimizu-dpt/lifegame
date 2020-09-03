package lifegame.game;

import java.util.ArrayList;

import javax.swing.JLabel;

public class Player {
	public String name;//名前
	public int money;//所持金
	public int move;//進めるマス
	public Buff buff;//一定期間の持続効果
	public ArrayList<Card> cards;//所持カード一覧(持てるカードは8枚まで)
	public Coordinates nowMass;//現在地
	public int shortest;//目的地までの最短距離(最短距離を求めるアルゴリズムを考える必要がある)
	public JLabel colt;//プレイヤーの駒
	public ArrayList<Property> propertys;//プレイヤーが保有している物件情報

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
		this.addMoney(card.sellPrice);
	}

	public void buyCard(Card card) {
		this.addCard(card);
		this.addMoney(-card.buyPrice);
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
	public boolean isEffect() {
		if(this.effect != 0) {
			return true;
		}else {
			return false;
		}
	}
}
