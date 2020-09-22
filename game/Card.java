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

package lifegame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import lifegame.game.map.Coordinates;
import lifegame.game.player.Player;
import lifegame.game.search.Searcher;

public class Card {
	private String name;//名前
	private String cardText;//能力説明
	private int sellPrice;//売る時の値段
	private int buyPrice;//買う時の値段
	private int count;//カード破壊カウント(周遊用)
	private int rarity;
	private int ability;
	private int id;
	public static ArrayList<Card> cardList = new ArrayList<Card>();
	public static boolean usedCard;
	public static boolean usedFixedCard;
	public static boolean usedRandomCard;
	public static boolean usedOthersCard;

	public Card(String name,int buy,int rarity,String cardText,int id,int ability) {
		this.name = name;
		this.count=0;
		this.rarity=rarity;
		this.sellPrice = buy/2;
		this.buyPrice = buy;
		this.cardText=cardText;
		this.id=id;
		this.ability=ability;
	}

	public boolean contains(Card card) {
		return this.name.equals(card.name);
	}

	public boolean contains(String name) {
		return this.name.equals(name);
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public String getText() {
		return this.cardText;
	}

	public int getSellPrice() {
		return this.sellPrice;
	}

	public int getBuyPrice() {
		return this.buyPrice;
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setAbility(int ability) {
		this.ability = ability;
	}

	//0,1
	private void useAbility(Dice dice) {
		if(this.id==0) {
			dice.setNum(this.ability);
		}else if(this.id == 1) {
			Card.usedFixedCard();
			dice.setResult(this.ability);
		}
	}

	//2,5
	private void useAbility(Window window,Map<Integer,Player> players,int turn) {
		Random rand = new Random();
		if(this.id==2) {
			Coordinates coor = new Coordinates();
			//誰に影響を与えるのか
			Card.usedRandomCard();
			if(name.equals("サミットカード")) {
				coor.setValue(players.get(turn).getNowMass());
				for(int roop=0;roop<4;roop++) {
					if(roop==turn)continue;
					window.moveMaps(roop,coor);
				}
			}else if(name.equals("北へ！カード")) {
				do {
					coor = this.useRandomAbility();
				}while(players.get(turn).getNowMass().getY()<coor.getY());
			}else if(name.equals("ピッタリカード")){
				coor.setValue(players.get(rand.nextInt(4)).getNowMass());
			}else if(name.equals("最寄り駅カード")){
				Searcher.searchNearestStation(window,players.get(turn));
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestStationList.get(rand.nextInt(Searcher.nearestStationList.size())));
			}else if(name.equals("星に願いをカード")){
				Searcher.searchNearestShop(window,players.get(turn));
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestShopList.get(rand.nextInt(Searcher.nearestShopList.size())));
			}else {
				coor = this.useRandomAbility();
			}
			window.moveMaps(turn,coor);
			players.get(turn).getNowMass().setValue(coor);

		}else if(this.id==5) {
			if(name.equals("福袋カード")) {
				int count=0;
				do {
					int randcard = rand.nextInt(Card.cardList.size());
					players.get(turn).addCard(Card.cardList.get(randcard));
					if(players.get(turn).getCards().size()>8) {
						window.cardFull();
					}
					count++;
				}while(rand.nextInt(100)<50 && count<5);
			}else if(name.equals("ダビングカード")) {
				window.printDubbing();
			}
		}
	}

	//3,4
	private void useAbility(Map<Integer,Player> players,int turn) {
		if(this.id==3) {
			Random rand = new Random();
			int enemy = players.get(turn).getRandomEnemy();
			int period;
			do {
				period = rand.nextInt(5);
			}while(period <= 1);
			players.get(enemy).getBuff().addBuff(this.ability, period);
			System.out.println(players.get(turn).getName());
		}else if(this.id==4) {
			Card.usedOthersCard();
			if(name.equals("一頭地を抜くカード")) {
				int maxMoney=0;
				for(Player player:players.values()) {
					if(player.containsMoney(maxMoney)>0) {
						maxMoney=player.getMoney();
					}
				}
				players.get(turn).addMoney(maxMoney);
			}else if(name.equals("起死回生カード")) {
				if(players.get(turn).containsMoney(0)<0) {
					players.get(turn).addMoney(-players.get(turn).getMoney()*2);
				}
			}else if(name.equals("徳政令カード")) {
				for(int player=0;player<4;player++) {
					if(players.get(player).containsMoney(0)<0) {
						players.get(player).addMoney(-players.get(player).getMoney());
					}
				}
			}
		}
	}


	public void useAbility(Window window, Dice dice, Map<Integer,Player> players, int turn) {
		if(id==0 || id==1) {
			useAbility(dice);
		}else if(id==2 || id==5){
			useAbility(window,players,turn);
		}else if(id==3 || id==4) {
			useAbility(players,turn);
		}
		if(!players.get(turn).isPlayer()) System.out.println("Use Card!  "+name+"   user:"+players.get(turn).getName());//何を使ったか表示(ポップアップに変更すべき)

		//周遊カードの場合は確率でカードを破壊
		if(name.split("周遊").length==2) {
			this.setCount(this.getCount()+1);
			if(new Random().nextInt(100)<30 || this.getCount()>5) {
				players.get(turn).getCards().remove(this);
			}
		}else {
			players.get(turn).getCards().remove(this);
		}

		if(!name.equals("徳政令カード")) {
			Card.usedCard();//カードを使ったことにする
		}
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

	public Coordinates useRandomAbility() {
		Random rand = new Random();
		Coordinates movedMass=new Coordinates();
		int x,y;
		while(true) {
			x=rand.nextInt(17);
			y=rand.nextInt(17);
			if(Window.japan.contains(x, y)) {
				break;
			}
		}
		movedMass.setValue(x, y);
		System.out.println("random move  x:"+x+"  y:"+y);
		return movedMass;
	}

	public static void raritySort(ArrayList<Card> cards){
		Collections.sort(cards,new Comparator<Card>() {
        	public int compare(Card card1, Card card2) {
				return Integer.compare(card1.getRarity(), card2.getRarity());
			}
        });
    }

	public static void priceSort(ArrayList<Card> cards){
        Collections.sort(cards,new Comparator<Card>() {
        	public int compare(Card card1, Card card2) {
				return Integer.compare(card1.getBuyPrice(), card2.getBuyPrice());
			}
        });
    }

	public static void init(Window window) {
		resetUsedCard();
		resetUsedFixedCard();
		resetUsedRandomCard();
		resetUsedOthersCard();


		//サイコロ数
		cardList.add(new Card("急行カード",400,1,"サイコロを2つ回すことが出来る",0,2));
		cardList.add(new Card("急行周遊カード",8000,2,"何度かサイコロを2つ回すことが出来る",0,2));
		cardList.add(new Card("特急カード",4000,2,"サイコロを3つ回すことが出来る",0,3));
		cardList.add(new Card("特急周遊カード",30000,3,"何度かサイコロを3つ回すことが出来る",0,3));
		cardList.add(new Card("新幹線カード",7000,3,"サイコロを4つ回すことが出来る",0,4));
		cardList.add(new Card("新幹線周遊カード",50000,4,"何度かサイコロを4つ回すことが出来る",0,4));
		cardList.add(new Card("のぞみカード",20000,4,"サイコロを5つ回すことが出来る",0,5));


		//固定値
		cardList.add(new Card("足踏みカード",4000,3,"その場に留まることが出来る",1,0));
		cardList.add(new Card("1進めるカード",10000,3,"1マス進める",1,1));
		cardList.add(new Card("2進めるカード",10000,3,"2マス進める",1,2));
		cardList.add(new Card("3進めるカード",10000,3,"3マス進める",1,3));
		cardList.add(new Card("4進めるカード",10000,3,"4マス進める",1,4));
		cardList.add(new Card("5進めるカード",10000,3,"5マス進める",1,5));
		cardList.add(new Card("6進めるカード",10000,3,"6マス進める",1,6));


		//どこかへ移動する
		cardList.add(new Card("ぶっとびカード",10000,1,"どこかに移動することが出来る",2,0));
		cardList.add(new Card("ぶっとび周遊カード",40000,2,"何度かどこかに移動することが出来る",2,0));
		cardList.add(new Card("北へ！カード",10000,1,"北に移動することが出来る",2,0));
		cardList.add(new Card("ピッタリカード",14000,2,"誰かと同じマスに移動することが出来る",2,0));
		cardList.add(new Card("サミットカード",16000,3,"他の人を呼び寄せることが出来る",2,0));
		cardList.add(new Card("最寄り駅カード",10000,2,"最寄り駅に移動することが出来る",2,0));
		cardList.add(new Card("星に願いをカード",40000,2,"最寄りのカードショップに移動することが出来る",2,0));

		//バフ・デバフ
		cardList.add(new Card("牛歩カード",4000,3,"しばらくの間、誰かが進むマスを3マス減らす",3,-3));


		//お金がもらえる
		cardList.add(new Card("一頭地を抜くカード",40000,3,"一番お金を持っている人と同じだけお金がもらえる",4,0));
		cardList.add(new Card("起死回生カード",16000,2,"持ち金のマイナスがそのままプラスになる",4,0));
		cardList.add(new Card("徳政令カード",500,1,"全ての人の借金を0にする",4,0));


		//カードがもらえる
		cardList.add(new Card("福袋カード",6000,2,"カードがたくさん出てくる",5,0));
		cardList.add(new Card("ダビングカード",3000,2,"自分が持っているカードを複製することが出来る",5,0));

		Card.raritySort(Card.cardList);

	}
}