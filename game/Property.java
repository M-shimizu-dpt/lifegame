package lifegame.game;

import java.util.ArrayList;

public class Property {
	public String name;//名前
	public String owner;//管理者
	public int money;//購入金額
	public ArrayList<Double> rate = new ArrayList<Double>();//利益率(3段階)
	public int level;//利益率の段階

	public Property(String name,int money,double rate1, double rate2,double rate3) {
		this.name=name;
		this.money=money;
		this.owner = "";
		rate.add(rate1);
		rate.add(rate2);
		rate.add(rate3);
		this.level=0;
	}

	//利益計算
	public int getProfit() {
		int profit=0;
		profit = (int)((double)money * rate.get(this.level));
		return profit;
	}

	public void buy(String owner,int level) {
		this.owner=owner;
		this.level=level;
	}

	public void sell() {
		this.owner="";
		this.level=0;
	}
}
