/*
 * ボンビーに関する処理を実装するクラス
 * ボンビーが行う処理を記述
 */


package lifegame.game;

public  class Binbo{
	private Player catchplayer;
	//private int playerId;
	private int month = 4;

	public int getBmonth() {
		return this.month;
	}

	public void binboTurn() {
		System.out.println("ボンビーのターン");
	}

	public void setBinboPlayer(Player players) {
		this.catchplayer = players;
	}

	public Player getBinboPlayer() {
		return this.catchplayer;
	}

}
