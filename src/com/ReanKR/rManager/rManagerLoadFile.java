package com.ReanKR.rManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class rManagerLoadFile extends JavaPlugin
{
		public static String LoadGuildName(String GuildUUID)
		{
			String GuildName = "NONE";
			File GuildFile = new File("mstore/factions_faction/" + GuildUUID + ".json");
			if(! GuildFile.exists()) return GuildName;
			String Line;
			try
			{
				BufferedReader R = new BufferedReader(new FileReader(GuildFile));
				while((Line = R.readLine()) != null)
				{
					String[] Cut = Line.split(": ");
					if(Cut[0].equalsIgnoreCase("  \"name\""))
					{
						String[] Cut2 = Cut[1].split("\"");
						GuildName = Cut2[1];
						break;
					}
				}
				R.close();
			}
			catch(Exception e)
			{
				return GuildName;
			}
			return GuildName;
		}
		
		public static String[] LoadInfoGuild(Player player)
		{
			String[] PlayerInfo = new String[2];
			PlayerInfo[0] = "NONE";
			PlayerInfo[1] = "NONE";
			// PlayerInfo[0] : �÷��̾��� ��� ��, PlayerInfo[1] : ��� �������� ��å
			File PlayerFile = new File("mstore/factions_mplayer/" + player.getUniqueId().toString() + ".json");
			if(! PlayerFile.exists())
			{
				PlayerInfo[0] = "NONE";
				PlayerInfo[1] = "NONE";
				return PlayerInfo;
			}
			String Line;
			try
			{
				BufferedReader R = new BufferedReader(new FileReader(PlayerFile));
				while((Line = R.readLine()) != null)
				{
					String[] Cut = Line.split(": ");
					if(Cut[0].equalsIgnoreCase("  \"factionId\""))
					{
						String[] Cut2 = Cut[1].split("\"");
						PlayerInfo[0] = Cut2[1];
					}
					if(Cut[0].equalsIgnoreCase("  \"role\""))
					{
						
						String[] Cut2 = Cut[1].split("\"");
						PlayerInfo[1] = Cut2[1];
						break;
					}
				}
				R.close();
			}
			catch(Exception e)
			{
				Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��c�÷��̾��� ������ �Ҿ���� �� ������ ���� �о�� �� �������ϴ�.");
				PlayerInfo[0] = "LOAD_FAILED";
				PlayerInfo[1] = "LOAD_FAILED";
				return PlayerInfo;
			}
			return PlayerInfo;
		}
}
