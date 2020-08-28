package lifegame.game;

import java.util.Random;

public class Dice {
	public int result;
	public int num;
	public Dice(){
		//サイコロの準備
		this.result=0;
		this.num=1;
	}

	public void shuffle(Player player) {
		Random rand = new Random();
		this.result += rand.nextInt(6)+1+player.buff.effect;
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
