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
	private boolean monoflag;

	public Property(String name,int amount,int group,double rate1, double rate2,double rate3) {
		this.name=name;
		this.amount=amount;
		this.owner = "";
		this.group=group;
		ArrayList<Double> falseRate = new ArrayList<Double>();
		falseRate.add(rate1);
		falseRate.add(rate2);
		falseRate.add(rate3);
		this.rate.put(false, falseRate);
		ArrayList<Double> trueRate = new ArrayList<Double>();
		trueRate.add(rate1*2);
		trueRate.add(rate2*2);
		trueRate.add(rate3*2);
		this.rate.put(true, trueRate);
		this.level=0;
		this.monoflag=false;
	}

	public boolean containsOwner(Property property) {
		return this.owner.equals(property.owner);
	}

	public boolean isOwner() {
		return !this.owner.equals("");
	}

	public String getName() {
		return this.name;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner=owner;
	}

	public int getAmount() {
		return this.amount;
	}

	public int getGroup() {
		return this.group;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean isMono() {
		return monoflag;
	}

	public void setMono(boolean mono) {
		this.monoflag=mono;
	}

	public void updateMono(Station station) {
		monoflag = station.getMono();
	}

	public int getRate() {
		return (int)((double)rate.get(monoflag).get(level)*100.0);
	}

	public int getRate(int level) {
		return (int)((double)rate.get(monoflag).get(level)*100.0);
	}

	public int getRate(boolean monoflag,int level) {
		return (int)((double)rate.get(monoflag).get(level)*100.0);
	}

	//利益計算
	public int getProfit() {
		int profit=0;
		profit = (int)((double)amount * rate.get(monoflag).get(level));
		return profit;
	}

	public void buy(Player player,int level) {
		this.owner=player.getName();
		player.addProperty(this);
		player.addMoney(-this.amount);
		this.level=level;
	}
	public void buy(Player player) {
		this.owner=player.getName();
		player.addProperty(this);
		player.addMoney(-this.amount);
		this.level++;
	}

	public void sell(Player player) {
		this.owner="";
		player.removeProperty(this);
		player.addMoney(this.amount/2);
		this.level=0;
	}
}
