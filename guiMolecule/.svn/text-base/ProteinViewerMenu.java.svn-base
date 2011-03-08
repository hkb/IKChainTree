package guiMolecule;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.*;

public class ProteinViewerMenu {
	JMenuBar menuBar = new JMenuBar();
	JMenu showHide;;
	JMenuItem showHideAlpha;
	JMenuItem showHideBeta;
	JMenuItem showHideCoil;
	JMenuItem showHideSideChains;
	JPopupMenu sideChainsPopupMenu;
	
	public ProteinViewerMenu(ProteinViewer pv) {
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
	
		JMenuItem load  = new JMenuItem("Load");
		JMenuItem clear = new JMenuItem("Clear");
		JMenuItem exit  = new JMenuItem("Exit");
		JMenuItem test  = new JMenuItem("Test");

	
		file.add(load);
		file.add(clear);
		file.add(exit);
		file.add(test);
		
		load.addActionListener(pv);
		clear.addActionListener(pv);
		exit.addActionListener(pv);
		test.addActionListener(pv);
	
		JMenu model = new JMenu("Model");
		menuBar.add(model);
		
		JMenuItem atoms = new JMenuItem("Atoms");
		JMenuItem vdW   = new JMenuItem("vdW");
		
		model.add(atoms);
		model.add(vdW);
		
		atoms.addActionListener(pv);
		vdW.addActionListener(pv);
		
		JMenu view = new JMenu("View");
		menuBar.add(view);
	
		JMenuItem CaTrace    = new JMenuItem("CaTrace");
		JMenuItem backbone   = new JMenuItem("Backbone");
		JMenuItem oxygen     = new JMenuItem("Oxygen");
		JMenuItem sideChains = new JMenuItem("Side Chains");
		
		view.add(CaTrace);
		view.add(backbone);
		view.add(oxygen);
		view.add(sideChains);
		
		CaTrace.addActionListener(pv);
		backbone.addActionListener(pv);
		oxygen.addActionListener(pv);
		sideChains.addActionListener(pv);
	
		
		JMenu highlight = new JMenu("Highlight");
		menuBar.add(highlight);
		
		JMenuItem alpha = new JMenuItem("a-highlight");
		JMenuItem beta  = new JMenuItem("b-highlight");
		
		highlight.add(alpha);
		highlight.add(beta);
		
		alpha.addActionListener(pv);
		beta.addActionListener(pv);
	
	
		showHide = new JMenu("Show/Hide");
		menuBar.add(showHide);

		showHideAlpha = new JMenuItem("a-Helices");
		showHideBeta  = new JMenuItem("b-Sheets");
		showHideCoil  = new JMenuItem("Coil");
		JMenu showHideSideChains = new JMenu("Side Chains");
		
		showHide.add(showHideAlpha);
		showHide.add(showHideBeta);
		showHide.add(showHideCoil);
		showHide.add(showHideSideChains);
		
		showHideAlpha.addActionListener(pv);
		showHideBeta.addActionListener(pv);
		showHideCoil.addActionListener(pv);
		
		showHideSideChains.add(new JMenuItem("Ala"));
		showHideSideChains.add(new JMenuItem("Arg"));
		showHideSideChains.add(new JMenuItem("Asn"));
		showHideSideChains.add(new JMenuItem("Asp"));
		showHideSideChains.add(new JMenuItem("Cys"));
		showHideSideChains.add(new JMenuItem("Glu"));
		showHideSideChains.add(new JMenuItem("Gln"));
		showHideSideChains.add(new JMenuItem("Gly"));
		showHideSideChains.add(new JMenuItem("His"));
		showHideSideChains.add(new JMenuItem("Ile"));
		showHideSideChains.add(new JMenuItem("Leu"));
		showHideSideChains.add(new JMenuItem("Lys"));
		showHideSideChains.add(new JMenuItem("Met"));
		showHideSideChains.add(new JMenuItem("Phe"));
		showHideSideChains.add(new JMenuItem("Pro"));
		showHideSideChains.add(new JMenuItem("Ser"));
		showHideSideChains.add(new JMenuItem("Thr"));
		showHideSideChains.add(new JMenuItem("Trp"));
		showHideSideChains.add(new JMenuItem("Tyr"));
		showHideSideChains.add(new JMenuItem("Val"));
		
	}
	
	public JMenuBar getMenuBar() { return menuBar; }
	public JMenuItem getAlphaMenuItem() { return showHideAlpha; }
	public JMenuItem getBetaMenuItem()  { return showHideBeta; }
	public JMenuItem getCoilMenuItem()  { return showHideCoil; }

	public void setAlphaMenuItem(JMenuItem showHideAlpha) { this.showHideAlpha = showHideAlpha; }
	public void setBetaMenuItem(JMenuItem showHideBeta)   { this.showHideBeta  = showHideBeta; }
	public void setCoilMenuItem(JMenuItem showHideCoil)   { this.showHideCoil  = showHideCoil; }
	public void setJMenuItemLabel(JMenuItem menuItem, String txt) { menuItem.setText(txt); }

	class SideChainsPopupListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			sideChainsPopupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		public void mouseExited(MouseEvent e) { 
			sideChainsPopupMenu.setVisible(false);
		}
		public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) sideChainsPopupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
