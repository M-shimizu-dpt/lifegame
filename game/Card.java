package lifegame.game;

import java.util.ArrayList;

public class Card {
	public String name;//名前
	public String cardText;//能力説明
	public int moveAbility;//サイコロ数の変化能力
	public int fixedMoveAbility;//固定値移動能力
	public int randomMoveAbility;//ランダム移動能力
	public int moneyAbility;//金銭能力
	public int sellPrice;//売る時の値段
	public int buyPrice;//買う時の値段
	public static ArrayList<Card> cardList = new ArrayList<Card>();

	public Card() {

	}

	public Card(String name,int buy,String cardText) {
		this.name = name;
		this.sellPrice = buy/2;
		this.buyPrice = buy;
		this.moveAbility=0;
		this.fixedMoveAbility=0;
		this.randomMoveAbility=0;
		this.moneyAbility=0;
		this.cardText=cardText;
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
		this.moveAbility = ability;
	}

	public int useAbility() {
		if(moveAbility>0) {
			return moveAbility;
		}
		if(fixedMoveAbility>0) {
			return fixedMoveAbility;
		}
		if(moneyAbility>0) {
			return moneyAbility;
		}
		return 0;
	}

	public Coordinates useRandomAbility() {
		Coordinates movedMass=new Coordinates();
		if(randomMoveAbility>0) {

		}
		return movedMass;
	}

	public static void init() {
		//周遊カードシリーズ(使う度にrandom関数を使い壊れるかを判定、5回使っても壊れなかった場合強制破壊)
		cardList.add(new Card("急行カード",400,"サイコロを2つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(2);
		cardList.add(new Card("急行周遊カード",8000,"何度かサイコロを2つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(2);
		cardList.add(new Card("特急カード",4000,"サイコロを3つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(3);
		cardList.add(new Card("特急周遊カード",30000,"何度かサイコロを3つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(3);
		cardList.add(new Card("新幹線カード",7000,"サイコロを4つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(4);
		cardList.add(new Card("新幹線周遊カード",50000,"何度かサイコロを4つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(4);
		cardList.add(new Card("のぞみカード",20000,"サイコロを5つ回すことが出来る"));
		cardList.get(cardList.size()-1).setMoveAbility(5);


		//固定値進む
		cardList.add(new Card("足踏みカード",4000,"その場に留まることが出来る"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(0);
		cardList.add(new Card("1進めるカード",10000,"1マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(1);
		cardList.add(new Card("2進めるカード",10000,"2マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(2);
		cardList.add(new Card("3進めるカード",10000,"3マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(3);
		cardList.add(new Card("4進めるカード",10000,"4マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(4);
		cardList.add(new Card("5進めるカード",10000,"5マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(5);
		cardList.add(new Card("6進めるカード",10000,"6マス進める"));
		cardList.get(cardList.size()-1).setFixedMoveAbility(6);

		/*
		//どこかへ移動する
		cardList.add(new Card("ぶっとびカード",10000,"どこかに移動することが出来る"));
		cardList.add(new Card("ぶっとび周遊カード",40000,"何度かどこかに移動することが出来る"));
		cardList.add(new Card("北へ！カード",10000,"北に移動することが出来る"));
		cardList.add(new Card("ピッタリカード",14000,"誰かと同じマスに移動することが出来る"));
		cardList.add(new Card("サミットカード",16000,"他の人を呼び寄せることが出来る"));
		cardList.add(new Card("最寄り駅カード",10000,"最寄り駅に移動することが出来る"));
		cardList.add(new Card("星に願いをカード",40000,"最寄りのカードショップに移動することが出来る"));


		//お金・カードがもらえる
		cardList.add(new Card("一頭地を抜くカード",40000,"一番お金を持っている人よりも多くのお金がもらえる"));
		cardList.add(new Card("起死回生カード",16000,"持ち金のマイナスがそのままプラスになる"));
		cardList.add(new Card("福袋カード",6000,"カードがたくさん出てくる"));
		cardList.add(new Card("ダビングカード",3000,"自分が持っているカードを複製することが出来る"));
		cardList.add(new Card("徳政令カード",500,"全ての人の借金を0にする"));

		*/
	}
}
