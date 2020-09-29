package lifegame.game.object.map.print.animation;

import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.object.map.print.Window;
import lifegame.game.object.map.print.animation.model.AnimationThreadModel;

//未完成
public class DiceAnimationThread extends AnimationThreadModel{
	CardLayout dicelist = new CardLayout();
	JLayeredPane panel;
	public DiceAnimationThread(Window window,JLayeredPane panel) {
		panel.setLayout(dicelist);
		for(int i=1;i<=6;i++) {
			JLabel dice = window.createText(50,50,300,300,100,String.valueOf(i));
			panel.add(dice);
			dicelist.addLayoutComponent(dice,dice.getText());
		}
		this.panel=panel;
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		//int i=1;
		do {
			System.out.println(this.isDaemon());
			//dicelist.show(panel,String.valueOf(i));
			dicelist.next(panel);
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			//i++;
			//if(i==7) i=1;
		}while(!DiceAnimationThread.isEnd());
	}
}
