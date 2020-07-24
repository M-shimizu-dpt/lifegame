package lifegame.game;

import java.util.ArrayList;

import javax.swing.JLabel;

public class Player {
	public String name;//名前
	public int money;//所持金
	public int move;//進めるマス
	public int boost;//カードを使った時の移動用ブースト
	public ArrayList<Card> cards;//所持カード一覧(持てるカードは8枚まで)
	public Coordinates nowMass;//現在地
	public int shortest;//目的地までの最短距離(最短距離を求めるアルゴリズムを考える必要がある)
	public JLabel colt;//プレイヤーの駒
	
	public Player(String name,int money) {
		this.money=0;
		this.move=0;
		this.boost=0;
		cards = new ArrayList<Card>();
		setName(name);
		addMoney(money);
		nowMass = new Coordinates();
		this.nowMass.setValue(6, 9);
		clearMove();
	}
	
	public void addCard(Card card) {
		cards.add(card);
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
