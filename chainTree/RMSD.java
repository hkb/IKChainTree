package chainTree;

import jbcl.calc.structural.Crmsd;
import jbcl.data.types.Vector3D;
import geom3d.Point3d;
import geom3d.PointSet3d;

public abstract class RMSD {
	public static boolean MIRROR_RMSD = false;

	
	
	
	public static double getRMSD(ChainTree ct){
		PointSet3d nativeCoords = ct.points;

		PointSet3d struc = new PointSet3d();
		for(int i=0;i<ct.points.getSize();i++){
			struc.insert(ct.getPosition(i));
		}

		Vector3D[][] jbclCoords = getJBCLCoords(struc, nativeCoords);
		if(MIRROR_RMSD){
			double rmsd12 = Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[1]);
			double rmsd13 = Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[2]);
			return Math.min(rmsd12, rmsd13);
		}else{
			return Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[1]);
		}

	}
	
	
	public static double getRMSD(ChainTreeTest ct, int maxCoord){
		PointSet3d nativeCoords = new PointSet3d();
		PointSet3d strucCoords = new PointSet3d();

		for(int i=0;i<Math.min(ct.points.getSize(),maxCoord);i++){
			nativeCoords.insert(ct.points.get(i));
			strucCoords.insert(ct.getPosition(i));
		}

		Vector3D[][] jbclCoords = getJBCLCoords(strucCoords, nativeCoords);
		if(MIRROR_RMSD){
			double rmsd12 = Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[1]);
			double rmsd13 = Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[2]);
			return Math.min(rmsd12, rmsd13);
		}else{
			return Crmsd.optimalCrmsd(jbclCoords[0], jbclCoords[1]);
		}

	}

	static Vector3D[][] getJBCLCoords(PointSet3d struc1, PointSet3d struc2){
		int L=struc1.getSize();
		if(L!=struc2.getSize()){
			System.err.printf("Mismatch in structure lengths (%d vs %d)\n",
					L, struc2.getSize());
			return null;
		}

		if(MIRROR_RMSD){
			Vector3D[] s1JBCLCoords = new Vector3D[L];
			Vector3D[] s2JBCLCoords = new Vector3D[L];
			Vector3D[] s3JBCLCoords = new Vector3D[L];
			for(int r=0;r<L;r++){
				Point3d v;
				v=struc1.get(r);
				s1JBCLCoords[r] = new Vector3D(v.x, v.y, v.z); 
				v=struc2.get(r);
				s2JBCLCoords[r] = new Vector3D(v.x, v.y, v.z); 
				s3JBCLCoords[r] = new Vector3D(-v.x, v.y, v.z);
			}

			return new Vector3D[][]{s1JBCLCoords, s2JBCLCoords, s3JBCLCoords};
		}else{
			Vector3D[] s1JBCLCoords = new Vector3D[L];
			Vector3D[] s2JBCLCoords = new Vector3D[L];
			for(int r=0;r<L;r++){
				Point3d v;
				v=struc1.get(r);
				s1JBCLCoords[r] = new Vector3D(v.x, v.y, v.z); 
				v=struc2.get(r);
				s2JBCLCoords[r] = new Vector3D(v.x, v.y, v.z); 
			}

			return new Vector3D[][]{s1JBCLCoords, s2JBCLCoords};			
		}
	}
}
