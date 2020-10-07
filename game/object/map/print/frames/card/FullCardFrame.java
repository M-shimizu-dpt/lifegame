package lifegame.game.object.map.print.frames.card;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.CardEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class FullCardFrame extends FrameModel{
	private static boolean throwFlag=false;//カードを捨てるまで待つためのフラグ
	private int id=-1;

	public FullCardFrame() {
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

	public void setID(int id) {
		this.id=id;
	}
	public void open() {
		JLayeredPane cardFull = this.getLayeredPane();
		JLabel titleName = createText(170,10,100,40,30,"名前");
		for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	if(!ContainsEvent.isPlayer()) {
    			throwButton.setEnabled(false);
    		}
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,200,30,10,Player.player.getCardName(i));
        	label.setBackground(Color.LIGHT_GRAY);
        	cardFull.add(label);
        	throwButton.setActionCommand(Player.player.getCardName(i)+"t");
        	cardFull.add(throwButton);
        }
		cardFull.add(titleName);

		this.setVisible(true);

		if(!ContainsEvent.isPlayer()) {
			Player.player.cardFullCPU();
			this.setVisible(false);
		}
	}

	@Override
	public void close() {
		FullCardFrame.throwFlag=true;
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		CardEvent.throwCard(cmd);
		if(ContainsEvent.isMaxCard()) {
			reopen();
		}else {
			if(this.id==0) {
				FrameEvent.closeFullCardFromPlay();
			}else if(this.id==1) {
				FrameEvent.closeFullCardFromRandom();
			}
		}
	}
}
