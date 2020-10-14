/*
 * サイコロの状態を管理するクラス
 * サイコロに関する処理を記述
 */

package lifegame.game.object;

import java.util.Random;

import lifegame.game.event.ContainsEvent;

public abstract class Dice {
	private static int result=0;
	private static int num=1;

	public static int shuffle(Player player) {
		for(int i=0;i<Dice.num;i++) {//サイコロの数だけサイコロを回わす；
			if(ContainsEvent.isUsedFixedCard())break;//初めからresultが入力されていれば
			Random rand = new Random();
			Dice.result += rand.nextInt(6)+1+player.getEffect();
			if(Dice.result<=0) {
				Dice.result=1;
			}
			player.getBuff().elapsed();
		}
		System.out.println("result:"+Dice.result+"  num:"+Dice.num);
		return Dice.result;
	}
	public static int shuffle() {
		for(int i=0;i<Dice.num;i++) {//サイコロの数だけサイコロを回わす；
			Random rand = new Random();
			Dice.result += rand.nextInt(6)+1;
			if(Dice.result<=0) {
				Dice.result=1;
			}
		}
		System.out.println("result:"+Dice.result+"  num:"+Dice.num);
		return Dice.result;
	}

	public static void init() {
		clearResult();
		clearNum();
	}

	public static void setResult(int result) {
		Dice.result=result;
	}

	public static void clearResult() {
		Dice.result=0;
	}

	public static void setNum(int num) {
		Dice.num=num;
	}

	public static void clearNum() {
		Dice.num=1;
	}

	public static void clear() {
		clearResult();
		clearNum();
	}

	public static int getNum() {
		return num;
	}

	public static int getResult() {
		return result;
	}
}
