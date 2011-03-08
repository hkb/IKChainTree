package molecule;

//Nitrogen is normally shown in blue color

public class Nitrogen extends Atom {
	private static final String name = "nitrogen"; 
	private static final char symbol = 'N';
	private static final int atomNumber = 7;
//	private static final double atomicRadius = 0.56;
	private static final double covalentRadius = 0.75;
//	private static final double vanderWaalsRadius = 1.55;
	
	public Nitrogen() {
		atomicRadius = 0.56;
		vdWRadius = 1.55;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

