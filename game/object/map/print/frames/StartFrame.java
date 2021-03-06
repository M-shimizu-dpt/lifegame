package lifegame.game.object.map.print.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import lifegame.game.event.WaitThread;
import lifegame.game.main.App;

public class StartFrame extends JFrame implements ActionListener{

	public StartFrame() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}

	public static ArrayList<String> setNames = new ArrayList<String>();
	ButtonGroup Order;
	ButtonGroup Count;
	public int count;
	public int playerorder;
	public static ArrayList<Integer> PlayerOrder = new ArrayList<Integer>();

	public int[] open() {
  		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("挑大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(250, 10, 400, 60);
    	//年数設定
    	JLabel labelYear = new JLabel("何年プレイしますか？(１～１００年)");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 100, 400, 50);
    	JLabel text =new JLabel("年");
    	text.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	text.setBounds(490, 100, 50, 50);
    	
    	JSpinner spinnerYear = new JSpinner(new SpinnerNumberModel(3,1,100,1));
    	spinnerYear.setFont(new Font("SansSerif",Font.BOLD,20));
    	spinnerYear.setBounds(430, 100, 55, 50);
    	JSpinner.NumberEditor model = new JSpinner.NumberEditor(spinnerYear);
    	spinnerYear.setEditor(model);
    	JFormattedTextField textYear = model.getTextField();
    	textYear.setEditable(false);
    	textYear.setBackground(Color.white);
    	
    	//プレイヤーの人数設定
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 180, 300, 50);
    	
    	JRadioButton count1 = new JRadioButton("人間:1 VS CPU:3",true);
    	count1.addActionListener(this);
    	count1.setActionCommand("1");
    	count1.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count1.setBounds(350, 150, 150, 50);
    	JRadioButton count2 = new JRadioButton("人間:2 VS CPU:2");
    	count2.addActionListener(this);
    	count2.setActionCommand("2");
    	count2.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count2.setBounds(500, 150, 150, 50);
    	JRadioButton count3 = new JRadioButton("人間:3 VS CPU:1");
    	count3.addActionListener(this);
    	count3.setActionCommand("3");
    	count3.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count3.setBounds(350, 200, 150, 50);
    	JRadioButton count4 = new JRadioButton("人間:4 VS CPU:0");
    	count4.addActionListener(this);
    	count4.setActionCommand("4");
    	count4.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count4.setBounds(500, 200, 150, 50);
    	JRadioButton count0 = new JRadioButton("人間:0 VS CPU:4");
    	count0.addActionListener(this);
    	count0.setActionCommand("0");
    	count0.setFont(new Font("SansSerif", Font.ITALIC, 15));
    	count0.setBounds(650, 200, 150, 50);
    	Count = new ButtonGroup();
    	Count.add(count0);
    	Count.add(count1);
    	Count.add(count2);
    	Count.add(count3);
    	Count.add(count4);
    	
    	//順番の設定
    	JLabel labelOrder = new JLabel("順番をランダムに入れ替えますか？");
    	labelOrder.setBounds(20, 250, 350, 50);
    	labelOrder.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	JRadioButton order1 = new JRadioButton("入れ替える");
    	order1.addActionListener(this);
    	order1.setActionCommand("0");
    	order1.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	order1.setBounds(400, 250, 150, 50);
    	JRadioButton order2 = new JRadioButton("そのまま",true);
    	order2.addActionListener(this);
    	order2.setActionCommand("1");
    	order2.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	order2.setBounds(550, 250, 150, 50);
    	Order = new ButtonGroup();
    	Order.add(order1);
    	Order.add(order2);
    	
    	//名前の変更設定
    	JLabel labelPlayerName = new JLabel("名前を変更する場合は下に入力してください");
    	labelPlayerName.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayerName.setBounds(20, 300, 400, 50);

    	JLabel player1 = new JLabel("1人目");
    	player1.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player1.setBounds(50, 350, 150, 50);
    	JTextField textplayer1 = new JTextField("p1");
    	textplayer1.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer1.setHorizontalAlignment(JTextField.CENTER);
    	textplayer1.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer1.setBounds(50, 410, 150, 50);

    	JLabel player2 = new JLabel("2人目");
    	player2.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player2.setBounds(225, 350, 150, 50);
    	JTextField textplayer2 = new JTextField("p2");
    	textplayer2.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer2.setHorizontalAlignment(JTextField.CENTER);
    	textplayer2.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer2.setBounds(225, 410, 150, 50);

    	JLabel player3 = new JLabel("3人目");
    	player3.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player3.setBounds(400, 350, 150, 50);
    	JTextField textplayer3 = new JTextField("p3");
    	textplayer3.setToolTipText("半角は7文字、全角は4文字以内で入力してください");
    	textplayer3.setHorizontalAlignment(JTextField.CENTER);
    	textplayer3.setFont(new Font("SansSerif",Font.BOLD,20));
    	textplayer3.setBounds(400, 410, 150, 50);

    	JLabel player4 = new JLabel("4人目");
    	player4.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	player4.setBounds(575, 350, 150, 50);
    	JTextField textplayer4 = new JTextField("p4");
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
    	start.add(spinnerYear);
    	start.add(text);
    	start.add(labelPlayers);
    	start.add(count1);
    	start.add(count2);
    	start.add(count3);
    	start.add(count4);
    	start.add(count0);
    	start.add(labelPlayerName);
    	start.add(player1);
    	start.add(textplayer1);
    	start.add(player2);
    	start.add(textplayer2);
    	start.add(player3);
    	start.add(textplayer3);
    	start.add(player4);
    	start.add(textplayer4);
    	start.add(order1);
    	start.add(order2);
    	start.add(labelOrder);
    	start.add(startButton);

    	this.setVisible(true);

    	WaitThread wait = new WaitThread(10);
    	wait.start();
    	try {
    		wait.join();
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}

    	this.setVisible(false);

    	int[] list= {0,0};
    	list[0] = count;
    	list[1] = Integer.parseInt((String)textYear.getText());

    	setNames.add(textplayer1.getText());
    	setNames.add(textplayer2.getText());
    	setNames.add(textplayer3.getText());
    	setNames.add(textplayer4.getText());

    	return list;
  	}

	public void actionPerformed(ActionEvent e) {
		//プレイ人数決め
		String cnt = Count.getSelection().getActionCommand();
		count = Integer.valueOf(cnt);
		
		//順番決め
		String odr = Order.getSelection().getActionCommand();
		playerorder=Integer.valueOf(odr);
		if(PlayerOrder.size()!=0) {
			PlayerOrder.clear();
		}
		for(int i=0;i<4;i++) {
			PlayerOrder.add(i);
		}
		if(playerorder==0) {
			Collections.shuffle(PlayerOrder);//順番のシャッフル
			//System.out.println(PlayerOrder);
		}else {
			Collections.sort(PlayerOrder);//初期設定値
			//System.out.println(PlayerOrder);
		}
		//ゲーム開始
		String cmd = e.getActionCommand();
		if(cmd.equals("始める")) {
    		App.start();
    	}
	}

	public int getPlayerOrder(int index) {
		return PlayerOrder.get(index);
	}

	public String getPlayerName(int index) {
		return setNames.get(index);
	}
}
