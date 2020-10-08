/*
 * 探索機構に関する処理を実装するクラス
 * 探索する必要のある処理を記述
 */

package lifegame.game.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lifegame.game.event.search.MassSearchThread;
import lifegame.game.event.search.NearestSearchThread;
import lifegame.game.event.search.OnlyDistanceSearchThread;
import lifegame.game.event.search.OnlyDistanceSearchThread1;
import lifegame.game.event.search.OnlyDistanceSearchThread2;
import lifegame.game.event.search.OnlyDistanceSearchThread3;
import lifegame.game.event.search.OnlyDistanceSearchThread4;
import lifegame.game.event.search.SearchThread;
import lifegame.game.event.search.ShopSearchThread;
import lifegame.game.event.search.StationSearchThread;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;

public class Searcher{
	//Eventに情報をため込むのは望ましくない
	public static Map<Integer,ArrayList<ArrayList<Coordinates>>> nearestTrajectoryList = new HashMap<Integer,ArrayList<ArrayList<Coordinates>>>();//目的地までの移動の軌跡
	public static Map<Coordinates,ArrayList<ArrayList<Coordinates>>> canMoveTrajectoryList = new HashMap<Coordinates,ArrayList<ArrayList<Coordinates>>>();//行くことが出来るマスとそれまでの移動の軌跡
	public static ArrayList<Coordinates> nearestStationList = new ArrayList<Coordinates>();//最寄り駅のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestShopList = new ArrayList<Coordinates>();//最寄り店のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestMassToGoalList = new ArrayList<Coordinates>();//ゴールから最も近いマスリスト

	public static int count;//目的のマスまでの最短距離
	public static long time;//マルチスレッド開始からの経過時間


	public static void searchGoalDistance() {
		OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread();
		thread.start();
	}

	//行くことが出来るマスを探索
	public static int searchCanMoveMass(Player player) {
		canMoveTrajectoryList.clear();
		MassSearchThread thread = new MassSearchThread(player.getMove());
		thread.setMass(player.getNowMass());
		thread.start();
		WaitThread waitthread = new WaitThread(4);
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
	public static void searchNearestStation(Player player) {
		nearestStationList.clear();
		StationSearchThread thread = new StationSearchThread();
		thread.setMass(player.getNowMass());
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
	public static void searchNearestShop(Player player) {
		nearestShopList.clear();
		ShopSearchThread thread = new ShopSearchThread();
		thread.setMass(player.getNowMass());
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
	public static int searchShortestRoute(Player player) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		do{
			nearestTrajectoryList.clear();
			//Threadを立ち上げる
			SearchThread thread = new SearchThread(player.getNowMass(),SearchThread.searchTime+againtime);
			thread.start();

			WaitThread wt = new WaitThread(2);
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

	@Deprecated
	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static int searchShortestRouteSelectPlayer(Player selectedPlayer) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		boolean endflag;
		do{
			Japan.allClose();
			//Threadを立ち上げる
			OnlyDistanceSearchThread1 thread1 = new OnlyDistanceSearchThread1(OnlyDistanceSearchThread1.searchTime+againtime);
			thread1.start();
			OnlyDistanceSearchThread2 thread2 = new OnlyDistanceSearchThread2(OnlyDistanceSearchThread2.searchTime+againtime);
			thread2.start();
			OnlyDistanceSearchThread3 thread3 = new OnlyDistanceSearchThread3(OnlyDistanceSearchThread3.searchTime+againtime);
			thread3.start();
			OnlyDistanceSearchThread4 thread4 = new OnlyDistanceSearchThread4(OnlyDistanceSearchThread4.searchTime+againtime);
			thread4.start();

			WaitThread wt = new WaitThread(11,againtime);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			System.out.println("again:"+(againtime/100)+"     id:"+thread1.getId());
			endflag = true;
			if(ContainsEvent.isDefaultGoalDistance(selectedPlayer))endflag=false;
		}while(!endflag && againtime<1000);
		return 0;
	}

	@Deprecated
	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static void searchShortestRouteAllPlayers() {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		boolean endflag1=false,endflag2=false,endflag3=false,endflag4=false;
		OnlyDistanceSearchThread1 thread1 = new OnlyDistanceSearchThread1();
		OnlyDistanceSearchThread2 thread2 = new OnlyDistanceSearchThread2();
		OnlyDistanceSearchThread3 thread3 = new OnlyDistanceSearchThread3();
		OnlyDistanceSearchThread4 thread4 = new OnlyDistanceSearchThread4();
		do{
			//Threadを立ち上げる
			if(!endflag1) {
				thread1 = new OnlyDistanceSearchThread1(OnlyDistanceSearchThread1.searchTime+againtime);
				thread1.start();
			}
			if(!endflag2) {
				thread2 = new OnlyDistanceSearchThread2(OnlyDistanceSearchThread2.searchTime+againtime);
				thread2.start();
			}
			if(!endflag3) {
				thread3 = new OnlyDistanceSearchThread3(OnlyDistanceSearchThread3.searchTime+againtime);
				thread3.start();
			}
			if(!endflag4) {
				thread4 = new OnlyDistanceSearchThread4(OnlyDistanceSearchThread4.searchTime+againtime);
				thread4.start();
			}
			WaitThread wt = new WaitThread(11,againtime);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			if(!ContainsEvent.isDefaultGoalDistance(Player.getPlayer(0))) {
				System.out.println("thread1:end");
				endflag1=true;
			}
			if(!endflag1) {
				System.out.println("again:"+(againtime/100)+"     thread1");
			}
			if(!ContainsEvent.isDefaultGoalDistance(Player.getPlayer(1))) {
				System.out.println("thread2:end");
				endflag2=true;
			}
			if(!endflag2) {
				System.out.println("again:"+(againtime/100)+"     thread2");
			}
			if(!ContainsEvent.isDefaultGoalDistance(Player.getPlayer(2))) {
				System.out.println("thread3:end");
				endflag3=true;
			}
			if(!endflag3) {
				System.out.println("again:"+(againtime/100)+"     thread3");
			}
			if(!ContainsEvent.isDefaultGoalDistance(Player.getPlayer(3))) {
				System.out.println("thread4:end");
				endflag4=true;
			}
			if(!endflag4) {
				System.out.println("again:"+(againtime/100)+"     thread4");
			}
		}while(!(endflag1 && endflag2 && endflag3 && endflag4) && againtime<500);
	}
}
