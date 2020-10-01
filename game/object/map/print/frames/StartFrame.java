package lifegame.game.object.map.print.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import lifegame.game.event.WaitThread;
import lifegame.game.main.App;

public class StartFrame extends JFrame implements ActionListener{

	public StartFrame() {
		this.setTitle("桃大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}

	public int[] open() {
  		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(280, 10, 400, 60);
    	JLabel labelYear = new JLabel("何年プレイしますか？");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 50, 200, 50);
    	JTextField textYear = new JTextField("3");
    	textYear.setBounds(20, 100, 200, 50);
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 150, 300, 50);
    	JTextField textPlayerCount = new JTextField("1");
    	textPlayerCount.setBounds(20, 200, 200, 50);
    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	start.add(labelTitle);
    	start.add(labelYear);
    	start.add(textYear);
    	start.add(labelPlayers);
    	start.add(textPlayerCount);
    	start.add(startButton);

    	this.setVisible(true);

    	WaitThread wait = new WaitThread(10);
    	wait.setDaemon(true);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}

    	this.setVisible(false);

    	int[] list= {0,0};
    	list[0] = Integer.parseInt(textPlayerCount.getText());
    	list[1] = Integer.parseInt(textYear.getText());
    	return list;
  	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("始める")) {
    		App.start();
    	}
	}
}
