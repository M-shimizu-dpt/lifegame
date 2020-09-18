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

public  class Binbo{
	/*
	 * フィールド
	 */
	private Player catchplayer;
	//private int playerId;
	private int month = 4;
	public int getBmonth() {
		return this.month;
	}
	/*
	 * メソッド
	 */
	public void binboTurn() {
		//スタブメソッド
		System.out.println("ボンビーのターン");

	}

	public void setBinboPlayer(Player players) {
		this.catchplayer = players;
	}

	public Player getBinboPlayer() {
		return this.catchplayer;
	}

}



//マスに到着した時のマスのイベント処理
//379
//を呼んでるメソッド
//moveMaps
//1325













