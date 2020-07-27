package lifegame.game;

public class Dice {
	public int result;
	public int num;
	public Dice(){
		//サイコロの準備
		this.result=0;
		this.num=1;
	}

	//サイコロの数に応じたサイコロ処理
	public void shuffle() {
		int rand = (int)(Math.random()*10.0)%6;
		this.result += rand+1;
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
