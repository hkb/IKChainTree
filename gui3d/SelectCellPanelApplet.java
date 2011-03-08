package gui3d;

import cells3d.*;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.*;
//import javax.media.j3d.Appearance;
//import javax.media.j3d.ColoringAttributes;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cells3d.Vertex;
import cells3d.Viewer3d;
import misc.Colors;
import com.sun.j3d.utils.geometry.Sphere;

public class SelectCellPanelApplet extends JApplet implements ActionListener, ListSelectionListener {
	private Viewer3d viewer;
	private SelectCellPanel selectCellPanel;

	
	private class SelectCellPanel extends JPanel{
		SelectCellPanelApplet selectCellPanelApplet;
		private JList cellList;

		public SelectCellPanel(SelectCellPanelApplet selectCellPanelApplet) {
			this.selectCellPanelApplet = selectCellPanelApplet;
			
			setLayout(new GridLayout(2,1));
			JPanel pickPanel = new JPanel();
			add(pickPanel);
			
				int size = selectCellPanelApplet.getViewer().getCells().getNrCells();
				String[] data = new String[size];
				for (int i = 0; i < size; i++) {
					Cell cell = selectCellPanelApplet.getViewer().getCells().getCell(i);
					data[i] = cell.toString();
				}
				cellList = new JList(data);
				cellList.addListSelectionListener(selectCellPanelApplet );
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.getViewport().add(cellList);
				pickPanel.add(scrollPane);
			
			JPanel movePanel = new JPanel();
			add(movePanel);
		}
		
		public JList getCellList() { return cellList;}
	}

	
	
	public SelectCellPanelApplet(Viewer3d viewer) {
		this.viewer = viewer;
		init();
	}
		
	public Viewer3d getViewer() { return viewer; }

	// initiates the creation of the cell selection panel and makes it visible
	public void init() {
		Container contentPane = getContentPane();
		selectCellPanel = new SelectCellPanel(this);
		contentPane.add(selectCellPanel);
		JFrame f = new JFrame("Cells");
		f.add("Center", this);
		f.setSize(200,150);
		f.pack();
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String cmd = actionEvent.getActionCommand();
	}

	public void valueChanged( ListSelectionEvent event ) {
		JList cellList = selectCellPanel.getCellList();
		if (event.getSource() == cellList && !event.getValueIsAdjusting()) {
			Cell oldSelectedCell = viewer.getSelectedCell();
			if (oldSelectedCell != null) {                          // restores the old cell view to standard
				oldSelectedCell.changeAppearance(1,LineAttributes.PATTERN_SOLID, Colors.yellow);
				Cell oldSelectedAdjCell = viewer.getSelectedAdjCell();
				if (oldSelectedAdjCell != null) {                   // restores the old adjacent cell view to standard
					oldSelectedAdjCell.changeAppearance(1, LineAttributes.PATTERN_SOLID, Colors.yellow);
					viewer.setSelectedAdjCell(null);
					viewer.setSelectedAdjCellIndex(3);              // why is the index set to 3 here?
				}
			}
			String indexString = (String)cellList.getSelectedValue();   // picks up the selected cell
			indexString = indexString.substring(5, indexString.indexOf(':')).trim();
			Cell cell = viewer.getCells().findCell(Integer.valueOf(indexString));
			viewer.setSelectedCell(cell);
			viewer.setSelectedTetrahedronColor(Colors.green);
			cell.changeAppearance(5, LineAttributes.PATTERN_SOLID,Colors.green);
	 	}
	}
}
