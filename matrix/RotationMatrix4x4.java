package matrix;

import misc.Functions;
import geom3d.Vector3d;

public class RotationMatrix4x4 {
	protected double a11, a12, a13, a14, a21, a22, a23, a24, a31, a32, a33, a34;
	
	public RotationMatrix4x4() {
		a11 = 1; a12 = 0; a13 = 0; a14 = 0;
		a21 = 0; a22 = 1; a23 = 0; a24 = 0;
		a31 = 0; a32 = 0; a33 = 1; a34 = 0;
	}
	
	public RotationMatrix4x4(double a11, double a12, double a13, double a14,
			double a21, double a22, double a23, double a24,
			double a31, double a32, double a33, double a34) {
		this.a11 = a11; this.a12 = a12; this.a13 = a13; this.a14 = a14;
		this.a21 = a21; this.a22 = a22; this.a23 = a23; this.a24 = a24;
		this.a31 = a31; this.a32 = a32; this.a33 = a33; this.a34 = a34;
	}
		
	public RotationMatrix4x4(RotationMatrix4x4 m) {
		a11 = m.a11; a12 = m.a12; a13 = m.a13; a14 = m.a14;
		a21 = m.a21; a22 = m.a22; a23 = m.a23; a24 = m.a24;
		a31 = m.a31; a32 = m.a32; a33 = m.a33; a34 = m.a34;
	}
	
	public RotationMatrix4x4(RotationMatrix4x4 l, RotationMatrix4x4 r) {
		a11 = l.a11*r.a11 + l.a12*r.a21 + l.a13*r.a31;
		a12 = l.a11*r.a12 + l.a12*r.a22 + l.a13*r.a32;
		a13 = l.a11*r.a13 + l.a12*r.a23 + l.a13*r.a33;
		a14 = l.a11*r.a14 + l.a12*r.a24 + l.a13*r.a34 + l.a14;
		a21 = l.a21*r.a11 + l.a22*r.a21 + l.a23*r.a31;
		a22 = l.a21*r.a12 + l.a22*r.a22 + l.a23*r.a32;
		a23 = l.a21*r.a13 + l.a22*r.a23 + l.a23*r.a33;
		a24 = l.a21*r.a14 + l.a22*r.a24 + l.a23*r.a34 + l.a24;
		a31 = l.a31*r.a11 + l.a32*r.a21 + l.a33*r.a31;
		a32 = l.a31*r.a12 + l.a32*r.a22 + l.a33*r.a32;
		a33 = l.a31*r.a13 + l.a32*r.a23 + l.a33*r.a33;
		a34 = l.a31*r.a14 + l.a32*r.a24 + l.a33*r.a34 + l.a34;

	}
	
	public double get11() { return a11; }  public void set11(double b) { a11 = b; } 
	public double get12() { return a12; }  public void set12(double b) { a12 = b; } 
	public double get13() { return a13; }  public void set13(double b) { a13 = b; } 
	public double get14() { return a14; }  public void set14(double b) { a14 = b; } 
	public double get21() { return a21; }  public void set21(double b) { a21 = b; } 
	public double get22() { return a22; }  public void set22(double b) { a22 = b; } 
	public double get23() { return a23; }  public void set23(double b) { a23 = b; } 
	public double get24() { return a24; }  public void set24(double b) { a24 = b; }
	public double get31() { return a31; }  public void set31(double b) { a31 = b; } 
	public double get32() { return a32; }  public void set32(double b) { a32 = b; } 
	public double get33() { return a33; }  public void set33(double b) { a33 = b; } 
	public double get34() { return a34; }  public void set34(double b) { a34 = b; }

	/*
	 * Rotation matrices l and r are multipied and the result is saved in the rotation matrix m
	 */
	public static void mult(RotationMatrix4x4 l, RotationMatrix4x4 r, RotationMatrix4x4 m) {
		m.a11 = l.a11*r.a11 + l.a12*r.a21 + l.a13*r.a31;
		m.a12 = l.a11*r.a12 + l.a12*r.a22 + l.a13*r.a32;
		m.a13 = l.a11*r.a13 + l.a12*r.a23 + l.a13*r.a33;
		m.a14 = l.a11*r.a14 + l.a12*r.a24 + l.a13*r.a34 + l.a14;
		m.a21 = l.a21*r.a11 + l.a22*r.a21 + l.a23*r.a31;
		m.a22 = l.a21*r.a12 + l.a22*r.a22 + l.a23*r.a32;
		m.a23 = l.a21*r.a13 + l.a22*r.a23 + l.a23*r.a33;
		m.a24 = l.a21*r.a14 + l.a22*r.a24 + l.a23*r.a34 + l.a24;
		m.a31 = l.a31*r.a11 + l.a32*r.a21 + l.a33*r.a31;
		m.a32 = l.a31*r.a12 + l.a32*r.a22 + l.a33*r.a32;
		m.a33 = l.a31*r.a13 + l.a32*r.a23 + l.a33*r.a33;
		m.a34 = l.a31*r.a14 + l.a32*r.a24 + l.a33*r.a34 + l.a34;
	}
	
	/* 
	 * This rotation matrix is right multiplied by the rotation matrix m. 
	 * Original content of the matrix destroyed
	 */
	public void multR(RotationMatrix4x4 m) {
		double b11, b12, b13, b14, b21, b22, b23, b24, b31, b32, b33, b34;
		b11 = a11*m.a11 + a12*m.a21 + a13*m.a31;
		b12 = a11*m.a12 + a12*m.a22 + a13*m.a32;
		b13 = a11*m.a13 + a12*m.a23 + a13*m.a33;
		b14 = a11*m.a14 + a12*m.a24 + a13*m.a34 + a14;
		b21 = a21*m.a11 + a22*m.a21 + a23*m.a31;
		b22 = a21*m.a12 + a22*m.a22 + a23*m.a32;
		b23 = a21*m.a13 + a22*m.a23 + a23*m.a33;
		b24 = a21*m.a14 + a22*m.a24 + a23*m.a34 + a24;

		b31 = a31*m.a11 + a32*m.a21 + a33*m.a31;
		b32 = a31*m.a12 + a32*m.a22 + a33*m.a32;
		b33 = a31*m.a13 + a32*m.a23 + a33*m.a33;
		b34 = a31*m.a14 + a32*m.a24 + a33*m.a34 + a34;

		a11 = b11; a12 = b12; a13 = b13; a14 = b14;
		a21 = b21; a22 = b22; a23 = b23; a24 = b24;
		a31 = b31; a32 = b32; a33 = b33; a34 = b34;
	}

	/* 
	 * This rotation matrix is left multiplied by the rotation matrix m. 
	 * Original content of the matrix id destroyed
	 */
	public void multL(RotationMatrix4x4 m) {
		double b11, b12, b13, b14, b21, b22, b23, b24, b31, b32, b33, b34;
		b11 = m.a11*a11 + m.a12*a21 + m.a13*a31;
		b12 = m.a11*a12 + m.a12*a22 + m.a13*a32;
		b13 = m.a11*a13 + m.a12*a23 + m.a13*a33;
		b14 = m.a11*a14 + m.a12*a24 + m.a13*a34 + m.a14;
		b21 = m.a21*a11 + m.a22*a21 + m.a23*a31;
		b22 = m.a21*a12 + m.a22*a22 + m.a23*a32;
		b23 = m.a21*a13 + m.a22*a23 + m.a23*a33;
		b24 = m.a21*a14 + m.a22*a24 + m.a23*a34 + m.a24;

		b31 = m.a31*a11 + m.a32*a21 + m.a33*a31;
		b32 = m.a31*a12 + m.a32*a22 + m.a33*a32;
		b33 = m.a31*a13 + m.a32*a23 + m.a33*a33;
		b34 = m.a31*a14 + m.a32*a24 + m.a33*a34 + m.a34;

		a11 = b11; a12 = b12; a13 = b13; a14 = b14;
		a21 = b21; a22 = b22; a23 = b23; a24 = b24;
		a31 = b31; a32 = b32; a33 = b33; a34 = b34;
	}
	
	public void copy(RotationMatrix4x4 m) {
		a11 = m.a11; a12 = m.a12; a13 = m.a13; a14 = m.a14;
		a21 = m.a21; a22 = m.a22; a23 = m.a23; a24 = m.a24;
		a31 = m.a31; a32 = m.a32; a33 = m.a33; a34 = m.a34;
	}

	public void reset() {
		a11 = 1; a12 = 0; a13 = 0; a14 = 0;
		a21 = 0; a22 = 1; a23 = 0; a24 = 0;
		a31 = 0; a32 = 0; a33 = 1; a34 = 0;
	}
	
	public Vector3d mult(Vector3d v) {
		return new Vector3d(a11*v.getX() + a12*v.getY() + a13*v.getZ() + a14, 
							a21*v.getX() + a22*v.getY() + a23*v.getZ() + a24,
							a31*v.getX() + a32*v.getY() + a33*v.getZ() + a34);
	}

	public void toConsole(int dec) {
		System.out.println("[" + Functions.toString(a11,dec) + " " + Functions.toString(a12,dec) + " " + Functions.toString(a13,dec) + " " + Functions.toString(a14,dec));
		System.out.println(" " + Functions.toString(a21,dec) + " " + Functions.toString(a22,dec) + " " + Functions.toString(a23,dec) + " " + Functions.toString(a24,dec));
		System.out.println(" " + Functions.toString(a31,dec) + " " + Functions.toString(a32,dec) + " " + Functions.toString(a33,dec) + " " + Functions.toString(a34,dec));		
	}

}
