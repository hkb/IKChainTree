package molecule;

import java.io.File;

public class PDBFileFilter extends javax.swing.filechooser.FileFilter 
{
	 public boolean accept(File f) {
	    if (f.isDirectory()) return true;
	    else return getExtension(f).equals("pdb"); 
	  }
	    
	  public String getDescription() { return "PDB files"; }
	  /**
	    Method to get the extension of the file, in lowercase
	   */
	  private String getExtension(File f) {
	    String s = f.getName();
	    int i = s.lastIndexOf('.');
	    if (i > 0 &&  i < s.length() - 1) return s.substring(i+1).toLowerCase();
	    return "";
	  }
}
