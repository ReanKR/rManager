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
				player.sendMessage(rManager.Prefix + ChatColor.AQUA + GuildName +" ��e��尡��f ������ ��� ���� ���� ��ġ�� ���� ���� �õ����Դϴ�...");
				rManagerWarp.PublicWarp(player, GuildName, false);
				return;
			}
			i++;
		}
		player.sendMessage(rManager.Prefix + ChatColor.RED + GuildName +" ��忡 ���� ������ �����ϴ�!");
		player.sendMessage(rManager.Prefix + ChatColor.RED + "��尡 ��ȿ�ϰ� �� ��� ������ 1���̶� �湮�ϸ� ������ �ڵ� ���");
		player.sendMessage(rManager.Prefix + ChatColor.RED + "�˴ϴ�.");
		return;
	}
}
