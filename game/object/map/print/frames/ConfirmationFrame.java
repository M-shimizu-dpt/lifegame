package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ConfirmationFrame extends FrameModel{
	private String article;
	private int time;
	private int size;

	public ConfirmationFrame() {
		resetValues();
	}

	public void resetValues() {
		this.article="";
		this.time=0;
		this.size=0;
	}

	public void setArticle(String article) {
		this.article=article;
	}

	public void setTime(int time) {
		this.time=time;
	}

	public void setTextSize(int size) {
		this.size=size;
	}

	public void open() {
		JLayeredPane confirmation = this.getLayeredPane();
		JLabel art;
		String artresult = FrameEvent.adjustText(article);
		if(this.size>0) {
			art = createText(0,0,800,600,size,artresult);
		}else {
			art = createText(0,0,800,600,20,artresult);
		}
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		if(time>0) {
			setCloseFrame();
		}else{
			JButton closeButton =createButton(700,500,70,50,10,"閉じる");
			if(!ContainsEvent.isPlayer()) {
				closeButton.setEnabled(false);
			}
			confirmation.add(closeButton,JLayeredPane.PALETTE_LAYER);
		}
		this.setVisible(true);
	}

	public void setCloseFrame() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				FrameEvent.closePopUp();
			}
		}, time);
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
		resetValues();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("閉じる")) {
			FrameEvent.closePopUp();
		}
	}

}