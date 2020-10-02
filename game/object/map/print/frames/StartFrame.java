package lifegame.game.object.map.print.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

	public static ArrayList<String> setNames = new ArrayList<String>();
	
	public int[] open() {
  		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(280, 10, 400, 60);
    	//年数設定
    	JLabel labelYear = new JLabel("何年プレイしますか？");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 100, 200, 50);
    	JTextField textYear = new JTextField("3");
    	textYear.setHorizontalAlignment(JTextField.CENTER);
    	textYear.setFont(new Font("SansSerif",Font.BOLD,20));
    	textYear.setBounds(330, 100, 200, 50);
    	//プレイヤーの人数設定
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 200, 300, 50);
    	JTextField textPlayerCount = new JTextField("1");
    	textPlayerCount.setHorizontalAlignment(JTextField.CENTER);
    	textPlayerCount.setFont(new Font("SansSerif",Font.BOLD,20));
    	textPlayerCount.setBounds(330, 200, 200, 50);
    	//名前の変更設定
    	JLabel labelPlayerName = new JLabel("名前を変更する場合は下に入力してください");
    	labelPlayerName.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayerName.setBounds(20, 300, 400, 50);
    	
    	
    	JLabel player1 = new JLabel("1番目");
    	player1.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player1.setBounds(50, 350, 150, 50);
    	JTextField textplayer1 = new JTextField("player1");
    	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer1.setHorizontalAlignment(JTextField.CENTER);
    	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer1.setBounds(50, 410, 150, 50);
    	
    	JLabel player2 = new JLabel("2番目");
    	player2.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player2.setBounds(225, 350, 150, 50);
    	JTextField textplayer2 = new JTextField("player2");
    	textplayer2.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer2.setHorizontalAlignment(JTextField.CENTER);
    	textplayer2.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer2.setBounds(225, 410, 150, 50);

    	JLabel player3 = new JLabel("3番目");
    	player3.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player3.setBounds(400, 350, 150, 50);
    	JTextField textplayer3 = new JTextField("player3");
    	textplayer3.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer3.setHorizontalAlignment(JTextField.CENTER);
    	textplayer3.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer3.setBounds(400, 410, 150, 50);

    	JLabel player4 = new JLabel("4番目");
    	player4.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player4.setBounds(575, 350, 150, 50);
    	JTextField textplayer4 = new JTextField("player4");
    	textplayer4.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer4.setHorizontalAlignment(JTextField.CENTER);
    	textplayer4.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer4.setBounds(575, 410, 150, 50);

    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	start.add(labelTitle);
    	start.add(labelYear);
    	start.add(textYear);
    	start.add(labelPlayers);
    	start.add(textPlayerCount);
    	start.add(labelPlayerName);
    	start.add(player1);
    	start.add(textplayer1);
    	start.add(player2);
    	start.add(textplayer2);
    	start.add(player3);
    	start.add(textplayer3);
    	start.add(player4);
    	start.add(textplayer4);
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
    	list[0] = Integer.parseInt((String) textPlayerCount.getText());
    	list[1] = Integer.parseInt((String)textYear.getText());
    	
    	setNames.add(textplayer1.getText());
    	setNames.add(textplayer2.getText());
    	setNames.add(textplayer3.getText());
    	setNames.add(textplayer4.getText());
    	
    	return list;
  	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("始める")) {
    		App.start();
    	}
	}
}
