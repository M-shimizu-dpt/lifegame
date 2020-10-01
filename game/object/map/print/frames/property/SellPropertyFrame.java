package lifegame.game.object.map.print.frames.property;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.event.SaleEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class SellPropertyFrame extends FrameModel{

	public SellPropertyFrame() {
		this.setTitle("売却");
	}

	public void open() {
		int takeProCount=0;
		int i=0;
		JLayeredPane sellStation = this.getLayeredPane();
		sellStation.add(createText(150,10,200,40,20,"物件名"));
		sellStation.add(createText(400,10,150,40,20,"値段"));
		sellStation.add(createText(550,10,100,40,20,"利益率"));
		sellStation.add(createText(650,10,100,40,20,"所有者"));
		for(Property property:Player.player.getPropertys()) {
			takeProCount++;
			JButton sellButton = createButton(80,15+(takeProCount+1)*35,60,30,10,"売却");
			sellButton.setActionCommand(property.getName()+"s:"+i);

			sellStation.add(sellButton);
			int rate = property.getRate();//利益率(3段階)
			int pMoney = property.getAmount()/2;
			sellStation.add(createText(150,10+(i+1)*35,200,40,15,property.getName()));
			if(pMoney<10000) {
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney+"万円"));
			}else if(pMoney%10000==0){
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億円"));
			}else {//今登録している物件では呼ばれないかも
				sellStation.add(createText(400,10+(i+1)*35,150,40,15,pMoney/10000+"億"+pMoney%10000+"万円"));
			}
			sellStation.add(createText(550,10+(i+1)*35,100,40,15,rate + "%"));
			sellStation.add(createText(650,10+(i+1)*35,100,40,15,property.getOwner()));
			i++;
		}
		this.setSize(800, 35*Player.player.getPropertys().size()+150);

		this.setVisible(true);

		if(!Player.player.isPlayer()) {
			Player.player.sellPropertyCPU(this);
		}
	}

	//物件売却処理
	public void sellPropertys(Property property) {
		SaleEvent.sellPropertys(property);

		if(Player.player.getPropertys().size()>0) {
			this.setVisible(false);
			this.getLayeredPane().removeAll();
			open();
		}else {
			FrameEvent.closeSellProperty();
		}
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		String pre[] = cmd.split(":");
		if(pre.length==2) {
			for(Property property:Player.player.getPropertys()) {
				if(pre[0].equals(property.getName()+"s")) {//物件を売却
					sellPropertys(property);
					break;
				}
			}
		}
	}
}
