package lifegame.game.object.map.print.frames;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import lifegame.game.event.ClosingEvent;
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
			nameList.add(createText(20,100*i+120,100,100,20,Player.players.get(i).getName()));
			//所持金情報
			if(Player.players.get(i).getMoney()<10000) {
				moneyList.add(createText(120,100*i+120,100,100,20,Player.players.get(i).getMoney() + "万円"));
			}else if(Player.players.get(i).getMoney()%10000==0){
				moneyList.add(createText(120,100*i+120,100,100,20,Player.players.get(i).getMoney()/10000 + "億円"));
			}else {
				moneyList.add(createText(120,100*i+120,150,100,20,Player.players.get(i).getMoney()/10000 + "億　" + Player.players.get(i).getMoney()%10000 + "万円"));
			}
			//資産情報
			if(i==0)ClosingEvent.nowAllAssets();
			if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])<10000) {
				assetsList.add(createText(270,100*i+120,100,100,20,String.valueOf(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])+"万円"));
			}else if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])%10000 == 0) {
				assetsList.add(createText(270,100*i+120,100,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])/10000)+"億円"));
			}else {
				assetsList.add(createText(270,100*i+120,150,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])/10000)+"億"+String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[i])%10000)+"万円"));
			}
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
