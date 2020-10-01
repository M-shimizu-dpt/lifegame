package lifegame.game.object.map.print.frames.card;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.SaleEvent;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ShopFrame extends FrameModel{
	private ArrayList<Card> canBuyCardList = new ArrayList<Card>();//店の購入可能カードリスト
	public ShopFrame() {
		this.setSize(600, 600);
	}

	//カードショップの売買画面を閉じる
	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}
	private void buyCard(String pre) {
		SaleEvent.buyCard(pre);
		reopen();
	}
	private void sellCard(String pre) {
		SaleEvent.sellCard(pre);
		reopen();
	}
	private void reopen() {
		close();
		open();
	}

	public void addCardList(ArrayList<Card> cardList) {
		canBuyCardList.addAll(cardList);
	}
	public void clearCardList() {
		canBuyCardList.clear();
	}
	public void open(int id) {
		if(id==0) {
			openBuyShop();
		}else if(id==1) {
			openSellShop();
		}
		this.setVisible(true);
	}

	public void openBuyShop() {
		this.setTitle("購入");
		JLayeredPane buyShop = this.getLayeredPane();

		JButton closeButton = createButton(500,500,70,50,10,"戻る");
		buyShop.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JLabel myMoney = createText(10,5,400,40,10,"所持金"+Player.player.getMoney());
		buyShop.add(myMoney);

		for(int i=1; i<=canBuyCardList.size(); i++) {
			JButton buyButton = createButton(500,i*50,70,50,10,"購入");
			buyButton.setActionCommand(canBuyCardList.get(i-1).getName()+":b");
			if(canBuyCardList.get(i-1).getBuyPrice() > Player.player.getMoney() || Player.player.getCardSize() > 7) {
				buyButton.setEnabled(false);
			}
			buyShop.add(buyButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,canBuyCardList.get(i-1).getName());
			buyShop.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(canBuyCardList.get(i-1).getBuyPrice()));
			buyShop.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}

		if(Player.player.getCardSize() > 7) {
			JLabel cardFull = createText(450,5,130,40,10,"カードがいっぱいです");
			buyShop.add(cardFull);
		}
	}
	//カードショップの売却画面
	private void openSellShop() {
		this.setTitle("売却");
		JLayeredPane shopSell = this.getLayeredPane();
		JButton closeButton = createButton(500,500,70,50,10,"戻る");
		closeButton.setActionCommand("カード売却を終える");
		shopSell.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		for(int i=1; i<=Player.player.getCardSize(); i++) {
			JButton sellButton = createButton(500,i*50,70,50,10,"売却");
			sellButton.setActionCommand(Player.player.getCardName(i-1)+":s");
			shopSell.add(sellButton,JLayeredPane.PALETTE_LAYER,0);
			JLabel name = createText(10,i*50,300,50,10,Player.player.getCardName(i-1));
			shopSell.add(name,JLayeredPane.PALETTE_LAYER,-1);
			JLabel amount = createText(320,i*50,100,50,10,String.valueOf(Player.player.getCard(i-1).getSellPrice()));
			shopSell.add(amount,JLayeredPane.PALETTE_LAYER,-1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String pre[] = cmd.split(":");
		if(cmd.equals("戻る")) {
			close();
		}else if(ContainsEvent.isCard(pre[0])) {//カード名かどうか判定
			if(pre[1].equals("s")) {
				sellCard(pre[0]);
			}else if(pre[1].equals("b")) {
				buyCard(pre[0]);
			}
		}
	}
}
