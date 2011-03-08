package matrix;

import geom3d.Point3d;
import geom3d.PointSet3d;
import geom3d.Vector3d;
import Jama.EigenvalueDecomposition;
import algebra.Polynomial;
import misc.Functions;

public class Matrix3x3 {
	protected double a11, a12, a13, a21, a22, a23, a31, a32, a33;
	
	public Matrix3x3() {}
	public Matrix3x3(double a11, double a12, double a13,
			double a21, double a22, double a23,
			double a31, double a32, double a33) {
		this.a11 = a11; this.a12 = a12; this.a13 = a13;
		this.a21 = a21; this.a22 = a22; this.a23 = a23;
		this.a31 = a31; this.a32 = a32; this.a33 = a33;
	}
	
	public double get(int i, int j) {
		switch (i) {
		case 0:
			switch (j) {
			case 0: return a11;
			case 1: return a12;
			case 2: return a13;
			}
		case 1:
			switch (j) {
			case 0: return a21;
			case 1: return a22;
			case 2: return a23;
			}
		case 2:
			switch (j) {
			case 0: return a31;
			case 1: return a32;
			case 2: return a33;
			}
		}
		return -1;
	}
	
	public void set(int i, int j, double a) {
		switch (i) {
		case 0:
			switch (j) {
			case 0: a11 = a; break;
			case 1: a12 = a; break;
			case 2: a13 = a; break;
			}
		case 1:
			switch (j) {
			case 0: a21 = a; break;
			case 1: a22 = a; break;
			case 2: a23 = a; break;
			}
		case 2:
			switch (j) {
			case 0: a31 = a; break;
			case 1: a32 = a; break;
			case 2: a33 = a; break;
			}
		}
	}
	
	public void swapRows(int i, int j) {
		double bi0 = get(i,0), bi1 = get(i,1), bi2 = get(i,2);
		set(i,0,get(j,0)); set(i,1,get(j,1)); set(i,2,get(j,2));
		set(j,0,bi0); set(j,1,bi1); set(j,2,bi2);
	}
	
	public double getDet() { return a11*a22*a33 + a12*a23*a31 + a13*a21*a32 - a11*a23*a32 - a12*a21*a33 - a13*a22*a31; }
		
	public Vector3d getEigenValues() {    // not correct
		double c[] = new double[4];
		c[3] = -1.0;
		c[2] = a11+a22+a33;
		c[1] = a21*a12 + a31*a13 + a23*a32 - a11*a22 - a11*a33 - a22*a33;
		c[0] = a11*a22*a33 - a11*a23*a32 - a21*a12*a33 + a21*a13*a32 + a31*a12*a23 - a31*a13*a22;
		return Polynomial.cubicEquation(c[3], c[2], c[1], c[0]);
	}
	
	public Vector3d getEigenVector(double lambda) {
		if (Math.abs(a11-lambda) < a21) { swapRows(0,1); System.out.println("rows 0 and 1 swapped"); }
		if (Math.abs(a11-lambda) < a31) { swapRows(0,2); System.out.println("rows 0 and 2 swapped"); }
		double a, b, c;
		if (a11 - lambda != 0) {
			if ((a11-lambda)*a22 - a12*a21 != 0) {
				b = a22-lambda - a21*a12/(a11-lambda);
				if (b != 0.0) {
					b = (a21*a13/(a11-lambda) - a13)/b;		
					a = -a12*b/(a11-lambda);
					c = Math.sqrt(1/(a*a + b*b + 1));
					return new Vector3d(a*c, b*c, c);
				}
				else {
					return null;
				}
			}
			else {
				System.out.println("(a11-lambda)*a22 - a12*a21 = 0");
				if ((a11-lambda)*a33 - a13*a31 != 0) {
					System.out.println("/a11-lambda)*a33 - a13*a31 = 0");
					return null;
				}
				else {
					System.out.println("(a11-lambda)*a33 - a13*a31 = 0");
					return null;
				}
			}
		}
		else { 
			System.out.println("a11-lambda = 0");
			return null;
		}
	}
	
	/** Returns an array where the three first elements are the orthogonal basis 
	 * of the eigen-vector space, and the fourth vector contains the corresponding 
	 * eigenvalues.  
	 * @author Rasmus
	 */
	public Vector3d[] getEigenvectors(){
		/*double[][] coords = new double[3][3];
		for(int i=0;i<3;i++) for(int j=0;j<3;j++) coords[i][j] = get(i, j);
		Jama.Matrix A = new Jama.Matrix(coords);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(A);
		Jama.Matrix eV = ed.getV();
		
		Vector3d r = new Vector3d(eV.get(0, 0), eV.get(1, 0), eV.get(2, 0));
		Vector3d t = new Vector3d(eV.get(0, 1), eV.get(1, 1), eV.get(2, 1));
		Vector3d s = new Vector3d(eV.get(0, 2), eV.get(1, 2), eV.get(2, 2)); 
		double[] eigenValues = ed.getRealEigenvalues();
		return new Vector3d[]{r,s,t, new Vector3d(eigenValues[0], eigenValues[1], eigenValues[2])};*/
		double[][] array = new double[3][3];
		for(int i=0;i<3;i++) for(int j=0;j<3;j++) array[i][j] = get(i,j);
		Jama.Matrix A = new Jama.Matrix(array);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(A);
		Jama.Matrix eigenM = ed.getV();
		Vector3d[] ret = new Vector3d[eigenM.getColumnDimension()];
		double[] eigenVals = ed.getRealEigenvalues();
		for(int i=0;i<ret.length;i++){
			ret[i] = new Vector3d(eigenM.get(0, i), eigenM.get(1, i), eigenM.get(2, i)).scaleToLength(eigenVals[i]);
		}
		return ret;
	}
		
	public static Matrix3x3 createCovarianceMatrix(PointSet3d points){
		Matrix3x3 ret = new Matrix3x3();
		double[] means = points.getCentroid().toDoubleArray();
		
		for(int i=0;i<3;i++){
			for(int j=i;j<3;j++){
				ret.set(i,j,covariance(points, means, i,j));
				ret.set(j,i,ret.get(i, j));
			}
		}
		return ret;
	}
	
	private static double covariance(PointSet3d points, double[] means, int dim1, int dim2){
		double ret = 0;
		for (int i = 0; i < points.getSize(); i++) {
			Point3d v = points.get(i);
//		for(Point3d v: points){
			ret+=(v.get(dim1)-means[dim1])*(v.get(dim2)-means[dim2]);
		}
		
		return ret/points.getSize();
	}
	
	
	
	public Matrix3x3 transpose() { return new Matrix3x3(a11, a21, a31, a12, a22, a32, a13, a23, a33); }
		
	public Matrix3x3 scale(double d) { return new Matrix3x3(d*a11, d*a12, d*a13, d*a21, d*a22, d*a23, d*a31, d*a32, d*a33); }
	public Matrix3x3 add(Matrix3x3 B) { return new Matrix3x3(a11+B.a11, a12+B.a12, a13+B.a13, a21+B.a21, a22+B.a22, a23+B.a23, a31+B.a31, a32+B.a32, a33+B.a33); }
	public Matrix3x3 subtract(Matrix3x3 B) { return new Matrix3x3(a11-B.a11, a12-B.a12, a13-B.a13, a21-B.a21, a22-B.a22, a23-B.a23, a31-B.a31, a32-B.a32, a33-B.a33); }
	public Matrix3x3 multiply(Matrix3x3 B) { return new Matrix3x3(a11*B.a11 + a12*B.a21 + a13*B.a31, a11*B.a12 + a12*B.a22 + a13* B.a32, a11*B.a13 + a12*B.a23 + a13*B.a33,
			a21*B.a11 + a22*B.a21 + a23*B.a31, a21*B.a12 + a22*B.a22 + a13* B.a32, a21*B.a13 + a22*B.a23 + a23*B.a33,
			a31*B.a11 + a32*B.a21 + a33*B.a31, a31*B.a12 + a32*B.a22 + a33* B.a32, a31*B.a13 + a32*B.a23 + a33*B.a33); }
	
	public Matrix3x3 adjoint() { return new Matrix3x3(a22*a33-a23*a32, -(a21*a33-a23*a31),  a21*a32-a22*a31, 
													-(a12*a33-a13*a32),  a11*a33-a13*a31, -(a11*a32-a12*a31),
													  a12*a23-a13*a22, -(a11*a23-a13*a21),  a11*a22 -a12*a21); } 
	
	public Matrix3x3 inverse() {
		double det = getDet();
		if (det != 0.0) return transpose().adjoint().scale(1/det); 
		else return null;
	}
		
	/*
	 * solves the system of equations Ax = z
	 */
	public Point3d solve(Point3d z) {
		Matrix3x3 inv = inverse();
//		inv.toConsole(3);
		if (inv != null) return new Point3d(inv.a11*z.getX() + inv.a12*z.getY() + inv.a13*z.getZ(), 
				        					inv.a21*z.getX() + inv.a22*z.getY() + inv.a23*z.getZ(), 
				        					inv.a31*z.getX() + inv.a32*z.getY() + inv.a33*z.getZ());
		else return null;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<3;i++){
			sb.append("|");
			for(int j=0;j<3;j++){
				sb.append(String.format("%7.2f ",this.get(i, j)));
			}
			sb.append("|\n");
		}
		return sb.toString();
	}
	
	public void toConsole(int dec) {
		System.out.println("[" + Functions.toString(a11,dec) + " " + Functions.toString(a12,dec) + " " + Functions.toString(a13,dec));
		System.out.println(" " + Functions.toString(a21,dec) + " " + Functions.toString(a22,dec) + " " + Functions.toString(a23,dec));
		System.out.println(" " + Functions.toString(a31,dec) + " " + Functions.toString(a32,dec) + " " + Functions.toString(a33,dec) + "]");
		
	}
		public static void main(String[] args) {
			Matrix3x3 m = new Matrix3x3(1.5, 0.5, 0.75, 0.5, 0.5, 0.25, 0.75, 0.25, 0.5);
			Vector3d eigenValues = Polynomial.cubicEquation(-1, 2.5, -0.875, 0.0625);
			Vector3d r = m.getEigenVector(eigenValues.getX());
			Vector3d s = m.getEigenVector(eigenValues.getY());
			Vector3d t = m.getEigenVector(eigenValues.getZ());
			System.out.println(eigenValues.toString(4));
			System.out.println("R = " + r.toString(4));
			System.out.println("S = " + s.toString(4));
			System.out.println("T = " + t.toString(4));

		}
		public void applyToIn(Vector3d x) {
			double[] newCoords = {0,0,0};
			for(int r=0;r<3;r++)
				for(int c=0;c<3;c++)
					newCoords[r]+=get(r,c)*x.get(c);
			x.set(newCoords);
		}
		public static Matrix3x3 createColumnMatrix(Vector3d v1, Vector3d v2, Vector3d v3) {
			Matrix3x3 m = new Matrix3x3();
			for(int r=0;r<3;r++) m.set(r, 0, v1.get(r));
			for(int r=0;r<3;r++) m.set(r, 1, v2.get(r));
			for(int r=0;r<3;r++) m.set(r, 2, v3.get(r));
			return m;
		}

}
