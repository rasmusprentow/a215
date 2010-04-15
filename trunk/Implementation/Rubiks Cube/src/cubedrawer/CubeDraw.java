package cubedrawer;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cube.Cube;

public class CubeDraw extends JPanel {
	private Cube cube;
	private int rectHW = 30; 
	public CubeDraw() {
		// TODO Auto-generated constructor stub
		
		
		cube = new Cube();
	
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(400,300));
	}

	public void paintComponent(Graphics g){
		super.paintComponents(g);
		int startX = 10;
		int startY = 10;
		// TOP
		draw3x3(startX + 3*rectHW, startY,g, 	cube.getPrimary()[0].getFacelet().toColor());
		
		// Left
		draw3x3(startX , startY + 3*rectHW,g, 	cube.getTertiary()[0].getFacelet().toColor());
		
		// Front
		draw3x3(startX  + 3*rectHW , startY + 3*rectHW,g ,	cube.getSecondary()[0].getFacelet().toColor());
		
		// Right
		draw3x3(startX  + 3*rectHW*2 , startY + 3*rectHW,g, cube.getTertiary()[1].getFacelet().toColor());
		
		// Back
		draw3x3(startX  + 3*rectHW*3 , startY + 3*rectHW,g, cube.getSecondary()[1].getFacelet().toColor());
		
		// Down
		draw3x3(startX  + 3*rectHW , startY + 3*rectHW * 2,g, cube.getPrimary()[1].getFacelet().toColor());
		
	}

	public void draw3x3(int x, int y, Graphics g, Color centerColor){
		for(int i = 0; i < 9; i++){
		
			g.setColor(centerColor);
			g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
			g.setColor(Color.black);
			g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);
			
		}
		g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);
	}
	
	public Cube getCube(){
		return cube;
	}

	
	enum MoveButtons { 
		R, RP, F, FP, T, TP, D, DP, L, LP, B, BP;
		
		public String toString(){
			String old = super.toString();
			try {
				if(old.charAt(1) == 'P'){
					return old.charAt(0) + "'";
				}
			} catch (IndexOutOfBoundsException e){
				return old;
			}
			return old;
			
		}
	}
}
