package lifegame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Japan {
	private ArrayList<Station> stations = new ArrayList<Station>();//駅一覧
	private ArrayList<Coordinates> blue = new ArrayList<Coordinates>();//青マスの座標一覧
	private ArrayList<Coordinates> red = new ArrayList<Coordinates>();//赤マスの座標一覧
	private ArrayList<Coordinates> yellow = new ArrayList<Coordinates>();//黄マスの座標一覧
	private ArrayList<Coordinates> shop = new ArrayList<Coordinates>();//カード屋の座標一覧
	private Map<Coordinates,ArrayList<Boolean>> railBoolMapping = new HashMap<Coordinates,ArrayList<Boolean>>();//移動可能方向
	private Map<Coordinates,ArrayList<Coordinates>> railMapping = new HashMap<Coordinates,ArrayList<Coordinates>>();//移動可能座標
	private int goal;//目的地の要素番号
	private int saveGoal;//ゴール保存用

	//1マス10
	public Japan() {
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
		stations.get(0).addProperty(new Property("塩饅頭屋", 1000, 0.5, 0.6, 0.7));
		stations.get(0).addProperty(new Property("忠臣蔵グッズ屋", 1000, 1.0, 1.5, 2.0));
		stations.get(0).addProperty(new Property("布海苔養殖場", 30000, 0.01, 0.02, 0.03));
		stations.get(0).addProperty(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		stations.get(0).addProperty(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		stations.get(0).addProperty(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		//姫路
		stations.get(1).addProperty(new Property("手延べそうめん屋", 1000, 0.25, 0.3, 0.4));//2
		stations.get(1).addProperty(new Property("手延べそうめん屋", 1000, 0.25, 0.3, 0.4));//2
		stations.get(1).addProperty(new Property("焼きアナゴ屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(1).addProperty(new Property("焼きアナゴ屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(1).addProperty(new Property("革製品工場", 10000, 0.02, 0.03, 0.04));
		stations.get(1).addProperty(new Property("石油工場", 800000, 0.01, 0.03, 0.05));
		stations.get(1).addProperty(new Property("化学工場", 1000000, 0.01, 0.03, 0.05));
		stations.get(1).addProperty(new Property("製鉄所", 1400000, 0.03, 0.04, 0.05));
		//明石
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		stations.get(2).addProperty(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		stations.get(2).addProperty(new Property("海鮮市場", 60000, 0.02, 0.03, 0.04));
		//淡路島
		stations.get(3).addProperty(new Property("引き戻し屋", 500, 1.0, 2.0, 3.0));
		stations.get(3).addProperty(new Property("玉ねぎ畑", 5000, 0.08, 0.12, 0.16));//2
		stations.get(3).addProperty(new Property("玉ねぎ畑", 5000, 0.08, 0.12, 0.16));//2
		stations.get(3).addProperty(new Property("お香工場", 10000, 0.02, 0.04, 0.06));
		stations.get(3).addProperty(new Property("淡路瓦工場", 30000, 0.01, 0.02, 0.03));
		//城崎
		stations.get(4).addProperty(new Property("湯上りジュース屋", 500, 0.5, 0.7, 0.8));
		stations.get(4).addProperty(new Property("カニ寿司屋", 1000, 0.25, 0.3, 0.5));
		stations.get(4).addProperty(new Property("麦わら細工工房", 3000, 0.25, 0.3, 0.5));
		stations.get(4).addProperty(new Property("城崎温泉旅館", 100000, 0.02, 0.03, 0.05));
		stations.get(4).addProperty(new Property("カニ割烹旅館", 200000, 0.05, 0.06, 0.07));
		//福知山
		stations.get(5).addProperty(new Property("音頭せんべい屋", 1000, 0.5, 1.0, 1.5));
		stations.get(5).addProperty(new Property("ブドウ園", 5000, 0.05, 0.06, 0.07));
		stations.get(5).addProperty(new Property("タケノコ林", 8000, 0.05, 0.06, 0.07));
		stations.get(5).addProperty(new Property("栗のテリーヌ屋", 20000, 0.1, 0.15 ,0.2));
		stations.get(5).addProperty(new Property("痔の薬品工場", 500000, 0.1, 0.15, 0.2));
		stations.get(5).addProperty(new Property("磁気テープ工場", 600000, 0.03, 0.04, 0.05));
		stations.get(5).addProperty(new Property("ビタミン剤工場", 1000000, 0.07, 0.1, 0.13));
		//三田
		stations.get(6).addProperty(new Property("すき焼きパン屋", 1000, 0.5, 0.6, 0.7));
		stations.get(6).addProperty(new Property("丹波栗屋", 10000, 0.02, 0.03, 0.04));//2
		stations.get(6).addProperty(new Property("丹波栗屋", 10000, 0.02, 0.03, 0.04));//2
		stations.get(6).addProperty(new Property("三田牛ステーキ屋", 30000, 0.03, 0.04, 0.05));
		stations.get(6).addProperty(new Property("丹波松茸屋", 50000, 0.07, 0.08, 0.09));
		//神戸
		stations.get(7).addProperty(new Property("そばめし屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(7).addProperty(new Property("そばめし屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(7).addProperty(new Property("フランスパン屋", 10000, 0.03, 0.04, 0.05));//2
		stations.get(7).addProperty(new Property("フランスパン屋", 10000, 0.03, 0.04, 0.05));//2
		stations.get(7).addProperty(new Property("中華飯店", 100000, 0.02, 0.03, 0.05));
		stations.get(7).addProperty(new Property("ステーキハウス", 150000, 0.03, 0.05, 0.07));
		stations.get(7).addProperty(new Property("ハーバーパーク", 1300000, 0.02, 0.03, 0.04));
		stations.get(7).addProperty(new Property("南京町中華街", 2000000, 0.02, 0.03, 0.04));
		//吹田
		stations.get(8).addProperty(new Property("くずきり工場", 20000, 0.03, 0.04, 0.05));
		stations.get(8).addProperty(new Property("げりぴー工場", 50000, 0.02, 0.03, 0.04));
		stations.get(8).addProperty(new Property("てっぺん化粧品工場", 60000, 0.02, 0.03, 0.04));
		stations.get(8).addProperty(new Property("即席めん工場", 600000, 0.07, 0.08, 0.09));
		stations.get(8).addProperty(new Property("ビール工場", 1000000, 0.04, 0.05, 0.06));
		stations.get(8).addProperty(new Property("ぞうきん工場", 1200000, 0.02, 0.03, 0.04));
		stations.get(8).addProperty(new Property("目薬メーカー", 1500000, 0.04, 0.05, 0.06));
		//出石
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(9).addProperty(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(9).addProperty(new Property("露店トマト店", 1000, 0.8, 1.0, 1.5));
		stations.get(9).addProperty(new Property("出石白磁工房", 10000, 0.01, 0.02, 0.03));
		//天保山
		stations.get(10).addProperty(new Property("ロックカフェ", 20000, 0.03, 0.04, 0.05));
		stations.get(10).addProperty(new Property("大観覧車", 400000, 0.03, 0.04, 0.05));
		stations.get(10).addProperty(new Property("アウトレットモール", 1000000, 0.04, 0.05, 0.06));
		stations.get(10).addProperty(new Property("ジンベイザメ水族館", 6000000, 0.1, 0.2, 0.3));
		stations.get(10).addProperty(new Property("映画ランドジャパン", 35000000, 0.01, 0.02, 0.03));
		//堺
		stations.get(11).addProperty(new Property("かすうどん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(11).addProperty(new Property("かすうどん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(11).addProperty(new Property("くるみ餅屋", 1000, 1.0, 2.0, 3.0));
		stations.get(11).addProperty(new Property("刃物工場", 20000, 0.03, 0.04, 0.05));
		stations.get(11).addProperty(new Property("クラッカー菓子工場", 20000, 0.07, 0.08, 0.09));
		stations.get(11).addProperty(new Property("回転寿司チェーン", 200000, 0.07, 0.08, 0.09));
		stations.get(11).addProperty(new Property("引越センター", 370000, 0.07, 0.08, 0.09));
		//岸和田
		stations.get(12).addProperty(new Property("だんじりグッズ屋", 1000, 1.0, 2.0, 3.0));
		stations.get(12).addProperty(new Property("玉ねぎ畑", 3000, 0.05, 0.06, 0.07));
		stations.get(12).addProperty(new Property("水ナス畑", 5000, 0.1, 0.11, 0.12));//2
		stations.get(12).addProperty(new Property("水ナス畑", 5000, 0.1, 0.11, 0.12));//2
		stations.get(12).addProperty(new Property("顕微鏡ガラス工場", 200000, 0.02, 0.03, 0.04));
		//和歌山
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		stations.get(13).addProperty(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		stations.get(13).addProperty(new Property("富有柿園", 5000, 0.1, 0.15, 0.2));
		stations.get(13).addProperty(new Property("ネーブル園", 8000, 0.05, 0.08, 0.1));
		stations.get(13).addProperty(new Property("梅干し林", 10000, 0.05, 0.08, 0.1));//2
		stations.get(13).addProperty(new Property("梅干し林", 10000, 0.05, 0.08, 0.1));//2
		//御坊
		stations.get(14).addProperty(new Property("ジャンボかまぼこ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(14).addProperty(new Property("ピーマン畑", 3000, 0.05, 0.06, 0.07));
		stations.get(14).addProperty(new Property("ミカン畑", 5000, 0.08, 0.1, 0.12));//2
		stations.get(14).addProperty(new Property("ミカン畑", 5000, 0.08, 0.1, 0.12));//2
		stations.get(14).addProperty(new Property("梅林", 8000, 0.05, 0.06, 0.07));//2
		stations.get(14).addProperty(new Property("梅林", 8000, 0.05, 0.06, 0.07));//2
		stations.get(14).addProperty(new Property("麻雀牌工場", 100000, 0.1, 0.11, 0.12));
		//白浜
		stations.get(15).addProperty(new Property("レタス栽培", 3000, 0.05, 0.08, 0.1));
		stations.get(15).addProperty(new Property("露天風呂", 30000, 0.01, 0.02, 0.03));
		stations.get(15).addProperty(new Property("アニマルパーク", 80000, 0.01, 0.02, 0.03));
		stations.get(15).addProperty(new Property("日本旅館", 100000, 0.01, 0.02, 0.03));//2
		stations.get(15).addProperty(new Property("日本旅館", 100000, 0.01, 0.02, 0.03));//2
		stations.get(15).addProperty(new Property("ホテル", 300000, 0.02, 0.03, 0.04));//2
		stations.get(15).addProperty(new Property("ホテル", 300000, 0.02, 0.03, 0.04));//2
		stations.get(15).addProperty(new Property("ゴルフ場", 500000, 0.01, 0.02, 0.03));
		//大阪
		stations.get(16).addProperty(new Property("たこ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(16).addProperty(new Property("たこ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(16).addProperty(new Property("お好み焼き屋", 1000, 0.5, 0.6, 0.7));
		stations.get(16).addProperty(new Property("ねぎ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(16).addProperty(new Property("ねぎ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(16).addProperty(new Property("きつねうどん屋", 1000, 0.5, 0.6, 0.7));
		stations.get(16).addProperty(new Property("串カツ屋", 1000, 0.8, 1.0, 1.5));
		stations.get(16).addProperty(new Property("はりはり鍋屋", 10000, 0.05, 0.06, 0.07));
		stations.get(16).addProperty(new Property("お笑い劇場", 80000, 0.1, 0.2, 0.3));
		stations.get(16).addProperty(new Property("製薬会社", 400000, 0.02, 0.03, 0.05));//2
		stations.get(16).addProperty(new Property("製薬会社", 400000, 0.02, 0.03, 0.05));//2
		stations.get(16).addProperty(new Property("プロ野球チーム", 770000, 0.04, 0.05, 0.06));
		stations.get(16).addProperty(new Property("水族館", 1350000, 0.01, 0.02, 0.03));
		stations.get(16).addProperty(new Property("テレビ局", 4440000, 0.05, 0.06, 0.1));
		stations.get(16).addProperty(new Property("映画ランドジャパン", 5400000, 0.02, 0.03, 0.04));
		//なんば
		stations.get(17).addProperty(new Property("たこ焼き屋", 1000, 1.0, 1.5, 2.0));
		stations.get(17).addProperty(new Property("豚まん屋", 1000, 1.0, 1.5, 2.0));
		stations.get(17).addProperty(new Property("お笑い劇場", 50000, 0.15, 0.2, 0.25));
		stations.get(17).addProperty(new Property("黒門市場", 160000, 0.06, 0.07, 0.08));
		stations.get(17).addProperty(new Property("お笑い興業", 500000, 0.1, 0.12, 0.14));
		stations.get(17).addProperty(new Property("日本橋電気街", 1000000, 0.03, 0.04, 0.05));
		stations.get(17).addProperty(new Property("老舗デパート", 5600000, 0.04, 0.05, 0.06));
		//天王寺
		stations.get(18).addProperty(new Property("将棋場", 1000, 0.5, 0.6, 0.7));
		stations.get(18).addProperty(new Property("シチューうどん屋", 1000, 0.5, 0.6, 0.7));
		stations.get(18).addProperty(new Property("串カツ屋", 1000, 0.8, 1.0, 1.2));
		stations.get(18).addProperty(new Property("ヨーグルトケーキ屋", 1000, 1.0, 1.5, 2.0));
		stations.get(18).addProperty(new Property("動物園", 400000, 0.01, 0.02, 0.03));
		stations.get(18).addProperty(new Property("仰天閣タワー", 800000, 0.02, 0.03, 0.04));
		stations.get(18).addProperty(new Property("あべのルルカス", 13000000, 0.02, 0.03, 0.04));
		//舞鶴
		stations.get(19).addProperty(new Property("肉じゃが屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(19).addProperty(new Property("肉じゃが屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		stations.get(19).addProperty(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		stations.get(19).addProperty(new Property("万願寺唐辛子畑", 3000, 0.05, 0.08, 0.1));
		stations.get(19).addProperty(new Property("こっぺ蟹料理屋", 20000, 0.05, 0.06, 0.07));//2
		stations.get(19).addProperty(new Property("こっぺ蟹料理屋", 20000, 0.05, 0.06, 0.07));//2
		//北浜
		stations.get(20).addProperty(new Property("あったらいいな製薬", 350000, 0.06, 0.08, 0.1));
		stations.get(20).addProperty(new Property("アルバム制作会社", 660000, 0.04, 0.06, 0.08));
		stations.get(20).addProperty(new Property("シノノギ製薬", 2000000, 0.04, 0.06, 0.08));
		stations.get(20).addProperty(new Property("紡績工場", 2200000, 0.01, 0.02, 0.03));
		stations.get(20).addProperty(new Property("繊維メーカー", 2400000, 0.01, 0.02, 0.03));
		stations.get(20).addProperty(new Property("スタミナ製薬", 5000000, 0.02, 0.03, 0.4));
		stations.get(20).addProperty(new Property("ビタミンA製薬", 15000000, 0.06, 0.08, 0.1));
		//京橋
		stations.get(21).addProperty(new Property("フランクフルト屋", 1000, 0.8, 1.0, 1.3));
		stations.get(21).addProperty(new Property("つかみ寿司屋", 10000, 0.1, 0.2, 0.3));
		stations.get(21).addProperty(new Property("お笑い劇場", 40000, 0.1, 0.2, 0.3));
		stations.get(21).addProperty(new Property("テレビ局1", 100000, 0.07, 0.1, 0.13));
		stations.get(21).addProperty(new Property("テレビ局2", 1000000, 0.1, 0.15, 0.2));
		//鶴橋
		stations.get(22).addProperty(new Property("チヂミ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(22).addProperty(new Property("トッポギ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(22).addProperty(new Property("韓国キムチ屋", 1000, 1.0, 1.5, 2.0));
		stations.get(22).addProperty(new Property("韓国焼き肉屋", 3000, 0.8, 1.0, 1.2));//2
		stations.get(22).addProperty(new Property("韓国焼き肉屋", 3000, 0.8, 1.0, 1.2));//2
		//嵐山
		stations.get(23).addProperty(new Property("湯豆腐料理屋", 10000, 0.03, 0.04, 0.05));//2
		stations.get(23).addProperty(new Property("湯豆腐料理屋", 10000, 0.03, 0.04, 0.05));//2
		stations.get(23).addProperty(new Property("高級料亭", 50000, 0.1, 0.2, 0.3));//2
		stations.get(23).addProperty(new Property("高級料亭", 50000, 0.1, 0.2, 0.3));//2
		stations.get(23).addProperty(new Property("料亭億兆", 600000, 0.07, 0.1, 0.12));
		//門真
		stations.get(24).addProperty(new Property("クワイ園", 10000, 0.04, 0.08, 0.1));
		stations.get(24).addProperty(new Property("自動車部品工場", 60000, 0.03, 0.05, 0.07));
		stations.get(24).addProperty(new Property("経営の神様記念館", 240000, 0.1, 0.2, 0.3));
		stations.get(24).addProperty(new Property("魔法瓶工場", 400000, 0.03, 0.04, 0.05));
		stations.get(24).addProperty(new Property("ジェネリック薬品", 470000, 0.04, 0.05, 0.06));
		stations.get(24).addProperty(new Property("電池充電器工場", 800000, 0.03, 0.04, 0.05));
		stations.get(24).addProperty(new Property("ポニョソニック電機", 40000000, 0.1, 0.2, 0.3));
		//五條
		stations.get(25).addProperty(new Property("柿の葉寿司屋", 1000, 0.8, 1.5, 2.0));//2
		stations.get(25).addProperty(new Property("柿の葉寿司屋", 1000, 0.8, 1.5, 2.0));//2
		stations.get(25).addProperty(new Property("富有柿園", 30000, 0.1, 0.2, 0.3));//2
		stations.get(25).addProperty(new Property("富有柿園", 30000, 0.1, 0.2, 0.3));//2
		stations.get(25).addProperty(new Property("柿ワイン工場", 50000, 0.04, 0.06, 0.08));
		//京都
		stations.get(26).addProperty(new Property("油とり紙屋", 5000, 0.8, 1.0, 2.0));//2
		stations.get(26).addProperty(new Property("油とり紙屋", 5000, 0.8, 1.0, 2.0));//2
		stations.get(26).addProperty(new Property("麩まんじゅう屋", 10000, 0.05, 0.06, 0.07));
		stations.get(26).addProperty(new Property("老舗コーヒー屋", 10000, 0.08, 0.09, 0.1));
		stations.get(26).addProperty(new Property("あぶり餅屋", 10000, 0.1, 0.15, 0.2));
		stations.get(26).addProperty(new Property("生八つ橋屋", 30000, 0.1, 0.15, 0.2));//2
		stations.get(26).addProperty(new Property("生八つ橋屋", 30000, 0.1, 0.15, 0.2));//2
		stations.get(26).addProperty(new Property("湯豆腐料理屋", 80000, 0.05, 0.1, 0.15));
		stations.get(26).addProperty(new Property("錦上市場", 250000, 0.04, 0.08, 0.1));
		stations.get(26).addProperty(new Property("料亭億兆", 1000000, 0.05, 0.08, 0.15));
		//橿原
		stations.get(27).addProperty(new Property("柿の葉寿司", 1000, 0.5, 0.6, 0.7));
		stations.get(27).addProperty(new Property("石舞台グッズ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(27).addProperty(new Property("牛乳スープ鍋屋", 1000, 1.0, 1.5, 2.0));
		stations.get(27).addProperty(new Property("富有柿園", 10000, 0.1, 0.2, 0.3));//2
		stations.get(27).addProperty(new Property("富有柿園", 10000, 0.1, 0.2, 0.3));//2
		//祇園
		stations.get(28).addProperty(new Property("天然かき氷屋", 10000, 0.1, 0.2, 0.3));
		stations.get(28).addProperty(new Property("帆布工場", 10000, 0.07, 0.1, 0.15));
		stations.get(28).addProperty(new Property("葛きり屋", 20000, 0.1, 0.15, 0.2));
		stations.get(28).addProperty(new Property("京の米料亭", 20000, 0.15, 0.2, 0.3));
		stations.get(28).addProperty(new Property("生麩屋", 30000, 0.1, 0.2, 0.3));
		stations.get(28).addProperty(new Property("鮨割烹", 30000, 0.15, 0.2, 0.3));
		stations.get(28).addProperty(new Property("イタリアン料理店", 40000, 0.15, 0.2, 0.3));
		//奈良
		stations.get(29).addProperty(new Property("鹿せんべい屋", 1000, 0.25, 0.3, 0.4));//2
		stations.get(29).addProperty(new Property("鹿せんべい屋", 1000, 0.25, 0.3, 0.4));//2
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(29).addProperty(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		stations.get(29).addProperty(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		//新宮
		stations.get(30).addProperty(new Property("さんま寿司屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(30).addProperty(new Property("さんま寿司屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(30).addProperty(new Property("めはり寿司屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(30).addProperty(new Property("めはり寿司屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		stations.get(30).addProperty(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		//大津
		stations.get(31).addProperty(new Property("走り餅屋", 1000, 0.5, 0.6, 0.7));
		stations.get(31).addProperty(new Property("しじみめし屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(31).addProperty(new Property("しじみめし屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(31).addProperty(new Property("和菓子屋", 10000, 0.02, 0.04, 0.06));
		stations.get(31).addProperty(new Property("産業用センサー工場", 280000, 0.02, 0.04, 0.06));
		stations.get(31).addProperty(new Property("バイオ研究所", 900000, 0.03, 0.04, 0.05));
		stations.get(31).addProperty(new Property("液晶用ガラス工場", 3200000, 0.04, 0.05, 0.06));
		//伊賀
		stations.get(32).addProperty(new Property("手裏剣せんべい屋", 1000, 0.5, 0.6, 0.7));
		stations.get(32).addProperty(new Property("堅焼きせんべい屋", 1000, 0.5, 0.6, 0.7));
		stations.get(32).addProperty(new Property("松尾芭蕉グッズ屋", 3000, 0.1, 0.15, 0.2));
		stations.get(32).addProperty(new Property("忍者屋敷", 20000, 0.03, 0.04, 0.05));
		stations.get(32).addProperty(new Property("伊賀焼き物工房", 40000, 0.02, 0.04, 0.06));
		stations.get(32).addProperty(new Property("伊賀牛屋", 60000, 0.03, 0.04, 0.05));//2
		stations.get(32).addProperty(new Property("伊賀牛屋", 60000, 0.03, 0.04, 0.05));//2
		//長浜
		stations.get(33).addProperty(new Property("焼きサバそうめん屋", 1000, 0.5, 0.6, 0.7));
		stations.get(33).addProperty(new Property("豊臣秀吉グッズ屋", 3000, 0.8, 1.0, 2.0));
		stations.get(33).addProperty(new Property("オルゴール館", 30000, 0.02, 0.03, 0.04));
		stations.get(33).addProperty(new Property("ガラス工房", 50000, 0.03, 0.04, 0.05));
		stations.get(33).addProperty(new Property("フィギュア博物館", 80000, 0.05, 0.06, 0.07));
		stations.get(33).addProperty(new Property("鉄道記念館", 510000, 0.02, 0.03, 0.04));
		stations.get(33).addProperty(new Property("黒壁の街並み", 700000, 0.02, 0.03, 0.05));
		//彦根
		stations.get(34).addProperty(new Property("ひこっしーグッズ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(34).addProperty(new Property("和ローソク屋", 3000, 0.5, 0.6, 0.7));//2
		stations.get(34).addProperty(new Property("和ローソク屋", 3000, 0.5, 0.6, 0.7));//2
		stations.get(34).addProperty(new Property("赤かぶら漬け屋", 3000, 0.5, 0.6, 0.7));
		stations.get(34).addProperty(new Property("塩すき焼き屋", 10000, 0.01, 0.02, 0.03));
		//近江八幡
		stations.get(35).addProperty(new Property("でっちようかん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(35).addProperty(new Property("でっちようかん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(35).addProperty(new Property("赤こんにゃく屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(35).addProperty(new Property("赤こんにゃく屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(35).addProperty(new Property("バウムクーヘン屋", 10000, 0.1, 0.15, 0.2));
		stations.get(35).addProperty(new Property("近江牛屋", 70000, 0.05, 0.06, 0.07));//2
		stations.get(35).addProperty(new Property("近江牛屋", 70000, 0.05, 0.06, 0.07));//2
		stations.get(35).addProperty(new Property("製薬会社", 120000, 0.02, 0.03, 0.04));
		//四日市
		stations.get(36).addProperty(new Property("とんてき屋", 1000, 0.5, 0.6, 0.7));
		stations.get(36).addProperty(new Property("点火プラグ工場", 750000, 0.03, 0.04, 0.05));
		stations.get(36).addProperty(new Property("食品素材工場", 770000, 0.03, 0.04, 0.05));
		stations.get(36).addProperty(new Property("物流倉庫会社", 840000, 0.03, 0.04, 0.05));
		stations.get(36).addProperty(new Property("板ガラス工場", 1000000, 0.03, 0.04, 0.05));
		stations.get(36).addProperty(new Property("石油精製工場", 2000000, 0.02, 0.03, 0.04));//2
		stations.get(36).addProperty(new Property("石油精製工場", 2000000, 0.02, 0.03, 0.04));//2
		//津
		stations.get(37).addProperty(new Property("福引せんべい屋", 1000, 0.5, 0.6, 0.7));
		stations.get(37).addProperty(new Property("巨大餃子屋", 1000, 0.5, 0.6, 0.7));
		stations.get(37).addProperty(new Property("うなぎ屋", 1000, 0.5, 0.6, 0.7));
		stations.get(37).addProperty(new Property("天むす屋", 1000, 0.8, 1.0, 2.0));
		stations.get(37).addProperty(new Property("ベビーラーメン工場", 180000, 0.02, 0.03, 0.04));
		stations.get(37).addProperty(new Property("肉まんあんまん工場", 220000, 0.05, 0.06, 0.07));
		stations.get(37).addProperty(new Property("造船所", 300000, 0.03, 0.04, 0.05));
		//松阪
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		stations.get(38).addProperty(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		//伊勢
		stations.get(39).addProperty(new Property("伊勢うどん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(39).addProperty(new Property("伊勢うどん屋", 1000, 0.5, 0.6, 0.7));//2
		stations.get(39).addProperty(new Property("てこね寿司屋", 5000, 0.8, 1.0, 1.5));//2
		stations.get(39).addProperty(new Property("てこね寿司屋", 5000, 0.8, 1.0, 1.5));//2
		stations.get(39).addProperty(new Property("ふくふく餅屋", 10000, 0.04, 0.05, 0.06));
		stations.get(39).addProperty(new Property("戦国パーク", 100000, 0.01, 0.02, 0.03));
		stations.get(39).addProperty(new Property("おまいり横丁", 2000000, 0.02, 0.03, 0.04));
		//鳥羽
		stations.get(40).addProperty(new Property("真珠養殖工場", 50000, 0.03, 0.05, 0.07));//2
		stations.get(40).addProperty(new Property("真珠養殖工場", 50000, 0.03, 0.05, 0.07));//2
		stations.get(40).addProperty(new Property("黒鯛漁", 70000, 0.04, 0.05, 0.6));
		stations.get(40).addProperty(new Property("牡蠣養殖工場", 80000, 0.05, 0.06, 0.07));//2
		stations.get(40).addProperty(new Property("牡蠣養殖工場", 80000, 0.05, 0.06, 0.07));//2

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
		railBoolMapping.put(red.get(26),getBoolList(true,true,false,false));
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
				//if(from.contains(to))continue;
				int x = from.getX()-to.getX();
				int y = from.getY()-to.getY();
				if(!((x>=-2 && x<=2) && (y>=-2 && y<=2)))continue;//処理数を減らす
				if(containsStation(from.getX(),from.getY())) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates()).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates()).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates()).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates()).get(0))) {
						if(x==2) {
							if(!contains(from.getX()-1,from.getY())) {
								stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().addLinks(to);
							}
						}else if(x==-2) {
							if(!contains(from.getX()+1,from.getY())) {
								stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().addLinks(to);
							}
						}else if(y==2) {
							if(!contains(from.getX(),from.getY()-1)) {
								stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().addLinks(to);
							}
						}else if(y==-2) {
							if(!contains(from.getX(),from.getY()+1)) {
								stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().addLinks(to);
							}
						}else {
							stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().addLinks(to);
						}
					}
					railMapping.put(stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates(),stations.get(getIndexOfStation(from.getX(),from.getY())).getCoordinates().getLinks());
				}
				if(containsBlue(from.getX(),from.getY())) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(blue.get(getIndexOfBlue(from.getX(),from.getY()))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(blue.get(getIndexOfBlue(from.getX(),from.getY()))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(blue.get(getIndexOfBlue(from.getX(),from.getY()))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(blue.get(getIndexOfBlue(from.getX(),from.getY()))).get(0))) {
						if(x==2) {
							if(!contains(from.getX()-1,from.getY())) {
								blue.get(getIndexOfBlue(from.getX(),from.getY())).addLinks(to);
							}
						}else if(x==-2) {
							if(!contains(from.getX()+1,from.getY())) {
								blue.get(getIndexOfBlue(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==2) {
							if(!contains(from.getX(),from.getY()-1)) {
								blue.get(getIndexOfBlue(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==-2) {
							if(!contains(from.getX(),from.getY()+1)) {
								blue.get(getIndexOfBlue(from.getX(),from.getY())).addLinks(to);
							}
						}else {
							blue.get(getIndexOfBlue(from.getX(),from.getY())).addLinks(to);
						}
					}
					railMapping.put(blue.get(getIndexOfBlue(from.getX(),from.getY())),blue.get(getIndexOfBlue(from.getX(),from.getY())).getLinks());
				}
				if(containsRed(from.getX(),from.getY())) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(red.get(getIndexOfRed(from.getX(),from.getY()))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(red.get(getIndexOfRed(from.getX(),from.getY()))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(red.get(getIndexOfRed(from.getX(),from.getY()))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(red.get(getIndexOfRed(from.getX(),from.getY()))).get(0))) {
						if(x==2) {
							if(!contains(from.getX()-1,from.getY())) {
								red.get(getIndexOfRed(from.getX(),from.getY())).addLinks(to);
							}
						}else if(x==-2) {
							if(!contains(from.getX()+1,from.getY())) {
								red.get(getIndexOfRed(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==2) {
							if(!contains(from.getX(),from.getY()-1)) {
								red.get(getIndexOfRed(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==-2) {
							if(!contains(from.getX(),from.getY()+1)) {
								red.get(getIndexOfRed(from.getX(),from.getY())).addLinks(to);
							}
						}else {
							red.get(getIndexOfRed(from.getX(),from.getY())).addLinks(to);
						}
					}
					railMapping.put(red.get(getIndexOfRed(from.getX(),from.getY())),red.get(getIndexOfRed(from.getX(),from.getY())).getLinks());
				}
				if(containsYellow(from.getX(),from.getY())) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(yellow.get(getIndexOfYellow(from.getX(),from.getY()))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(yellow.get(getIndexOfYellow(from.getX(),from.getY()))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(yellow.get(getIndexOfYellow(from.getX(),from.getY()))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(yellow.get(getIndexOfYellow(from.getX(),from.getY()))).get(0))) {
						if(x==2) {
							if(!contains(from.getX()-1,from.getY())) {
								yellow.get(getIndexOfYellow(from.getX(),from.getY())).addLinks(to);
							}
						}else if(x==-2) {
							if(!contains(from.getX()+1,from.getY())) {
								yellow.get(getIndexOfYellow(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==2) {
							if(!contains(from.getX(),from.getY()-1)) {
								yellow.get(getIndexOfYellow(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==-2) {
							if(!contains(from.getX(),from.getY()+1)) {
								yellow.get(getIndexOfYellow(from.getX(),from.getY())).addLinks(to);
							}
						}else {
							yellow.get(getIndexOfYellow(from.getX(),from.getY())).addLinks(to);
						}
					}
					railMapping.put(yellow.get(getIndexOfYellow(from.getX(),from.getY())),yellow.get(getIndexOfYellow(from.getX(),from.getY())).getLinks());
				}
				if(containsShop(from.getX(),from.getY())) {
					if((x==0 && (y==-1 || y==-2) && railBoolMapping.get(shop.get(getIndexOfShop(from.getX(),from.getY()))).get(3)) ||
							(x==0 && (y==1 || y==2) && railBoolMapping.get(shop.get(getIndexOfShop(from.getX(),from.getY()))).get(2)) ||
							((x==-1 || x==-2) && y==0 && railBoolMapping.get(shop.get(getIndexOfShop(from.getX(),from.getY()))).get(1)) ||
							((x==1 || x==2) && y==0 && railBoolMapping.get(shop.get(getIndexOfShop(from.getX(),from.getY()))).get(0))) {
						if(x==2) {
							if(!contains(from.getX()-1,from.getY())) {
								shop.get(getIndexOfShop(from.getX(),from.getY())).addLinks(to);
							}
						}else if(x==-2) {
							if(!contains(from.getX()+1,from.getY())) {
								shop.get(getIndexOfShop(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==2) {
							if(!contains(from.getX(),from.getY()-1)) {
								shop.get(getIndexOfShop(from.getX(),from.getY())).addLinks(to);
							}
						}else if(y==-2) {
							if(!contains(from.getX(),from.getY()+1)) {
								shop.get(getIndexOfShop(from.getX(),from.getY())).addLinks(to);
							}
						}else {
							shop.get(getIndexOfShop(from.getX(),from.getY())).addLinks(to);
						}
					}
					railMapping.put(shop.get(getIndexOfShop(from.getX(),from.getY())),shop.get(getIndexOfShop(from.getX(),from.getY())).getLinks());
				}
			}
		}
	}

	//全てのマスのコストをリセットする
	public void allClose() {
		for(int i=0;i<getAllCoordinates().size();i++) {
			getAllCoordinates().get(i).close();
		}
	}

	//ゴールの要素番号を取得
	public int getGoalIndex() {
		return goal;
	}

	//ゴールの座標を取得
	public Coordinates getGoal() {
		return getStationCoor(goal);
	}

	//ゴールの名前を取得
	public String getGoalName() {
		return getStationName(getStationCoor(goal));
	}

	//ゴールを一時的に保存
	public void saveGoal() {
		saveGoal = goal;
	}

	//保存したゴールの要素番号を取得
	public int getSaveGoalIndex() {
		return saveGoal;
	}

	//保存したゴールの座標を取得
	public Coordinates getSaveGoal() {
		return getStationCoor(saveGoal);
	}

	//駅名で指定した駅の独占状態のOn/Offを切り替える
	public void monopoly(String name) {
		getStation(name).changePropertysMono();
	}

	//物件情報で指定した駅の独占状態のOn/Offを切り替える
	public void monopoly(Property property) {
		getStation(property).changePropertysMono();
	}

	//駅の数を返す
	public int stationSize() {
		return stations.size();
	}

	//要素番号で指定した駅の座標を取得
	public Coordinates getStationCoor(int index) {
		return stations.get(index).getCoordinates();
	}

	//駅の座標一覧を取得
	public ArrayList<Coordinates> getStationsCoor(){
		ArrayList<Coordinates> result = new ArrayList<Coordinates>();
		for(Station s:stations) {
			result.add(s.getCoordinates());
		}
		return result;
	}

	//青マスの座標一覧を取得
	public ArrayList<Coordinates> getBlueCoor(){
		return blue;
	}

	//要素番号で指定した青マスの座標を取得
	public Coordinates getBlueCoor(int index) {
		return blue.get(index);
	}

	//赤マスの座標一覧を取得
	public ArrayList<Coordinates> getRedCoor(){
		return red;
	}

	//要素番号で指定した赤マスの座標を取得
	public Coordinates getRedCoor(int index) {
		return red.get(index);
	}

	//黄マスの座標一覧を取得
	public ArrayList<Coordinates> getYellowCoor(){
		return yellow;
	}

	//要素番号で指定した黄マスの座標を取得
	public Coordinates getYellowCoor(int index) {
		return yellow.get(index);
	}

	//店マスの座標一覧を取得
	public ArrayList<Coordinates> getshopCoor(){
		return shop;
	}

	//要素番号で指定した店マスの座標を取得
	public Coordinates getShopCoor(int index) {
		return shop.get(index);
	}

	//駅の一覧を取得
	public ArrayList<Station> getStations(){
		return stations;
	}

	//駅の名前一覧を取得
	public ArrayList<String> getStationNameList(){
		ArrayList<String> list = new ArrayList<String>();
		for(Station sta:stations) {
			list.add(sta.getName());
		}
		return list;
	}

	//要素番号で指定した駅を取得
	public Station getStation(int index) {
		return stations.get(index);
	}

	//指定のpropertyを含むstationを取得
	public Station getStation(Property property) {
		for(Station sta : stations) {
			if(sta.containsProperty(property)) {
				return sta;
			}
		}
		return null;
	}

	//駅名で指定した駅を取得
	public Station getStation(String stationName) {
		for(Station sta : stations) {
			if(sta.getName().equals(stationName)) {
				return sta;
			}
		}
		return null;
	}

	//座標で指定した駅名を取得
	public String getStationName(Coordinates coor) {
		for(Station sta : stations) {
			if(sta.getCoordinates().contains(coor)) {
				return sta.getName();
			}
		}
		return null;
	}

	//全ての物件の数を取得
	public int propertySize() {
		int size=0;
		for(Station sta:stations) {
			size+=sta.getPropertySize();
		}
		return size;
	}

	//全ての物件情報を取得
	public ArrayList<Property> getPropertys(){
		ArrayList<Property> list = new ArrayList<Property>();
		for(Station sta:stations) {
			list.addAll(sta.getPropertys());
		}
		return list;
	}

	//駅名で指定した駅に属する物件一覧を取得
	public ArrayList<Property> getStaInPropertys(String name){
		for(Station sta : stations) {
			if(sta.getName().equals(name)) {
				return sta.getPropertys();
			}
		}
		return null;
	}

	//駅名で指定した駅に属する物件の内、要素番号で指定した物件を取得
	public Property getStaInProperty(String name,int index){
		for(Station sta : stations) {
			if(sta.getName().equals(name)) {
				return sta.getProperty(index);
			}
		}
		return null;
	}

	//駅名で指定した駅の物件数を取得
	public int getStaInPropertySize(String name){
		for(Station sta : stations) {
			if(sta.getName().equals(name)) {
				return sta.getPropertySize();
			}
		}
		System.out.println("error");
		return -1;
	}

	//全てのマス座標を取得
	public ArrayList<Coordinates> getAllCoordinates(){
		ArrayList<Coordinates> list = new ArrayList<Coordinates>();
		list.addAll(getStationsCoor());
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

	//指定の座標にマスが存在するか
	public synchronized boolean contains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:getAllCoordinates()) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標にマスが存在するか
	public synchronized boolean contains(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:getAllCoordinates()) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標のCoordinatesインスタンスを取得
	public synchronized Coordinates getCoordinates(int x,int y) {
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(list.get(i).getX()==x && list.get(i).getY()==y) {
				return list.get(i);
			}
		}
		return null;
	}

	//指定の座標のCoordinatesインスタンスを取得
	public synchronized Coordinates getCoordinates(Coordinates coordinates) {
		int x=coordinates.getX();
		int y=coordinates.getY();
		ArrayList<Coordinates> list = getAllCoordinates();
		for(int i = 0;i<list.size();i++) {
			if(list.get(i).getX()==x && list.get(i).getY()==y) {
				return list.get(i);
			}
		}
		return null;
	}

	//指定の座標に駅が存在するか
	public boolean containsStation(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:getStationsCoor()) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に駅が存在するか
	public boolean containsStation(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:getStationsCoor()) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に青マスが存在するか
	public boolean containsBlue(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:blue) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に青マスが存在するか
	public boolean containsBlue(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:blue) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に赤マスが存在するか
	public boolean containsRed(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:red) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に赤マスが存在するか
	public boolean containsRed(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:red) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に黄マスが存在するか
	public boolean containsYellow(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:yellow) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に黄マスが存在するか
	public boolean containsYellow(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:yellow) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に店マスが存在するか
	public boolean containsShop(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:shop) {
			if(coor.getX()==x && coor.getY()==y) {
				flag=true;
			}
		}
		return flag;
	}

	//指定の座標に店マスが存在するか
	public boolean containsShop(Coordinates coordinates) {
		Boolean flag=false;
		for(Coordinates coor:shop) {
			if(coor.contains(coordinates)) {
				flag=true;
			}
		}
		return flag;
	}

	//ゴールマスを初期化
	public void initGoal() {
		int x=0,y=0;
		while(!(this.containsStation(x, y) && (x!=6 || y!=9))) {//スタート地点がゴールにならない為
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		goal = getIndexOfStation(x,y);
	}

	//ゴールマスを変更
	public void changeGoal() {
		int x=0,y=0;
		while((!this.containsStation(x, y)) || goal==getIndexOfStation(x,y)) {
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		goal = getIndexOfStation(x,y);
	}

	//指定の座標のマスの配列番号を取得
	public int getIndexOf(int x,int y) {
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

	//指定の座標の駅の配列番号を取得
	public int getIndexOfStation(int x,int y){
		for(int list=0;list<this.stations.size();list++) {
			if(this.stations.get(list).getCoordinates().contains(x,y)) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	//指定の座標の青マスの配列番号を取得
	public int getIndexOfBlue(int x,int y){
		for(int list=0;list<this.blue.size();list++) {
			if(this.blue.get(list).getX() == x && this.blue.get(list).getY() == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	//指定の座標の赤マスの配列番号を取得
	public int getIndexOfRed(int x,int y){
		for(int list=0;list<this.red.size();list++) {
			if(this.red.get(list).getX() == x && this.red.get(list).getY() == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	//指定の座標の黄マスの配列番号を取得
	public int getIndexOfYellow(int x,int y){
		for(int list=0;list<this.yellow.size();list++) {
			if(this.yellow.get(list).getX() == x && this.yellow.get(list).getY() == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	//指定の座標の店マスの配列番号を取得
	public int getIndexOfShop(int x,int y){
		for(int list=0;list<this.shop.size();list++) {
			if(this.shop.get(list).getX() == x && this.shop.get(list).getY() == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	//指定したTFをリストにして取得(処理を分ける方が良い)
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
			if(stations.get(list).getCoordinates().contains(x/size,y/size)) {//駅の座標が来たら
				return railBoolMapping.get(stations.get(list).getCoordinates());
			}
		}
		for(int list=0;list<blue.size();list++) {
			if(blue.get(list).getX() == x/size && blue.get(list).getY() == y/size) {
				return railBoolMapping.get(blue.get(list));
			}
		}
		for(int list=0;list<red.size();list++) {
			if(red.get(list).getX() == x/size && red.get(list).getY() == y/size) {
				return railBoolMapping.get(red.get(list));
			}
		}
		for(int list=0;list<yellow.size();list++) {
			if(yellow.get(list).getX() == x/size && yellow.get(list).getY() == y/size) {
				return railBoolMapping.get(yellow.get(list));
			}
		}
		for(int list=0;list<shop.size();list++) {
			if(shop.get(list).getX() == x/size && shop.get(list).getY() == y/size) {
				return railBoolMapping.get(shop.get(list));
			}
		}
		return null;
	}

	//指定した座標から移動可能な方角一覧を取得
	public ArrayList<Boolean> getVector(Coordinates coor,int size){
		int x=coor.getX();
		int y=coor.getY();
		for(int list=0;list<stations.size();list++) {
			if(stations.get(list).getCoordinates().contains(x/size,y/size)) {//駅の座標が来たら
				return railBoolMapping.get(stations.get(list).getCoordinates());
			}
		}
		for(int list=0;list<blue.size();list++) {
			if(blue.get(list).getX() == x/size && blue.get(list).getY() == y/size) {
				return railBoolMapping.get(blue.get(list));
			}
		}
		for(int list=0;list<red.size();list++) {
			if(red.get(list).getX() == x/size && red.get(list).getY() == y/size) {
				return railBoolMapping.get(red.get(list));
			}
		}
		for(int list=0;list<yellow.size();list++) {
			if(yellow.get(list).getX() == x/size && yellow.get(list).getY() == y/size) {
				return railBoolMapping.get(yellow.get(list));
			}
		}
		for(int list=0;list<shop.size();list++) {
			if(shop.get(list).getX() == x/size && shop.get(list).getY() == y/size) {
				return railBoolMapping.get(shop.get(list));
			}
		}
		return null;
	}

	//指定した座標から移動可能な座標一覧を取得
	public ArrayList<Coordinates> getMovePossibles(int x,int y) {
		if(containsStation(x,y)) {
			return railMapping.get(stations.get(getIndexOfStation(x,y)).getCoordinates());
		}else if(containsBlue(x,y)) {
			return railMapping.get(blue.get(getIndexOfBlue(x,y)));
		}else if(containsRed(x,y)) {
			return railMapping.get(red.get(getIndexOfRed(x,y)));
		}else if(containsYellow(x,y)) {
			return railMapping.get(yellow.get(getIndexOfYellow(x,y)));
		}else if(containsShop(x,y)) {
			return railMapping.get(shop.get(getIndexOfShop(x,y)));
		}else {
			return null;
		}
	}

	//指定した座標から移動可能な座標一覧を取得
	public ArrayList<Coordinates> getMovePossibles(Coordinates coor) {
		int x=coor.getX();
		int y=coor.getY();
		if(containsStation(x,y)) {
			return railMapping.get(stations.get(getIndexOfStation(x,y)).getCoordinates());
		}else if(containsBlue(x,y)) {
			return railMapping.get(blue.get(getIndexOfBlue(x,y)));
		}else if(containsRed(x,y)) {
			return railMapping.get(red.get(getIndexOfRed(x,y)));
		}else if(containsYellow(x,y)) {
			return railMapping.get(yellow.get(getIndexOfYellow(x,y)));
		}else if(containsShop(x,y)) {
			return railMapping.get(shop.get(getIndexOfShop(x,y)));
		}else {
			return null;
		}
	}


}

//現在の位置から指定した目的地までの最短距離と軌跡を取得
class MultiThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	public static int searchTime;
	private Coordinates start = new Coordinates();
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;

	public MultiThread(Window window) {
		this.window=window;
	}

	public MultiThread(Window window,Coordinates start) {
		this.window=window;
		this.start.setValue(start);
		Window.time = System.currentTimeMillis();
		Window.count=500;
		MultiThread.initSearchTime();
	}

	public MultiThread(Window window,Coordinates start,int searchTime) {
		this.window=window;
		this.start.setValue(start);
		Window.time = System.currentTimeMillis();
		Window.count=500;
		MultiThread.searchTime=searchTime;
	}

	public static void initSearchTime() {
		MultiThread.searchTime=400;
	}

	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		while(count<=Window.count && count <= 40 && System.currentTimeMillis()-Window.time<=searchTime) {
			ArrayList<Coordinates> list = new ArrayList<Coordinates>();
			moveTrajectory.add(new Coordinates(nowMass));//移動履歴を追加
			if(Window.japan.getGoal().contains(nowMass)){
				goal();
				break;
			}
			count++;
			ArrayList<Coordinates> can = new ArrayList<Coordinates>();
			synchronized(MultiThread.lock1) {
				can.addAll(Window.japan.getMovePossibles(this.nowMass));
			}
			for(Coordinates possibles : can) {//移動可能マスを取得
				boolean conti=false;
				Coordinates coordinates = new Coordinates(possibles);
				for(Coordinates trajectory : moveTrajectory) {//既に通った場所を省く
					synchronized(MultiThread.lock3) {
						if(!Window.japan.contains(coordinates)) {
							coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
						}
					}
					if(trajectory.contains(coordinates)) {//来た道の場合
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
			if(list.size()>3) {
				System.out.println();
				for(Coordinates coor : list) {
					System.out.println("size over   x:"+coor.getX()+"   y:"+coor.getY());
				}
			}

			//open処理
			ArrayList<Integer> costs = new ArrayList<Integer>();//移動可能マスのコスト一覧
			synchronized(MultiThread.lock2) {
				for(Coordinates coor:list) {//open処理
					Window.japan.getCoordinates(coor).open(count);//探索予定のマスをopenにする。(コストを計算し保持する。)
					costs.add(Window.japan.getCoordinates(coor).getCost());//openの結果をコスト一覧に追加
				}
			}
			//コストの小さい順にソート
			Collections.sort(list,new Comparator<Coordinates>() {
	        	public int compare(Coordinates coor1,Coordinates coor2) {
					return Integer.compare(coor1.getCost(), coor2.getCost());
				}
	        });
			Collections.sort(costs,new Comparator<Integer>() {
	        	public int compare(Integer cost1,Integer cost2) {
					return Integer.compare(cost1, cost2);
				}
	        });

			boolean me=true;
			for(int i=0;i<list.size();i++) {//移動処理
				//2マス移動
				Coordinates coordinates = new Coordinates(list.get(i));
				synchronized(MultiThread.lock3) {
					if(!Window.japan.contains(coordinates)) {
						coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
					}
				}
				if(me) {
					setMass(coordinates);
					me=false;
				}else {
					//Threadを立ち上げ、移動する
					MultiThread thread = new MultiThread(window);
					thread.setStart(start);
					thread.threadCopy(this);
					thread.setMass(coordinates);//移動

					Thread t = new Thread(thread);
					if(i!=list.size()-1) {
						t.setPriority(Thread.NORM_PRIORITY);
					}else {
						t.setPriority(Thread.MIN_PRIORITY);
					}
					t.start();
				}
			}
			Thread.yield();
		}
	}

	public void setStart(Coordinates start) {
		this.start.setValue(start);
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	public void threadCopy(MultiThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void goal() {
		synchronized(MultiThread.lock4) {
			window.setSearchResult(count,moveTrajectory);
		}
	}
}

//目的地から現在の位置までの最短距離と軌跡を取得
class NearestSearchThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	public static int searchTime = 500;
	public static int nearestCount = 100;//目的地から移動可能マスまでの最短距離
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;
	private ArrayList<Coordinates> goals = new ArrayList<Coordinates>();

	public NearestSearchThread(Window window) {
		this.window=window;
	}

	public NearestSearchThread(Window window,int searchTime) {
		this.window=window;
		NearestSearchThread.searchTime=searchTime;
	}

	public static void initSearchTime() {
		NearestSearchThread.searchTime=500;
	}

	public synchronized void addGoal(Coordinates coor) {
		goals.add(coor);
	}

	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		ArrayList<Coordinates> list;

		boolean flag;
		boolean end;
		boolean setMassFlag;
		Coordinates next = new Coordinates();
		while(count<=NearestSearchThread.nearestCount && count<=35 && System.currentTimeMillis()-Window.time<searchTime) {
			System.out.println("nearest:"+nearestCount);
			next.setValue(0, 0);
			setMassFlag=false;
			end=true;
			flag=false;

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
					Coordinates coordinates = new Coordinates(coor);
					synchronized(NearestSearchThread.lock4) {
						if(!Window.japan.contains(coordinates.getX(),coordinates.getY())) {
							coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
						}
					}
					if(moveTrajectory.get(j).contains(coordinates)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				//2マス移動(競合の可能性)
				Coordinates coordinates = new Coordinates(coor);
				synchronized(NearestSearchThread.lock4) {
					if(!Window.japan.contains(coor.getX(),coor.getY())) {
						coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
					}
				}
				if(flag) {
					//Threadを立ち上げる
					NearestSearchThread thread = new NearestSearchThread(window);
					synchronized(NearestSearchThread.lock3) {
						thread.threadCopy(this);
						thread.setMass(coordinates);//移動
					}
					Thread t = new Thread(thread);
					t.start();
				}else {
					next.setValue(coordinates);
					setMassFlag=true;
					flag=true;
				}
				end=false;
			}
			if(setMassFlag) {
				synchronized(NearestSearchThread.lock3) {
					this.setMass(next);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	private void threadCopy(NearestSearchThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
		this.goals.addAll(original.goals);
	}

	private void goal() {
		synchronized(NearestSearchThread.lock2) {
			if(NearestSearchThread.nearestCount>=count) {
				NearestSearchThread.nearestCount=count;
			}
		}
	}
}


//最寄り駅を探索するためのスレッド
class StationSearchThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static int savecount=0;
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;

	public StationSearchThread(Window window) {
		this.window=window;
	}


	public void run() {
		ArrayList<Coordinates> list;

		boolean flag;
		boolean end;
		boolean setMassFlag;
		Coordinates next = new Coordinates();
		while(count<=Window.count && savecount<=1000 && count<=15 && System.currentTimeMillis()-Window.time<300) {
			next.setValue(0, 0);
			setMassFlag=false;
			end=true;
			flag=false;

			if(Window.japan.containsStation(this.nowMass)) {
				setResult();
				break;
			}

			count++;
			savecount++;
			synchronized(StationSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));

			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					Coordinates coordinates = new Coordinates(coor);
					synchronized(StationSearchThread.lock4) {
						if(!Window.japan.contains(coordinates)) {
							coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
						}
					}
					if(moveTrajectory.get(j).contains(coordinates)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				//2マス移動(競合の可能性)
				Coordinates coordinates = new Coordinates(coor);
				synchronized(StationSearchThread.lock4) {
					if(!Window.japan.contains(coor)) {
						coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
					}
				}
				if(flag) {
					//Threadを立ち上げる
					StationSearchThread thread = new StationSearchThread(window);
					synchronized(StationSearchThread.lock3) {
						thread.threadCopy(this);
						thread.setMass(coor);//移動
					}
					Thread t = new Thread(thread);
					t.start();
				}else {
					next.setValue(coordinates);
					setMassFlag=true;
					flag=true;
				}
				end=false;

			}
			if(setMassFlag) {
				synchronized(StationSearchThread.lock3) {
					this.setMass(next);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	private void threadCopy(StationSearchThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void setResult() {
		synchronized(StationSearchThread.lock2) {
			window.setNearestStationResult(count, nowMass);
		}
	}

}

//最寄り店を探索するためのスレッド
class ShopSearchThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static int savecount=0;
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;

	public ShopSearchThread(Window window) {
		this.window=window;
	}


	public void run() {
		ArrayList<Coordinates> list;

		boolean flag;
		boolean end;
		boolean setMassFlag;
		Coordinates next = new Coordinates();
		while(count<=Window.count && savecount<=1000 && count<=15 && System.currentTimeMillis()-Window.time<300) {
			next.setValue(0, 0);
			setMassFlag=false;
			end=true;
			flag=false;
			if(Window.japan.containsShop(this.nowMass)) {
				setResult();
				break;
			}

			count++;
			savecount++;
			synchronized(ShopSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}
			moveTrajectory.add(new Coordinates(nowMass));

			for(Coordinates coor:list) {
				boolean conti=false;
				for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
					Coordinates coordinates = new Coordinates(coor);
					synchronized(ShopSearchThread.lock4) {
						if(!Window.japan.contains(coordinates)) {
							coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
						}
					}
					if(moveTrajectory.get(j).contains(coordinates)) {//来た道の場合
						conti=true;
						break;
					}
				}
				if(conti) {
					continue;
				}
				//2マス移動(競合の可能性)
				Coordinates coordinates = new Coordinates(coor);
				synchronized(ShopSearchThread.lock4) {
					if(!Window.japan.contains(coor)) {
						coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
					}
				}
				if(flag) {
					//Threadを立ち上げる
					ShopSearchThread thread = new ShopSearchThread(window);
					synchronized(ShopSearchThread.lock3) {
						thread.threadCopy(this);
						thread.setMass(coor);//移動
					}
					Thread t = new Thread(thread);
					t.start();
				}else {
					next.setValue(coordinates);
					setMassFlag=true;
					flag=true;
				}
				end=false;

			}
			if(setMassFlag) {
				synchronized(ShopSearchThread.lock3) {
					this.setMass(next);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	private void threadCopy(ShopSearchThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void setResult() {
		synchronized(ShopSearchThread.lock2) {
			window.setNearestShopResult(count, nowMass);
		}
	}

}

//残りの進めるマス数で移動した先に何があるのかを探索するためのスレッド
class MassSearchThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;

	public MassSearchThread(Window window,int count) {
		this.window=window;
		this.count = count;
	}

	public MassSearchThread(Window window) {
		this.window=window;
	}

	public void run() {
		ArrayList<Coordinates> list;

		boolean flag;
		boolean end;
		boolean setMassFlag;
		Coordinates next = new Coordinates();
		while(System.currentTimeMillis()-Window.time<300) {
			next.setValue(0, 0);
			setMassFlag=false;
			end=true;
			flag=false;
			count--;
			moveTrajectory.add(Window.japan.getCoordinates(nowMass));
			if(count <= 0) {
				setResult();
				break;
			}
			synchronized(MassSearchThread.lock1) {
				list = Window.japan.getMovePossibles(this.nowMass);
			}

			for(Coordinates coor:list) {
				//既に通った場所を省く
				Coordinates c = new Coordinates(coor);
				synchronized(MassSearchThread.lock4) {
					if(!Window.japan.contains(c)) {
						c.setValue(c.getX()*2, c.getY()*2);
					}
				}
				if(moveTrajectory.size()>1) {
					if(moveTrajectory.get(moveTrajectory.size()-2).contains(c)) {//来た道の場合
						continue;
					}
				}
				//2マス移動(競合の可能性)
				Coordinates coordinates = new Coordinates(coor);
				synchronized(MassSearchThread.lock4) {
					if(!Window.japan.contains(coor)) {
						coordinates.setValue(coordinates.getX()*2, coordinates.getY()*2);
					}
				}
				if(flag) {
					//Threadを立ち上げる
					MassSearchThread thread = new MassSearchThread(window);
					synchronized(MassSearchThread.lock3) {
						thread.threadCopy(this);
						thread.setMass(coor);//移動
					}
					Thread t = new Thread(thread);
					t.start();
				}else {
					next.setValue(coordinates);
					setMassFlag=true;
					flag=true;
				}
				end=false;

			}
			if(setMassFlag) {
				synchronized(MassSearchThread.lock3) {
					this.setMass(next);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.setValue(x,y);
	}

	public void setMass(Coordinates coor) {
		this.nowMass.setValue(coor);
	}

	private void threadCopy(MassSearchThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void setResult() {
		synchronized(MassSearchThread.lock2) {
			window.setCanMoveMassResult(nowMass, moveTrajectory);
		}
	}

}
