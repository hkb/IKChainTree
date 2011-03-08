
package gui3d;
import cells3d.Vertex;
import misc.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Sphere;
import javax.vecmath.Color3f;

import geom3d.Point3d;
import geom3d.Simplex;
import gui3d.Viewer3d;

import java.awt.Container;
import java.awt.event.*;
import java.awt.*;

public class SelectPointPanelApplet extends JApplet implements ActionListener, ListSelectionListener {
	private Object viewer;
	private SelectPointPanel selectPointPanel;

	private class SelectPointPanel extends JPanel {          
		SelectPointPanelApplet selectPointPanelApplet;
		private JList pointList;

		public SelectPointPanel(SelectPointPanelApplet selectPointPanelApplet) {
			this.selectPointPanelApplet = selectPointPanelApplet;
			
			setLayout(new GridLayout(2,1));
			JPanel pickPanel = new JPanel();
			add(pickPanel);
			String[] data = null;
			Object viewer = selectPointPanelApplet.getViewer();
			if (viewer instanceof Viewer3d) {
				cells3d.Viewer3d viewer3d = (cells3d.Viewer3d)viewer;
				int size = viewer3d.getCells().getNrVertices();
				data = new String[size];
				for (int i = 0; i < size; i++) {
					Point3d point = viewer3d.getCells().getPoint(i);
					data[i] = String.valueOf(i) + ": " + point.toString(3);
				}
			}
			else {
				if (viewer instanceof ComplexViewer) {
					ComplexViewer complexViewer = (ComplexViewer)viewer;
					int size = complexViewer.complex.getSimplicies()[0].getSize();
					data = new String[size];
					for (int i = 0; i < size; i++) {
						Point3d point = ((Point3d)complexViewer.complex.getPoints().get(i));;
						data[i] = String.valueOf(i) + ": " + point.toString(3);
					}
				}
			}
			pointList = new JList(data);
			pointList.addListSelectionListener(selectPointPanelApplet );
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.getViewport().add(pointList);
			pickPanel.add(scrollPane);
			
//			JPanel movePanel = new JPanel();
//			add(movePanel);
		}
		public JList getPointList() { return pointList;}
	}

	
	
	
	public SelectPointPanelApplet(Object viewer) {
		this.viewer = viewer;
		init();
	}
	
	public Object getViewer() { return viewer; }

	// initiates the creation of the point selection panel and makes it visible
	public void init() {
		Container contentPane = getContentPane();
		selectPointPanel = new SelectPointPanel(this);
		contentPane.add(selectPointPanel);
		JFrame f = new JFrame("Vertices");
		f.add("Center", this);
		f.setSize(200,150);
		f.pack();
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String cmd = actionEvent.getActionCommand();
	}
	
 	public void valueChanged(ListSelectionEvent event )
 	{
 		JList pointList = selectPointPanel.getPointList();
 		if (event.getSource() == pointList && !event.getValueIsAdjusting()) {
 			if (viewer instanceof Viewer3d) {
 				cells3d.Viewer3d viewer3d = (cells3d.Viewer3d)viewer;
 				Vertex oldSelectedVertex = viewer3d.getSelectedVertex();
 				if (oldSelectedVertex != null) {                      // restores the old point view (saved by the viewer, not by the point)
 					Appearance ap = oldSelectedVertex.getSphere().getAppearance();
 					ap.setColoringAttributes(new ColoringAttributes(viewer3d.getSelectedSphereColor(), ColoringAttributes.NICEST));
 					oldSelectedVertex.getSphere().setAppearance(ap);
 				}
 				int index = pointList.getSelectedIndex();              // picks up the selected vertex.
 				Vertex v = viewer3d.getCells().getVertex(index);
 				viewer3d.setSelectedVertex(v);                           // saves settings for future restoring. 
 				viewer3d.setSelectedSphereColor(Colors.red);
 				ColoringAttributes ca = new ColoringAttributes();      // new settings are enforced 
 				ca.setColor(Colors.green);
 				Appearance ap = v.getSphere().getAppearance();  	
 				ap.setColoringAttributes(ca);
 				v.getSphere().setAppearance(ap);
 			}
 		}
 		else {
 			if (viewer instanceof ComplexViewer) {
 				ComplexViewer complexViewer = (ComplexViewer)viewer;
 				if (complexViewer.selectedSimplex != null) {                      // restores the old point view (saved by the viewer, not by the point)
 					Appearance ap = complexViewer.selectedSimplex.getSphere().getAppearance();
 					ap.setColoringAttributes(new ColoringAttributes(Colors.red, ColoringAttributes.NICEST));
 					complexViewer.selectedSimplex.getSphere().setAppearance(ap);
 				}
 				int index = pointList.getSelectedIndex();              // picks up the selected vertex.
 				complexViewer.selectedSimplex = (Simplex)complexViewer.complex.getSimplicies()[0].get(index); 
 				Appearance ap = complexViewer.selectedSimplex.getSphere().getAppearance();  	
 				ap.setColoringAttributes(new ColoringAttributes(Colors.green, ColoringAttributes.NICEST));      // new settings are enforced 
 				complexViewer.selectedSimplex.getSphere().setAppearance(ap);

 			}
 		}
 	}
}
