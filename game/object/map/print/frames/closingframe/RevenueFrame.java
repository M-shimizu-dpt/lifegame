package lifegame.game.object.map.print.frames.closingframe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import lifegame.game.event.ClosingEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class RevenueFrame extends FrameModel{

	public RevenueFrame() {
		this.setTitle("収益");
	}

	public void open() {
		JLayeredPane revenue = this.getLayeredPane();

		JLabel profitLabel = createText(10,10,370,40,15,"今までの収益の推移");
		profitLabel.setBackground(Color.BLUE);
		revenue.add(profitLabel);
		//グラフ作成(左半分)
		for(float i=0;i<ClosingEvent.getProfitListSize();i++) {
			for(int j=0;j<4;j++) {
				JPanel graph = new JPanel();
				int x=(int)(300*((i+1)/ClosingEvent.getProfitListSize()));								//x座標を算出
				int y=(int)(500-(400*ClosingEvent.getProfitList((int)i)[j]/(ClosingEvent.maxProfit-ClosingEvent.minProfit)));	//y座標を算出
				graph.setBounds(x,y,5,5);													//（x,y）をプロット
				graph.setBackground(Color.YELLOW);
				revenue.add(graph);
				//System.out.println("x:" + String.valueOf(x) + "\ty:" + String.valueOf(y));
				if(i==ClosingEvent.getProfitListSize()-1) {//ぬるぽ回避
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
				int x2=(int)(300*((i+2)/ClosingEvent.getProfitListSize()));									//翌年のx座標を算出
				int y2=(int)(500-(400*ClosingEvent.getProfitList((int)i+1)[j]/(ClosingEvent.maxProfit-ClosingEvent.minProfit)));		//翌年のy座標を算出

				//System.out.println("x1:"+x+"\ty1:"+y1+"\tx2:"+x2+"\ty2:"+y2);

				int a = x2 - x1;
				int b = y2 - y1;
				int dx,dy,fraction;

				if(a < 0) {
					dx = -1;
				}else {
					dx = 1;
				}
				if(b < 0) {
					dy = -1;
				}else {
					dy = 1;
				}

				int c = Math.abs(a * 2);
				int d = Math.abs(b * 2);

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
						revenue.add(line,JLayeredPane.DEFAULT_LAYER,-1);
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
						revenue.add(line,JLayeredPane.DEFAULT_LAYER,-1);
					}
				}
			}
		}

		//グラフの具体的な数値(右半部)
		JLabel profitThisYearLabel = createText(400,10,370,40,15,"今年の収益");
		profitThisYearLabel.setBackground(Color.BLUE);
		revenue.add(profitThisYearLabel);
		for(int i=0;i<4;i++) {
			JLabel playerNameLabel = createText(400,110+(100*i),100,40,10,Player.players.get(i).getName());
			playerNameLabel.setBackground(Color.white);
			revenue.add(playerNameLabel,JLayeredPane.DEFAULT_LAYER,0);
			JLabel playerProfitLabel = createText(500,110+(100*i),100,40,10,String.valueOf(ClosingEvent.getProfitList(ClosingEvent.getProfitListSize()-1)[i]));
			playerProfitLabel.setBackground(Color.white);
			revenue.add(playerProfitLabel,JLayeredPane.DEFAULT_LAYER,0);
		}

		JButton closeButton = createButton(700,500,80,50,10,"閉じる");
		revenue.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		this.setVisible(true);
		setCloseFrame();

		ClosingEvent.aggregateAssets();
	}

	//指定のFrameを1秒後に閉じる
	public void setCloseFrame() {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closeRevenue();
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
			FrameEvent.closeRevenue();
		}
	}
}
