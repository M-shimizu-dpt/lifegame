
package lifegame.game.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import lifegame.game.event.search.Searcher;
import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.print.Window;

public class CardEvent{
	public static void init(Window window) {
		CardEvent.resetFlags();
		Card.init(window);
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

	public static void resetUsedCard(){
		Card.resetUsed();
		Card.resetUsedFixed();
	}
	public static void resetFlags() {
		Card.resetUsed();
		Card.resetUsedFixed();
		Card.resetUsedRandom();
		Card.resetUsedOthers();
	}

	public static void sellCard(String pre[],Window window) {
		if(pre.length==2) {
			for(Card card:Player.player.getCards()) {
				if(pre[0].equals(card.getName()) && pre[1].equals("s")) {//カード売却
					SaleEvent.sellCard(card);
					window.shopFrameVisible();
					window.printSellShop();
					break;
				}
			}
		}
	}
	public static void buyCard(String pre[],Window window) {
		if(pre.length==2) {
			for(Card card:Card.getCardList()) {
				if(pre[0].equals(card.getName()) && pre[1].equals("b")) {//カード購入
					SaleEvent.buyCard(card);
					window.shopFrameVisible();
					window.printBuyShop();
					break;
				}
			}
		}
	}

	public static void UseCard(String cmd,Window window) {
		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName())) {//カードを使う
				CardEvent.useAbilitys(i,window);//Card.getCard(i).useAbilitys(this);
				if(ContainsEvent.id(Card.getCard(i),2)){
					window.moveMaps();
					try {
						Thread.sleep(2000);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				window.cardFrame();
				break;
			}else if(cmd.equals(Card.getCard(i).getName()+"t")) {//カードを捨てる
				Player.player.removeCard(Card.getCard(i));
				window.closeErrorFrame();
				break;
			}else if(cmd.equals(Card.getCard(i).getName()+"d")) {//カードを複製
				Player.player.addCard(Card.getCard(i));
				window.closeDubbing();
				break;
			}
		}

		if(ContainsEvent.isUsedRandomCard() || ContainsEvent.isUsedOthersCard()) {
			CardEvent.resetFlags();
			window.ableMenu();
			if(window.playFrameVisible()) {
				App.turnEnd();
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
				}while(rarity<Card.getRarity(index));
			}while(!get);
			canBuyCardlist.add(Card.getCard(index));
		}
		return canBuyCardlist;
	}

	//0,1
	private static void useAbility(Card card) {
		if(card.getID()==0) {
			Dice.setNum(card.getAbility());
		}else if(card.getID() == 1) {
			Card.usedFixed();
			Dice.setResult(card.getAbility());
		}
	}

	//2,3,4,5
	private static void useAbility(Card card,Window window) {
		Random rand = new Random();
		if(card.getID()==2) {
			Coordinates coor = new Coordinates();
			//誰に影響を与えるのか
			Card.usedRandom();
			if(card.getName().equals("サミットカード")) {
				coor.setValue(Player.player.getNowMass());
				for(int roop=0;roop<4;roop++) {
					if(ContainsEvent.isTurn(roop))continue;
					window.moveMaps(Player.players.get(roop),coor);
				}
			}else if(card.getName().equals("北へ！カード")) {
				do {
					coor = useRandomAbility();
				}while(Player.player.getNowMass().getY()<coor.getY());
			}else if(card.getName().equals("ピッタリカード")){
				coor.setValue(Player.player.getAnotherPlayer().getNowMass());
			}else if(card.getName().equals("最寄り駅カード")){
				Searcher.searchNearestStation(window,Player.player);
				Thread thread = new Thread(new WaitThread(2));
				thread.start();
				try {
					thread.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				coor.setValue(Searcher.nearestStationList.get(rand.nextInt(Searcher.nearestStationList.size())));
			}else if(card.getName().equals("星に願いをカード")){
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
				coor = useRandomAbility();
			}
			window.moveMaps(Player.player,coor);
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
						window.cardFull();
					}
					count++;
				}while(rand.nextInt(100)<50 && count<5);
			}else if(card.getName().equals("ダビングカード")) {
				window.printDubbing();
			}
		}
	}

	public static void useAbilitys(int i,Window window) {
		Card card = Card.getCard(i);
		if(card.getID()==0 || card.getID()==1) {
			useAbility(card);
		}else if(card.getID()==2 || card.getID()==3 || card.getID()==4 || card.getID()==5){
			useAbility(card,window);
		}
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