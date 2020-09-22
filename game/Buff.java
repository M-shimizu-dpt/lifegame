package lifegame.game;

public class Buff{//インターフェースっぽい？
	private int effect;
	private int period;
	public Buff() {
		this.effect=0;
		this.period=0;
	}
	//新規effect
	public void addBuff(int effect,int period) {
		this.period=period;
		this.effect=effect;
		System.out.println("effect:"+effect+"     period:"+period);
	}
	public void elapsed() {
		if(period > 0) {
			this.period--;
		}
		if(period == 0) {
			clearEffect();
		}else if(period < 0) {
			System.out.println("periodの値が不適切です。");
		}
	}
	public void clearEffect() {
		this.effect=0;
	}

	public int getEffect() {
		return this.effect;
	}

	public int getPeriod() {
		return this.period;
	}
}
