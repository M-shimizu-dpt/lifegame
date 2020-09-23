package lifegame.game.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lifegame.game.main.App;
import lifegame.game.map.information.Coordinates;
import lifegame.game.map.print.Window;
import lifegame.game.search.model.SearchThreadModel;

public class SearchThread extends SearchThreadModel{
	protected Coordinates start = new Coordinates();

	public SearchThread() {

	}

	//start
	public SearchThread(Window window,Coordinates start) {
		super.setWindow(window);
		this.start.setValue(start);
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
		SearchThread.initSearchTime();
	}

	//start
	public SearchThread(Window window,Coordinates start,int searchTime) {
		super.setWindow(window);
		this.start.setValue(start);
		Searcher.time = System.currentTimeMillis();
		Searcher.count=500;
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
		while(count<=Searcher.count && count <= 40 && System.currentTimeMillis()-Searcher.time<=searchTime) {
			synchronized(SearchThread.lock2) {
				if(App.japan.getCoordinates(nowMass).isMinRange(getStart(),App.japan.getGoal())) {//最適な範囲でごたついているThreadの優先度が高くなる可能性があるので、openlistを用意して今までのコストと比較し自分がどれくらいのレベルに居るのかを考慮すると本当に最適なpriorityを指定することが可能になるはず(処理が長くなり各threadが消費するリソースが膨大になる可能性を考慮すべし)
					super.setPriority(Thread.MIN_PRIORITY);
				}else if(App.japan.getCoordinates(nowMass).isNormRange(getStart(),App.japan.getGoal())){
					super.setPriority(Thread.NORM_PRIORITY);
				}else if(App.japan.getCoordinates(nowMass).isMaxRange(getStart(), App.japan.getGoal())){
					super.setPriority(Thread.MAX_PRIORITY);
				}
			}
			Thread.yield();
			ArrayList<Coordinates> list = new ArrayList<Coordinates>();
			super.moveTrajectory.add(new Coordinates(nowMass));//移動履歴を追加
			if(nowMass.contains(App.japan.getGoal())){
				goal();
				break;
			}
			count++;
			ArrayList<Coordinates> can = new ArrayList<Coordinates>();
			synchronized(SearchThread.lock1) {
				can.addAll(App.japan.getMovePossibles(super.nowMass));
			}
			for(Coordinates possibles : can) {//移動可能マスを取得
				boolean conti=false;
				for(Coordinates trajectory : super.moveTrajectory) {//既に通った場所を省く
					if(trajectory.contains(possibles)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				possibles.open(super.count);
				if(possibles.getCost() <= possibles.getMaxCost(start,App.japan.getGoal())) {
					list.add(possibles);
				}
				possibles.close();
			}
			//open処理
			synchronized(SearchThread.lock2) {
				for(Coordinates coor:list) {//open処理
					App.japan.getCoordinates(coor).open(super.count);//探索予定のマスをopenにする。(コストを計算し保持する。)
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
		super.setWindow(original.window);
		super.setCount(original.count);
		this.setStart(original.start);
		super.moveTrajectory.addAll(original.moveTrajectory);
	}

	@Override
	protected void goal() {
		synchronized(SearchThread.lock3) {
			Searcher.setSearchResult(super.count,super.moveTrajectory);
		}
	}
}
