package lifegame.game.object.map.print.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.event.WarpEvent;
import lifegame.game.object.map.information.Japan;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class SelectWarpFrame extends FrameModel{
	@SuppressWarnings("rawtypes")
	JList list;
	JScrollPane sp;
	public SelectWarpFrame() {
		super.setTitle("ワープ");
		super.setSize(300,400);
		JLayeredPane warp = super.getLayeredPane();
		JButton closeButton = super.createButton(100,210,100,50,10,"戻る");
		warp.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JButton warpButton = super.createButton(100,110,100,50,10, "ワープ");
		warp.add(warpButton,JLayeredPane.PALETTE_LAYER,0);
	}

	@Override
	public void open() {
		list = createList(10,10,200,100,10,Japan.getWarpNameList());
		sp = new JScrollPane();
	    sp.getViewport().setView(list);
	    sp.setSize(200,80);

	    super.getLayeredPane().add(sp);

	    JLabel label = new JLabel();
	    JPanel p2 = new JPanel();
	    p2.add(label);

	    super.getLayeredPane().add(p2, BorderLayout.SOUTH);
		super.setVisible(true);
	}

	@Override
	public void close() {
		super.setVisible(false);
		super.remove(sp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("戻る")) {
			FrameEvent.closeSelectWarp();
		}else if(cmd.equals("ワープ")) {
			WarpEvent.warp((String) list.getSelectedValue());
		}
	}

}
