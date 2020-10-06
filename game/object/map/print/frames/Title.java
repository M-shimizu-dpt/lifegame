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
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	public int[] open() {
		JLayeredPane start = this.getLayeredPane();
   	
    	JButton startButton = new JButton("遊ぶ");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	start.add(startButton);
    	this.setVisible(true);
    	int list[]= {0,0};
    	list[0]=FrameEvent.openSettingYear();
    	list[1]=FrameEvent.openSettingPlayer();
    	

    	WaitThread wait = new WaitThread(10);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}
    	
    	return list;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("遊ぶ")) {
			App.start();
    		this.setVisible(false);
    	}
	}
}
