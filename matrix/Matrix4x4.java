package matrix;

import javax.vecmath.Vector4d;

import geom3d.Vector3d;

import misc.Functions;

public class Matrix4x4 {
	protected double a11, a12, a13, a14, a21, a22, a23, a24, a31, a32, a33, a34, a41, a42, a43, a44;
	public Matrix4x4(){
		this(0,0,0,0,  0,0,0,0,  0,0,0,0,  0,0,0,0);
	}
			
	public Matrix4x4(double a11, double a12, double a13, double a14,
					 double a21, double a22, double a23, double a24,
					 double a31, double a32, double a33, double a34,
					 double a41, double a42, double a43, double a44) {
		this.a11 = a11; this.a12 = a12; this.a13 = a13; this.a14 = a14;
		this.a21 = a21; this.a22 = a22; this.a23 = a23; this.a24 = a24;
		this.a31 = a31; this.a32 = a32; this.a33 = a33; this.a34 = a34;
		this.a41 = a41; this.a42 = a42; this.a43 = a43; this.a44 = a44;
	}
	
	public Matrix4x4(Matrix4x4 m) {
		a11 = m.a11; a12 = m.a12; a13 = m.a13; a14 = m.a14;
		a21 = m.a21; a22 = m.a22; a23 = m.a23; a24 = m.a24;
		a31 = m.a31; a32 = m.a32; a33 = m.a33; a34 = m.a34;
		a41 = m.a41; a42 = m.a42; a43 = m.a43; a44 = m.a44;
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
	public double get41() { return a41; }  public void set41(double b) { a41 = b; } 
	public double get42() { return a42; }  public void set42(double b) { a42 = b; } 
	public double get43() { return a43; }  public void set43(double b) { a43 = b; } 
	public double get44() { return a44; }  public void set44(double b) { a44 = b; }
	
	public double get(int i, int j){
		if(i==0  && j==0 ) return a11;
		if(i==0  && j==1 ) return a12;
		if(i==0  && j==2 ) return a13;
		if(i==0  && j==3 ) return a14;
		if(i==1  && j==0 ) return a21;
		if(i==1  && j==1 ) return a22;
		if(i==1  && j==2 ) return a23;
		if(i==1  && j==3 ) return a24;
		if(i==2  && j==0 ) return a31;
		if(i==2  && j==1 ) return a32;
		if(i==2  && j==2 ) return a33;
		if(i==2  && j==3 ) return a34;
		if(i==3  && j==0 ) return a41;
		if(i==3  && j==1 ) return a42;
		if(i==3  && j==2 ) return a43;
		if(i==3  && j==3 ) return a44;
		throw new Error("Unknown indices");
	}
	
	public void multR(Matrix4x4 m) {
		double b11, b12, b13, b14, b21, b22, b23, b24, b31, b32, b33, b34, b41, b42, b43, b44;
		b11 = a11*m.a11 + a12*m.a21 + a13*m.a31 + a14*m.a41;
		b12 = a11*m.a12 + a12*m.a22 + a13*m.a32 + a14*m.a42;
		b13 = a11*m.a13 + a12*m.a23 + a13*m.a33 + a14*m.a43;
		b14 = a11*m.a14 + a12*m.a24 + a13*m.a34 + a14*m.a44;
		b21 = a21*m.a11 + a22*m.a21 + a23*m.a31 + a24*m.a41;
		b22 = a21*m.a12 + a22*m.a22 + a23*m.a32 + a24*m.a42;
		b23 = a21*m.a13 + a22*m.a23 + a23*m.a33 + a24*m.a43;
		b24 = a21*m.a14 + a22*m.a24 + a23*m.a34 + a24*m.a44;
		b31 = a31*m.a11 + a32*m.a21 + a33*m.a31 + a34*m.a41;
		b32 = a31*m.a12 + a32*m.a22 + a33*m.a32 + a34*m.a42;
		b33 = a31*m.a13 + a32*m.a23 + a33*m.a33 + a34*m.a43;
		b34 = a31*m.a14 + a32*m.a24 + a33*m.a34 + a34*m.a44;
		b41 = a41*m.a11 + a42*m.a21 + a43*m.a31 + a44*m.a41;
		b42 = a41*m.a12 + a42*m.a22 + a43*m.a32 + a44*m.a42;
		b43 = a41*m.a13 + a42*m.a23 + a43*m.a33 + a44*m.a43;
		b44 = a41*m.a14 + a42*m.a24 + a43*m.a34 + a44*m.a44;

		a11 = b11; a12 = b12; a13 = b13; a14 = b14;
		a21 = b21; a22 = b22; a23 = b23; a24 = b24;
		a31 = b31; a32 = b32; a33 = b33; a34 = b34;
		a41 = b41; a42 = b42; a43 = b43; a44 = b44;
	}

	
	public void multL(Matrix4x4 m) {
		double b11, b12, b13, b14, b21, b22, b23, b24, b31, b32, b33, b34, b41, b42, b43, b44;
		b11 = m.a11*a11 + m.a12*a21 + m.a13*a31 + m.a14*a41;
		b12 = m.a11*a12 + m.a12*a22 + m.a13*a32 + m.a14*a42;
		b13 = m.a11*a13 + m.a12*a23 + m.a13*a33 + m.a14*a43;
		b14 = m.a11*a14 + m.a12*a24 + m.a13*a34 + m.a14*a44;
		b21 = m.a21*a11 + m.a22*a21 + m.a23*a31 + m.a24*a41;
		b22 = m.a21*a12 + m.a22*a22 + m.a23*a32 + m.a24*a42;
		b23 = m.a21*a13 + m.a22*a23 + m.a23*a33 + m.a24*a43;
		b24 = m.a21*a14 + m.a22*a24 + m.a23*a34 + m.a24*a44;
		b31 = m.a31*a11 + m.a32*a21 + m.a33*a31 + m.a34*a41;
		b32 = m.a31*a12 + m.a32*a22 + m.a33*a32 + m.a34*a42;
		b33 = m.a31*a13 + m.a32*a23 + m.a33*a33 + m.a34*a43;
		b34 = m.a31*a14 + m.a32*a24 + m.a33*a34 + m.a34*a44;
		b41 = m.a41*a11 + m.a42*a21 + m.a43*a31 + m.a44*a41;
		b42 = m.a41*a12 + m.a42*a22 + m.a43*a32 + m.a44*a42;
		b43 = m.a41*a13 + m.a42*a23 + m.a43*a33 + m.a44*a43;
		b44 = m.a41*a14 + m.a42*a24 + m.a43*a34 + m.a44*a44;

		a11 = b11; a12 = b12; a13 = b13; a14 = b14;
		a21 = b21; a22 = b22; a23 = b23; a24 = b24;
		a31 = b31; a32 = b32; a33 = b33; a34 = b34;
		a41 = b41; a42 = b42; a43 = b43; a44 = b44;
	}
	
	public Vector4d mult(Vector4d v) {
		return new Vector4d(a11*v.x + a12*v.y + a13*v.z + a14*v.w, 
							a21*v.x + a22*v.y + a23*v.z + a24*v.w,
							a31*v.x + a32*v.y + a33*v.z + a34*v.w,
							a41*v.x + a42*v.y + a43*v.z + a44*v.w);
	}
	
	public static Matrix4x4 createRotationMatrix(double angle, Vector3d v){
		double ux = v.x;
		double uy = v.y;
		double uz = v.z;
		double cosA = (float)Math.cos(angle);
		double sinA = (float)Math.sin(angle);
		double[][] cs = new double[4][4];
		
		cs[0][0] = ux*ux + cosA*(1.0f-ux*ux);
		cs[1][0] = ux*uy*(1.0f-cosA) + uz*sinA;
		cs[2][0] = uz*ux*(1.0f-cosA) - uy*sinA;
		cs[3][0] = 0;
        
		cs[0][1] = ux*uy*(1.0f-cosA) - uz*sinA;
		cs[1][1] = uy*uy + cosA*(1.0f-uy*uy);
		cs[2][1] = uy*uz*(1.0f-cosA) + ux*sinA;
		cs[3][1] = 0;
		
		cs[0][2] = uz*ux*(1.0f-cosA) + uy*sinA;
		cs[1][2] = uy*uz*(1.0f-cosA) - ux*sinA;
		cs[2][2] = uz*uz + cosA*(1.0f-uz*uz);
		cs[3][2] = 0;
		
		cs[0][3] = 0;
		cs[1][3] = 0;
		cs[2][3] = 0;
		cs[3][3] = 1;
		int r,c;
		return new Matrix4x4(
				cs[r=0][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],  
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c]  );
	}
	
	public void toConsole(int dec) {
		System.out.println("[" + Functions.toString(a11,dec) + " " + Functions.toString(a12,dec) + " " + Functions.toString(a13,dec) + " " + Functions.toString(a14,dec));
		System.out.println(" " + Functions.toString(a21,dec) + " " + Functions.toString(a22,dec) + " " + Functions.toString(a23,dec) + " " + Functions.toString(a24,dec));
		System.out.println(" " + Functions.toString(a31,dec) + " " + Functions.toString(a32,dec) + " " + Functions.toString(a33,dec) + " " + Functions.toString(a34,dec));
		System.out.println(" " + Functions.toString(a41,dec) + " " + Functions.toString(a42,dec) + " " + Functions.toString(a43,dec) + " " + Functions.toString(a44,dec) + "]");	
	}

	public double[] getCoordArray(){
		double[] cArr = new double[4*4];
		int c=0;
		cArr[c++] = a11;
		cArr[c++] = a12;
		cArr[c++] = a13;
		cArr[c++] = a14;
		
		cArr[c++] = a21;
		cArr[c++] = a22;
		cArr[c++] = a23;
		cArr[c++] = a24;

		cArr[c++] = a31;
		cArr[c++] = a32;
		cArr[c++] = a33;
		cArr[c++] = a34;

		cArr[c++] = a41;
		cArr[c++] = a42;
		cArr[c++] = a43;
		cArr[c++] = a44;
		return cArr;
	}
	public double[] get3x3CoordArray(){
		double[] cArr = new double[3*3];
		int c=0;
		cArr[c++] = a11;
		cArr[c++] = a12;
		cArr[c++] = a13;
		
		cArr[c++] = a21;
		cArr[c++] = a22;
		cArr[c++] = a23;

		cArr[c++] = a31;
		cArr[c++] = a32;
		cArr[c++] = a33;
		return cArr;
	}

	public static Matrix4x4 createColumnMatrix(Vector3d xDir, Vector3d yDir, Vector3d zDir) {
		double[][] cs = new double[4][4];
		int r,c;
		cs[r=0][c=0] = 	xDir.x;
		cs[++r][c] = 	xDir.y;
		cs[++r][c] = 	xDir.z;
		cs[++r][c] = 	0;
		cs[r=0][++c] = 	yDir.x;
		cs[++r][c] = 	yDir.y;
		cs[++r][c] = 	yDir.z;
		cs[++r][c] = 	0;
		cs[r=0][++c] = 	zDir.x;
		cs[++r][c] = 	zDir.y;
		cs[++r][c] = 	zDir.z;
		cs[++r][c] = 	0;
		cs[r=0][++c] = 	0;
		cs[++r][c] = 	0;
		cs[++r][c] = 	0;
		cs[++r][c] = 	1;

		return new Matrix4x4(
				cs[r=0][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],  
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c]  );
	}
	public static Matrix4x4 createRowMatrix(Vector3d xDir, Vector3d yDir, Vector3d zDir) {
		double[][] cs = new double[4][4];
		int r,c;
		cs[r=0][c=0] = 	xDir.x;
		cs[r][++c] = 	xDir.y;
		cs[r][++c] = 	xDir.z;
		cs[r][++c] = 	0;
		cs[++r][c=0] = 	yDir.x;
		cs[r][++c] = 	yDir.y;
		cs[r][++c] = 	yDir.z;
		cs[r][++c] = 	0;
		cs[++r][c=0] = 	zDir.x;
		cs[r][++c] = 	zDir.y;
		cs[r][++c] = 	zDir.z;
		cs[r][++c] = 	0;
		cs[++r][c=0] = 	0;
		cs[r][++c] = 	0;
		cs[r][++c] = 	0;
		cs[r][++c] = 	1;

		return new Matrix4x4(
				cs[r=0][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],  
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c],   
				cs[++r][c=0],cs[r][++c],cs[r][++c],cs[r][++c]  );
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<4;i++){
			sb.append("|");
			for(int j=0;j<4;j++){
				sb.append(String.format("%7.2f ",get(i, j)));
			}
			sb.append("|\n");
		}
		return sb.toString();
	}
}
