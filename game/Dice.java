package lifegame.game;

public class Dice {
	public int result;
	public int num;
	public Dice(){
		//サイコロの準備
		this.result=0;
		this.num=1;
	}

	public void shuffle(Player player) {
		int rand = (int)(Math.random()*Math.random()*10.0)%6;
		this.result += rand+1+player.buff.effect;
		if(this.result<=0) {
			this.result=1;
		}
	}

	public void init() {
		clearResult();
		clearNum();
	}

	public void clearResult() {
		this.result=0;
	}

	public void clearNum() {
		this.num=1;
	}
}
