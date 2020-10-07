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
import lifegame.game.object.map.information.Ginga;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.information.Station;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class AllGingaMapFrame extends FrameModel{
	private JLabel p1 = new JLabel();
	private JLabel p2 = new JLabel();
	private JLabel p3 = new JLabel();
	private JLabel p4 = new JLabel();
	private int distance=30;
	public AllGingaMapFrame() {
		this.setTitle("全体マップ");
		JLayeredPane maps = this.getLayeredPane();
		JButton closeButton = createButton(580,500,180,50,10,"戻る");
		this.getLayeredPane().setBackground(Color.ORANGE);
		maps.add(closeButton);
		for(Coordinates coor : Ginga.getAllCoordinates()) {
			maps.add(createMassInGinga(coor,distance),JLayeredPane.DEFAULT_LAYER,0);
			drawLineInGinga(this.getLayeredPane(),coor,distance,5);
		}
	}

	public void open() {
		JLayeredPane maps = this.getLayeredPane();
		if(ContainsEvent.isGingaMap(Player.getPlayer(0))) {
			p1 = createText(Player.getPlayer(0).getNowMass().getX()*distance-5, Player.getPlayer(0).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "1");
			p1.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isGingaMap(Player.getPlayer(1))) {
			p2 = createText(Player.getPlayer(1).getNowMass().getX()*distance+5, Player.getPlayer(1).getNowMass().getY()*distance-5, distance/3, distance/3, 5, "2");
			p2.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isGingaMap(Player.getPlayer(2))) {
			p3 = createText(Player.getPlayer(2).getNowMass().getX()*distance-5, Player.getPlayer(2).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "3");
			p3.setBackground(Color.BLACK);
		}
		if(ContainsEvent.isGingaMap(Player.getPlayer(3))) {
			p4 = createText(Player.getPlayer(3).getNowMass().getX()*distance+5, Player.getPlayer(3).getNowMass().getY()*distance+5, distance/3, distance/3, 5, "4");
			p4.setBackground(Color.BLACK);
		}
		maps.add(p1,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p2,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p3,JLayeredPane.PALETTE_LAYER,-1);
		maps.add(p4,JLayeredPane.PALETTE_LAYER,-1);
		/*
		//ゴール地点の色塗り
		Coordinates coor = Japan.getGoalCoor();
		Component component = maps.getComponentAt(coor.getX()*distance, coor.getY()*distance);
		component.setBackground(Color.MAGENTA);
		*/
		this.setVisible(true);
	}

	public void close() {
		this.setVisible(false);
		this.getLayeredPane().remove(p1);
		this.getLayeredPane().remove(p2);
		this.getLayeredPane().remove(p3);
		this.getLayeredPane().remove(p4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("戻る")) {
			FrameEvent.closeAllMap();
		}
		for(Station sta:Japan.getStationList()) {
			if(cmd.equals(sta.getName())) {
				FrameEvent.openPropertys(cmd,1);
			}
		}
	}
}
