package chainTree;

import j3dScene.J3DScene;
import geom3d.Box3d;
import geom3d.Capsule3d;
import geom3d.Point3d;
import geom3d.PointSet3d;
import geom3d.RSS3d;
import geom3d.Sphere3d;
import geom3d.Vector3d;
import geom3d.Volume3d;
import matrix.RotationMatrix4x4;

public class BoundingVolumeManager {
	public static final int OBB = 0;
	public static final int RSS = 1;
	public static final int CAPSULE = 2;
	public static final int PSS = 3;
	private final ChainTree chainTree;

	BoundingVolumeManager(ChainTree ct){ chainTree = ct; }

	/** Creates a volume enclosing a leaf-node */
	Volume3d createLeafVolume(CTNode nd, double extension){
		switch(chainTree.volumeMode){
		case OBB:
			PointSet3d points = chainTree.getPoints(nd);
			Point3d anchor = nd.getPQ().multiply(0.5).toPoint();
			Vector3d b1 = nd.getPQ().norm(); 
			Vector3d b2 = nd.getPQ().cross(nd.getPQ().add(new Vector3d(1.01,1.02,1.03))).norm();
			Vector3d b3 = b1.cross(b2);
			Vector3d[] bases = { b1,b2,b3 };
			double[] extents = {nd.getPQ().length() + extension, extension, extension};
			Box3d ret = new Box3d(anchor, bases, extents);
			return ret;

		case CAPSULE: 	return new Capsule3d(new Point3d(0, 0, 0), new Point3d(nd.getPQ()), extension);
		case RSS: 
			anchor = nd.getPQ().multiply(0.5).toPoint();
			b1 = nd.getPQ().norm(); 
			b2 = nd.getPQ().cross(nd.getPQ().add(new Vector3d(1.01,1.02,1.03))).norm();
			b3 = b1.cross(b2);
			bases = new Vector3d[]{ b1.multiplyIn(nd.getPQ().length()/2),b2.multiplyIn(0.00001) };
			RSS3d rss = new RSS3d(anchor, bases, extension);
			return rss;
		case PSS:	return new Sphere3d(nd.getPQ().multiply(0.5).toPoint(), nd.getPQ().length()/2+extension); 
		}
		throw new Error("Unknown volume type chosen: "+chainTree.volumeMode);
	}
	
	public Volume3d transform(int i, int j, Volume3d shape) {
		RotationMatrix4x4 m = chainTree.getTransformMatrix4x4(i,j);
		return transform(m, shape);
	}

	public Volume3d transform(RotationMatrix4x4 m, Volume3d shape){
		switch(chainTree.volumeMode){
		case OBB: 
			Box3d box = (Box3d)shape;
			Vector3d[] newBases = {
					chainTree.getDirection(m, box.getBases()[0]),
					chainTree.getDirection(m, box.getBases()[1]),
					chainTree.getDirection(m, box.getBases()[2])
			};
			return new Box3d(
					chainTree.getPosition(m, box.getAnchor()),
					newBases,
					box.extents
			);
		case CAPSULE: 
			Capsule3d caps = (Capsule3d)shape;
			Capsule3d rotatedCaps =  new Capsule3d(
					chainTree.getPosition(m, caps.segment.getA()), 
					chainTree.getPosition(m, caps.segment.getB()), 
					caps.rad
			);
			return rotatedCaps;
		case RSS: 
			RSS3d rss = (RSS3d)shape;
			Point3d newCenter = chainTree.getPosition(m, rss.getCenter());
			newBases = new Vector3d[]{	chainTree.getDirection(m, rss.rectangle.bases[0]), 
										chainTree.getDirection(m, rss.rectangle.bases[1])};
			return new RSS3d(newCenter, newBases,rss.radius );
		case PSS:
			Sphere3d sphere = (Sphere3d)shape;
			newCenter = chainTree.getPosition(m,sphere.center);
			return new Sphere3d(newCenter,sphere.radius);
		}
		throw new Error("Unknown volume type chosen: "+chainTree.volumeMode);
	}
	public Volume3d createEnclosingVolume(Volume3d s1, Volume3d s2){
		switch(chainTree.volumeMode){
		case OBB: 		return Box3d.createBoundingBox_Covariance((Box3d)s1, (Box3d)s2);
		case CAPSULE:	return Capsule3d.createBoundingCapsule_MaxDist((Capsule3d)s1, (Capsule3d)s2);
		case RSS:		return RSS3d.createBoundingRSS_covariance((RSS3d)s1,(RSS3d)s2); 
		case PSS: 	return Sphere3d.createBoundingSphere((Sphere3d)s1, (Sphere3d)s2);
		}
		throw new Error("Unknown volume type chosen: "+chainTree.volumeMode);
	}

	/** Create a volume bounding all the points. Also extend the radius appropriately */
	Volume3d createEnclosingExtendedVolume(PointSet3d points, double extension){
		switch(chainTree.volumeMode){
		case OBB: 
			Box3d retBox = Box3d.createBoundingBox_Covariance(points);
			retBox.extents[0]+=extension;
			retBox.extents[1]+=extension;
			retBox.extents[2]+=extension;
			return retBox;
		case CAPSULE:	
			Capsule3d ret = Capsule3d.createBoundingCapsule_CovarianceFit(points);
			ret.rad+=extension;
			return ret;
		case RSS: 
			RSS3d rss = RSS3d.createBoundingRSS_covariance(points);
			rss.radius+=extension;
			
			return rss;
		case PSS:
			Sphere3d sphere = Sphere3d.getMinSphere(points);
			sphere.radius+=extension;
			return sphere;
		}
		throw new Error("Unknown volume type chosen: "+chainTree.volumeMode);
	}

	/** Updates the position of vol so it is placed at orig. This method is used for fast repaints in J3DScene*/
	void updateVolumePosition(Volume3d vol, Volume3d orig){
		switch(chainTree.volumeMode){
		case OBB: 
			Box3d box = (Box3d)vol;
			Box3d origBox = (Box3d)orig;
			box.setAnchor(origBox.getAnchor());
			box.setXDir(origBox.getXDir());
			box.setYDir(origBox.getYDir());
			box.setZDir(origBox.getZDir());
			return;
		case CAPSULE:
			Capsule3d caps = (Capsule3d)vol;
			Capsule3d origCaps = (Capsule3d)orig;
			caps.segment.setA(origCaps.segment.getA());
			caps.segment.setB(origCaps.segment.getB());
			caps.rad = origCaps.rad;
			return;
		case RSS:
			RSS3d rss = (RSS3d)vol;
			RSS3d origRSS = (RSS3d)orig;
			rss.rectangle.center.set(origRSS.rectangle.center);
			rss.rectangle.bases[0].set(origRSS.rectangle.bases[0]);
			rss.rectangle.bases[1].set(origRSS.rectangle.bases[1]);
			rss.radius = origRSS.radius;
			return;
		case PSS:
			Sphere3d sphere = (Sphere3d)vol;
			Sphere3d origSphere = (Sphere3d)orig;
			sphere.center.set(origSphere.center);
		}
		throw new Error("Unknown volume type chosen: "+chainTree.volumeMode);
	}
}
