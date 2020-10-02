package lifegame.game.object.map.print.frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.main.App;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class GoalFrame extends FrameModel{

	public GoalFrame() {
		this.setSize(500,300);
	}

	public void open() {
		this.setTitle(FrameEvent.getNowMassName());
		JLayeredPane goal = this.getLayeredPane();
		int goalMoney;
		Random rand = new Random();
		JButton closeButton = createButton(380,180,100,50,10,"閉じる");
		goalMoney=10000*App.year;
		goalMoney+=rand.nextInt(10000);
		goalMoney-=goalMoney%100;
		Player.player.addMoney(goalMoney);
		JLabel label = createText(10,30,400,100,10,Player.player.getName()+"さんには地元民から援助金として"+System.lineSeparator()+goalMoney/10000+"億"+goalMoney%10000+"万円が寄付されます。");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		this.setVisible(true);
		setCloseFrame(0);
	}

	public void openNextGoal() {
		this.setTitle("次の目的地");
		JLayeredPane goal = this.getLayeredPane();
		JButton closeButton = createButton(380,180,100,50,10,"閉じる");
		closeButton.setActionCommand("次");

		//探索が終わっていなければここで止める

		FrameEvent.resetGoalColor();
		Japan.changeGoal();
		FrameEvent.setGoalColor();
		JLabel label = createText(10,30,400,100,10,"次の目的地は"+Japan.getGoalName()+"です！");
		label.setBackground(Color.BLUE);
		goal.add(closeButton);
		goal.add(label);
		this.setVisible(true);

		setCloseFrame(1);

		BinboEvent.binboPossessPlayer();
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame(int id) {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			if(id==0) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						FrameEvent.closeGoal();
					}
				}, 1000);
			}else if(id==1) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						FrameEvent.closeNextGoal();
					}
				}, 1000);
			}
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
			FrameEvent.closeGoal();
		}else if(cmd.equals("次")) {
			FrameEvent.closeNextGoal();
		}
	}
}
