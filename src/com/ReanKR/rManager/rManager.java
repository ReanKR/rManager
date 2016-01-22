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
	public static String Prefix = "§b[§er§fManager§b] ";
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
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§e해당 서버는 BarAPI를 사용중입니다. 호환 가능합니다.");
		}
		int Version = 1;
		if(Main == null) Main = this;
		if(PF == null) PF = new rManagerPlayerFile();
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§e기본 디렉토리 불러오는 중");
		CreateNewFolder();
		CreateCfg();
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§e버전 불러오는 중");
		rManagerConfig.LoadCfg(Main);
		if(getVersion != Version)
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c콘피그 버전이 현재 버전과 맞지 않습니다.");
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§c정보를 불어올 때 문제가 발생할 수 있습니다!");
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§a콘피그 버전이 정상적으로 호환되었습니다.");
		}
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§b" + pdf.getName() + " version: " + pdf.getVersion() + " 가동됨.");
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§bMade by ReanKR, whitehack97@gmail.com");
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§fCopyright 2016, ReanKR all rights reservered.");
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
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§b플러그인 메인 디렉토리 만드는중");
		oDir.mkdir();
	}

	if(! pDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§b새로운 디렉토리 만드는 중 : /Players");
		pDir.mkdir();
	}
	if(! gDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§b새로운 디렉토리 만드는 중 : /Guilds");
		gDir.mkdir();
		
	}
	if(! wDir.exists())
	{
		Bukkit.getConsoleSender().sendMessage(rManager.Prefix + "§b새로운 디렉토리 만드는 중 : /Warps");
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
			msg(p, rManager.Prefix + "&9리&b엔 &e서버에서 &f유저에게 다양한 기능들을 제공해줍니다.");
			msg(p, rManager.Prefix + " ");
			msg(p, rManager.Prefix + "&6/rm guild &f: &b길드&f와 &e관련한 명령어를 봅니다.");
			msg(p, rManager.Prefix + "&6/rm tp&f: &9텔레포트&f와 &e관련한 명령어를 봅니다.");
			msg(p, rManager.Prefix + "&6/rm warp &f: &a워프&f와 &e관련한 명령어를 봅니다.");
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
					S.sendMessage(rManager.Prefix + "§3rManager reload completed.");
					return true;
					}
					else
					{
						msg(p, rManager.Prefix + "&c리로드를 할 수 있는 권한이 없습니다.");
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
						msg(p, rManager.Prefix + "&f워프를 할 수 있는 권한이 없습니다!");
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("warp"))
				{
					if(args.length < 2 || args[1].equalsIgnoreCase("1"))
					{
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a필수 기입&f, &9[ ] &f: &9선택 기입");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp &6Help Page &31&b/&32");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp [Page]");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp 도움말 페이지");	
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warpto [Warpname]");	
						msg(p, rManager.Prefix + "&f자신이 지정한 워프 이름으로 워프합니다.");
						msg(p, rManager.Prefix + "&f기입하지 않으면 기본 워프로 갑니다.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp set [Warpname]");	
						msg(p, rManager.Prefix + "&f워프 이름으로 워프 위치를 지정합니다.");
						msg(p, rManager.Prefix + "&f기입하지 않으면 기본 워프 위치를 지정합니다.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp del [Warpname]");	
						msg(p, rManager.Prefix + "&f워프 이름으로 워프 위치를 삭제합니다.");
						msg(p, rManager.Prefix + "&f기입하지 않으면 기본 워프 위치를 삭제합니다.");
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("2"))
					{
						msg(p, rManager.Prefix + "&f============ &bC&fustom&1P&elayer&aWarp &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a필수 기입&f, &9[ ] &f: &9선택 기입");
						msg(p, rManager.Prefix + "&bC&fustom&1P&elayer&aWarp &6Help Page &32&b/&32");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&a/rm warp info");	
						msg(p, rManager.Prefix + "&f자신의 워프 정보를 알아봅니다.");
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
							msg(p, rManager.Prefix + "&f개인 워프를 지정할 수 있는 권한이 없습니다!");
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
							msg(p, rManager.Prefix + "&f개인 워프를 지울 수 있는 권한이 없습니다!");
							return false;
						}
					}
				}
				if(args[0].equalsIgnoreCase("guild"))
				{
					if(args.length < 2 || args[1].equalsIgnoreCase("1"))
					{
					msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
					msg(p, rManager.Prefix + "&9< > &f: &a필수 기입&f, &9[ ] &f: &9선택 기입");
					msg(p, rManager.Prefix + "&c/example &f: &b길드장만 사용 가능");
					msg(p, rManager.Prefix + "&6/example &f: &a길드원 모두 사용 가능");
					msg(p, rManager.Prefix + "&4/example &f: &4관리자 또는 오피만 사용 가능");
					msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &31&b/&33");
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild [Page]");	
					msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild 도움말 페이지");	
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild info");	
					msg(p, rManager.Prefix + "&f자신의 길드가 가지고 있는 길드 월드 정보 알아보기");	
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild show &d<list | blacklist>");
					msg(p, rManager.Prefix + "&f길드 월드 권한을 가지고 있는 유저의 명단을  볼 수 있음.");
					msg(p, rManager.Prefix + "");
					msg(p, rManager.Prefix + "&a/rm guild spawn &9[GuildName]");
					msg(p, rManager.Prefix + "&f길드 월드 스폰 지역으로 돌아감. 길드 이름을 기입하면 해당 길드");
					msg(p, rManager.Prefix + "&f월드의 기본 스폰 위치로 갈수 있지만 &c해당 길드장의 허락이 있어야 함");
					msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
					return true;
					}
					if(args[1].equalsIgnoreCase("2"))
					{
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a필수 기입&f, &9[ ] &f: &9선택 기입");
						msg(p, rManager.Prefix + "&c/example &f: &b길드장만 사용 가능");
						msg(p, rManager.Prefix + "&6/example &f: &a길드원 모두 사용 가능");
						msg(p, rManager.Prefix + "&4/example &f: &4관리자 또는 오피만 사용 가능");
						msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &32&b/&33");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild world");
						msg(p, rManager.Prefix + "&f길드 월드로 갑니다. 처음으로 스폰 설정을 할 때 주로 사용합니다.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild bypass");
						msg(p, rManager.Prefix + "&f허가되지 않은 외부인이 길드 월드에 들어갈 수 있습니다.");
						msg(p, rManager.Prefix + "&5(보안성에 주의, 한번 더 입력시 상태 전환");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild user &d<user>");
						msg(p, rManager.Prefix + "&f<user>에게  길드 월드 접근 권한을 줍니다.");	
						msg(p, rManager.Prefix + "&9(길드원 및 길드장은 필요 없음,같은 이름을 또 입력시 접근 권한을 뺏음)");;
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild setspawn");
						msg(p, rManager.Prefix + "&f길드  월드 기본 스폰 지역을 설정합니다.");
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						return true;
					}
					if(args[1].equalsIgnoreCase("3"))
					{
						msg(p, rManager.Prefix + "&f============ &eM&fanage&bY&four&aG&fuild &f============");
						msg(p, rManager.Prefix + "&9< > &f: &a필수 기입&f, &9[ ] &f: &9선택 기입");
						msg(p, rManager.Prefix + "&c/example &f: &b길드장만 사용 가능");
						msg(p, rManager.Prefix + "&6/example &f: &a길드원 모두 사용 가능");
						msg(p, rManager.Prefix + "&4/example &f: &4관리자 또는 오피만 사용 가능");
						msg(p, rManager.Prefix + "&eM&fanage&bY&four&aG&fuild &6Help Page &33&b/&33");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&c/rm guild blacklist &d<user> &9[Reason]");
						msg(p, rManager.Prefix + "&f<user>님을 길드 월드 블랙리스트로 지정합니다.");
						msg(p, rManager.Prefix + "");
						msg(p, rManager.Prefix + "&4/rm guild addworld &d<Leader> <WorldName>");
						msg(p, rManager.Prefix + "&f<Leader>에게 <WorldName>의 이름을 가진 월드의 권한을 줍니다.");
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
								msg(p, rManager.Prefix + "&6소유하고 있는 길드 월드 기본 위치로 이동했습니다.");
								msg(p, rManager.Prefix + "&c만약 월드 스폰 지역이 설정되지 않았다면, &a다른 길드원들이 이용");
								msg(p, rManager.Prefix + "&a할수 있도록 &6바로 설정하는 것을 권장합니다.");
								msg(p, rManager.Prefix + "&6/rm guild setspawn");
								return true;
							}
							i++;
						}
						msg(p, rManager.Prefix + "&c길드장이 아니거나, 길드 월드를 소유하고 있지 않습니다.");
						msg(p, rManager.Prefix + "&c해당 명령어를 사용할 수 없었습니다.");
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
							S.sendMessage(rManager.Prefix + "§elist : 허가된 플레이어 목록");
							S.sendMessage(rManager.Prefix + "§eblacklist : 블랙리스트 목록");
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
							S.sendMessage(rManager.Prefix + "§e추가하고자 하는 플레이어의 이름을 적어주십시오.");
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
							S.sendMessage(rManager.Prefix + "§e추가하고자 하는 플레이어의 이름을 적어주십시오.");
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
							S.sendMessage(rManager.Prefix + "§e길드 리더의 이름을 적어주십시오.");
							return false;
						}
						if(args.length < 4)
						{
							S.sendMessage(rManager.Prefix + "§e소유를 주고자 하는 월드 명을 기입해주십시오.");
							return false;
						}
						String WorldName;
						if(server.getOfflinePlayer(args[2]).hasPlayedBefore() == false)
						{
							S.sendMessage(rManager.Prefix + "§e해당 플레이어는 해당 서버에 없는 유저입니다.");
							S.sendMessage(rManager.Prefix + "§e한번이라도 활동한 플레이어이면서 길드의 리더만 기입할 수 있습니다.");
							return false;
						}
						if(server.getWorld(args[3]) == null)
						{
							S.sendMessage(rManager.Prefix + "§e해당 월드는 서버에 존재하지 않아 등록 할 수 없었습니다.");
							return false;
						}
						WorldName = server.getWorld(args[3]).getName();
						rManagerRegister.RegisterWorld(S, args[2], WorldName);
						return true;
						}
						else
						{
							S.sendMessage(rManager.Prefix + "§c관리자 또는 이에 관련한 사람만 사용 가능합니다.");
							return false;
						}
					}
					S.sendMessage(rManager.Prefix + "§c명령어를 알 수 없습니다! /rm guild");
					return false;
				}
				S.sendMessage(rManager.Prefix + "§c명령어를 알 수 없습니다! /rm");
				return false;
			}
		}
		return false;
	}
}
