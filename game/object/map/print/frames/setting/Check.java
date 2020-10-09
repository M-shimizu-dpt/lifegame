package lifegame.game.object.map.print.frames.setting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.main.App;


public class Check extends JFrame implements ActionListener{


	public Check() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}

	public int count;
	public int year;

	public void open() {
		JLayeredPane start = this.getLayeredPane();
		JLabel Text = new JLabel("この内容で開始します");
		Text.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	Text.setBounds(20, 10, 400, 30);

		JLabel Year = new JLabel("プレイ年数  "+year+"年");//プレイ年数
		Year.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	Year.setBounds(100, 80, 200, 30);

		JLabel Count = new JLabel("プレイ人数  "+count+"人");//プレイする人間の数
		Count.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	Count.setBounds(400, 80, 200, 30);
    	//プレイする順番
    	JLabel Order = new JLabel("プレイ順");
    	Order.setFont(new Font("SansSerif", Font.ITALIC, 20));
		Order.setBounds(100, 140, 200, 30);
		JLabel Order1 = new JLabel("１番目");
		Order1.setFont(new Font("SansSerif", Font.ITALIC, 20));
		Order1.setBounds(100, 200, 200, 30);
		JLabel Order2 = new JLabel("２番目");
		Order2.setFont(new Font("SansSerif", Font.ITALIC, 20));
		Order2.setBounds(100, 250, 200, 30);
		JLabel Order3 = new JLabel("３番目");
		Order3.setFont(new Font("SansSerif", Font.ITALIC, 20));
		Order3.setBounds(100, 300, 200, 30);
		JLabel Order4 = new JLabel("４番目");
		Order4.setFont(new Font("SansSerif", Font.ITALIC, 20));
		Order4.setBounds(100, 350, 200, 30);
		//プレイヤーの名前
		JLabel PlayName = new JLabel("プレイヤー名");
		PlayName.setFont(new Font("SansSerif", Font.ITALIC, 20));
		PlayName.setBounds(400, 140, 200, 30);
		JLabel PlayName1 = new JLabel(FrameEvent.getName(FrameEvent.getPlayerOrder(0)));
		PlayName1.setFont(new Font("SansSerif", Font.ITALIC, 20));
		PlayName1.setBounds(400, 200, 200, 30);
		JLabel PlayName2 = new JLabel(FrameEvent.getName(FrameEvent.getPlayerOrder(1)));
		PlayName2.setFont(new Font("SansSerif", Font.ITALIC, 20));
		PlayName2.setBounds(400, 250, 200, 30);
		JLabel PlayName3 = new JLabel(FrameEvent.getName(FrameEvent.getPlayerOrder(2)));
		PlayName3.setFont(new Font("SansSerif", Font.ITALIC, 20));
		PlayName3.setBounds(400, 300, 200, 30);
		JLabel PlayName4 = new JLabel(FrameEvent.getName(FrameEvent.getPlayerOrder(3)));
		PlayName4.setFont(new Font("SansSerif", Font.ITALIC, 20));
		PlayName4.setBounds(400, 350, 200, 30);

		JButton changePlayerCount = new JButton("人数変更");
		changePlayerCount.setFont(new Font("SansSerif", Font.ITALIC, 20));
		changePlayerCount.setBounds(20, 500, 150, 60);
		changePlayerCount.addActionListener(this);
		JButton changePlayerName = new JButton("名前変更");
		changePlayerName.setFont(new Font("SansSerif", Font.ITALIC, 20));
		changePlayerName.setBounds(190, 500, 150, 60);
		changePlayerName.addActionListener(this);
		JButton changeYear = new JButton("年数変更");
		changeYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
		changeYear.setBounds(360, 500, 150, 60);
		changeYear.addActionListener(this);

    	JButton startButton = new JButton("遊ぶ");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(690,500,90,60);
    	startButton.addActionListener(this);

    	start.add(Text);
    	start.add(Year);
    	start.add(Count);
    	start.add(Order);
    	start.add(PlayName);
    	start.add(Order1);
    	start.add(PlayName1);
    	start.add(Order2);
    	start.add(PlayName2);
    	start.add(Order3);
    	start.add(PlayName3);
    	start.add(Order4);
    	start.add(PlayName4);
    	start.add(changePlayerCount);
    	start.add(changePlayerName);
    	start.add(changeYear);
    	start.add(startButton);
    	this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("遊ぶ")) {
			App.start();
    		this.setVisible(false);
    	}else if(cmd.equals("人数変更")) {
    		FrameEvent.resetPlayOrder();
    		FrameEvent.resetPlayName();
    		System.out.println(FrameEvent.displayNames());
			System.out.println(FrameEvent.displayOrder());
    		FrameEvent.openSettingPlayer();
    		this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}else if(cmd.equals("名前変更")){
    		FrameEvent.resetPlayName();
    		FrameEvent.openSettingPlayName();
    		this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}else {
    		FrameEvent.openSettingYear();
    		this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}
	}

	public int getYear() {
		return year;
	}
	public int getCount() {
		return count;
	}
}
