package lifegame.game.event;

import java.util.ArrayList;

import lifegame.game.main.App;
import lifegame.game.object.Binbo;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.information.Station;

public abstract class ContainsEvent {

	public static int goalDistance(int goaldistance) {
		if(Player.player.getGoalDistance()>goaldistance) {
			return 1;
		}else if(Player.player.getGoalDistance()<goaldistance){
			return -1;
		}else {
			return 0;
		}
	}
	public static int goalDistance(int goaldistance1,int goaldistance2) {
		if(goaldistance1>goaldistance2) {
			return 1;
		}else if(goaldistance1<goaldistance2) {
			return -1;
		}else {
			return 0;
		}
	}
	public static int goalDistance(Player player) {
		if(Player.player.getGoalDistance()>player.getGoalDistance()) {
			return 1;
		}else if(Player.player.getGoalDistance()<player.getGoalDistance()){
			return -1;
		}else {
			return 0;
		}
	}
	public static int goalDistance(Player player1,Player player2) {
		if(player1.getGoalDistance()>player2.getGoalDistance()) {
			return 1;
		}else if(player1.getGoalDistance()<player2.getGoalDistance()){
			return -1;
		}else {
			return 0;
		}
	}
	public static int goalDistance(Player player,int goaldistance) {
		if(player.getGoalDistance()>goaldistance) {
			return 1;
		}else if(player.getGoalDistance()<goaldistance){
			return -1;
		}else {
			return 0;
		}
	}
	public static boolean id(int id) {
		return Player.player.getID()==id;
	}
	public static boolean id(int id1,int id2) {
		return id1==id2;
	}
	public static boolean id(Player player) {
		return Player.player.getID()==player.getID();
	}
	public static boolean id(Player player1,Player player2) {
		return player1.getID()==player2.getID();
	}
	public static boolean id(Card card1,Card card2) {
		return card1.getID()==card2.getID();
	}
	public static int money(int money) {
		if(Player.player.getMoney()>money) {
			return 1;
		}else if(Player.player.getMoney()<money) {
			return -1;
		}else {
			return 0;
		}
	}
	public static int money(int money1,int money2) {
		if(money1>money2) {
			return 1;
		}else if(money1<money2){
			return -1;
		}else {
			return 0;
		}
	}
	public static int money(Player player) {
		if(Player.player.getMoney()>player.getMoney()) {
			return 1;
		}else if(Player.player.getMoney()<player.getMoney()){
			return -1;
		}else {
			return 0;
		}
	}
	public static int money(Player player1,Player player2) {
		if(player1.getMoney()>player2.getMoney()) {
			return 1;
		}else if(player1.getMoney()<player2.getMoney()){
			return -1;
		}else {
			return 0;
		}
	}
	public static int money(Player player,int money) {
		if(player.getMoney()>money) {
			return 1;
		}else if(player.getMoney()<money){
			return -1;
		}else {
			return 0;
		}
	}
	public static boolean money(Card card1,Card card2) {
		return card1.getBuyPrice()==card2.getBuyPrice();
	}
	public static boolean propertySize(ArrayList<Property> propertys) {
		return Player.player.getPropertys().size() > propertys.size();
	}
	public static boolean propertySize(ArrayList<Property> propertys1,ArrayList<Property> propertys2) {
		return propertys1.size() > propertys2.size();
	}
	public static boolean propertySize(Player player) {
		return Player.player.getPropertys().size() > player.getPropertys().size();
	}
	public static boolean propertySize(Player player1,Player player2) {
		return player1.getPropertys().size() > player2.getPropertys().size();
	}

	public static boolean binboNameBaby() {
		return Binbo.getName().equals("赤ちゃんボンビー");
	}
	public static boolean binboNameNormal() {
		return Binbo.getName().equals("ボンビー");
	}
	public static boolean binboNameHappy() {
		return Binbo.getName().equals("幸せボンビー");
	}
	public static boolean binboNameKing() {
		return Binbo.getName().equals("キングボンビー");
	}
	public static boolean binboNameTyphoon() {
		return Binbo.getName().equals("タイフーンボンビー");
	}
	public static boolean binboPlayer() {
		return Binbo.getBinboPlayer().getID()==Player.player.getID();
	}
	public static boolean binboPlayer(Player player) {
		return Binbo.getBinboPlayer().getID()==player.getID();
	}


	public static boolean name(String name1,String name2) {
		return name1.equals(name2);
	}
	public static boolean name(Card card1,Card card2) {
		return card1.getName().equals(card2.getName());
	}
	public static boolean name(Card card,String name) {
		return card.getName().equals(name);
	}
	public static boolean name(Property property1,Property property2) {
		return property1.getName().equals(property2.getName());
	}
	public static boolean name(Property property,String name) {
		return property.getName().equals(name);
	}
	public static boolean name(Station station1,Station station2) {
		return station1.getName().equals(station2.getName());
	}
	public static boolean name(Station station,String name) {
		return station.getName().equals(name);
	}

	public static int cardRarity(int rarity1,int rarity2) {
		if(rarity1>rarity2) {
			return 1;
		}else if(rarity1<rarity2) {
			return -1;
		}else {
			return 0;
		}
	}
	public static int cardRarity(Card card1,Card card2) {
		if(card1.getRarity()>card2.getRarity()) {
			return 1;
		}else if(card1.getRarity()<card2.getRarity()) {
			return -1;
		}else {
			return 0;
		}
	}
	public static boolean cardAbility(int ability1,int ability2) {
		return ability1==ability2;
	}
	public static boolean cardAbility(Card card1,Card card2) {
		return card1.getAbility()==card2.getAbility();
	}


	public static boolean coor(int x1,int y1,int x2,int y2) {
		return x1==x2 && y1==y2;
	}
	public static boolean coor(Coordinates coor1,Coordinates coor2) {
		return coor1.getX()==coor2.getX() && coor1.getY()==coor2.getY();
	}
	public static boolean coor(Player player1,Player player2) {
		return player1.getNowMass().getX()==player2.getNowMass().getX() && player1.getNowMass().getY()==player2.getNowMass().getY();
	}
	public static boolean coor(Player player,Coordinates coor) {
		return player.getNowMass().getX()==coor.getX() && player.getNowMass().getY()==coor.getY();
	}
	public static boolean coor(Player player,int x,int y) {
		return player.getNowMass().getX()==x && player.getNowMass().getY()==y;
	}
	public static boolean coor(Coordinates coor,int x,int y) {
		return coor.getX()==x && coor.getY()==y;
	}
	public static boolean coor(Station station1,Station station2) {
		return station1.getCoordinates().getX()==station2.getCoordinates().getX() && station1.getCoordinates().getY()==station2.getCoordinates().getY();
	}
	public static boolean coor(Station station,Coordinates coor) {
		return station.getCoordinates().getX()==coor.getX() && station.getCoordinates().getY()==coor.getY();
	}
	public static boolean coor(Station station,int x,int y) {
		return station.getCoordinates().getX()==x && station.getCoordinates().getY()==y;
	}
	public static boolean coorCost(int cost1,int cost2) {
		return cost1>=cost2;
	}
	public static boolean coorCost(Coordinates coor1,Coordinates coor2) {
		return coor1.getCost()>=coor2.getCost();
	}


	public static boolean propertyOwner(String owner1,String owner2) {
		return owner1.equals(owner2);
	}
	public static boolean propertyOwner(Property property1,Property property2) {
		return property1.getOwner().equals(property2.getOwner());
	}


	public static int propertyAmount(int amount1,int amount2) {
		if(amount1>amount2) {
			return 1;
		}else if(amount1<amount2) {
			return -1;
		}else {
			return 0;
		}
	}
	public static int propertyAmount(Property property1,Property property2) {
		if(property1.getAmount()>property2.getAmount()) {
			return 1;
		}else if(property1.getAmount()<property2.getAmount()) {
			return -1;
		}else {
			return 0;
		}
	}
	public static boolean propertyGroup(int group1,int group2) {
		return group1==group2;
	}
	public static boolean propertyGroup(Property property1,Property property2) {
		return property1.getGroup()==property2.getGroup();
	}


	public static boolean stationNameCoor(String name,Coordinates coor) {
		return name.equals(App.japan.getStation(coor).getName());
	}
	public static boolean stationPropertys(Station station, Property property) {
		return station.getPropertys().contains(property);
	}














}
