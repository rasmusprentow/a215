package cubedrawer;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import cube.CornerCubie;
import cube.Cube;
import cube.EdgeCubie;
import cube.Face;

public class DrawPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Cube cube;
	private int rectHW = 30; 
	
	public DrawPanel() {
		// TODO Auto-generated constructor stub
		
		
		cube = new Cube();
		this.setBackground(Color.white);
		//this.setPreferredSize(new Dimension(400,300));
		this.setPreferredSize(new Dimension(20 + rectHW*12 , 20 + rectHW*9));
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		int startX = 10;
		int startY = 10;
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth() + 1, this.getHeight());
		// TOP
		draw3x3(startX + 3*rectHW, startY,g,cube.getPrimary()[0]);
		
		// Left
		draw3x3(startX , startY + 3*rectHW,g, cube.getTertiary()[0]);
		
		// Front
		draw3x3(startX  + 3*rectHW , startY + 3*rectHW,g ,cube.getSecondary()[0]);
		
		// Right
		draw3x3(startX  + 3*rectHW*2 , startY + 3*rectHW,g, cube.getTertiary()[1]);
		
		// Back
		draw3x3(startX  + 3*rectHW*3 , startY + 3*rectHW,g, cube.getSecondary()[1]);
		
		// Down
		draw3x3(startX  + 3*rectHW , startY + 3*rectHW * 2,g, cube.getPrimary()[1]);
		
	}

	
	public void draw3x3(int x, int y, Graphics g, Face face){
		byte cornerCount = 0;
		byte edgeCount = 0;
		int[] newCornerOrder = {0 , 1, 3 ,2 };
		int[] newEdgeOrder = {0 , 3, 1 ,2 };
		int faceOrder = (int) Math.floor(face.getFacelet().ordinal()/2);
		for(int i = 0; i < 9; i++){
			if(i == 4){
				g.setColor(face.getFacelet().toColor());
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
			} else if(i%2 == 0){
				// finds the right facelet .
				CornerCubie ccubie = face.getCornerCubicle()[newCornerOrder[cornerCount]].getCubie();
				if(faceOrder == 0){
					if(ccubie.getPrimaryOrientation() == 0){
						g.setColor(ccubie.getFacelet(0).toColor());
					} else {
						if (ccubie.getSecondaryOrientation() == 0){
							g.setColor(ccubie.getFacelet(1).toColor());
						} else {
							g.setColor(ccubie.getFacelet(2).toColor());
						}
					}
					/*
					else if(ccubie.getPrimaryOrientation() == 1){
						if (ccubie.getSecondaryOrientation() == 0){
							g.setColor(ccubie.getFacelet(1).toColor());
						} else {
							g.setColor(ccubie.getFacelet(2).toColor());
						}
					}
					else if(ccubie.getPrimaryOrientation() == 2){
						if (ccubie.getSecondaryOrientation() == 0){
							g.setColor(ccubie.getFacelet(1).toColor());
						} else {
							g.setColor(ccubie.getFacelet(2).toColor());
						}
					}
					*/
					
				} else 	if(faceOrder == 1){
					if(ccubie.getPrimaryOrientation() == 1){
						g.setColor(ccubie.getFacelet(0).toColor());
						//System.out.print("NO");
					} else {
						if (ccubie.getSecondaryOrientation() == 1){
							g.setColor(ccubie.getFacelet(1).toColor());
						} else {
							g.setColor(ccubie.getFacelet(2).toColor());
						}
					}
					/*
					if(ccubie.getSecondaryOrientation() == 0){
						g.setColor(ccubie.getFacelet(1).toColor());
						System.out.println("dir 0");
					}
					else if(ccubie.getSecondaryOrientation() == 1){
						g.setColor(ccubie.getFacelet(0).toColor());
						System.out.println("dir 1");
					}
					else if(ccubie.getSecondaryOrientation() == 2){
						g.setColor(ccubie.getFacelet(2).toColor());
						System.out.println("dir 2");
					}
					*/

				} else 	if(faceOrder == 2){
					if(ccubie.getPrimaryOrientation() == 2){
						g.setColor(ccubie.getFacelet(0).toColor());
					} else {
						if (ccubie.getSecondaryOrientation() == 2){
							g.setColor(ccubie.getFacelet(1).toColor());
						} else {
							g.setColor(ccubie.getFacelet(2).toColor());
						}
					}
					/*
					if(ccubie.getTertiaryOrientation() == 0){
						g.setColor(ccubie.getFacelet(2).toColor());
					}
					else if(ccubie.getTertiaryOrientation() == 1){
						g.setColor(ccubie.getFacelet(1).toColor());
					}
					else if(ccubie.getTertiaryOrientation() == 2){
						g.setColor(ccubie.getFacelet(0).toColor());
					}
					*/
				}
				
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.setColor(Color.black);
			//	g.drawString(Integer.toString(ccubie.name), i%3*rectHW + x + 10, (int)Math.ceil(i/3)*rectHW + y + 15);
				cornerCount++;
				
			} else if (i%2 != 0){
				
				EdgeCubie ecubie = face.getEdgeCubicle()[newEdgeOrder[edgeCount]].getCubie();


				if(faceOrder == 0){
					g.setColor(ecubie.getFacelet(ecubie.getPrimaryOrientation()).toColor());



				} else 	if(faceOrder == 1){
					if(i == 7 || i == 1){
						if(ecubie.getPrimaryOrientation() == 0){
							g.setColor(ecubie.getFacelet(1).toColor());
						} else {
							g.setColor(ecubie.getFacelet(0).toColor());
						}
					} else {
						if(ecubie.getPrimaryOrientation() == 0){
							g.setColor(ecubie.getFacelet(0).toColor());
						} else {
							g.setColor(ecubie.getFacelet(1).toColor());
						}
					}



				} else 	if(faceOrder == 2){
					if(ecubie.getPrimaryOrientation() == 0){
						g.setColor(ecubie.getFacelet(1).toColor());
					} else {
						g.setColor(ecubie.getFacelet(0).toColor());
					}



				}

				//g.setColor(face.getEdgeCubicle()[edgeCount].getEdgeCubie().getFacelet(faceOrder).toColor());
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.setColor(Color.black);
				//g.drawString(Integer.toString(ecubie.name), i%3*rectHW + x + 10, (int)Math.ceil(i/3)*rectHW + y + 15);
				edgeCount++;
				//Remember
			}
			g.setColor(Color.black);
			g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);

		}
		g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);
	}
	
	public void reset(){
		cube = new Cube();
		repaint();
	}
	
	public Cube getCube(){
		return cube;
	}

	
	
}
