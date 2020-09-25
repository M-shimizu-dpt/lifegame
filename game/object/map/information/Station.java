/*
 * 駅に関する情報を管理するクラス
 * 駅の座標や駅に属する物件などを取得・変更することが出来る。
 */

package lifegame.game.object.map.information;

import java.util.ArrayList;

public class Station {
	private String name;
	private Coordinates coordinates = new Coordinates();
	private ArrayList<Property> propertys = new ArrayList<Property>();
	private boolean monoFlag;

	public Station(String name, Coordinates coor) {
		this.name = name;
		this.coordinates = coor;
		this.monoFlag=false;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public String getName() {
		return name;
	}

	public boolean getMono() {
		return monoFlag;
	}

	public boolean isMono() {
		updateMono();//別ですべき
		return monoFlag;
	}

	public void updateMono() {
		for(int i=0;i<getPropertySize();i++) {
			for(int j=i;j<getPropertySize();j++) {
				if(i==j)continue;
				if(!getProperty(i).getOwner().equals(getProperty(j).getOwner()) || !getProperty(i).isOwner()) {
					monoFlag=false;
					return;
				}
			}
		}
		monoFlag=true;
	}

	public void changePropertysMono() {
		for(int i=0;i<getPropertySize();i++) {
			getProperty(i).updateMono(this);
		}
	}

	public boolean containsProperty(Property property) {
		return propertys.contains(property);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addProperty(Property property) {
		propertys.add(property);
	}

	public ArrayList<Property> getPropertys(){
		return propertys;
	}

	public Property getProperty(int index) {
		return propertys.get(index);
	}

	public int getPropertySize() {
		return propertys.size();
	}
}
