package lifegame.game.object.map.print.frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import lifegame.game.event.ClosingEvent;
import lifegame.game.event.ContainsEvent;
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
		JLabel titleMoney = createText(120,20,150,100,20,"　所持金　");
		JLabel titleNowAssets = createText(270,20,150,100,20,"現在の資産");
		JLabel titleRestMass = createText(420,20,100,100,20,"残りマス");
		ArrayList <JLabel> nameList = new ArrayList<JLabel>();
		ArrayList <JLabel> moneyList = new ArrayList<JLabel>();
		ArrayList <JLabel> assetsList = new ArrayList<JLabel>();
		ArrayList <JLabel> restMassList = new ArrayList<JLabel>();
		for(int i=0;i<4;i++) {
			if(ContainsEvent.isBinboPlayer(Player.getPlayer(i))) {
				JLabel label = createText(570,100*i+120,100,100,20,"ボンビー");
				label.setBackground(Color.RED);
				nameList.add(label);
			}
			nameList.add(createText(20,100*i+120,100,100,20,Player.players.get(i).getName()));
			//所持金情報
			moneyList.add(createText(120,100*i+120,100,100,20,FrameEvent.convertMoney(Player.players.get(i).getMoney())));
			//資産情報
			if(i==0)ClosingEvent.nowAllAssets();
			assetsList.add(createText(270,100*i+120,100,100,20,FrameEvent.convertMoney(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])));
			//目的地までの残りマス数
			restMassList.add(createText(420,100*i+120,100,100,20,String.valueOf(Player.players.get(i).getGoalDistance())+"マス"));
		}

		for(JLabel name:nameList) {
			info.add(name);
		}
		for(JLabel money:moneyList) {
			info.add(money);
		}
		for(JLabel assets:assetsList) {
			info.add(assets);
		}
		for(JLabel restMass:restMassList) {
			info.add(restMass);
		}
		info.add(titleName);
		info.add(titleMoney);
		info.add(titleNowAssets);
		info.add(titleRestMass);
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
