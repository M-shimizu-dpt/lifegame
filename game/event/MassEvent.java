package lifegame.game.event;

import java.util.Random;

import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;

public abstract class MassEvent {
	//マスに到着した時のマスのイベント処理
	public static void massEvent(String massName) {
		if(Player.isStop()) {
			WaitThread wait = new WaitThread(7);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		assert massName.substring(0, 1).equals("B") || massName.substring(0, 1).equals("R") || massName.substring(0, 1).equals("Y")
			|| massName.substring(0, 1).equals("S") || Japan.getStationNameList().contains(massName) : "massNameが正しくありません";

		if(massName.substring(0, 1).equals("B")) {
			blueEvent();
		}else if(massName.substring(0, 1).equals("R")) {
			redEvent();
		}else if(massName.substring(0, 1).equals("Y")) {
			yellowEvent();
		}else if(massName.substring(0, 1).equals("S")) {
			shopEvent();
		}else{
			if(ContainsEvent.goal(massName)) {
				FrameEvent.openGoal();
			}
			FrameEvent.printPropertys(massName,2);
		}
	}

	//青マスイベント
	private static void blueEvent() {
		Random rand = new Random();
		int result=0;
		while(result<500) {
			result =rand.nextInt(2000);
		}
		result += result*(App.year/10);
		result -= result%100;
		System.out.println(result);
		Player.player.addMoney(result);
		if(rand.nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			App.turnEnd();
		}
	}

	//赤マスイベント
	private static void redEvent() {
		Random rand = new Random();
		int result=0;
		while(result<500) {
			result = rand.nextInt(2000);
		}
		result += result*(App.year/10);
		result -= result%100;
		System.out.println(-result);
		Player.player.addMoney(-result);
		if(ContainsEvent.money(0) < 0 && ContainsEvent.propertySize()) {
			FrameEvent.openSellProperty();
		}else{
			if(rand.nextInt(100) < 3) {
				RandomEvent.randomEvent();
			}else {
				App.turnEnd();
			}
		}
	}

	//黄マスイベント
	private static void yellowEvent() {
		Random rand = new Random();
		boolean get=false;
		int index=0;
		while(true) {
			get=false;
			index = rand.nextInt(Card.getCardListSize());
			int i=0;
			do {
				if(rand.nextInt(100)<30) {
					get=true;
				}
				i++;
			}while(i<Card.getCard(index).getRarity());
			if(!get) {
				break;
			}
		}
		Player.player.addCard(Card.getCard(index));
		System.out.println("Card Get! name:"+Card.getCard(index).getName()+"  rarity"+Card.getCard(index).getRarity());
		if(ContainsEvent.isMaxCard()) {
			FrameEvent.openError();
			WaitThread wait = new WaitThread(9);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(rand.nextInt(100) < 3) {
			RandomEvent.randomEvent();
		}else {
			App.turnEnd();
		}
	}

	//店マスイベント
	private static void shopEvent() {
		FrameEvent.openShopFront();
	}

}
