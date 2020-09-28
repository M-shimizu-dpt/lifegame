package lifegame.game.object.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lifegame.game.object.Card;

public abstract class CardModel {
	protected String name;//名前
	protected String cardText;//能力説明
	protected int sellPrice;//売る時の値段
	protected int buyPrice;//買う時の値段
	protected int count;//カード破壊カウント(周遊用)
	protected int rarity;
	protected int ability;
	protected int id;
	protected static ArrayList<Card> cardList = new ArrayList<Card>();
	protected static boolean usedCard;
	protected static boolean usedFixedCard;
	protected static boolean usedRandomCard;
	protected static boolean usedOthersCard;


	public int getAbility() {
		return this.ability;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public String getName(int index) {
		return cardList.get(index).getName();
	}

	public String getText() {
		return this.cardText;
	}

	public String getText(int index) {
		return cardList.get(index).getText();
	}

	public int getSellPrice() {
		return this.sellPrice;
	}

	public int getSellPrice(int index) {
		return cardList.get(index).getSellPrice();
	}

	public int getBuyPrice() {
		return this.buyPrice;
	}

	public int getBuyPrice(int index) {
		return cardList.get(index).getBuyPrice();
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getRarity(int index) {
		return cardList.get(index).getRarity();
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

	public void setName(String name) {
		this.name=name;
	}

	public void setID(int id) {
		this.id=id;
	}

	public void setText(String text) {
		this.cardText = text;
	}

	public void setSellPrice(int price) {
		this.sellPrice = price;
	}

	public void setBuyPrice(int price) {
		this.buyPrice = price;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
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

	public static Card getCard(int index) {
		return cardList.get(index);
	}
	public static ArrayList<Card> getCardList() {
		return cardList;
	}
	public static int getCardListSize() {
		return cardList.size();
	}

	public static void resetUsed() {
		usedCard=false;
	}

	public static void used() {
		usedCard=true;
	}

	public static boolean isUsed() {
		return usedCard;
	}

	public static void resetUsedFixed() {
		usedFixedCard=false;
	}

	public static void usedFixed() {
		usedFixedCard=true;
	}

	public static boolean isUsedFixed() {
		return usedFixedCard;
	}

	public static void resetUsedRandom() {
		usedRandomCard=false;
	}

	public static void usedRandom() {
		usedRandomCard=true;
	}

	public static boolean isUsedRandom() {
		return usedRandomCard;
	}

	public static void resetUsedOthers() {
		usedOthersCard=false;
	}

	public static void usedOthers() {
		usedOthersCard=true;
	}

	public static boolean isUsedOthers() {
		return usedOthersCard;
	}

	public static void resetFlags() {
		usedCard=false;
		usedFixedCard=false;
		usedRandomCard=false;
		usedOthersCard=false;
	}
}
