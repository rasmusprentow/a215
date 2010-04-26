package cubedrawer;




import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Port;
import javax.swing.JPanel;
import javax.swing.Timer;

import util.*;

import algorithms.*;

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
	private int dispHW = 10; //the distance moved after drawing each polygon
	private int startDelay = 500;
	private int test = 0;
	private Console console;
	private boolean toggleView;
	private boolean moving;
	private boolean specialMove;
	private boolean doNotSaveNextMove;
	private EnumSet<MoveButtons> moves = EnumSet.of(U, UP ,U2, D, DP, D2, F, FP,F2,  B, BP, B2, L, LP, L2, R, RP , R2);
	private Timer scrambleDanceTimer;
	private Timer playDanceTimer;
	private Timer beginnersTimer;
	private MP3 mp3;
	private Beginners beginners;
	private Kociemba kociemba;
	private KociembaV2 kociembaV2;
	Thread kociembaThread;
	//private ArrayList<MoveButtons> previousMoves;
	private LinkedList<MoveButtons> previousMoves;
	LinkedList<MoveButtons> solvingMoves; // For slow begginners.

	public DrawPanel(Console c) {
		this.console = c;
		cube = new Cube();
		this.setBackground(Color.white);
		//this.setPreferredSize(new Dimension(400,300));
		console.addTextln("Behold the Cube");
		//previousMoves = new ArrayList<MoveButtons>();
		previousMoves = new LinkedList<MoveButtons>();

		beginners = new Beginners(cube);
		kociemba = new Kociemba(cube , console);
		kociembaV2 = new KociembaV2(cube , console);

		this.setPreferredSize(new Dimension(20 + rectHW*12 , 20 + rectHW*9));
		scrambleDanceTimer = new Timer(startDelay, new ActionListener() { 
			public void actionPerformed(ActionEvent evt) { 	
				scramble(1); 
				if(scrambleDanceTimer.getDelay() > 100){ scrambleDanceTimer.setDelay(scrambleDanceTimer.getDelay() - 23); }
				repaint(); 
			}
		});
		playDanceTimer = new Timer(2*60*1000+28*1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mp3.play();
			}
		});
		beginnersTimer = new Timer(200, new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!solvingMoves.isEmpty()){
					
				console.addText(solvingMoves.getFirst() + "");
				Cube.permute(cube, solvingMoves.getFirst());
				solvingMoves.removeFirst();
				}
				else {
					beginnersTimer.stop();
				}
				repaint();
			}
		});


		String filename = "Khachaturian-Sabre_Dance.mp3";
		mp3 = new MP3(filename);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		int startX = 10;
		int startY = 10;
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING ,    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, this.getWidth() + 1, this.getHeight());

		if (toggleView) {

			draw3x3(startX + 3*rectHW, startY,g2,cube.getPrimary()[0]);
			draw3x3(startX  + 3*rectHW , startY + 3*rectHW * 2,g2, cube.getPrimary()[1]);
			draw3x3(startX  + 3*rectHW , startY + 3*rectHW,g2 ,cube.getSecondary()[0]);
			draw3x3(startX  + 3*rectHW*3 , startY + 3*rectHW,g2, cube.getSecondary()[1]);
			draw3x3(startX , startY + 3*rectHW,g2, cube.getTertiary()[0]);
			draw3x3(startX  + 3*rectHW*2 , startY + 3*rectHW,g2, cube.getTertiary()[1]);

		} else {

			draw3x3(startX  + 3*rectHW , startY + 3*rectHW * 2,g2, cube.getPrimary()[1]);
			draw3x3(startX  + 3*rectHW , startY + 3*rectHW, g2, cube.getSecondary()[0]);
			draw3x3(startX  + 2*rectHW * 4 , startY + 2*rectHW, g2, cube.getSecondary()[1]);
			draw3x3(startX , startY + 3*rectHW, g2, cube.getTertiary()[0]);
			draw3x3PrimaryPolygon(startX + 3*rectHW + 4*dispHW, startY + 7*dispHW, g2,cube.getPrimary()[0]);
			draw3x3SecondaryPolygon(startX  + 3*rectHW*2 , startY + 3*rectHW,g2, cube.getTertiary()[1]);

		}
	}


	public void draw3x3(int x, int y, Graphics2D g, Face face){
		byte cornerCount = 0;
		byte edgeCount = 0;
		int[] newCornerOrder = {0 , 1, 3 ,2 };
		int[] newEdgeOrder = {0 , 3, 1 ,2 };
		int faceOrder = (int) Math.floor(face.getFacelet().ordinal()/2);
		for(int i = 0; i < 9; i++){
			g.setColor(Color.black);
			g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);
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

				edgeCount++;
				g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
			}

			g.setColor(Color.black);
			g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);

		}
		g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);
	}

	public void draw3x3PrimaryPolygon(int x, int y, Graphics2D g, Face face){

		int[] listX = {x, x + 2*dispHW, x + rectHW + 2*dispHW, x + rectHW};
		int[] listY = {y, y - dispHW, y - dispHW, y};
		int[][] polygons = new int[18][4];
		byte cornerCount = 0;
		byte edgeCount = 0;
		int[] newCornerOrder = {0 , 1, 3 ,2 };
		int[] newEdgeOrder = {0 , 3, 1 ,2 };
		int faceOrder = (int) Math.floor(face.getFacelet().ordinal()/2);

		for(int i = 0; i < 9; i++){

			for (int j = 0; j < 4; j++) {
				polygons[i][j] = listX[j] + i%3*rectHW - ((int)Math.ceil(i/3)*2*dispHW);
				polygons[i+9][j] = listY[j] + (int)Math.ceil(i/3)*dispHW;
			}

			g.setColor(Color.black);
			g.drawPolygon(polygons[i], polygons[i+9], 4);

			if(i == 4){

				g.setColor(face.getFacelet().toColor());
				g.fillPolygon(polygons[i], polygons[i+9], 4);

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

				//g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.fillPolygon(polygons[i], polygons[i+9], 4);
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

				edgeCount++;
				//g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.fillPolygon(polygons[i], polygons[i+9], 4);
			}

			g.setColor(Color.black);
			//g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);
			g.drawPolygon(polygons[i], polygons[i+9], 4);

		}
		//g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);

		g.drawPolygon(new int[]{x + 2*dispHW, x - 4*dispHW, x - dispHW + 2*rectHW, x + 2*dispHW + 3*rectHW+1}, 
				new int[]{y - dispHW-1, y + 2*dispHW-1, y + 2*dispHW-1, y - dispHW-1}, 4);

	}

	public void draw3x3SecondaryPolygon(int x, int y, Graphics2D g, Face face){

		int[] listX = {x + 2*dispHW, x, x, x + 2*dispHW};
		int[] listY = {y - dispHW, y, y + rectHW, y + rectHW - dispHW};
		int[][] polygons = new int[18][4];
		byte cornerCount = 0;
		byte edgeCount = 0;
		int[] newCornerOrder = {0 , 1, 3 ,2 };
		int[] newEdgeOrder = {0 , 3, 1 ,2 };
		int faceOrder = (int) Math.floor(face.getFacelet().ordinal()/2);

		for(int i = 0; i < 9; i++){

			for (int j = 0; j < 4; j++) {
				polygons[i][j] = listX[j] + i%3*2*dispHW;
				polygons[i+9][j] = listY[j] + (int)Math.ceil(i/3)*3*dispHW - i%3*dispHW;
			}

			g.setColor(Color.black);
			g.drawPolygon(polygons[i], polygons[i+9], 4);

			if(i == 4){

				g.setColor(face.getFacelet().toColor());
				g.fillPolygon(polygons[i], polygons[i+9], 4);

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

				//g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.fillPolygon(polygons[i], polygons[i+9], 4);
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

				edgeCount++;
				//g.fillRect(i%3*rectHW + x + 1, (int)Math.ceil(i/3)*rectHW + y + 1, rectHW - 1, rectHW -1);
				g.fillPolygon(polygons[i], polygons[i+9], 4);
			}

			g.setColor(Color.black);
			//g.drawRect(i%3*rectHW + x, (int)Math.ceil(i/3)*rectHW + y, rectHW, rectHW);
			g.drawPolygon(polygons[i], polygons[i+9], 4);

		}
		//g.drawRect(x - 1, y - 1, 3*rectHW, 3*rectHW);
		//int[] listX = {x + 2*dispHW, x, x, x + 2*dispHW};
		//int[] listY = {y - dispHW, y, y + rectHW, y + rectHW - dispHW};

		g.drawPolygon(new int[]{x, x, x + 2*rectHW, x + 2*rectHW + 1}, 
				new int[]{y - 1, y + 3*rectHW - 1, y + 6*dispHW - 1, y - 3*dispHW - 1}, 4);

	}


	public void buttonHandler(MoveButtons t){
		if(!specialMove && moves.contains(t) && !doNotSaveNextMove){
			startMoving();
			console.addText(t + " ");

			previousMoves.add(t);
		} else if(doNotSaveNextMove) {

			doNotSaveNextMove = false;
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
			scramble(50);
			break;
		case YOU_KNOW:
			setSystemVolume(30000);
			if(scrambleDanceTimer.isRunning()){
				//System.out.println("aha");
				mp3.close();
				scrambleDanceTimer.stop();
				playDanceTimer.stop();
				scrambleDanceTimer.setDelay(startDelay);

			} else {
				mp3.play();
				playDanceTimer.start();
				scrambleDanceTimer.start();
			}
			break;
		case UNDO:
			undo();
			break;
		case KOCIEMBA:
			kociemba();
			break;
		case KOCIEMBAV2:
			kociembaV2();
			break;
		case BEGINNERS:
			beginners();
			break;
		case TEST:
			stopMoving();
			test();
			break;
		case TOGGLEVIEW:
			toggleView();
			break;
		case BEGINNERSLOW:
			this.reset();
			String moveSequence = "";
			LinkedList<MoveButtons> moves = new LinkedList<MoveButtons>();
			solvingMoves = new LinkedList<MoveButtons>();
			console.addText("Getting a new cube and scrambles it with theese moves:");
			for(int i = 0; i < 50; i++){
				int moveNum = (int)(Math.random()*18);
				moves.add((MoveButtons)this.moves.toArray()[moveNum]);
				moveSequence = moveSequence + " " + ((MoveButtons)this.moves.toArray()[moveNum]).toString();
				Cube.permute(cube, (MoveButtons)this.moves.toArray()[moveNum]);
			}
			repaint();
			//LinkedList<MoveButtons> moveSpec = ;
			solvingMoves.addAll(MoveTools.eliminateAll(beginners.solve()));
			console.addTextln(moveSequence);
			console.addTextln("Now i solve it with these " + solvingMoves.size() + " moves:");
			
			for(MoveButtons m: moves){
				Cube.permute(cube, m);
			}
			beginnersTimer.start();
			break;
		default:
			console.addTextln("Something is wrong");
		}	
	}

	private void kociembaV2() {
		/*
		stopMoving();
		console.addTextln("Solving with Kociemba's improved algorithm, please wait.");
		MoveButtons[] kociembasMoveSequence;
		kociembasMoveSequence = kociembaV2.solve(12);
		System.out.print("The shortest movesequence is: ");
		console.addTextln("The shortest movesequence is: ");
		for (int i = 0 ; i < kociembasMoveSequence.length; i++) {
			System.out.print(kociembasMoveSequence[i] + " ");
			console.addText(kociembasMoveSequence[i] + " ");
		}
		System.out.println();

		repaint();
		*/
		
		if(kociembaThread != null && kociembaThread.isAlive()) {
			try {
				kociembaThread.stop();
				kociembaThread = null;
			} catch (IllegalMonitorStateException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			kociembaThread = new Thread() {
	            public void run() {
	            	stopMoving();
	        		console.addTextln("Solving with Kociemba's improved algorithm, please wait.");
	        		MoveButtons[] kociembasMoveSequence;
	                kociembasMoveSequence = kociembaV2.solve(12);

	                System.out.print("The shortest movesequence is: ");
	                console.addTextln("The shortest movesequence is: ");
					for (int i = 0 ; i < kociembasMoveSequence.length; i++) {
						System.out.print(kociembasMoveSequence[i] + " ");
						console.addText(kociembasMoveSequence[i] + " ");
					}
					System.out.println();

					repaint();

					kociembaThread.stop();
					kociembaThread = null;

	                //twistSequence(kociembasMoveSequence);
	        		//stopMoving();
	            }
	        };

	        kociembaThread.start();
		}	
		 
		return;
	}

	private void toggleView() {
		if (toggleView) {
			toggleView = false;
		} else {
			toggleView = true;
		}
	}

	private void startMoving(){
		if(moving == false){
			previousMoves.clear();
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
		//console.addTextln("");
		//moving = false;
		//moving = false;
	}

	/**
	 * 
	 * @param n the number of moves to be performed on the cube
	 */
	private void scramble(int n){
		stopMoving();
		specialMove = true;
		// TODO Auto-generated method stub
		String moveSequence = "";
		console.addText("Scrambling:");
		for(int i = 0; i < n; i++){
			int moveNum = (int)(Math.random()*18);
			twistSequence((MoveButtons)moves.toArray()[moveNum]);
			moveSequence = moveSequence + " " + ((MoveButtons)moves.toArray()[moveNum]).toString();
		}
		console.addTextln(moveSequence);
		specialMove = false;
		/*
		if(scrambles > 500){
			timer.stop();
			mp3.close();
		}
		scrambles++;
		 */
	}

	public void twistSequence(MoveButtons... t){
		//stopMoving();
		for(MoveButtons key: t){
			buttonHandler(key);
			previousMoves.add(key);
		}
	}

	public void twistSequence(ArrayList<MoveButtons> e){
		//stopMoving();
		for(MoveButtons key: e){
			if (key != null) {
				buttonHandler(key);
				previousMoves.add(key);
			}
		}
	}

	public void reset(){
		stopMoving();
		previousMoves.clear();
		console.addTextln("Pick up screwdriver, disassemble cube, assemble cube correctly \n");
		cube = new Cube();
		beginners = new Beginners(cube);
		kociemba = new Kociemba(cube , console);
		kociembaV2 = new KociembaV2(cube , console);
		repaint();
	}

	private void beginners() {
		stopMoving();
		//moving = true;
		//specialMove = true;
		LinkedList<MoveButtons> original = beginners.solve();
		int size =  original.size();
		LinkedList<MoveButtons> moves = MoveTools.eliminateAll((LinkedList<MoveButtons>)original.clone());
		previousMoves.addAll(moves);
		console.addTextln("Solving with beginners Algorithm using " + moves.size() + " twists  - Original had"  + size );
		for(MoveButtons key: moves){
			console.addText(key + "");
		}
		console.addTextln("");




		//specialMove = false;		
	}

	public Cube getCube(){
		return cube;
	}

	public void setSystemVolume(final int vol)
	{
		final Port lineOut;
		try
		{
			if(AudioSystem.isLineSupported(Port.Info.LINE_OUT))
			{
				lineOut = (Port)AudioSystem.getLine(Port.Info.LINE_OUT);
				lineOut.open();
			}
			else if(AudioSystem.isLineSupported(Port.Info.HEADPHONE))
			{
				lineOut = (Port)AudioSystem.getLine(Port.Info.HEADPHONE);
				lineOut.open();
			}
			else if(AudioSystem.isLineSupported(Port.Info.SPEAKER))
			{
				lineOut = (Port)AudioSystem.getLine(Port.Info.SPEAKER);
				lineOut.open();
			}
			else
			{
				System.out.println("Unable to get Output Port");
				return;
			}

			final FloatControl controlIn = (FloatControl)lineOut.getControl(FloatControl.Type.VOLUME);
			final float volume = 100 * (controlIn.getValue() / controlIn.getMaximum());
			controlIn.setValue((float)vol / 100);
			System.out.println("SetSystemVolume : volume = " + volume);
		}
		catch(final Exception e)
		{
			System.out.println(e + " LINE_OUT");
		}
	}

	private void undo() {
		stopMoving();
		doNotSaveNextMove = true;
		try {
			buttonHandler(previousMoves.removeLast().invert());
		}
		catch (NoSuchElementException e) {
			// TODO: handle exception
		}
	}

	private void kociemba() {
		/*
		stopMoving();
		console.addTextln("Solving with Kociemba's algorithm, please wait.");
		MoveButtons[] kociembasMoveSequence;
		kociembasMoveSequence = kociemba.solve(12);

		System.out.print("The shortest movesequence is: ");
		console.addTextln("The shortest movesequence is: ");
		for (int i = 0 ; i < kociembasMoveSequence.length; i++) {
			System.out.print(kociembasMoveSequence[i] + " ");
			console.addText(kociembasMoveSequence[i] + " ");
		}
		System.out.println();

		repaint();

		*/
		if(kociembaThread != null && kociembaThread.isAlive()) {
			try {
				kociembaThread.stop();
				kociembaThread = null;
			} catch (IllegalMonitorStateException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			kociembaThread = new Thread() {
	            public void run() {
	            	stopMoving();
	        		console.addTextln("Solving with Kociemba's algorithm, please wait.");
	        		MoveButtons[] kociembasMoveSequence;
	                kociembasMoveSequence = kociemba.solve(12);

	                System.out.print("The shortest movesequence is: ");
	                console.addTextln("The shortest movesequence is: ");
					for (int i = 0 ; i < kociembasMoveSequence.length; i++) {
						System.out.print(kociembasMoveSequence[i] + " ");
						console.addText(kociembasMoveSequence[i] + " ");
					}
					System.out.println();

					repaint();

					kociembaThread.stop();
					kociembaThread = null;

	                //twistSequence(kociembasMoveSequence);
	        		//stopMoving();
	            }
	        };

	        kociembaThread.start();
		}	

		 
		return;
	}

	private void test() {
		MoveButtons[] seq = {U,DP,F,BP,L};

		if(test == 0) {
			Cube.permute(cube, seq);
			for(int i = 0 ; i < seq.length ; i++) {
				System.out.print(seq[i] + " ");
			}
			System.out.println();
			test = 1;
		} else {
			Cube.permute(cube, MoveButtons.inverseOf(seq));
			for(int i = 0 ; i < seq.length ; i++) {
				System.out.print(seq[i] + " ");
			}
			System.out.println();
			test = 0;
		}
	}
}
