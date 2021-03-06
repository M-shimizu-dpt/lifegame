package lifegame.game.object.map.print.frames.setting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButton;
import lifegame.game.event.FrameEvent;

public class SettingPlayer extends JFrame implements ActionListener {
	
	public SettingPlayer() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	ButtonGroup Count;
	ButtonGroup Order;
	public int count;
	public int playerorder;
	public ArrayList<Integer> PlayerOrder = new ArrayList<Integer>();
	
	public void open() {
		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("プレイヤー設定");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(200, 10, 400, 60);
    	
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 100, 300, 50);
    	
    	JRadioButton count1 = new JRadioButton("1.人間:1 VS CPU:3",true);
    	count1.addActionListener(this);
    	count1.setActionCommand("1");
    	count1.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count1.setBounds(50, 150, 180, 50);
    	count1.setMnemonic(KeyEvent.VK_1);
    	JRadioButton count2 = new JRadioButton("2.人間:2 VS CPU:2");
    	count2.addActionListener(this);
    	count2.setActionCommand("2");
    	count2.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count2.setBounds(230, 150, 180, 50);
    	count2.setMnemonic(KeyEvent.VK_2);
    	JRadioButton count3 = new JRadioButton("3.人間:3 VS CPU:1");
    	count3.addActionListener(this);
    	count3.setActionCommand("3");
    	count3.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count3.setBounds(410, 150, 180, 50);
    	count3.setMnemonic(KeyEvent.VK_3);
    	JRadioButton count4 = new JRadioButton("4.人間:4 VS CPU:0");
    	count4.addActionListener(this);
    	count4.setActionCommand("4");
    	count4.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count4.setBounds(590, 150, 180, 50);
    	count4.setMnemonic(KeyEvent.VK_4);
    	JRadioButton count0 = new JRadioButton("0.人間:0 VS CPU:4");//
    	count0.addActionListener(this);
    	count0.setActionCommand("0");
    	count0.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count0.setBounds(770, 150, 180, 50);
    	count0.setMnemonic(KeyEvent.VK_0);
    	Count = new ButtonGroup();
    	Count.add(count0);
    	Count.add(count1);
    	Count.add(count2);
    	Count.add(count3);
    	Count.add(count4);
    	
    	//順番の設定
    	JLabel labelOrder = new JLabel("順番をランダムに入れ替えますか？");
    	labelOrder.setBounds(20, 200, 350, 50);
    	labelOrder.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	JRadioButton order1 = new JRadioButton("A:入れ替える",true);
    	order1.addActionListener(this);
    	order1.setActionCommand("a");
    	order1.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	order1.setBounds(100, 250, 150, 50);
    	order1.setMnemonic(KeyEvent.VK_A);
    	JRadioButton order2 = new JRadioButton("B:そのまま");
    	order2.addActionListener(this);
    	order2.setActionCommand("b");
    	order2.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	order2.setBounds(450, 250, 150, 50);
    	order2.setMnemonic(KeyEvent.VK_B);
    	Order = new ButtonGroup();
    	Order.add(order1);
    	Order.add(order2);
    	
		JButton startButton = new JButton("OK");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(580,490,180,60);
    	startButton.addActionListener(this);
    	getRootPane().setDefaultButton(startButton);
    	startButton.setMnemonic(KeyEvent.VK_O);
    	
    	start.add(labelTitle);
    	start.add(labelPlayers);
    	start.add(count1);
    	start.add(count2);
    	start.add(count3);
    	start.add(count4);
    	start.add(count0);
    	start.add(order1);
    	start.add(order2);
    	start.add(labelOrder);
    	start.add(startButton);

    	this.setVisible(true);
    	
	}
	
	public void actionPerformed(ActionEvent e) {
		String cnt = Count.getSelection().getActionCommand();
		count = Integer.valueOf(cnt);
		
		//順番決め
		String odr = Order.getSelection().getActionCommand();
		int docomo;
		if(odr.equals("a")) {
			docomo=0;
		}else {
			docomo=1;
		}
		
		playerorder=Integer.valueOf(docomo);
		if(PlayerOrder.size()!=0) {
			PlayerOrder.clear();
		}
		for(int i=0;i<4;i++) {
			PlayerOrder.add(i);
		}
		
		if(playerorder==0) {
			Collections.shuffle(PlayerOrder);//順番のシャッフル
		}else {
			Collections.sort(PlayerOrder);//初期設定値
		}
		
		
		String cmd = e.getActionCommand();
		if(cmd.equals("OK")) {
			FrameEvent.setPlayNumber();
			FrameEvent.openSettingPlayName();
			this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}
	}
	
	public int getPlayerOrder(int index) {
		return PlayerOrder.get(index);
	}
	
}
