/*
 * id=0→ターンエンド待ち
 * id=1→借金返済待ち,
 * id=2→最短経路探索待ち
 * id=3→決算待ち
 * id=4→移動可能マス探索待ち
 * id=5→ボンビーターン待ち
 * id=6→全てのプレイヤーの最短距離探索待ち
 * id=7→一時停止
 * id=8→randomイベント待ち
 * id=9→カード捨て待ち
 * id=10→アプリ開始待ち
 */
package lifegame.game.event;

import lifegame.game.event.search.OnlyDistanceSearchThread1;
import lifegame.game.event.search.OnlyDistanceSearchThread2;
import lifegame.game.event.search.OnlyDistanceSearchThread3;
import lifegame.game.event.search.OnlyDistanceSearchThread4;
import lifegame.game.event.search.SearchThread;
import lifegame.game.main.App;
import lifegame.game.object.Binbo;
import lifegame.game.object.Player;

public class WaitThread extends Thread{
	private int id;
	private int money;
	private int size;
	private int againtime;

	public WaitThread(int id) {
		this.setDaemon(true);
		this.id=id;
		this.againtime=0;
	}
	public WaitThread(int id,int againtime) {
		this.setDaemon(true);
		this.id=id;
		this.againtime=againtime;
	}

	public WaitThread(int id,int money,int size) {
		this.setDaemon(true);
		this.id=id;
		this.money=money;
		this.size=size;
		this.againtime=0;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Override
	public void run() {
		switch(id) {
		case 0:
			while(!App.isTurnEnd()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			App.initTurnEndFlag();
			break;
		case 1:
			while(this.money < 0 && this.size > 0) {
				try {
		            Thread.sleep(100);
		        } catch (InterruptedException e) {
		        	e.printStackTrace();
		        }
			}
			break;
		case 2:
			while(System.currentTimeMillis()-Searcher.time <= SearchThread.searchTime+againtime) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			SearchThread.initSearchTime();
			break;
		case 3:
			while(!ClosingEvent.closingEndFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			ClosingEvent.closingEndFlag=false;
			break;
		case 4:
			while(System.currentTimeMillis()-Searcher.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			break;
		case 5:

			while(!Binbo.isBinboTurn()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			Binbo.initBinboFlag();
			break;
		case 6:
			while(System.currentTimeMillis()-Searcher.time <= SearchThread.searchTime*4) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			SearchThread.initSearchTime();
			break;
		case 7:
			while(Player.isStop()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		case 8:
			while(!RandomEvent.isEnd()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			RandomEvent.initEndFlag();
			break;
		case 9:
			while(!ContainsEvent.isThrowed()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			FrameEvent.initThrowFlag();
			break;
		case 10:
			while(!App.isStarted()) {
	    		try {
	    			Thread.sleep(100);
	    		}catch(InterruptedException e) {

	    		}
	    	}
			break;
		case 11:
			while(System.currentTimeMillis()-OnlyDistanceSearchThread1.time <= OnlyDistanceSearchThread1.searchTime+againtime
			|| System.currentTimeMillis()-OnlyDistanceSearchThread2.time <= OnlyDistanceSearchThread2.searchTime+againtime
			|| System.currentTimeMillis()-OnlyDistanceSearchThread3.time <= OnlyDistanceSearchThread3.searchTime+againtime
			|| System.currentTimeMillis()-OnlyDistanceSearchThread4.time <= OnlyDistanceSearchThread4.searchTime+againtime) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			OnlyDistanceSearchThread1.initSearchTime();
			OnlyDistanceSearchThread2.initSearchTime();
			OnlyDistanceSearchThread3.initSearchTime();
			OnlyDistanceSearchThread4.initSearchTime();
			break;
		case 12:
			while(!MassEvent.isMassEventEnd()) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			MassEvent.initMassEvent();
			break;
		default:
			assert null==null : "null";
			break;

		}
	}
}