package lifegame.game.event;

import java.util.Random;

import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.Window;

public abstract class MassEvent {
	//マスに到着した時のマスのイベント処理
	public static void massEvent(Window window, String massName) {
		if(Player.isStop()) {
			WaitThread wait = new WaitThread(7);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		if(massName.substring(0, 1).equals("B")) {
			blueEvent(window);
		}else if(massName.substring(0, 1).equals("R")) {
			redEvent(window);
		}else if(massName.substring(0, 1).equals("Y")) {
			yellowEvent(window);
		}else if(massName.substring(0, 1).equals("S")) {
			shopEvent(window);
		}else{
			if(ContainsEvent.goal(massName)) {
				window.goal();
			}else {
				window.printPropertys(massName);
			}
		}
	}

	//青マスイベント
	private static void blueEvent(Window window) {
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
			window.randomEvent();
		}else {
			App.turnEnd();
		}
	}

	//赤マスイベント
	private static void redEvent(Window window) {
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
			window.printTakeStations();
		}else{
			if(rand.nextInt(100) < 3) {
				window.randomEvent();
			}else {
				App.turnEnd();
			}
		}
	}

	//黄マスイベント
	private static void yellowEvent(Window window) {
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
		if(ContainsEvent.isMaxCard()) {
			window.cardFull();
			WaitThread wait = new WaitThread(9);
			wait.start();
			try {
				wait.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Card Get! name:"+Card.getCard(index).getName()+"  rarity"+Card.getCard(index).getRarity());
		if(rand.nextInt(100) < 3) {
			window.randomEvent();
		}else {
			App.turnEnd();
		}
	}

	//店マスイベント
	private static void shopEvent(Window window) {
		window.printShop(Card.getElectedCard());
	}

}
