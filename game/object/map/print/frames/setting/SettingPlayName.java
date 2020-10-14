package lifegame.game.object.map.print.frames.setting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import lifegame.game.event.FrameEvent;

public class SettingPlayName extends JFrame implements ActionListener {

	public SettingPlayName() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	public int number;
	public ArrayList<String> setNames = new ArrayList<String>();
	
	JTextField textplayer1;
	JTextField textplayer2;
	JTextField textplayer3;
	JTextField textplayer4;
	
	JLabel textPlayer1;
	JLabel textPlayer2;
	JLabel textPlayer3;
	JLabel textPlayer4;
	
	public void open() {
		JLayeredPane start = this.getLayeredPane();
		JLabel labelTitle = new JLabel("プレイヤー設定");
		labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(200, 10, 400, 60);
    	
		//名前の変更設定
    	JLabel labelPlayerName;
    	
    	
    	if(number==0) {
    		labelPlayerName = new JLabel("以下の名前で設定します");
    	}else {
    		labelPlayerName = new JLabel("名前を変更する場合は下に入力してください");
    	}
    	labelPlayerName.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayerName.setBounds(20, 80, 400, 50);

    	JLabel player1 = new JLabel("1人目");
    	player1.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player1.setBounds(150, 120, 150, 50);
    	JLabel player2 = new JLabel("2人目");
    	player2.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player2.setBounds(150, 200, 150, 50);
    	JLabel player3 = new JLabel("3人目");
    	player3.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player3.setBounds(150, 280, 150, 50);
    	JLabel player4 = new JLabel("4人目");
    	player4.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player4.setBounds(150, 360, 150, 50);
    	
    	if(number==0) {
    		textPlayer1 = new JLabel("CPU1");
    		textPlayer1.setFont(new Font("SansSerif",Font.BOLD,20));
    		textPlayer1.setBounds(325, 120, 150, 50);
        	
        	textPlayer2 = new JLabel("CPU2");
        	textPlayer2.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer2.setBounds(325, 200, 150, 50);
        	
        	textPlayer3 = new JLabel("CPU3");
        	textPlayer3.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer3.setBounds(325, 280, 150, 50);
        	
        	textPlayer4 = new JLabel("CPU4");
        	textPlayer4.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer4.setBounds(325, 360, 150, 50);
        	
        	start.add(textPlayer1);
        	start.add(textPlayer2);
        	start.add(textPlayer3);
        	start.add(textPlayer4);
    	}else if(number==1) {
    		textplayer1 = new JTextField("Player");
        	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer1.setHorizontalAlignment(JTextField.CENTER);
        	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer1.setBounds(300, 120, 150, 50);
        	
        	textPlayer2 = new JLabel("CPU1");
        	textPlayer2.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer2.setBounds(325, 200, 150, 50);
        	
        	textPlayer3 = new JLabel("CPU2");
        	textPlayer3.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer3.setBounds(325, 280, 150, 50);
        	
        	textPlayer4 = new JLabel("CPU3");
        	textPlayer4.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer4.setBounds(325, 360, 150, 50);
        	
        	start.add(textplayer1);
        	start.add(textPlayer2);
        	start.add(textPlayer3);
        	start.add(textPlayer4);
    	}else if(number==2) {
    		textplayer1 = new JTextField("Player1");
        	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer1.setHorizontalAlignment(JTextField.CENTER);
        	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer1.setBounds(300, 120, 150, 50);
        	
        	textplayer2 = new JTextField("Player2");
        	textplayer2.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer2.setHorizontalAlignment(JTextField.CENTER);
        	textplayer2.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer2.setBounds(300, 200, 150, 50);
        	
        	textPlayer3 = new JLabel("CPU1");
        	textPlayer3.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer3.setBounds(325, 280, 150, 50);
        	
        	textPlayer4 = new JLabel("CPU2");
        	textPlayer4.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer4.setBounds(325, 360, 150, 50);
        	
        	start.add(textplayer1);
        	start.add(textplayer2);
        	start.add(textPlayer3);
        	start.add(textPlayer4);
    	}else if(number==3) {
    		textplayer1 = new JTextField("Player1");
        	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer1.setHorizontalAlignment(JTextField.CENTER);
        	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer1.setBounds(300, 120, 150, 50);
        	
        	textplayer2 = new JTextField("Player2");
        	textplayer2.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer2.setHorizontalAlignment(JTextField.CENTER);
        	textplayer2.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer2.setBounds(300, 200, 150, 50);
        	
        	textplayer3 = new JTextField("Player3");
        	textplayer3.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer3.setHorizontalAlignment(JTextField.CENTER);
        	textplayer3.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer3.setBounds(300, 280, 150, 50);
        	
        	textPlayer4 = new JLabel("CPU");
        	textPlayer4.setFont(new Font("SansSerif",Font.BOLD,20));
        	textPlayer4.setBounds(325, 360, 150, 50);
        	
        	start.add(textplayer1);
        	start.add(textplayer2);
        	start.add(textplayer3);
        	start.add(textPlayer4);
    	}else {
    		textplayer1 = new JTextField("Player1");
        	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer1.setHorizontalAlignment(JTextField.CENTER);
        	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer1.setBounds(300, 120, 150, 50);
        	
        	textplayer2 = new JTextField("Player2");
        	textplayer2.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer2.setHorizontalAlignment(JTextField.CENTER);
        	textplayer2.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer2.setBounds(300, 200, 150, 50);
        	
        	textplayer3 = new JTextField("Player3");
        	textplayer3.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer3.setHorizontalAlignment(JTextField.CENTER);
        	textplayer3.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer3.setBounds(300, 280, 150, 50);
        	
        	textplayer4 = new JTextField("Player4");
        	textplayer4.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
        	textplayer4.setHorizontalAlignment(JTextField.CENTER);
        	textplayer4.setFont(new Font("SansSerif",Font.BOLD,20));
        	textplayer4.setBounds(300, 360, 150, 50);
        	
        	start.add(textplayer1);
        	start.add(textplayer2);
        	start.add(textplayer3);
        	start.add(textplayer4);
    	}
    	    	
		JButton startButton = new JButton("OK");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(580,490,180,60);
    	startButton.addActionListener(this);
    	getRootPane().setDefaultButton(startButton);
    	startButton.setMnemonic(KeyEvent.VK_O);
    	JButton backButton = new JButton("Back");
    	backButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	backButton.setBounds(20,490,180,60);
    	backButton.addActionListener(this);
    	backButton.setMnemonic(KeyEvent.VK_B);
    	
    	start.add(player1);
    	start.add(player2);
    	start.add(player3);
    	start.add(player4);
    	start.add(labelTitle);
    	start.add(labelPlayerName);
    	start.add(backButton);
    	start.add(startButton);
    	this.setVisible(true);

	}
	
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		if(cmd.equals("OK")) {
			if(number==0) {
	    		setNames.add(textPlayer1.getText());
	        	setNames.add(textPlayer2.getText());
	        	setNames.add(textPlayer3.getText());
	        	setNames.add(textPlayer4.getText());
	    	}else if(number==1) {
	    		setNames.add(textplayer1.getText());
	        	setNames.add(textPlayer2.getText());
	        	setNames.add(textPlayer3.getText());
	        	setNames.add(textPlayer4.getText());
	        	
	    	}else if(number==2) {
	    		setNames.add(textplayer1.getText());
	        	setNames.add(textplayer2.getText());
	        	setNames.add(textPlayer3.getText());
	        	setNames.add(textPlayer4.getText());
	    	}else if(number==3) {
	    		setNames.add(textplayer1.getText());
	        	setNames.add(textplayer2.getText());
	        	setNames.add(textplayer3.getText());
	        	setNames.add(textPlayer4.getText());
	    	}else {
	    		setNames.add(textplayer1.getText());
	        	setNames.add(textplayer2.getText());
	        	setNames.add(textplayer3.getText());
	        	setNames.add(textplayer4.getText());
	    	}
			FrameEvent.setPlayerCount();
			FrameEvent.openSettingYear();
			this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}
		else {
			FrameEvent.resetPlayName();
			FrameEvent.resetPlayOrder();
			FrameEvent.openSettingPlayer();
    		this.setVisible(false);
    		this.getLayeredPane().removeAll();
    	}
	}
	
	public String getName(int index) {
		return setNames.get(index);
	}
}
