/*TodoList
 * 	init ゴールされたら一番遠い人につく(始まってゴールされるまではoff)
 * 		遠い人が複数人いる場合、
 *
 * 			ゴール判定
 * 			一番遠い人を探索
 * 			(ゴール時に一番ゴールまでの距離が多い人を検索し、一人を選択)
 * 			一番遠い人名前を取得
 * 			 「名前よろしくねぇ～ん」
 *
 * 	インスタンス化は最初
 * 	ゴールしたタイミングで一番遠い人のpoorgodFlagがON
 * 	その人を保持(名前だけ?) もしくは poorflagがOnの人を探索
 *
 *		 移動したマスに別のプレイヤーが存在したら、つく人変更(一番最後にそのマスについた人)
 *		軌跡をとっているListがあるらしいのでそのリスト内とプレイヤーのマスが一致した際に片方がONだった場合に,ONとOFFいれかえ
 *
 *
 *
 * 	プレイヤーがターン終了時にボンビーがついていたら、ボンビーのターン発動
 *
 *
 */




//japan最後の方のようにwindow
//window window;

//getwindow(windows);
//this.window = windows

//のように新スタンスのコピーをもち、PlayerのbonbyフラグがONにやつに影響を与える。
//今の実装ではplayerの
//プレイヤーは自分のもっているぷろぱてぃ
//プロパティはオーナーをもっている
//ジャパンはステーション(すべて)
//ステーションはプロパティをもっている
//
//
//物件


//改修作業
//moveTrajectoryについて
//coordinate型に変更させる


package lifegame.game;

import java.util.Random;

public  class Binbo{
	/*
	 * フィールド
	 */
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
	/*
	 * メソッド
	 */
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



//マスに到着した時のマスのイベント処理
//379
//を呼んでるメソッド
//moveMaps
//1325













