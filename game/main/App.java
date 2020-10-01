/*
 * アプリの基本操作を行うクラス
 * 基本的な処理は全てこのクラスに実装
 */

package lifegame.game.main;

import java.util.ArrayList;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.CardEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.event.Searcher;
import lifegame.game.event.WaitThread;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;

public class App {
	public static int turn=0;//現在のターン
	public static int year=1;//今の年
	public static int month=4;//今の月

	private static boolean startFlag = false;//スタート画面が終わるまで待つためのフラグ
	private static boolean turnEndFlag=false;//ターン交代するためのフラグ


    public static void main(String[] args) {
        App app = new App();
    	app.run();
    }

  	public static void initTurnEndFlag() {
  		App.turnEndFlag=false;
  	}

  	public static boolean isTurnEnd() {
  		return App.turnEndFlag;
  	}

  	public static boolean isStarted() {
  		return App.startFlag;
  	}

  	public static void start() {
  		App.startFlag=true;
  	}

  	public static void turnEnd() {
  		App.turnEndFlag=true;
  	}

  	public void monthUpdate(boolean first) {
  		if(first) {
  			FrameEvent.printMonthFrame();
  		}else {
    		if(App.turn==3) {
    			ArrayList<Integer> moneyList = new ArrayList<Integer>();
    			for(Player player:Player.players.values()) {
    				moneyList.add(player.getMoney());
    			}
    			if(App.month==3) {
    				FrameEvent.openClosing();
	    			App.year++;
				}
    			App.month++;
    			if(App.month==13) {
    				App.month=1;
    			}
    			FrameEvent.printMonthFrame();
    			App.turn=0;
    		}else {
    			App.turn++;
    		}
  		}
  	}

  	 //プレイ中の動作
  	private void play(int endYear) throws InterruptedException{
  		Boolean first=true;
  		Player.setStopFlag(false);
  		//BinboEvent.initBinbo();初期でボンビーを憑けないなら
  		BinboEvent.initIsBinbo();//初期でボンビーを憑けさせるなら//debug

  		while(true) {
  			monthUpdate(first);
  			if(ContainsEvent.isOwners()) {
  				FrameEvent.openRandom2();
  			}
	  		if(ContainsEvent.isEnd(endYear)) {
		  		break;
		  	}
		  	first=false;
		  	Player.setNowPlayer();//このターンのプレイヤーを選定
		  	FrameEvent.waitButtonUpdate();
		  	Searcher.searchShortestRoute(Player.player);//目的地までの最短経路を探索
		  	WaitThread waitthred  = new WaitThread(2);//再探索に対応していない為、3回程再探索を行っていた場合reloadInfoで正しく更新されない可能性がある。
		  	waitthred.start();
		  	waitthred.join();
		  	Japan.saveGoal();
		  	FrameEvent.moveMaps();//画面遷移が少し遅い
		  	FrameEvent.reloadInfo();//画面上部に表示している情報を更新
		  	CardEvent.priceSort(Player.player.getCards());//プレイヤーが持つカードを価格順にソート
		  	if(!Player.player.isPlayer()) {//cpu操作
		  		Player.player.cpu();
		  	}else {
		  		FrameEvent.printMenu();
		  	}
			WaitThread turnEnd  = new WaitThread(0);//ターン終了まで待機
			turnEnd.start();
			turnEnd.join();
			if(ContainsEvent.binboPlayer()) {
				BinboEvent.start();
				WaitThread bonbyTurnEnd  = new WaitThread(5);//ターン終了まで待機
				bonbyTurnEnd.start();
				bonbyTurnEnd.join();
			}
			Thread.sleep(1000);
			CardEvent.resetFlags();
			Japan.alreadys.clear();//このターンに購入した物件リストを初期化
		}
  		assert endYear < year;
  		FrameEvent.finish();
		System.out.println("終わり");
	}

    private void run() {
    	Japan.init();

    	int[] result = FrameEvent.openStartFrame();

    	int playerCount = result[0];
    	int yearLimit = result[1];
    	assert(playerCount>=0 && playerCount<=4);
    	assert(yearLimit>0 && yearLimit<=100);


    	Card.init();

    	Dice.init();

    	FrameEvent.init(playerCount);
    	try {
        	play(yearLimit);
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
    }
}