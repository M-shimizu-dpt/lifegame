package lifegame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchThreadModel extends Thread{
	protected ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	protected static final Object lock1 = new Object();
	protected static final Object lock2 = new Object();
	protected static final Object lock3 = new Object();
	protected static final Object lock4 = new Object();
	protected static int searchTime;
	protected int count=0;
	protected Coordinates nowMass=new Coordinates();
	protected Window window;
	protected ArrayList<Coordinates> goals = new ArrayList<Coordinates>();

	protected static void initSearchTime() {
		searchTime=500;
	}

	protected static void initSearchTime(int searchtime) {
		searchTime=searchtime;
	}

	protected synchronized void addGoal(Coordinates coor) {
		goals.add(coor);
	}

	protected void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	protected void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	protected void setWindow(Window window) {
		this.window=window;
	}

	protected void setCount(int count) {
		this.count = count;
	}

}

//現在の位置から指定した目的地までの最短距離と軌跡を取得
class SearchThread extends SearchThreadModel{
	protected Coordinates start = new Coordinates();

	public SearchThread() {

	}

	//start
	public SearchThread(Window window,Coordinates start) {
		this.setWindow(window);
		this.start.setValue(start);
		Window.time = System.currentTimeMillis();
		Window.count=500;
		SearchThread.initSearchTime();
	}

	//start
	public SearchThread(Window window,Coordinates start,int searchTime) {
		this.setWindow(window);
		this.start.setValue(start);
		Window.time = System.currentTimeMillis();
		Window.count=500;
		SearchThread.initSearchTime(searchTime);
	}

	//continue
	public SearchThread(SearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		while(count<=Window.count && count <= 40 && System.currentTimeMillis()-Window.time<=searchTime) {
			synchronized(SearchThread.lock2) {
				if(Window.japan.getCoordinates(nowMass).isMinRange(getStart(),Window.japan.getGoal())) {//最適な範囲でごたついているThreadの優先度が高くなる可能性があるので、openlistを用意して今までのコストと比較し自分がどれくらいのレベルに居るのかを考慮すると本当に最適なpriorityを指定することが可能になるはず(処理が長くなり各threadが消費するリソースが膨大になる可能性を考慮すべし)
					setPriority(Thread.MIN_PRIORITY);
				}else if(Window.japan.getCoordinates(nowMass).isNormRange(getStart(),Window.japan.getGoal())){
					setPriority(Thread.NORM_PRIORITY);
				}else if(Window.japan.getCoordinates(nowMass).isMaxRange(getStart(), Window.japan.getGoal())){
					setPriority(Thread.MAX_PRIORITY);
				}
			}
			Thread.yield();
			ArrayList<Coordinates> list = new ArrayList<Coordinates>();
			moveTrajectory.add(new Coordinates(nowMass));//移動履歴を追加
			if(nowMass.contains(Window.japan.getGoal())){
				goal();
				break;
			}
			count++;
			ArrayList<Coordinates> can = new ArrayList<Coordinates>();
			synchronized(SearchThread.lock1) {
				can.addAll(Window.japan.getMovePossibles(this.nowMass));
			}
			for(Coordinates possibles : can) {//移動可能マスを取得
				boolean conti=false;
				for(Coordinates trajectory : moveTrajectory) {//既に通った場所を省く
					if(trajectory.contains(possibles)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				possibles.open(count);
				if(possibles.getCost() <= possibles.getMaxCost(start,Window.japan.getGoal())) {
					list.add(possibles);
				}
				possibles.close();
			}
			//open処理
			synchronized(SearchThread.lock2) {
				for(Coordinates coor:list) {//open処理
					Window.japan.getCoordinates(coor).open(count);//探索予定のマスをopenにする。(コストを計算し保持する。)
				}
			}
			Collections.sort(list,new Comparator<Coordinates>() {
	        	public int compare(Coordinates coor1,Coordinates coor2) {
					return Integer.compare(coor1.getCost(), coor2.getCost());
				}
	        });
			boolean me=true;
			for(Coordinates coor:list) {//移動処理
				if(me) {
					setMass(coor);
					me=false;
				}else {
					//Threadを立ち上げ、移動する
					SearchThread thread = new SearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
		}
	}

	protected void setStart(Coordinates start) {
		this.start.setValue(start);
	}

	protected Coordinates getStart() {
		return this.start;
	}

	protected void threadCopy(SearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.setStart(original.start);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	protected void goal() {
		synchronized(SearchThread.lock3) {
			window.setSearchResult(count,moveTrajectory);
		}
	}
}

//現在の位置から指定した目的地までの最短距離を取得
class OnlyDistanceSearchThread extends SearchThread{
	private Player player;

	//start
	public OnlyDistanceSearchThread(Window window,Player player) {
		this.setWindow(window);
		this.start.setValue(player.getNowMass());
		this.player=player;
		Window.time = System.currentTimeMillis();
		Window.count=500;
		OnlyDistanceSearchThread.initSearchTime();
	}

	//start
	public OnlyDistanceSearchThread(Window window,Player player,int searchTime) {
		this.setWindow(window);
		this.start.setValue(player.getNowMass());
		this.player=player;
		Window.time = System.currentTimeMillis();
		Window.count=500;
		OnlyDistanceSearchThread.initSearchTime(searchTime);
	}

	//continue
	public OnlyDistanceSearchThread(OnlyDistanceSearchThread original) {
		threadCopy(original);
	}

	protected void threadCopy(OnlyDistanceSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.setStart(original.start);
		this.player=original.player;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	protected void goal() {
		synchronized(OnlyDistanceSearchThread.lock3) {
			window.setSearchResult(player,count);
		}
	}
}

//単一指定位置から複数の指定位置の経路の内、最短距離の指定位置を探索
class NearestSearchThread extends SearchThreadModel{
	protected static int nearestCount = 100;//目的地から移動可能マスまでの最短距離
	public NearestSearchThread() {

	}

	//start
	public NearestSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		NearestSearchThread.nearestCount=100;
		this.setWindow(window);
		NearestSearchThread.initSearchTime();
	}

	//start
	public NearestSearchThread(Window window,int searchTime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		NearestSearchThread.nearestCount=100;
		this.setWindow(window);
		NearestSearchThread.initSearchTime(searchTime);
	}

	//continue
	public NearestSearchThread(NearestSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		while(count<=NearestSearchThread.nearestCount && count<=35 && System.currentTimeMillis()-Window.time<searchTime) {
			next.setValue(0, 0);
			first=true;

			count++;
			synchronized(NearestSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));
			for(Coordinates goal:goals) {
				if(goal.contains(nowMass)){
					goal();
					break;
				}
			}
			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					if(moveTrajectory.get(j).contains(coor)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					NearestSearchThread thread = new NearestSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				this.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	private void goal() {
		synchronized(NearestSearchThread.lock2) {
			window.setNearestMass(nowMass,count);
		}
	}

	private void threadCopy(NearestSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
		this.goals.addAll(original.goals);
	}
}


//最寄り駅を探索するためのスレッド
class StationSearchThread extends SearchThreadModel{
	//start
	public StationSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime();
	}

	//start
	public StationSearchThread(Window window,int searchtime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime(searchtime);
	}

	//continue
	public StationSearchThread(StationSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;//現在地の近傍マス

		boolean first;//分岐検知用フラグ
		Coordinates next = new Coordinates();//次の移動マス格納用
		while(count<=Window.count && count<=10 && System.currentTimeMillis()-Window.time<StationSearchThread.searchTime) {
			//初期化
			next.setValue(0, 0);
			first=true;

			//終了
			synchronized(StationSearchThread.lock2) {
				if(Window.japan.containsStation(this.nowMass)) {
					goal();
					break;
				}
			}

			//情報の取得・更新
			super.count++;
			synchronized(StationSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			super.moveTrajectory.add(new Coordinates(this.nowMass));

			//移動
			for(Coordinates coor:list) {
				boolean conti=false;
				for(Coordinates trajectory:super.moveTrajectory) {//既に通った場所を省く
					if(trajectory.contains(coor)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					StationSearchThread thread = new StationSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				super.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	private void threadCopy(StationSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void goal() {
		synchronized(StationSearchThread.lock2) {
			this.window.setNearestStationResult(this.count, this.nowMass);
		}
	}

}

//最寄り店を探索するためのスレッド
class ShopSearchThread extends SearchThreadModel{
	//start
	public ShopSearchThread(Window window) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime();
	}
	//start
	public ShopSearchThread(Window window,int searchtime) {
		Window.time = System.currentTimeMillis();
		Window.count=100;
		super.setWindow(window);
		super.initSearchTime(searchtime);
	}
	//continue
	public ShopSearchThread(ShopSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		while(count<=Window.count && count<=20 && System.currentTimeMillis()-Window.time<ShopSearchThread.searchTime) {
			next.setValue(0, 0);
			first=true;
			synchronized(ShopSearchThread.lock2) {
				if(Window.japan.containsShop(this.nowMass)) {
					goal();
					break;
				}
			}

			count++;
			synchronized(ShopSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));

			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					if(moveTrajectory.get(j).contains(coor)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					//Threadを立ち上げる
					ShopSearchThread thread = new ShopSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				this.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	private void threadCopy(ShopSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void goal() {
		synchronized(ShopSearchThread.lock2) {
			window.setNearestShopResult(count, nowMass);
		}
	}

}

//残りの進めるマス数で移動した先に何があるのかを探索するためのスレッド
class MassSearchThread extends SearchThreadModel{
	//start
	public MassSearchThread(Window window,int count) {
		Window.time = System.currentTimeMillis();
		this.setWindow(window);
		this.setCount(count);
	}

	//continue
	public MassSearchThread(MassSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		ArrayList<Coordinates> list;

		boolean first;
		Coordinates next = new Coordinates();
		//while(true) {
		while(System.currentTimeMillis()-Window.time<MassSearchThread.searchTime) {
			next.setValue(0, 0);
			first=true;
			count--;
			synchronized(MassSearchThread.lock2) {
				moveTrajectory.add(Window.japan.getCoordinates(nowMass));
			}
			if(count < 0) {
				goal();
				break;
			}
			synchronized(MassSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}

			for(Coordinates coor:list) {
				//既に通った場所を省く
				if(moveTrajectory.size()>1) {
					if(moveTrajectory.get(moveTrajectory.size()-2).contains(coor)) {//来た道の場合
						continue;
					}
				}
				if(first) {
					next.setValue(coor);
					first=false;
				}else {
					MassSearchThread thread = new MassSearchThread(this);
					thread.setMass(coor);//移動
					thread.start();
				}
			}
			if(list.size()>0 && !first) {
				this.setMass(next);//移動
			}else {
				break;
			}
			Thread.yield();
		}
	}

	private void threadCopy(MassSearchThread original) {
		this.setWindow(original.window);
		this.setCount(original.count);
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void goal() {
		synchronized(MassSearchThread.lock2) {
			window.setCanMoveMassResult(nowMass, moveTrajectory);
		}
	}

}

