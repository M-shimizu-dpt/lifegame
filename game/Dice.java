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

	public int shuffle(Player player) {
		for(int i=0;i<this.num;i++) {//サイコロの数だけサイコロを回わす；
			if(Card.usedFixedCard)break;//初めからresultが入力されていれば
			Random rand = new Random();
			this.result += rand.nextInt(6)+1+player.buff.effect;
			if(this.result<=0) {
				this.result=1;
			}
			player.buff.elapsed();
		}
		System.out.println("result:"+this.result+"  num:"+this.num);
		return this.result;
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
