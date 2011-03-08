package molecule;

//Carbon is normally shown in grey (occasionally black) color

public class Carbon extends Atom {

	private static final String name = "carbon"; 
	private static final char symbol = 'C';
	private static final int atomNumber = 6;
	private static final double covalentRadius = 0.77;
	
	
	public Carbon() {
		atomicRadius = 0.70;
		vdWRadius    = 1.70;
	}
}

