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

import lifegame.game.event.WaitThread;
import lifegame.game.main.App;

public class settingYear extends JFrame implements ActionListener {
	
	public settingYear() {
		this.setTitle("桃大郎電鉄");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}
	
	public int year;
	
	public int open() {
		JLayeredPane start = this.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
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
    	
    	JButton startButton = new JButton("OK");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	
    	this.setVisible(true);
    	start.add(labelTitle);
    	start.add(labelYear);
    	start.add(spinnerYear);
    	start.add(text);
    	start.add(startButton);
    	
    	year=Integer.parseInt((String)textYear.getText());
    	
    	return year;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("OK")) {
			this.setVisible(false);
    	}
	}
	
}
