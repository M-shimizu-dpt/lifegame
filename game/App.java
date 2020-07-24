package lifegame.game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

public class App  implements ActionListener{
	private Boolean flag = false;

    public static void main(String[] args) {
        App app = new App();
    	app.run();
    }

    private void run() {
    	JFrame initFrame = new JFrame("桃大郎電鉄");
    	initFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
    	initFrame.setSize(800,600);
    	initFrame.setLayout(null);
    	initFrame.setLocationRelativeTo(null);
    	JLayeredPane init = initFrame.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(280, 10, 400, 60);
    	JLabel labelYear = new JLabel("何年プレイしますか？");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 50, 200, 50);
    	JTextField textYear = new JTextField("3");
    	textYear.setBounds(20, 100, 200, 50);
    	JLabel labelPlayers = new JLabel("CPUの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 150, 300, 50);
    	JTextField textPlayers = new JTextField("3");
    	textPlayers.setBounds(20, 200, 200, 50);
    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	init.add(labelTitle);
    	init.add(labelYear);
    	init.add(textYear);
    	init.add(labelPlayers);
    	init.add(textPlayers);
    	init.add(startButton);

    	initFrame.setVisible(true);
    	while(!flag) {
    		try {
    			Thread.sleep(100);
    		}catch(InterruptedException e) {

    		}
    	}
    	initFrame.setVisible(false);
    	
    	assert(Integer.parseInt(textPlayers.getText())>=0 && Integer.parseInt(textPlayers.getText())<=3);
    	assert(Integer.parseInt(textYear.getText())>0 && Integer.parseInt(textYear.getText())<=100);
    	
    	start(Integer.parseInt(textYear.getText()));//将来的にはCPUを実装しその人数を入力できるようにする
    }

    private void start(int endYear) {
    	new Window(endYear);
    }
    public void actionPerformed(ActionEvent e){
    	String cmd = e.getActionCommand();
    	if(cmd.equals("始める")) {
    		flag=true;
    	}
    }
}