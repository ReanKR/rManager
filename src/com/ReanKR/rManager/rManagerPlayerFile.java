package com.ReanKR.rManager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class rManagerPlayerFile
{
	@EventHandler
	public void JoinNewPlayer(PlayerJoinEvent event)
	{
		String[] PlayerInfo = new String[2];
		String GuildName = null;
		Player player = event.getPlayer();
		String PlayerName = event.getPlayer().getName();
		File PlayerFile = new File("plugins/rManager/Players/" + PlayerName + ".yml");
		if(! PlayerFile.exists())
		{
			try
			{
				PlayerFile.createNewFile();
			}
			catch(Exception e)
			{	
				Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c플레이어 파일 생성이 안됨. " + PlayerName + ".yml");
				e.printStackTrace();
				return;
			}
		}
		if(! PlayerFile.exists())
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c플레이어 파일 저장 실패! " + PlayerName + ".yml");
			return;
		}
		PlayerInfo = rManagerLoadFile.LoadInfoGuild(player);
		GuildName = rManagerLoadFile.LoadGuildName(PlayerInfo[0]);
		YamlConfiguration y = YamlConfiguration.loadConfiguration(PlayerFile);
		y.addDefault("UUID", player.getUniqueId().toString());
		y.addDefault("BlockTeleport", rManager.DefaultBlockTeleport);
		y.addDefault("BlockWorldTeleport", rManager.DefaultBlockWorldTeleport);
		y.addDefault("Set-Warp-Left-Amount", rManager.DefaultWarpAmount);
		y.addDefault("GuildUUID", PlayerInfo[0].toString());
		y.addDefault("GuildName", GuildName);
		y.addDefault("GuildLevel", PlayerInfo[1].toString());
		y.addDefault("hasWorldName", "NONE");
		y.addDefault("Allow-Players", "");
		y.addDefault("Blacklist", "");
		y.options().copyDefaults(true);
		try
		{
			y.save(PlayerFile);
		}
		catch(Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c플레이어 파일 저장 실패! " + PlayerName + ".yml");
		}
	}

	public static void LoadPlayer(Player player)
	{
		String[] PlayerInfo = new String[2];
		String GuildName = null;
		String PlayerName = player.getName();
		File PlayerFile = new File("plugins/rManager/Players/" + PlayerName + ".yml");
		PlayerInfo = rManagerLoadFile.LoadInfoGuild(player);
		GuildName = rManagerLoadFile.LoadGuildName(PlayerInfo[0]);
		YamlConfiguration y = YamlConfiguration.loadConfiguration(PlayerFile);
		y.set("GuildUUID", PlayerInfo[0].toString());
		y.set("GuildName", GuildName);
		y.set("GuildLevel", PlayerInfo[1].toString());
		try
		{
			y.save(PlayerFile);
		}
		catch(Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c플레이어 파일 저장 실패! " + PlayerName + ".yml");
		}
	}
}
