package lifegame.game.object.map.print.frames;

import java.awt.Container;
import java.awt.event.ActionEvent;

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
		JLabel player1Name = createText(20,120,100,100,20,Player.players.get(0).getName());
		JLabel player2Name = createText(20,220,100,100,20,Player.players.get(1).getName());
		JLabel player3Name = createText(20,320,100,100,20,Player.players.get(2).getName());
		JLabel player4Name = createText(20,420,100,100,20,Player.players.get(3).getName());
		JLabel player1Money;
		JLabel player2Money;
		JLabel player3Money;
		JLabel player4Money;
		JLabel player1Assets;
		JLabel player2Assets;
		JLabel player3Assets;
		JLabel player4Assets;
		JLabel player1RestMass;
		JLabel player2RestMass;
		JLabel player3RestMass;
		JLabel player4RestMass;
		
		
		//所持金情報
		if(Player.players.get(0).getMoney()<10000) {
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney() + "万円");
		}else if(Player.players.get(0).getMoney()%10000==0){
			player1Money = createText(120,120,100,100,20,Player.players.get(0).getMoney()/10000 + "億円");
		}else {
			player1Money = createText(120,120,150,100,20,Player.players.get(0).getMoney()/10000 + "億　" + Player.players.get(0).getMoney()%10000 + "万円");
		}
		if(Player.players.get(1).getMoney()<10000) {
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney() + "万円");
		}else if(Player.players.get(1).getMoney()%10000==0){
			player2Money = createText(120,220,100,100,20,Player.players.get(1).getMoney()/10000 + "億円");
		}else {
			player2Money = createText(120,220,150,100,20,Player.players.get(1).getMoney()/10000 + "億　" + Player.players.get(1).getMoney()%10000 + "万円");
		}
		if(Player.players.get(2).getMoney()<10000) {
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney() + "万円");
		}else if(Player.players.get(2).getMoney()%10000==0){
			player3Money = createText(120,320,100,100,20,Player.players.get(2).getMoney()/10000 + "億円");
		}else {
			player3Money = createText(120,320,150,100,20,Player.players.get(2).getMoney()/10000 + "億　" + Player.players.get(2).getMoney()%10000 + "万円");
		}
		if(Player.players.get(3).getMoney()<10000) {
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney() + "万円");
		}else if(Player.players.get(3).getMoney()%10000==0){
			player4Money = createText(120,420,100,100,20,Player.players.get(3).getMoney()/10000 + "億円");
		}else {
			player4Money = createText(120,420,150,100,20,Player.players.get(3).getMoney()/10000 + "億　" + Player.players.get(3).getMoney()%10000 + "万円");
		}
		
		//資産情報
		ClosingEvent.nowAllAssets();
		if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])<10000) {
			player1Assets = createText(270,120,100,100,20,String.valueOf(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])+"万円");
		}else if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])%10000 == 0) {
			player1Assets = createText(270,120,100,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])/10000)+"億円");
		}else {
			player1Assets = createText(270,120,150,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])/10000)+"億"+String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[0])%10000)+"万円");
		}
		if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])<10000) {
			player2Assets = createText(270,220,100,100,20,String.valueOf(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])+"万円");
		}else if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])%10000 == 0) {
			player2Assets = createText(270,220,100,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])/10000)+"億円");
		}else {
			player2Assets = createText(270,220,150,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])/10000)+"億"+String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[1])%10000)+"万円");
		}
		if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])<10000) {
			player3Assets = createText(270,320,100,100,20,String.valueOf(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])+"万円");
		}else if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])%10000 == 0) {
			player3Assets = createText(270,320,100,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])/10000)+"億円");
		}else {
			player3Assets = createText(270,320,150,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])/10000)+"億"+String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[2])%10000)+"万円");
		}
		if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])<10000) {
			player4Assets = createText(270,420,100,100,20,String.valueOf(ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])+"万円");
		}else if((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])%10000 == 0) {
			player4Assets = createText(270,420,100,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])/10000)+"億円");
		}else {
			player4Assets = createText(270,420,150,100,20,String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])/10000)+"億"+String.valueOf((ClosingEvent.getNowAssetsList(ClosingEvent.getNowAssetsListSize()-1)[3])%10000)+"万円");
		}
		
		//目的地までの残りマス数
		player1RestMass = createText(420,120,100,100,20,String.valueOf(Player.players.get(0).getGoalDistance())+"マス");
		player2RestMass = createText(420,220,100,100,20,String.valueOf(Player.players.get(1).getGoalDistance())+"マス");
		player3RestMass = createText(420,320,100,100,20,String.valueOf(Player.players.get(2).getGoalDistance())+"マス");
		player4RestMass = createText(420,420,100,100,20,String.valueOf(Player.players.get(3).getGoalDistance())+"マス");
		
		info.add(titleName);
		info.add(titleMoney);
		info.add(titleNowAssets);
		info.add(titleRestMass);
		info.add(player1Name);
		info.add(player1Money);
		info.add(player1Assets);
		info.add(player1RestMass);
		info.add(player2Name);
		info.add(player2Money);
		info.add(player2Assets);
		info.add(player2RestMass);
		info.add(player3Name);
		info.add(player3Money);
		info.add(player3Assets);
		info.add(player3RestMass);
		info.add(player4Name);
		info.add(player4Money);
		info.add(player4Assets);
		info.add(player4RestMass);
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
