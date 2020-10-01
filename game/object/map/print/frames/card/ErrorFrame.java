package lifegame.game.object.map.print.frames.card;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Card;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ErrorFrame extends FrameModel{
	private static boolean throwFlag=false;//カードを捨てるまで待つためのフラグ

	public ErrorFrame() {
		this.setSize(400,500);
	}

	public void initThrowFlag() {
		throwFlag=false;
	}
	public boolean isThrowed() {
		return throwFlag;
	}
	public void throwEnd() {
		throwFlag=true;
	}

	private void reopen() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
		open();
	}

	@Override
	public void open() {
		JLayeredPane error = this.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,Player.player.getCardName(i));
        	label.setBackground(Color.LIGHT_GRAY);
        	error.add(label);
        	throwButton.setActionCommand(Player.player.getCardName(i)+"t");
        	error.add(throwButton);
        }
		error.add(titleName);

		this.setVisible(true);

		if(!Player.player.isPlayer()) {
			Player.player.cardFullCPU();
			this.setVisible(false);
		}
	}

	@Override
	public void close() {
		ErrorFrame.throwFlag=true;
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		for(int i=0;i<Card.getCardListSize();i++) {
			if(cmd.equals(Card.getCard(i).getName()+"t")) {//カードを捨てる
				Player.player.removeCard(Card.getCard(i));
				if(ContainsEvent.isMaxCard()) {
					reopen();
					break;
				}else {
					FrameEvent.closeError();
					break;
				}
			}
		}
	}
}
