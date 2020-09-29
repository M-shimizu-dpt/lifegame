/*
 * 探索機構に関する処理を実装するクラス
 * 探索する必要のある処理を記述
 */

package lifegame.game.event.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.WaitThread;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.Window;

public class Searcher{
	public static Map<Integer,ArrayList<ArrayList<Coordinates>>> nearestTrajectoryList = new HashMap<Integer,ArrayList<ArrayList<Coordinates>>>();//目的地までの移動の軌跡
	public static Map<Coordinates,ArrayList<ArrayList<Coordinates>>> canMoveTrajectoryList = new HashMap<Coordinates,ArrayList<ArrayList<Coordinates>>>();//行くことが出来るマスとそれまでの移動の軌跡
	public static ArrayList<Coordinates> nearestStationList = new ArrayList<Coordinates>();//最寄り駅のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestShopList = new ArrayList<Coordinates>();//最寄り店のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestMassToGoalList = new ArrayList<Coordinates>();//ゴールから最も近いマスリスト

	public static int count;//目的のマスまでの最短距離
	public static long time;//マルチスレッド開始からの経過時間

	//行くことが出来るマスを探索
	public static int searchCanMoveMass(Window window,Player player) {
		canMoveTrajectoryList.clear();
		MassSearchThread thread = new MassSearchThread(window,player.getMove());
		thread.setMass(player.getNowMass());
		thread.setDaemon(true);
		thread.start();
		WaitThread waitthread = new WaitThread(4);
		waitthread.setDaemon(true);
		waitthread.start();
		try {
			thread.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return 0;
	}
	//行くことが出来るマスの内、目的地に最も近いマスを探索
	public static synchronized void setNearestMass(Coordinates nearest,int count) {
		if(NearestSearchThread.nearestCount>=count) {
			if(NearestSearchThread.nearestCount>count) nearestMassToGoalList.clear();
			NearestSearchThread.nearestCount=count;
			nearestMassToGoalList.add(Japan.getCoordinates(nearest));
		}
	}

	//行くことが出来るマスの探索結果を格納
	public static synchronized void setCanMoveMassResult(Coordinates canMoveMass, ArrayList<Coordinates> trajectory) {
		boolean flag = true;
		for(Coordinates coor : canMoveTrajectoryList.keySet()) {
			if(ContainsEvent.coor(coor, canMoveMass)) {
				flag=false;
			}
		}

		canMoveMass = Japan.getCoordinates(canMoveMass);//インスタンスの統一
		if(flag) {
			canMoveTrajectoryList.put(Japan.getCoordinates(canMoveMass), new ArrayList<ArrayList<Coordinates>>());
		}
		canMoveTrajectoryList.get(Japan.getCoordinates(canMoveMass)).add(trajectory);
	}

	//最寄り駅を探索
	public static void searchNearestStation(Window window,Player player) {
		nearestStationList.clear();
		StationSearchThread thread = new StationSearchThread(window);
		thread.setMass(player.getNowMass());
		thread.setDaemon(true);
		thread.start();
	}

	//最寄り駅の探索結果を格納
	public static synchronized void setNearestStationResult(int count, Coordinates nearestStation) {
		if(Searcher.count>=count) {
			if(Searcher.count>count) nearestStationList.clear();//最寄り駅の更新があった場合
			Searcher.count=count;
			boolean flag=true;
			for(Coordinates coor:nearestStationList) {//既に探索済みの駅か
				if(ContainsEvent.coor(coor, nearestStation)) {
					flag=false;
				}
			}
			if(flag) {
				nearestStationList.add(nearestStation);
				//System.out.println("name add:"+japan.getStationName(nearestStation)+"  x:"+nearestStation.getX()+"   y:"+nearestStation.getY());
			}
		}
	}

	//最寄り店を探索
	public static void searchNearestShop(Window window,Player player) {
		nearestShopList.clear();
		ShopSearchThread thread = new ShopSearchThread(window);
		thread.setMass(player.getNowMass());
		thread.setDaemon(true);
		thread.start();
	}

	//最寄り店の探索結果を格納
	public static synchronized void setNearestShopResult(int count, Coordinates nearestShop) {
		if(Searcher.count>=count) {
			if(Searcher.count>count)nearestShopList.clear();
			Searcher.count=count;
			boolean flag=true;
			for(Coordinates coor:nearestShopList) {
				if(ContainsEvent.coor(coor, nearestShop)) {
					flag=false;
				}
			}
			if(flag) {
				nearestShopList.add(nearestShop);
				//System.out.println("x:"+nearestShop.getX()+"   y:"+nearestShop.getY());
			}
		}
	}

	//目的地までの最短距離を計算し、最短ルートを取得
	public static int searchShortestRoute(Window window,Player player) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		do{
			nearestTrajectoryList.clear();
			Japan.allClose();
			//Threadを立ち上げる
			SearchThread thread = new SearchThread(window,player.getNowMass(),SearchThread.searchTime+againtime);
			thread.setMass(player.getNowMass());
			Japan.getCoordinates(player.getNowMass()).open(0);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.setDaemon(true);
			thread.start();

			WaitThread wt = new WaitThread(2);
			wt.setDaemon(true);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			//System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
		}while(Searcher.count==500 && againtime<1000);
		if(Searcher.count==500) System.out.println("探索失敗");
		return 0;
	}

	//目的地までの最短距離と最短ルートを格納
	public static synchronized void setSearchResult(int count, ArrayList<Coordinates> trajectory) {
		trajectory.remove(0);
		if(Searcher.count>=count) {
			Searcher.count=count;
			if(!nearestTrajectoryList.containsKey(count)) {
				nearestTrajectoryList.put(count,new ArrayList<ArrayList<Coordinates>>());
			}
			nearestTrajectoryList.get(count).add(trajectory);
		}
	}

	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static int searchShortestRouteSelectPlayer(Player selectedPlayer) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		boolean endflag;
		do{
			Japan.allClose();
			//Threadを立ち上げる
			OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread(selectedPlayer,OnlyDistanceSearchThread.searchTime+againtime);
			thread.setMass(selectedPlayer.getNowMass());
			Japan.getCoordinates(selectedPlayer.getNowMass()).open(0);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.setDaemon(true);
			thread.start();

			WaitThread wt = new WaitThread(2,againtime);
			wt.setDaemon(true);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
			endflag = true;
			if(ContainsEvent.isDefaultGoalDistance(selectedPlayer))endflag=false;
		}while(!endflag && againtime<1000);
		return 0;
	}

	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static int searchShortestRouteAllPlayers() {
		//再探索は10回まで(1回で出てほしい…)
		for(Player selectedPlayer:Player.players.values()) {
			int againtime=0;
			boolean endflag;
			do{
				Japan.allClose();
				//Threadを立ち上げる
				OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread(selectedPlayer,OnlyDistanceSearchThread.searchTime+againtime);
				thread.setMass(selectedPlayer.getNowMass());
				Japan.getCoordinates(selectedPlayer.getNowMass()).open(0);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.setDaemon(true);
				thread.start();

				WaitThread wt = new WaitThread(2,againtime);
				wt.setDaemon(true);
				wt.start();
				try {
					wt.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				againtime+=100;
				System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
				endflag = true;
				if(ContainsEvent.isDefaultGoalDistance(selectedPlayer))endflag=false;
			}while(!endflag && againtime<1000);
		}
		return 0;
	}
}
