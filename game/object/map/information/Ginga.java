package lifegame.game.object.map.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lifegame.game.event.ContainsEvent;
import lifegame.game.object.Player;

public abstract class Ginga {
	static final int size=8;
	private static ArrayList<Coordinates> rareBlue = new ArrayList<Coordinates>();
	private static ArrayList<Coordinates> rareYellow = new ArrayList<Coordinates>();
	private final static Coordinates start = new Coordinates(1,1);
	private final static Coordinates goal = new Coordinates(4,4);
	//private static Map<Coordinates,ArrayList<Coordinates>> railMapping = new HashMap<Coordinates,ArrayList<Coordinates>>();//移動可能座標
	private static Map<Coordinates,Integer> goalDistance = new HashMap<Coordinates,Integer>();

	//railMappingにリンクを入れたがCoordinatesのリンクを
	public static void init() {
		for(int y=1;y<size;y++) {
			for(int x=1;x<size;x++) {
				if((x==1 && y==1) || (x==size/2 && y==size/2)) continue;
				Coordinates coor = new Coordinates(x,y);
				if(new Random().nextInt(5)<3) {
					rareBlue.add(coor);
				}else {
					rareYellow.add(coor);
				}
			}
		}
		for(Coordinates coor1 : getAllCoordinates()) {//このfor文を1つにしてContainsEvent.isMassのようなメソッドを活用してリンクを張った方が効率がいい
			for(Coordinates coor2 : getAllCoordinates()) {
				if((coor1.getX()-coor2.getX() < -1 || coor1.getX()-coor2.getX() > 1 || coor1.getY()-coor2.getY() < -1 || coor1.getY()-coor2.getY() > 1)
						|| ContainsEvent.coor(coor1, coor2) || (coor1.getX()!=coor2.getX() && coor1.getY()!=coor2.getY())) continue;
				//ここに来るのは周り4マスのみ
				for(int i=1;i<4;i++) {
					if((coor1.getX()>=i && coor1.getX()<=size-i && coor2.getX()>=i && coor2.getX()<=size-i)
							&& (coor2.getY()==i || coor2.getY()==size-i) && (coor1.getY() == coor2.getY())) {
						getCoordinates(coor1).addLinks(coor2);
					}
					if((coor1.getY()>=i && coor1.getY()<=size-i && coor2.getY()>=i && coor2.getY()<=size-i)
							&& (coor2.getX()==i || coor2.getX()==size-i) && (coor1.getX() == coor2.getX())) {
						getCoordinates(coor1).addLinks(coor2);
					}
				}
				if(coor1.getX()==coor1.getY() && coor1.getX()<size/2 && coor2.getX()==coor2.getY()-1 && getCoordinates(coor1).getLinks().contains(coor2)) {
					getCoordinates(coor1).removeLinks(coor2);
					if(coor1.getX()!=1) {
						getCoordinates(coor1).addLinks(getCoordinates(coor1.getX()-1,coor1.getY()));
					}
				}
				if(coor2.getX()==coor2.getY() && coor1.getX()<6 && coor1.getX()==coor1.getY()-1 && getCoordinates(coor1).getLinks().contains(coor2)) {
					getCoordinates(coor1).removeLinks(coor2);
					if(coor2.getX()<6) {
						getCoordinates(coor1).addLinks(getCoordinates(coor1.getX()+1,coor1.getY()));
					}
				}
			}
		}
		getCoordinates(5,6).addLinks(getCoordinates(6,6));
		getCoordinates(size/2,size/2).addLinks(getCoordinates(3,4));

		Thread thread = new InitGoalDistanceThread();
		thread.start();
	}

	public static ArrayList<Coordinates> getLinks(Coordinates coor){
		return getCoordinates(coor).getLinks();
	}

	public static ArrayList<Coordinates> getLinks(int x,int y){
		return getCoordinates(x,y).getLinks();
	}

	public static void setGoalDistance(Coordinates coor,int distance) {
		goalDistance.put(getCoordinates(coor), distance);
	}

	public static ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> all = new ArrayList<Coordinates>();
		all.add(start);
		all.add(goal);
		all.addAll(rareBlue);
		all.addAll(rareYellow);
		return all;
	}
	public static Coordinates getCoordinates(int x, int y) {
		ArrayList<Coordinates> list = new ArrayList<Coordinates>();
		list.addAll(getAllCoordinates());
		for(Coordinates coor : getAllCoordinates()) {
			if(ContainsEvent.coor(coor, x,y)) {
				return coor;
			}
		}
		return null;
	}
	public static Coordinates getCoordinates(Coordinates c) {
		ArrayList<Coordinates> list = new ArrayList<Coordinates>();
		list.addAll(getAllCoordinates());
		for(Coordinates coor : getAllCoordinates()) {
			if(ContainsEvent.coor(coor, c)) {
				return coor;
			}
		}
		return null;
	}
	public static int getIndexOfBlue(int x,int y){
		for(int list=0;list<rareBlue.size();list++) {
			if(ContainsEvent.coor(rareBlue.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfBlue(Coordinates coor){
		for(int list=0;list<rareBlue.size();list++) {
			if(ContainsEvent.coor(rareBlue.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static Coordinates getBlue(int x,int y){
		for(Coordinates blue:rareBlue) {
			if(ContainsEvent.coor(blue,x,y)) {
				return blue;
			}
		}
		return null;
	}
	public static Coordinates getBlue(Coordinates coor){
		for(Coordinates blue:rareBlue) {
			if(ContainsEvent.coor(blue,coor)) {
				return blue;
			}
		}
		return null;
	}
	public static ArrayList<Coordinates> getBlueList(){
		return rareBlue;
	}
	public static int getIndexOfYellow(int x,int y){
		for(int list=0;list<rareYellow.size();list++) {
			if(ContainsEvent.coor(rareYellow.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfYellow(Coordinates coor){
		for(int list=0;list<rareYellow.size();list++) {
			if(ContainsEvent.coor(rareYellow.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static Coordinates getYellow(int x,int y){
		for(Coordinates yellow:rareYellow) {
			if(ContainsEvent.coor(yellow,x,y)) {
				return yellow;
			}
		}
		return null;
	}
	public static Coordinates getYellow(Coordinates coor){
		for(Coordinates yellow:rareYellow) {
			if(ContainsEvent.coor(yellow,coor)) {
				return yellow;
			}
		}
		return null;
	}
	public static ArrayList<Coordinates> getYellowList(){
		return rareYellow;
	}

	public static int getGoalDistance() {
		return goalDistance.get(getCoordinates(Player.player.getNowMass()));
	}
	public static int getGoalDistance(Player player) {
		return goalDistance.get(getCoordinates(player.getNowMass()));
	}
	public static int getGoalDistance(Coordinates coor) {
		return goalDistance.get(getCoordinates(coor));
	}
	public static int getGoalDistance(int x,int y) {
		return goalDistance.get(getCoordinates(x,y));
	}

	public static Coordinates getGoalCoor() {
		return goal;
	}

	public static Coordinates getStartCoor() {
		return start;
	}

	public static ArrayList<Boolean> getVector(Coordinates coor, int size){
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		boolean left=false;
		boolean right=false;
		boolean top=false;
		boolean bottom=false;
		for(Coordinates next:getLinks(coor)) {
			if(coor.getX()-1 == next.getX() && coor.getY() == next.getY()) {
				left=true;
			}else if(coor.getX()+1 == next.getX() && coor.getY() == next.getY()) {
				right=true;
			}else if(coor.getX() == next.getX() && coor.getY()-1 == next.getY()) {
				top=true;
			}else if(coor.getX() == next.getX() && coor.getY()+1 == next.getY()) {
				bottom=true;
			}
		}
		list.add(left);
		list.add(right);
		list.add(top);
		list.add(bottom);

		return list;
	}
}

class InitGoalDistanceThread extends Thread{
	private Coordinates nowMass=new Coordinates();
	private ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();
	public InitGoalDistanceThread() {
		nowMass.setValue(Ginga.getGoalCoor());
		this.setDaemon(true);
	}

	@Override
	public void run() {
		int distance=0;
		do {
			Ginga.setGoalDistance(nowMass,distance);

			moveTrajectory.add(new Coordinates(nowMass));

			if(moveTrajectory.size()>1) {
				for(Coordinates coor:Ginga.getLinks(Ginga.getCoordinates(nowMass))) {
					if(ContainsEvent.coor(coor, moveTrajectory.get(moveTrajectory.size()-2)))continue;
					nowMass.setValue(coor);
				}
			}else {
				nowMass.setValue(Ginga.getLinks(Ginga.getCoordinates(nowMass)).get(0));
			}

			distance++;
		}while(!ContainsEvent.coor(nowMass, Ginga.getStartCoor()));
		Ginga.setGoalDistance(nowMass,distance);
	}
}



