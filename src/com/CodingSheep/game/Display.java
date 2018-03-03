package com.CodingSheep.game;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.CodingSheep.game.graphics.Screen;
import com.CodingSheep.game.gui.Launcher;
import com.CodingSheep.game.input.Controller;
import com.CodingSheep.game.input.InputHandler;

/**
 * @author Jarrod Raine
 * @author CodingSheep
 */

public class Display extends Canvas implements Runnable
{
	public static final String TITLE = "3D Test Game";
	public static double mouseSpeed;
	public static int selection = 0;
	public static int width = 800;
	public static int height = 600;
	
	private boolean running = false;
	private BufferedImage img;
	private Game game;
	private InputHandler input;
	private int fps;
	private int newX = 0;
	private int oldX = 0;
	private int[] pixels;
	private static Launcher launcher;
	private Screen screen;
	private Thread thread;
	
	public Display()
	{
		Dimension size = new Dimension(getGameWidth(), getGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		screen = new Screen(getGameWidth(), getGameHeight());
		game = new Game();
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	public static void main(String[] args)
	{
		getLauncherInstance();
	}
	
	public static Launcher getLauncherInstance()
	{
		if(launcher == null)
			launcher = new Launcher(0);
		return launcher;
	}
	
	public synchronized void start()
	{
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void run()
	{
		double delta = 0;
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int updates = 0;
		long previousTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		requestFocus();
		while(running)
		{
			long currentTime = System.nanoTime();
			delta += (currentTime - previousTime) / ns;
			previousTime = currentTime;
			if(delta >= 1)
			{
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			while(System.currentTimeMillis() - timer > 1000)
			{
				timer++;
				System.out.println();
				fps = frames;
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void stop()
	{
		if(!running)
			return;
		running = false;
		try
		{
			thread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public int getGameHeight()
	{
		return height;
	}
	
	public int getGameWidth()
	{
		return width;
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		screen.render(game);
		
		for(int i = 0; i < getGameWidth() * getGameHeight(); i++)
			pixels[i] = screen.pixels[i];
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
		g.setFont(new Font("Comic Sans MS", 0, 20));
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + fps, 0, 15);
		g.dispose();
		bs.show();
	}
	
	private void tick()
	{
		game.tick(input.key);
		
		newX = InputHandler.mouseX;
		if(newX > oldX)
			Controller.turnRight = true;
		if(newX < oldX)
			Controller.turnLeft = true;
		if(newX == oldX)
		{
			Controller.turnLeft = false;
			Controller.turnRight = false;
		}
		mouseSpeed = Math.abs(newX - oldX);
		oldX = newX;
	}
}