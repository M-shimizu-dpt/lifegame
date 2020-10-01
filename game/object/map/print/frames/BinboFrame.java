package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Binbo;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class BinboFrame extends FrameModel{

	public BinboFrame() {

	}

	public void open() {
		this.setTitle(Binbo.getName()+"のターン");
		JLayeredPane binbo = this.getLayeredPane();
		JLabel text1=new JLabel();
		JLabel text2=new JLabel();
		JButton closeButton = createButton(580,500,180,50,10,"閉じる");
		if(!Player.player.isPlayer()) {
			closeButton.setEnabled(false);
		}
		binbo.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		text1 = createText(10,10,600,100,20,"テストボンビー1");
		text2 = createText(10,110,600,100,20,"テストボンビー２");

		text1.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text2);
		this.setVisible(true);

		setCloseFrame();
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
		BinboEvent.turnFinish();
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame() {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closeBinbo();
				}
			}, 1000);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("閉じる")) {
			FrameEvent.closeBinbo();
		}
	}
}
