package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ConfirmationFrame extends FrameModel{

	public ConfirmationFrame() {

	}

	public void open(String title,String article) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();

		String artresult = FrameEvent.adjustText(article);

		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		JButton closeButton =createButton(700,500,70,50,10,"閉じる");
		confirmation.add(closeButton,JLayeredPane.PALETTE_LAYER);
	}

	public void open(String title,String article,int time) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();
		String artresult = FrameEvent.adjustText(article);
		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		this.setVisible(true);

		setCloseFrame(time);
	}

	public void open(String title,String article,int size,int time) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();
		String artresult="<html><body>"+article+"</body></html>";
		JLabel art = createText(0,0,800,600,size,artresult);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		this.setVisible(true);

		setCloseFrame(time);
	}

	public void setCloseFrame(int time) {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closePopUp();
				}
			}, time);
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
		if(cmd.equals("閉じる")) {
			FrameEvent.closePopUp();
		}
	}

}