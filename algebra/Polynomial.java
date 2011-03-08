package algebra;
import geom3d.Vector3d;

public class Polynomial {

	private static final double  sqrt3 = Math.sqrt(3);

	
	public static double[] solveQuadraticEquation(double a, double b, double c){
		double D = b*b-4*a*c;
		if(D<0) return new double[]{};
		if(D==0) return new double[]{-b/(2*a)};
		else {
			double aa = 2*a;
			double sqD = Math.sqrt(D);
			return new double[]{(-b-sqD)/aa, (-b+sqD)/aa};
		}
	}
	
	public static Vector3d cubicEquation(double a, double b, double c, double d) {
		double aa = a*a, bb = b*b, aaa = aa*a, bbb = bb*b; 
		double f = (3*c/a - bb/aa)/3; 
		double g = (2*bbb/aaa - 9*b*c/aa + 27*d/a)/27;
		double fff = f*f*f;
		double h = g*g/4 + fff/27;
		double p = -b/(3*a);
		if (h > 0) {                     // complex roots
			System.out.println("Complex roots");
			return null;
/*			double r = -g/2 + fff/27;
			double s = Math.pow(r, 1.0/3.0);
			double t = -g/2 - Math.sqrt(h);
			double u = Math.pow(t, 1.0/3.0);
			double v = -(s+u)/2;
			double z = -(s-u)*sqrt3/2;
			return new Vector3d(s + u + p, v + p + iz, v + p - iz);
*/		}
		else {
			if ((h == 0) && (f == 0) && (g == 0)) {
				double da = Math.pow(d/a, 1.0/3.);
				return new Vector3d(da, da, da);
			}			
			else {
				double i = Math.sqrt(-fff/27);
				double j = -Math.pow(i, 1.0/3.0);
				double k = Math.acos(-g/(2*i))/3;
				double m = Math.cos(k);
				double n = sqrt3*Math.sin(k);

				return new Vector3d(p - 2*j*m, j*(m-n) + p, j*(m+n) + p);
			}
		}
	}
}
