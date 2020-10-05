package lifegame.game.object.map.print.frames.property;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lifegame.game.event.FrameEvent;
import lifegame.game.event.SaleEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class BuyPropertyFrame extends FrameModel{
	private int id=-1;
	public BuyPropertyFrame() {

	}

	public int getID() {
		return this.id;
	}

	//物件購入・増築処理
	private void buyPropertys(String name, int index) {
		SaleEvent.buyPropertys(name, index);

		//System.out.println(Japan.getStaInProperty(name,index).getName()+"を購入"+"("+index+")");
		this.setVisible(false);
		this.getContentPane().removeAll();
		open(name,id);
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getContentPane().removeAll();
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame() {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					FrameEvent.closePropertys();
				}
			}, 1000);
		}
	}

	//駅の物件情報を表示
	public void open(String name,int id) {
		this.id=id;
		this.setTitle(name + "の物件情報");
		this.setSize(800, 35*Japan.getStaInPropertySize(name)+150);

		Container propertys = this.getContentPane();
		JButton closeButton = createButton(580,35*Japan.getStaInPropertySize(name)+50,180,50,10,"閉じる");
		closeButton.setActionCommand("物件情報を閉じる");

		JPanel info = new JPanel();
		info.setBounds(10, 10, 780, 40);
		info.setLayout(null);
		info.add(createText(150,10,200,40,20,"物件名"));
		info.add(createText(400,10,150,40,20,"値段"));
		info.add(createText(550,10,100,40,20,"利益率"));
		info.add(createText(650,10,100,40,20,"所有者"));
		if(Japan.getStation(name).isMono()) {
			JLabel label = createText(750,10,30,40,20,"独");
			label.setBackground(Color.RED);
			info.add(label);
		}
		propertys.add(info);
		for(int i=0;i<Japan.getStaInPropertySize(name);i++) {
			//JPanel property = new JPanel();
			//property.setBounds(10, 15+(i+1)*35, 40, 15);
			//property.setLayout(null);
			Property property = Japan.getStaInProperty(name,i);
			JButton buyButton = createButton(20,15+(i+1)*35,80,30,10,"購入");

			if(property.getLevel()>=2 || (!property.getOwner().equals("") && !property.getOwner().equals(Player.player.getName()))
					|| Player.player.getMoney()<property.getAmount() || id!=2) {
				buyButton.setEnabled(false);
			}

			if(Japan.alreadys.contains(property.getName()+i)) {
				for(String already:Japan.alreadys) {
					if(already.equals(property.getName()+i)) {
						buyButton.setEnabled(false);
						break;
					}
				}
			}
			buyButton.setActionCommand(name+"b:"+i);
			propertys.add(buyButton);
			int rate = property.getRate();//利益率(3段階)
			propertys.add(createText(150,10+(i+1)*35,200,40,15,property.getName()));
			if(property.getAmount()<10000) {
				propertys.add(createText(400,10+(i+1)*35,150,40,15,property.getAmount()+"万円"));
			}else if(property.getAmount()%10000==0){
				propertys.add(createText(400,10+(i+1)*35,150,40,15,property.getAmount()/10000+"億円"));
			}else {//今登録している物件では呼ばれないかも
				propertys.add(createText(400,10+(i+1)*35,150,40,15,property.getAmount()/10000+"億"+property.getAmount()%10000+"万円"));
			}
			propertys.add(createText(550,10+(i+1)*35,100,40,15,rate + "%"));
			propertys.add(createText(650,10+(i+1)*35,100,40,15,property.getOwner()));
			//property.setVisible(true);
			//propertys.add(property);
		}
		propertys.add(closeButton);

		if(!Player.player.isPlayer()) {
			SaleEvent.buyPropertysCPU(name);
		}
		setCloseFrame();

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String pre[] = cmd.split(":");
		if(cmd.equals("物件情報を閉じる")) {
			FrameEvent.closePropertys();
		}
		for(int i=0;i<Japan.getStationSize();i++) {
			if(pre[0].equals(Japan.getStationName(Japan.getStationCoor(i))+"b")) {//物件を購入
				buyPropertys(pre[0].substring(0, pre[0].length()-1),Integer.parseInt(pre[1]));
				break;
			}
		}
	}
}

