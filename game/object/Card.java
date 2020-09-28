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

import java.util.ArrayList;
import java.util.Random;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.WaitThread;
import lifegame.game.event.search.Searcher;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.print.Window;
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

	public static ArrayList<Card> getElectedCard(){
		ArrayList<Card> canBuyCardlist = new ArrayList<Card>();//店の購入可能カードリスト
		Random rand = new Random();
		boolean get=false;
		int index=0;
		for(int i = 0;i<8;i++) {//表示するカード8枚を選出
			do {
				get=false;
				boolean flag;
				do {//今まで選出したカードと今回選出したカードが被った場合は再選
					flag=true;
					index = rand.nextInt(Card.cardList.size());
					for(Card card : canBuyCardlist) {
						if(ContainsEvent.name(card, Card.cardList.get(index))) {
							flag=false;
						}
					}
				}while(!flag);
				int rarity=0;
				do {
					if(rand.nextInt(100)<30) {
						get=true;
					}
					rarity++;
				}while(rarity<Card.cardList.get(index).getRarity());
			}while(!get);
			canBuyCardlist.add(Card.cardList.get(index));
		}
		return canBuyCardlist;
	}

	//0,1
	private void useAbility() {
		if(this.id==0) {
			Dice.setNum(this.ability);
		}else if(this.id == 1) {
			Card.usedFixed();
			Dice.setResult(this.ability);
		}
	}

	//2,3,4,5
	private void useAbility(Window window) {
		Random rand = new Random();
		if(this.id==2) {
			Coordinates coor = new Coordinates();
			//誰に影響を与えるのか
			Card.usedRandom();
			if(name.equals("サミットカード")) {
				coor.setValue(Player.player.getNowMass());
				for(int roop=0;roop<4;roop++) {
					if(ContainsEvent.isTurn(roop))continue;
					window.moveMaps(Player.players.get(roop),coor);
				}
			}else if(name.equals("北へ！カード")) {
				do {
					coor = this.useRandomAbility();
				}while(Player.player.getNowMass().getY()<coor.getY());
			}else if(name.equals("ピッタリカード")){
				coor.setValue(Player.players.get(rand.nextInt(4)).getNowMass());
			}else if(name.equals("最寄り駅カード")){
				Searcher.searchNearestStation(window,Player.player);
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestStationList.get(rand.nextInt(Searcher.nearestStationList.size())));
			}else if(name.equals("星に願いをカード")){
				Searcher.searchNearestShop(window,Player.player);
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
			window.moveMaps(Player.player,coor);
			Player.player.getNowMass().setValue(coor);

		}else if(this.id==3) {
			int enemy = Player.player.getAnotherPlayer();
			int period;
			do {
				period = rand.nextInt(5);
			}while(period <= 1);
			Player.players.get(enemy).getBuff().addBuff(this.ability, period);
			//System.out.println(Player.players.get(App.turn).getName());
		}else if(this.id==4) {
			Card.usedOthers();
			if(name.equals("一頭地を抜くカード")) {
				int maxMoney=0;
				for(Player player:Player.players.values()) {
					if(ContainsEvent.money(player, maxMoney)>0) {
						maxMoney=player.getMoney();
					}
				}
				Player.player.addMoney(maxMoney);
			}else if(name.equals("起死回生カード")) {
				if(ContainsEvent.money(0)<0) {
					Player.player.addMoney(-Player.player.getMoney()*2);
				}
			}else if(name.equals("徳政令カード")) {
				for(int player=0;player<4;player++) {
					if(ContainsEvent.money(Player.players.get(player), 0)<0) {
						Player.players.get(player).addMoney(-Player.players.get(player).getMoney());
					}
				}
			}
		}else if(this.id==5) {
			if(name.equals("福袋カード")) {
				int count=0;
				do {
					int randcard = rand.nextInt(Card.cardList.size());
					Player.player.addCard(Card.cardList.get(randcard));
					if(Player.player.getCards().size()>8) {
						window.cardFull();
					}
					count++;
				}while(rand.nextInt(100)<50 && count<5);
			}else if(name.equals("ダビングカード")) {
				window.printDubbing();
			}
		}
	}

	public void useAbilitys(Window window) {
		if(id==0 || id==1) {
			useAbility();
		}else if(id==2 || id==3 || id==4 || id==5){
			useAbility(window);
		}
		if(!Player.player.isPlayer()) System.out.println("Use Card!  "+name+"   user:"+Player.player.getName());//何を使ったか表示(ポップアップに変更すべき)

		//周遊カードの場合は確率でカードを破壊
		if(name.split("周遊").length==2) {
			this.setCount(this.getCount()+1);
			if(new Random().nextInt(100)<30 || this.getCount()>5) {
				Player.player.removeCard(this);
			}
		}else {
			Player.player.removeCard(this);
		}

		if(!name.equals("徳政令カード")) {
			Card.used();//カードを使ったことにする
		}
	}

	public Coordinates useRandomAbility() {
		Random rand = new Random();
		Coordinates movedMass=new Coordinates();
		int x,y;
		while(true) {
			x=rand.nextInt(17);
			y=rand.nextInt(17);
			if(ContainsEvent.isMass(x, y))break;
		}
		movedMass.setValue(x, y);
		//System.out.println("random move  x:"+x+"  y:"+y);
		return movedMass;
	}

	public static void init(Window window) {
		resetFlags();

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
		cardList.add(new Card("ぶっとびカード",10000,1,"どこかに移動することが出来る",2));
		cardList.add(new Card("ぶっとび周遊カード",40000,2,"何度かどこかに移動することが出来る",2));
		cardList.add(new Card("北へ！カード",10000,1,"北に移動することが出来る",2));
		cardList.add(new Card("ピッタリカード",14000,2,"誰かと同じマスに移動することが出来る",2));
		cardList.add(new Card("サミットカード",16000,3,"他の人を呼び寄せることが出来る",2));
		cardList.add(new Card("最寄り駅カード",10000,2,"最寄り駅に移動することが出来る",2));
		cardList.add(new Card("星に願いをカード",40000,2,"最寄りのカードショップに移動することが出来る",2));

		//バフ・デバフ
		cardList.add(new Card("牛歩カード",4000,3,"しばらくの間、誰かが進むマスを3マス減らす",3,-3));

		//お金がもらえる
		cardList.add(new Card("一頭地を抜くカード",40000,3,"一番お金を持っている人と同じだけお金がもらえる",4));
		cardList.add(new Card("起死回生カード",16000,2,"持ち金のマイナスがそのままプラスになる",4));
		cardList.add(new Card("徳政令カード",500,1,"全ての人の借金を0にする",4));


		//カードがもらえる
		cardList.add(new Card("福袋カード",6000,2,"カードがたくさん出てくる",5));
		cardList.add(new Card("ダビングカード",3000,2,"自分が持っているカードを複製することが出来る",5));

		Card.raritySort(Card.cardList);

	}
}