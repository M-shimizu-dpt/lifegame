
package lifegame.game.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;

public class CardEvent{
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

	public static void resetUsedCard(){
		Card.resetUsed();
		Card.resetUsedFixed();
	}
	public static void resetFlags() {
		Card.resetFlags();
	}

	public static void UseCard(String cmd) {
		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName())) {//カードを使う
				CardEvent.useAbilitys(i);//Card.getCard(i).useAbilitys(this);
				if(ContainsEvent.id(Card.getCard(i),2)){
					FrameEvent.moveMaps();
					try {
						Thread.sleep(2000);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}

	public static void throwCard(String cmd) {
		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName()+"t")) {//カードを捨てる
				Player.player.removeCard(Card.getCard(i));
				break;
			}
		}
	}

	public static void dubbingCard(String cmd) {
		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName()+"d")) {//カードを複製
				Player.player.addCard(Card.getCard(i));
				break;
			}
		}
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
					index = rand.nextInt(Card.getCardListSize());
					for(Card card : canBuyCardlist) {
						if(ContainsEvent.name(card, Card.getCard(index))) {
							flag=false;
						}
					}
				}while(!flag);
				int rarity=0;
				do {
					if(rand.nextInt(10)<3) {
						get=true;
					}
					rarity++;
				}while(rarity<Card.getCard(index).getRarity());
			}while(!get);
			canBuyCardlist.add(Card.getCard(index));
		}
		return canBuyCardlist;
	}

	//0,1,2,3,4,5
	private static void useAbility(Card card) {
		Random rand = new Random();
		if(card.getID()==0) {
			Dice.setNum(card.getAbility());
		}else if(card.getID() == 1) {
			Card.usedFixed();
			Dice.setResult(card.getAbility());
		}else if(card.getID()==2) {
			Coordinates coor = new Coordinates();
			//誰に影響を与えるのか
			Card.usedRandom();
			if(card.getName().equals("サミットカード")) {
				coor.setValue(Player.player.getNowMass());
				for(int roop=0;roop<4;roop++) {
					if(ContainsEvent.isTurn(roop))continue;
					FrameEvent.moveMaps(Player.players.get(roop),coor);
				}
			}else if(card.getName().equals("北へ！カード")) {
				do {
					coor = useRandomAbility();
				}while(Player.player.getNowMass().getY()<coor.getY());
			}else if(card.getName().equals("ピッタリカード")){
				coor.setValue(Player.player.getAnotherPlayer().getNowMass());
			}else if(card.getName().equals("最寄り駅カード")){
				Searcher.searchNearestStation(Player.player);
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestStationList.get(rand.nextInt(Searcher.nearestStationList.size())));
			}else if(card.getName().equals("星に願いをカード")){
				Searcher.searchNearestShop(Player.player);
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestShopList.get(rand.nextInt(Searcher.nearestShopList.size())));
			}else {
				coor = useRandomAbility();
			}
			FrameEvent.moveMaps(Player.player,coor);
			Player.player.getNowMass().setValue(coor);

		}else if(card.getID()==3) {
			int period;
			do {
				period = rand.nextInt(5);
			}while(period <= 1);
			Player.player.getAnotherPlayer().addBuff(card.getAbility(), period);
		}else if(card.getID()==4) {
			Card.usedOthers();
			if(card.getName().equals("一頭地を抜くカード")) {
				int maxMoney=0;
				for(Player player:Player.players.values()) {
					if(ContainsEvent.money(player, maxMoney)>0) {
						maxMoney=player.getMoney();
					}
				}
				Player.player.addMoney(maxMoney);
			}else if(card.getName().equals("起死回生カード")) {
				if(ContainsEvent.money(0)<0) {
					Player.player.addMoney(-Player.player.getMoney()*2);
				}
			}else if(card.getName().equals("徳政令カード")) {
				for(int player=0;player<4;player++) {
					if(ContainsEvent.money(Player.players.get(player), 0)<0) {
						Player.players.get(player).addMoney(-Player.players.get(player).getMoney());
					}
				}
			}
		}else if(card.getID()==5) {
			if(card.getName().equals("福袋カード")) {
				int count=0;
				do {
					int randcard = rand.nextInt(Card.getCardListSize());
					Player.player.addCard(Card.getCard(randcard));
					if(Player.player.getCardSize()>8) {
						FrameEvent.openError();
						//window.cardFull();
					}
					count++;
				}while(rand.nextInt(100)<50 && count<5);
			}else if(card.getName().equals("ダビングカード")) {
				FrameEvent.openDubbing();
			}
		}
	}

	public static void useAbilitys(int i) {
		Card card = Card.getCard(i);

		useAbility(card);

		if(!Player.player.isPlayer()) System.out.println("Use Card!  "+card.getName()+"   user:"+Player.player.getName());//何を使ったか表示(ポップアップに変更すべき)

		//周遊カードの場合は確率でカードを破壊
		if(card.getName().split("周遊").length==2) {
			card.setCount(card.getCount()+1);
			if(new Random().nextInt(100)<30 || card.getCount()>5) {
				Player.player.removeCard(card);
			}
		}else {
			Player.player.removeCard(card);
		}

		if(!card.getName().equals("徳政令カード")) {
			Card.used();//カードを使ったことにする
		}
	}

	public static Coordinates useRandomAbility() {
		Random rand = new Random();
		Coordinates movedMass=new Coordinates();
		int x,y;
		do {
			x=rand.nextInt(17);
			y=rand.nextInt(17);
		}while(!ContainsEvent.isMass(x, y));
		movedMass.setValue(x, y);
		//System.out.println("random move  x:"+x+"  y:"+y);
		return movedMass;
	}


}