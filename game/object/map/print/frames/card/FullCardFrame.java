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
		super.setSize(700,500);
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
		JLabel titleText = createText(420,10,100,40,30,"説明");
		for(int i=0;i<Player.player.getCardSize();i++) {
        	JButton throwButton = createButton(10,35*(i+1)+30,70,30,10,"捨てる");
        	if(!ContainsEvent.isPlayer()) {
    			throwButton.setEnabled(false);
    		}
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel label = createText(100,35*(i+1)+30,180,30,10,Player.player.getCardName(i));
        	JLabel text = createText(300,35*(i+1)+30,350,30,10,Player.player.getCardText(i));
        	label.setBackground(Color.LIGHT_GRAY);
        	text.setBackground(Color.LIGHT_GRAY);
        	cardFull.add(label);
        	cardFull.add(text);
        	throwButton.setActionCommand(Player.player.getCardName(i)+"t");
        	cardFull.add(throwButton);
        }
		cardFull.add(titleName);
		cardFull.add(titleText);
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
