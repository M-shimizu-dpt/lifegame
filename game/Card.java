package lifegame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Card {
	public String name;//名前
	public String cardText;//能力説明
	public int moveAbility;//サイコロ数の変化能力
	public int fixedMoveAbility;//固定値移動能力
	public int randomMoveAbility;//ランダム移動持ちかどうか(T/Fにすべき)
	public int othersAbility;//金銭能力持ちかどうか
	public int sellPrice;//売る時の値段
	public int buyPrice;//買う時の値段
	public int count;
	public int rarity;
	public static ArrayList<Card> cardList = new ArrayList<Card>();
	public static boolean usedCard;
	public static boolean usedFixedCard;
	public static boolean usedRandomCard;
	public static boolean usedOthersCard;
	private static Window window;

	public Card() {

	}

	public Card(String name,int buy,int rarity,String cardText) {
		this.name = name;
		this.count=0;
		this.rarity=rarity;
		this.sellPrice = buy/2;
		this.buyPrice = buy;
		this.moveAbility=0;
		this.fixedMoveAbility=-1;
		this.randomMoveAbility=0;
		this.othersAbility=0;
		this.cardText=cardText;
	}

	public static void resetUsedCard() {
		Card.usedCard=false;
	}

	public static void usedCard() {
		Card.usedCard=true;
	}

	public static void resetUsedFixedCard() {
		Card.usedFixedCard=false;
	}

	public static void usedFixedCard() {
		Card.usedFixedCard=true;
	}

	public static void resetUsedRandomCard() {
		Card.usedRandomCard=false;
	}

	public static void usedRandomCard() {
		Card.usedRandomCard=true;
	}

	public static void resetUsedOthersCard() {
		Card.usedOthersCard=false;
	}

	public static void usedOthersCard() {
		Card.usedOthersCard=true;
	}

	private void setMoveAbility(int ability) {
		this.moveAbility = ability;
	}
	private void setFixedMoveAbility(int ability) {
		this.fixedMoveAbility = ability;
	}
	private void setRandomMoveAbility(int ability) {
		this.randomMoveAbility = ability;
	}

	private void setMoneyAbility(int ability) {
		this.othersAbility = ability;
	}

	public int useAbility() {
		if(moveAbility>0) {
			return moveAbility;
		}
		if(fixedMoveAbility!=-1) {
			return fixedMoveAbility;
		}

		return 0;
	}

	public Coordinates useRandomAbility() {
		Random rand = new Random();
		Coordinates movedMass=new Coordinates();
		int x,y;
		while(true) {
			x=rand.nextInt(17);
			y=rand.nextInt(17);
			if(window.japan.contains(x, y)) {
				break;
			}
		}
		movedMass.setValue(x, y);
		System.out.println("random move  x:"+x+"  y:"+y);
		return movedMass;
	}

	public static void sort(){
        Collections.sort(Card.cardList,new Comp());
    }

	public static void init(Window window) {
		Card.window=window;
		resetUsedCard();
		resetUsedFixedCard();
		resetUsedRandomCard();
		resetUsedOthersCard();


		//周遊カードシリーズ(使う度にrandom関数を使い壊れるかを判定、5回使っても壊れなかった場合強制破壊)
		cardList.add(new Card("急行カード",400,1,"サイコロを2つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(2);
		cardList.add(new Card("急行周遊カード",8000,2,"何度かサイコロを2つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(2);
		cardList.add(new Card("特急カード",4000,2,"サイコロを3つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(3);
		cardList.add(new Card("特急周遊カード",30000,3,"何度かサイコロを3つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(3);
		cardList.add(new Card("新幹線カード",7000,3,"サイコロを4つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(4);
		cardList.add(new Card("新幹線周遊カード",50000,4,"何度かサイコロを4つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(4);
		cardList.add(new Card("のぞみカード",20000,4,"サイコロを5つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(5);


		//固定値進む
		cardList.add(new Card("足踏みカード",4000,3,"その場に留まることが出来る"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(0);
		cardList.add(new Card("1進めるカード",10000,3,"1マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(1);
		cardList.add(new Card("2進めるカード",10000,3,"2マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(2);
		cardList.add(new Card("3進めるカード",10000,3,"3マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(3);
		cardList.add(new Card("4進めるカード",10000,3,"4マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(4);
		cardList.add(new Card("5進めるカード",10000,3,"5マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(5);
		cardList.add(new Card("6進めるカード",10000,3,"6マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(6);

		cardList.add(new Card("牛歩カード",4000,3,"しばらくの間、誰かが進むマスを3マス減らす"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(-3);



		//どこかへ移動する
		cardList.add(new Card("ぶっとびカード",10000,1,"どこかに移動することが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		cardList.add(new Card("ぶっとび周遊カード",40000,2,"何度かどこかに移動することが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		cardList.add(new Card("北へ！カード",10000,1,"北に移動することが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		cardList.add(new Card("ピッタリカード",14000,2,"誰かと同じマスに移動することが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		cardList.add(new Card("サミットカード",16000,3,"他の人を呼び寄せることが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		cardList.add(new Card("最寄り駅カード",10000,2,"最寄り駅に移動することが出来る"));
		cardList.get(cardList.size()-1).setRandomMoveAbility(1);
		//cardList.add(new Card("星に願いをカード",2,40000,"最寄りのカードショップに移動することが出来る"));
		//cardList.get(cardList.size()-1).setRandomMoveAbility(1);


		//お金・カードがもらえる
		cardList.add(new Card("一頭地を抜くカード",40000,3,"一番お金を持っている人と同じだけお金がもらえる"));
		cardList.get(cardList.size()-1).setMoneyAbility(1);
		cardList.add(new Card("起死回生カード",16000,2,"持ち金のマイナスがそのままプラスになる"));
		cardList.get(cardList.size()-1).setMoneyAbility(1);
		cardList.add(new Card("福袋カード",6000,2,"カードがたくさん出てくる"));
		cardList.get(cardList.size()-1).setMoneyAbility(1);
		cardList.add(new Card("ダビングカード",3000,2,"自分が持っているカードを複製することが出来る"));
		cardList.get(cardList.size()-1).setMoneyAbility(1);
		cardList.add(new Card("徳政令カード",500,1,"全ての人の借金を0にする"));
		cardList.get(cardList.size()-1).setMoneyAbility(1);


		Card.sort();

	}
}

class Comp implements Comparator<Card>{
	public int compare(Card card1, Card card2) {
		return card1.rarity < card2.rarity ? -1 : 1;
	}

}
