/*
 * アプリの基本操作を行うクラス
 * 基本的な処理は全てこのクラスに実装
 */

package lifegame.game.main;

import lifegame.game.WaitThread;
import lifegame.game.map.information.Japan;
import lifegame.game.map.print.Window;
import lifegame.game.object.Binbo;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.search.Searcher;

public class App {
	public static int turn=0;//現在のターン
	public static int year=1;//今の年
	public static int month=4;//今の月
	public static Japan japan = new Japan();//物件やマス情報
	public static boolean startFlag = false;//スタート画面が終わるまで待つためのフラグ
	public static Binbo poorgod = new Binbo();

    public static void main(String[] args) {
        App app = new App();
    	app.run();
    }

    /*
     * プレイヤーの順番をランダムに入れ替えれるようにする
     */
    private void run() {
    	Window window = new Window();

    	int[] result = window.printStart();

    	int playerCount = result[0];
    	int yearLimit = result[1];
    	assert(playerCount>=0 && playerCount<=3);
    	assert(yearLimit>0 && yearLimit<=100);


    	Card.init(window);
    	Dice.init();

    	window.initWindow(yearLimit,playerCount);
    	try {
        	play(window,yearLimit);
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
    }

    //プレイ中の動作
  	private void play(Window window,int endYear) throws InterruptedException{
  		Boolean first=true;
  		Player.setStopFlag(false);
  		boolean onceflag=false;//bonb付着debug用

  		while(true) {
	  		if(window.monthUpdate(first,endYear)) {
		  		break;
		  	}
		  	first=false;
		  	System.out.println(App.turn);
		  	Player.setNowPlayer();//このターンのプレイヤーを選定
		  	window.waitButtonUpdate();
		  	Searcher.searchShortestRoute(window,Player.player);//目的地までの最短経路を探索
		  	WaitThread waitthred  = new WaitThread(2);//再探索に対応していない為、3回程再探索を行っていた場合reloadInfoで正しく更新されない可能性がある。
		  	waitthred.start();
		  	waitthred.join();
		  	App.japan.saveGoal();
		  	window.moveMaps();//画面遷移が少し遅い
		  	window.reloadInfo();//画面上部に表示している情報を更新
		  	Card.priceSort(Player.player.getCards());//プレイヤーが持つカードを価格順にソート
		  	if(!Player.player.isPlayer()) {//cpu操作
		  		Player.player.cpu(window,turn);
		  	}else {
		  		window.printMenu();
		  	}
		  	if(onceflag==false) {//debug用
				//ボンビー初期設定//debug用
				Player.player.changeBonby();//debug
				poorgod.setPlayerBinbo(Player.player);
				onceflag=true;
		  	}
		  	poorgod.clearStopPlayersNowMass();
		  	poorgod.setStopPlayersNowMass();//動いていない人の現在地を取得(ボンビー用)
			WaitThread turnEnd  = new WaitThread(0);//ターン終了まで待機
			turnEnd.start();
			turnEnd.join();
			if(poorgod.containsBinbo(Player.player)) {
				poorgod.start(window);
				WaitThread bonbyTurnEnd  = new WaitThread(5);//ターン終了まで待機
				bonbyTurnEnd.start();
				bonbyTurnEnd.join();
			}
			Thread.sleep(1000);
			Window.turnEndFlag=false;
			App.japan.alreadys.clear();//このターンに購入した物件リストを初期化
		}
		System.out.println("終わり");
	}
  	public static void start() {
  		App.startFlag=true;
  	}
}