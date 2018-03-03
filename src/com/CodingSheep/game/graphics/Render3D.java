package com.CodingSheep.game.graphics;

import java.util.Random;

import com.CodingSheep.game.Display;
import com.CodingSheep.game.Game;
import com.CodingSheep.game.input.Controller;
import com.CodingSheep.game.level.Block;
import com.CodingSheep.game.level.Level;

public class Render3D extends Render
{	
	private double[] zBuffer;
	private double[] zBufferWall;
	private double renderDistance = 5000;
	private double forward, right, up, walking, cos, sin;
	private int c = 0;
	private int spriteSheetWidth = 128;
	
	public Render3D(int width, int height)
	{
		super(width, height);
		zBuffer = new double[width * height];
		zBufferWall = new double[width];
	}
	
	public void floor(Game game)
	{
		//Floor and Ceiling
		double ceilingPos = 8;
		double floorPos = 8;
		
		//Movement
		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;
		walking = 0;
		
		//Rotations
		double rotation = game.controls.rotation;
		cos = Math.cos(rotation);
		sin = Math.sin(rotation);
		
		for(int x = 0; x < width; x++)
			zBufferWall[x] = 0;
		
		for(int y = 0; y < height; y++)
		{	
			double ceiling = (y - height / 2.0) / height;
			double z = (floorPos + up) / ceiling;
			c = 0;
			
			//Handles Walk Speed
			if(Controller.crouchWalk && Controller.walk)
			{
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPos + up + walking) / ceiling;
			}
			if(Controller.runWalk && Controller.walk)
			{
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPos + up + walking) / ceiling;
			}
			if(Controller.walk)
			{
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPos + up + walking) / ceiling;
			}
			
			if(ceiling < 0)
			{
				z = (ceilingPos - up) / -ceiling;
				c = 1;
				if(Controller.walk)
					z = (ceilingPos - up - walking) / -ceiling;
			}
			
			for(int x = 0; x < width; x++)
			{
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cos + z * sin;
				double yy = z * cos - depth * sin;
				
				int xPix = (int)((xx + right) * 4);
				int yPix = (int)((yy + forward) * 4);
				zBuffer[x + y * width] = z;
				if(c == 0)
					pixels[x + y * width] = Texture.texture.pixels[(xPix & 31) + (yPix & 31) * spriteSheetWidth];
				else
					pixels[x + y * width] = Texture.texture.pixels[((xPix & 31) + 32) + (yPix & 31) * spriteSheetWidth];
				
				if(z > 500)
					pixels[x + y * width] = 0;
			}
		}
		
		Level level = game.level;
		int size = 50;
		for(int xBlock = -size; xBlock <= size; xBlock++)
			for(int zBlock = -size; zBlock <= size; zBlock++)
			{
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				
				if(block.solid)
				{
					if(!east.solid)
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					if(!south.solid)
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
				}
				else
				{
					if(east.solid)
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					if(south.solid)
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
				}
			}
		for(int xBlock = -size; xBlock <= size; xBlock++)
			for(int zBlock = -size; zBlock <= size; zBlock++)
			{
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				
				if(block.solid)
				{
					if(!east.solid)
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					if(!south.solid)
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
				}
				else
				{
					if(east.solid)
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					if(south.solid)
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
				}
			}
		for(int xBlock = -size; xBlock <= size; xBlock++)
			for(int zBlock = -size; zBlock <= size; zBlock++)
			{
				Block block = level.create(xBlock, zBlock);
				for(int s = 0; s < block.sprites.size(); s++)
				{
					Sprite sprite = block.sprites.get(s);
					renderSprite(xBlock + sprite.x, sprite.y, zBlock + sprite.z, 0.5);
				}
			}
	}

	public void renderDistanceLimiter()
	{
		for(int i = 0; i < width * height; i++)
		{
			int color = pixels[i];
			int brightness = (int)(renderDistance / zBuffer[i]);
			
			if(brightness < 0)
				brightness = 0;
			if(brightness > 255)
				brightness = 255;
			
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;
			
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}
	
	public void renderSprite(double x, double y, double z, double heightOffset)
	{
		double correct = 0.0625;
		
		double xCalc = ((x / 2) - (right * correct)) * 2 + 0.5;
		double yCalc = ((y / 2) - (up * -0.125)) + (walking * correct) * 2 + heightOffset;
		double zCalc = ((z / 2) - (forward * correct)) * 2;
		
		double rotX = xCalc * cos - zCalc * sin;
		double rotY = yCalc;
		double rotZ = zCalc * cos + xCalc * sin;
		
		double centerX = Display.width / 2;
		double centerY = Display.height / 2;
		
		double xPixel = rotX / rotZ * height + centerX;
		double yPixel = rotY / rotZ * height + centerY;
		
		double xPixelL = xPixel - (height / 2) / rotZ;
		double xPixelR = xPixel + (height / 2) / rotZ;
		double yPixelL = yPixel - (height / 2) / rotZ;
		double yPixelR = yPixel + (height / 2) / rotZ;
		
		int xpl = (int)xPixelL;
		int xpr = (int)xPixelR;
		int ypl = (int)yPixelL;
		int ypr = (int)yPixelR;
		
		//Fixes Clipping
		if(xpl < 0)
			xpl = 0;
		if(xpr > width)
			xpr = width;
		if(ypl < 0)
			ypl = 0;
		if(ypr > height)
			ypr = height;
		
		rotZ *= 8;
		
		for(int yp = ypl; yp < ypr; yp++)
		{
			double pixelRotY = (yp - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int)(pixelRotY * 32);
			for(int xp = xpl; xp < xpr; xp++)
			{
				double pixelRotX = (xp - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int)(pixelRotX * 32);
				if(zBuffer[xp + yp * width] > rotZ)
				{
					int color = Texture.texture.pixels[((xTexture & 31) + 96) + (yTexture & 31) * spriteSheetWidth];
					if(color != 0xffff80ff)
					{
						pixels[xp + yp * width] = color;
						zBuffer[xp + yp * width] = rotZ;
					}
				}
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistanceL, double zDistanceR, double yHeight)
	{
		//Corrections for Walls
		double forwardCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double upCorrect = 0.0625;
		double walkCorrect = -0.0625;
		
		//Calculations
		double xCalcL = ((xLeft / 2) - (right * rightCorrect)) * 2;
		double xCalcR = ((xRight / 2) - (right * rightCorrect)) * 2;
		double zCalcL = ((zDistanceL / 2) - (forward * forwardCorrect)) * 2;
		double zCalcR = ((zDistanceR / 2) - (forward * forwardCorrect)) * 2;
		
		//Corners
		double yCornerTL = ((-yHeight) - (-up * upCorrect + walking * walkCorrect)) * 2;
		double yCornerTR = ((-yHeight) - (-up * upCorrect + walking * walkCorrect)) * 2;
		double yCornerBL = ((0.5 - yHeight) - (-up * upCorrect + walking * walkCorrect)) * 2;
		double yCornerBR = ((0.5 - yHeight) - (-up * upCorrect + walking * walkCorrect)) * 2;
		
		//Rotations
		double rotLeftX = xCalcL * cos - zCalcL * sin;
		double rotRightX = xCalcR * cos - zCalcR * sin;
		double rotLeftZ = zCalcL * cos + xCalcL * sin;
		double rotRightZ = zCalcR * cos + xCalcR * sin;
		
		//Textures + Helper Algorithms
		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;
				
		if(rotLeftZ < clip && rotRightZ < clip)
			return;
		if(rotLeftZ < clip)
		{
			double clip0 = (clip - rotLeftZ) / (rotRightZ - rotLeftZ);
			rotLeftZ = rotLeftZ + (rotRightZ - rotLeftZ) * clip0;
			rotLeftX = rotLeftX + (rotRightX - rotLeftX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}
		if(rotRightZ < clip)
		{
			double clip0 = (clip - rotLeftZ) / (rotRightZ - rotLeftZ);
			rotRightZ = rotLeftZ + (rotRightZ - rotLeftZ) * clip0;
			rotRightX = rotLeftX + (rotRightX - rotLeftX) * clip0;
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}
		
		double tex1 = 1 / rotLeftZ;
		double tex2 = 1 / rotRightZ;
		double tex3 = tex30 / rotLeftZ;
		double tex4 = tex40 / rotRightZ - tex3;
		
		//Pixels
		double xPixL = rotLeftX / rotLeftZ * height + width / 2;
		double xPixR = rotRightX / rotRightZ * height + width / 2;
		
		//Get Rid of Negative Pixels
		if(xPixL >= xPixR)
			return;
		
		int xPixIntL = (int)xPixL;
		int xPixIntR = (int)xPixR;
		
		if(xPixIntL < 0)
			xPixIntL = 0;
		if(xPixIntR > width)
			xPixIntR = width;
		
		//Wall Boundaries and Rendering
		double yPixTL = yCornerTL / rotLeftZ * height + height / 2.0;
		double yPixBL = yCornerBL / rotLeftZ * height + height / 2.0;
		double yPixTR = yCornerTR / rotRightZ * height + height / 2.0;
		double yPixBR = yCornerBR / rotRightZ * height + height / 2.0;
		
		//Wall Maker
		for(int x = xPixIntL; x < xPixIntR; x++)
		{	
			double pixRotX = (x - xPixL) / (xPixR - xPixL);
			double yPixT = yPixTL + (yPixTR - yPixTL) * pixRotX;
			double yPixB = yPixBL + (yPixBR - yPixBL) * pixRotX;
			double zWall = tex1 + (tex2 - tex1) * pixRotX;
			int xTex = (int) (((tex3 + tex4 * pixRotX) / zWall) * 4);
			int yPixIntT = (int) (yPixT);
			int yPixIntB = (int) (yPixB);
			
			//Clip Buffer
			if(zBufferWall[x] > zWall)
				continue;
			zBufferWall[x] = zWall;
			
			//Nothing Out of Bounds
			if(yPixIntT < 0)
				yPixIntT = 0;
			if(yPixIntB > height)
				yPixIntB = height;
			
			for(int y = yPixIntT; y < yPixIntB; y++)
			{
				double pixRotY = (y - yPixT) / (yPixB - yPixT);
				int yTex = (int) (32 * pixRotY);
				pixels[x + y * width] = Texture.texture.pixels[((xTex & 31) + 64) + ((yTex & 31) + 0) * spriteSheetWidth];
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixRotX) * 8;
			}
		}
	}
}