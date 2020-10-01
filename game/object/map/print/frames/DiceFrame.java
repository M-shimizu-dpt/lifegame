package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import lifegame.game.event.DiceEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.map.print.animation.DiceAnimationThread;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class DiceFrame extends FrameModel{
	public DiceFrame() {
		this.setTitle("サイコロ");
		this.setSize(600,600);
		JLayeredPane dice = this.getLayeredPane();
		JButton button =createButton(490,450,70,50,10,"回す");
		JButton closeButton =createButton(490,500,70,50,10,"戻る");
		dice.add(button,JLayeredPane.PALETTE_LAYER,0);
		dice.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
	}

	public void switchingDice() {
		DiceAnimationThread anime = new DiceAnimationThread(this,this.getLayeredPane());
		anime.setDaemon(true);
		anime.start();
		try {
			anime.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("asdfasdfasd");//debug
	}

	@Override
	public void close() {
		DiceAnimationThread.end();
		this.setVisible(false);
	}

	public void open() {
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("回す")) {
			DiceEvent.shuffleDice();
			FrameEvent.closeDice();
		}else if(cmd.equals("戻る")) {
			FrameEvent.closeDice();
		}
	}
}
