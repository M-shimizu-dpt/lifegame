package lifegame.game.object.map.print.frames.closing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import lifegame.game.event.ClosingEvent;
import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class AssetsFrame extends FrameModel{

	public AssetsFrame() {
		this.setTitle("総資産");
	}

	public void open() {
		JLayeredPane Assets = this.getLayeredPane();
		//System.out.println("x="+getProfitList(Size());
		JLabel assetsLabel = createText(10,10,370,40,15,"今までの総資産の推移");
		assetsLabel.setBackground(Color.BLUE);
		Assets.add(assetsLabel);

		for(float i=0;i<ClosingEvent.getAssetsListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x = (int)(300*((i+1)/ClosingEvent.getAssetsListSize()));
				int y = (int)(500-(400*ClosingEvent.getAssetsList((int)i)[j]/(ClosingEvent.maxAssets-ClosingEvent.minAssets)));
				graph.setBounds(x,y,5,5);
				graph.setBackground(Color.YELLOW);
				Assets.add(graph);
				//System.out.println("x:"+x+"\ty:"+y);
				if(i==ClosingEvent.getAssetsListSize()-1) {//ぬるぽ回避
					continue;
				}
				//グラフ線分
				/*
				 * (x1,y1):線分の始点
				 * (x2,y2):線分の終点
				 * a:x座標の増加分　b:y座標の増加分
				 * c:aを2倍し絶対値を取った値
				 * d:bを2倍し絶対値を取った値
				 * dx,dy:増減を格納用
				 * fraction:
				 *
				 */
				int x1=(int)x;
				int y1=(int)y;
				int x2=(int)(300*((i+2)/ClosingEvent.getAssetsListSize()));									//翌年のx座標
				int y2=(int)(500-(400*ClosingEvent.getAssetsList((int)i+1)[j]/(ClosingEvent.maxAssets-ClosingEvent.minAssets)));		//翌年のy座標

				//System.out.println("x1:"+x+"\ty1:"+y1+"\tx2:"+x2+"\ty2:"+y2);

				int a = x2 - x1;
				int b = y2 - y1;
				int dx,dy,fraction;
					if(a<0) {
					dx = -1;
				}else {
					dx = 1;
				}
				if(b<0) {
					dy = -1;
				}else {
					dy = 1;
				}

				int c=Math.abs(a*2);
				int d=Math.abs(b*2);

				if(c > d) {
					fraction = d - c/2;
					while(x1 != x2) {
						if(fraction >= 0) {
							y1 += dy;
							fraction -= c;
						}
						x1 += dx;
						fraction += d;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						Assets.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}else {
					fraction = c - d/2;
					while(y1 != y2) {
						if(fraction >= 0) {
							x1 += dx;
							fraction -= d;
						}
						y1 += dy;
						fraction += c;
						JPanel line = new JPanel();
						line.setBackground(Color.blue);
						line.setLocation(x1,y1);
						line.setSize(1,1);
						Assets.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}


		//グラフの具体的な数値(右半部)
		JLabel assetsThisYearLabel = createText(400,10,370,40,15,"今年の総資産");
		assetsThisYearLabel.setBackground(Color.BLUE);
		Assets.add(assetsThisYearLabel);
		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,Player.players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			Assets.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerAssetsLabel = createText(500,110+(100*i),100,40,10,String.valueOf(ClosingEvent.getAssetsList(ClosingEvent.getAssetsListSize()-1)[i]));
			playerAssetsLabel.setBackground(Color.white);
			Assets.add(playerAssetsLabel,JLayeredPane.DEFAULT_LAYER,0);
		}
		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		if(!ContainsEvent.isPlayer()) {
			closeButton.setEnabled(false);
		}
		Assets.add(closeButton,JLayeredPane.PALETTE_LAYER,0);

		this.setVisible(true);
		setCloseFrame();
		//ここまで総資産
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame() {
		if(!ContainsEvent.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closeAssets();
				}
			}, 3000);
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
		if(cmd.equals("閉じる")) {
			FrameEvent.closeAssets();
		}

	}
}
