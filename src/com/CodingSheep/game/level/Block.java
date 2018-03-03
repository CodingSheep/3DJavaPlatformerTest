package com.CodingSheep.game.level;

import java.util.List;
import java.util.ArrayList;
import com.CodingSheep.game.graphics.Sprite;

public class Block
{
	public boolean solid = false;
	public static Block solidWall = new SolidBlock();
	public List<Sprite> sprites = new ArrayList<Sprite>();
	
	public void addSprite(Sprite sprite)
	{
		sprites.add(sprite);
	}
}
