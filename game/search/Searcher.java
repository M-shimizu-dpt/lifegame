/*
 * 探索機構に関する処理を実装するクラス
 * 探索する必要のある処理を記述
 */

package lifegame.game.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lifegame.game.WaitThread;
import lifegame.game.Window;
import lifegame.game.map.Coordinates;
import lifegame.game.player.Player;

public class Searcher{
	public static Map<Integer,ArrayList<ArrayList<Coordinates>>> nearestTrajectoryList = new HashMap<Integer,ArrayList<ArrayList<Coordinates>>>();//目的地までの移動の軌跡
	public static Map<Coordinates,ArrayList<ArrayList<Coordinates>>> canMoveTrajectoryList = new HashMap<Coordinates,ArrayList<ArrayList<Coordinates>>>();//行くことが出来るマスとそれまでの移動の軌跡
	public static ArrayList<Coordinates> nearestStationList = new ArrayList<Coordinates>();//最寄り駅のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestShopList = new ArrayList<Coordinates>();//最寄り店のリスト(複数存在する場合、その中からランダムに選択)
	public static ArrayList<Coordinates> nearestMassToGoalList = new ArrayList<Coordinates>();//ゴールから最も近いマスリスト

	//行くことが出来るマスを探索
	public static void searchCanMoveMass(Window window,Player player) {
		canMoveTrajectoryList.clear();
		MassSearchThread thread = new MassSearchThread(window,player.getMove());
		thread.setMass(player.getNowMass());
		thread.start();
		WaitThread waitthread = new WaitThread(4);
		waitthread.start();
		try {
			thread.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	//行くことが出来るマスの内、目的地に最も近いマスを探索
	public static synchronized void setNearestMass(Coordinates nearest,int count) {
		if(NearestSearchThread.nearestCount>=count) {
			if(NearestSearchThread.nearestCount>count) nearestMassToGoalList.clear();
			NearestSearchThread.nearestCount=count;
			nearestMassToGoalList.add(Window.japan.getCoordinates(nearest));
		}
	}

	//行くことが出来るマスの探索結果を格納
	public static synchronized void setCanMoveMassResult(Coordinates canMoveMass, ArrayList<Coordinates> trajectory) {
		boolean flag = true;
		for(Coordinates coor : canMoveTrajectoryList.keySet()) {
			if(coor.contains(canMoveMass)) {
				flag=false;
			}
		}

		canMoveMass = Window.japan.getCoordinates(canMoveMass);//インスタンスの統一
		if(flag) {
			canMoveTrajectoryList.put(Window.japan.getCoordinates(canMoveMass), new ArrayList<ArrayList<Coordinates>>());
		}
		canMoveTrajectoryList.get(Window.japan.getCoordinates(canMoveMass)).add(trajectory);
	}

	//最寄り駅を探索
	public static void searchNearestStation(Window window,Player player) {
		nearestStationList.clear();
		StationSearchThread thread = new StationSearchThread(window);
		thread.setMass(player.getNowMass());
		thread.start();
	}

	//最寄り駅の探索結果を格納
	public static synchronized void setNearestStationResult(int count, Coordinates nearestStation) {
		if(Window.count>=count) {
			if(Window.count>count) nearestStationList.clear();//最寄り駅の更新があった場合
			Window.count=count;
			boolean flag=true;
			for(Coordinates coor:nearestStationList) {//既に探索済みの駅か
				if(coor.contains(nearestStation)) {
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
		thread.start();
	}

	//最寄り店の探索結果を格納
	public static synchronized void setNearestShopResult(int count, Coordinates nearestShop) {
		if(Window.count>=count) {
			if(Window.count>count)nearestShopList.clear();
			Window.count=count;
			boolean flag=true;
			for(Coordinates coor:nearestShopList) {
				if(coor.contains(nearestShop)) {
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
	public static void searchShortestRoute(Window window,Player player) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		do{
			nearestTrajectoryList.clear();
			Window.japan.allClose();
			//Threadを立ち上げる
			SearchThread thread = new SearchThread(window,player.getNowMass(),SearchThread.searchTime+againtime);
			thread.setMass(player.getNowMass());
			Window.japan.getCoordinates(player.getNowMass()).open(0);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();

			WaitThread wt = new WaitThread(2);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
		}while(Window.count==500 && againtime<1000);
		if(Window.count==500) System.out.println("探索失敗");
	}

	//目的地までの最短距離と最短ルートを格納
	public static synchronized void setSearchResult(int count, ArrayList<Coordinates> trajectory) {
		trajectory.remove(0);
		if(Window.count>=count) {
			Window.count=count;
			if(!nearestTrajectoryList.containsKey(count)) {
				nearestTrajectoryList.put(count,new ArrayList<ArrayList<Coordinates>>());
			}
			nearestTrajectoryList.get(count).add(trajectory);
		}
	}

	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static void searchShortestRouteSelectPlayer(Window window,Player selectedPlayer) {
		//再探索は10回まで(1回で出てほしい…)
		int againtime=0;
		do{
			Window.japan.allClose();
			//Threadを立ち上げる
			OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread(window,selectedPlayer,OnlyDistanceSearchThread.searchTime+againtime);
			thread.setMass(selectedPlayer.getNowMass());
			Window.japan.getCoordinates(selectedPlayer.getNowMass()).open(0);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();

			WaitThread wt = new WaitThread(2,againtime);
			wt.start();
			try {
				wt.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			againtime+=100;
			System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
		}while(Window.count==500 && againtime<1000);
		if(Window.count==500) System.out.println("探索失敗");
	}

	//目的地までの最短距離を計算し、最短ルートを取得(指定したプレイヤーの最短距離の探索)
	public static void searchShortestRouteAllPlayers(Window window,Map<Integer,Player> players) {
		//再探索は10回まで(1回で出てほしい…)
		for(Player selectedPlayer:players.values()) {
			int againtime=0;
			do{
				Window.japan.allClose();
				//Threadを立ち上げる
				OnlyDistanceSearchThread thread = new OnlyDistanceSearchThread(window,selectedPlayer,OnlyDistanceSearchThread.searchTime+againtime);
				thread.setMass(selectedPlayer.getNowMass());
				Window.japan.getCoordinates(selectedPlayer.getNowMass()).open(0);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.start();

				WaitThread wt = new WaitThread(2,againtime);
				wt.start();
				try {
					wt.join();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				againtime+=100;
				System.out.println("again:"+(againtime/100)+"     id:"+thread.getId());
			}while(Window.count==500 && againtime<1000);
			if(Window.count==500) System.out.println("探索失敗");
		}
	}
}
