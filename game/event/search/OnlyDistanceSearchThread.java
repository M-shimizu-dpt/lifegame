package lifegame.game.event.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.Searcher;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;


public class OnlyDistanceSearchThread extends SearchThread{
	private Player player;

	//start
	public OnlyDistanceSearchThread(Player player) {
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
		OnlyDistanceSearchThread.initSearchTime();
	}

	//start
	public OnlyDistanceSearchThread(Player player,int searchTime) {
		super.start.setValue(player.getNowMass());
		this.player=player;
		player.initGoalDistance();
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
		OnlyDistanceSearchThread.initSearchTime(searchTime);
	}

	//continue
	public OnlyDistanceSearchThread(OnlyDistanceSearchThread original) {
		threadCopy(original);
	}

	@Override
	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		while(count<=Searcher.count && count <= 40 && System.currentTimeMillis()-Searcher.time<=searchTime) {
			synchronized(OnlyDistanceSearchThread.lock2) {
				if(ContainsEvent.isMinRange(Japan.getCoordinates(nowMass), getStart(), Japan.getGoalCoor())) {//最適な範囲でごたついているThreadの優先度が高くなる可能性があるので、openlistを用意して今までのコストと比較し自分がどれくらいのレベルに居るのかを考慮すると本当に最適なpriorityを指定することが可能になるはず(処理が長くなり各threadが消費するリソースが膨大になる可能性を考慮すべし)
					super.setPriority(Thread.MIN_PRIORITY);
				}else if(ContainsEvent.isNormRange(Japan.getCoordinates(nowMass), getStart(), Japan.getGoalCoor())){
					super.setPriority(Thread.NORM_PRIORITY);
				}else if(ContainsEvent.isMaxRange(Japan.getCoordinates(nowMass), getStart(), Japan.getGoalCoor())){
					super.setPriority(Thread.MAX_PRIORITY);
				}
			}
			Thread.yield();
			ArrayList<Coordinates> list = new ArrayList<Coordinates>();
			super.moveTrajectory.add(new Coordinates(nowMass));//移動履歴を追加
			if(ContainsEvent.isGoal(nowMass)){
				goal();
				break;
			}
			count++;
			ArrayList<Coordinates> can = new ArrayList<Coordinates>();
			synchronized(OnlyDistanceSearchThread.lock1) {
				can.addAll(Japan.getMovePossibles(super.nowMass));
			}
			for(Coordinates possibles : can) {//移動可能マスを取得
				boolean conti=false;
				for(Coordinates trajectory : super.moveTrajectory) {//既に通った場所を省く
					if(ContainsEvent.coor(trajectory, possibles)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				possibles.open(super.count);
				if(ContainsEvent.isBestRange(possibles,start)) {
					list.add(possibles);
				}
				possibles.close();
			}
			//open処理
			synchronized(OnlyDistanceSearchThread.lock2) {
				for(Coordinates coor:list) {//open処理
					Japan.getCoordinates(coor).open(super.count);//探索予定のマスをopenにする。(コストを計算し保持する。)
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

	protected void threadCopy(OnlyDistanceSearchThread original) {
		super.setCount(original.count);
		super.setStart(original.start);
		this.player=original.player;
		super.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(OnlyDistanceSearchThread.lock3) {
			if(ContainsEvent.goalDistance(this.player, super.count)==1) {
				this.player.setGoalDistance(super.count);
			}
		}
	}
}
