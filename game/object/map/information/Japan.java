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
import lifegame.game.event.FrameEvent;
import lifegame.game.event.Searcher;

public abstract class Japan{
	private static ArrayList<Station> stations = new ArrayList<Station>();//駅一覧
	private static ArrayList<Coordinates> blue = new ArrayList<Coordinates>();//青マスの座標一覧
	private static ArrayList<Coordinates> red = new ArrayList<Coordinates>();//赤マスの座標一覧
	private static ArrayList<Coordinates> yellow = new ArrayList<Coordinates>();//黄マスの座標一覧
	private static ArrayList<Coordinates> shop = new ArrayList<Coordinates>();//カード屋の座標一覧
	private static Map<Coordinates,ArrayList<Boolean>> railBoolMapping = new HashMap<Coordinates,ArrayList<Boolean>>();//移動可能方向
	private static Map<Coordinates,ArrayList<Coordinates>> railMapping = new HashMap<Coordinates,ArrayList<Coordinates>>();//移動可能座標
	private static Station goal;//目的地
	private static Station saveGoal;//ゴール保存用
	public static ArrayList<String> alreadys = new ArrayList<String>();//そのターンに購入した物件リスト(連続購入を防ぐため)

	public static void init() {
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

		//赤穂
		stations.get(0).addProperty(new Property("塩饅頭屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(0)));
		stations.get(0).addProperty(new Property("忠臣蔵グッズ屋", 1000, 6, 1.0, 1.5, 2.0,stations.get(0)));
		stations.get(0).addProperty(new Property("布海苔養殖場", 30000, 4, 0.01, 0.02, 0.03,stations.get(0)));
		stations.get(0).addProperty(new Property("製塩工場", 70000, 1, 0.03, 0.04, 0.05,stations.get(0)));//3
		stations.get(0).addProperty(new Property("製塩工場", 70000, 1, 0.03, 0.04, 0.05,stations.get(0)));//3
		stations.get(0).addProperty(new Property("製塩工場", 70000, 1, 0.03, 0.04, 0.05,stations.get(0)));//3
		//姫路
		stations.get(1).addProperty(new Property("手延べそうめん屋", 1000, 1, 0.25, 0.3, 0.4,stations.get(1)));//2
		stations.get(1).addProperty(new Property("手延べそうめん屋", 1000, 1, 0.25, 0.3, 0.4,stations.get(1)));//2
		stations.get(1).addProperty(new Property("焼きアナゴ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(1)));//2
		stations.get(1).addProperty(new Property("焼きアナゴ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(1)));//2
		stations.get(1).addProperty(new Property("革製品工場", 10000, 4, 0.02, 0.03, 0.04,stations.get(1)));
		stations.get(1).addProperty(new Property("石油工場", 800000, 4, 0.01, 0.03, 0.05,stations.get(1)));
		stations.get(1).addProperty(new Property("化学工場", 1000000, 4, 0.01, 0.03, 0.05,stations.get(1)));
		stations.get(1).addProperty(new Property("製鉄所", 1400000, 4, 0.03, 0.04, 0.05,stations.get(1)));
		//明石
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(2)));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(2)));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(2)));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(2)));//4
		stations.get(2).addProperty(new Property("海鮮市場", 60000, 4, 0.02, 0.03, 0.04,stations.get(2)));
		//淡路島
		stations.get(3).addProperty(new Property("引き戻し屋", 500, 6, 1.0, 2.0, 3.0,stations.get(3)));
		stations.get(3).addProperty(new Property("玉ねぎ畑", 5000, 2, 0.08, 0.12, 0.16,stations.get(3)));//2
		stations.get(3).addProperty(new Property("玉ねぎ畑", 5000, 2, 0.08, 0.12, 0.16,stations.get(3)));//2
		stations.get(3).addProperty(new Property("お香工場", 10000, 5, 0.02, 0.04, 0.06,stations.get(3)));
		stations.get(3).addProperty(new Property("淡路瓦工場", 30000, 5, 0.01, 0.02, 0.03,stations.get(3)));
		//城崎
		stations.get(4).addProperty(new Property("湯上りジュース屋", 500, 1, 0.5, 0.7, 0.8,stations.get(4)));
		stations.get(4).addProperty(new Property("カニ寿司屋", 1000, 1, 0.25, 0.3, 0.5,stations.get(4)));
		stations.get(4).addProperty(new Property("麦わら細工工房", 3000, 3, 0.25, 0.3, 0.5,stations.get(4)));
		stations.get(4).addProperty(new Property("城崎温泉旅館", 100000, 3, 0.02, 0.03, 0.05,stations.get(4)));
		stations.get(4).addProperty(new Property("カニ割烹旅館", 200000, 3, 0.05, 0.06, 0.07,stations.get(4)));
		//福知山
		stations.get(5).addProperty(new Property("音頭せんべい屋", 1000, 1, 0.5, 1.0, 1.5,stations.get(5)));
		stations.get(5).addProperty(new Property("ブドウ園", 5000, 2, 0.05, 0.06, 0.07,stations.get(5)));
		stations.get(5).addProperty(new Property("タケノコ林", 8000, 2, 0.05, 0.06, 0.07,stations.get(5)));
		stations.get(5).addProperty(new Property("栗のテリーヌ屋", 20000, 1, 0.1, 0.15 ,0.2,stations.get(5)));
		stations.get(5).addProperty(new Property("痔の薬品工場", 500000, 4, 0.1, 0.15, 0.2,stations.get(5)));
		stations.get(5).addProperty(new Property("磁気テープ工場", 600000, 4, 0.03, 0.04, 0.05,stations.get(5)));
		stations.get(5).addProperty(new Property("ビタミン剤工場", 1000000, 4, 0.07, 0.1, 0.13,stations.get(5)));
		//三田
		stations.get(6).addProperty(new Property("すき焼きパン屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(6)));
		stations.get(6).addProperty(new Property("丹波栗屋", 10000, 1, 0.02, 0.03, 0.04,stations.get(6)));//2
		stations.get(6).addProperty(new Property("丹波栗屋", 10000, 1, 0.02, 0.03, 0.04,stations.get(6)));//2
		stations.get(6).addProperty(new Property("三田牛ステーキ屋", 30000, 1, 0.03, 0.04, 0.05,stations.get(6)));
		stations.get(6).addProperty(new Property("丹波松茸屋", 50000, 1, 0.07, 0.08, 0.09,stations.get(6)));
		//神戸
		stations.get(7).addProperty(new Property("そばめし屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(7)));//2
		stations.get(7).addProperty(new Property("そばめし屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(7)));//2
		stations.get(7).addProperty(new Property("フランスパン屋", 10000, 1, 0.03, 0.04, 0.05,stations.get(7)));//2
		stations.get(7).addProperty(new Property("フランスパン屋", 10000, 1, 0.03, 0.04, 0.05,stations.get(7)));//2
		stations.get(7).addProperty(new Property("中華飯店", 100000, 1, 0.02, 0.03, 0.05,stations.get(7)));
		stations.get(7).addProperty(new Property("ステーキハウス", 150000, 1, 0.03, 0.05, 0.07,stations.get(7)));
		stations.get(7).addProperty(new Property("ハーバーパーク", 1300000, 3, 0.02, 0.03, 0.04,stations.get(7)));
		stations.get(7).addProperty(new Property("南京町中華街", 2000000, 1, 0.02, 0.03, 0.04,stations.get(7)));
		//吹田
		stations.get(8).addProperty(new Property("くずきり工場", 20000, 1, 0.03, 0.04, 0.05,stations.get(8)));
		stations.get(8).addProperty(new Property("げりぴー工場", 50000, 5, 0.02, 0.03, 0.04,stations.get(8)));
		stations.get(8).addProperty(new Property("てっぺん化粧品工場", 60000, 5, 0.02, 0.03, 0.04,stations.get(8)));
		stations.get(8).addProperty(new Property("即席めん工場", 600000, 1, 0.07, 0.08, 0.09,stations.get(8)));
		stations.get(8).addProperty(new Property("ビール工場", 1000000, 1, 0.04, 0.05, 0.06,stations.get(8)));
		stations.get(8).addProperty(new Property("ぞうきん工場", 1200000, 5, 0.02, 0.03, 0.04,stations.get(8)));
		stations.get(8).addProperty(new Property("目薬メーカー", 1500000, 5, 0.04, 0.05, 0.06,stations.get(8)));
		//出石
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(9)));//3
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(9)));//3
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(9)));//3
		stations.get(9).addProperty(new Property("露店トマト店", 1000, 1, 0.8, 1.0, 1.5,stations.get(9)));
		stations.get(9).addProperty(new Property("出石白磁工房", 10000, 6, 0.01, 0.02, 0.03,stations.get(9)));
		//天保山
		stations.get(10).addProperty(new Property("ロックカフェ", 20000, 1, 0.03, 0.04, 0.05,stations.get(10)));
		stations.get(10).addProperty(new Property("大観覧車", 400000, 3, 0.03, 0.04, 0.05,stations.get(10)));
		stations.get(10).addProperty(new Property("アウトレットモール", 1000000, 6, 0.04, 0.05, 0.06,stations.get(10)));
		stations.get(10).addProperty(new Property("ジンベイザメ水族館", 6000000, 3, 0.1, 0.2, 0.3,stations.get(10)));
		stations.get(10).addProperty(new Property("映画ランドジャパン", 35000000, 3, 0.01, 0.02, 0.03,stations.get(10)));
		//堺
		stations.get(11).addProperty(new Property("かすうどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(11)));//2
		stations.get(11).addProperty(new Property("かすうどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(11)));//2
		stations.get(11).addProperty(new Property("くるみ餅屋", 1000, 1, 1.0, 2.0, 3.0,stations.get(11)));
		stations.get(11).addProperty(new Property("刃物工場", 20000, 5, 0.03, 0.04, 0.05,stations.get(11)));
		stations.get(11).addProperty(new Property("クラッカー菓子工場", 20000, 1, 0.07, 0.08, 0.09,stations.get(11)));
		stations.get(11).addProperty(new Property("回転寿司チェーン", 200000, 1, 0.07, 0.08, 0.09,stations.get(11)));
		stations.get(11).addProperty(new Property("引越センター", 370000, 6, 0.07, 0.08, 0.09,stations.get(11)));
		//岸和田
		stations.get(12).addProperty(new Property("だんじりグッズ屋", 1000, 3, 1.0, 2.0, 3.0,stations.get(12)));
		stations.get(12).addProperty(new Property("玉ねぎ畑", 3000, 2, 0.05, 0.06, 0.07,stations.get(12)));
		stations.get(12).addProperty(new Property("水ナス畑", 5000, 2, 0.1, 0.11, 0.12,stations.get(12)));//2
		stations.get(12).addProperty(new Property("水ナス畑", 5000, 2, 0.1, 0.11, 0.12,stations.get(12)));//2
		stations.get(12).addProperty(new Property("顕微鏡ガラス工場", 200000, 5, 0.02, 0.03, 0.04,stations.get(12)));
		//和歌山
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 2, 0.05, 0.08, 0.1,stations.get(13)));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 2, 0.05, 0.08, 0.1,stations.get(13)));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 2, 0.05, 0.08, 0.1,stations.get(13)));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 2, 0.05, 0.08, 0.1,stations.get(13)));//4
		stations.get(13).addProperty(new Property("富有柿園", 5000, 2, 0.1, 0.15, 0.2,stations.get(13)));
		stations.get(13).addProperty(new Property("ネーブル園", 8000, 2, 0.05, 0.08, 0.1,stations.get(13)));
		stations.get(13).addProperty(new Property("梅干し林", 10000, 2, 0.05, 0.08, 0.1,stations.get(13)));//2
		stations.get(13).addProperty(new Property("梅干し林", 10000, 2, 0.05, 0.08, 0.1,stations.get(13)));//2
		//御坊
		stations.get(14).addProperty(new Property("ジャンボかまぼこ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(14)));
		stations.get(14).addProperty(new Property("ピーマン畑", 3000, 2, 0.05, 0.06, 0.07,stations.get(14)));
		stations.get(14).addProperty(new Property("ミカン畑", 5000, 2, 0.08, 0.1, 0.12,stations.get(14)));//2
		stations.get(14).addProperty(new Property("ミカン畑", 5000, 2, 0.08, 0.1, 0.12,stations.get(14)));//2
		stations.get(14).addProperty(new Property("梅林", 8000, 2, 0.05, 0.06, 0.07,stations.get(14)));//2
		stations.get(14).addProperty(new Property("梅林", 8000, 2, 0.05, 0.06, 0.07,stations.get(14)));//2
		stations.get(14).addProperty(new Property("麻雀牌工場", 100000, 6, 0.1, 0.11, 0.12,stations.get(14)));
		//白浜
		stations.get(15).addProperty(new Property("レタス栽培", 3000, 2, 0.05, 0.08, 0.1,stations.get(15)));
		stations.get(15).addProperty(new Property("露天風呂", 30000, 3, 0.01, 0.02, 0.03,stations.get(15)));
		stations.get(15).addProperty(new Property("アニマルパーク", 80000, 3, 0.01, 0.02, 0.03,stations.get(15)));
		stations.get(15).addProperty(new Property("日本旅館", 100000, 3, 0.01, 0.02, 0.03,stations.get(15)));//2
		stations.get(15).addProperty(new Property("日本旅館", 100000, 3, 0.01, 0.02, 0.03,stations.get(15)));//2
		stations.get(15).addProperty(new Property("ホテル", 300000, 3, 0.02, 0.03, 0.04,stations.get(15)));//2
		stations.get(15).addProperty(new Property("ホテル", 300000, 3, 0.02, 0.03, 0.04,stations.get(15)));//2
		stations.get(15).addProperty(new Property("ゴルフ場", 500000, 6, 0.01, 0.02, 0.03,stations.get(15)));
		//大阪
		stations.get(16).addProperty(new Property("たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));//2
		stations.get(16).addProperty(new Property("たこ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));//2
		stations.get(16).addProperty(new Property("お好み焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));
		stations.get(16).addProperty(new Property("ねぎ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));//2
		stations.get(16).addProperty(new Property("ねぎ焼き屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));//2
		stations.get(16).addProperty(new Property("きつねうどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(16)));
		stations.get(16).addProperty(new Property("串カツ屋", 1000, 1, 0.8, 1.0, 1.5,stations.get(16)));
		stations.get(16).addProperty(new Property("はりはり鍋屋", 10000, 1, 0.05, 0.06, 0.07,stations.get(16)));
		stations.get(16).addProperty(new Property("お笑い劇場", 80000, 3, 0.1, 0.2, 0.3,stations.get(16)));
		stations.get(16).addProperty(new Property("製薬会社", 400000, 5, 0.02, 0.03, 0.05,stations.get(16)));//2
		stations.get(16).addProperty(new Property("製薬会社", 400000, 5, 0.02, 0.03, 0.05,stations.get(16)));//2
		stations.get(16).addProperty(new Property("プロ野球チーム", 770000, 3, 0.04, 0.05, 0.06,stations.get(16)));
		stations.get(16).addProperty(new Property("水族館", 1350000, 3, 0.01, 0.02, 0.03,stations.get(16)));
		stations.get(16).addProperty(new Property("テレビ局", 4440000, 3, 0.05, 0.06, 0.1,stations.get(16)));
		stations.get(16).addProperty(new Property("映画ランドジャパン", 5400000, 3, 0.02, 0.03, 0.04,stations.get(16)));
		//なんば
		stations.get(17).addProperty(new Property("たこ焼き屋", 1000, 1, 1.0, 1.5, 2.0,stations.get(17)));
		stations.get(17).addProperty(new Property("豚まん屋", 1000, 1, 1.0, 1.5, 2.0,stations.get(17)));
		stations.get(17).addProperty(new Property("お笑い劇場", 50000, 3, 0.15, 0.2, 0.25,stations.get(17)));
		stations.get(17).addProperty(new Property("黒門市場", 160000, 4, 0.06, 0.07, 0.08,stations.get(17)));
		stations.get(17).addProperty(new Property("お笑い興業", 500000, 4, 0.1, 0.12, 0.14,stations.get(17)));
		stations.get(17).addProperty(new Property("日本橋電気街", 1000000, 6, 0.03, 0.04, 0.05,stations.get(17)));
		stations.get(17).addProperty(new Property("老舗デパート", 5600000, 6, 0.04, 0.05, 0.06,stations.get(17)));
		//天王寺
		stations.get(18).addProperty(new Property("将棋場", 1000, 3, 0.5, 0.6, 0.7,stations.get(18)));
		stations.get(18).addProperty(new Property("シチューうどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(18)));
		stations.get(18).addProperty(new Property("串カツ屋", 1000, 1, 0.8, 1.0, 1.2,stations.get(18)));
		stations.get(18).addProperty(new Property("ヨーグルトケーキ屋", 1000, 1, 1.0, 1.5, 2.0,stations.get(18)));
		stations.get(18).addProperty(new Property("動物園", 400000, 3, 0.01, 0.02, 0.03,stations.get(18)));
		stations.get(18).addProperty(new Property("仰天閣タワー", 800000, 3, 0.02, 0.03, 0.04,stations.get(18)));
		stations.get(18).addProperty(new Property("あべのルルカス", 13000000, 6, 0.02, 0.03, 0.04,stations.get(18)));
		//舞鶴
		stations.get(19).addProperty(new Property("肉じゃが屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(19)));//2
		stations.get(19).addProperty(new Property("肉じゃが屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(19)));//2
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 1, 0.5, 0.6, 0.7,stations.get(19)));//3
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 1, 0.5, 0.6, 0.7,stations.get(19)));//3
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 1, 0.5, 0.6, 0.7,stations.get(19)));//3
		stations.get(19).addProperty(new Property("万願寺唐辛子畑", 3000, 2, 0.05, 0.08, 0.1,stations.get(19)));
		stations.get(19).addProperty(new Property("こっぺ蟹料理屋", 20000, 1, 0.05, 0.06, 0.07,stations.get(19)));//2
		stations.get(19).addProperty(new Property("こっぺ蟹料理屋", 20000, 1, 0.05, 0.06, 0.07,stations.get(19)));//2
		//北浜
		stations.get(20).addProperty(new Property("あったらいいな製薬", 350000, 5, 0.06, 0.08, 0.1,stations.get(20)));
		stations.get(20).addProperty(new Property("アルバム制作会社", 660000, 5, 0.04, 0.06, 0.08,stations.get(20)));
		stations.get(20).addProperty(new Property("シノノギ製薬", 2000000, 5, 0.04, 0.06, 0.08,stations.get(20)));
		stations.get(20).addProperty(new Property("紡績工場", 2200000, 5, 0.01, 0.02, 0.03,stations.get(20)));
		stations.get(20).addProperty(new Property("繊維メーカー", 2400000, 5, 0.01, 0.02, 0.03,stations.get(20)));
		stations.get(20).addProperty(new Property("スタミナ製薬", 5000000, 5, 0.02, 0.03, 0.4,stations.get(20)));
		stations.get(20).addProperty(new Property("ビタミンA製薬", 15000000, 5, 0.06, 0.08, 0.1,stations.get(20)));
		//京橋
		stations.get(21).addProperty(new Property("フランクフルト屋", 1000, 1, 0.8, 1.0, 1.3,stations.get(21)));
		stations.get(21).addProperty(new Property("つかみ寿司屋", 10000, 1, 0.1, 0.2, 0.3,stations.get(21)));
		stations.get(21).addProperty(new Property("お笑い劇場", 40000, 3, 0.1, 0.2, 0.3,stations.get(21)));
		stations.get(21).addProperty(new Property("テレビ局1", 100000, 3, 0.07, 0.1, 0.13,stations.get(21)));
		stations.get(21).addProperty(new Property("テレビ局2", 1000000, 3, 0.1, 0.15, 0.2,stations.get(21)));
		//鶴橋
		stations.get(22).addProperty(new Property("チヂミ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(22)));
		stations.get(22).addProperty(new Property("トッポギ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(22)));
		stations.get(22).addProperty(new Property("韓国キムチ屋", 1000, 1, 1.0, 1.5, 2.0,stations.get(22)));
		stations.get(22).addProperty(new Property("韓国焼き肉屋", 3000, 1, 0.8, 1.0, 1.2,stations.get(22)));//2
		stations.get(22).addProperty(new Property("韓国焼き肉屋", 3000, 1, 0.8, 1.0, 1.2,stations.get(22)));//2
		//嵐山
		stations.get(23).addProperty(new Property("湯豆腐料理屋", 10000, 1, 0.03, 0.04, 0.05,stations.get(23)));//2
		stations.get(23).addProperty(new Property("湯豆腐料理屋", 10000, 1, 0.03, 0.04, 0.05,stations.get(23)));//2
		stations.get(23).addProperty(new Property("高級料亭", 50000, 1, 0.1, 0.2, 0.3,stations.get(23)));//2
		stations.get(23).addProperty(new Property("高級料亭", 50000, 1, 0.1, 0.2, 0.3,stations.get(23)));//2
		stations.get(23).addProperty(new Property("料亭億兆", 600000, 1, 0.07, 0.1, 0.12,stations.get(23)));
		//門真
		stations.get(24).addProperty(new Property("クワイ園", 10000, 2, 0.04, 0.08, 0.1,stations.get(24)));
		stations.get(24).addProperty(new Property("自動車部品工場", 60000, 5, 0.03, 0.05, 0.07,stations.get(24)));
		stations.get(24).addProperty(new Property("経営の神様記念館", 240000, 3, 0.1, 0.2, 0.3,stations.get(24)));
		stations.get(24).addProperty(new Property("魔法瓶工場", 400000, 5, 0.03, 0.04, 0.05,stations.get(24)));
		stations.get(24).addProperty(new Property("ジェネリック薬品", 470000, 5, 0.04, 0.05, 0.06,stations.get(24)));
		stations.get(24).addProperty(new Property("電池充電器工場", 800000, 5, 0.03, 0.04, 0.05,stations.get(24)));
		stations.get(24).addProperty(new Property("ポニョソニック電機", 40000000, 5, 0.1, 0.2, 0.3,stations.get(24)));
		//五條
		stations.get(25).addProperty(new Property("柿の葉寿司屋", 1000, 1, 0.8, 1.5, 2.0,stations.get(25)));//2
		stations.get(25).addProperty(new Property("柿の葉寿司屋", 1000, 1, 0.8, 1.5, 2.0,stations.get(25)));//2
		stations.get(25).addProperty(new Property("富有柿園", 30000, 2, 0.1, 0.2, 0.3,stations.get(25)));//2
		stations.get(25).addProperty(new Property("富有柿園", 30000, 2, 0.1, 0.2, 0.3,stations.get(25)));//2
		stations.get(25).addProperty(new Property("柿ワイン工場", 50000, 1, 0.04, 0.06, 0.08,stations.get(25)));
		//京都
		stations.get(26).addProperty(new Property("油とり紙屋", 5000, 6, 0.8, 1.0, 2.0,stations.get(26)));//2
		stations.get(26).addProperty(new Property("油とり紙屋", 5000, 6, 0.8, 1.0, 2.0,stations.get(26)));//2
		stations.get(26).addProperty(new Property("麩まんじゅう屋", 10000, 1, 0.05, 0.06, 0.07,stations.get(26)));
		stations.get(26).addProperty(new Property("老舗コーヒー屋", 10000, 1, 0.08, 0.09, 0.1,stations.get(26)));
		stations.get(26).addProperty(new Property("あぶり餅屋", 10000, 1, 0.1, 0.15, 0.2,stations.get(26)));
		stations.get(26).addProperty(new Property("生八つ橋屋", 30000, 1, 0.1, 0.15, 0.2,stations.get(26)));//2
		stations.get(26).addProperty(new Property("生八つ橋屋", 30000, 1, 0.1, 0.15, 0.2,stations.get(26)));//2
		stations.get(26).addProperty(new Property("湯豆腐料理屋", 80000, 1, 0.05, 0.1, 0.15,stations.get(26)));
		stations.get(26).addProperty(new Property("錦上市場", 250000, 4, 0.04, 0.08, 0.1,stations.get(26)));
		stations.get(26).addProperty(new Property("料亭億兆", 1000000, 1, 0.05, 0.08, 0.15,stations.get(26)));
		//橿原
		stations.get(27).addProperty(new Property("柿の葉寿司", 1000, 1, 0.5, 0.6, 0.7,stations.get(27)));
		stations.get(27).addProperty(new Property("石舞台グッズ屋", 1000, 3, 0.5, 0.6, 0.7,stations.get(27)));
		stations.get(27).addProperty(new Property("牛乳スープ鍋屋", 1000, 1, 1.0, 1.5, 2.0,stations.get(27)));
		stations.get(27).addProperty(new Property("富有柿園", 10000, 2, 0.1, 0.2, 0.3,stations.get(27)));//2
		stations.get(27).addProperty(new Property("富有柿園", 10000, 2, 0.1, 0.2, 0.3,stations.get(27)));//2
		//祇園
		stations.get(28).addProperty(new Property("天然かき氷屋", 10000, 1, 0.1, 0.2, 0.3,stations.get(28)));
		stations.get(28).addProperty(new Property("帆布工場", 10000, 5, 0.07, 0.1, 0.15,stations.get(28)));
		stations.get(28).addProperty(new Property("葛きり屋", 20000, 1, 0.1, 0.15, 0.2,stations.get(28)));
		stations.get(28).addProperty(new Property("京の米料亭", 20000, 1, 0.15, 0.2, 0.3,stations.get(28)));
		stations.get(28).addProperty(new Property("生麩屋", 30000, 1, 0.1, 0.2, 0.3,stations.get(28)));
		stations.get(28).addProperty(new Property("鮨割烹", 30000, 1, 0.15, 0.2, 0.3,stations.get(28)));
		stations.get(28).addProperty(new Property("イタリアン料理店", 40000, 1, 0.15, 0.2, 0.3,stations.get(28)));
		//奈良
		stations.get(29).addProperty(new Property("鹿せんべい屋", 1000, 1, 0.25, 0.3, 0.4,stations.get(29)));//2
		stations.get(29).addProperty(new Property("鹿せんべい屋", 1000, 1, 0.25, 0.3, 0.4,stations.get(29)));//2
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(29)));//3
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(29)));//3
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(29)));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 2, 0.1, 0.15, 0.2,stations.get(29)));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 2, 0.1, 0.15, 0.2,stations.get(29)));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 2, 0.1, 0.15, 0.2,stations.get(29)));//3
		//新宮
		stations.get(30).addProperty(new Property("さんま寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(30)));//2
		stations.get(30).addProperty(new Property("さんま寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(30)));//2
		stations.get(30).addProperty(new Property("めはり寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(30)));//2
		stations.get(30).addProperty(new Property("めはり寿司屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(30)));//2
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 2, 0.05, 0.08, 0.1,stations.get(30)));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 2, 0.05, 0.08, 0.1,stations.get(30)));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 2, 0.05, 0.08, 0.1,stations.get(30)));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 2, 0.05, 0.08, 0.1,stations.get(30)));//4
		//大津
		stations.get(31).addProperty(new Property("走り餅屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(31)));
		stations.get(31).addProperty(new Property("しじみめし屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(31)));//2
		stations.get(31).addProperty(new Property("しじみめし屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(31)));//2
		stations.get(31).addProperty(new Property("和菓子屋", 10000, 1, 0.02, 0.04, 0.06,stations.get(31)));
		stations.get(31).addProperty(new Property("産業用センサー工場", 280000, 5, 0.02, 0.04, 0.06,stations.get(31)));
		stations.get(31).addProperty(new Property("バイオ研究所", 900000, 5, 0.03, 0.04, 0.05,stations.get(31)));
		stations.get(31).addProperty(new Property("液晶用ガラス工場", 3200000, 5, 0.04, 0.05, 0.06,stations.get(31)));
		//伊賀
		stations.get(32).addProperty(new Property("手裏剣せんべい屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(32)));
		stations.get(32).addProperty(new Property("堅焼きせんべい屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(32)));
		stations.get(32).addProperty(new Property("松尾芭蕉グッズ屋", 3000, 3, 0.1, 0.15, 0.2,stations.get(32)));
		stations.get(32).addProperty(new Property("忍者屋敷", 20000, 3, 0.03, 0.04, 0.05,stations.get(32)));
		stations.get(32).addProperty(new Property("伊賀焼き物工房", 40000, 6, 0.02, 0.04, 0.06,stations.get(32)));
		stations.get(32).addProperty(new Property("伊賀牛屋", 60000, 1, 0.03, 0.04, 0.05,stations.get(32)));//2
		stations.get(32).addProperty(new Property("伊賀牛屋", 60000, 1, 0.03, 0.04, 0.05,stations.get(32)));//2
		//長浜
		stations.get(33).addProperty(new Property("焼きサバそうめん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(33)));
		stations.get(33).addProperty(new Property("豊臣秀吉グッズ屋", 3000, 3, 0.8, 1.0, 2.0,stations.get(33)));
		stations.get(33).addProperty(new Property("オルゴール館", 30000, 3, 0.02, 0.03, 0.04,stations.get(33)));
		stations.get(33).addProperty(new Property("ガラス工房", 50000, 3, 0.03, 0.04, 0.05,stations.get(33)));
		stations.get(33).addProperty(new Property("フィギュア博物館", 80000, 3, 0.05, 0.06, 0.07,stations.get(33)));
		stations.get(33).addProperty(new Property("鉄道記念館", 510000, 3, 0.02, 0.03, 0.04,stations.get(33)));
		stations.get(33).addProperty(new Property("黒壁の街並み", 700000, 3, 0.02, 0.03, 0.05,stations.get(33)));
		//彦根
		stations.get(34).addProperty(new Property("ひこっしーグッズ屋", 1000, 3, 0.5, 0.6, 0.7,stations.get(34)));
		stations.get(34).addProperty(new Property("和ローソク屋", 3000, 6, 0.5, 0.6, 0.7,stations.get(34)));//2
		stations.get(34).addProperty(new Property("和ローソク屋", 3000, 6, 0.5, 0.6, 0.7,stations.get(34)));//2
		stations.get(34).addProperty(new Property("赤かぶら漬け屋", 3000, 1, 0.5, 0.6, 0.7,stations.get(34)));
		stations.get(34).addProperty(new Property("塩すき焼き屋", 10000, 1, 0.01, 0.02, 0.03,stations.get(34)));
		//近江八幡
		stations.get(35).addProperty(new Property("でっちようかん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(35)));//2
		stations.get(35).addProperty(new Property("でっちようかん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(35)));//2
		stations.get(35).addProperty(new Property("赤こんにゃく屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(35)));//2
		stations.get(35).addProperty(new Property("赤こんにゃく屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(35)));//2
		stations.get(35).addProperty(new Property("バウムクーヘン屋", 10000, 1, 0.1, 0.15, 0.2,stations.get(35)));
		stations.get(35).addProperty(new Property("近江牛屋", 70000, 1, 0.05, 0.06, 0.07,stations.get(35)));//2
		stations.get(35).addProperty(new Property("近江牛屋", 70000, 1, 0.05, 0.06, 0.07,stations.get(35)));//2
		stations.get(35).addProperty(new Property("製薬会社", 120000, 5, 0.02, 0.03, 0.04,stations.get(35)));
		//四日市
		stations.get(36).addProperty(new Property("とんてき屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(36)));
		stations.get(36).addProperty(new Property("点火プラグ工場", 750000, 5, 0.03, 0.04, 0.05,stations.get(36)));
		stations.get(36).addProperty(new Property("食品素材工場", 770000, 5, 0.03, 0.04, 0.05,stations.get(36)));
		stations.get(36).addProperty(new Property("物流倉庫会社", 840000, 6, 0.03, 0.04, 0.05,stations.get(36)));
		stations.get(36).addProperty(new Property("板ガラス工場", 1000000, 5, 0.03, 0.04, 0.05,stations.get(36)));
		stations.get(36).addProperty(new Property("石油精製工場", 2000000, 5, 0.02, 0.03, 0.04,stations.get(36)));//2
		stations.get(36).addProperty(new Property("石油精製工場", 2000000, 5, 0.02, 0.03, 0.04,stations.get(36)));//2
		//津
		stations.get(37).addProperty(new Property("福引せんべい屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(37)));
		stations.get(37).addProperty(new Property("巨大餃子屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(37)));
		stations.get(37).addProperty(new Property("うなぎ屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(37)));
		stations.get(37).addProperty(new Property("天むす屋", 1000, 1, 0.8, 1.0, 2.0,stations.get(37)));
		stations.get(37).addProperty(new Property("ベビーラーメン工場", 180000, 1, 0.02, 0.03, 0.04,stations.get(37)));
		stations.get(37).addProperty(new Property("肉まんあんまん工場", 220000, 1, 0.05, 0.06, 0.07,stations.get(37)));
		stations.get(37).addProperty(new Property("造船所", 300000, 5, 0.03, 0.04, 0.05,stations.get(37)));
		//松阪
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 1, 0.03, 0.04, 0.05,stations.get(38)));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 1, 0.03, 0.04, 0.05,stations.get(38)));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 1, 0.03, 0.04, 0.05,stations.get(38)));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 1, 0.03, 0.04, 0.05,stations.get(38)));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 1, 0.03, 0.04, 0.05,stations.get(38)));//5
		//伊勢
		stations.get(39).addProperty(new Property("伊勢うどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(39)));//2
		stations.get(39).addProperty(new Property("伊勢うどん屋", 1000, 1, 0.5, 0.6, 0.7,stations.get(39)));//2
		stations.get(39).addProperty(new Property("てこね寿司屋", 5000, 1, 0.8, 1.0, 1.5,stations.get(39)));//2
		stations.get(39).addProperty(new Property("てこね寿司屋", 5000, 1, 0.8, 1.0, 1.5,stations.get(39)));//2
		stations.get(39).addProperty(new Property("ふくふく餅屋", 10000, 1, 0.04, 0.05, 0.06,stations.get(39)));
		stations.get(39).addProperty(new Property("戦国パーク", 100000, 3, 0.01, 0.02, 0.03,stations.get(39)));
		stations.get(39).addProperty(new Property("おまいり横丁", 2000000, 3, 0.02, 0.03, 0.04,stations.get(39)));
		//鳥羽
		stations.get(40).addProperty(new Property("真珠養殖工場", 50000, 4, 0.03, 0.05, 0.07,stations.get(40)));//2
		stations.get(40).addProperty(new Property("真珠養殖工場", 50000, 4, 0.03, 0.05, 0.07,stations.get(40)));//2
		stations.get(40).addProperty(new Property("黒鯛漁", 70000, 4, 0.04, 0.05, 0.6,stations.get(40)));
		stations.get(40).addProperty(new Property("牡蠣養殖工場", 80000, 4, 0.05, 0.06, 0.07,stations.get(40)));//2
		stations.get(40).addProperty(new Property("牡蠣養殖工場", 80000, 4, 0.05, 0.06, 0.07,stations.get(40)));//2

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

		createLink();
	}

	private static void createLink() {
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
							if(!ContainsEvent.isMassInJapan(from.getX()-1,from.getY())) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX()+1,from.getY())) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()-1)) {
								stations.get(getIndexOfStation(from)).getCoordinates().addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()+1)) {
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
							if(!ContainsEvent.isMassInJapan(from.getX()-1,from.getY())) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX()+1,from.getY())) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()-1)) {
								blue.get(getIndexOfBlue(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()+1)) {
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
							if(!ContainsEvent.isMassInJapan(from.getX()-1,from.getY())) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX()+1,from.getY())) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()-1)) {
								red.get(getIndexOfRed(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()+1)) {
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
							if(!ContainsEvent.isMassInJapan(from.getX()-1,from.getY())) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX()+1,from.getY())) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()-1)) {
								yellow.get(getIndexOfYellow(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()+1)) {
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
							if(!ContainsEvent.isMassInJapan(from.getX()-1,from.getY())) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(x==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX()+1,from.getY())) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(y==2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()-1)) {
								shop.get(getIndexOfShop(from)).addLinks(to);
							}
						}else if(y==-2) {
							if(!ContainsEvent.isMassInJapan(from.getX(),from.getY()+1)) {
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

	//全てのマスのコストをリセットする
	public static void allClose() {
		for(int i=0;i<getAllCoordinates().size();i++) {
			getAllCoordinates().get(i).close();
		}
	}

	public static void resetGoalDistance() {
		for(Coordinates coor:getAllCoordinates()) {
			coor.setGoalDistance(100);
		}
	}

	public static void setGoalDistance() {
		resetGoalDistance();
		Searcher.searchGoalDistance();
	}

	public static void setGoalDistance(Coordinates coor,int distance) {
		getCoordinates(coor).setGoalDistance(distance);
	}

	public static int getGoalDistance(Coordinates coor) {
		return getCoordinates(coor).getGoalDistance();
	}
	public static int getGoalDistance(int x,int y) {
		return getCoordinates(x,y).getGoalDistance();
	}

	//ゴールの座標を取得
	public static Coordinates getGoalCoor() {
		return goal.getCoordinates();
	}

	public static Station getGoal() {
		return goal;
	}

	//ゴールの名前を取得
	public static String getGoalName() {
		return goal.getName();
	}

	//ゴールを一時的に保存
	public static void saveGoal() {
		saveGoal = goal;
	}

	//保存したゴールの座標を取得
	public static Coordinates getSaveGoalCoor() {
		return saveGoal.getCoordinates();
	}

	public static Station getSaveGoal() {
		return saveGoal;
	}

	public static String getSaveGoalName() {
		return saveGoal.getName();
	}

	//駅名で指定した駅の独占状態のOn/Offを切り替える
	public static void updateStationMono(String name) {
		getStation(name).updateMono();
	}

	//物件情報で指定した駅の独占状態のOn/Offを切り替える
	public static void updateStationMono(Property property) {
		getStation(property).updateMono();
	}

	//駅の数を返す
	public static int getStationSize() {
		return stations.size();
	}

	//要素番号で指定した駅の座標を取得
	public static Coordinates getStationCoor(int index) {
		Coordinates staCoor = stations.get(index).getCoordinates();
		return staCoor;
	}

	//駅の座標一覧を取得
	public static ArrayList<Coordinates> getStationCoorList(){
		ArrayList<Coordinates> result = new ArrayList<Coordinates>();
		for(Station s:stations) {
			result.add(s.getCoordinates());
		}
		return result;
	}

	//青マスの座標一覧を取得
	public static ArrayList<Coordinates> getBlueCoorList(){
		return blue;
	}

	//要素番号で指定した青マスの座標を取得
	public static Coordinates getBlueCoor(int index) {
		Coordinates blueCoor = blue.get(index);
		return blueCoor;
	}

	//赤マスの座標一覧を取得
	public static ArrayList<Coordinates> getRedCoorList(){
		return red;
	}

	//要素番号で指定した赤マスの座標を取得
	public static Coordinates getRedCoor(int index) {
		Coordinates redCoor = red.get(index);
		return redCoor;
	}

	//黄マスの座標一覧を取得
	public static ArrayList<Coordinates> getYellowCoorList(){
		return yellow;
	}

	//要素番号で指定した黄マスの座標を取得
	public static Coordinates getYellowCoor(int index) {
		Coordinates yellowCoor=yellow.get(index);
		return yellowCoor;
	}

	//店マスの座標一覧を取得
	public static ArrayList<Coordinates> getShopCoorList(){
		return shop;
	}

	//要素番号で指定した店マスの座標を取得
	public static Coordinates getShopCoor(int index) {
		Coordinates shopCoor = shop.get(index);
		return shopCoor;
	}

	//駅の一覧を取得
	public static ArrayList<Station> getStationList(){
		return stations;
	}

	//駅の名前一覧を取得
	public static ArrayList<String> getStationNameList(){
		ArrayList<String> list = new ArrayList<String>();
		for(Station sta:stations) {
			list.add(sta.getName());
		}
		return list;
	}

	//要素番号で指定した駅を取得
	public static Station getStation(int index) {
		Station station = stations.get(index);
		return station;
	}
	public static Station getStation(Property property) {
		for(Station sta : stations) {
			if(ContainsEvent.station(sta, property)) {
				return sta;
			}
		}

		return null;
	}
	public static Station getStation(Coordinates coor) {
		for(Station sta : stations) {
			if(ContainsEvent.coor(sta, coor)) {
				return sta;
			}
		}

		return null;
	}
	public static Station getStation(int x,int y) {
		for(Station sta : stations) {
			if(ContainsEvent.coor(sta, x,y)) {
				return sta;
			}
		}

		return null;
	}
	public static Station getStation(String stationName) {
		for(Station sta : stations) {
			if(ContainsEvent.name(sta,stationName)) {
				return sta;
			}
		}

		return null;
	}

	//座標で指定した駅名を取得
	public static String getStationName(Coordinates coor) {
		for(Station sta : stations) {
			if(ContainsEvent.coor(sta, coor)) {
				return sta.getName();
			}
		}

		return null;
	}
	public static String getStationName(int x,int y) {
		for(Station sta : stations) {
			if(ContainsEvent.coor(sta, x, y)) {
				return sta.getName();
			}
		}

		return null;
	}

	//全ての物件の数を取得
	public static int propertySize() {
		int size=0;
		for(Station sta:stations) {
			size+=sta.getPropertySize();
		}
		return size;
	}

	//全ての物件情報を取得
	public static ArrayList<Property> getPropertys(){
		ArrayList<Property> list = new ArrayList<Property>();
		for(Station sta:stations) {
			list.addAll(sta.getPropertys());
		}
		return list;
	}

	//駅名で指定した駅に属する物件一覧を取得
	public static ArrayList<Property> getStaInPropertys(String name){
		for(Station sta : stations) {
			if(sta.getName().equals(name)) {
				return sta.getPropertys();
			}
		}

		return null;
	}

	//駅名で指定した駅に属する物件の内、要素番号で指定した物件を取得
	public static Property getStaInProperty(String name,int index){
		for(Station sta : stations) {
			if(ContainsEvent.name(sta,name)) {
				return sta.getProperty(index);
			}
		}

		return null;
	}

	//駅名で指定した駅の物件数を取得
	public static int getStaInPropertySize(String name){
		for(Station sta : stations) {
			if(ContainsEvent.name(sta,name)) {
				return sta.getPropertySize();
			}
		}

		return -1;
	}

	//全てのマス座標を取得
	public static ArrayList<Coordinates> getAllCoordinates(){
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
	public static synchronized Coordinates getCoordinates(int x,int y) {
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(ContainsEvent.coor(list.get(i),x,y)) {
				return list.get(i);
			}
		}

		return null;
	}
	public static synchronized Coordinates getCoordinates(Coordinates coor) {
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(ContainsEvent.coor(list.get(i),coor)) {
				return list.get(i);
			}
		}

		return null;
	}

	//ゴールマスを初期化
	public static void initGoal() {
		int x=0,y=0;
		while(!(ContainsEvent.isStation(x,y) && (x!=6 || y!=9))) {//スタート地点がゴールにならない為
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		goal = getStation(x,y);
		FrameEvent.setMapGoalColor();

  		Japan.setGoalDistance();
	}

	//ゴールマスを変更
	public static void changeGoal() {
		int x=0,y=0;
		while((!ContainsEvent.isStation(x, y)) || goal==getStation(x,y)) {
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		FrameEvent.resetMapGoalColor();
		goal = getStation(x,y);
		FrameEvent.setMapGoalColor();

  		Japan.setGoalDistance();
	}

	//指定の座標のマスの配列番号を取得
	public static int getIndexOf(int x,int y) {
		int result;
		result=getIndexOfStation(x,y);
		if(result!=-1)return result;
		result=getIndexOfBlue(x,y);
		if(result!=-1)return result;
		result=getIndexOfRed(x,y);
		if(result!=-1)return result;
		result=getIndexOfYellow(x,y);
		if(result!=-1)return result;
		result=getIndexOfShop(x,y);
		if(result!=-1)return result;

		return -1;
	}
	public static int getIndexOf(Coordinates coor) {
		int result;
		result=getIndexOfStation(coor);
		if(result!=-1)return result;
		result=getIndexOfBlue(coor);
		if(result!=-1)return result;
		result=getIndexOfRed(coor);
		if(result!=-1)return result;
		result=getIndexOfYellow(coor);
		if(result!=-1)return result;
		result=getIndexOfShop(coor);
		if(result!=-1)return result;

		return -1;
	}

	//指定の座標の駅の配列番号を取得
	public static int getIndexOfStation(int x,int y){
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list), x, y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfStation(Coordinates coor){
		for(int list=0;list<stations.size();list++) {
			if(ContainsEvent.coor(stations.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の青マスの配列番号を取得
	public static int getIndexOfBlue(int x,int y){
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfBlue(Coordinates coor){
		for(int list=0;list<blue.size();list++) {
			if(ContainsEvent.coor(blue.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の赤マスの配列番号を取得
	public static int getIndexOfRed(int x,int y){
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfRed(Coordinates coor){
		for(int list=0;list<red.size();list++) {
			if(ContainsEvent.coor(red.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の黄マスの配列番号を取得
	public static int getIndexOfYellow(int x,int y){
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfYellow(Coordinates coor){
		for(int list=0;list<yellow.size();list++) {
			if(ContainsEvent.coor(yellow.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定の座標の店マスの配列番号を取得
	public static int getIndexOfShop(int x,int y){
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),x,y)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}
	public static int getIndexOfShop(Coordinates coor){
		for(int list=0;list<shop.size();list++) {
			if(ContainsEvent.coor(shop.get(list),coor)) {//駅の座標が来たら
				return list;
			}
		}

		return -1;
	}

	//指定したTFをリストにして取得
	private static ArrayList<Boolean> getBoolList(Boolean top,Boolean bottom,Boolean left,Boolean right){
		ArrayList<Boolean> rail = new ArrayList<Boolean>();
		rail.add(left);
		rail.add(right);
		rail.add(top);
		rail.add(bottom);
		return rail;
	}

	//指定した座標から移動可能な方角一覧を取得
	public static ArrayList<Boolean> getVector(int x,int y,int size){
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
	public static ArrayList<Boolean> getVector(Coordinates coor,int size){
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
	public static ArrayList<Coordinates> getMovePossibles(int x,int y) {
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
	public static ArrayList<Coordinates> getMovePossibles(Coordinates coor) {
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
