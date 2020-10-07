package lifegame.game.object.map.print.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.event.WaitThread;
import lifegame.game.main.App;


public class Title extends JFrame implements ActionListener{

	
	public Title() {
		this.setTitle("桃大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(400, 300);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	public static int count;
	public static int year;
	
	public void open() {
		JLayeredPane start = this.getLayeredPane();
   	
    	JButton startButton = new JButton("遊ぶ");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(110,100,180,60);
    	startButton.addActionListener(this);
    	start.add(startButton);
    	this.setVisible(true);
    	FrameEvent.openSetting();
    	
    	WaitThread wait = new WaitThread(10);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("遊ぶ")) {
			App.start();
    		this.setVisible(false);
    	}
	}
	
	public int getSetYear() {
		return year;
	}
	public int getSetCount() {
		return count;
	}
}
