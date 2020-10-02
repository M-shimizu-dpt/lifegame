/*
 * マップの情報を管理するクラス
 * マップ情報を取得したり、stationやcoordinates等を用いてマップを構築する処理を記述
 */

package lifegame.game.object.map.information;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lifegame.game.event.ContainsEvent;

public class PrivateJapan {
	private ArrayList<Station> stations = new ArrayList<Station>();//駅一覧
	private ArrayList<Coordinates> blue = new ArrayList<Coordinates>();//青マスの座標一覧
	private ArrayList<Coordinates> red = new ArrayList<Coordinates>();//赤マスの座標一覧
	private ArrayList<Coordinates> yellow = new ArrayList<Coordinates>();//黄マスの座標一覧
	private ArrayList<Coordinates> shop = new ArrayList<Coordinates>();//カード屋の座標一覧
	private Map<Coordinates,ArrayList<Boolean>> railBoolMapping = new HashMap<Coordinates,ArrayList<Boolean>>();//移動可能方向
	private Map<Coordinates,ArrayList<Coordinates>> railMapping = new HashMap<Coordinates,ArrayList<Coordinates>>();//移動可能座標
	private Station goal = new Station("",new Coordinates());//目的地の要素番号
	public ArrayList<String> alreadys = new ArrayList<String>();//そのターンに購入した物件リスト(連続購入を防ぐため)

	public PrivateJapan() {

	}

	public void init() {
		//近畿の駅
		stations.add(new Station("赤穂",new Coordinates(1,8)));
		stations.add(new Station("姫路",new Coordinates(2,7)));
		stations.add(new Station("明石",new Coordinates(3,9)));
		stations.add(new Station("淡路島",new Coordinates(3,10)));
		stations.add(new Station("城崎",new Coordinates(4,1)));
		stations.add(new Station("福知山",new Coordinates(4,4)));
		stations.add(new Station("三田",new Coordinates(4,8)));
		stations.add(new Station("神戸",new Coordinates(4,9)));
		stations.add(new Station("吹田",new Coordinates(7,7)));
		stations.add(new Station("出石",new Coordinates(5,2)));
		stations.add(new Station("天保山",new Coordinates(5,10)));
		stations.add(new Station("堺",new Coordinates(5,12)));
		stations.add(new Station("岸和田",new Coordinates(5,13)));
		stations.add(new Station("和歌山",new Coordinates(5,14)));
		stations.add(new Station("御坊",new Coordinates(5,15)));
		stations.add(new Station("白浜",new Coordinates(5,16)));
		stations.add(new Station("大阪",new Coordinates(6,9)));
		stations.add(new Station("なんば",new Coordinates(6,10)));
		stations.add(new Station("天王寺",new Coordinates(6,11)));
		stations.add(new Station("舞鶴",new Coordinates(7,1)));
		stations.add(new Station("北浜",new Coordinates(7,10)));
		stations.add(new Station("京橋",new Coordinates(8,10)));
		stations.add(new Station("鶴橋",new Coordinates(8,11)));
		stations.add(new Station("嵐山",new Coordinates(9,6)));
		stations.add(new Station("門真",new Coordinates(9,9)));
		stations.add(new Station("五條",new Coordinates(9,13)));
		stations.add(new Station("京都",new Coordinates(10,6)));
		stations.add(new Station("橿原",new Coordinates(10,12)));
		stations.add(new Station("祇園",new Coordinates(11,6)));
		stations.add(new Station("奈良",new Coordinates(11,11)));
		stations.add(new Station("新宮",new Coordinates(11,14)));
		stations.add(new Station("大津",new Coordinates(12,5)));
		stations.add(new Station("伊賀",new Coordinates(12,10)));
		stations.add(new Station("長浜",new Coordinates(13,2)));
		stations.add(new Station("彦根",new Coordinates(13,4)));
		stations.add(new Station("近江八幡",new Coordinates(13,5)));
		stations.add(new Station("四日市",new Coordinates(14,9)));
		stations.add(new Station("津",new Coordinates(14,11)));
		stations.add(new Station("松阪",new Coordinates(14,12)));
		stations.add(new Station("伊勢",new Coordinates(14,13)));
		stations.add(new Station("鳥羽",new Coordinates(15,13)));

		railBoolMapping.put(stations.get(0).getCoordinates(),getBoolList(true,false,false,true));
		railBoolMapping.put(stations.get(1).getCoordinates(),getBoolList(false,true,true,true));
		railBoolMapping.put(stations.get(2).getCoordinates(),getBoolList(true,true,false,true));
		railBoolMapping.put(stations.get(3).getCoordinates(),getBoolList(true,false,false,false));
		railBoolMapping.put(stations.get(4).getCoordinates(),getBoolList(false,true,true,false));
		railBoolMapping.put(stations.get(5).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(6).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(7).getCoordinates(),getBoolList(false,false,true,true));
		railBoolMapping.put(stations.get(8).getCoordinates(),getBoolList(false,true,false,true));
		railBoolMapping.put(stations.get(9).getCoordinates(),getBoolList(false,false,true,false));
		railBoolMapping.put(stations.get(10).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(11).getCoordinates(),getBoolList(false,true,false,true));
		railBoolMapping.put(stations.get(12).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(13).getCoordinates(),getBoolList(true,true,false,true));
		railBoolMapping.put(stations.get(14).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(15).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(16).getCoordinates(),getBoolList(true,true,true,true));
		railBoolMapping.put(stations.get(17).getCoordinates(),getBoolList(true,true,false,true));
		railBoolMapping.put(stations.get(18).getCoordinates(),getBoolList(true,true,true,true));
		railBoolMapping.put(stations.get(19).getCoordinates(),getBoolList(false,true,true,true));
		railBoolMapping.put(stations.get(20).getCoordinates(),getBoolList(false,false,true,true));
		railBoolMapping.put(stations.get(21).getCoordinates(),getBoolList(true,true,true,true));
		railBoolMapping.put(stations.get(22).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(23).getCoordinates(),getBoolList(true,false,false,true));
		railBoolMapping.put(stations.get(24).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(25).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(26).getCoordinates(),getBoolList(true,true,true,true));
		railBoolMapping.put(stations.get(27).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(28).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(29).getCoordinates(),getBoolList(false,false,true,false));
		railBoolMapping.put(stations.get(30).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(31).getCoordinates(),getBoolList(false,false,true,true));
		railBoolMapping.put(stations.get(32).getCoordinates(),getBoolList(false,false,true,true));
		railBoolMapping.put(stations.get(33).getCoordinates(),getBoolList(false,true,true,false));
		railBoolMapping.put(stations.get(34).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(35).getCoordinates(),getBoolList(true,false,true,false));
		railBoolMapping.put(stations.get(36).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(37).getCoordinates(),getBoolList(true,true,true,false));
		railBoolMapping.put(stations.get(38).getCoordinates(),getBoolList(true,true,false,false));
		railBoolMapping.put(stations.get(39).getCoordinates(),getBoolList(true,false,false,true));
		railBoolMapping.put(stations.get(40).getCoordinates(),getBoolList(false,false,true,false));

		//青マス
		blue.add(new Coordinates(5,9));
		blue.add(new Coordinates(7,8));
		blue.add(new Coordinates(7,9));
		blue.add(new Coordinates(7,13));
		blue.add(new Coordinates(8,9));
		blue.add(new Coordinates(12,2));
		blue.add(new Coordinates(12,11));
		blue.add(new Coordinates(13,11));
		blue.add(new Coordinates(6,16));
		blue.add(new Coordinates(6,17));
		blue.add(new Coordinates(9,15));
		blue.add(new Coordinates(10,15));
		blue.add(new Coordinates(10,2));
		blue.add(new Coordinates(2,1));
		blue.add(new Coordinates(2,2));
		blue.add(new Coordinates(1,4));
		blue.add(new Coordinates(3,3));
		blue.add(new Coordinates(3,4));
		blue.add(new Coordinates(2,5));
		blue.add(new Coordinates(1,5));
		blue.add(new Coordinates(11,1));
		blue.add(new Coordinates(12,1));
		blue.add(new Coordinates(14,3));

		railBoolMapping.put(blue.get(0),getBoolList(false,true,true,true));
		railBoolMapping.put(blue.get(1),getBoolList(true,false,true,false));
		railBoolMapping.put(blue.get(2),getBoolList(false,false,true,true));
		railBoolMapping.put(blue.get(3),getBoolList(false,true,false,true));
		railBoolMapping.put(blue.get(4),getBoolList(false,true,true,false));
		railBoolMapping.put(blue.get(5),getBoolList(true,false,true,true));
		railBoolMapping.put(blue.get(6),getBoolList(false,true,false,true));
		railBoolMapping.put(blue.get(7),getBoolList(true,false,true,true));
		railBoolMapping.put(blue.get(8),getBoolList(false,true,false,true));
		railBoolMapping.put(blue.get(9),getBoolList(true,false,true,false));
		railBoolMapping.put(blue.get(10),getBoolList(false,false,true,true));
		railBoolMapping.put(blue.get(11),getBoolList(true,false,true,false));
		railBoolMapping.put(blue.get(12),getBoolList(false,true,false,true));
		railBoolMapping.put(blue.get(13),getBoolList(false,true,false,true));
		railBoolMapping.put(blue.get(14),getBoolList(true,true,true,false));
		railBoolMapping.put(blue.get(15),getBoolList(true,true,false,false));
		railBoolMapping.put(blue.get(16),getBoolList(false,true,true,false));
		railBoolMapping.put(blue.get(17),getBoolList(true,false,true,false));
		railBoolMapping.put(blue.get(18),getBoolList(true,false,true,false));
		railBoolMapping.put(blue.get(19),getBoolList(true,true,false,true));
		railBoolMapping.put(blue.get(20),getBoolList(false,false,true,true));
		railBoolMapping.put(blue.get(21),getBoolList(false,true,true,true));
		railBoolMapping.put(blue.get(22),getBoolList(false,true,true,false));

		//赤マス
		red.add(new Coordinates(1,7));
		red.add(new Coordinates(2,8));
		red.add(new Coordinates(3,1));
		red.add(new Coordinates(3,7));
		red.add(new Coordinates(4,6));
		red.add(new Coordinates(5,6));
		red.add(new Coordinates(5,17));
		red.add(new Coordinates(6,6));
		red.add(new Coordinates(7,3));
		red.add(new Coordinates(7,14));
		red.add(new Coordinates(8,5));
		red.add(new Coordinates(8,7));
		red.add(new Coordinates(8,13));
		red.add(new Coordinates(8,15));
		red.add(new Coordinates(8,16));
		red.add(new Coordinates(9,5));
		red.add(new Coordinates(9,10));
		red.add(new Coordinates(10,3));
		red.add(new Coordinates(10,5));
		red.add(new Coordinates(10,9));
		red.add(new Coordinates(10,11));
		red.add(new Coordinates(10,14));
		red.add(new Coordinates(11,5));
		red.add(new Coordinates(11,10));
		red.add(new Coordinates(11,13));
		red.add(new Coordinates(12,13));
		red.add(new Coordinates(13,3));
		red.add(new Coordinates(14,7));
		red.add(new Coordinates(14,8));
		red.add(new Coordinates(2,3));
		red.add(new Coordinates(2,4));
		red.add(new Coordinates(10,1));
		red.add(new Coordinates(14,4));

		railBoolMapping.put(red.get(0),getBoolList(true,true,false,true));
		railBoolMapping.put(red.get(1),getBoolList(true,false,true,false));
		railBoolMapping.put(red.get(2),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(3),getBoolList(true,true,true,false));
		railBoolMapping.put(red.get(4),getBoolList(true,true,false,true));
		railBoolMapping.put(red.get(5),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(6),getBoolList(true,false,false,true));
		railBoolMapping.put(red.get(7),getBoolList(true,false,true,false));
		railBoolMapping.put(red.get(8),getBoolList(true,false,true,true));
		railBoolMapping.put(red.get(9),getBoolList(true,false,true,false));
		railBoolMapping.put(red.get(10),getBoolList(true,false,false,true));
		railBoolMapping.put(red.get(11),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(12),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(13),getBoolList(false,true,false,true));
		railBoolMapping.put(red.get(14),getBoolList(true,false,true,false));
		railBoolMapping.put(red.get(15),getBoolList(false,true,true,false));
		railBoolMapping.put(red.get(16),getBoolList(true,false,true,true));
		railBoolMapping.put(red.get(17),getBoolList(true,true,false,false));
		railBoolMapping.put(red.get(18),getBoolList(true,true,false,false));
		railBoolMapping.put(red.get(19),getBoolList(true,true,false,false));
		railBoolMapping.put(red.get(20),getBoolList(true,true,false,true));
		railBoolMapping.put(red.get(21),getBoolList(false,true,false,true));
		railBoolMapping.put(red.get(22),getBoolList(false,true,false,true));
		railBoolMapping.put(red.get(23),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(24),getBoolList(false,true,false,true));
		railBoolMapping.put(red.get(25),getBoolList(true,false,true,false));
		railBoolMapping.put(red.get(26),getBoolList(true,true,false,true));
		railBoolMapping.put(red.get(27),getBoolList(true,true,false,false));
		railBoolMapping.put(red.get(28),getBoolList(true,true,false,false));
		railBoolMapping.put(red.get(29),getBoolList(true,false,true,true));
		railBoolMapping.put(red.get(30),getBoolList(false,true,false,true));
		railBoolMapping.put(red.get(31),getBoolList(false,false,true,true));
		railBoolMapping.put(red.get(32),getBoolList(true,true,false,false));

		//黄マス
		yellow.add(new Coordinates(3,8));
		yellow.add(new Coordinates(4,2));
		yellow.add(new Coordinates(4,3));
		yellow.add(new Coordinates(4,5));
		yellow.add(new Coordinates(4,7));
		yellow.add(new Coordinates(5,11));
		yellow.add(new Coordinates(6,1));
		yellow.add(new Coordinates(6,3));
		yellow.add(new Coordinates(6,4));
		yellow.add(new Coordinates(6,5));
		yellow.add(new Coordinates(6,8));
		yellow.add(new Coordinates(6,12));
		yellow.add(new Coordinates(6,14));
		yellow.add(new Coordinates(8,1));
		yellow.add(new Coordinates(8,3));
		yellow.add(new Coordinates(8,4));
		yellow.add(new Coordinates(9,1));
		yellow.add(new Coordinates(9,7));
		yellow.add(new Coordinates(9,8));
		yellow.add(new Coordinates(9,12));
		yellow.add(new Coordinates(10,7));
		yellow.add(new Coordinates(10,8));
		yellow.add(new Coordinates(10,10));
		yellow.add(new Coordinates(13,10));
		yellow.add(new Coordinates(14,6));
		yellow.add(new Coordinates(14,10));
		yellow.add(new Coordinates(1,2));
		yellow.add(new Coordinates(1,3));

		railBoolMapping.put(yellow.get(0),getBoolList(true,true,false,true));
		railBoolMapping.put(yellow.get(1),getBoolList(true,true,false,true));
		railBoolMapping.put(yellow.get(2),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(3),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(4),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(5),getBoolList(true,false,false,true));
		railBoolMapping.put(yellow.get(6),getBoolList(false,false,false,true));
		railBoolMapping.put(yellow.get(7),getBoolList(false,true,false,true));
		railBoolMapping.put(yellow.get(8),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(9),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(10),getBoolList(false,true,false,true));
		railBoolMapping.put(yellow.get(11),getBoolList(true,false,true,false));
		railBoolMapping.put(yellow.get(12),getBoolList(false,false,true,true));
		railBoolMapping.put(yellow.get(13),getBoolList(false,false,true,true));//8,1
		railBoolMapping.put(yellow.get(14),getBoolList(false,true,true,true));
		railBoolMapping.put(yellow.get(15),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(16),getBoolList(false,false,true,true));//9,1
		railBoolMapping.put(yellow.get(17),getBoolList(false,true,true,true));
		railBoolMapping.put(yellow.get(18),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(19),getBoolList(false,true,false,true));
		railBoolMapping.put(yellow.get(20),getBoolList(true,true,true,false));
		railBoolMapping.put(yellow.get(21),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(22),getBoolList(true,true,true,true));//10,10
		railBoolMapping.put(yellow.get(23),getBoolList(false,true,true,true));
		railBoolMapping.put(yellow.get(24),getBoolList(true,true,false,false));
		railBoolMapping.put(yellow.get(25),getBoolList(true,true,true,false));
		railBoolMapping.put(yellow.get(26),getBoolList(false,false,false,true));
		railBoolMapping.put(yellow.get(27),getBoolList(false,true,false,true));

		//店
		shop.add(new Coordinates(13,1));
		shop.add(new Coordinates(9,3));
		shop.add(new Coordinates(14,5));
		shop.add(new Coordinates(7,16));
		shop.add(new Coordinates(3,6));

		railBoolMapping.put(shop.get(0),getBoolList(false,false,true,false));
		railBoolMapping.put(shop.get(1),getBoolList(false,false,true,false));
		railBoolMapping.put(shop.get(2),getBoolList(true,true,false,false));
		railBoolMapping.put(shop.get(3),getBoolList(false,false,true,true));
		railBoolMapping.put(shop.get(4),getBoolList(false,true,false,false));

		//リンク作成
		for(Coordinates from : getAllCoordinates()) {
			for(Coordinates to : getAllCoordinates()) {
				int x = from.getX()-to.getX();
				int y = from.getY()-to.getY();
				if(!((x>=-2 && x<=2) && (y>=-2 && y<=2)))continue;//処理数を減らす
				if(ContainsEvent.isStation(from)) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(stations.get(getIndexOfStation(from)).getCoordinates()).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(stations.get(getIndexOfStation(from)).getCoordinates()).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(stations.get(getIndexOfStation(from)).getCoordinates()).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(stations.get(getIndexOfStation(from)).getCoordinates()).get(0))) {
						if(x==2) {
							if(!ContainsEvent.isMass(from.getX()-1,from.getY())) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMass(from.getX()+1,from.getY())) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()-1)) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()+1)) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else {
							stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
						}
					}
					railMapping.put(stations.get(getIndexOfStation(from)).getCoordinates(),stations.get(getIndexOfStation(from)).getCoordinates().getLinks());
				}
				if(ContainsEvent.isBlue(from)) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(blue.get(getIndexOfBlue(from))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(blue.get(getIndexOfBlue(from))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(blue.get(getIndexOfBlue(from))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(blue.get(getIndexOfBlue(from))).get(0))) {
						if(x==2) {
							if(!ContainsEvent.isMass(from.getX()-1,from.getY())) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMass(from.getX()+1,from.getY())) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()-1)) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()+1)) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else {
							blue.get(getIndexOfBlue(from)).addLinks(to);
						}
					}
					railMapping.put(blue.get(getIndexOfBlue(from)),blue.get(getIndexOfBlue(from)).getLinks());
				}
				if(ContainsEvent.isRed(from)) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(red.get(getIndexOfRed(from))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(red.get(getIndexOfRed(from))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(red.get(getIndexOfRed(from))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(red.get(getIndexOfRed(from))).get(0))) {
						if(x==2) {
							if(!ContainsEvent.isMass(from.getX()-1,from.getY())) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMass(from.getX()+1,from.getY())) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()-1)) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()+1)) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else {
							red.get(getIndexOfRed(from)).addLinks(to);
						}
					}
					railMapping.put(red.get(getIndexOfRed(from)),red.get(getIndexOfRed(from)).getLinks());
				}
				if(ContainsEvent.isYellow(from)) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(yellow.get(getIndexOfYellow(from))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(yellow.get(getIndexOfYellow(from))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(yellow.get(getIndexOfYellow(from))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(yellow.get(getIndexOfYellow(from))).get(0))) {
						if(x==2) {
							if(!ContainsEvent.isMass(from.getX()-1,from.getY())) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMass(from.getX()+1,from.getY())) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()-1)) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()+1)) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else {
							yellow.get(getIndexOfYellow(from)).addLinks(to);
						}
					}
					railMapping.put(yellow.get(getIndexOfYellow(from)),yellow.get(getIndexOfYellow(from)).getLinks());
				}
				if(ContainsEvent.isShop(from)) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(shop.get(getIndexOfShop(from))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(shop.get(getIndexOfShop(from))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(shop.get(getIndexOfShop(from))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(shop.get(getIndexOfShop(from))).get(0))) {
						if(x==2) {
							if(!ContainsEvent.isMass(from.getX()-1,from.getY())) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMass(from.getX()+1,from.getY())) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()-1)) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMass(from.getX(),from.getY()+1)) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else {
							shop.get(getIndexOfShop(from)).addLinks(to);
						}
					}
					railMapping.put(shop.get(getIndexOfShop(from)),shop.get(getIndexOfShop(from)).getLinks());
				}
			}
		}
	}

	//ゴールの座標を取得
	public Coordinates getGoalCoor() {
		return goal.getCoordinates();
	}

	public void setGoal(Station goal) {
		this.goal=goal;
	}

	public Station getGoal() {
		return goal;
	}

	//要素番号で指定した駅の座標を取得
	public Coordinates getStationCoor(int index) {
		Coordinates staCoor = stations.get(index).getCoordinates();
		return staCoor;
	}

	//駅の座標一覧を取得
	public ArrayList<Coordinates> getStationCoorList(){
		ArrayList<Coordinates> result = new ArrayList<Coordinates>();
		for(Station s:stations) {
			result.add(s.getCoordinates());
		}
		return result;
	}

	//全てのマス座標を取得
	public ArrayList<Coordinates> getAllCoordinates(){
		ArrayList<Coordinates> list = new ArrayList<Coordinates>();
		list.addAll(getStationCoorList());
		list.addAll(blue);
		list.addAll(red);
		list.addAll(yellow);
		list.addAll(shop);
		Collections.sort(list,new Comparator<Coordinates>() {
        	public int compare(Coordinates cost1,Coordinates cost2) {
				return Integer.compare(cost1.getY(), cost2.getY());
			}
        });
		Collections.sort(list,new Comparator<Coordinates>() {
        	public int compare(Coordinates cost1,Coordinates cost2) {
				return Integer.compare(cost1.getX(), cost2.getX());
			}
        });
		return list;
	}

	//指定の座標のCoordinatesインスタンスを取得
	public synchronized Coordinates getCoordinates(int x,int y) {
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(ContainsEvent.coor(list.get(i),x,y)) {
				return list.get(i);
			}
		}

		return null;
	}
	public synchronized Coordinates getCoordinates(Coordinates coor) {
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(ContainsEvent.coor(list.get(i),coor)) {
				return list.get(i);
			}
		}

		return null;
	}

	//指定の座標の駅の配列番号を取得
	public int getIndexOfStation(int x,int y){
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list), x, y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public int getIndexOfStation(Coordinates coor){
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の青マスの配列番号を取得
	public int getIndexOfBlue(int x,int y){
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public int getIndexOfBlue(Coordinates coor){
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の赤マスの配列番号を取得
	public int getIndexOfRed(int x,int y){
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public int getIndexOfRed(Coordinates coor){
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の黄マスの配列番号を取得
	public int getIndexOfYellow(int x,int y){
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public int getIndexOfYellow(Coordinates coor){
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の店マスの配列番号を取得
	public int getIndexOfShop(int x,int y){
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public int getIndexOfShop(Coordinates coor){
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定したTFをリストにして取得
	private ArrayList<Boolean> getBoolList(Boolean top,Boolean bottom,Boolean left,Boolean right){
		ArrayList<Boolean> rail = new ArrayList<Boolean>();
		rail.add(left);
		rail.add(right);
		rail.add(top);
		rail.add(bottom);
		return rail;
	}

	//指定した座標から移動可能な方角一覧を取得
	public ArrayList<Boolean> getVector(int x,int y,int size){
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list), x/size, y/size)) {//駅の座標が来たら
				return railBoolMapping.get(stations.get(list).getCoordinates());
			}
		}
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),x/size,y/size)) {
				return railBoolMapping.get(blue.get(list));
			}
		}
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),x/size,y/size)) {
				return railBoolMapping.get(red.get(list));
			}
		}
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),x/size,y/size)) {
				return railBoolMapping.get(yellow.get(list));
			}
		}
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),x/size,y/size)) {
				return railBoolMapping.get(shop.get(list));
			}
		}

		return null;
	}
	public ArrayList<Boolean> getVector(Coordinates coor,int size){
		int x=coor.getX();
		int y=coor.getY();
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list), x/size, y/size)) {//駅の座標が来たら
				return railBoolMapping.get(stations.get(list).getCoordinates());
			}
		}
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),x/size,y/size)) {
				return railBoolMapping.get(blue.get(list));
			}
		}
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),x/size,y/size)) {
				return railBoolMapping.get(red.get(list));
			}
		}
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),x/size,y/size)) {
				return railBoolMapping.get(yellow.get(list));
			}
		}
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),x/size,y/size)) {
				return railBoolMapping.get(shop.get(list));
			}
		}

		return null;
	}
	//指定した座標から移動可能な座標一覧を取得
	public ArrayList<Coordinates> getMovePossibles(int x,int y) {
		if(ContainsEvent.isStation(x,y)) {
			return railMapping.get(stations.get(getIndexOfStation(x,y)).getCoordinates());
		}else if(ContainsEvent.isBlue(x,y)) {
			return railMapping.get(blue.get(getIndexOfBlue(x,y)));
		}else if(ContainsEvent.isRed(x,y)) {
			return railMapping.get(red.get(getIndexOfRed(x,y)));
		}else if(ContainsEvent.isYellow(x,y)) {
			return railMapping.get(yellow.get(getIndexOfYellow(x,y)));
		}else if(ContainsEvent.isShop(x,y)) {
			return railMapping.get(shop.get(getIndexOfShop(x,y)));
		}else {

			return null;
		}
	}
	public ArrayList<Coordinates> getMovePossibles(Coordinates coor) {
		if(ContainsEvent.isStation(coor)) {
			return railMapping.get(stations.get(getIndexOfStation(coor)).getCoordinates());
		}else if(ContainsEvent.isBlue(coor)) {
			return railMapping.get(blue.get(getIndexOfBlue(coor)));
		}else if(ContainsEvent.isRed(coor)) {
			return railMapping.get(red.get(getIndexOfRed(coor)));
		}else if(ContainsEvent.isYellow(coor)) {
			return railMapping.get(yellow.get(getIndexOfYellow(coor)));
		}else if(ContainsEvent.isShop(coor)) {
			return railMapping.get(shop.get(getIndexOfShop(coor)));
		}else {

			return null;
		}
	}
}
