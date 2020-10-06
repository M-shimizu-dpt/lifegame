package lifegame.game.object.map.print.frames.setting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class CheckFrame extends JFrame implements ActionListener{
	public CheckFrame() {
		this.setTitle("桃大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}

	public void open() {
		JLayeredPane start = this.getLayeredPane();
    	JLabel checklabel = new JLabel("この内容で始めますか？");
    	checklabel.setFont(new Font("SansSerif",Font.ITALIC,50));
    	checklabel.setBounds(250, 10, 400, 60);
    	
    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	start.add(checklabel);
    	start.add(startButton);
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("始める")) {
    		
    		this.setVisible(false);
    	}
	}
}
