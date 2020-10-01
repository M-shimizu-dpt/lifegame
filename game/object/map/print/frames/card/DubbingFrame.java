package lifegame.game.object.map.print.frames.card;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.CardEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class DubbingFrame extends FrameModel{

	public DubbingFrame() {
		this.setSize(700,500);
	}

	public void open() {
		JLayeredPane dubbing = this.getLayeredPane();
		JLabel titleName = createText(150,10,100,40,30,"名前");
        JLabel titleText = createText(420,10,100,40,30,"説明");
        for(int roop=0;roop<Player.player.getCardSize();roop++) {
        	JButton useButton = createButton(10,35*(roop+1)+30,70,30,10,"複製");
        	//ここにプレイヤーの所持カード一覧を作成し、使用ボタンとカード名をリンクさせる。
        	JLabel labelName = createText(100,35*(roop+1)+30,180,30,10,Player.player.getCardName(roop));
        	JLabel labelText = createText(300,35*(roop+1)+30,350,30,10,Player.player.getCardText(roop));
        	labelName.setBackground(Color.LIGHT_GRAY);
        	labelText.setBackground(Color.LIGHT_GRAY);
        	useButton.setActionCommand(Player.player.getCardName(roop)+"d");
        	dubbing.add(labelName);
        	dubbing.add(labelText);
        	dubbing.add(useButton);
        }
        dubbing.add(titleName);
        dubbing.add(titleText);

        this.setVisible(true);

        if(!Player.player.isPlayer()) {
	        Player.player.addCard(Player.player.getCard(0));//無いも考えず一番上のカードを複製
	        setCloseFrame();
        }
	}
	//指定のFrameを1秒後に閉じる
	public void setCloseFrame() {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closeDubbing();
				}
			}, 3000);
		}
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		CardEvent.dubbingCard(cmd);
		FrameEvent.closeDubbing();
	}
}
