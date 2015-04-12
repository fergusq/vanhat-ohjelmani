package cubegame.map;

public class PerlinMapGenerator {
	public float[][] heights;
	 public int size;
	 
	 private PerlinGenerator Perlin;
	 
	 public PerlinMapGenerator(int size, int seed)
	 {
	  this.size = size;
	  heights = new float[size][size];
	  Perlin = new PerlinGenerator(seed);
	 }

	 public void init(){
		addPerlinNoise(6.0f);
		perturb(32.0f, 32.0f);
		for (int i = 0; i < 32; i++ ) erode(16.0f);
		smoothen();
	 }
	 
	 public void addPerlinNoise(float f)
	 {
	  for (int i = 0; i < size; i++)
	  {
	   for (int j = 0; j < size; j++)
	   {
	    heights[i][j] += Perlin.noise(f * i / (float)size, f * j / (float)size, 0);
	   }
	  }
	 }
	 
	 public void perturb(float f, float d)
	 {
	  int u, v;
	  float[][] temp = new float[size][size];
	  for (int i = 0; i < size; ++i)
	  {
	   for (int j = 0; j < size; ++j)
	   {
	    u = i + (int)(Perlin.noise(f * i / (float)size, f * j / (float)size, 0) * d);
	    v = j + (int)(Perlin.noise(f * i / (float)size, f * j / (float)size, 1) * d);
	    if (u < 0) u = 0; if (u >= size) u = size - 1;
	    if (v < 0) v = 0; if (v >= size) v = size - 1;
	    temp[i][j] = heights[u][v];
	   }
	  }
	  heights = temp;
	 }
	 
	 public void erode(float smoothness)
	 {
	  for (int i = 1; i < size - 1; i++)
	  {
	   for (int j = 1; j < size - 1; j++)
	   {
	    float d_max = 0.0f;
	    int[] match = { 0, 0 };
	  
	    for (int u = -1; u <= 1; u++)
	    {
	     for (int v = -1; v <= 1; v++)
	     {
	      if(Math.abs(u) + Math.abs(v) > 0)
	      {
	       float d_i = heights[i][j] - heights[i + u][j + v];
	       if (d_i > d_max)
	       {
	        d_max = d_i;
	        match[0] = u; match[1] = v;
	       }
	      }
	     }
	    }
	  
	    if(0 < d_max && d_max <= (smoothness / (float)size))
	    {
	     float d_h = 0.5f * d_max;
	     heights[i][j] -= d_h;
	     heights[i + match[0]][j + match[1]] += d_h;
	    }
	   }
	  }
	 }
	 
	 public void smoothen()
	 {
	  for (int i = 1; i < size - 1; ++i)
	  {
	   for (int j = 1; j < size - 1; ++j)
	   {
	    float total = 0.0f;
	    for (int u = -1; u <= 1; u++)
	    {
	     for (int v = -1; v <= 1; v++)
	     {
	      total += heights[i + u][j + v];
	     }
	    }
	  
	    heights[i][j] = total / 9.0f;
	   }
	  }
	 }
}
