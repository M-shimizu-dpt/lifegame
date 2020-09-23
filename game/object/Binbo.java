/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */


package lifegame.game.object;

import java.util.Random;

public  class Binbo{
	private String name;
	private Player catchplayer;
	//private int playerId;
	private int month = 4;

	public Binbo() {
		name = "ボンビー";
	}


	public int getBmonth() {
		return this.month;
	}

	public void binboTurn() {
		//スタブメソッド
		System.out.println(name + "のターン");
		System.out.println(this.catchplayer.getName());
		randomBinboEvent();
		System.out.println(name +"のターン終了");
	}
	public void sutabBinboFinishTurn() {
		//スタブメソッド

		Window.BonbyTurnEndFlag = true;
	}

	public void setBinboPlayer(Player players) {
		this.catchplayer = players;
	}

	public Player getBinboPlayer() {
		return this.catchplayer;
	}


	private void randomBinboEvent() {
		Random rand = new Random();
		int result = rand.nextInt(100);
		if(result<10) {
			Makeover();
		}else {
			Makeover();
		}
	}

	private void Makeover() {
		Random rand = new Random();
		double result = rand.nextDouble();
		if(result<0.3) {
			this.name = "赤ちゃんボンビー";
		}else if(result<0.5) {
			this.name = "タイフーンボンビー";
		}else if(result<0.7) {
			this.name = "幸せボンビー";
		}else{
			this.name = "キングボンビー";
		}
	}

}
