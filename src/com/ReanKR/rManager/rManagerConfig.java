package com.ReanKR.rManager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class rManagerConfig extends JavaPlugin
{
	private static rManager plugin;
	private static FileConfiguration cfg = null;
	
	public static void LoadCfg(rManager Master)
	{
		plugin = Master;
		if(cfg == null) cfg = plugin.getConfig();
		rManager.getVersion = cfg.getInt("Config-Version");
		rManager.DefaultBlockTeleport = cfg.getBoolean("Default-Block-Teleport");
		rManager.DefaultBlockWorldTeleport = cfg.getBoolean("Default-Block-World-Teleport");
		rManager.DefaultWarpAmount = cfg.getInt("Default-Warp-Amounts");
		rManager.BlockWorlds = cfg.getStringList("Block-Worlds");
		rManager.RegisteredAmount = cfg.getInt("Group-Registered-Amounts");
		rManager.GuildInfo = cfg.getStringList("Groups");
		rManager.RunCommand = cfg.getString("Run-Command-By-Block");
		rManager.UsingBarAPI = cfg.getBoolean("BarAPI-Enabled");
		if(! Master.getServer().getPluginManager().getPlugin("BarAPI").isEnabled() && rManager.UsingBarAPI == true)
		{
			rManager.UsingBarAPI = false;
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��e�ش� ������ BarAPI�� ������� �����Ƿ� BarAPI ����� �����ϴ�.");
			cfg.set("BarAPI-Enabled", rManager.UsingBarAPI);
			Master.saveConfig();
		}
	}

	public static void CreatePublicWarp()
	{
		File PublicWarp = new File("plugins/rManager/Warps/PublicWarp.yml");
		if(! PublicWarp.exists())
		{
			try {
				Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b���ο� ���� ������: PublicWarp.yml");
				PublicWarp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PublicWarp);
		Y.addDefault("Warps", "");
		try {
			Y.save(PublicWarp);
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��a���� �Ϸ�! PublicWarp.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
