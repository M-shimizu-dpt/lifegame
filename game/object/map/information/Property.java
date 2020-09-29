/*
 * 物件情報を管理するクラス
 * 物件情報を取得したり、利益計算などを記述
 */

package lifegame.game.object.map.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lifegame.game.object.Player;

public class Property {
	private String name;//名前
	private String owner;//管理者
	private int amount;//購入金額
	private int group;//物件分類（1：食品、2：農林、3：観光、4：水産、5：工業、6：商業）
	private Map<Boolean,ArrayList<Double>> rate = new HashMap<Boolean,ArrayList<Double>>();//利益率(3段階)
	private int level;//利益率の段階
	private Station station;//所属する駅

	public Property(String name,int amount,int group,double rate1, double rate2,double rate3,Station station) {
		this.setName(name);
		this.setAmount(amount);
		this.setOwner("");
		this.setGroup(group);
		this.setLevel(0);
		this.setStation(station);
		ArrayList<Double> normRate = new ArrayList<Double>();
		normRate.add(rate1);
		normRate.add(rate2);
		normRate.add(rate3);
		this.rate.put(false, normRate);
		ArrayList<Double> monoRate = new ArrayList<Double>();
		monoRate.add(rate1*2);
		monoRate.add(rate2*2);
		monoRate.add(rate3*2);
		this.rate.put(true, monoRate);

	}

	public Station getStation() {
		return this.station;
	}

	public void setStation(Station station) {
		this.station=station;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner=owner;
	}
	public void setOwner(Player player) {
		this.owner=player.getName();
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount=amount;
	}

	public int getGroup() {
		return this.group;
	}

	public void setGroup(int group) {
		this.group=group;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level=level;
	}

	public boolean isMono() {
		return station.isMono();
	}

	public int getRate() {
		return (int)((double)rate.get(this.station.isMono()).get(level)*100.0);
	}

	public int getRate(int level) {
		return (int)((double)rate.get(this.station.isMono()).get(level)*100.0);
	}

	public int getRate(boolean monoflag,int level) {
		return (int)((double)rate.get(monoflag).get(level)*100.0);
	}

	//利益計算
	public int getProfit() {
		int profit=0;
		profit = (int)((double)amount * rate.get(this.station.isMono()).get(level));
		return profit;
	}
}
