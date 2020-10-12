package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import lifegame.game.event.FrameEvent;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class WarpFrame extends FrameModel{
	public WarpFrame() {
		super.setTitle("ワープ");
		super.setSize(300,300);
		JLayeredPane warp = super.getLayeredPane();
		JButton closeButton = super.createButton(100,110,100,50,10,"出る");
		warp.add(closeButton,JLayeredPane.PALETTE_LAYER,0);
		JButton warpButton = super.createButton(100,10,100,50,10, "ワープ");
		warp.add(warpButton,JLayeredPane.PALETTE_LAYER,0);
	}

	@Override
	public void open() {
		super.setVisible(true);
	}

	@Override
	public void close() {
		super.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("出る")) {
			FrameEvent.closeWarp();
		}else if(cmd.equals("ワープ")) {
			FrameEvent.openSelectWarp();
		}
	}
}
