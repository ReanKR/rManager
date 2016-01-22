package com.ReanKR.rTutorial;

import org.bukkit.Bukkit;

public class ErrorReporter
{
	public static void ResultErrorReport()
	{
		if(rTutorial.ErrorReporting.size() == 0)
		{
			return;
		}
		Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + "」e============ 」cE」frror」cR」feport 」e============");
		int i = 0;
		while(i != rTutorial.ErrorReporting.size())
		{
			Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + rTutorial.ErrorReporting.get(i));
			i++;
		}
		Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + "」e============ 」cE」frror」cR」feport 」e============");
	}
	

}
