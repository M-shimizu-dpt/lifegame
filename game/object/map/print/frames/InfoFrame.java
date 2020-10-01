package lifegame.game.object.map.print.frames;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class InfoFrame extends FrameModel{
	public InfoFrame() {
		this.setTitle("会社情報");
	}

	public void open() {
		//会社情報の表示
		Container info = this.getContentPane();
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		JLabel titleName = createText(20,20,100,100,20,"名前");
		JLabel titleMoney = createText(120,20,100,100,20,"所持金");
		JLabel player1Name = createText(20,120,100,100,20,Player.players.get(0).getName());
		JLabel player2Name = createText(20,220,100,100,20,Player.players.get(1).getName());
		JLabel player3Name = createText(20,320,100,100,20,Player.players.get(2).getName());
		JLabel player4Name = createText(20,420,100,100,20,Player.players.get(3).getName());
		JLabel player1Money;
		JLabel player2Money;
		JLabel player3Money;
		JLabel player4Money;
		if(Player.players.get(0).getMoney()<10000) {
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney() + "万円");
		}else if(Player.players.get(0).getMoney()%10000==0){
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney() + "億円");
		}else {
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney()/10000 + "億円" + Player.players.get(0).getMoney()%10000 + "万円");
		}
		if(Player.players.get(1).getMoney()<10000) {
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney() + "万円");
		}else if(Player.players.get(1).getMoney()%10000==0){
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney() + "億円");
		}else {
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney()/10000 + "億円" + Player.players.get(1).getMoney()%10000 + "万円");
		}
		if(Player.players.get(2).getMoney()<10000) {
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney() + "万円");
		}else if(Player.players.get(2).getMoney()%10000==0){
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney() + "億円");
		}else {
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney()/10000 + "億円" + Player.players.get(2).getMoney()%10000 + "万円");
		}
		if(Player.players.get(3).getMoney()<10000) {
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney() + "万円");
		}else if(Player.players.get(3).getMoney()%10000==0){
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney() + "億円");
		}else {
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney()/10000 + "億円" + Player.players.get(3).getMoney()%10000 + "万円");
		}
		info.add(titleName);
		info.add(titleMoney);
		info.add(player1Name);
		info.add(player1Money);
		info.add(player2Name);
		info.add(player2Money);
		info.add(player3Name);
		info.add(player3Money);
		info.add(player4Name);
		info.add(player4Money);
		info.add(closeButton);
		this.setVisible(true);
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getContentPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("戻る")) {
			FrameEvent.closeInfo();
		}
	}
}
