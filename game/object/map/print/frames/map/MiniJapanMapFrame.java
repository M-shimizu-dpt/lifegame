package lifegame.game.object.map.print.frames.map;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import lifegame.game.event.ContainsEvent;
import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Station;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class MiniJapanMapFrame extends FrameModel{
	private JLabel p1 = new JLabel();
	private JLabel p2 = new JLabel();
	private JLabel p3 = new JLabel();
	private JLabel p4 = new JLabel();
	int x,y;
	int distance=70;
	public MiniJapanMapFrame() {
		this.setTitle("詳細マップ");
		JLayeredPane maps = this.getLayeredPane();
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		JButton right = createButton(730,250,50,40,10,"→");
		JButton left = createButton(10,250,50,40,10,"←");
		JButton top = createButton(380,10,50,40,10,"↑");
		JButton bottom = createButton(380,510,50,40,10,"↓");
		right.setBackground(Color.WHITE);
		left.setBackground(Color.WHITE);
		top.setBackground(Color.WHITE);
		bottom.setBackground(Color.WHITE);
		this.getLayeredPane().setBackground(Color.ORANGE);
		maps.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		maps.add(right,JLayeredPane.PALETTE_LAYER,0);
		maps.add(left,JLayeredPane.PALETTE_LAYER,0);
		maps.add(top,JLayeredPane.PALETTE_LAYER,0);
		maps.add(bottom,JLayeredPane.PALETTE_LAYER,0);
		x=0;
		y=0;
		for(Coordinates coor : Japan.getAllCoordinates()) {
			if(ContainsEvent.isStation(coor)) {//駅の座標が来たら
				JButton button = createButton(coor.getX()*distance-20,coor.getY()*distance-5,60,30,8,Japan.getStationName(coor));
				maps.add(button,JLayeredPane.DEFAULT_LAYER,0);//駅の名前を出力するためにMapの構成を考え直す
			}else {
				maps.add(createMassInJapan(coor,distance),JLayeredPane.DEFAULT_LAYER,0);
			}
			drawLineInJapan(maps,coor,distance,10);
		}
	}

	public void open() {
		JLayeredPane maps = this.getLayeredPane();
		if(ContainsEvent.isNormalMap(Player.getPlayer(0))) {
			p1 = createText(Player.players.get(0).getNowMass().getX()*distance-15+x, Player.players.get(0).getNowMass().getY()*distance-5+y, 20, 10, 10, "1");
			p1.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isNormalMap(Player.getPlayer(1))) {
			p2 = createText(Player.players.get(1).getNowMass().getX()*distance+15+x, Player.players.get(1).getNowMass().getY()*distance-5+y, 20, 10, 10, "2");
			p2.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isNormalMap(Player.getPlayer(2))) {
			p3 = createText(Player.players.get(2).getNowMass().getX()*distance-15+x, Player.players.get(2).getNowMass().getY()*distance+15+y, 20, 10, 10, "3");
			p3.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isNormalMap(Player.getPlayer(3))) {
			p4 = createText(Player.players.get(3).getNowMass().getX()*distance+15+x, Player.players.get(3).getNowMass().getY()*distance+15+y, 20, 10, 10, "4");
			p4.setBackground(Color.BLACK);
		}

		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);

		this.setVisible(true);
	}

	public void setGoalColor() {
		JLayeredPane maps = this.getLayeredPane();
		for(int i=0;i<maps.getComponentCount();i++) {
			if(maps.getComponent(i).getName()==null)continue;
			if(maps.getComponent(i).getName().equals(Japan.getGoalName())) {
				maps.getComponent(i).setBackground(Color.MAGENTA);
				break;
			}
		}
	}

	public void resetGoalColor() {
		JLayeredPane maps = this.getLayeredPane();
		for(int i=0;i<maps.getComponentCount();i++) {
			if(maps.getComponent(i).getName()==null)continue;
			if(maps.getComponent(i).getName().equals(Japan.getGoalName())) {
				maps.getComponent(i).setBackground(Color.WHITE);
				break;
			}
		}
	}

	public void close() {
		this.setVisible(false);
		this.getLayeredPane().remove(p1);
		this.getLayeredPane().remove(p2);
		this.getLayeredPane().remove(p3);
		this.getLayeredPane().remove(p4);
	}

	private void moveMaps(String cmd) {
		JLayeredPane maps = this.getLayeredPane();
		int x=0,y=0;
		for(int i=0;i<maps.getComponentCount();i++) {
			if(i>=0&&i<5) {//標準装備のコンポーネント以外
				continue;
			}
			if(maps.getComponent(i).getX() < 0 && cmd.equals("←")) {//左にフレームアウトしているコンポーネントが存在しない場合それ以上左に行けないようにする
				x=50;
				this.x+=50;
				break;
			}
			if(maps.getComponent(i).getX() > 670 && cmd.equals("→")) {//右にフレームアウトしているコンポーネントが存在しない場合それ以上右に行けないようにする
				x=-50;
				this.x-=50;
				break;
			}
			if(maps.getComponent(i).getY() < 0 && cmd.equals("↑")) {//上にフレームアウトしているコンポーネントが存在しない場合それ以上上に行けないようにする
				y=50;
				this.y+=50;
				break;
			}
			if(maps.getComponent(i).getY() > 470 && cmd.equals("↓")) {//下にフレームアウトしているコンポーネントが存在しない場合それ以上下に行けないようにする
				y=-50;
				this.y-=50;
				break;
			}
		}
		for(int i=0;i<maps.getComponentCount();i++) {
			if(!(i>=0&&i<5)) {//移動・閉じるボタン以外を動かす
				maps.getComponent(i).setLocation(maps.getComponent(i).getX()+x,maps.getComponent(i).getY()+y);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("→") || cmd.equals("←") || cmd.equals("↑") || cmd.equals("↓")) {
			moveMaps(cmd);
		}if(cmd.equals("戻る")) {
			FrameEvent.closeMiniMap();
		}
		for(Station sta:Japan.getStationList()) {
			if(cmd.equals(sta.getName())) {
				FrameEvent.openPropertys(cmd,0);
			}
		}
	}
}
