package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.BinboEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class BinboFrame extends FrameModel{
	private String binboName;
	private String action;

	public BinboFrame() {

	}

	public void setBinboName(String name) {
		this.binboName=name;
	}

	public void setAction(String action) {
		this.action=action;
	}

	public void open() {
		this.setTitle(binboName+"のターン");
		JLayeredPane binbo = this.getLayeredPane();
		JLabel text=new JLabel();
		JButton closeButton = createButton(580,500,180,50,10,"閉じる");
		if(!ContainsEvent.isPlayer()) {
			closeButton.setEnabled(false);
		}
		binbo.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		text = createText(10,10,600,400,20,FrameEvent.adjustText(action));

		text.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text);
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
		if(!ContainsEvent.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
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

/*
 * 	public void bonbyPlayer(String st1,String st2,String st3,String st4) {
		playFrame.setVisible(false);
		binboFrame = new JFrame();
		JLayeredPane binbo = binboFrame.getLayeredPane();
		binboFrame.setSize(800,600);
		binboFrame.setLocationRelativeTo(null);
		//ImageIcon icon =  new ImageIcon("./img/days_res.png");
		//icon.createImageIcon("./img/days_res.png",");
		JLabel text1=new JLabel();
		JLabel text2=new JLabel();
		JLabel text3=new JLabel();
		JButton closeButton = createButton(580,500,180,50,10,"閉じる");
		closeButton.setActionCommand("貧乏神イベントを閉じる");
		if(!ContainsEvent.isPlayer()) {
			closeButton.setEnabled(false);
		}
		binbo.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		binboFrame.setName("ボンビーのターン");

		text1 = createText(10,10,600,100,20,"プレイヤー名:"+st1+"ボンビー名:"+st4);
		text2 = createText(10,110,600,100,20,st2);
		text3 = createText(10,210,600,100,20,st3);

		text1.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text2);
		text3.setHorizontalAlignment(SwingConstants.LEFT);
		binbo.add(text3);
		binboFrame.setVisible(true);

		setCloseFrame(5);

	}*/
