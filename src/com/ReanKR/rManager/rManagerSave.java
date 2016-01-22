package com.ReanKR.rManager;

import org.bukkit.configuration.file.FileConfiguration;

public class rManagerSave
{
	private static rManager plugin;
	private static FileConfiguration cfg = null;

	public static void SaveCfg(rManager Master)
	{
		plugin = Master;
		if(cfg == null) cfg = plugin.getConfig();
		cfg.set("Default-Block-Teleport", rManager.DefaultBlockTeleport);
		cfg.set("Default-Block-World-Teleport", rManager.DefaultBlockWorldTeleport);
		cfg.set("Default-Warp-Amounts", rManager.DefaultWarpAmount);
		cfg.set("Block-Worlds", rManager.BlockWorlds);
		cfg.set("Group-Registered-Amounts", rManager.RegisteredAmount);
		cfg.set("Groups", rManager.GuildInfo);
		plugin.saveConfig();
	}

}
