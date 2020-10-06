
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
	public static void resetUsedRandom(){
		Card.resetUsedRandom();
	}

	public static void useCard(String cmd) {
		for(Card card : Card.getCardList()) {
			if(cmd.equals(card.getName())) {
				CardEvent.useAbilitys(card);
				if(ContainsEvent.id(card,2)){
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
		if(card.getID()==0) {
			Dice.setNum(card.getAbility());
		}else if(card.getID() == 1) {
			Card.usedFixed();
			Dice.setResult(card.getAbility());
		}else {
			Card.usedRandom();
			if(card.getID()==2) {
				Coordinates coor = new Coordinates();
				if(card.getName().equals("サミットカード")) {
					coor = CardEvent.summit();
					Card.usedCardAfterNotEvent();
				}else if(card.getName().equals("北へ！カード")) {
					coor = CardEvent.north();
					Card.usedCardAfterNotEvent();
				}else if(card.getName().equals("ピッタリカード")){
					coor = CardEvent.just();
					Card.usedCardAfterNotEvent();
				}else if(card.getName().equals("足踏みカード")) {
					coor = CardEvent.stayMass();
				}else if(card.getName().equals("最寄り駅カード")){
					coor = CardEvent.nearestStation();
				}else if(card.getName().equals("星に願いをカード")){
					coor = CardEvent.starsHope();
				}else {//ぶっ飛び用
					coor = useRandomAbility();
				}
				FrameEvent.moveMaps(Player.player,coor);
				//Player.player.getNowMass().setValue(coor);
			}else if(card.getID()==3) {
				CardEvent.stepCow(card);
				Card.usedCardAfterNotEvent();
			}else if(card.getID()==4) {
				if(card.getName().equals("一頭地を抜くカード")) {
					CardEvent.richest();
					Card.usedCardAfterNotEvent();
				}else if(card.getName().equals("起死回生カード")) {
					CardEvent.resuscitation();
					Card.usedCardAfterNotEvent();
				}else if(card.getName().equals("徳政令カード")) {
					CardEvent.decreeOfVirtue();
					Card.resetUsedRandom();
				}
			}else if(card.getID()==5) {
				if(card.getName().equals("福袋カード")) {
					CardEvent.bags();
					Card.resetUsedRandom();
				}else if(card.getName().equals("ダビングカード")) {
					CardEvent.dubbing();
					Card.resetUsedRandom();
				}
			}
		}
	}

	public static void useAbilitys(Card card) {
		Card.used();
		useAbility(card);

		if(!ContainsEvent.isPlayer()) System.out.println("Use Card!  "+card.getName()+"   user:"+Player.player.getName());//何を使ったか表示(ポップアップに変更すべき)

		//周遊カードの場合は確率でカードを破壊
		if(card.getName().split("周遊").length==2) {
			card.setCount(card.getCount()+1);
			if(new Random().nextInt(100)<30 || card.getCount()>5) {
				Player.player.removeCard(card);
			}
		}else {
			Player.player.removeCard(card);
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

	private static Coordinates stayMass() {
		Coordinates coor = new Coordinates();
		coor.setValue(Player.player.getNowMass());
		return coor;
	}
	private static Coordinates summit() {
		Coordinates coor = new Coordinates();
		coor.setValue(Player.player.getNowMass());
		for(int roop=0;roop<4;roop++) {
			if(ContainsEvent.isTurn(roop))continue;
			FrameEvent.moveMaps(Player.players.get(roop),coor);
		}
		return coor;
	}
	private static Coordinates north() {
		Coordinates coor = new Coordinates();
		do {
			coor = useRandomAbility();
		}while(Player.player.getNowMass().getY()<coor.getY());
		return coor;
	}
	private static Coordinates just() {
		Coordinates coor = new Coordinates();
		coor.setValue(Player.player.getAnotherPlayer().getNowMass());
		return coor;
	}
	private static Coordinates nearestStation() {
		Coordinates coor = new Coordinates();
		Random rand  = new Random();
		Searcher.searchNearestStation(Player.player);
		Thread thread = new Thread(new WaitThread(2));
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		coor.setValue(Searcher.nearestStationList.get(rand.nextInt(Searcher.nearestStationList.size())));
		Card.resetUsedRandom();
		return coor;
	}
	private static Coordinates starsHope() {
		Coordinates coor = new Coordinates();
		Random rand = new Random();
		Searcher.searchNearestShop(Player.player);
		Thread thread = new Thread(new WaitThread(2));
		thread.start();
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		coor.setValue(Searcher.nearestShopList.get(rand.nextInt(Searcher.nearestShopList.size())));
		Card.resetUsedRandom();
		return coor;
	}
	private static void stepCow(Card card) {
		Random rand = new Random();
		int period;
		do {
			period = rand.nextInt(5);
		}while(period <= 1);
		Player.player.getAnotherPlayer().addBuff(card.getAbility(), period);
	}
	private static void richest() {
		int maxMoney=0;
		for(Player player:Player.players.values()) {
			if(ContainsEvent.money(player, maxMoney)>0) {
				maxMoney=player.getMoney();
			}
		}
		Player.player.addMoney(maxMoney);
	}
	private static void resuscitation() {
		if(ContainsEvent.money(0)<0) {
			Player.player.addMoney(-Player.player.getMoney()*2);
		}
	}
	private static void decreeOfVirtue() {
		for(int player=0;player<4;player++) {
			if(ContainsEvent.money(Player.players.get(player), 0)<0) {
				Player.players.get(player).addMoney(-Player.players.get(player).getMoney());
			}
		}
	}
	private static void bags() {
		Random rand = new Random();
		int count=0;
		do {
			int randcard = rand.nextInt(Card.getCardListSize());
			Player.player.addCard(Card.getCard(randcard));
			if(Player.player.getCardSize()>8) {
				FrameEvent.openFullCardFromPlay();
			}
			count++;
		}while(rand.nextInt(100)<50 && count<5);
	}
	private static void dubbing() {
		FrameEvent.openDubbing();
	}
}