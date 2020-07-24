package lifegame.game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class PaintCanvas extends JFrame{
	
	public int x,y,sizeX,sizeY;
	
	public PaintCanvas(){
		this.x=0;
		this.y=0;
		this.sizeX=0;
		this.sizeY=0;
	}
	/*
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x+sizeX, y+sizeY);
		System.out.println("draw");
	}
	*/
	public void set(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public void setSizeX(int sizeX) {
		this.sizeX=sizeX;
	}
	public void setSizeY(int sizeY) {
		this.sizeY=sizeY;
	}
	public void clearSize() {
		this.sizeX=0;
		this.sizeY=0;
	}
	
	@Override
	public void paint(Graphics g) {
		System.out.println("draw");
		super.paint(g);
		paintLine(g);
	}

	void paintLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x+sizeX, y+sizeY);
	}
}
