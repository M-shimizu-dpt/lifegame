/*
 * id=0→ターンエンド待ち
 * id=1→借金返済待ち,
 * id=2→最短経路探索待ち
 * id=3→決算待ち
 * id=4→移動可能マス探索待ち
 * id=6→全てのプレイヤーの最短距離探索待ち
 * id=7→一時停止
 * id=8→カードを捨てる待ち
 */
package lifegame.game;

import lifegame.game.map.print.Window;
import lifegame.game.object.Player;
import lifegame.game.search.SearchThread;

public class WaitThread extends Thread{
	private int id;
	private int money;
	private int size;
	private int againtime;

	public WaitThread(int id) {
		this.id=id;
		this.againtime=0;
	}
	public WaitThread(int id,int againtime) {
		this.id=id;
		this.againtime=againtime;
	}

	public WaitThread(int id,int money,int size) {
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
			while(!Window.turnEndFlag) {//プレイヤーのターン中の処理が終わるとループを抜ける
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
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
			while(System.currentTimeMillis()-Window.time <= SearchThread.searchTime+againtime) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			SearchThread.initSearchTime();
			break;
		case 3:
			while(!Window.closingEndFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			Window.closingEndFlag=false;
			break;
		case 4:
			while(System.currentTimeMillis()-Window.time <= 500) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			break;
		case 5:
			while(!Window.bonbyTurnEndFlag) {//ボンビーターン中の処理が終わるとループを抜ける
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		case 6:
			while(System.currentTimeMillis()-Window.time <= SearchThread.searchTime*4) {
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
			while(!Window.random2EndFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {

				}
			}
			Window.random2EndFlag=false;
			break;
		case 9:
			while(!Window.throwFlag) {
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			Window.throwFlag=false;
			break;
		default:
			break;
		}
	}
}