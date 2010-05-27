package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Delayer {
	
	boolean run;
	int delay;
	
	public Delayer() {
		run = true;
		delay = 0;
	}
	
	public Delayer(int milliseconds) {
		run = true;
		delay = milliseconds;
		
		Timer time = new Timer(delay, new WhatToCallIt());
		time.start();
		
		int i = 0;
		int j;
		while(run) {
			i++;
			j = (i*3+1);
		}
		time.stop();
	}
	
	public void pause() {
		run = true;
		
		Timer time = new Timer(delay, new WhatToCallIt());
		time.start();
		time.addActionListener(new WhatToCallIt());
		
		int i = 0;
		int j;
		while(run) {
			i++;
			j = (i*3+1);
		}
		time.stop();
	}
	
	public void pause(int milliseconds) {
		run = true;
		delay = milliseconds;
		
		System.out.println(delay);
		Timer time = new Timer(delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Stop");
				run = false;
				notifyAll();
			}
			
		});
		time.start();
		try {
			wait();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		time.stop();
		
		
	}
	
	private class WhatToCallIt implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("Stop");
			run = false;
		}
		
	}
}
