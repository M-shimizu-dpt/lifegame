package lifegame.game.object.map.print.frames.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lifegame.game.event.ContainsEvent;
import lifegame.game.object.map.information.Coordinates;
import lifegame.game.object.map.information.Ginga;
import lifegame.game.object.map.information.Japan;

public abstract class FrameModel extends JFrame implements ActionListener{
	protected boolean defaultSize=false;
	protected boolean cardSize=false;
	protected boolean goalSize=false;

	public FrameModel() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(800, 600);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
	}

	public abstract void open();
	public abstract void close();

	//ボタンを作成
	protected JButton createButton(int x,int y,int w,int h,int size,String name) {
		JButton button = new JButton(name);
		button.setFont(new Font("SansSerif", Font.ITALIC, size));
		button.setBounds(x,y,w,h);
		button.addActionListener(this);
		button.setActionCommand(name);
		button.setName(name);
		return button;
	}
	protected JButton createButton(Coordinates coor,int w,int h,int size,String name) {
		JButton button = new JButton(name);
		button.setFont(new Font("SansSerif", Font.ITALIC, size));
		button.setBounds(coor.getX(),coor.getY(),w,h);
		button.addActionListener(this);
		button.setActionCommand(name);
		button.setName(name);
		return button;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected JList createList(int x,int y,int w,int h,int size,ArrayList<String> name) {
		JList list = new JList(name.toArray());
		list.setFont(new Font("SansSerif", Font.ITALIC, size));
		list.setBounds(x,y,w,h);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected JList createList(Coordinates coor,int w,int h,int size,ArrayList<String> name) {
		JList list = new JList(name.toArray());
		list.setFont(new Font("SansSerif", Font.ITALIC, size));
		list.setBounds(coor.getX(),coor.getY(),w,h);
		return list;
	}

	//textを作成
	public JLabel createText(int x,int y,int w,int h,int size,String name) {
		JLabel text = new JLabel(name,SwingConstants.CENTER);
		text.setOpaque(true);
		text.setBounds(x, y, w, h);
		text.setFont(new Font("SansSerif", Font.ITALIC, size));
		text.setName(name);
		return text;
	}
	public JLabel createText(Coordinates coor,int w,int h,int size,String name) {
		JLabel text = new JLabel(name,SwingConstants.CENTER);
		text.setOpaque(true);
		text.setBounds(coor.getX(), coor.getY(), w, h);
		text.setFont(new Font("SansSerif", Font.ITALIC, size));
		text.setName(name);
		return text;
	}

	//imageを作成
	public JLabel createImage(int x,int y,int w,int h,int size,String image) {
		ImageIcon icon = new ImageIcon(image);
		assert icon==null : "null";
		System.out.println("h:"+icon.getIconHeight()+"   w:"+icon.getIconWidth());
		JLabel images = new JLabel(icon);
		images.setOpaque(true);
		images.setBounds(x, y, w, h);
		return images;
	}
	public JLabel createImage(Coordinates coor,int w,int h,int size,String image) {
		ImageIcon icon = new ImageIcon(image);
		assert icon==null : "null";
		System.out.println("h:"+icon.getIconHeight()+"   w:"+icon.getIconWidth());
		JLabel images = new JLabel(icon);
		images.setOpaque(true);
		images.setBounds(coor.getX(), coor.getY(), w, h);
		return images;
	}

	protected void drawLineInJapan(JLayeredPane lines,int x,int y,int size,int somethig) {
		ArrayList<Coordinates> list = Japan.getLinks(x, y);
		for(Coordinates coor : list) {
			JPanel line = new JPanel();
			line.setBackground(Color.BLACK);
			if(x>coor.getX()) {
				line.setLocation(x*size+somethig-size, y*size+somethig);
				line.setSize(size,2);
			}else if(x<coor.getX()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(size,2);
			}else if(y>coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig-size);
				line.setSize(2,size);
			}else if(y<coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(2,size);
			}
			lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
		}
	}
	protected void drawLineInJapan(JLayeredPane lines,Coordinates coor,int size,int somethig) {
		ArrayList<Coordinates> list = Japan.getLinks(coor);
		for(Coordinates coordinates : list) {
			JPanel line = new JPanel();
			line.setBackground(Color.BLACK);
			if(coor.getX()>coordinates.getX()) {
				line.setLocation(coor.getX()*size+somethig-size, coor.getY()*size+somethig);
				line.setSize(size,2);
			}else if(coor.getX()<coordinates.getX()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig);
				line.setSize(size,2);
			}else if(coor.getY()>coordinates.getY()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig-size);
				line.setSize(2,size);
			}else if(coor.getY()<coordinates.getY()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig);
				line.setSize(2,size);
			}
			lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
		}
	}
	//駅以外のマスを作成
	protected JPanel createMassInJapan(int j,int i,int distance) {
		JPanel mass = new JPanel();
		mass.setBounds(j*distance, i*distance, distance/3, distance/3);
		if(ContainsEvent.isBlue(j,i)) {
			mass.setBackground(Color.BLUE);
			mass.setName("B"+Japan.getIndexOfBlue(j, i));
		}else if(ContainsEvent.isRed(j,i)) {
			mass.setBackground(Color.RED);
			mass.setName("R"+Japan.getIndexOfRed(j, i));
		}else if(ContainsEvent.isYellow(j,i)) {
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+Japan.getIndexOfYellow(j, i));
		}else if(ContainsEvent.isShop(j,i)) {
			mass.setBackground(Color.GRAY);
			mass.setName("S"+Japan.getIndexOfShop(j, i));
		}else if(ContainsEvent.isWarp(j,i)) {
			mass.setBackground(Color.GREEN);
			mass.setName("W"+Japan.getIndexOfWarp(j, i));
		}
		return mass;
	}
	//駅以外のマスを作成
	protected JPanel createMassInJapan(Coordinates coor,int distance) {
		JPanel mass = new JPanel();
		int j=coor.getX();
		int i=coor.getY();
		mass.setBounds(j*distance, i*distance, distance/3, distance/3);
		if(ContainsEvent.isBlue(j,i)) {
			mass.setBackground(Color.BLUE);
			mass.setName("B"+Japan.getIndexOfBlue(j, i));
		}else if(ContainsEvent.isRed(j,i)) {
			mass.setBackground(Color.RED);
			mass.setName("R"+Japan.getIndexOfRed(j, i));
		}else if(ContainsEvent.isYellow(j,i)) {
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+Japan.getIndexOfYellow(j, i));
		}else if(ContainsEvent.isShop(j,i)) {
			mass.setBackground(Color.GRAY);
			mass.setName("S"+Japan.getIndexOfShop(j, i));
		}else if(ContainsEvent.isWarp(j,i)) {
			mass.setBackground(Color.GREEN);
			mass.setName("W"+Japan.getIndexOfWarp(j, i));
		}
		return mass;
	}

	protected void drawLineInGinga(JLayeredPane lines,int x,int y,int size,int somethig) {
		ArrayList<Coordinates> list = Ginga.getLinks(x, y);
		for(Coordinates coor : list) {
			JPanel line = new JPanel();
			line.setBackground(Color.BLACK);
			if(x>coor.getX()) {
				line.setLocation(x*size+somethig-size, y*size+somethig);
				line.setSize(size,2);
			}else if(x<coor.getX()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(size,2);
			}else if(y>coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig-size);
				line.setSize(2,size);
			}else if(y<coor.getY()) {
				line.setLocation(x*size+somethig, y*size+somethig);
				line.setSize(2,size);
			}
			lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
		}
	}
	protected void drawLineInGinga(JLayeredPane lines,Coordinates coor,int size,int somethig) {
		ArrayList<Coordinates> list = Ginga.getLinks(coor);
		for(Coordinates coordinates : list) {
			JPanel line = new JPanel();
			line.setBackground(Color.BLACK);
			if(coor.getX()>coordinates.getX()) {
				line.setLocation(coor.getX()*size+somethig-size, coor.getY()*size+somethig);
				line.setSize(size,2);
			}else if(coor.getX()<coordinates.getX()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig);
				line.setSize(size,2);
			}else if(coor.getY()>coordinates.getY()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig-size);
				line.setSize(2,size);
			}else if(coor.getY()<coordinates.getY()) {
				line.setLocation(coor.getX()*size+somethig, coor.getY()*size+somethig);
				line.setSize(2,size);
			}
			lines.add(line,JLayeredPane.DEFAULT_LAYER,-1);
		}
	}
	protected JPanel createMassInGinga(int j,int i,int distance) {
		JPanel mass = new JPanel();
		if(ContainsEvent.isBlueInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.BLUE);
			mass.setName("B"+Ginga.getIndexOfBlue(j, i));
		}else if(ContainsEvent.isYellowInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+Ginga.getIndexOfYellow(j, i));
		}else if(ContainsEvent.isStartInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.WHITE);
			mass.setName("S0");
		}else if(ContainsEvent.isGoalInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.GRAY);
			mass.setName("G0");
		}
		return mass;
	}
	protected JPanel createMassInGinga(Coordinates coor,int distance) {
		JPanel mass = new JPanel();
		int j=coor.getX();
		int i=coor.getY();
		if(ContainsEvent.isBlueInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.BLUE);
			mass.setName("B"+Ginga.getIndexOfBlue(j, i));
		}else if(ContainsEvent.isYellowInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.YELLOW);
			mass.setName("Y"+Ginga.getIndexOfYellow(j, i));
		}else if(ContainsEvent.isStartInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.WHITE);
			mass.setName("S0");
		}else if(ContainsEvent.isGoalInGinga(j,i)) {
			mass.setBounds(j*distance, i*distance, distance/3, distance/3);
			mass.setBackground(Color.GRAY);
			mass.setName("G0");
		}
		return mass;
	}

	public void actionPerformed(ActionEvent e) {

	}
}
