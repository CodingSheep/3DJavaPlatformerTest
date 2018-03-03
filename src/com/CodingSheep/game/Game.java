package com.CodingSheep.game;

import java.awt.event.KeyEvent;

import com.CodingSheep.game.input.Controller;
import com.CodingSheep.game.level.Level;

public class Game
{
	public int time;
	public Controller controls;
	public Level level;
	
	public Game()
	{
		controls = new Controller();
		level = new Level(50, 50);
	}
	
	public void tick(boolean[] key)
	{
		time++;
		boolean forward = key[KeyEvent.VK_W];
		boolean left = key[KeyEvent.VK_A];
		boolean back = key[KeyEvent.VK_S];
		boolean right = key[KeyEvent.VK_D];
		boolean rLeft = key[KeyEvent.VK_LEFT];
		boolean rRight = key[KeyEvent.VK_RIGHT];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_CONTROL];
		boolean run = key[KeyEvent.VK_SHIFT];
		boolean quit = key[KeyEvent.VK_ESCAPE];
		
		controls.tick(forward, left, back, right, rLeft, rRight, jump, crouch, run, quit);
	}
}
