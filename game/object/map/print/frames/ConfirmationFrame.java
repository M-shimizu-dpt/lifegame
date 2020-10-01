package lifegame.game.object.map.print.frames;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import lifegame.game.event.FrameEvent;
import lifegame.game.object.Player;
import lifegame.game.object.map.print.frames.model.FrameModel;

public class ConfirmationFrame extends FrameModel{

	public ConfirmationFrame() {

	}

	public void open(String title,String article) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();
		List<String> articles = new ArrayList<String>();

		if(article.length()>35) {
			List<String> list = new ArrayList<String>();
			if(article.contains("\n")) {//改行文字毎に改行
				list.addAll(Arrays.asList(article.split("\n")));
			}else {
				list.add(article);
			}
			for(String longart:list) {//改行しても35文字を超える場合は超えたところで改行
				Matcher m = Pattern.compile("[\\s\\S]{1,35}").matcher(longart);
				while (m.find()) {
					articles.add(m.group());
				}
			}
		}else {
			articles.add(article);
		}
		if(articles.size()>13) System.out.println("はみ出ています");
		String artresult="<html><body>";
		for(String art : articles) {
			artresult = artresult + art + "<br />";
		}
		artresult=artresult+"</body></html>";
		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		JButton closeButton =createButton(700,500,70,50,10,"閉じる");
		confirmation.add(closeButton,JLayeredPane.PALETTE_LAYER);
	}

	public void open(String title,String article,int time) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();
		List<String> articles = new ArrayList<String>();

		if(article.length()>35) {
			List<String> list = new ArrayList<String>();
			if(article.contains("\n")) {//改行文字毎に改行
				list.addAll(Arrays.asList(article.split("\n")));
			}else {
				list.add(article);
			}
			for(String longart:list) {//改行しても35文字を超える場合は超えたところで改行
				Matcher m = Pattern.compile("[\\s\\S]{1,35}").matcher(longart);
				while (m.find()) {
					articles.add(m.group());
				}
			}
		}else {
			articles.add(article);
		}
		if(articles.size()>13) System.out.println("はみ出ています");
		String artresult="<html><body>";
		for(String art : articles) {
			artresult = artresult + art + "<br />";
		}
		artresult=artresult+"</body></html>";
		JLabel art = createText(0,0,800,600,20,artresult);
		art.setHorizontalAlignment(SwingConstants.LEFT);
		art.setVerticalAlignment(SwingConstants.TOP);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		this.setVisible(true);

		setCloseFrame(time);
	}

	public void open(String title,String article,int size,int time) {
		this.setTitle(title);
		JLayeredPane confirmation = this.getLayeredPane();
		String artresult="<html><body>"+article+"</body></html>";
		JLabel art = createText(0,0,800,600,size,artresult);
		confirmation.add(art,JLayeredPane.DEFAULT_LAYER);
		this.setVisible(true);

		setCloseFrame(time);
	}

	public void setCloseFrame(int time) {
		if(!Player.player.isPlayer()) {//コードの行数を減らすためにif文をここに記載(可読性を上げるなら呼び出し元に書いた方がいいかも)
			Timer timer = new Timer(false);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					FrameEvent.closePopUp();
				}
			}, time);
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
			FrameEvent.closePopUp();
		}
	}

}