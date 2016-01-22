package com.ReanKR.rTutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class rTutorialRegister
{
	public static void LocationRegister(Location Loc, String Msg, String SubMsg, int FadeIn, int FadeOut, int Duration, int index) throws IOException
	{
		File file = new File("plugins/rTutorial/Location.yml");
		YamlConfiguration LocationYaml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection CS = LocationYaml.getConfigurationSection("Locations");
		Double x = Loc.getX();
		Double y = Loc.getY();
		Double z = Loc.getZ();
		float pitch = Loc.getPitch();
		float yaw = Loc.getYaw();
		CS.createSection(String.valueOf(rTutorial.MethodAmount + 1));
		ConfigurationSection CS2 = LocationYaml.getConfigurationSection("Locations").getConfigurationSection(String.valueOf(rTutorial.MethodAmount + 1));
		CS2.createSection("Location");
		CS2.createSection("Message");
		ConfigurationSection CS3 = rTutorialFileLoader.PlusSelect(CS2, "Location");
		CS3.set("Coordinate", x + "," + y + "," + z);
		CS3.set("Angle", String.valueOf(pitch + "," + yaw));
		ConfigurationSection CS4 = rTutorialFileLoader.PlusSelect(CS2, "Message");
		CS4.createSection("Time");
		ConfigurationSection Time = rTutorialFileLoader.PlusSelect(CS4, "Time");
		if(FadeIn != 0)
		{
			Time.set("FadeIn", FadeIn);
		}
		if(FadeOut != 0)
		{
			Time.set("FadeOut", FadeOut);
		}
		if(Duration != 0)
		{
			Time.set("Duration", Duration);
		}
		CS4.set("Main", String.valueOf(Msg));
		if(!(SubMsg.equalsIgnoreCase("NONE") || SubMsg.equalsIgnoreCase(null)))
		{
			CS4.set("Sub", String.valueOf(SubMsg));
		}
		LocationYaml.save(file);
		rTutorialFileLoader.LocationCfg();
	}
}
