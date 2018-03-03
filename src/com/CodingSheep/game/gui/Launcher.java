package com.CodingSheep.game.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.CodingSheep.game.Config;
import com.CodingSheep.game.Display;
import com.CodingSheep.game.RunGame;
import com.CodingSheep.game.input.InputHandler;

public class Launcher extends Canvas implements Runnable
{	
	private boolean running = false;
	private JButton play, options, help, quit;
	private JPanel window = new JPanel();
	private Rectangle rPlay, rOptions, rHelp, rQuit;
	private int buttonWidth = 80;
	private int buttonHeight = 40;
	private int height = 400;
	private int width = 800;
	
	Config config = new Config();
	JFrame frame = new JFrame();
	Thread thread;
	
	public Launcher(int id)
	{	
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		frame.setUndecorated(true);
		frame.setTitle("3D Test Game");
		frame.setSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().add(window);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		window.setLayout(null);
		
		if(id == 0)
			drawButtons();
		
		InputHandler input = new InputHandler();
		addFocusListener(input);
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		startMenu();
		frame.repaint();
	}
	
	public void run()
	{
		requestFocus();
		while(running)
		{
			renderMenu();
			updateFrame();
		}
	}
	
	public void startMenu()
	{
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}
	
	public void stopMenu()
	{
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	//Delete Later
	private void drawButtons()
	{
		
		//Making Buttons and adding them
		play = new JButton("Play!");
		options = new JButton("Options");
		help = new JButton("Help");
		quit = new JButton("Exit");
		
		rPlay = new Rectangle(width / 2 - buttonWidth / 2, 90, buttonWidth, buttonHeight);
		rOptions = new Rectangle(width / 2 - buttonWidth / 2, 140, buttonWidth, buttonHeight);
		rHelp = new Rectangle(width / 2 - buttonWidth / 2, 190, buttonWidth, buttonHeight);
		rQuit = new Rectangle(width / 2 - buttonWidth / 2, 240, buttonWidth, buttonHeight);
		
		play.setBounds(rPlay);
		options.setBounds(rOptions);
		help.setBounds(rHelp);
		quit.setBounds(rQuit);
		
		window.add(play);
		window.add(options);
		window.add(help);
		window.add(quit);
		
		//Action Listeners
		play.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				config.loadConfig("res/settings/config.xml");
				frame.dispose();
				new RunGame();
			}
		});
		
		options.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
				new Options();
			}
		});
		
		help.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Help");
			}
		});
		
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
	}

	private void renderMenu() throws IllegalStateException
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 400);
		try
		{
			g.drawImage(ImageIO.read(Display.class.getResource("/menuImage.png")), 0, 0, 800, 400, null);
			//Play Button
			if(InputHandler.mouseX > 690 && InputHandler.mouseX < 770 && InputHandler.mouseY > 130 && InputHandler.mouseY < 160)
			{
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/playButtonOn.png")), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 770, 135, 20, 20, null);
				if(InputHandler.mouseButton == 1)
				{
					config.loadConfig("res/settings/config.xml");
					frame.dispose();
					new RunGame();
				}
			}
			else
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/playButton.png")), 690, 130, 80, 30, null);
			
			//Options Button
			if(InputHandler.mouseX > 670 && InputHandler.mouseX < 770 && InputHandler.mouseY > 170 && InputHandler.mouseY < 200)
			{
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/optionsOn.png")), 670, 170, 100, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 770, 175, 20, 20, null);
				if(InputHandler.mouseButton == 1)
				{
					new Options();
				}
			}
			else
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/optionsOff.png")), 670, 170, 100, 30, null);
			
			//Help Button
			if(InputHandler.mouseX > 690 && InputHandler.mouseX < 770 && InputHandler.mouseY > 210 && InputHandler.mouseY < 240)
			{
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/helpOn.png")), 690, 210, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 770, 215, 20, 20, null);
				if(InputHandler.mouseButton == 1)
					System.out.println("Help");
			}
			else
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/helpOff.png")), 690, 210, 80, 30, null);
			
			//Exit Button
			if(InputHandler.mouseX > 690 && InputHandler.mouseX < 770 && InputHandler.mouseY > 250 && InputHandler.mouseY < 280)
			{
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exitOn.png")), 690, 250, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 770, 255, 20, 20, null);
				if(InputHandler.mouseButton == 1)
					System.exit(0);
			}
			else
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exitOff.png")), 690, 250, 80, 30, null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		g.dispose();
		bs.show();
	}
	
	private void updateFrame()
	{
		if(InputHandler.dragged)
		{
			Point p = frame.getLocation();
			frame.setLocation(p.x + InputHandler.mouseDX - InputHandler.mousePX, p.y + InputHandler.mouseDY - InputHandler.mousePY);
		}
	}
}