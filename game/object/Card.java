/*
 * カードの管理をするクラス
 * カード一覧や各処理などを記述
 *
 * id:能力カテゴリ
 * 0:サイコロ数を増やす
 * 1:固定値進む
 * 2:ランダム移動
 * 3:バフデバフ
 * 4:金銭授受
 * 5:カード授受
 */

package lifegame.game.object;

import lifegame.game.object.model.CardModel;

public class Card extends CardModel{

	public Card(String name,int buy,int rarity,String cardText,int id,int ability) {
		super.setName(name);
		super.setCount(0);
		super.setRarity(rarity);
		super.setSellPrice(buy/2);
		super.setBuyPrice(buy);
		super.setText(cardText);
		super.setID(id);
		super.setAbility(ability);
	}

	public Card(String name,int buy,int rarity,String cardText,int id) {
		super.setName(name);
		super.setCount(0);
		super.setRarity(rarity);
		super.setSellPrice(buy/2);
		super.setBuyPrice(buy);
		super.setText(cardText);
		super.setID(id);
	}

	public static void init() {
		resetFlags();
		//サイコロ数
		/*
		cardList.add(new Card("急行カード",400,1,"サイコロを2つ回すことが出来る",0,2));
		cardList.add(new Card("急行周遊カード",8000,2,"何度かサイコロを2つ回すことが出来る",0,2));
		cardList.add(new Card("特急カード",4000,2,"サイコロを3つ回すことが出来る",0,3));
		cardList.add(new Card("特急周遊カード",30000,3,"何度かサイコロを3つ回すことが出来る",0,3));
		cardList.add(new Card("新幹線カード",7000,3,"サイコロを4つ回すことが出来る",0,4));
		cardList.add(new Card("新幹線周遊カード",50000,4,"何度かサイコロを4つ回すことが出来る",0,4));
		cardList.add(new Card("のぞみカード",20000,4,"サイコロを5つ回すことが出来る",0,5));

		//固定値
		cardList.add(new Card("1進めるカード",10000,3,"1マス進める",1,1));
		cardList.add(new Card("2進めるカード",10000,3,"2マス進める",1,2));
		cardList.add(new Card("3進めるカード",10000,3,"3マス進める",1,3));
		cardList.add(new Card("4進めるカード",10000,3,"4マス進める",1,4));
		cardList.add(new Card("5進めるカード",10000,3,"5マス進める",1,5));
		cardList.add(new Card("6進めるカード",10000,3,"6マス進める",1,6));
		//どこかへ移動する
		cardList.add(new Card("ぶっとびカード",10000,1,"どこかに移動することが出来る",2));*/
		cardList.add(new Card("ぶっとび周遊カード",40000,2,"何度かどこかに移動することが出来る",2));/*
		cardList.add(new Card("北へ！カード",10000,1,"北に移動することが出来る",2));
		cardList.add(new Card("ピッタリカード",14000,2,"誰かと同じマスに移動することが出来る",2));
		cardList.add(new Card("サミットカード",16000,3,"他の人を呼び寄せることが出来る",2));
		//どこかへ移動した後にMassEvent
		cardList.add(new Card("足踏みカード",4000,3,"その場に留まることが出来る",2,0));
		cardList.add(new Card("最寄り駅カード",10000,2,"最寄り駅に移動することが出来る",2));
		cardList.add(new Card("星に願いをカード",40000,2,"最寄りのカードショップに移動することが出来る",2));

		//バフ・デバフ
		cardList.add(new Card("牛歩カード",4000,3,"しばらくの間、1マスしか進めません",3,-3));

		//お金がもらえる
		cardList.add(new Card("一頭地を抜くカード",40000,3,"一番お金を持っている人と同じだけお金がもらえる",4));
		cardList.add(new Card("起死回生カード",16000,2,"持ち金のマイナスがそのままプラスになる",4));
		cardList.add(new Card("徳政令カード",500,1,"全ての人の借金を0にする",4));

		//カードがもらえる
		cardList.add(new Card("福袋カード",6000,2,"カードがたくさん出てくる",5));
		cardList.add(new Card("ダビングカード",3000,2,"自分が持っているカードを複製することが出来る",5));

		//別次元に移動*/
		cardList.add(new Card("銀河鉄道カード",0,5,"銀河に行くことが出来る",6));

		Card.raritySort(Card.cardList);

	}
}