package lifegame.game.event;

import java.util.Random;

import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Player;

public abstract class MassEvent {
	//マスに到着した時のマスのイベント処理
	private static boolean massEventFlag=false;//MassEventフラグ

  	public static void massEventEnd() {
  		massEventFlag=true;
  	}

  	public static boolean isMassEventEnd() {
  		return massEventFlag;
  	}
  	public static void initMassEvent() {
  		massEventFlag=false;
  	}

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

		if(ContainsEvent.isGingaMap()) {
			if(massName.substring(0, 1).equals("B")) {
				rareBlueEvent();
			}else if(massName.substring(0, 1).equals("Y")) {
				rareYellowEvent();
			}
		}else if(ContainsEvent.isNormalMap()) {
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
					goal();
				}else {
					FrameEvent.openPropertys(massName,2);
				}
			}
		}else if(ContainsEvent.isBonbirasMap()) {
			//ボンビラス星の処理
		}
	}

	private static void goal() {
		Searcher.searchShortestRouteAllPlayers();
		FrameEvent.openGoal();
	}

	//レア青マスイベント
	private static void rareBlueEvent() {
		Random rand = new Random();
		int result=0;
		while(result<2500) {
			result=rand.nextInt(10000);
		}
		result += result*(App.year/10);
		result -= result%100;
		System.out.println(result);
		Player.player.addMoney(result);
		if(rand.nextInt(100)<3) {
			RandomEvent.randomEvent();
		}else {
			massEventEnd();
		}
	}

	//レア黄マスイベント
	private static void rareYellowEvent() {
		Random rand = new Random();
		boolean get=false;
		int index=0;
		while(true) {
			get=false;
			index = rand.nextInt(Card.getCardListSize());
			if(3>Card.getCard(index).getRarity())continue;
			int i=0;
			do {
				if(rand.nextInt(2)<1) {
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
			FrameEvent.openFullCardFromPlay();
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
			massEventEnd();
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
			massEventEnd();
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
		if(ContainsEvent.money(0) < 0 && ContainsEvent.isHaveProperty()) {
			FrameEvent.openSellProperty();
		}else{
			if(rand.nextInt(100) < 3) {
				RandomEvent.randomEvent();
			}else {
				massEventEnd();
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
			FrameEvent.openFullCardFromPlay();
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
			massEventEnd();
		}
	}

	//店マスイベント
	private static void shopEvent() {
		FrameEvent.openShopFront();
	}

}
