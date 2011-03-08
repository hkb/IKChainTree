package gui2d;
import geom2d.*;
import geom2d.Polygon;

import java.awt.*;

public class PolygonDrawer extends Frame {
	Polygon polygon;
	int diam = 6;
	Color lineColor = Color.blue;
	Color pointColor = Color.red;
	
	public PolygonDrawer(Polygon polygon) {
		this.polygon = polygon;
	}
	public PolygonDrawer(Polygon polygon, Color color) {
		this(polygon);
		this.lineColor = color;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(lineColor);
		Point2d p;
		for (int i = 0; i < polygon.getSize(); i++) {
			p = (Point2d)polygon.get(i);
			int j = (i == polygon.getSize()-1)? 0 : i+1;
			Point2d q = (Point2d)polygon.get(j);
			g.drawLine((int)(p.getX()*(getWidth()-20))+10, (int)(p.getY()*(getHeight()-40))+30,
			(int)(q.getX()*(getWidth()-20))+10, (int)(q.getY()*(getHeight()-40))+30);

		}
		g.setColor(pointColor);
		for (int i = 0; i < polygon.getSize(); i++) {
			p = (Point2d)polygon.get(i);
			g.drawOval((int)(p.getX()*(getWidth()-20))-3+10, (int)(p.getY()*(getHeight()-40))-3+30, diam, diam);
			g.fillOval((int)(p.getX()*(getWidth()-20))-3+10, (int)(p.getY()*(getHeight()-40))-3+30, diam, diam);
		}
	}

	public static void main(String[] args) {
		Polygon polygon = new Polygon(10);
		PolygonDrawer polygonDrawer = new PolygonDrawer(polygon);
		polygonDrawer.setSize(520,520);
		polygonDrawer.setVisible(true);
	}
}
