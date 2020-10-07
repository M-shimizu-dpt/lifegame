package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.event.RandomEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Property;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class RandomFrame extends FrameModel{
	private int id;
	private int randInt;
	private double randDouble;

	public RandomFrame() {
		id=-1;
	}

	public void setRand(int rand) {
		this.randInt=rand;
	}
	public void setRand(double rand) {
		this.randDouble=rand;
	}
	public void setID(int id) {
		this.id=id;
	}

	public void open() {
		if(id==1) {
			open1(randDouble);
		}else if(id==2){
			open2(randInt);
		}
	}

	private void open1(double rand) {
		JLayeredPane random = this.getLayeredPane();
		JLabel text1=new JLabel();
		JLabel text2=new JLabel();
		JLabel text3=new JLabel();
		JButton closeButton = createButton(580,500,180,50,10,"閉じる");
		if(!ContainsEvent.isPlayer()) {
			closeButton.setEnabled(false);
		}
		random.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		if(rand < 0.1) {
			this.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を1/4失った");
		}else if(rand < 0.2) {
			this.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を半分失った");
		}else if(rand < 0.3) {
			this.setName("スリの銀一");
			text1 = createText(10,10,600,100,20,"スリの銀一が現れた！");
			text2 = createText(10,110,600,100,20,"スリの銀一「金は頂いていくぜ」");
			text3 = createText(10,210,600,100,20,"所持金を全て失った");
		}else if(rand < 0.4) {
			this.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"1億円もらった");
		}else if(rand < 0.5) {
			this.setName("お金の神様");
			text1 = createText(10,10,600,100,20,"お金の神様が現れた！");
			text2 = createText(10,110,600,100,20,"ふぉっふぉっふぉ…お金が欲しいと見える。いくらか授けてやろう");
			text3 = createText(10,210,600,100,20,"2億円もらった");
		}else if(rand < 0.6) {
			this.setName("しあわせの小鳥");
			text1 = createText(10,10,600,100,20,"ちゅんちゅんちゅん");
			text2 = createText(10,110,600,100,20,"ちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅんちゅん");
			text3 = createText(10,210,600,100,20,"ちゅんちゅんちゅん(一頭地を抜くカードをもらった)");
		}else if(rand < 0.7 && ContainsEvent.isOwners()) {
			this.setName("鋼鉄の人");
			text1 = createText(10,10,600,100,20,"全身赤い装甲に身を包んだアメリカの空飛ぶ天才発明家が現れた！");
			text2 = createText(10,110,600,100,20,"Destroying properties at random！Don't hold a grudge.");
			//誰かの物件の所有権を初期化
			for(Property property : Japan.getPropertys()) {
				if(ContainsEvent.isOwner(property)) {
					text3 = createText(10,210,600,100,20,"誰かの物件が破壊された(" + property.getOwner() + "の" + property.getName() + ")");
					break;
				}
			}
		}else if(rand < 0.8) {
			this.setName("偉い人");
			text1 = createText(10,10,600,100,20,"偉い人が現れた！");
			text2 = createText(10,110,600,100,20,"山形の開発工事を行いたいを思っているので所持金全部投資してください");
			if(new Random().nextInt(100) < 50) {
				text3 = createText(10,210,600,100,20,"事業が成功し、所持金が倍になります");
			}else {
				text3 = createText(10,210,600,100,20,"事業が失敗し、所持金が無くなります");
			}
		}else if(rand < 0.9) {
			this.setName("スキャンダル");
			text1 = createText(10,10,600,100,20,"若者とキャッキャウフフしていたのがばれた");
			text2 = createText(10,110,600,100,20,"世間体を気にして移動を自粛することにした");
			if(ContainsEvent.isEffect()) {
				text3 = createText(10,210,600,100,20,"移動距離が-3される");
			}else {
				text3 = createText(10,210,600,100,20,"3カ月の間、移動距離が-3される");
			}
		}else {
			this.setName("富士山");
			text1 = createText(10,10,600,100,20,Player.player.getName()+"「富士山に行きたい！！！」");
			text2 = createText(10,110,600,100,20,"富士山の登頂に成功し、気分が良くなった");
			text3 = createText(10,210,600,100,20,"登山費用として5000万円失った");
		}
		text1.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text2);
		text3.setHorizontalAlignment(SwingConstants.LEFT);
		random.add(text3);
		this.setVisible(true);
		setCloseFrame();
	}

	/*
	 * ランダムイベント
	 * ①臨時収入
	 * Player.playerが所有する物件の中から1件選び臨時収入が入る
	 *
	 * propertyに指定したスパン毎の部門属性を付与
	 * ownerがいるproeprty全てを取得するメソッドを用意
	 * その中から指定の部門を抽出
	 * 抽出したpropertyの臨時収入をownerに付与
	 *
	 * ・見た目
	 * 専用フレームを作る
	 * 必要な情報を記載
	 * 流れに合う箇所に記述する
	 *
	 */
	private void open2(int rndnum) {

		//System.out.println("year:"+App.year+"\tmonth:"+App.month+"\trndnum:"+rndnum);

		this.setTitle("トピックス");
		JLayeredPane Random2 = this.getLayeredPane();

		JLabel text1=new JLabel();
		JLabel text2=new JLabel();
		JLabel text3=new JLabel();
		JLabel text4=new JLabel();

		text1 = createText(10,10,600,100,20,"トピックスです");
    	text2 = createText(10,110,600,100,20,"全国の放送局で特集が放送されました！");
		text3 = createText(10,210,600,100,20,"テレビの影響はすごく,大きな収入が出ています。");
		//System.out.println("所持金");
		/*
		for(int i=0;i<4;i++) {
			System.out.println(Player.players.get(i).getName()+":"+Player.players.get(i).getMoney());
		}
		*/

		//物件の情報を取得
		for(Property property : Japan.getPropertys()) {
			//オーナーの有無の判断
			if(ContainsEvent.isOwner(property)) {
				//物件が1かどうか判断
				if(property.getGroup()==1) {
					//物件の選出
					text4 = createText(10,310,600,100,20,"臨時収入が入ります(" + property.getOwner() + "の" + property.getName() + ")");
					//System.out.println("臨時収入:"+property.getOwner()+"の"+ property.getName());
					//臨時収入を追加
					RandomEvent.random2Event(property,rndnum);
					break;
				}
			}
    	}
		//System.out.println("所持金");
		/*
		for(int i=0;i<4;i++) {
			System.out.println(Player.players.get(i).getName()+":"+Player.players.get(i).getMoney());
		}
		*/

		text1.setHorizontalAlignment(SwingConstants.LEFT);//左に寄せたいができない
		Random2.add(text1);
		text2.setHorizontalAlignment(SwingConstants.LEFT);
		Random2.add(text2);
		text3.setHorizontalAlignment(SwingConstants.LEFT);
		Random2.add(text3);
		text4.setHorizontalAlignment(SwingConstants.LEFT);
		Random2.add(text4);

		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		if(!ContainsEvent.isPlayer()) {
			closeButton.setEnabled(false);
		}
		closeButton .setActionCommand("閉じる2");
		Random2.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		this.setVisible(true);
    	setCloseFrame();
	}

	//指定のFrameを1秒後に閉じる
	private void setCloseFrame() {
		if(!ContainsEvent.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			if(id==1) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						FrameEvent.closeRandom();
					}
				}, 3000);
			}else if(id==2) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						FrameEvent.closeRandom2();
					}
				}, 3000);
			}
		}
	}

	@Override
	public void close() {
		this.setVisible(false);
		this.getLayeredPane().removeAll();
	}

	public void openSave() {
		this.setVisible(true);
	}

	public void closeSave() {
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("閉じる")) {
			FrameEvent.closeRandom();
		}else if(cmd.equals("閉じる2")) {
			FrameEvent.closeRandom2();
		}
	}
}