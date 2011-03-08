package geom2d;
import dataStructures.Set;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Polygon extends PointSet2d {
	public Polygon() { super(); }
	
	public Polygon(int n) { super(n); }
	
	public Polygon(Polygon polygon) { super(polygon); }
	
	/*
	 * deletes object with specified index. Note !!! Order is  preserved !!!
	 */
	public Object deleteIndex(int k) {
		if ((k < 0) || (k >= n)) throw new IllegalArgumentException("object not in the set");
		Object object = elements[k];
		for (int i = k+1; i < n; i++) elements[i-1] = elements[i];
		elements[n-1] = null;
		n--;
		if (n <= elements.length/4) {
			int newCapacity = elements.length/2 + 1;
			Object[] newElements = new Object[newCapacity];
			System.arraycopy(elements, 0, newElements, 0, n);
			elements = newElements;
		}
		return object;
	}

	
	/*
	 * returns true if a point is inside the polygon 
	 */
	public boolean isInside(Point2d p) {
		boolean inside = false;
		int n = getSize();
		int i = 0;
		Point2d a, b = (Point2d)get(0), c = (Point2d)get(1);
		while (i < n) {
			a = b; b = c; c = (Point2d)get((i+2)%n);
			if ((a.y < p.y) && (p.y < b.y)) {
				if (Point2d.leftTurn(p, a, b)) inside = !inside;
			}
			else {
				if ((a.y > p.y) && (p.y > b.y)) {
					if (Point2d.rightTurn(p, a, b)) inside = !inside;
				}
				else {
					if (p.y == b.y) {
						while (b.y == c.y) { i++; a = b;  b = c; c = (Point2d)get((i+2)%n); } 
						if ((Point2d.leftTurn(p, a, b) && (c.y > p.y)) || (Point2d.rightTurn(p, a, b) && (c.y < p.y))) inside = !inside;
					}
				}
			}
			i++; a = b; b = c; c = (Point2d)get((i+2)%n);
		}
		return inside;
	}
	
	/*
	 * returns n-2 segments of a triangulation of the polygon
	 */
	public Segment2d[] triangulate() {
		Polygon polygon = new Polygon(this);
		int n = polygon.getSize(), i = 0, j = 0, k = 0;
		System.out.println(n);
		Segment2d diagonals[] = new Segment2d[n-2];
		Point2d a = (Point2d)polygon.get(0), b = (Point2d)polygon.get(1%n), c = (Point2d)polygon.get(2%n), d;
		boolean emptyTriangle;
		while (n != 3) {
			if (Point2d.leftTurn(a,b,c)) {
				emptyTriangle = true;
				j = (i+3)%n;
				while (emptyTriangle && (j != i)) {
					d = (Point2d)polygon.get(j);
					emptyTriangle = !d.inTriangle(a,b,c);
					j = (j+1)%n;
				}
				if (emptyTriangle) {
					diagonals[k++] = new Segment2d(a,c);
					System.out.println(k + ". diagonal added " + diagonals[k-1].toString());
					polygon.deleteIndex((i+1)%n); 
					n--;
					b = c; c = (Point2d)polygon.get((i+2)%n);
				}
				else {
					i = (i+1)%n;
					a = b; b = c; c = (Point2d)polygon.get((i+2)%n);
				}
			}
			else {
				i = (i+1)%n;
				a = b; b = c; c = (Point2d)polygon.get((i+2)%n);			
			}
		}
		return diagonals;
	}
	
	/*
	 * returns twice the signed area of a polygon (positive if counterclockwise, negative if clockwise)
	 */
	public double area2() {
		Point2d p0 = (Point2d)get(0);
		double sum = 0.0;
		for (int i = 1; i < getSize()-1; i++) 
			sum =+ Point2d.area2(p0, (Point2d)get(i), (Point2d)get(i+1));
		return sum;
	}
	
	private static void createAndShowGUI(Polygon polygon) {
		JFrame frame = new JFrame("Polygon Drawer");
		frame.setLocation(100,100);
		frame.setSize(600,400);
		frame.add(new DrawPanel(polygon));
		frame.setVisible(true);
		
		
	}
	
	public static void main(String[] args) {
		Polygon polygon = new Polygon();
		createAndShowGUI(polygon);
	}
}


class DrawPanel extends JPanel {
	Graphics gr;
	private static final long serialVersionUID = 1L;
	Polygon pol;
	int x0, y0, xA, yA;
	boolean constructMode = false;
	boolean queryMode = false;
	int mouseX, mouseY;
	Segment2d[] diagonals = null;
	
	DrawPanel(Polygon pol) {
		this.pol = pol; 
		addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { addPoint(evt); }} );
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) { placePoint(evt); }} );

	}

	private void addPoint(MouseEvent evt) {
		if (!constructMode && !queryMode) {
			pol.clear();
			x0 = evt.getX(); y0 = evt.getY();
			pol.insert(new Point2d(x0, y0));
			constructMode = true;
		}
		else {
			if (constructMode) {
				xA = evt.getX(); yA = evt.getY();
				if ((xA-x0)*(xA-x0) + (yA-y0)*(yA-y0) < 32) { 
					constructMode = false;
					queryMode = true; 
					System.out.println("Polygon drawing completed");
					pol.toConsole();
				}
				else pol.insert(new Point2d(xA, yA));
			}
			else {
				if (queryMode) diagonals = pol.triangulate();
			}
		}
		repaint();
	}
	
	private void placePoint(MouseEvent evt) {
		mouseX = evt.getX(); mouseY = evt.getY();
		repaint();	
	}
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (pol.isEmpty()) return;
		Point2d a = (Point2d)pol.get(0), b = null, c = a;
			// Show tiny rectangle around first vertex
		g.drawRect((int)a.x-2, (int)a.y-2, 4, 4);
		int n = pol.getSize();
		for (int i = 1; i < n; i++) {
			b = (Point2d)pol.get(i);
			g.drawLine((int)a.x, (int)a.y, (int)b.x , (int)b.y);
			a = b;
		}
		if (!constructMode && queryMode) g.drawLine((int)a.x, (int)a.y, (int)c.x , (int)c.y);
		else g.drawLine((int)a.x, (int)a.y, mouseX, mouseY);
		if (diagonals != null) {
			for (int i = 0; i < n-3; i++) g.drawLine((int)diagonals[i].a.x, (int)diagonals[i].a.y, (int)diagonals[i].b.x, (int)diagonals[i].b.y);
		}
	}
	
	
	/*
	 * answers queries if a point specified by mouse click is inside the polygon
	 */
	private void isPointInsideQuery(Polygon pol, MouseEvent evt) {
		xA = evt.getX(); yA = evt.getY();
		if ((xA-x0)*(xA-x0) + (yA-y0)*(yA-y0) < 32) queryMode = false;
		else {
			if (pol.isInside(new Point2d(xA,yA))) System.out.println("inside");
			else System.out.println("outside");
		}
	}
	
	
}