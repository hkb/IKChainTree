package moleculeMaps;

import javax.swing.*;
import java.awt.*;

public class ContactMapFrame extends JFrame {
	
	private class CMCanvas extends Canvas {
		ContactMap cm;
		public CMCanvas(ContactMap cm) {
			super();
			this.cm= cm;
		}
			
		public void paint(Graphics g) {
			int n = cm.getSize();
			int x = 5;
			for (int i = 0; i < n; i++) {
				x = x+3;
				for (int j = 0; j < n; j++) 
					if (cm.get(i,j)) g.fillRect(x, 5+3*(n-j), 2, 2);
			}
		}
	}

	
	
	public ContactMapFrame(ContactMap cm) {
		super("Contact Map Display");
		setBounds(0,0,35+3*cm.getSize(),35+3*cm.getSize());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); // inherit main frame
	    con.add(new CMCanvas(cm)); setVisible(true);  // create drawing canvas and add to frame and show
	}
}

