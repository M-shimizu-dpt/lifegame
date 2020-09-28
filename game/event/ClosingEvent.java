package lifegame.game.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import lifegame.game.object.Player;
import lifegame.game.object.map.information.Property;

public abstract class ClosingEvent {
	public static boolean closingEndFlag=false;//決算処理が終了するのを待つためのフラグ
	public static int maxProfit=100;//最高収益(グラフ作成用)
	public static int minProfit=0;//最低収益(グラフ作成用)
	public static int maxAssets=100;//最高資産(グラフ作成用)
	public static int minAssets=0;//最低資産(グラフ作成用)

	private static ArrayList<Integer[]> allProfitList = new ArrayList<Integer[]>();//各プレイヤーの総収益(過去も含む)
	private static ArrayList<Integer[]> allAssetsList = new ArrayList<Integer[]>();//各プレイヤーの総資産(過去も含む)
	private static ArrayList<Integer[]> rank = new ArrayList<>();
	
	public static ArrayList<Integer[]> getProfitList(){
		return ClosingEvent.allProfitList;
	}

	public static Integer[] getProfitList(int index){
		return ClosingEvent.allProfitList.get(index);
	}

	public static int getProfitListSize(){
		return ClosingEvent.allProfitList.size();
	}

	public static void addProfitList(Integer[] list){
		ClosingEvent.allProfitList.add(list);
	}

	public static void addAssetsList(Integer[] list){
		ClosingEvent.allAssetsList.add(list);
	}

	public static ArrayList<Integer[]> getAssetsList(){
		return ClosingEvent.allAssetsList;
	}

	public static Integer[] getAssetsList(int index){
		return ClosingEvent.allAssetsList.get(index);
	}

	public static int getAssetsListSize(){
		return ClosingEvent.allAssetsList.size();
	}

	public static Integer[] getRankList(int index){
		return ClosingEvent.rank.get(index);
	}

	/*
	 * 決算
	 */
	public static void closing(Map<Integer, Player> players) {
		addProfit(players);
		aggregateProfit(players);

	}

	//収益を加算
	private static void addProfit(Map<Integer, Player> players) {
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			players.get(i).addProfit();
		}
	}

	//収益を集計
	private static void aggregateProfit(Map<Integer, Player> players) {
		Integer profitList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			//全ての物件の所有者にその物件の収益を加算
			for(Property property:players.get(i).getPropertys()) {
				profitList[i]+=property.getProfit();
			}
			ClosingEvent.maxProfit=Math.max(ClosingEvent.maxProfit,profitList[i]);
			ClosingEvent.minProfit=Math.min(ClosingEvent.minProfit, profitList[i]);
		}
		ClosingEvent.addProfitList(profitList);
	}

	//総資産を集計
	public static void aggregateAssets(Map<Integer, Player> players) {
		Integer assetsList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			assetsList[i]+=players.get(i).getMoney();
			for(Property property : players.get(i).getPropertys()) {
				assetsList[i]+=property.getAmount();
			}
			ClosingEvent.maxAssets=Math.max(ClosingEvent.maxAssets,assetsList[i]);
			ClosingEvent.minAssets=Math.min(ClosingEvent.minAssets, assetsList[i]);
		}
		ClosingEvent.addAssetsList(assetsList);
	}
	
	//総資産で降順に並び替え、一番を返す
	public static String finish() {
		for(int i=0;i<4;i++) {
			rank.add(getAssetsList(i));
		}
		Collections.sort(rank, Collections.reverseOrder());
		for(int i=0;i<4;i++) {
			if(getRankList(0)==getAssetsList(i)) {
				return Player.players.get(i).getName();
			}
		}
		return null;
	}

	public static void closed() {
		closingEndFlag=true;
	}
}
