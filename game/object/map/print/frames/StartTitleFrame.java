package lifegame.game.object.map.print.frames;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.event.WaitThread;

public class StartTitleFrame extends JFrame {
	
	public StartTitleFrame() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
        
	}
	
	public void open() {
		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("挑大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.BOLD, 100));
    	labelTitle.setBounds(50, 50, 600, 100);
    	
    	start.add(labelTitle);
    	
    	this.setVisible(true);
    	try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
    	close();
    	FrameEvent.openSetting();
    	
    	WaitThread wait = new WaitThread(10);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}
	}
	
	public void close() {
		this.setVisible(false);
	}
}
