package data;

import java.text.NumberFormat;

public class DataHandler {
	
	public static String nanoToString(long nanos) {
		long remainder = nanos;
		int minutes = (int) (remainder / 60000000000L);
		remainder -= minutes * 60000000000L;
		float seconds = remainder / 1000000000f;
		if (nanos >= 60000000000L) {
			return minutes + "m " + roundFloat(seconds, 3) + "s";
		} else {
			return roundFloat(seconds, 3) + "s";
		}
	}
	
	public static String formatInt(int n) {
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(true);
		return format.format(n);
	}
	
	public static float roundFloat(float n, int precision) {
		float factor = (float) Math.pow(10, precision);
		n *= factor;
		n = (float) (int) n;
		return n / factor;
	}
	
}
