package lifegame.game;

import java.util.ArrayList;

public class Property {
	public String name;//名前
	public String owner;//管理者
	public int money;//購入金額
	public ArrayList<Double> rate = new ArrayList<Double>();//利益率(3段階)
	public int level;//利益率の段階
	private boolean monoflag;

	public Property(String name,int money,double rate1, double rate2,double rate3) {
		this.name=name;
		this.money=money;
		this.owner = "";
		this.rate.add(rate1);
		this.rate.add(rate2);
		this.rate.add(rate3);
		this.level=0;
		this.monoflag=false;
	}

	public void monoOn() {
		if(!this.monoflag) {
			for(int i=0;i<this.rate.size();i++) {
				this.rate.set(i,this.rate.get(i)*2);
			}
			this.monoflag=true;
		}
	}

	public void monoOff() {
		if(this.monoflag) {
			for(int i=0;i<this.rate.size();i++) {
				this.rate.set(i,this.rate.get(i)/2);
			}
			this.monoflag=false;
		}
	}

	public int getRate() {
		return (int)((double)rate.get(level)*100.0);
	}

	//利益計算
	public int getProfit() {
		int profit=0;
		profit = (int)((double)money * rate.get(this.level));
		return profit;
	}

	public void buy(Player player,int level) {
		this.owner=player.name;
		player.addProperty(this);
		player.addMoney(-this.money);
		this.level=level;
	}

	public void sell(Player player) {
		this.owner="";
		player.removeProperty(this);
		player.addMoney(this.money/2);
		this.level=0;
	}
}
