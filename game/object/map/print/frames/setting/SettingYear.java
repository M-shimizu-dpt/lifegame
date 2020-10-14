package lifegame.game.object.map.print.frames.setting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import lifegame.game.event.FrameEvent;

public class SettingYear extends JFrame implements ActionListener {
	
	public SettingYear() {
		this.setTitle("挑大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	public int year;
	JFormattedTextField textYear;
	
	public void open() {
		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("プレイ年数設定");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(200, 50, 400, 60);
    	//年数設定
    	JLabel labelYear = new JLabel("何年プレイしますか？(１～１００年)");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 200, 400, 50);
    	JLabel text =new JLabel("年");
    	text.setFont(new Font("SansSerif", Font.ITALIC, 80));
    	text.setBounds(500, 300, 100, 100);
    	
    	JSpinner spinnerYear = new JSpinner(new SpinnerNumberModel(3,1,100,1));
    	spinnerYear.setFont(new Font("SansSerif",Font.BOLD,80));
    	spinnerYear.setBounds(350, 300, 150, 100);
    	JSpinner.NumberEditor model = new JSpinner.NumberEditor(spinnerYear);
    	spinnerYear.setEditor(model);
    	textYear = model.getTextField();
    	textYear.setEditable(false);
    	textYear.setBackground(Color.white);
    	
    	JButton startButton = new JButton("OK");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(580,490,180,60);
    	startButton.addActionListener(this);
    	JButton backButton = new JButton("戻る");
    	backButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	backButton.setBounds(20,490,180,60);
    	backButton.addActionListener(this);
    	
    	this.setVisible(true);
    	start.add(labelTitle);
    	start.add(labelYear);
    	start.add(spinnerYear);
    	start.add(text);
    	start.add(backButton);
    	start.add(startButton);
    	
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("OK")) {
			year=Integer.parseInt((String)textYear.getText());
			FrameEvent.setPlayYear();
			FrameEvent.openCheck();
			this.setVisible(false);
			this.getLayeredPane().removeAll();
    	}
		else {
    		FrameEvent.openSettingPlayName();
    		this.setVisible(false);
    		this.getLayeredPane().removeAll();
    	}
	}
	
}
