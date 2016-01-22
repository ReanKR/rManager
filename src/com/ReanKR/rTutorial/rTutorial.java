package com.ReanKR.rTutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class rTutorial extends JavaPlugin implements Listener
{
	public static int ConfigVersion;
	public static boolean RunFirstJoinPlayer;
	public static boolean BlockMovement = true;
	public static boolean BlockAllCommands = true;
	public static boolean CompleteBroadcast = false;
	public static boolean EditComplete;
	public static boolean[] CompatiblePlugins;
	public static List<String> ResultCommands;
	public static List<ItemStack> ResultItems;
	public static Plugin plugin;
	public static String Prefix = "」e[」9r」aT」futorial」e]」f ";
	public static HashMap<String, Boolean> CompleteTutorial;
	public static HashMap<String, String> StatusTutorial;
	public static Map<String, String> MainMessages;
	public static List<String> ErrorReporting;
	public static List<String> LocationMethod;
	public static List<String> MessageMethod;
	public static List<String> TimeMethod;
	public static int MethodAmount;
	public static List<Boolean> UseTitleMessage;
	public static Plugin rTutorialPlugin;
	public rTutorialConfigManager ConfigManager;
	ConsoleCommandSender Server = Bukkit.getConsoleSender();

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Override
	public void onEnable()
	{
		plugin = this;
		rTutorialPlugin = plugin.getServer().getPluginManager().getPlugin("rTutorial");
		rTutorial.CompleteTutorial = new HashMap<String, Boolean>();
		rTutorial.StatusTutorial = new HashMap<String, String>();
		rTutorial.ResultItems = new ArrayList();
		rTutorial.ResultCommands = new ArrayList();
		rTutorial.ErrorReporting = new ArrayList();
		rTutorial.LocationMethod = new ArrayList();
		rTutorial.MessageMethod = new ArrayList();
		rTutorial.TimeMethod = new ArrayList();
		rTutorial.UseTitleMessage = new ArrayList();
		rTutorial.CompatiblePlugins = new boolean[3];
		getServer().getPluginManager().registerEvents(new rTutorialListener(), this);
		rTutorialFileLoader Loader = new rTutorialFileLoader(this);
		this.LoadFile("config");
		this.LoadFile("message");
		Loader.PlayerCfg();
		Loader.LoadCfg();
		Loader.LocationCfg();
		getCommand("rtutorial.main").setExecutor(new rTutorialCommands(this));
		rTutorialMessage.LoadMessage();
		ConfigManager.ScanPlugin();
		ErrorReporter.ResultErrorReport();
		if(rTutorial.CompatiblePlugins[1] == true)
		{
			Server.sendMessage(Prefix + ChatColor.YELLOW + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " now Enabled.");
			Server.sendMessage(Prefix + "」bM」fade 」bb」fy Rean KR,」9 whitehack97@gmail.com");
			Server.sendMessage(Prefix + "」bD」fevoloper 」bW」febsite 」e: 」fhttp://cafe.naver.com/suserver24");
		}
		else
		{
			Server.sendMessage(Prefix + "」cVault Required. rTutorial cannot running.");
			Bukkit.getPluginManager().disablePlugin(rTutorial.rTutorialPlugin);
		}
	}

	@Override
	public void onDisable() 
	{
		Server.sendMessage(Prefix + ChatColor.RED+ plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " now Disabled.");
		Server.sendMessage(Prefix + "」bM」fade 」bb」fy Rean KR,」9 whitehack97@gmail.com");
		Server.sendMessage(Prefix + "」bD」fevoloper 」bW」febsite 」e: 」fhttp://cafe.naver.com/suserver24」f");
	}

	public static YamlConfiguration LoadFile(String Path)
	{
		if(! Path.endsWith(".yml"))
		{
			Path = Path + ".yml";
		}
		File file = new File("plugins/rTutorial/" + Path);
		if(! file.exists())
		{
			try
			{
				plugin.saveResource(Path, true);
				Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + "Create New File " + file.getAbsolutePath());
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage(Prefix + "Cannot save " + Path);
				return null;
			}
		}
		YamlConfiguration Config = YamlConfiguration.loadConfiguration(file);
		return Config;
	}
}
