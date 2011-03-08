package gui3d;

import geom3d.Point3d;
import geom3d.PointSet3d;
import geom3d.Simplex;

import java.awt.*;
import java.awt.event.*;
import misc.*;

//import com.artchase.cells3d.*;

import javax.swing.*;

public class InputPanelRandomApplet extends JApplet implements ActionListener {
//	private Viewer3d viewer;
	private Object viewer;
	private InputPanelRandom inputPanelRandom;
	private JFrame f;
	private int n;
	
	public class InputPanelRandom extends JPanel {
		InputPanelRandomApplet inputPanelRandomApplet;
		JTextField randomField;
		
		public InputPanelRandom(InputPanelRandomApplet inputPanelRandomApplet) {
			this.inputPanelRandomApplet = inputPanelRandomApplet;
			
			setLayout(new GridLayout(1,2));
			JLabel randomLabel = new JLabel("number of random points: ", SwingConstants.RIGHT);
			randomField = new JTextField("");
			randomField.setActionCommand("randomField");
			randomField.addActionListener(inputPanelRandomApplet);
			add(randomLabel);
			add(randomField);
		}	
		public JTextField getRandomField() { return randomField; }
	}

	
	public InputPanelRandomApplet(Object viewer) {
		this.viewer = viewer;
		init();
	}

	public void init() {
		Container contentPane = getContentPane();
		inputPanelRandom = new InputPanelRandom(this);
		contentPane.add(inputPanelRandom);
		 f = new JFrame("Input Panel - Random");
		 f.add("Center", this);
		 f.setSize(400,60);
		 f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent actionEvent) {
		String cmd = actionEvent.getActionCommand();
		if ("randomField".equals(cmd)) {
			JTextField randomField = (JTextField)actionEvent.getSource();
			String randomString = randomField.getText();
			n = Integer.parseInt(randomString);
//			viewer.getCells().clear();
			PointSet3d points = new PointSet3d(n);
			Point3d point;
			if (viewer instanceof Viewer3d) {
				cells3d.Viewer3d viewer3d = (cells3d.Viewer3d)viewer;
				for (int i = 0; i < n; i++) {
					point = (Point3d)points.get(i);
					cells3d.Vertex v = new cells3d.Vertex(point, i);
					viewer3d.getCells().getVertices().insert(v);
					viewer3d.addSphere(v, 0.02f, Colors.red);
				}
				viewer3d.getCells().setNrVertices(n);
			}
			else {
				if (viewer instanceof ComplexViewer) {
					ComplexViewer complexViewer = (ComplexViewer)viewer;
					for (int i = 0; i < n; i++) {
						point = (Point3d)points.get(i);
						complexViewer.complex.getPoints().insert(point);
						Simplex simplex = new Simplex(i);
						complexViewer.complex.insert(simplex);
						complexViewer.addSphere(simplex, 0.02f, Colors.red);
					}
				}
			}
			destroy();
			f.dispose();
		}
	}
}

