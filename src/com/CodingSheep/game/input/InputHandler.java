package com.CodingSheep.game.input;

import java.awt.event.*;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener
{
	public static boolean dragged = false;
	public static int mouseX, mouseY, mouseDX, mouseDY, mousePX, mousePY, mouseButton;
	public boolean[] key = new boolean[68836];
	
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length)
			key[keyCode] = true;
	}

	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length)
			key[keyCode] = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void focusGained(FocusEvent e)
	{
		
	}

	public void focusLost(FocusEvent e)
	{
		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	public void mouseDragged(MouseEvent e)
	{
		dragged = true;
		mouseDX = e.getX();
		mouseDY = e.getY();
	}

	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mousePressed(MouseEvent e)
	{
		mouseButton = e.getButton();
		mousePX = e.getX();
		mousePY = e.getY();
	}
	
	public void mouseReleased(MouseEvent e)
	{
		dragged = false;
		mouseButton = 0;
	}
}