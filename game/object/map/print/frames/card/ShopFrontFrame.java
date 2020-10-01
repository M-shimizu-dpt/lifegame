package lifegame.game.object.map.print.frames.card;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ShopFrontFrame extends FrameModel{
	//shoppingCardListに改名＋static化した後Cardクラスに移動する
	private ArrayList<Card> canBuyCardList = new ArrayList<Card>();//店の購入可能カードリスト

	public ShopFrontFrame() {
		this.setTitle("カードショップ");
		this.setSize(300,400);
	}

	public void open() {
		JLayeredPane shop = this.getLayeredPane();

		JButton closeButton = createButton(100,210,100,50,10,"出る");
		shop.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JButton buyButton = createButton(100,10,100,50,10,"買う");
		shop.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
		JButton sellButton = createButton(100,110,100,50,10,"売る");
		if(Player.player.getCardSize()==0) {
			sellButton.setEnabled(false);
		}
		if(!Player.player.isPlayer()) {
			closeButton.setEnabled(false);
			buyButton.setEnabled(false);
			sellButton.setEnabled(false);
		}
		shop.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
		this.setVisible(true);
		setCloseFrame();
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	public void setCardList(ArrayList<Card> cardList) {
		canBuyCardList.addAll(cardList);
	}

	public void clearCardList() {
		canBuyCardList.clear();
	}

	public void setCloseFrame() {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closeShopFront();
				}
			}, 1000);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("出る")) {
			FrameEvent.closeShopFront();
		}else if(cmd.equals("買う")) {
			FrameEvent.openBuyShop();
		}else if(cmd.equals("売る")) {
			FrameEvent.openSellShop();
		}
	}
}