package cubedrawer;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JPanel;
import javax.swing.Timer;

import static cubedrawer.MoveButtons.*;
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
	private Console console;
	private boolean moving;
	private boolean specialMove;
	private EnumSet<MoveButtons> moves = EnumSet.of(U, UP ,U2, D, DP, D2, F, FP,F2,  B, BP, B2, L, LP, L2, R, RP , R2);
	private Timer timer;
	private int scrambles;
	private MP3 mp3;
	
	public DrawPanel(Console console) {
		this.console = console;
		cube = new Cube();
		this.setBackground(Color.white);
		//this.setPreferredSize(new Dimension(400,300));
		console.addTextln("Behold the Cube ");
		this.setPreferredSize(new Dimension(20 + rectHW*12 , 20 + rectHW*9));
		
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		int startX = 10;
		int startY = 10;
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth() + 1, this.getHeight());
		draw3x3(startX + 3*rectHW, startY,g,cube.getPrimary()[0]);
		draw3x3(startX , startY + 3*rectHW,g, cube.getTertiary()[0]);
		draw3x3(startX  + 3*rectHW , startY + 3*rectHW,g ,cube.getSecondary()[0]);
		draw3x3(startX  + 3*rectHW*2 , startY + 3*rectHW,g, cube.getTertiary()[1]);
		draw3x3(startX  + 3*rectHW*3 , startY + 3*rectHW,g, cube.getSecondary()[1]);
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
				CornerCubie ccubie = face.getCornerCubicle()[newCornerOrder[cornerCount]].getCubie();
				
				if(ccubie.getPrimaryOrientation() == faceOrder){
					g.setColor(ccubie.getFacelet(0).toColor());
				} else {
					if (ccubie.getSecondaryOrientation() == faceOrder){
						g.setColor(ccubie.getFacelet(1).toColor());
					} else {
						g.setColor(ccubie.getFacelet(2).toColor());
					}
				}
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.setColor(Color.black);
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
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.setColor(Color.black);
				edgeCount++;
			}
			g.setColor(Color.black);
			g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);

		}
		g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);
	}


	public void buttonHandler(MoveButtons t){
		if(!specialMove && moves.contains(t)){
			startMoving();
			console.addText(t + " ");
		}
		switch(t){
		case U:
			cube.getPrimary()[0].cwTwist();
			break;
		case UP:
			cube.getPrimary()[0].ccwTwist();
			break;
		case U2:
			cube.getPrimary()[0].cwTwist();
			cube.getPrimary()[0].cwTwist();
			break;
		case D:
			cube.getPrimary()[1].cwTwist();
			break;
		case DP:
			cube.getPrimary()[1].ccwTwist();
			break;
		case D2:
			cube.getPrimary()[1].cwTwist();
			cube.getPrimary()[1].cwTwist();
			break;
		case F:
			cube.getSecondary()[0].cwTwist();
			break;
		case FP:
			cube.getSecondary()[0].ccwTwist();
			break;
		case F2:
			cube.getSecondary()[0].cwTwist();
			cube.getSecondary()[0].cwTwist();
			break;
		case B:
			cube.getSecondary()[1].cwTwist();
			break;
		case BP:
			cube.getSecondary()[1].ccwTwist();
			break;
		case B2:
			cube.getSecondary()[1].cwTwist();
			cube.getSecondary()[1].cwTwist();
			break;
		case L:
			cube.getTertiary()[0].cwTwist();
			break;
		case LP:
			cube.getTertiary()[0].ccwTwist();
			break;
		case L2:
			cube.getTertiary()[0].cwTwist();
			cube.getTertiary()[0].cwTwist();
			break;
		case R:
			cube.getTertiary()[1].cwTwist();
			break;
		case RP:
			cube.getTertiary()[1].ccwTwist();
			break;
		case R2:
			cube.getTertiary()[1].cwTwist();
			cube.getTertiary()[1].cwTwist();
			break;
		case SCREWDRIVER:
			reset();
			break;
		case SCRAMBLE:
			scramble();
			break;
		case YOU_KNOW:
			youKnowMove();
			break;
		default:
			console.addTextln("Something is wrong");

		}	
	

	}

	private void startMoving(){
		if(moving == false){
			moving = true;
			console.addText("Applying Moves: ");
		} 
	}
	
	private void stopMoving(){
		if(moving){

		moving = false;
		console.addTextln("");
		}
	}

	public void youKnowMove(){
		
		stopMoving();
		moving = true;
		console.addText("Pons asinurum:");
		twistSequence(U2, D2,F2, B2,  L2, R2);
		console.addTextln("");
		//moving = false;
		moving = false;
	}

	private void scramble() {
		 String filename = "Khachaturian-Sabre_Dance.mp3";
	     mp3 = new MP3(filename);
	      mp3.play();
	      
	  	timer = new Timer(100, new ActionListener() { public void actionPerformed(ActionEvent evt) { 	doScramble(); repaint(); 	}  });
	      timer.start();
	}
	private void doScramble(){
		stopMoving();
		specialMove = true;
		// TODO Auto-generated method stub
		String moveSequence = "";
		console.addText("Scrambling:");
		for(int i = 0; i < 10; i++){
			int moveNum = (int)(Math.random()*18);
			twistSequence((MoveButtons)moves.toArray()[moveNum]);
			moveSequence = moveSequence + " " + ((MoveButtons)moves.toArray()[moveNum]).toString();
		}
		console.addTextln(moveSequence);
		specialMove = false;
		if(scrambles > 200){
			timer.stop();
			mp3.close();
		}
		scrambles++;
		
	}

	public void twistSequence(MoveButtons... t){
		//stopMoving();
		for(MoveButtons key: t){
			buttonHandler(key);
		}
	}


	public void reset(){
		stopMoving();
		console.addTextln("Pick up screwdriver, disassemble cube, assemble cube correctly \n");
		cube = new Cube();
		repaint();
	}

	public Cube getCube(){
		return cube;
	}



}
