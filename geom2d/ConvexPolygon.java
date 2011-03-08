package geom2d;

import misc.*;

public class ConvexPolygon extends StarPolygon {

	/*
	 * returns the segment between most distant pair of points. 
	 */
	public Segment2d getDiameter() {
		int n = getSize();
		int i = getExtremeIndex(0,1,true);  
		int iStart = i;
		int iNext = (i + 1 == n)? 0 : i+1;
		Vector2d vi = new Vector2d((Point2d)get(i), (Point2d)get(iNext));
		int j = getExtremeIndex(0,1,false); 
		int jStart = j;
		int jNext = (j + 1 == n)? 0 : j+1;
		Vector2d vj = new Vector2d((Point2d)get(jNext), (Point2d)get(j));
		Segment2d seg = new Segment2d((Point2d)get(i), (Point2d)get(j));
		Segment2d diameter = new Segment2d(seg);
		double diamLength = seg.getSquaredLength();
		double lng;
		do {
			System.out.println(i + ": " + ((Point2d)get(i)).toString() + ",  " + j +": " + ((Point2d)get(j)).toString() + ",  " + seg.getSquaredLength());
			System.out.println("Vectors vi and vj: " + vi.toString() + "  " + vj.toString());
			if  (Vector2d.rightTurn(vi, vj)) {
				j = jNext;
				jNext = (j + 1 == n)? 0 : j+1;
				vj = new Vector2d((Point2d)get(jNext), (Point2d)get(j));
				seg.setB((Point2d)get(j));
			}
			else {
				i = iNext;
				iNext = (i + 1 == n)? 0 : i+1;
				vi = new Vector2d((Point2d)get(i), (Point2d)get(iNext));
				seg.setA((Point2d)get(i));
			}
			lng = seg.getSquaredLength();
			if (lng > diamLength) { diameter = new Segment2d(seg); diamLength = lng; }
		} while  ((i != jStart) || (j != iStart)) ;
		return diameter;
	}
	
	
	public Rectangle2d getMinRectangle() {
		int n = getSize();
		int[][] i = new int[4][3];
		Point2d[] p = new Point2d[4];
		Vector2d[] u = new Vector2d[4];
		Vector2d[] v = new Vector2d[4];
		i[0][0] = i[0][1] = getExtremeIndex(0,1,true);  
		i[1][0] = i[1][1] = getExtremeIndex(1,0,true);  
		i[2][0] = i[2][1] = getExtremeIndex(0,1,false);  
		i[3][0] = i[3][1] = getExtremeIndex(1,0,false);  
		for (int k = 0; k < 4; k++) {
			p[k] = (Point2d)get(i[k][1]);
			System.out.println("Point #" + k + " in position " + i[k][1] + ": " + p[k].toString(4));
			i[k][2] = (i[k][1]+1 == n)? 0 : i[k][1]+1;
			u[k] = new Vector2d((Point2d)get(i[k][1]), (Point2d)get(i[k][2]));
			v[k] = u[k].createRotatedVector(-k*0.5*Math.PI);
			System.out.println("Vector #" + k + ": " + u[k].toString(4) + ". Slope = " + Functions.toString(v[k].getSlope(),4));
		}
		Rectangle2d minRect = new Rectangle2d(p[0].x, p[1].y, p[2].x, p[3].y);
		System.out.println("Initial axis-aligned rectangle: " + minRect.toString(4) + ". Area is " + minRect.getArea());
		double minArea = minRect.getSquaredArea();
		Rectangle2d curRect;
		double curArea;
		int indx;
		int count = 0;
		do {
			if ((i[0][1] != i[1][0]) && Vector2d.rightTurn(v[1], v[0]))
				if (Vector2d.rightTurn(v[2], v[0])) 
					if (Vector2d.rightTurn(v[3], v[0])) indx = 0; 
					else indx = (i[3][1] != i[0][0])? 3 : 0;
				else indx = ((i[2][1] != i[3][0]) && Vector2d.rightTurn(v[3], v[2]))? 2 : 3;
			else
				if ((i[1][1] != i[2][0]) && Vector2d.rightTurn(v[2], v[1])) 
					if (Vector2d.rightTurn(v[3], v[1])) indx = 1;
					else indx = (i[3][1] != i[0][0])? 3 : 1;
				else indx = ((i[2][1] != i[3][0]) && Vector2d.rightTurn(v[3], v[2]))? 2 : 3;
			System.out.println("Index: " + indx);
			curRect = new Rectangle2d(u[indx], p, indx);
			curArea = curRect.getSquaredArea();
			System.out.println("Current rectangle: " + curRect.toString(4) + ". Area is " + curRect.getArea());
			if (curArea < minArea) { minRect = curRect; minArea = curArea; }
			i[indx][1] = i[indx][2];
			i[indx][2] = (i[indx][2]+1 == n)? 0 : i[indx][2]+1;
			p[indx] = (Point2d)get(i[indx][1]);
			System.out.println("Point #" + indx + " replaced by " + p[indx].toString(4));
			u[indx] = new Vector2d((Point2d)get(i[indx][1]), (Point2d)get(i[indx][2]));
			v[indx] = u[indx].createRotatedVector(-indx*0.5*Math.PI);
			System.out.println("Vector #" + indx + " replaced by " + u[indx].toString(4)  + ". Slope = " + Functions.toString(v[indx].getSlope(),4));
		} while  ((count++ < 8) && ((i[0][1] != i[1][0]) || (i[1][1] != i[2][0]) || (i[2][1] != i[3][0]) || (i[3][1] != i[0][0]))) ;
		return minRect; 
	}

	
	@Override
	public boolean isInside(Point2d p) {
	
		/*
		 * returns true if point p is inside the convex polygon
		 */
		Point2d pi = (Point2d)get(0);
		Point2d pj;
		int j;
		int size = getSize();
		for (int i = 0; i < size; i++) {
			j = (i == size-1)? 0 : i+1;
			pj = (Point2d)get(j);
			if (Point2d.rightTurn(pi, pj, p)) return false; 
			pi = pj;
		}
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

