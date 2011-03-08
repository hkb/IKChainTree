package gui3d;

import geom3d.*;
import misc.*;
import java.awt.*;
import java.awt.event.*;

import cells3d.*;

import javax.swing.*;

public class InputPanelPointApplet extends JApplet implements ActionListener {
	private ComplexViewer viewer;
	private InputPanelPoint inputPanelPoint;
	private JFrame f;
	private geom3d.Point3d pointToAdd = new geom3d.Point3d();

	private class InputPanelPoint extends JPanel  {
		InputPanelPointApplet inputPanelPointApplet;
		JTextField xTextField;
		JTextField yTextField;
		JTextField zTextField;
		JTextField pointField;
		JButton pointButton;
		
		/*
		 *  Builds a panel for 3d point input
		 */ 
		public InputPanelPoint(InputPanelPointApplet inputPanelPointApplet) {
			this.inputPanelPointApplet = inputPanelPointApplet;
			
			// Set the layout with 2 row and 6 columns
			setLayout(new GridLayout(2,1));
			JPanel coordinates = new JPanel();
			add(coordinates);
			coordinates.setLayout(new GridLayout(1,6));
			JPanel point = new JPanel();
			add(point);
			point.setLayout(new GridLayout(1,3));
			
			// Create text fields
			xTextField = new JTextField("");
			yTextField = new JTextField("");
			zTextField = new JTextField("");
			
			xTextField.setActionCommand("xTextField");
			yTextField.setActionCommand("yTextField");
			zTextField.setActionCommand("zTextField");
			 
			xTextField.addActionListener(inputPanelPointApplet);
			yTextField.addActionListener(inputPanelPointApplet);
			zTextField.addActionListener(inputPanelPointApplet);
			
			// Create right-adjusted label fields
			JLabel xLabel = new JLabel("x: ", SwingConstants.RIGHT);
			JLabel yLabel = new JLabel("y: ", SwingConstants.RIGHT);
			JLabel zLabel = new JLabel("z: ", SwingConstants.RIGHT);
			
			// Insert labels and text fields into the panel
			coordinates.add(xLabel);
			coordinates.add(xTextField);
			coordinates.add(yLabel);
			coordinates.add(yTextField);
			coordinates.add(zLabel);
			coordinates.add(zTextField);
			
			JLabel pLabel = new JLabel("Point: ",SwingConstants.CENTER);
			pointField = new JTextField("");
			pointField.setActionCommand("pointField");
			pointField.addActionListener(inputPanelPointApplet);
			pointField.setText(inputPanelPointApplet.getPointToAdd().toString());

			pointButton = new JButton("Enter");
			pointButton.addActionListener(inputPanelPointApplet);
			point.add(pLabel);
			point.add(pointField);
			point.add(pointButton);
		}
		
		public JTextField getXTextField() { return xTextField; }
		public JTextField getYTextField() { return yTextField; }
		public JTextField getZTextField() { return zTextField; }
		public JTextField getPointField() { return pointField; }
		public void setPoint(Point3d p) { pointField.setText(p.toString()); }

	}

	
	
	public InputPanelPointApplet(ComplexViewer viewer) {
		this.viewer = viewer;
		init();
	}
	
	public Point3d getPointToAdd() { return pointToAdd; }
	
	public void init() {
		Container contentPane = getContentPane();
		inputPanelPoint = new InputPanelPoint(this);
		contentPane.add(inputPanelPoint);
		 f = new JFrame("Input Panel - Point");
		 f.add("Center", this);
		 f.setSize(400,60);
		 f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent actionEvent) {
		String cmd = actionEvent.getActionCommand();
		if ("xTextField".equals(cmd)) {
			JTextField xTextField = (JTextField)actionEvent.getSource();
			String xString = xTextField.getText();
			if (xString.equals("")) pointToAdd.setX(0.0);
			else pointToAdd.setX(Double.parseDouble(xTextField.getText()));
			inputPanelPoint.setPoint(pointToAdd);
			inputPanelPoint.getYTextField().requestFocus();
		}
		else if ("yTextField".equals(cmd)) {
			JTextField yTextField = (JTextField)actionEvent.getSource();
			String yString = yTextField.getText();
			if (yString.equals("")) pointToAdd.setY(0.0);
			else pointToAdd.setY(Double.parseDouble(yTextField.getText()));
			inputPanelPoint.setPoint(pointToAdd);
			inputPanelPoint.getZTextField().requestFocus();
		}
		else if ("zTextField".equals(cmd)) {
			JTextField zTextField = (JTextField)actionEvent.getSource();
			String zString = zTextField.getText();
			if (zString.equals("")) pointToAdd.setZ(0.0);
			else pointToAdd.setZ(Double.parseDouble(zTextField.getText()));
			inputPanelPoint.setPoint(pointToAdd);
			inputPanelPoint.getPointField().requestFocus();
		}
		else if ("Enter".equals(cmd) || "pointField".equals(cmd)) {
//			int size = viewer.getCells().getVertices().getSize();
//			Vertex v = new Vertex(pointToAdd, size);
//			viewer.getCells().getVertices().insert(v);
//			viewer.addSphere(v.getPoint(), 0.02f, Colors.red);
//			viewer.getCells().setNrVertices(size+1);
//			destroy();
//			f.dispose();
		}
	}
}
