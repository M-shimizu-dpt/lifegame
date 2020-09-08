package lifegame.game;

import java.util.Random;

public class Dice {
	private int result;
	private int num;

	public Dice(){
		//サイコロの準備
		this.result=0;
		this.num=1;
	}

	public int shuffle(Player player) {
		for(int i=0;i<this.num;i++) {//サイコロの数だけサイコロを回わす；
			if(Card.usedFixedCard)break;//初めからresultが入力されていれば
			Random rand = new Random();
			this.result += rand.nextInt(6)+1+player.getBuff().effect;
			if(this.result<=0) {
				this.result=1;
			}
			player.getBuff().elapsed();
		}
		System.out.println("result:"+this.result+"  num:"+this.num);
		return this.result;
	}

	public void init() {
		clearResult();
		clearNum();
	}

	public void setResult(int result) {
		this.result=result;
	}

	public void clearResult() {
		this.result=0;
	}

	public void setNum(int num) {
		this.num=num;
	}

	public void clearNum() {
		this.num=1;
	}

	public void clear() {
		clearResult();
		clearNum();
	}
}
