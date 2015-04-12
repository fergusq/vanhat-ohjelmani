package cubegame.map;

import java.util.Random;

public class PerlinNoiseC implements NoiseGenerator{

	private final static int B  = 0x100;
	private final static int BM  = 0xff;
	private final static int N  = 0x1000;
	private final static int NP = 12;   /* 2^N */
	private final static int NM = 0xfff;
	
	private static int[] p = new int[B + B + 2];
	private static double[][] g3 = new double[B + B + 2][3];
	private static double[][] g2 = new double[B + B + 2][2];
	private static double[] g1 = new double[B + B + 2];
	private static int start = 1;

	static double lerp(double t, double a, double b) { return a + t * (b - a); }
	static double s_curve(double t) { return t * t * (3. - 2. * t); }
	static double at2(double rx, double ry, double[] q) { return rx * q[0] + ry * q[1]; }
	static double at3(double rx, double ry, double rz, double[] q) { return rx * q[0] + ry * q[1] + rz * q[2]; }

	static double noise1(double arg)
	{
	   int bx0, bx1;
	   double rx0, rx1, sx, t, u, v;
	   double[] vec = new double[1];
	   

	   vec[0] = arg;
	   if (start == 1) {
	      start = 0;
	      init();
	   }
	   t = vec[0] + N;
		 bx0 = ((int)t) & BM;
		 bx1 = (bx0+1) & BM;
  	rx0 = t - (int)t;
  	rx1 = rx0 - 1.;

	   sx = s_curve(rx0);
	   u = rx0 * g1[ p[ bx0 ] ];
	   v = rx1 * g1[ p[ bx1 ] ];

	   return(lerp(sx, u, v));
	}

	static double noise2(double[] vec)
	{
	   int bx0, bx1, by0, by1, b00, b10, b01, b11;
	   double rx0, rx1, ry0, ry1, sx, sy, a, b, t, u, v;
	double[] q;
	   int i, j;

	   if (start == 1) {
	      start = 0;
	      init();
	   }

	   t = vec[0] + N;
		 bx0 = ((int)t) & BM;
		 bx1 = (bx0+1) & BM;
	rx0 = t - (int)t;
	rx1 = rx0 - 1.;
	
	
	   t = vec[1] + N;
		 by0 = ((int)t) & BM;
		 by1 = (by0+1) & BM;
	ry0 = t - (int)t;
	ry1 = ry0 - 1.;

	   i = p[ bx0 ];
	   j = p[ bx1 ];

	   b00 = p[ i + by0 ];
	   b10 = p[ j + by0 ];
	   b01 = p[ i + by1 ];
	   b11 = p[ j + by1 ];

	   sx = s_curve(rx0);
	   sy = s_curve(ry0);

	   q = g2[ b00 ] ; u = at2(rx0,ry0, q);
	   q = g2[ b10 ] ; v = at2(rx1,ry0, q);
	   a = lerp(sx, u, v);

	   q = g2[ b01 ] ; u = at2(rx0,ry1, q);
	   q = g2[ b11 ] ; v = at2(rx1,ry1, q);
	   b = lerp(sx, u, v);

	   return lerp(sy, a, b);
	}

	
	static double noise3(double[] vec)
	{
	   int bx0, bx1, by0, by1, bz0, bz1, b00, b10, b01, b11;
	   double rx0, rx1, ry0, ry1, rz0, rz1, sy, sz, a, b, c, d, t, u, v;
	double[] q;
	   int i, j;

	   if (start == 1) {
	      start = 0;
	      init();
	   }

	   t = vec[0] + N;
		 bx0 = ((int)t) & BM;
		 bx1 = (bx0+1) & BM;
	rx0 = t - (int)t;
	rx1 = rx0 - 1.;
	
	
	   t = vec[1] + N;
		 by0 = ((int)t) & BM;
		 by1 = (by0+1) & BM;
	ry0 = t - (int)t;
	ry1 = ry0 - 1.;
	
	t = vec[2] + N;
	 bz0 = ((int)t) & BM;
	 bz1 = (bz0+1) & BM;
rz0 = t - (int)t;
rz1 = rz0 - 1.;

	   i = p[ bx0 ];
	   j = p[ bx1 ];

	   b00 = p[ i + by0 ];
	   b10 = p[ j + by0 ];
	   b01 = p[ i + by1 ];
	   b11 = p[ j + by1 ];

	   t  = s_curve(rx0);
	   sy = s_curve(ry0);
	   sz = s_curve(rz0);

	   q = g3[ b00 + bz0 ] ; u = at3(rx0,ry0,rz0, q);
	   q = g3[ b10 + bz0 ] ; v = at3(rx1,ry0,rz0, q);
	   a = lerp(t, u, v);

	   q = g3[ b01 + bz0 ] ; u = at3(rx0,ry1,rz0, q);
	   q = g3[ b11 + bz0 ] ; v = at3(rx1,ry1,rz0, q);
	   b = lerp(t, u, v);

	   c = lerp(sy, a, b);

	   q = g3[ b00 + bz1 ] ; u = at3(rx0,ry0,rz1, q);
	   q = g3[ b10 + bz1 ] ; v = at3(rx1,ry0,rz1, q);
	   a = lerp(t, u, v);

	   q = g3[ b01 + bz1 ] ; u = at3(rx0,ry1,rz1, q);
	   q = g3[ b11 + bz1 ] ; v = at3(rx1,ry1,rz1, q);
	   b = lerp(t, u, v);

	   d = lerp(sy, a, b);

	   return lerp(sz, c, d);
	}

	static double[] normalize2(double[] v)
	{
	   double s;

	   s = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
	   v[0] = v[0] / s;
	   v[1] = v[1] / s;
	   
	   return v;
	}

	static double[] normalize3(double[] v)
	{
	   double s;

	   s = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	   v[0] = v[0] / s;
	   v[1] = v[1] / s;
	   v[2] = v[2] / s;
	   return v;
	}

	static void init()
	{
	   int i, j, k;

	   for (i = 0 ; i < B ; i++) {
	      p[i] = i;
	      g1[i] = (double)((random() % (B + B)) - B) / B;

	      for (j = 0 ; j < 2 ; j++)
	         g2[i][j] = (double)((random() % (B + B)) - B) / B;
	      g2[i] = normalize2(g2[i]);

	      for (j = 0 ; j < 3 ; j++)
	         g3[i][j] = (double)((random() % (B + B)) - B) / B;
	      g3[i] = normalize3(g3[i]);
	   }

	   while (i == 0) {
	      k = p[i];
	      p[i] = p[j = random() % B];
	      p[j] = k;
	      i--;
	   }

	   for (i = 0 ; i < B + 2 ; i++) {
	      p[B + i] = p[i];
	      g1[B + i] = g1[i];
	      for (j = 0 ; j < 2 ; j++)
	         g2[B + i][j] = g2[i][j];
	      for (j = 0 ; j < 3 ; j++)
	         g3[B + i][j] = g3[i][j];
	   }
	}

	private static int random() {
		return (new Random()).nextInt();
	}
	/* --- My harmonic summing functions - PDB --------------------------*/
	/*
	   In what follows "alpha" is the weight when the sum is formed.
	   Typically it is 2, As this approaches 1 the function is noisier.
	   "beta" is the harmonic scaling/spacing, typically 2.
	*/
	
	/**
	   In what follows "alpha" is the weight when the sum is formed.
	   Typically it is 2, As this approaches 1 the function is noisier.
	   "beta" is the harmonic scaling/spacing, typically 2.
	*/

	public static double perlinNoise1D(double x,double alpha,double beta,int n)
	{
	   int i;
	   double val,sum = 0;
	   double p,scale = 1;

	   p = x;
	   for (i=0;i<n;i++) {
	      val = noise1(p);
	      sum += val / scale;
	      scale *= alpha;
	      p *= beta;
	   }
	   return(sum);
	}

	/**
	   In what follows "alpha" is the weight when the sum is formed.
	   Typically it is 2, As this approaches 1 the function is noisier.
	   "beta" is the harmonic scaling/spacing, typically 2.
	*/
	public static double perlinNoise2D(double x,double y,double alpha,double beta,int n)
	{
	   int i;
	   double val,sum = 0;
	   double scale = 1;
	   double[] p = new double[2];

	   p[0] = x;
	   p[1] = y;
	   for (i=0;i<n;i++) {
	      val = noise2(p);
	      sum += val / scale;
	      scale *= alpha;
	      p[0] *= beta;
	      p[1] *= beta;
	   }
	   return(sum);
	}

	/**
	   In what follows "alpha" is the weight when the sum is formed.
	   Typically it is 2, As this approaches 1 the function is noisier.
	   "beta" is the harmonic scaling/spacing, typically 2.
	*/
	public static double perlinNoise3D(double x,double y,double z,double alpha,double beta,int n)
	{
	   int i;
	   double val,sum = 0;
	   double scale = 1;
	   double[] p = new double[3];

	   p[0] = x;
	   p[1] = y;
	   p[2] = z;
	   for (i=0;i<n;i++) {
	      val = noise3(p);
	      sum += val / scale;
	      scale *= alpha;
	      p[0] *= beta;
	      p[1] *= beta;
	      p[2] *= beta;
	   }
	   return(sum);
	}
	@Override
	public float noise(float x, float y, float z) {
		return (float) perlinNoise3D(x, y, z, 2, 2, 1);
	}

}
