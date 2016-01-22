package com.ReanKR.rManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class rManager extends JavaPlugin implements Listener
{
	public static List<String> PlayerAllowPlayers;
	public static List<String> PlayerBlacklist;
	public static volatile rManager Main = null;
	public static volatile rManagerPlayerFile PF = null;
	public static volatile rManagerRegister MR = null;
	public static String Prefix = "��b[��er��fManager��b] ";
	public static boolean UsingBarAPI = false;
	public PluginDescriptionFile pdf = this.getDescription();
	protected static List<String> GuildInfo;
	protected static String RunCommand;
	protected static List<String> PublicWarp;
	protected static int getVersion;
	protected static boolean DefaultBlockTeleport;
	protected static boolean DefaultBlockWorldTeleport;
	protected static int DefaultWarpAmount;
	protected static List<String> BlockWorlds;
	protected static int RegisteredAmount;
	public static List<String> PrivateWarp;
	public String MainAddress = getDataFolder().getPath();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		if(getServer().getPluginManager().getPlugin("BarAPI").isEnabled())
		{
			UsingBarAPI = true;
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��e�ش� ������ BarAPI�� ������Դϴ�. ȣȯ �����մϴ�.");
		}
		int Version = 1;
		if(Main == null) Main = this;
		if(PF == null) PF = new rManagerPlayerFile();
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��e�⺻ ���丮 �ҷ����� ��");
		CreateNewFolder();
		CreateCfg();
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��e���� �ҷ����� ��");
		rManagerConfig.LoadCfg(Main);
		if(getVersion != Version)
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��c���Ǳ� ������ ���� ������ ���� �ʽ��ϴ�.");
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��c������ �Ҿ�� �� ������ �߻��� �� �ֽ��ϴ�!");
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��a���Ǳ� ������ ���������� ȣȯ�Ǿ����ϴ�.");
		}
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b" + pdf.getName() + " version: " + pdf.getVersion() + " ������.");
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��bMade by ReanKR, whitehack97@gmail.com");
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��fCopyright 2016, ReanKR all rights reservered.");
	}
	
	@Override
	public void onDisable() {
		saveConfig();
	}
	
	public void CreateCfg()
	{
		FileConfiguration c = getConfig();
		c.options().header("ProfileManager v" + getDescription().getVersion() + " Main config file");
		c.addDefault("Config-Version", 1);
		c.addDefault("Default-Block-Teleport", false);
		c.addDefault("Default-Block-World-Teleport", true);
		c.addDefault("Default-Warp-Amounts", 2);
		c.addDefault("Group-Registered-Amounts", 0);
		c.addDefault("Block-Worlds", Arrays.asList(new String[]{"ExampleWorld"}));
		c.addDefault("Groups", "");
		c.addDefault("Run-Command-By-Block", "/example");
		c.addDefault("BarAPI-Enabled", UsingBarAPI == true ? true: false);
		c.options().copyDefaults(true);
		saveConfig();
	}
	
	public void reload()
	{
		saveConfig();
		reloadConfig();
		rManagerConfig.LoadCfg(Main);
    }
	  
	public void CreateNewFolder()
	{
	File oDir = getDataFolder();
	File pDir = new File("plugins/rManager/Players");
	File gDir = new File("plugins/rManager/Guilds");
	File wDir = new File("plugins/rManager/Warps");
	if(! oDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b�÷����� ���� ���丮 �������");
		oDir.mkdir();
	}

	if(! pDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b���ο� ���丮 ����� �� : /Players");
		pDir.mkdir();
	}
	if(! gDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b���ο� ���丮 ����� �� : /Guilds");
		gDir.mkdir();
		
	}
	if(! wDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "��b���ο� ���丮 ����� �� : /Warps");
		wDir.mkdir();
	}
	}
	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent Event)
	{
		rManagerSecurity.InteractJoinToWorld(Event);
	}
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent Event)
	{
		Player player = Event.getPlayer();
		String PlayerName = Event.getPlayer().getName();
		File PlayerFile = new File("Plugins/rManager/Players/" + PlayerName + ".yml");
		if(! PlayerFile.exists())
		{
			PF.JoinNewPlayer(Event);
		}
		else
		{
			rManagerPlayerFile.LoadPlayer(Event.getPlayer());
		}
		rManagerRegister.RegisterGuild(player.getName());
	}
	
	public void msg(CommandSender Sender, String Message)
	{
	Sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender S, Command command, String string, String[] args)
	{
		Server server = Main.getServer();
		
		if(string.equalsIgnoreCase("rm"))
		{
			if(!(S instanceof Player))
			{
				msg(S, rManager.Prefix + "&f============ &er&bManager &f============");
				msg(S, rManager.Prefix + "&er&bManager &9Version &f:&3 " + Main.getDescription().getVersion());
				msg(S, rManager.Prefix + "&eMade by Rean KR, whitehack97@gmail.com");
				msg(S, rManager.Prefix + "&eCopyright 2016, ReanKR all rights reservered.");
				msg(S, rManager.Prefix + "&f============ &6P&erofile&bM&eanager &f============");
				return true;
			}
			Player p = (Player)S;
			if(args.length < 1)
			{
			msg(p, rManager.Prefix + "&f============ &er&bManager &f============");
			msg(p, rManager.Prefix + "&eMade by Rean KR, &9Version &f:&3 " + Main.getDescription().getVersion());
			msg(p, rManager.Prefix + "&9��&b�� &e�������� &f�������� �پ��� ��ɵ��� �������ݴϴ�.");
			msg(p, rManager.Prefix + " ");
			msg(p, rManager.Prefix + "&6/rm guild &f: &b���&f�� &e������ ��ɾ ���ϴ�.");
			msg(p, rManager.Prefix + "&6/rm tp&f: &9�ڷ���Ʈ&f�� &e������ ��ɾ ���ϴ�.");
			msg(p, rManager.Prefix + "&6/rm warp &f: &a����&f�� &e������ ��ɾ ���ϴ�.");
			msg(p, rManager.Prefix + "&6/rm reload &f: &3rManager reload");
			msg(p, rManager.Prefix + " ");
			msg(p, rManager.Prefix + "&8Devoloper Server : [ reankr.com:25565 ]");
			msg(p, rManager.Prefix + "&f============ &er&bManager &f============");
			return true;
			}
			else
			{
				rManagerPlayerFile.LoadPlayer(p);
				if(args[0].equalsIgnoreCase("reload"))
				{
					if(S.isOp())
					{
					rManagerSave.SaveCfg(this);
					reloadConfig();
					S.sendMessage(rManager.Prefix + "��3rManager reload completed.");
					return true;
					}
					else
					{
						msg(p, rManager.Prefix + "&c���ε带 �� �� �ִ� ������ �����ϴ�.");
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("warpto"))
				{
					if(S.hasPermission("customplayerwarp.warp"))
					{
						String WarpName = null;
						if(args.length < 3)
						{
							WarpName = "Default";
						}
						else
						{
							WarpName = args[1];
						}
						rManagerWarp.PlayerWarp(p, WarpName);
						return true;
					}
					else
					{
						msg(p, rManager.Prefix + "&f������ �� �� �ִ� ������ �����ϴ�!");
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("warp"))
				{
					if(args.length < 2 || args[1].equalsIgnoreCase("1"))
					{
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a�ʼ� ����&f, &9[ ] &f: &9���� ����");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp &6Help Page &31&b/&32");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp [Page]");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp ���� ������");	
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warpto [Warpname]");	
						msg(p, rManager.Prefix + "&f�ڽ��� ������ ���� �̸����� �����մϴ�.");
						msg(p, rManager.Prefix + "&f�������� ������ �⺻ ������ ���ϴ�.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp set [Warpname]");	
						msg(p, rManager.Prefix + "&f���� �̸����� ���� ��ġ�� �����մϴ�.");
						msg(p, rManager.Prefix + "&f�������� ������ �⺻ ���� ��ġ�� �����մϴ�.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp del [Warpname]");	
						msg(p, rManager.Prefix + "&f���� �̸����� ���� ��ġ�� �����մϴ�.");
						msg(p, rManager.Prefix + "&f�������� ������ �⺻ ���� ��ġ�� �����մϴ�.");
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("2"))
					{
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a�ʼ� ����&f, &9[ ] &f: &9���� ����");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp &6Help Page &32&b/&32");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp info");	
						msg(p, rManager.Prefix + "&f�ڽ��� ���� ������ �˾ƺ��ϴ�.");
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("info"))
					{
						rManagerWarp.ShowWarpList(p);
						return true;
					}
					if(args[1].equalsIgnoreCase("set"))
					{
						if(S.hasPermission("customplayerwarp.setwarp"))
						{
							String WarpName = null;
							if(args.length < 3)
							{
								WarpName = "Default";
							}
							else
							{
								WarpName = args[2];
							}
							rManagerWarp.PlayerAddWarp(p, WarpName);
							return true;
						}
						else
						{
							msg(p, rManager.Prefix + "&f���� ������ ������ �� �ִ� ������ �����ϴ�!");
							return false;
						}
					}
					
					if(args[1].equalsIgnoreCase("del"))
					{
						if(S.hasPermission("customplayerwarp.delwarp"))
						{
							String WarpName = null;
							if(args.length < 3)
							{
								WarpName = "Default";
							}
							else
							{
								WarpName = args[2];
							}
							rManagerWarp.PlayerDelWarp(p, WarpName);
							return true;
						}
						else
						{
							msg(p, rManager.Prefix + "&f���� ������ ���� �� �ִ� ������ �����ϴ�!");
							return false;
						}
					}
				}
				if(args[0].equalsIgnoreCase("guild"))
				{
					if(args.length < 2 || args[1].equalsIgnoreCase("1"))
					{
					msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
					msg(p, rManager.Prefix + "&9< > &f: &a�ʼ� ����&f, &9[ ] &f: &9���� ����");
					msg(p, rManager.Prefix + "&c/example &f: &b����常 ��� ����");
					msg(p, rManager.Prefix + "&6/example &f: &a���� ��� ��� ����");
					msg(p, rManager.Prefix + "&4/example &f: &4������ �Ǵ� ���Ǹ� ��� ����");
					msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &31&b/&33");
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild [Page]");	
					msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild ���� ������");	
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild info");	
					msg(p, rManager.Prefix + "&f�ڽ��� ��尡 ������ �ִ� ��� ���� ���� �˾ƺ���");	
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild show &d<list | blacklist>");
					msg(p, rManager.Prefix + "&f��� ���� ������ ������ �ִ� ������ �����  �� �� ����.");
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild spawn &9[GuildName]");
					msg(p, rManager.Prefix + "&f��� ���� ���� �������� ���ư�. ��� �̸��� �����ϸ� �ش� ���");
					msg(p, rManager.Prefix + "&f������ �⺻ ���� ��ġ�� ���� ������ &c�ش� ������� ����� �־�� ��");
					msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
					return true;
					}
					if(args[1].equalsIgnoreCase("2"))
					{
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a�ʼ� ����&f, &9[ ] &f: &9���� ����");
						msg(p, rManager.Prefix + "&c/example &f: &b����常 ��� ����");
						msg(p, rManager.Prefix + "&6/example &f: &a���� ��� ��� ����");
						msg(p, rManager.Prefix + "&4/example &f: &4������ �Ǵ� ���Ǹ� ��� ����");
						msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &32&b/&33");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild world");
						msg(p, rManager.Prefix + "&f��� ����� ���ϴ�. ó������ ���� ������ �� �� �ַ� ����մϴ�.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild bypass");
						msg(p, rManager.Prefix + "&f�㰡���� ���� �ܺ����� ��� ���忡 �� �� �ֽ��ϴ�.");
						msg(p, rManager.Prefix + "&5(���ȼ��� ����, �ѹ� �� �Է½� ���� ��ȯ");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild user &d<user>");
						msg(p, rManager.Prefix + "&f<user>����  ��� ���� ���� ������ �ݴϴ�.");	
						msg(p, rManager.Prefix + "&9(���� �� ������� �ʿ� ����,���� �̸��� �� �Է½� ���� ������ ����)");;
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild setspawn");
						msg(p, rManager.Prefix + "&f���  ���� �⺻ ���� ������ �����մϴ�.");
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("3"))
					{
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a�ʼ� ����&f, &9[ ] &f: &9���� ����");
						msg(p, rManager.Prefix + "&c/example &f: &b����常 ��� ����");
						msg(p, rManager.Prefix + "&6/example &f: &a���� ��� ��� ����");
						msg(p, rManager.Prefix + "&4/example &f: &4������ �Ǵ� ���Ǹ� ��� ����");
						msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &33&b/&33");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild blacklist &d<user> &9[Reason]");
						msg(p, rManager.Prefix + "&f<user>���� ��� ���� ������Ʈ�� �����մϴ�.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&4/rm guild addworld &d<Leader> <WorldName>");
						msg(p, rManager.Prefix + "&f<Leader>���� <WorldName>�� �̸��� ���� ������ ������ �ݴϴ�.");
						msg(p, rManager.Prefix + "&f============&eM&fanage&bY&four&aG&fuild &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("world"))
					{
						File ConfigFile = new File("plugins/rManager/config.yml");
						YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
						rManager.GuildInfo = C.getStringList("Groups");
						int i = 0;
						while(i != rManager.GuildInfo.size())
						{
							String[] Cutter = rManager.GuildInfo.get(i).split(",");
							if(Cutter[2].equalsIgnoreCase(p.getName()) && !(Cutter[3].equalsIgnoreCase("NONE") || Cutter[3].equalsIgnoreCase("UNREGISTERED")))
							{
								World world = Main.getServer().getWorld(Cutter[3]);
								Location Loc = new Location(world,0,0,0,(float)358.203,(float)-7.95);
								p.teleport(Loc);
								msg(p, rManager.Prefix + "&6�����ϰ� �ִ� ��� ���� �⺻ ��ġ�� �̵��߽��ϴ�.");
								msg(p, rManager.Prefix + "&c���� ���� ���� ������ �������� �ʾҴٸ�, &a�ٸ� �������� �̿�");
								msg(p, rManager.Prefix + "&a�Ҽ� �ֵ��� &6�ٷ� �����ϴ� ���� �����մϴ�.");
								msg(p, rManager.Prefix + "&6/rm guild setspawn");
								return true;
							}
							i++;
						}
						msg(p, rManager.Prefix + "&c������� �ƴϰų�, ��� ���带 �����ϰ� ���� �ʽ��ϴ�.");
						msg(p, rManager.Prefix + "&c�ش� ��ɾ ����� �� �������ϴ�.");
						return false;
					}
					if(args[1].equalsIgnoreCase("bypass"))
					{
						rManagerGuild.BypassWorld(S);
						return true;
					}
					if(args[1].equalsIgnoreCase("spawn"))
					{
						File PlayerFile = new File("plugins/rManager/Players/" + p.getName() + ".yml");
						YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerFile);
						String GuildName = Y.getString("GuildName");
						if(args.length < 3)
						{
							rManagerWarp.PublicWarp(p, GuildName, false);
							return true;
						}
						else
						{
							rManagerTeleport.TeleportOtherGuild(p, args[2]);
							return true;
						}
					}
					if(args[1].equalsIgnoreCase("setspawn"))
					{
						 rManagerGuild.SetDefaultRespawn(p);
						 return true;
					}
					
					if(args[1].equalsIgnoreCase("show"))
					{
						if(args.length < 3)
						{
							S.sendMessage(rManager.Prefix + "��elist : �㰡�� �÷��̾� ���");
							S.sendMessage(rManager.Prefix + "��eblacklist : ������Ʈ ���");
							return false;
						}
						if(args[2].equalsIgnoreCase("list"))
						{
							rManagerGuild.AllowUserlist(S);
							return true;
						}
						if((args[2].equalsIgnoreCase("blacklist")))
						{
							rManagerGuild.ShowBlackList(S);
							return true;
						}
					}
					if(args[1].equalsIgnoreCase("blacklist"))
					{
						if(args.length < 3)
						{
							S.sendMessage(rManager.Prefix + "��e�߰��ϰ��� �ϴ� �÷��̾��� �̸��� �����ֽʽÿ�.");
							return false;
						}
				        String message = null;
				        for (String part : args)
				        {
				          if (args[0].equals(part) || args[1].equals(part) || args[2].equals(part))
				          {
				            continue;
				          }
				          if (message == null) message = part;
				          else
				          {
				        	  message = message + " " + part;
				          }
				        }
						rManagerGuild.SetBlacklist(S, args[2], message);
						return true;
					}

					if(args[1].equalsIgnoreCase("user"))
					{
						if(args.length < 3)
						{
							S.sendMessage(rManager.Prefix + "��e�߰��ϰ��� �ϴ� �÷��̾��� �̸��� �����ֽʽÿ�.");
							return false;
						}
						rManagerGuild.AllowUser(S, args[2]);
						return true;
					}

					if(args[1].equalsIgnoreCase("addworld"))
					{
						if(S.isOp())
						{
						if(args.length < 3)
						{
							S.sendMessage(rManager.Prefix + "��e��� ������ �̸��� �����ֽʽÿ�.");
							return false;
						}
						if(args.length < 4)
						{
							S.sendMessage(rManager.Prefix + "��e������ �ְ��� �ϴ� ���� ���� �������ֽʽÿ�.");
							return false;
						}
						String WorldName;
						if(server.getOfflinePlayer(args[2]).hasPlayedBefore() == false)
						{
							S.sendMessage(rManager.Prefix + "��e�ش� �÷��̾�� �ش� ������ ���� �����Դϴ�.");
							S.sendMessage(rManager.Prefix + "��e�ѹ��̶� Ȱ���� �÷��̾��̸鼭 ����� ������ ������ �� �ֽ��ϴ�.");
							return false;
						}
						if(server.getWorld(args[3]) == null)
						{
							S.sendMessage(rManager.Prefix + "��e�ش� ����� ������ �������� �ʾ� ��� �� �� �������ϴ�.");
							return false;
						}
						WorldName = server.getWorld(args[3]).getName();
						rManagerRegister.RegisterWorld(S, args[2], WorldName);
						return true;
						}
						else
						{
							S.sendMessage(rManager.Prefix + "��c������ �Ǵ� �̿� ������ ����� ��� �����մϴ�.");
							return false;
						}
					}
					S.sendMessage(rManager.Prefix + "��c��ɾ �� �� �����ϴ�! /rm guild");
					return false;
				}
				S.sendMessage(rManager.Prefix + "��c��ɾ �� �� �����ϴ�! /rm");
				return false;
			}
		}
		return false;
	}
}
