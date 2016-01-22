package com.ReanKR.rTutorial;

import java.util.HashMap;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class rTutorialMessage
{
	public static void LoadMessage()
	{
		rTutorial.MainMessages = new HashMap<String, String>();
		FileConfiguration LangFile = rTutorial.LoadFile("message.yml");
		ConfigurationSection LangNode = LangFile.getConfigurationSection("Messages");
		Set<String> MessageMethod = LangNode.getKeys(true);
		for(String MethodName : MessageMethod)
		{
			rTutorial.MainMessages.put(MethodName, LangNode.getString(MethodName));
		}

	}
	public void SubMsg(String MessageMethod, Player player)
	{
		String Message = rTutorial.MainMessages.get(MessageMethod);
		String Replacement = Message.replaceAll("%player%", player.getName());
		String Replacements = Replacement.replaceAll("%command%", "/tutorial");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',rTutorial.Prefix + Replacements));
	}
	
}
