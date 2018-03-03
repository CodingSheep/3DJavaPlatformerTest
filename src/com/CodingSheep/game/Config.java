package com.CodingSheep.game;

import java.io.*;
import java.util.Properties;

public class Config
{
	Properties props = new Properties();
	
	public void loadConfig(String path)
	{
		try
		{
			InputStream read = new FileInputStream(path);
			props.loadFromXML(read);
			String width = props.getProperty("width");
			String height = props.getProperty("height");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		}
		catch(FileNotFoundException e)
		{
			saveConfig("width", 800);
			saveConfig("height", 600);
			loadConfig(path);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveConfig(String key, int value)
	{
		String path = "res/settings/config.xml";
		try
		{
			File file = new File(path);
			boolean exist = file.exists();
			if(!exist)
				file.createNewFile();
			OutputStream write = new FileOutputStream(path);
			props.setProperty(key, Integer.toString(value));
			props.storeToXML(write, "Resolution");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setResolution(int width, int height)
	{
		Display.width = width;
		Display.height = height;
	}
}