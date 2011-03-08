package moleculeMaps;

import javax.swing.*;
import java.awt.*;

/*
 * displays histogram of CN numbers of a given CN Number object cn
 */
public class ContactNumberFrame extends JFrame {
	public ContactNumberFrame(ContactNumber cn) {
		super("Contact Number Display");
		setBounds(0,0,35+3*cn.getSize(),350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); // inherit main frame
	    con.add(new CNCanvas(cn)); setVisible(true);  // create drawing canvas and add to frame and show
	}
}

class CNCanvas extends Canvas {
	ContactNumber cn;
	public CNCanvas(ContactNumber cn) {
		super();
		this.cn = cn;
	}
		
	public void paint(Graphics g) {
		int n = cn.getSize();
		int k;
		g.drawLine(5,300, 5, 10);
		g.drawLine(5, 300, 15+3*n, 300);
		for (int i = 35; i < 3*n+10; i = i+30) {
			g.drawLine(i, 305, i, 295);
			if ((i-5)/3 < 100) k=8; else k=12;
			g.drawString(String.valueOf((i-5)/3),i-k,320);
		}
		int x = 5;
		for (int i = 1; i < n; i++) {
			x = x+3;
			g.drawLine(x-3, 300-5*cn.get(i-1), x, 300-5*cn.get(i));
		}
	}
}

