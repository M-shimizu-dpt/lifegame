package lifegame.game.event;

import java.util.Map;

import lifegame.game.map.information.Property;
import lifegame.game.object.Player;

public abstract class ClosingEvent {

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
			if(Player.maxProfit<profitList[i]) {
				Player.maxProfit=profitList[i];
			}
			if(Player.minProfit>profitList[i]) {
				Player.minProfit=profitList[i];
			}
		}
		Player.addProfitList(profitList);
	}

	//総資産を集計
	public static void aggregateAssets(Map<Integer, Player> players) {
		Integer assetsList[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			assetsList[i]+=players.get(i).getMoney();
			for(Property property : players.get(i).getPropertys()) {
				assetsList[i]+=property.getAmount();
			}
			if(Player.maxAssets<assetsList[i]) {
				Player.maxAssets=assetsList[i];
			}
			if(Player.minAssets>assetsList[i]) {
				Player.minAssets=assetsList[i];
			}
		}
		Player.addAssetsList(assetsList);
	}

}
