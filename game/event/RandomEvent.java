package lifegame.game.event;

import java.util.Random;

import lifegame.game.main.App;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;

public abstract class RandomEvent {
	private static boolean random2EndFlag=false;//randomイベントが終わるまで待つためのフラグ

	public static void end() {
		random2EndFlag=true;
	}

	public static void initEndFlag() {
		random2EndFlag=false;
	}

	public static boolean isEnd() {
		return random2EndFlag;
	}

	public static double randomEvent() {//playFrameの変化を追記
		Random rand = new Random();
		double randomNum = rand.nextDouble();
		if(randomNum < 0.1) {
			Player.player.addMoney(-Player.player.getMoney()/4);
		}else if(randomNum < 0.2) {
			Player.player.addMoney(-Player.player.getMoney()/2);
		}else if(randomNum < 0.3) {
			Player.player.addMoney(-Player.player.getMoney());
		}else if(randomNum < 0.4) {
			Player.player.addMoney(10000);
		}else if(randomNum < 0.5) {
			Player.player.addMoney(20000);
		}else if(randomNum < 0.6) {
			for(int i=0;i<Card.getCardListSize();i++) {
				if(ContainsEvent.name(Card.getCard(i), "一頭地を抜くカード")) {
					Player.player.addCard(Card.getCard(i));
				}
			}
		}else if(randomNum < 0.7 && ContainsEvent.isOwners()) {
			//誰かの物件の所有権を初期化
			for(Property property : Japan.getPropertys()) {
				if(ContainsEvent.isOwner(property)) {
					property.setOwner("");
					Japan.updateStationMono(property);
					break;
				}
			}
		}else if(randomNum < 0.8) {
			if(rand.nextInt(100) < 50) {
				Player.player.addMoney(Player.player.getMoney());
			}else {
				Player.player.addMoney(-Player.player.getMoney());
			}
		}else if(randomNum < 0.9) {
			//3か月間移動距離を制限する
			Player.player.getBuff().addBuff(-3, 3);
		}else {
			Player.player.addMoney(-5000);
		}
		return randomNum;
	}

	public static void random2Event(Property property,int rndnum) {
		//物件の選出
		//System.out.println("臨時収入:"+property.getOwner()+"の"+ property.getName());
		//臨時収入を追加
		int s;
		s=(1000*App.year*rndnum);

		for(int i=0;i<4;i++) {
			if(ContainsEvent.owner(property,Player.players.get(i))) {
				Player.players.get(i).addMoney(s);
				break;
			}
		}
	}

}
