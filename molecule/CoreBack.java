package molecule;

import geom3d.*;
import misc.*;

/*
 * Expected length of N-Ca bond is 1.45Å
 * Expected length of Ca-C bond is 1.52Å
 * Expected length of C-N (peptide bond) is 1.33.
 * Expected angle between Ca and N at C is 116.
 * Expected angle between N and O at C is 123.
 * Expected angle between O and Ca at C is 121.
 * Expected angle between Ca and C at N is 122.
 * Expected angle between C and HN at N is 120.
 * Expected angle bettwen HN and C at N is 118. 
 */

public class CoreBack {
	AminoAcid aminoAcid;   // amino acid to which this core belongs
	Nitrogen N   = null;
	Carbon   Ca  = null;
	Carbon   C   = null;
	Oxygen   O   = null;
	Hydrogen HN  = null;
	Hydrogen HCa = null;
	
	public CoreBack(AminoAcid aminoAcid, String type) {
		this.aminoAcid = aminoAcid;
		Ca = new Carbon();
		if (!type.equals("Ca")) {
			N = new Nitrogen();
			C = new Carbon();
			if (!type.equals("NCaC")) {
				O  = new Oxygen();
				if (!type.equals("NCaCO")) {
					HN  = new Hydrogen();
					HCa = new Hydrogen();
				}
			}
		}
	}
	
	public Nitrogen getN()   { return N; }
	public Carbon   getCa()  { return Ca; }
	public Carbon   getC()   { return C; }
	public Oxygen   getO()   { return O; }
	public Hydrogen getHN()  { return HN; }
	public Hydrogen getHCa() { return HCa; }
	
	public boolean hasO() { return O != null; }
	public boolean hasH() { return (HN != null) && (HCa != null); }
	
	public double getNCaDistance() { return getN().getPosition().getDistance(getCa().getPosition()); }
	public double getCaCDistance() { return getCa().getPosition().getDistance(getC().getPosition()); }
	public double getCODistance() { if (hasO()) return getC().getPosition().getDistance(getO().getPosition()); else return 1.23; }
	
	/*
	 * returns distance of the peptide bond between N  atom of this amino acid to the C atom of previous amino acid.
	 * returns -1.0 if the core of the previous amino acid is not defined.
	 */	
	public double getNCDistance() {
		AminoAcid prev = aminoAcid.getPrev();
		if ((prev != null) && prev.hasCore()) return prev.getCore().getC().getPosition().getDistance(getN().getPosition());
		else return 1.33;	
	}
	
	/*
	 * returns distance of the peptide bond between C atom of this amino acid to the N atom of next amino acid.
	 * returns 0.0 if the core of the next amino acid is not defined
	 */
	public double getCNDistance() {
		AminoAcid next = aminoAcid.getNext();
		if ((next != null) && next.hasCore()) return next.getCore().getN().getPosition().getDistance(getC().getPosition());
		else return 0.0;	
	}

	/*
	 * returns the angle between C-Ca and C-N vectors
	 */
	public double getCCaCNAngle() {
		Point3d p = getC().getPosition();
		return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getCa().getPosition()), new Vector3d(p, getN().getPosition())));
	}

	/*
	 * returns the angle between C-N and C-O vectors
	 */
	public double getCNCOAngle() {
		if (hasO()) {
			Point3d p = getC().getPosition();
			return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getN().getPosition()), new Vector3d(p, getO().getPosition())));		
		}
		else { return 0.0; }
	}
	
	public double getCOCaAngle() {
		if (hasO()) {
			Point3d p = getC().getPosition();
			return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getO().getPosition()), new Vector3d(p, getCa().getPosition())));		
		}
		else { return 121.0; }
	}

	public double getNCaCAngle() {
		Point3d p = getN().getPosition();
		return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getCa().getPosition()), new Vector3d(p, getC().getPosition())));
	}

	public double getCHNAngle() {
		if (hasH()) {
			Point3d p = getN().getPosition();
			return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getC().getPosition()), new Vector3d(p, getHN().getPosition())));		
		}
		else { return 120.0; }
	}
	
	public double getHNCaAngle() {
		if (hasH()) {
			Point3d p = getN().getPosition();
			return Functions.toRadians(Vector3d.getAngle(new Vector3d(p ,getHN().getPosition()), new Vector3d(p, getCa().getPosition())));		
		}
		else { return 118.0; }
	}

	public double getPhiAngle() {
		AminoAcid prev = aminoAcid.getPrev();
		if ((prev != null) && prev.hasCore()) 
			return Functions.toRadians(Point3d.getDihedralAngle(prev.getCore().getC().getPosition(), getN().getPosition() , getCa().getPosition(), getC().getPosition()));
		else return 360.0;
	}	
		
	public double getPsiAngle() {
		AminoAcid next = aminoAcid.getNext();
		if ((next != null) && next.hasCore()) 
			return Functions.toRadians(Point3d.getDihedralAngle(getN().getPosition(), getCa().getPosition(), getC().getPosition(), next.getCore().getN().getPosition()));
		else return 360.0;
	}	

	
	/*
	 * The OMEGA angle tends to be planar (0 or 180o) due to delocalization of the carbonyl pi electrons & the nitrogen lone pair. Trans is generally favored over cis:
	 * Only 116 (0.36%) of 32,539 angles in 154 X-ray structures were found to be cis (Stewart et al. 1990). However...... some specific bonds are often cis, eg. 
	 * Tyr-Pro (25%), Ser-Pro (11%), X-Pro (6.5%) This leaves phi and psi for flexible folding of the chain. However, steric conflicts limit even these angles as well.
	 */
	public double getOmegaAngle() {
		AminoAcid next = aminoAcid.getNext();
		if ((next != null) && next.hasCore()) 
			return Functions.toRadians(Point3d.getDihedralAngle(getCa().getPosition(), getC().getPosition(), next.getCore().getN().getPosition(), next.getCore().getCa().getPosition()));
		else return 360.0;
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

