package experiments;

import geom3d.Point3d;
import geom3d.PointSet3d;
import geom3d.Volume3d;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;

public class VolumeCostExperiment {
	static final int WITH_TRANSFORM = 0;
	static final int NO_TRANSFORM = 1;
	static int MODE = WITH_TRANSFORM;

	public static void main(String[] args){
		int volume = BoundingVolumeManager.PSS;
		MODE=NO_TRANSFORM;
		printAvgC_V(volume);
		MODE=WITH_TRANSFORM;
		printAvgC_V(volume);
		//		printAvgC_P();
	}

	static void printAvgC_P(){
		List<Point3d[]> pairs = new ArrayList<Point3d[]>();
		for(int i=0;i<1000000;i++) pairs.add(new Point3d[]{
				new Point3d(Math.random()*100,Math.random()*100,Math.random()*100),
				new Point3d(Math.random()*100,Math.random()*100,Math.random()*100)
		});
		long start = getUserTime();//System.nanoTime();
		for(Point3d[] pair: pairs){
			double d = pair[0].getDistance(pair[1]);
		}
		long end = getUserTime();//System.nanoTime();
		System.out.println("Average time for primitive check is "+((end-start)*1.0/(1000000.0*pairs.size()))+"ms");
	}

	static void printAvgC_U(int volumeType){
		System.out.print("Finding C_U for "+volumeName(volumeType)+". Generating volume pairs .. ");
		List<Object[]> pairs = genVolumePairs(volumeType, false);
		System.out.println("done. There are "+pairs.size());
		System.out.print("Shuffling .. ");
		Collections.shuffle(pairs);
		System.out.println("done");
		System.out.print("Finding average C_U time "+(MODE==WITH_TRANSFORM?"(with transform)":"(no transform)")+" .. ");
		//		for(int c=0;c<100;c++){
		for(Object[] pair: pairs){
			Volume3d v = enclose(volumeType, pair);
		}

		long start = getUserTime();//System.nanoTime();
		for(int i=0;i<1000;i++){
			for(Object[] pair: pairs){
				Volume3d v = enclose(volumeType, pair);
			}
		}
		long end = getUserTime();//System.nanoTime();
		System.out.println("done");
		System.out.println("Average time for "+volumeName(volumeType)+" is "+((end-start)*1.0/(1000000.0*1000*pairs.size()))+"ms ("+(end-start)+")");

	}
	static Volume3d enclose(int volumeType, Object[] pair){
		Volume3d v, v1, v2=null;
		v1 = (Volume3d)pair[0];
		if(MODE==NO_TRANSFORM)
			v2 = (Volume3d)pair[1];
		if(MODE==WITH_TRANSFORM){
			v2 = ((BoundingVolumeManager)pair[5]).transform((Integer)pair[3], (Integer)pair[4], (Volume3d)pair[2]);
		}
		v = ((BoundingVolumeManager)pair[5]).createEnclosingVolume(v1, v2);
		return v;
	}

	static void printAvgC_V(int volumeType){
		System.out.print("Finding C_V for "+volumeName(volumeType)+". Generating volume pairs .. ");
		List<Object[]> pairs = genVolumePairs(volumeType, true);
		System.out.println("done. There are "+pairs.size());
		System.out.print("Shuffling .. ");
		Collections.shuffle(pairs);
		System.out.println("done");
		System.out.print("Finding average C_V time "+(MODE==WITH_TRANSFORM?"(with transform)":"(no transform)")+" .. ");
		for(Object[] pair: pairs){
			Volume3d v1 = (Volume3d)pair[0];
			Volume3d v2 = null;
			if(MODE==NO_TRANSFORM) v2 = (Volume3d)pair[1];
			if(MODE==WITH_TRANSFORM) v2 = ((BoundingVolumeManager)pair[5]).transform((Integer)pair[3], (Integer)pair[4], (Volume3d)pair[2]);
			boolean o = v1.overlaps(v2);
		}
		long start = getUserTime();//System.nanoTime();
		for(int i=0;i<100;i++){
			for(Object[] pair: pairs){
				Volume3d v1 = (Volume3d)pair[0];
				Volume3d v2 = null;
				if(MODE==NO_TRANSFORM) v2 = (Volume3d)pair[1];
				if(MODE==WITH_TRANSFORM) v2 = ((BoundingVolumeManager)pair[5]).transform((Integer)pair[3], (Integer)pair[4], (Volume3d)pair[2]);
				boolean o = v1.overlaps(v2);
			}
		}
		long end = getUserTime();//System.nanoTime();
		System.out.println("done");
		System.out.println("Average time for "+volumeName(volumeType)+" is "+((end-start)*1.0/(1000000.0*100*pairs.size()))+"ms ("+(end-start)+")");

	}


	static List<Object[]> genVolumePairs(int volumeType, boolean overlap_notUpdate){
		List<Object[]> ret = new ArrayList<Object[]>();

		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
		for(String pdb: pdbs){
			ret.addAll(getVolumesFromChainTree(pdb,overlap_notUpdate, volumeType, true, false, false));
		}

		return ret;
	}

	static List<Object[]> getVolumesFromChainTree(String pdbId, boolean overlap_NotUpdate,
			int volume, boolean groupPeptide, boolean groupSS, boolean rebalance){
		PrintStream out = System.out;
		System.setOut(new PrintStream(new OutputStream() {public void write(int b) {/*noop*/}}));
		
		List<Object[]> ret = new ArrayList<Object[]>();

		Protein protein = new Protein(pdbId, 2, true);

		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, groupPeptide);
		cTree.boundingMode = 3;
		cTree.volumeMode = volume;


		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		cTree.lockAlphaHelices(groupSS, rebalance);
		cTree.lockBetaStrands(groupSS,rebalance);

		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);

		for (int i = 1; i < cTree.nodes.length-1; i++) 
			cTree.changeRotationAngle(i, 
					cTree.getDihedralAngle(i)-
					cTree.proteinDihedralAngles[i]);

		for (int i = 0; i < cTree.nodes.length; i++) {
			if (!cTree.nodes[i].isLocked) {
				if(overlap_NotUpdate)	cTree.clashCheckedPairs = new ArrayList<Object[]>();
				else cTree.clashUpdatePairs = new ArrayList<Object[]>();
				cTree.changeRotationAngle(i, Math.PI/180.0);
				cTree.isClashing(i);
				if(overlap_NotUpdate)	ret.addAll(cTree.clashCheckedPairs);
				else					ret.addAll(cTree.clashUpdatePairs);
				//				if(ret.size()>10000) break;
				cTree.changeRotationAngle(i, -Math.PI/180.0);
			}
		}
		System.setOut(out);
		return ret;
	}


	static String volumeName(int v){
		switch(v){
		case BoundingVolumeManager.OBB: return "OBB";
		case BoundingVolumeManager.RSS: return "RSS";
		case BoundingVolumeManager.CAPSULE: return "LSS";
		case BoundingVolumeManager.PSS: return "PSS"; 
		}
		return "Unknown";
	}


	public static long getUserTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported()?bean.getCurrentThreadUserTime():0L;
	}
}
