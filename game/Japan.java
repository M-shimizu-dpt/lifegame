package lifegame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Japan {
	Map<Coordinates,String> prefectureMapping = new HashMap<Coordinates,String>();//駅名、座標
	ArrayList<Property> property = new ArrayList<Property>();//物件情報
	Map<String,ArrayList<Property>> prefectureInfo = new HashMap<String,ArrayList<Property>>();//駅名、駅の物件
	ArrayList<Coordinates> prefectures = new ArrayList<Coordinates>();//駅
	ArrayList<Coordinates> blue = new ArrayList<Coordinates>();//青マス
	ArrayList<Coordinates> red = new ArrayList<Coordinates>();//赤マス
	ArrayList<Coordinates> yellow = new ArrayList<Coordinates>();//黄マス
	ArrayList<Coordinates> shop = new ArrayList<Coordinates>();//カード屋
	Map<Coordinates,ArrayList<Boolean>> railMapping = new HashMap<Coordinates,ArrayList<Boolean>>();//移動可能方向
	public int goal;//目的地の要素番号

	//1マス10
	public Japan() {
		//近畿の駅
		prefectures.add(new Coordinates(1,8));
		prefectures.add(new Coordinates(2,7));
		prefectures.add(new Coordinates(3,9));
		prefectures.add(new Coordinates(3,10));
		prefectures.add(new Coordinates(4,1));
		prefectures.add(new Coordinates(4,4));
		prefectures.add(new Coordinates(4,8));
		prefectures.add(new Coordinates(4,9));
		prefectures.add(new Coordinates(7,7));
		prefectures.add(new Coordinates(5,2));
		prefectures.add(new Coordinates(5,10));
		prefectures.add(new Coordinates(5,12));
		prefectures.add(new Coordinates(5,13));
		prefectures.add(new Coordinates(5,14));
		prefectures.add(new Coordinates(5,15));
		prefectures.add(new Coordinates(5,16));
		prefectures.add(new Coordinates(6,9));
		prefectures.add(new Coordinates(6,10));
		prefectures.add(new Coordinates(6,11));
		prefectures.add(new Coordinates(7,1));
		prefectures.add(new Coordinates(7,10));
		prefectures.add(new Coordinates(8,10));
		prefectures.add(new Coordinates(8,11));
		prefectures.add(new Coordinates(9,6));
		prefectures.add(new Coordinates(9,9));
		prefectures.add(new Coordinates(9,13));
		prefectures.add(new Coordinates(10,6));
		prefectures.add(new Coordinates(10,12));
		prefectures.add(new Coordinates(11,6));
		prefectures.add(new Coordinates(11,11));
		prefectures.add(new Coordinates(11,14));
		prefectures.add(new Coordinates(12,5));
		prefectures.add(new Coordinates(12,10));
		prefectures.add(new Coordinates(13,2));
		prefectures.add(new Coordinates(13,4));
		prefectures.add(new Coordinates(13,5));
		prefectures.add(new Coordinates(14,9));
		prefectures.add(new Coordinates(14,11));
		prefectures.add(new Coordinates(14,12));
		prefectures.add(new Coordinates(14,13));
		prefectures.add(new Coordinates(15,13));

		prefectureMapping.put(prefectures.get(0),"赤穂");
		prefectureMapping.put(prefectures.get(1),"姫路");
		prefectureMapping.put(prefectures.get(2),"明石");
		prefectureMapping.put(prefectures.get(3),"淡路島");
		prefectureMapping.put(prefectures.get(4),"城崎");
		prefectureMapping.put(prefectures.get(5),"福知山");
		prefectureMapping.put(prefectures.get(6),"三田");
		prefectureMapping.put(prefectures.get(7),"神戸");
		prefectureMapping.put(prefectures.get(8),"吹田");
		prefectureMapping.put(prefectures.get(9),"出石");
		prefectureMapping.put(prefectures.get(10),"天保山");
		prefectureMapping.put(prefectures.get(11),"堺");
		prefectureMapping.put(prefectures.get(12),"岸和田");
		prefectureMapping.put(prefectures.get(13),"和歌山");
		prefectureMapping.put(prefectures.get(14),"御坊");
		prefectureMapping.put(prefectures.get(15),"白浜");
		prefectureMapping.put(prefectures.get(16),"大阪");
		prefectureMapping.put(prefectures.get(17),"なんば");
		prefectureMapping.put(prefectures.get(18),"天王寺");
		prefectureMapping.put(prefectures.get(19),"舞鶴");
		prefectureMapping.put(prefectures.get(20),"北浜");
		prefectureMapping.put(prefectures.get(21),"京橋");
		prefectureMapping.put(prefectures.get(22),"鶴橋");
		prefectureMapping.put(prefectures.get(23),"嵐山");
		prefectureMapping.put(prefectures.get(24),"門真");
		prefectureMapping.put(prefectures.get(25),"五條");
		prefectureMapping.put(prefectures.get(26),"京都");
		prefectureMapping.put(prefectures.get(27),"橿原");
		prefectureMapping.put(prefectures.get(28),"祇園");
		prefectureMapping.put(prefectures.get(29),"奈良");
		prefectureMapping.put(prefectures.get(30),"新宮");
		prefectureMapping.put(prefectures.get(31),"大津");
		prefectureMapping.put(prefectures.get(32),"伊賀");
		prefectureMapping.put(prefectures.get(33),"長浜");
		prefectureMapping.put(prefectures.get(34),"彦根");
		prefectureMapping.put(prefectures.get(35),"近江八幡");
		prefectureMapping.put(prefectures.get(36),"四日市");
		prefectureMapping.put(prefectures.get(37),"津");
		prefectureMapping.put(prefectures.get(38),"松阪");
		prefectureMapping.put(prefectures.get(39),"伊勢");
		prefectureMapping.put(prefectures.get(40),"鳥羽");

		//赤穂
		property.add(new Property("塩饅頭屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("忠臣蔵グッズ屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("布海苔養殖場", 30000, 0.01, 0.02, 0.03));
		property.add(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		property.add(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		property.add(new Property("製塩工場", 70000, 0.03, 0.04, 0.05));//3
		//姫路
		property.add(new Property("手延べそうめん屋", 1000, 0.25, 0.3, 0.4));//2
		property.add(new Property("手延べそうめん屋", 1000, 0.25, 0.3, 0.4));//2
		property.add(new Property("焼きアナゴ屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("焼きアナゴ屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("革製品工場", 10000, 0.02, 0.03, 0.04));
		property.add(new Property("石油工場", 800000, 0.01, 0.03, 0.05));
		property.add(new Property("化学工場", 1000000, 0.01, 0.03, 0.05));
		property.add(new Property("製鉄所", 1400000, 0.03, 0.04, 0.05));
		//明石
		property.add(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		property.add(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		property.add(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		property.add(new Property("明石たこ焼き屋", 1000, 0.5, 0.6, 0.7));//4
		property.add(new Property("海鮮市場", 60000, 0.02, 0.03, 0.04));
		//淡路島
		property.add(new Property("引き戻し屋", 500, 1.0, 2.0, 3.0));
		property.add(new Property("玉ねぎ畑", 5000, 0.08, 0.12, 0.16));//2
		property.add(new Property("玉ねぎ畑", 5000, 0.08, 0.12, 0.16));//2
		property.add(new Property("お香工場", 10000, 0.02, 0.04, 0.06));
		property.add(new Property("淡路瓦工場", 30000, 0.01, 0.02, 0.03));
		//城崎
		property.add(new Property("湯上りジュース屋", 500, 0.5, 0.7, 0.8));
		property.add(new Property("カニ寿司屋", 1000, 0.25, 0.3, 0.5));
		property.add(new Property("麦わら細工工房", 3000, 0.25, 0.3, 0.5));
		property.add(new Property("城崎温泉旅館", 100000, 0.02, 0.03, 0.05));
		property.add(new Property("カニ割烹旅館", 200000, 0.05, 0.06, 0.07));
		//福知山
		property.add(new Property("音頭せんべい屋", 1000, 0.5, 1.0, 1.5));
		property.add(new Property("ブドウ園", 5000, 0.05, 0.06, 0.07));
		property.add(new Property("タケノコ林", 8000, 0.05, 0.06, 0.07));
		property.add(new Property("栗のテリーヌ屋", 20000, 0.1, 0.15 ,0.2));
		property.add(new Property("痔の薬品工場", 500000, 0.1, 0.15, 0.2));
		property.add(new Property("磁気テープ工場", 600000, 0.03, 0.04, 0.05));
		property.add(new Property("ビタミン剤工場", 1000000, 0.07, 0.1, 0.13));
		//三田
		property.add(new Property("すき焼きパン屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("丹波栗屋", 10000, 0.02, 0.03, 0.04));//2
		property.add(new Property("丹波栗屋", 10000, 0.02, 0.03, 0.04));//2
		property.add(new Property("三田牛ステーキ屋", 30000, 0.03, 0.04, 0.05));
		property.add(new Property("丹波松茸屋", 50000, 0.07, 0.08, 0.09));
		//神戸
		property.add(new Property("そばめし屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("そばめし屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("フランスパン屋", 10000, 0.03, 0.04, 0.05));//2
		property.add(new Property("フランスパン屋", 10000, 0.03, 0.04, 0.05));//2
		property.add(new Property("中華飯店", 100000, 0.02, 0.03, 0.05));
		property.add(new Property("ステーキハウス", 150000, 0.03, 0.05, 0.07));
		property.add(new Property("ハーバーパーク", 1300000, 0.02, 0.03, 0.04));
		property.add(new Property("南京町中華街", 2000000, 0.02, 0.03, 0.04));
		//吹田
		property.add(new Property("くずきり工場", 20000, 0.03, 0.04, 0.05));
		property.add(new Property("げりぴー工場", 50000, 0.02, 0.03, 0.04));
		property.add(new Property("てっぺん化粧品工場", 60000, 0.02, 0.03, 0.04));
		property.add(new Property("即席めん工場", 600000, 0.07, 0.08, 0.09));
		property.add(new Property("ビール工場", 1000000, 0.04, 0.05, 0.06));
		property.add(new Property("ぞうきん工場", 1200000, 0.02, 0.03, 0.04));
		property.add(new Property("目薬メーカー", 1500000, 0.04, 0.05, 0.06));
		//出石
		property.add(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("皿そば屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("露店トマト店", 1000, 0.8, 1.0, 1.5));
		property.add(new Property("出石白磁工房", 10000, 0.01, 0.02, 0.03));
		//天保山
		property.add(new Property("ロックカフェ", 20000, 0.03, 0.04, 0.05));
		property.add(new Property("大観覧車", 400000, 0.03, 0.04, 0.05));
		property.add(new Property("アウトレットモール", 1000000, 0.04, 0.05, 0.06));
		property.add(new Property("ジンベイザメ水族館", 6000000, 0.1, 0.2, 0.3));
		property.add(new Property("映画ランドジャパン", 35000000, 0.01, 0.02, 0.03));
		//堺
		property.add(new Property("かすうどん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("かすうどん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("くるみ餅屋", 1000, 1.0, 2.0, 3.0));
		property.add(new Property("刃物工場", 20000, 0.03, 0.04, 0.05));
		property.add(new Property("クラッカー菓子工場", 20000, 0.07, 0.08, 0.09));
		property.add(new Property("回転寿司チェーン", 200000, 0.07, 0.08, 0.09));
		property.add(new Property("引越センター", 370000, 0.07, 0.08, 0.09));
		//岸和田
		property.add(new Property("だんじりグッズ屋", 1000, 1.0, 2.0, 3.0));
		property.add(new Property("玉ねぎ畑", 3000, 0.05, 0.06, 0.07));
		property.add(new Property("水ナス畑", 5000, 0.1, 0.11, 0.12));//2
		property.add(new Property("水ナス畑", 5000, 0.1, 0.11, 0.12));//2
		property.add(new Property("顕微鏡ガラス工場", 200000, 0.02, 0.03, 0.04));
		//和歌山
		property.add(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ミカン畑", 5000, 0.05, 0.08, 0.1));//4
		property.add(new Property("富有柿園", 5000, 0.1, 0.15, 0.2));
		property.add(new Property("ネーブル園", 8000, 0.05, 0.08, 0.1));
		property.add(new Property("梅干し林", 10000, 0.05, 0.08, 0.1));//2
		property.add(new Property("梅干し林", 10000, 0.05, 0.08, 0.1));//2
		//御坊
		property.add(new Property("ジャンボかまぼこ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("ピーマン畑", 3000, 0.05, 0.06, 0.07));
		property.add(new Property("ミカン畑", 5000, 0.08, 0.1, 0.12));//2
		property.add(new Property("ミカン畑", 5000, 0.08, 0.1, 0.12));//2
		property.add(new Property("梅林", 8000, 0.05, 0.06, 0.07));//2
		property.add(new Property("梅林", 8000, 0.05, 0.06, 0.07));//2
		property.add(new Property("麻雀牌工場", 100000, 0.1, 0.11, 0.12));
		//白浜
		property.add(new Property("レタス栽培", 3000, 0.05, 0.08, 0.1));
		property.add(new Property("露天風呂", 30000, 0.01, 0.02, 0.03));
		property.add(new Property("アニマルパーク", 80000, 0.01, 0.02, 0.03));
		property.add(new Property("日本旅館", 100000, 0.01, 0.02, 0.03));//2
		property.add(new Property("日本旅館", 100000, 0.01, 0.02, 0.03));//2
		property.add(new Property("ホテル", 300000, 0.02, 0.03, 0.04));//2
		property.add(new Property("ホテル", 300000, 0.02, 0.03, 0.04));//2
		property.add(new Property("ゴルフ場", 500000, 0.01, 0.02, 0.03));
		//大阪
		property.add(new Property("たこ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("たこ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("お好み焼き屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("ねぎ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("ねぎ焼き屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("きつねうどん屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("串カツ屋", 1000, 0.8, 1.0, 1.5));
		property.add(new Property("はりはり鍋屋", 10000, 0.05, 0.06, 0.07));
		property.add(new Property("お笑い劇場", 80000, 0.1, 0.2, 0.3));
		property.add(new Property("製薬会社", 400000, 0.02, 0.03, 0.05));//2
		property.add(new Property("製薬会社", 400000, 0.02, 0.03, 0.05));//2
		property.add(new Property("プロ野球チーム", 770000, 0.04, 0.05, 0.06));
		property.add(new Property("水族館", 1350000, 0.01, 0.02, 0.03));
		property.add(new Property("テレビ局", 4440000, 0.05, 0.06, 0.1));
		property.add(new Property("映画ランドジャパン", 5400000, 0.02, 0.03, 0.04));
		//なんば
		property.add(new Property("たこ焼き屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("豚まん屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("お笑い劇場", 50000, 0.15, 0.2, 0.25));
		property.add(new Property("黒門市場", 160000, 0.06, 0.07, 0.08));
		property.add(new Property("お笑い興業", 500000, 0.1, 0.12, 0.14));
		property.add(new Property("日本橋電気街", 1000000, 0.03, 0.04, 0.05));
		property.add(new Property("老舗デパート", 5600000, 0.04, 0.05, 0.06));
		//天王寺
		property.add(new Property("将棋場", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("シチューうどん屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("串カツ屋", 1000, 0.8, 1.0, 1.2));
		property.add(new Property("ヨーグルトケーキ屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("動物園", 400000, 0.01, 0.02, 0.03));
		property.add(new Property("仰天閣タワー", 800000, 0.02, 0.03, 0.04));
		property.add(new Property("あべのルルカス", 13000000, 0.02, 0.03, 0.04));
		//舞鶴
		property.add(new Property("肉じゃが屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("肉じゃが屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("軍港カレー", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("万願寺唐辛子畑", 3000, 0.05, 0.08, 0.1));
		property.add(new Property("こっぺ蟹料理屋", 20000, 0.05, 0.06, 0.07));//2
		property.add(new Property("こっぺ蟹料理屋", 20000, 0.05, 0.06, 0.07));//2
		//北浜
		property.add(new Property("あったらいいな製薬", 350000, 0.06, 0.08, 0.1));
		property.add(new Property("アルバム制作会社", 660000, 0.04, 0.06, 0.08));
		property.add(new Property("シノノギ製薬", 2000000, 0.04, 0.06, 0.08));
		property.add(new Property("紡績工場", 2200000, 0.01, 0.02, 0.03));
		property.add(new Property("繊維メーカー", 2400000, 0.01, 0.02, 0.03));
		property.add(new Property("スタミナ製薬", 5000000, 0.02, 0.03, 0.4));
		property.add(new Property("ビタミンA製薬", 15000000, 0.06, 0.08, 0.1));
		//京橋
		property.add(new Property("フランクフルト屋", 1000, 0.8, 1.0, 1.3));
		property.add(new Property("つかみ寿司屋", 10000, 0.1, 0.2, 0.3));
		property.add(new Property("お笑い劇場", 40000, 0.1, 0.2, 0.3));
		property.add(new Property("テレビ局1", 100000, 0.07, 0.1, 0.13));
		property.add(new Property("テレビ局2", 1000000, 0.1, 0.15, 0.2));
		//鶴橋
		property.add(new Property("チヂミ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("トッポギ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("韓国キムチ屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("韓国焼き肉屋", 3000, 0.8, 1.0, 1.2));//2
		property.add(new Property("韓国焼き肉屋", 3000, 0.8, 1.0, 1.2));//2
		//嵐山
		property.add(new Property("湯豆腐料理屋", 10000, 0.03, 0.04, 0.05));//2
		property.add(new Property("湯豆腐料理屋", 10000, 0.03, 0.04, 0.05));//2
		property.add(new Property("高級料亭", 50000, 0.1, 0.2, 0.3));//2
		property.add(new Property("高級料亭", 50000, 0.1, 0.2, 0.3));//2
		property.add(new Property("料亭億兆", 600000, 0.07, 0.1, 0.12));
		//門真
		property.add(new Property("クワイ園", 10000, 0.04, 0.08, 0.1));
		property.add(new Property("自動車部品工場", 60000, 0.03, 0.05, 0.07));
		property.add(new Property("経営の神様記念館", 240000, 0.1, 0.2, 0.3));
		property.add(new Property("魔法瓶工場", 400000, 0.03, 0.04, 0.05));
		property.add(new Property("ジェネリック薬品", 470000, 0.04, 0.05, 0.06));
		property.add(new Property("電池充電器工場", 800000, 0.03, 0.04, 0.05));
		property.add(new Property("ポニョソニック電機", 40000000, 0.1, 0.2, 0.3));
		//五條
		property.add(new Property("柿の葉寿司屋", 1000, 0.8, 1.5, 2.0));//2
		property.add(new Property("柿の葉寿司屋", 1000, 0.8, 1.5, 2.0));//2
		property.add(new Property("富有柿園", 30000, 0.1, 0.2, 0.3));//2
		property.add(new Property("富有柿園", 30000, 0.1, 0.2, 0.3));//2
		property.add(new Property("柿ワイン工場", 50000, 0.04, 0.06, 0.08));
		//京都
		property.add(new Property("油とり紙屋", 5000, 0.8, 1.0, 2.0));//2
		property.add(new Property("油とり紙屋", 5000, 0.8, 1.0, 2.0));//2
		property.add(new Property("麩まんじゅう屋", 10000, 0.05, 0.06, 0.07));
		property.add(new Property("老舗コーヒー屋", 10000, 0.08, 0.09, 0.1));
		property.add(new Property("あぶり餅屋", 10000, 0.1, 0.15, 0.2));
		property.add(new Property("生八つ橋屋", 30000, 0.1, 0.15, 0.2));//2
		property.add(new Property("生八つ橋屋", 30000, 0.1, 0.15, 0.2));//2
		property.add(new Property("湯豆腐料理屋", 80000, 0.05, 0.1, 0.15));
		property.add(new Property("錦上市場", 250000, 0.04, 0.08, 0.1));
		property.add(new Property("料亭億兆", 1000000, 0.05, 0.08, 0.15));
		//橿原
		property.add(new Property("柿の葉寿司", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("石舞台グッズ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("牛乳スープ鍋屋", 1000, 1.0, 1.5, 2.0));
		property.add(new Property("富有柿園", 10000, 0.1, 0.2, 0.3));//2
		property.add(new Property("富有柿園", 10000, 0.1, 0.2, 0.3));//2
		//祇園
		property.add(new Property("天然かき氷屋", 10000, 0.1, 0.2, 0.3));
		property.add(new Property("帆布工場", 10000, 0.07, 0.1, 0.15));
		property.add(new Property("葛きり屋", 20000, 0.1, 0.15, 0.2));
		property.add(new Property("京の米料亭", 20000, 0.15, 0.2, 0.3));
		property.add(new Property("生麩屋", 30000, 0.1, 0.2, 0.3));
		property.add(new Property("鮨割烹", 30000, 0.15, 0.2, 0.3));
		property.add(new Property("イタリアン料理店", 40000, 0.15, 0.2, 0.3));
		//奈良
		property.add(new Property("鹿せんべい屋", 1000, 0.25, 0.3, 0.4));//2
		property.add(new Property("鹿せんべい屋", 1000, 0.25, 0.3, 0.4));//2
		property.add(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("柿の葉寿司屋", 1000, 0.5, 0.6, 0.7));//3
		property.add(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		property.add(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		property.add(new Property("富有柿園", 8000, 0.1, 0.15, 0.2));//3
		//新宮
		property.add(new Property("さんま寿司屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("さんま寿司屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("めはり寿司屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("めはり寿司屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		property.add(new Property("ヨシノスギ林", 10000, 0.05, 0.08, 0.1));//4
		//大津
		property.add(new Property("走り餅屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("しじみめし屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("しじみめし屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("和菓子屋", 10000, 0.02, 0.04, 0.06));
		property.add(new Property("産業用センサー工場", 280000, 0.02, 0.04, 0.06));
		property.add(new Property("バイオ研究所", 900000, 0.03, 0.04, 0.05));
		property.add(new Property("液晶用ガラス工場", 3200000, 0.04, 0.05, 0.06));
		//伊賀
		property.add(new Property("手裏剣せんべい屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("堅焼きせんべい屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("松尾芭蕉グッズ屋", 3000, 0.1, 0.15, 0.2));
		property.add(new Property("忍者屋敷", 20000, 0.03, 0.04, 0.05));
		property.add(new Property("伊賀焼き物工房", 40000, 0.02, 0.04, 0.06));
		property.add(new Property("伊賀牛屋", 60000, 0.03, 0.04, 0.05));//2
		property.add(new Property("伊賀牛屋", 60000, 0.03, 0.04, 0.05));//2
		//長浜
		property.add(new Property("焼きサバそうめん屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("豊臣秀吉グッズ屋", 3000, 0.8, 1.0, 2.0));
		property.add(new Property("オルゴール館", 30000, 0.02, 0.03, 0.04));
		property.add(new Property("ガラス工房", 50000, 0.03, 0.04, 0.05));
		property.add(new Property("フィギュア博物館", 80000, 0.05, 0.06, 0.07));
		property.add(new Property("鉄道記念館", 510000, 0.02, 0.03, 0.04));
		property.add(new Property("黒壁の街並み", 700000, 0.02, 0.03, 0.05));
		//彦根
		property.add(new Property("ひこっしーグッズ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("和ローソク屋", 3000, 0.5, 0.6, 0.7));//2
		property.add(new Property("和ローソク屋", 3000, 0.5, 0.6, 0.7));//2
		property.add(new Property("赤かぶら漬け屋", 3000, 0.5, 0.6, 0.7));
		property.add(new Property("塩すき焼き屋", 10000, 0.01, 0.02, 0.03));
		//近江八幡
		property.add(new Property("でっちようかん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("でっちようかん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("赤こんにゃく屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("赤こんにゃく屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("バウムクーヘン屋", 10000, 0.1, 0.15, 0.2));
		property.add(new Property("近江牛屋", 70000, 0.05, 0.06, 0.07));//2
		property.add(new Property("近江牛屋", 70000, 0.05, 0.06, 0.07));//2
		property.add(new Property("製薬会社", 120000, 0.02, 0.03, 0.04));
		//四日市
		property.add(new Property("とんてき屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("点火プラグ工場", 750000, 0.03, 0.04, 0.05));
		property.add(new Property("食品素材工場", 770000, 0.03, 0.04, 0.05));
		property.add(new Property("物流倉庫会社", 840000, 0.03, 0.04, 0.05));
		property.add(new Property("板ガラス工場", 1000000, 0.03, 0.04, 0.05));
		property.add(new Property("石油精製工場", 2000000, 0.02, 0.03, 0.04));//2
		property.add(new Property("石油精製工場", 2000000, 0.02, 0.03, 0.04));//2
		//津
		property.add(new Property("福引せんべい屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("巨大餃子屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("うなぎ屋", 1000, 0.5, 0.6, 0.7));
		property.add(new Property("天むす屋", 1000, 0.8, 1.0, 2.0));
		property.add(new Property("ベビーラーメン工場", 180000, 0.02, 0.03, 0.04));
		property.add(new Property("肉まんあんまん工場", 220000, 0.05, 0.06, 0.07));
		property.add(new Property("造船所", 300000, 0.03, 0.04, 0.05));
		//松阪
		property.add(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		property.add(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		property.add(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		property.add(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		property.add(new Property("松阪牛屋", 80000, 0.03, 0.04, 0.05));//5
		//伊勢
		property.add(new Property("伊勢うどん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("伊勢うどん屋", 1000, 0.5, 0.6, 0.7));//2
		property.add(new Property("てこね寿司屋", 5000, 0.8, 1.0, 1.5));//2
		property.add(new Property("てこね寿司屋", 5000, 0.8, 1.0, 1.5));//2
		property.add(new Property("ふくふく餅屋", 10000, 0.04, 0.05, 0.06));
		property.add(new Property("戦国パーク", 100000, 0.01, 0.02, 0.03));
		property.add(new Property("おまいり横丁", 2000000, 0.02, 0.03, 0.04));
		//鳥羽
		property.add(new Property("真珠養殖工場", 50000, 0.03, 0.05, 0.07));//2
		property.add(new Property("真珠養殖工場", 50000, 0.03, 0.05, 0.07));//2
		property.add(new Property("黒鯛漁", 70000, 0.04, 0.05, 0.6));
		property.add(new Property("牡蠣養殖工場", 80000, 0.05, 0.06, 0.07));//2
		property.add(new Property("牡蠣養殖工場", 80000, 0.05, 0.06, 0.07));//2


		prefectureInfo.put("赤穂",new ArrayList<Property>());
		for(int i=0;i<6;i++) {
			prefectureInfo.get("赤穂").add(property.get(i));
		}
		prefectureInfo.put("姫路",new ArrayList<Property>());
		for(int i=6;i<14;i++) {
			prefectureInfo.get("姫路").add(property.get(i));
		}
		prefectureInfo.put("明石",new ArrayList<Property>());
		for(int i=14;i<19;i++) {
			prefectureInfo.get("明石").add(property.get(i));
		}
		prefectureInfo.put("淡路島",new ArrayList<Property>());
		for(int i=19;i<24;i++) {
			prefectureInfo.get("淡路島").add(property.get(i));
		}
		prefectureInfo.put("城崎",new ArrayList<Property>());
		for(int i=24;i<29;i++) {
			prefectureInfo.get("城崎").add(property.get(i));
		}
		prefectureInfo.put("福知山",new ArrayList<Property>());
		for(int i=29;i<36;i++) {
			prefectureInfo.get("福知山").add(property.get(i));
		}
		prefectureInfo.put("三田",new ArrayList<Property>());
		for(int i=36;i<41;i++) {
			prefectureInfo.get("三田").add(property.get(i));
		}
		prefectureInfo.put("神戸",new ArrayList<Property>());
		for(int i=41;i<49;i++) {
			prefectureInfo.get("神戸").add(property.get(i));
		}
		prefectureInfo.put("吹田",new ArrayList<Property>());
		for(int i=49;i<56;i++) {
			prefectureInfo.get("吹田").add(property.get(i));
		}
		prefectureInfo.put("出石",new ArrayList<Property>());
		for(int i=56;i<61;i++) {
			prefectureInfo.get("出石").add(property.get(i));
		}
		prefectureInfo.put("天保山",new ArrayList<Property>());
		for(int i=61;i<66;i++) {
			prefectureInfo.get("天保山").add(property.get(i));
		}
		prefectureInfo.put("堺",new ArrayList<Property>());
		for(int i=66;i<73;i++) {
			prefectureInfo.get("堺").add(property.get(i));
		}
		prefectureInfo.put("岸和田",new ArrayList<Property>());
		for(int i=73;i<78;i++) {
			prefectureInfo.get("岸和田").add(property.get(i));
		}
		prefectureInfo.put("和歌山",new ArrayList<Property>());
		for(int i=78;i<86;i++) {
			prefectureInfo.get("和歌山").add(property.get(i));
		}
		prefectureInfo.put("御坊",new ArrayList<Property>());
		for(int i=86;i<93;i++) {
			prefectureInfo.get("御坊").add(property.get(i));
		}
		prefectureInfo.put("白浜",new ArrayList<Property>());
		for(int i=93;i<101;i++) {
			prefectureInfo.get("白浜").add(property.get(i));
		}
		prefectureInfo.put("大阪",new ArrayList<Property>());
		for(int i=101;i<116;i++) {
			prefectureInfo.get("大阪").add(property.get(i));
		}
		prefectureInfo.put("なんば",new ArrayList<Property>());
		for(int i=116;i<123;i++) {
			prefectureInfo.get("なんば").add(property.get(i));
		}
		prefectureInfo.put("天王寺",new ArrayList<Property>());
		for(int i=123;i<130;i++) {
			prefectureInfo.get("天王寺").add(property.get(i));
		}
		prefectureInfo.put("舞鶴",new ArrayList<Property>());
		for(int i=130;i<138;i++) {
			prefectureInfo.get("舞鶴").add(property.get(i));
		}
		prefectureInfo.put("北浜",new ArrayList<Property>());
		for(int i=138;i<145;i++) {
			prefectureInfo.get("北浜").add(property.get(i));
		}
		prefectureInfo.put("京橋",new ArrayList<Property>());
		for(int i=145;i<150;i++) {
			prefectureInfo.get("京橋").add(property.get(i));
		}
		prefectureInfo.put("鶴橋",new ArrayList<Property>());
		for(int i=150;i<155;i++) {
			prefectureInfo.get("鶴橋").add(property.get(i));
		}
		prefectureInfo.put("嵐山",new ArrayList<Property>());
		for(int i=155;i<160;i++) {
			prefectureInfo.get("嵐山").add(property.get(i));
		}
		prefectureInfo.put("門真",new ArrayList<Property>());
		for(int i=160;i<167;i++) {
			prefectureInfo.get("門真").add(property.get(i));
		}
		prefectureInfo.put("五條",new ArrayList<Property>());
		for(int i=167;i<172;i++) {
			prefectureInfo.get("五條").add(property.get(i));
		}
		prefectureInfo.put("京都",new ArrayList<Property>());
		for(int i=172;i<182;i++) {
			prefectureInfo.get("京都").add(property.get(i));
		}
		prefectureInfo.put("橿原",new ArrayList<Property>());
		for(int i=182;i<187;i++) {
			prefectureInfo.get("橿原").add(property.get(i));
		}
		prefectureInfo.put("祇園",new ArrayList<Property>());
		for(int i=187;i<194;i++) {
			prefectureInfo.get("祇園").add(property.get(i));
		}
		prefectureInfo.put("奈良",new ArrayList<Property>());
		for(int i=194;i<202;i++) {
			prefectureInfo.get("奈良").add(property.get(i));
		}
		prefectureInfo.put("新宮",new ArrayList<Property>());
		for(int i=202;i<210;i++) {
			prefectureInfo.get("新宮").add(property.get(i));
		}
		prefectureInfo.put("大津",new ArrayList<Property>());
		for(int i=210;i<217;i++) {
			prefectureInfo.get("大津").add(property.get(i));
		}
		prefectureInfo.put("伊賀",new ArrayList<Property>());
		for(int i=217;i<224;i++) {
			prefectureInfo.get("伊賀").add(property.get(i));
		}
		prefectureInfo.put("長浜",new ArrayList<Property>());
		for(int i=224;i<231;i++) {
			prefectureInfo.get("長浜").add(property.get(i));
		}
		prefectureInfo.put("彦根",new ArrayList<Property>());
		for(int i=231;i<236;i++) {
			prefectureInfo.get("彦根").add(property.get(i));
		}
		prefectureInfo.put("近江八幡",new ArrayList<Property>());
		for(int i=236;i<244;i++) {
			prefectureInfo.get("近江八幡").add(property.get(i));
		}
		prefectureInfo.put("四日市",new ArrayList<Property>());
		for(int i=244;i<251;i++) {
			prefectureInfo.get("四日市").add(property.get(i));
		}
		prefectureInfo.put("津",new ArrayList<Property>());
		for(int i=251;i<258;i++) {
			prefectureInfo.get("津").add(property.get(i));
		}
		prefectureInfo.put("松阪",new ArrayList<Property>());
		for(int i=258;i<263;i++) {
			prefectureInfo.get("松阪").add(property.get(i));
		}
		prefectureInfo.put("伊勢",new ArrayList<Property>());
		for(int i=263;i<270;i++) {
			prefectureInfo.get("伊勢").add(property.get(i));
		}
		prefectureInfo.put("鳥羽",new ArrayList<Property>());
		for(int i=270;i<275;i++) {
			prefectureInfo.get("鳥羽").add(property.get(i));
		}

		railMapping.put(prefectures.get(0),getBoolList(true,false,false,true));
		railMapping.put(prefectures.get(1),getBoolList(false,true,true,true));
		railMapping.put(prefectures.get(2),getBoolList(true,true,false,true));
		railMapping.put(prefectures.get(3),getBoolList(true,false,false,false));
		railMapping.put(prefectures.get(4),getBoolList(false,true,true,false));
		railMapping.put(prefectures.get(5),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(6),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(7),getBoolList(false,false,true,true));
		railMapping.put(prefectures.get(8),getBoolList(false,true,false,true));
		railMapping.put(prefectures.get(9),getBoolList(false,false,true,false));
		railMapping.put(prefectures.get(10),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(11),getBoolList(false,true,false,true));
		railMapping.put(prefectures.get(12),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(13),getBoolList(true,true,false,true));
		railMapping.put(prefectures.get(14),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(15),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(16),getBoolList(true,true,true,true));
		railMapping.put(prefectures.get(17),getBoolList(true,true,false,true));
		railMapping.put(prefectures.get(18),getBoolList(true,true,true,true));
		railMapping.put(prefectures.get(19),getBoolList(false,true,true,true));
		railMapping.put(prefectures.get(20),getBoolList(false,false,true,true));
		railMapping.put(prefectures.get(21),getBoolList(true,true,true,true));
		railMapping.put(prefectures.get(22),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(23),getBoolList(true,false,false,true));
		railMapping.put(prefectures.get(24),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(25),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(26),getBoolList(true,true,true,true));
		railMapping.put(prefectures.get(27),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(28),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(29),getBoolList(false,false,true,false));
		railMapping.put(prefectures.get(30),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(31),getBoolList(false,false,true,true));
		railMapping.put(prefectures.get(32),getBoolList(false,false,true,true));
		railMapping.put(prefectures.get(33),getBoolList(false,true,true,false));
		railMapping.put(prefectures.get(34),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(35),getBoolList(true,false,true,false));
		railMapping.put(prefectures.get(36),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(37),getBoolList(true,true,true,false));
		railMapping.put(prefectures.get(38),getBoolList(true,true,false,false));
		railMapping.put(prefectures.get(39),getBoolList(true,false,false,true));
		railMapping.put(prefectures.get(40),getBoolList(false,false,true,false));

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


		railMapping.put(blue.get(0),getBoolList(false,true,true,true));
		railMapping.put(blue.get(1),getBoolList(true,false,true,false));
		railMapping.put(blue.get(2),getBoolList(false,false,true,true));
		railMapping.put(blue.get(3),getBoolList(false,true,false,true));
		railMapping.put(blue.get(4),getBoolList(false,true,true,false));
		railMapping.put(blue.get(5),getBoolList(true,false,true,true));
		railMapping.put(blue.get(6),getBoolList(false,true,false,true));
		railMapping.put(blue.get(7),getBoolList(true,false,true,true));
		railMapping.put(blue.get(8),getBoolList(false,true,false,true));
		railMapping.put(blue.get(9),getBoolList(true,false,true,false));
		railMapping.put(blue.get(10),getBoolList(false,false,true,true));
		railMapping.put(blue.get(11),getBoolList(true,false,true,false));
		railMapping.put(blue.get(12),getBoolList(false,true,false,true));
		railMapping.put(blue.get(13),getBoolList(false,true,false,true));
		railMapping.put(blue.get(14),getBoolList(true,true,true,false));
		railMapping.put(blue.get(15),getBoolList(true,true,false,false));
		railMapping.put(blue.get(16),getBoolList(false,true,true,false));
		railMapping.put(blue.get(17),getBoolList(true,false,true,false));
		railMapping.put(blue.get(18),getBoolList(true,false,true,false));
		railMapping.put(blue.get(19),getBoolList(true,true,false,true));
		railMapping.put(blue.get(20),getBoolList(false,false,true,true));
		railMapping.put(blue.get(21),getBoolList(false,true,true,false));

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

		railMapping.put(red.get(0),getBoolList(true,true,false,true));
		railMapping.put(red.get(1),getBoolList(true,false,true,false));
		railMapping.put(red.get(2),getBoolList(false,false,true,true));
		railMapping.put(red.get(3),getBoolList(false,true,true,false));
		railMapping.put(red.get(4),getBoolList(true,true,false,true));
		railMapping.put(red.get(5),getBoolList(false,false,true,true));
		railMapping.put(red.get(6),getBoolList(true,false,false,true));
		railMapping.put(red.get(7),getBoolList(true,false,true,false));
		railMapping.put(red.get(8),getBoolList(true,false,true,true));
		railMapping.put(red.get(9),getBoolList(true,false,true,false));
		railMapping.put(red.get(10),getBoolList(true,false,false,true));
		railMapping.put(red.get(11),getBoolList(false,false,true,true));
		railMapping.put(red.get(12),getBoolList(false,false,true,true));
		railMapping.put(red.get(13),getBoolList(false,true,false,true));
		railMapping.put(red.get(14),getBoolList(true,false,true,false));
		railMapping.put(red.get(15),getBoolList(false,true,true,false));
		railMapping.put(red.get(16),getBoolList(true,false,true,true));
		railMapping.put(red.get(17),getBoolList(true,true,false,false));
		railMapping.put(red.get(18),getBoolList(true,true,false,false));
		railMapping.put(red.get(19),getBoolList(true,true,false,false));
		railMapping.put(red.get(20),getBoolList(true,true,false,true));
		railMapping.put(red.get(21),getBoolList(false,true,false,true));
		railMapping.put(red.get(22),getBoolList(false,true,false,true));
		railMapping.put(red.get(23),getBoolList(false,false,true,true));
		railMapping.put(red.get(24),getBoolList(false,true,false,true));
		railMapping.put(red.get(25),getBoolList(true,false,true,false));
		railMapping.put(red.get(26),getBoolList(true,true,false,false));
		railMapping.put(red.get(27),getBoolList(true,true,false,false));
		railMapping.put(red.get(28),getBoolList(true,true,false,false));
		railMapping.put(red.get(29),getBoolList(true,false,true,true));
		railMapping.put(red.get(30),getBoolList(false,true,false,true));
		railMapping.put(red.get(31),getBoolList(false,false,true,true));

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

		railMapping.put(yellow.get(0),getBoolList(true,true,false,true));
		railMapping.put(yellow.get(1),getBoolList(true,true,false,true));
		railMapping.put(yellow.get(2),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(3),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(4),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(5),getBoolList(true,false,false,true));
		railMapping.put(yellow.get(6),getBoolList(false,false,false,true));
		railMapping.put(yellow.get(7),getBoolList(false,true,false,true));
		railMapping.put(yellow.get(8),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(9),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(10),getBoolList(false,true,false,true));
		railMapping.put(yellow.get(11),getBoolList(true,false,true,false));
		railMapping.put(yellow.get(12),getBoolList(false,false,true,true));
		railMapping.put(yellow.get(13),getBoolList(false,false,true,true));//8,1
		railMapping.put(yellow.get(14),getBoolList(false,true,true,false));
		railMapping.put(yellow.get(15),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(16),getBoolList(false,false,true,true));//9,1
		railMapping.put(yellow.get(17),getBoolList(false,true,true,true));
		railMapping.put(yellow.get(18),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(19),getBoolList(false,true,false,true));
		railMapping.put(yellow.get(20),getBoolList(true,true,true,false));
		railMapping.put(yellow.get(21),getBoolList(true,true,false,false));
		railMapping.put(yellow.get(22),getBoolList(true,true,true,true));//10,10
		railMapping.put(yellow.get(23),getBoolList(false,true,true,true));
		railMapping.put(yellow.get(24),getBoolList(false,true,false,false));
		railMapping.put(yellow.get(25),getBoolList(true,true,true,false));
		railMapping.put(yellow.get(26),getBoolList(false,false,false,true));
		railMapping.put(yellow.get(27),getBoolList(false,true,false,true));

		//店
		shop.add(new Coordinates());

	}

	public synchronized Boolean contains(int x,int y) {
		Boolean flag=false;
		ArrayList<ArrayList<Coordinates>> list = new ArrayList<ArrayList<Coordinates>>();
		list.add(prefectures);
		list.add(blue);
		list.add(red);
		list.add(yellow);
		list.add(shop);
		for(ArrayList<Coordinates> coordinates:list) {
			for(Coordinates coor:coordinates) {
				if(coor.x==x && coor.y==y) {
					flag=true;
				}
			}
		}
		return flag;
	}

	public Boolean prefectureContains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:prefectures) {
			if(coor.x==x && coor.y==y) {
				flag=true;
			}
		}
		return flag;
	}
	public Boolean blueContains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:blue) {
			if(coor.x==x && coor.y==y) {
				flag=true;
			}
		}
		return flag;
	}
	public Boolean redContains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:red) {
			if(coor.x==x && coor.y==y) {
				flag=true;
			}
		}
		return flag;
	}
	public Boolean yellowContains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:yellow) {
			if(coor.x==x && coor.y==y) {
				flag=true;
			}
		}
		return flag;
	}
	public Boolean shopContains(int x,int y) {
		Boolean flag=false;
		for(Coordinates coor:shop) {
			if(coor.x==x && coor.y==y) {
				flag=true;
			}
		}
		return flag;
	}

	public void initGoal() {
		int x=0,y=0;
		while(!(this.prefectureContains(x, y) && (x!=6 || y!=9))) {//スタート地点がゴールにならない為
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		goal = getIndexOfPrefecture(x,y);
		//System.out.println("目的地："+prefectureMapping.get(prefectures.get(goal))+"x:"+x+"  y:"+y);
	}

	public void changeGoal() {
		int x=0,y=0;
		while((!this.prefectureContains(x, y)) || goal==getIndexOfPrefecture(x,y)) {
			x=(int)(Math.random()*Math.random()*100.0)%16;
			y=(int)(Math.random()*Math.random()*100.0)%17;
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {

			}
		}
		goal = getIndexOfPrefecture(x,y);
		//System.out.println("目的地："+prefectureMapping.get(prefectures.get(goal))+"x:"+x+"  y:"+y);
	}

	public int getIndexOf(int x,int y) {
		int result;
		result=getIndexOfPrefecture(x,y);
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

	public int getIndexOfPrefecture(int x,int y){
		for(int list=0;list<this.prefectures.size();list++) {
			if(this.prefectures.get(list).x == x && this.prefectures.get(list).y == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	public int getIndexOfBlue(int x,int y){
		for(int list=0;list<this.blue.size();list++) {
			if(this.blue.get(list).x == x && this.blue.get(list).y == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	public int getIndexOfRed(int x,int y){
		for(int list=0;list<this.red.size();list++) {
			if(this.red.get(list).x == x && this.red.get(list).y == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	public int getIndexOfYellow(int x,int y){
		for(int list=0;list<this.yellow.size();list++) {
			if(this.yellow.get(list).x == x && this.yellow.get(list).y == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}
	public int getIndexOfShop(int x,int y){
		for(int list=0;list<this.shop.size();list++) {
			if(this.shop.get(list).x == x && this.shop.get(list).y == y) {//駅の座標が来たら
				return list;
			}
		}
		return -1;
	}

	private ArrayList<Boolean> getBoolList(Boolean top,Boolean bottom,Boolean left,Boolean right){
		ArrayList<Boolean> rail = new ArrayList<Boolean>();
		rail.add(left);
		rail.add(right);
		rail.add(top);
		rail.add(bottom);
		return rail;
	}

	public ArrayList<Boolean> getVector(int x,int y,int size){
		for(int list=0;list<prefectures.size();list++) {
			if(prefectures.get(list).x == x/size && prefectures.get(list).y == y/size) {//駅の座標が来たら
				return railMapping.get(prefectures.get(list));
			}
		}
		for(int list=0;list<blue.size();list++) {
			if(blue.get(list).x == x/size && blue.get(list).y == y/size) {
				return railMapping.get(blue.get(list));
			}
		}
		for(int list=0;list<red.size();list++) {
			if(red.get(list).x == x/size && red.get(list).y == y/size) {
				return railMapping.get(red.get(list));
			}
		}
		for(int list=0;list<yellow.size();list++) {
			if(yellow.get(list).x == x/size && yellow.get(list).y == y/size) {
				return railMapping.get(yellow.get(list));
			}
		}
		return null;
	}

}

class MultiThread implements Runnable{
	public ArrayList<Coordinates> moveTrajectory = new ArrayList<Coordinates>();//移動の軌跡
	public static int savecount=0;
	public static final Object lock1 = new Object();
	public static final Object lock2 = new Object();
	public static final Object lock3 = new Object();
	public static final Object lock4 = new Object();
	private int count=0;
	private Coordinates nowMass=new Coordinates();
	private Window window;

	public MultiThread(Window window) {
		this.window=window;
	}


	//駅マス以外での探索が鈍いように感じる
	public void run() {
		//来た方向以外に2方向以上に分岐している場合、新しくThreadを立ち上げて
		//内容をコピーした上で自分とは別方向に移動させる。
		ArrayList<Boolean> list;
		ArrayList<Coordinates> vList = new ArrayList<Coordinates>();
		for(int i=0;i<4;i++) {
			vList.add(new Coordinates());
		}
		vList.get(0).x=-1;vList.get(0).y=0;
		vList.get(1).x=1;vList.get(1).y=0;
		vList.get(2).x=0;vList.get(2).y=-1;
		vList.get(3).x=0;vList.get(3).y=1;

		int i;
		boolean flag;
		boolean end;
		boolean setMassFlag;
		int x,y;
		int goalX=window.japan.prefectures.get(window.japan.goal).x;
		int goalY=window.japan.prefectures.get(window.japan.goal).y;
		while(true) {
			x=0;y=0;
			setMassFlag=false;
			end=true;
			flag=false;
			if(count>Window.count) {//現時点での最短よりも多く移動しているThreadは閉じる(最短のはずのthreadも閉じてる？)
				//System.out.println("not shorter");
				break;
			}
			if(savecount>1000) {
				//System.out.println("all killed");
				break;
			}
			if(count>35) {
				//System.out.println("count over");
				break;
			}
			if(System.currentTimeMillis()-Window.time>=200) {
				//System.out.println("time out");
				break;
			}

			count++;
			savecount++;
			i=0;
			//なぜlistがnullになるのか…？？
			//やっぱりベクトルが異常値を取ってしまう
			// →1インスタンス内の複数のスレッドが同時にアクセスしようとした場合にロックが可能なので
			//	 Thread型のインスタンスを1つに絞りたい（インスタンスを複数作成している為）
			synchronized(MultiThread.lock1) {
				list = window.japan.getVector(this.nowMass.x,this.nowMass.y,1);
			}
			if(list==null) {
				//System.out.println("list_null");
				break;
			}
			moveTrajectory.add(new Coordinates(nowMass.x,nowMass.y));
			if((goalX==this.nowMass.x && goalY==this.nowMass.y)){
				goal();
				//System.out.println("正常終了goal  count:"+this.count+"   now.x:"+this.nowMass.x+"  now.y:"+this.nowMass.y);
				break;
			}
			for(Boolean bool:list) {
				if(bool) {
					boolean conti=false;
					for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
						int vx=vList.get(i).x;
						int vy=vList.get(i).y;
						synchronized(MultiThread.lock4) {
							if(!window.japan.contains(this.nowMass.x+vx,this.nowMass.y+vy)) {
								vx*=2;
								vy*=2;
							}
						}
						if((moveTrajectory.get(j).x == this.nowMass.x+vx) &&
								(moveTrajectory.get(j).y == this.nowMass.y+vy)) {//同じ場合、1つ前のmoveTrajectoryを削除
							i++;
							conti=true;
							break;
						}
					}
					if(conti) {
						continue;
					}
					//2マス移動(競合の可能性)
					synchronized(MultiThread.lock4) {
						if(!window.japan.contains(this.nowMass.x+vList.get(i).x,this.nowMass.y+vList.get(i).y)) {
							vList.get(i).x*=2;
							vList.get(i).y*=2;
						}
					}
					if(flag) {
						//Threadを立ち上げる
						MultiThread thread = new MultiThread(window);
						synchronized(MultiThread.lock3) {
							thread.threadCopy(this);
							thread.setMass(this.nowMass.x+vList.get(i).x, this.nowMass.y+vList.get(i).y);//移動
						}
						Thread t = new Thread(thread);
						t.start();
					}else {
						x=this.nowMass.x+vList.get(i).x;
						y=this.nowMass.y+vList.get(i).y;
						setMassFlag=true;
						flag=true;
					}
					end=false;
				}
				i++;
			}
			if(setMassFlag) {
				synchronized(MultiThread.lock3) {
					this.setMass(x, y);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				//System.out.println("正常終了end");
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.x=x;
		this.nowMass.y=y;
	}

	private void threadCopy(MultiThread original) {
		this.count=original.count;
		this.moveTrajectory.addAll(original.moveTrajectory);
	}

	private void goal() {
		synchronized(MultiThread.lock2) {
			window.setSearchResult(count,moveTrajectory);
		}
	}
}

//上のスレッドをimplements的なことをしてgoal()だけoverrideすべき？
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
		ArrayList<Boolean> list;
		ArrayList<Coordinates> vList = new ArrayList<Coordinates>();
		for(int i=0;i<4;i++) {
			vList.add(new Coordinates());
		}
		vList.get(0).x=-1;vList.get(0).y=0;
		vList.get(1).x=1;vList.get(1).y=0;
		vList.get(2).x=0;vList.get(2).y=-1;
		vList.get(3).x=0;vList.get(3).y=1;

		int i;
		boolean flag;
		boolean end;
		boolean setMassFlag;
		int x,y;
		while(true) {
			x=0;y=0;
			setMassFlag=false;
			end=true;
			flag=false;
			if(count>Window.count) {//現時点での最短よりも多く移動しているThreadは閉じる(最短のはずのthreadも閉じてる？)
				//System.out.println("not shorter");
				break;
			}
			if(savecount>1000) {
				//System.out.println("all killed");
				break;
			}
			if(count>15) {
				//System.out.println("count over");
				break;
			}
			if(System.currentTimeMillis()-Window.time>=200) {
				//System.out.println("time out");
				break;
			}
			if(window.japan.prefectureContains(this.nowMass.x,this.nowMass.y)) {
				//System.out.println("goal");
				setResult();
				break;
			}

			count++;
			savecount++;
			i=0;
			//なぜlistがnullになるのか…？？
			//やっぱりベクトルが異常値を取ってしまう
			// →1インスタンス内の複数のスレッドが同時にアクセスしようとした場合にロックが可能なので
			//	 Thread型のインスタンスを1つに絞りたい（インスタンスを複数作成している為）
			synchronized(StationSearchThread.lock1) {
				list = window.japan.getVector(this.nowMass.x,this.nowMass.y,1);
			}
			if(list==null) {
				//System.out.println("list_null");
				break;
			}
			moveTrajectory.add(new Coordinates(nowMass.x,nowMass.y));

			for(Boolean bool:list) {
				if(bool) {
					boolean conti=false;
					for(int j=0;j<moveTrajectory.size()-1;j++) {//既に通った場所を省く
						int vx=vList.get(i).x;
						int vy=vList.get(i).y;
						synchronized(StationSearchThread.lock4) {
							if(!window.japan.contains(this.nowMass.x+vx,this.nowMass.y+vy)) {
								vx*=2;
								vy*=2;
							}
						}
						if((moveTrajectory.get(j).x == this.nowMass.x+vx) &&
								(moveTrajectory.get(j).y == this.nowMass.y+vy)) {//同じ場合、1つ前のmoveTrajectoryを削除
							i++;
							conti=true;
							break;
						}
					}
					if(conti) {
						continue;
					}
					//2マス移動(競合の可能性)
					synchronized(StationSearchThread.lock4) {
						if(!window.japan.contains(this.nowMass.x+vList.get(i).x,this.nowMass.y+vList.get(i).y)) {
							vList.get(i).x*=2;
							vList.get(i).y*=2;
						}
					}
					if(flag) {
						//Threadを立ち上げる
						StationSearchThread thread = new StationSearchThread(window);
						synchronized(StationSearchThread.lock3) {
							thread.threadCopy(this);
							thread.setMass(this.nowMass.x+vList.get(i).x, this.nowMass.y+vList.get(i).y);//移動
						}
						Thread t = new Thread(thread);
						t.start();
					}else {
						x=this.nowMass.x+vList.get(i).x;
						y=this.nowMass.y+vList.get(i).y;
						setMassFlag=true;
						flag=true;
					}
					end=false;
				}
				i++;
			}
			if(setMassFlag) {
				synchronized(StationSearchThread.lock3) {
					this.setMass(x, y);//移動
				}
			}
			//行き先が無い場合終了
			if(end) {
				//System.out.println("正常終了end");
				break;
			}
			Thread.yield();
		}
	}

	public void setMass(int x,int y) {
		this.nowMass.x=x;
		this.nowMass.y=y;
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


