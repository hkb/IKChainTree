package misc;



public class Functions {

	public static double lg2 = Math.log(2);
	public static double lg(double x) { return Math.log(x)/lg2; }
	
	public static String toString(double x) { return toString(x, 2); }
	
	public static String toString(double x, int prec) {
		if (Math.abs(x) < 0.0000001) return "0.0";
		else {
//			System.out.println("x = " + x);
			String str = String.valueOf(x);
//			System.out.println("str = " + str);
			int lng = str.length();
			int pos = str.indexOf('.') + prec;
//			System.out.println("pos = " + pos);
			if (pos < lng) lng = pos;
//			System.out.println("substring = " + str.substring(0,lng));
			return str.substring(0,lng);
		}
	}
	
	public static double toDegrees(double alpha) { return 180*alpha/Math.PI; }
	public static double toRadians(double alpha) { return Math.PI*alpha/180; }
}

