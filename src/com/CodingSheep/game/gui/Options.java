package com.CodingSheep.game.gui;

import java.awt.Choice;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.CodingSheep.game.Config;

public class Options extends JFrame
{
	private Config config = new Config();
	private int buttonWidth = 80;
	private int buttonHeight = 40;
	private int width = 550;
	private int height = 450;
	private int w = 0;
	private int h = 0;
	private JButton OK;
	private JPanel window = new JPanel();
	private JLabel lWidth, lHeight;
	private JTextField tWidth, tHeight;
	private Rectangle rOK, rRes;
	
	private Choice res = new Choice();
	
	public Options()
	{
		setUndecorated(true);
		setTitle("Options - 3D Test Game");
		setSize(width, height);
		add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawButtons();
		repaint();
	}
	
	private void drawButtons()
	{
		OK = new JButton("Ok");
		rOK = new Rectangle((width - 100), (height - 70), buttonWidth, (buttonHeight - 10));
		OK.setBounds(rOK);
		window.add(OK);
		
		rRes = new Rectangle(50, 80, 80, 25);
		res.setBounds(rRes);
		res.add("640 x 480");
		res.add("800 x 600");
		res.add("1024 x 768");
		res.select(1);
		window.add(res);
		
		lWidth = new JLabel("Width:");
		lHeight = new JLabel("Height:");
		tWidth = new JTextField();
		tHeight = new JTextField();
		
		lWidth.setBounds(30, 150, 120, 20);
		lHeight.setBounds(30, 180, 120, 20);
		tWidth.setBounds(80, 150, 60, 20);
		tHeight.setBounds(80, 180, 60, 20);
		
		lWidth.setFont(new Font("Century" , 0, 14));
		lHeight.setFont(new Font("Century" , 0, 14));
		
		window.add(lWidth);
		window.add(lHeight);
		window.add(tWidth);
		window.add(tHeight);
		
		OK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				config.saveConfig("width", parseWidth());
				config.saveConfig("height", parseHeight());
			}
		});
	}
	
	private void drop()
	{
		int selection = res.getSelectedIndex();
		if(selection == 0)
		{
			w = 640;
			h = 480;
		}
		if(selection == 1 || selection == -1)
		{
			w = 800;
			h = 600;
		}
		if(selection == 2)
		{
			w = 1024;
			h = 768;
		}
	}
	
	private int parseHeight()
	{
		try
		{
			int h = Integer.parseInt(tHeight.getText());
			return h;
		}
		catch(NumberFormatException e)
		{
			drop();
			return h;
		}
	}
	
	private int parseWidth()
	{
		try
		{
			int w = Integer.parseInt(tWidth.getText());
			return w;
		}
		catch(NumberFormatException e)
		{
			drop();
			return w;
		}
	}
}