package com.CodingSheep.game.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Texture
{
	public static Render texture = loadBitmap("/textures/blocks.png");
	
	public static Render loadBitmap(String fileName)
	{
		try
		{
			BufferedImage img = ImageIO.read(Texture.class.getResource(fileName));
			int width = img.getWidth();
			int height = img.getHeight();
			Render result = new Render(width, height);
			img.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
		}catch(Exception e)
		{
			System.out.println("Missing file at: " + texture);
			throw new RuntimeException(e);
		}
	}
}