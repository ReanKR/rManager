package com.ReanKR.rManager;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class rManagerTeleport
{
	public static void TeleportOtherGuild(Player player, String GuildName)
	{
		File ConfigFile = new File("plugins/rManager/config.yml");
		YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
		rManager.GuildInfo = C.getStringList("Groups");
		int i = 0;
		while(i != rManager.GuildInfo.size())
		{
			String[] Cutter = rManager.GuildInfo.get(i).split(",");
			if(Cutter[1].equalsIgnoreCase(GuildName))
			{
				player.sendMessage(rManager.Prefix + ChatColor.AQUA + GuildName +" §e길드가§f 지정한 길드 월드 스폰 위치로 가기 위해 시도중입니다...");
				rManagerWarp.PublicWarp(player, GuildName, false);
				return;
			}
			i++;
		}
		player.sendMessage(rManager.Prefix + ChatColor.RED + GuildName +" 길드에 관한 정보가 없습니다!");
		player.sendMessage(rManager.Prefix + ChatColor.RED + "길드가 유효하고 그 길드 리더가 1번이라도 방문하면 정보가 자동 등록");
		player.sendMessage(rManager.Prefix + ChatColor.RED + "됩니다.");
		return;
	}
}
