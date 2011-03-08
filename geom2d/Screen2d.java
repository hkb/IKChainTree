package geom2d;
import java.awt.*;
import java.awt.event.*;

public class Screen2d extends Frame {
	private static final long serialVersionUID = 1L;
	public Screen2d() {
		super("Screen2d");
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } } );
		setSize(600,400);
		add("Center", new Cv());
		setVisible(true);
	}
	public static void main(String[] args) { new Screen2d(); }
}

class Cv extends Canvas {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		Dimension d = getSize();
		int maxX = d.width-1; int maxY = d.height-1;
		g.drawString("d.width = " + d.width, 10, 30);
		g.drawString("d.height = " + d.height, 10, 60);
		g.setColor(Color.red);
		g.drawRect(0, 0, maxX, maxY);
		Segment2d s = new Segment2d(10.0,20.0, 50.0,40.0);
		s.draw(g);
	}
}