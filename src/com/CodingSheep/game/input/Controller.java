package com.CodingSheep.game.input;

import com.CodingSheep.game.Display;
import com.CodingSheep.game.gui.Launcher;

public class Controller
{
	public double x, y, z, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;
	
	public void tick(boolean forward, boolean left, boolean back, boolean right, boolean rLeft, boolean rRight, boolean jump, boolean crouch, boolean run, boolean quit)
	{
		double crouchHeight = 0.3;
		double jumpHeight = 0.5;
		double rotationSpeed = 0.002 * Display.mouseSpeed;
		double walkSpeed = 0.75;
		double xMove = 0;
		double zMove = 0;
		
		if(forward && !back)
		{
			zMove = walkSpeed;
			walk = true;
		}
		if(back && !forward)
		{
			zMove = -walkSpeed;
			walk = true;
		}
		if(left && !right)
		{
			xMove = -walkSpeed;
			walk = true;
		}
		
		if(right && !left)
		{
			xMove = walkSpeed;
			walk = true;
		}
		if(turnLeft)
			rotationa -= rotationSpeed;
		if(turnRight)
			rotationa += rotationSpeed;
		if(rLeft)
			rotationa -= 0.012;
		if(rRight)
			rotationa += 0.012;
		if(jump)
		{
			y += jumpHeight;
			run = false;
			walk = false;
		}
		if(crouch)
		{
			y -= crouchHeight;
			walkSpeed = 0.2;
			run = false;
			crouchWalk = true;
		}
		if(run)
		{
			walkSpeed = 1;
			walk = true;
			runWalk = true;
		}
		if(!forward && !left && !right && !back)
			walk = false;
		if(!crouch)
			crouchWalk = false;
		if(!run)
			runWalk = false;
		
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		
		x += xa;
		z += za;
		xa *= 0.1;
		y *= 0.9;
		za *= 0.1;
		
		rotation += rotationa;
		rotation *= 0.8;
		
		if(quit)
			System.exit(0);
	}
}