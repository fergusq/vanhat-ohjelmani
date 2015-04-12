package perlin;
 
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
 
import javax.imageio.ImageIO;
 
public class Noise2D
{
	BufferedImage	image;
 
	public static void main(String[] args)
	{
		// generate a perlin noise map
		Noise2D[] noise = new Noise2D[5];
		float amplitude = 1.0f;
		double persistence = 2.5;
		int width = 1024;
		int height = 1024;
		noise[0] = new Noise2D(width, height, 2, 2, amplitude);
		amplitude /= persistence;
		noise[1] = new Noise2D(width, height, 4, 4, amplitude);
		amplitude /= persistence;
		noise[2] = new Noise2D(width, height, 8, 8, amplitude);
		amplitude /= persistence;
		noise[3] = new Noise2D(width, height, 16, 16, amplitude);
		amplitude /= persistence;
		noise[4] = new Noise2D(width, height, 32, 32, amplitude);
		BufferedImage perlin = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = perlin.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		for (int i = 0; i < noise.length; i++)
		{
			g.drawImage(noise[i].image, null, 0, 0);
		}
		// Manually re-calculate the RGB values
		// this is the section of code I would ideally like to replace
		for (int i = 0; i < perlin.getWidth(); i++)
		{
			for (int j = 0; j < perlin.getHeight(); j++)
			{
				int color = perlin.getRGB(i, j);
				float val = (float) Math.pow((color & 0xFF) / 255.0f, 5.0);
				assert (val <= 1.0f);
				color = (((int) (val * 0xFF)) << 16)
						+ (((int) (val * 0xFF)) << 8) + ((int) (val * 0xFF));
				perlin.setRGB(i, j, color);
			}
		}
		// write the image out to test.png so I can view it easily
		try
		{
			ImageIO.write(perlin, "png", new File("test.png"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	public Noise2D(int width, int height, int freqX, int freqY, float alpha)
	{
		BufferedImage temp = new BufferedImage(freqX, freqY,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = temp.createGraphics();
		// generate a low-res random image
		for (int i = 0; i < freqX; i++)
		{
			for (int j = 0; j < freqY; j++)
			{
				int val = new Random().nextInt(255);
				g.setColor(new Color(val, val, val, (int) (alpha * 0xFF)));
				g.fillRect(i, j, 1, 1);
			}
		}
		g.dispose();
		// re-scale the image up using interpolation (in this case, cubic)
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
 
		g.drawImage(temp, 0, 0, width, height, 0, 0, freqX, freqY, null);
		g.dispose();
	}
}
