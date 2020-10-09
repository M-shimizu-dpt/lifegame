package lifegame.game.event;

import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;

public abstract class SaleEvent {

	public static void buyCard(String cardName) {
		for(Card card:Card.getCardList()) {
			if(cardName.equals(card.getName())) {
				Player.player.addCard(card);
				Player.player.addMoney(-card.getBuyPrice());
				break;
			}
		}
	}
	public static void buyCard(Card card) {
		Player.player.addCard(card);
		Player.player.addMoney(-card.getBuyPrice());
	}
	public static void buyCard(Player player,Card card) {
		player.addCard(card);
		player.addMoney(-card.getBuyPrice());
	}

	public static void sellCard(String cardName) {
		for(Card card:Player.player.getCards()) {
			if(cardName.equals(card.getName())) {
				Player.player.removeCard(card);
				Player.player.addMoney(card.getSellPrice());
				break;
			}
		}
	}
	public static void sellCard(Card card) {
		Player.player.removeCard(card);
		Player.player.addMoney(card.getSellPrice());
	}
	public static void sellCard(Player player,Card card) {
		player.removeCard(card);
		player.addMoney(card.getSellPrice());
	}

	public static void buyPropertys(String name, int index) {
		Property property=Japan.getStaInProperty(name,index);
		Player.player.addProperty(property);
		Player.player.addMoney(-property.getAmount());
		if(!ContainsEvent.isOwner(property)) {
			property.setLevel(0);
			Japan.updateStationMono(name);
		}else {
			property.setLevel(property.getLevel()+1);
		}
		property.setOwner(Player.player.getName());
		Japan.alreadys.add(property.getName()+index);
		System.out.println("buy1");
	}
	public static void buyPropertys(Player player,String name, int index) {
		Property property=Japan.getStaInProperty(name,index);
		property.setOwner(player.getName());
		player.addProperty(property);
		player.addMoney(-property.getAmount());
		if(!ContainsEvent.isOwner(property)) {
			property.setLevel(0);
			Japan.updateStationMono(name);
		}else {
			property.setLevel(property.getLevel()+1);
		}
		Japan.alreadys.add(property.getName()+index);
		System.out.println("buy2");
	}
	public static void sellPropertys(Property property) {
		property.setOwner("");
		Player.player.removeProperty(property);
		Player.player.addMoney(property.getAmount()/2);
		property.setLevel(0);
		Japan.updateStationMono(property);
	}
	public static void sellPropertys(Player player,Property property) {
		property.setOwner("");
		player.removeProperty(property);
		player.addMoney(property.getAmount()/2);
		property.setLevel(0);
		Japan.updateStationMono(property);
	}
	public static void buyPropertysCPU(String name) {
		for(int index = 0;index<Japan.getStaInPropertySize(name);index++) {
			if(ContainsEvent.isBuyProperty(Japan.getStaInProperty(name,index))) {
				SaleEvent.buyPropertys(name, index);
				try {
					Thread.sleep(300);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			//System.out.println(Japan.getStaInProperty(name,index).getName()+"を購入"+"("+index+")");
		}
	}
	public static void lostPropertys(Property property) {
		property.setOwner("");
		Player.player.removeProperty(property);
		property.setLevel(0);
		Japan.updateStationMono(property);
	}
}
