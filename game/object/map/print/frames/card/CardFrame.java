package lifegame.game.object.map.print.frames.card;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.CardEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.main.App;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class CardFrame extends FrameModel{
	public CardFrame() {
		this.setTitle("カード一覧");
		this.setSize(700,500);
	}

	public void open() {
		JLayeredPane card = this.getLayeredPane();
		JButton closeButton = createButton(570,400,100,40,10,"戻る");
        JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton useButton = createButton(10,35*(i+1)+30,70,30,10,"使用");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(i+1)+30,180,30,10,Player.player.getCardName(i));
        	JLabel labelText = createText(300,35*(i+1)+30,350,30,10,Player.player.getCardText(i));
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(Player.player.getCardName(i));
        	if(ContainsEvent.name(Player.player.getCard(i), "ダビングカード") && Player.player.getCardSize()<2) {
        		useButton.setEnabled(false);
        	}
        	card.add(labelName);
        	card.add(labelText);
        	card.add(useButton);
        }
        card.add(titleName);
        card.add(titleText);
        card.add(closeButton);
        this.setVisible(true);
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("戻る")) {
			FrameEvent.closeCard();
			return;
		}else {
			CardEvent.UseCard(cmd);
			FrameEvent.closeCard();
			if(ContainsEvent.isUsedRandomCard() || ContainsEvent.isUsedOthersCard()) {
				CardEvent.resetFlags();
				if(ContainsEvent.isPlayShowing()) {
					App.turnEnd();
				}
			}
		}
	}
}
