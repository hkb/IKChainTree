package gui3d;
import java.awt.*;
import java.awt.event.*;
import geom3d.*;
import molecule.*;
import misc.*;
import sorting.*;
import cells3d.*;

import javax.swing.*;


public class InputPanelPDBApplet extends JApplet implements ActionListener {
	private Object viewer;
	private InputPanelPDB inputPanelPDB;
	private JFrame f;
	private int n;
	private String fileName = null;
	private String type = null;

	private class InputPanelPDB extends JPanel {
		InputPanelPDBApplet inputPanelPDBApplet;
		JTextField pdbNameField;
		JTextField pdbAtomsField;
		
		/*
		 * Builds a panel for PDB file input
		 */
		public InputPanelPDB(InputPanelPDBApplet inputPanelPDBApplet) {
			this.inputPanelPDBApplet = inputPanelPDBApplet;
			
			setLayout(new GridLayout(1,4));
			
			JLabel pdbNameLabel = new JLabel("name of the PDB file: ", SwingConstants.RIGHT);
			JLabel pdbAtomsLabel = new JLabel("atoms to include (Ca, NCaC, NCaCO, NCaCOH): ", SwingConstants.RIGHT);
			pdbNameField = new JTextField("");		
			pdbAtomsField = new JTextField("");

			pdbNameField.setActionCommand("pdbNameField");
			pdbAtomsField.setActionCommand("pdbAtomsField");
			
			pdbNameField.addActionListener(inputPanelPDBApplet);
			pdbAtomsField.addActionListener(inputPanelPDBApplet);
			
			add(pdbNameLabel);
			add(pdbNameField);
			add(pdbAtomsLabel);
			add(pdbAtomsField);
		}
		
		public JTextField getPDBNameField() { return pdbNameField; }
		public JTextField getAtomsField() { return pdbAtomsField; }
	}

	
	
	public InputPanelPDBApplet(Object viewer) {
		this.viewer = viewer;
		
		init();
	}

	public void init() {
		Container contentPane = getContentPane();
		inputPanelPDB = new InputPanelPDB(this);
		contentPane.add(inputPanelPDB);
		 f = new JFrame("Input Panel - PDB");
		 f.add("Center", this);
		 f.setSize(800,60);
		 f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent actionEvent) {
		String cmd = actionEvent.getActionCommand();
		if ("pdbNameField".equals(cmd)) {
			JTextField pdbField = (JTextField)actionEvent.getSource();
			fileName = pdbField.getText();
			inputPanelPDB.getAtomsField().requestFocus();
		}
		else { 
			if ("pdbAtomsField".equals(cmd)) {
				JTextField atomsField = (JTextField)actionEvent.getSource();
				type = atomsField.getText();
			
				//Edited by Rasmus
				Protein protein = new Protein(fileName, Integer.parseInt(type), true);
				//Protein protein = new Protein(fileName, type, true);
				PointSet3d points = protein.getPointSet();
				points.translate(points.getCentroid());
			
/*			SortTool tool = new Point3dXYZSortTool();
			SelectionSorter sorter = new SelectionSorter();
			sorter.Sort(points,tool,true);
			points.toConsole();
*/
				Point3d point;
				n = points.getSize();

				if (viewer instanceof Viewer3d) {
					cells3d.Viewer3d viewer3d = (cells3d.Viewer3d)viewer;
					for (int i = 0; i < n; i++) {
						point = (Point3d)points.get(i);
						System.out.println(point.toString());
						Vertex v = new Vertex(point, i);
						viewer3d.getCells().getVertices().insert(v);
						viewer3d.addSphere(v, 0.02f, Colors.red);
					}
					viewer3d.getCells().setNrVertices(n);
					destroy();
					f.dispose();
				}
				else {
					if (viewer instanceof ComplexViewer) {
						ComplexViewer complexViewer = (ComplexViewer)viewer;
						for (int i = 0; i < n; i++) {
							point = (Point3d)points.get(i);
							complexViewer.getComplex().getPoints().insert(point);
							Simplex simplex = new Simplex(i);
							complexViewer.getComplex().insert(simplex);
							complexViewer.addSphere(simplex, 0.02f, Colors.red);
						}
						destroy();
						f.dispose();
					}
				}
			}
		}
	}
}

