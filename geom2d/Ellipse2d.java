package geom2d;

public class Ellipse2d {

	
	/*
	 * creates an ellipse through 5 given points
	 */
		/* toconic takes five points in homogeneous coordinates, and returns the
		 * coefficients of a general conic equation in a, b, c, ..., f:
		 * 
		 * a*x*x + b*x*y + c*y*y + d*x + e*y + f = 0.
		 * 
		 * The routine returns 1 on success; 0 otherwise.  (It can fail, for
		 * example, if there are duplicate points.
		 * 
		 * Typically, the points will be finite, in which case the third (w)
		 * coordinate for all the input vectors will be 1, although the code
		 * deals cleanly with points at infinity.
		 * 
		 * For example, to find the equation of the conic passing through (5, 0), 
		 * (-5, 0), (3, 2), (3, -2), and (-3, 2), set:
		 * 
		 * p0[0] =  5, p0[1] =  0, p0[2] = 1, 
		 * p1[0] = -5, p1[1] =  0, p1[2] = 1, 
		 * p2[0] =  3, p2[1] =  2, p2[2] = 1, 
		 * p3[0] =  3, p3[1] = -2, p3[2] = 1, 
		 * p4[0] = -3, p4[1] =  2, p4[2] = 1.
		 *
		 * But if you want the equation of the hyperbola that is tangent to the
		 * line 2x=y at infinity,  simply make one of the points be the point at
		 * infinity along that line, for example:
		 *
		 * p0[0] = 1, p0[1] = 2, p0[2] = 0.
		 */
	/*
	public Ellipse2d(Point2d p0, Point2d p1, Point2d p2, Point2d p3, Point2d p4) {
		
		{
		    FLOAT L0[3], L1[3], L2[3], L3[3];
		    FLOAT A, B, C, Q[3];
		    FLOAT a1, a2, b1, b2, c1, c2;
		    FLOAT x0, x4, y0, y4, w0, w4;
		    FLOAT aa, bb, cc, dd, ee, ff;
		    FLOAT y4w0, w4y0, w4w0, y4y0, x4w0, w4x0, x4x0, y4x0, x4y0;
		    FLOAT a1a2, a1b2, a1c2, b1a2, b1b2, b1c2, c1a2, c1b2, c1c2;
		    
		    doble L0 = Vector2d.crossProduct(p0, p1);
		    Vector2d L1 = Vector2d.crossProduct(p1, p2);
		    cross(p2, p3, L2)
		    cross(p3, p4, L3)
		    cross(L0, L3, Q)
		    A = Q[0]; B = Q[1]; C = Q[2];
		    a1 = L1[0]; b1 = L1[1]; c1 = L1[2];
		    a2 = L2[0]; b2 = L2[1]; c2 = L2[2];
		    x0 = p0[0]; y0 = p0[1]; w0 = p0[2];
		    x4 = p4[0]; y4 = p4[1]; w4 = p4[2];
		    
		    y4w0 = y4*w0;
		    w4y0 = w4*y0;
		    w4w0 = w4*w0;
		    y4y0 = y4*y0;
		    x4w0 = x4*w0;
		    w4x0 = w4*x0;
		    x4x0 = x4*x0;
		    y4x0 = y4*x0;
		    x4y0 = x4*y0;
		    a1a2 = a1*a2;
		    a1b2 = a1*b2;
		    a1c2 = a1*c2;
		    b1a2 = b1*a2;
		    b1b2 = b1*b2;
		    b1c2 = b1*c2;
		    c1a2 = c1*a2;
		    c1b2 = c1*b2;
		    c1c2 = c1*c2;

		    aa = -A*a1a2*y4w0
			 +A*a1a2*w4y0 
			 -B*b1a2*y4w0
			 -B*c1a2*w4w0
			 +B*a1b2*w4y0
			 +B*a1c2*w4w0
			 +C*b1a2*y4y0
			 +C*c1a2*w4y0
			 -C*a1b2*y4y0
			 -C*a1c2*y4w0;

		    cc =  A*c1b2*w4w0
		         +A*a1b2*x4w0
			 -A*b1c2*w4w0
			 -A*b1a2*w4x0
			 +B*b1b2*x4w0
			 -B*b1b2*w4x0
			 +C*b1c2*x4w0
			 +C*b1a2*x4x0
			 -C*c1b2*w4x0
			 -C*a1b2*x4x0;

		    ff =  A*c1a2*y4x0
			 +A*c1b2*y4y0
			 -A*a1c2*x4y0
			 -A*b1c2*y4y0
			 -B*c1a2*x4x0
			 -B*c1b2*x4y0
			 +B*a1c2*x4x0
			 +B*b1c2*y4x0
			 -C*c1c2*x4y0
			 +C*c1c2*y4x0;

		    bb =  A*c1a2*w4w0
			 +A*a1a2*x4w0
			 -A*a1b2*y4w0
			 -A*a1c2*w4w0
			 -A*a1a2*w4x0
			 +A*b1a2*w4y0
			 +B*b1a2*x4w0
			 -B*b1b2*y4w0
			 -B*c1b2*w4w0
			 -B*a1b2*w4x0
			 +B*b1b2*w4y0
			 +B*b1c2*w4w0
			 -C*b1c2*y4w0
			 -C*b1a2*x4y0
			 -C*b1a2*y4x0
			 -C*c1a2*w4x0
			 +C*c1b2*w4y0
			 +C*a1b2*x4y0
			 +C*a1b2*y4x0
			 +C*a1c2*x4w0;

		    dd = -A*c1a2*y4w0
			 +A*a1a2*y4x0
			 +A*a1b2*y4y0
			 +A*a1c2*w4y0
			 -A*a1a2*x4y0
			 -A*b1a2*y4y0
			 +B*b1a2*y4x0
			 +B*c1a2*w4x0
			 +B*c1a2*x4w0
			 +B*c1b2*w4y0
			 -B*a1b2*x4y0
			 -B*a1c2*w4x0
			 -B*a1c2*x4w0
			 -B*b1c2*y4w0
			 +C*b1c2*y4y0
			 +C*c1c2*w4y0
			 -C*c1a2*x4y0
			 -C*c1b2*y4y0
			 -C*c1c2*y4w0
			 +C*a1c2*y4x0;

		    ee = -A*c1a2*w4x0
			 -A*c1b2*y4w0
			 -A*c1b2*w4y0
			 -A*a1b2*x4y0
			 +A*a1c2*x4w0
			 +A*b1c2*y4w0
			 +A*b1c2*w4y0
			 +A*b1a2*y4x0
			 -B*b1a2*x4x0
			 -B*b1b2*x4y0
			 +B*c1b2*x4w0
			 +B*a1b2*x4x0
			 +B*b1b2*y4x0
			 -B*b1c2*w4x0
			 -C*b1c2*x4y0
			 +C*c1c2*x4w0
			 +C*c1a2*x4x0
			 +C*c1b2*y4x0
			 -C*c1c2*w4x0
			 -C*a1c2*x4x0;

		    if (aa != 0.0) {
			bb /= aa; cc /= aa; dd /= aa; ee /= aa; ff /= aa; aa = 1.0;
		    } else if (bb != 0.0) {
			cc /= bb; dd /= bb; ee /= bb; ff /= bb; bb = 1.0;
		    } else if (cc != 0.0) {
			dd /= cc; ee /= cc; ff /= cc; cc = 1.0;
		    } else if (dd != 0.0) {
			ee /= dd; ff /= dd; dd = 1.0;
		    } else if (ee != 0.0) {
			ff /= ee; ee = 1.0;
		    } else {
			return 0;
		    }
		    *a = aa;
		    *b = bb;
		    *c = cc;
		    *d = dd;
		    *e = ee;
		    *f = ff;
		    return 1;
		}

		FLOAT p0[3] = {0, 0, 1};
		FLOAT p1[3] = {1, 1, 1};
		FLOAT p2[3] = {-1, -1, 1};
		FLOAT p3[3] = {2, 2, 1};
		FLOAT p4[3] = {3, 3, 1};

		main()
		{
		    FLOAT a, b, c, d, e, f, s0, s1, s2, s3, s4;
		    FLOAT x, y, w;
		    int i, ret;

		    //ret = toconic(p0, p1, p2, p3, p4, &a, &b, &c, &d, &e, &f);
		    //if (ret == 1) {
			//printf("success\n");
			//printf("%g %g %g %g %g %g\n", a, b, c, d, e, f);
			//x = p0[0]; y = p0[1]; w = p0[2];
			//printf("%g ", a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w);
			//x = p1[0]; y = p1[1]; w = p1[2];
			//printf("%g ", a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w);
			//x = p2[0]; y = p2[1]; w = p2[2];
			//printf("%g ", a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w);
			//x = p3[0]; y = p3[1]; w = p3[2];
			//printf("%g ", a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w);
			//x = p4[0]; y = p4[1]; w = p4[2];
			//printf("%g\n", a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w);
		    //} else {
			//printf("toconic failed\n");
		    //}

		    for (i = 0; i < 100000; i++) {
			p0[0] = (FLOAT) (rand()%30);
			p0[1] = (FLOAT) (rand()%30);
			p0[2] = (FLOAT) (rand()%30);
			p1[0] = (FLOAT) (rand()%30);
			p1[1] = (FLOAT) (rand()%30);
			p1[2] = (FLOAT) (rand()%30);
			p2[0] = (FLOAT) (rand()%30);
			p2[1] = (FLOAT) (rand()%30);
			p2[2] = (FLOAT) (rand()%30);
			p3[0] = (FLOAT) (rand()%30);
			p3[1] = (FLOAT) (rand()%30);
			p3[2] = (FLOAT) (rand()%30);
			p4[0] = (FLOAT) (rand()%30);
			p4[1] = (FLOAT) (rand()%30);
			p4[2] = (FLOAT) (rand()%30);
			if (toconic(p0, p1, p2, p3, p4, &a, &b, &c, &d, &e, &f)) {
			    x = p0[0]; y = p0[1]; w = p0[2];
			    s0=a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w;
			    x = p1[0]; y = p1[1]; w = p1[2];
			    s1=a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w;
			    x = p2[0]; y = p2[1]; w = p2[2];
			    s2=a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w;
			    x = p3[0]; y = p3[1]; w = p3[2];
			    s3=a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w;
			    x = p4[0]; y = p4[1]; w = p4[2];
			    s4=a*x*x+b*x*y+c*y*y+d*x*w+e*y*w+f*w*w;
			    if (abs(s0) > .00001 || abs(s1) > .00001 || abs(s2) > .00001
				|| abs(s3) > .00001 || abs(s4) > .00001) {
				    printf("%g %g %g %g %g\n", s0, s1, s2, s3, s4);
				    printf("\t%g %g %g\n", p0[0], p0[1], p0[2]);
				    printf("\t%g %g %g\n", p1[0], p1[1], p1[2]);
				    printf("\t%g %g %g\n", p2[0], p2[1], p2[2]);
				    printf("\t%g %g %g\n", p3[0], p3[1], p3[2]);
				    printf("\t%g %g %g\n", p4[0], p4[1], p4[2]);
				    printf("%g %g %g %g %g %g\n", a, b, c, d, e, f);
				}
			}
		    }
		}


	}
	*/
}
