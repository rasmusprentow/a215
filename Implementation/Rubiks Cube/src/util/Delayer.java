package util;

import java.util.Timer;

public class Delayer {

	public Delayer(int milliseconds) {
		Timer time = new Timer(milliseconds, arg1);
	}
}
