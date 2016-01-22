package com.ReanKR.rManager;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class rManagerGuild
{
	public static void BypassWorld(CommandSender s)
	{
		Player player = (Player) s;
		File LeaderFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		String GuildLevel = Y.getString("GuildLevel");
		String hasWorld = Y.getString("hasWorldName");
		if(GuildLevel.equalsIgnoreCase("LEADER") && !(hasWorld.equalsIgnoreCase("NONE")))
		{
			String ChackBypass = null;
			ChackBypass = Y.getString("Bypass-in-the-World");
			try
			{
				if(ChackBypass.equalsIgnoreCase(null))
				{
					Y.addDefault("Bypass-in-the-World", false);
					try
					{
						Y.save(LeaderFile);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch(NullPointerException e)
			{
				Y.addDefault("Bypass-in-the-World", false);
				try
				{
					Y.save(LeaderFile);
				}
				catch (IOException E)
				{
					E.printStackTrace();
				}
			}
			boolean ByPass = Y.getBoolean("Bypass-in-the-World");
			if(ByPass)
			{
				Y.set("Bypass-in-the-World", false);
				player.sendMessage(rManager.Prefix + "§b지금부터 다른 사람이 길드 월드를 자유롭게 들어갈 수 없습니다.");
			}
			else
			{
				Y.set("Bypass-in-the-World", true);
				player.sendMessage(rManager.Prefix + "§f[§4경고!§f]§c 지금부터 다른 사람이 길드 월드를 자유롭게 들어갈 수 있습니다!");
			}
			try {
				Y.save(LeaderFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드 월드를 소지하고 있는 길드장만 사용할 수 있습니다.");
			return;
		}
	}

	public static void AllowUser(CommandSender s, String OtherPlayer)
	{
		Player player = (Player) s;
		File LeaderFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		boolean found = false;
		if(Y.getString("GuildLevel").equalsIgnoreCase("LEADER"))
		{
			int i = 0;
			rManager.PlayerAllowPlayers = Y.getStringList("Allow-Players");
			while(i != rManager.PlayerAllowPlayers.size())
			{
				if(rManager.PlayerAllowPlayers.get(i).equalsIgnoreCase(OtherPlayer))
				{
					rManager.PlayerAllowPlayers.remove(i);
					player.sendMessage(rManager.Prefix + "§e플레이어 이름 §f:§b " + OtherPlayer);
					player.sendMessage(rManager.Prefix + "§a상태 §f: §a허가됨 §9-> §c허가 안됨");
					player.sendMessage(rManager.Prefix + "§c해당 플레이어의 길드 월드 접근 권한을 거부하였습니다.");
					found = true;
					break;
				}
				i++;
			}
			if(! found)
			{
				rManager.PlayerAllowPlayers.add(OtherPlayer);
				player.sendMessage(rManager.Prefix + "§e플레이어 이름 §f:§b " + OtherPlayer);
				player.sendMessage(rManager.Prefix + "§a상태 §f: §c허가 안됨 §9-> §a허가됨");
				player.sendMessage(rManager.Prefix + "§b해당 플레이어에게 길드 월드 접근 권한을 부여했습니다.");
				player.sendMessage(rManager.Prefix + "§f[§b유의!§f] §6길드원이나 리더는 굳이 추가하지 않아도 됩니다.");
			}
			Y.set("Allow-Players", rManager.PlayerAllowPlayers);
			try
			{
				Y.save(LeaderFile);
			}
			catch(Exception e)
			{
				
			}
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드 리더만 사용 가능한 명령어입니다.");
		}
	}

	public static void SetBlacklist(CommandSender Sender, String MisconductUser, String Reason)
	{
		String Message = null;
		if(Reason == null) Message = "사유 미기재";
		else Message = Reason;
		Message = (ChatColor.translateAlternateColorCodes('&', Message));
		Player player = (Player) Sender;
		File LeaderFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		rManager.PlayerBlacklist = Y.getStringList("Blacklist");
		if(Y.getString("GuildLevel").equalsIgnoreCase("LEADER"))
		{
			if(MisconductUser.equalsIgnoreCase(player.getName()))
			{
				player.sendMessage(rManager.Prefix + "§c길드장을 블랙리스트로 추가할 수 없습니다.");
				return;
			}
		boolean found = false;
		int i = 0;
		while(i != rManager.PlayerBlacklist.size())
		{
			String[] Cutter = rManager.PlayerBlacklist.get(i).split(",");
			if(Cutter[0].equalsIgnoreCase(MisconductUser))
			{
				rManager.PlayerBlacklist.remove(i);
				player.sendMessage(rManager.Prefix + "§e플레이어 이름 §f:§b " + MisconductUser);
				player.sendMessage(rManager.Prefix + "§c블랙리스트 명단에서 삭제하였습니다.");
				player.sendMessage(rManager.Prefix + "§f[§b유의!§f] §6블랙리스트를 헤제했다고 해도 길드원 또는 특별");
				player.sendMessage(rManager.Prefix + "§6히 허가한 사람만 길드 월드에 접근할 수 있습니다.");
				found = true;
				break;
			}
			i++;
		}
		if(! found)
		{
			rManager.PlayerBlacklist.add(MisconductUser + "," + Message);
			player.sendMessage(rManager.Prefix + "§e플레이어 이름 §f:§b " + MisconductUser);
			player.sendMessage(rManager.Prefix + "§6블랙리스트로 추가되었습니다.");
			player.sendMessage(rManager.Prefix + "§f[§b유의!§f] §6블랙리스트는 길드원도 추가할 수 있습니다!");
		}
		Y.set("Blacklist", rManager.PlayerBlacklist);

		try
		{
			Y.save(LeaderFile);
		}
		
		catch(Exception e)
		{
			
		}
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드 리더만 사용 가능한 명령어입니다.");
			return;
		}

	}

	public static void AllowUserlist(CommandSender Sender)
	{
		Player player = (Player) Sender;
		File ConfigFile = new File("plugins/rManager/config.yml");
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
		YamlConfiguration P = YamlConfiguration.loadConfiguration(PlayerFile);
		rManager.GuildInfo = C.getStringList("Groups");
		String GuildName = P.getString("GuildName");
		String LeaderName = "NONE";
		if(!(GuildName.equalsIgnoreCase("NONE")) || !(GuildName.equalsIgnoreCase("LOAD_FAILED")))
		{
			int j = 0;
			while(j != rManager.GuildInfo.size())
			{
				String[] Cutter = rManager.GuildInfo.get(j).split(",");
				if(Cutter[1].equalsIgnoreCase(GuildName))
				{
					LeaderName = Cutter[2];
					break;
				}
				j++;
			}
			if(LeaderName.equalsIgnoreCase("NONE") || LeaderName.equalsIgnoreCase("UNREGISTERED"))
			{
				player.sendMessage(rManager.Prefix + "§e해당 길드의 리더가 아직 정보를 완벽하게 등록되지 않아");
				player.sendMessage(rManager.Prefix + "§e허가된 유저 목록을 볼 수 없었습니다.");
				player.sendMessage(rManager.Prefix + "§e이 문제가 지속되면 관리자 또는 길드 리더에게 문의하십시오.");
				return;
			}
		
		File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		rManager.PlayerAllowPlayers = Y.getStringList("Allow-Players");
		player.sendMessage(rManager.Prefix + "§f============ §eMYG §f============");
		player.sendMessage(rManager.Prefix + "§d현재 길드 월드의 접근이 별도로 허가된 플레이어들");
		player.sendMessage(rManager.Prefix + "§e길드명 §f:§b " + GuildName);
		if(rManager.PlayerAllowPlayers.size() == 0)
		{
			player.sendMessage(rManager.Prefix + "");
			player.sendMessage(rManager.Prefix + "§9플레이어가 등록되지 않았습니다.");
			player.sendMessage(rManager.Prefix + "");
		}
		int i = 0;
		while(i != rManager.PlayerAllowPlayers.size())
		{
			player.sendMessage(rManager.Prefix + "§3[§f" + i + "§3]§b " + rManager.PlayerAllowPlayers.get(i));
			i++;
		}
		player.sendMessage(rManager.Prefix + "§6§l길드 리더 및 길드원들은 자동으로 허용됨.");
		player.sendMessage(rManager.Prefix + "§f============ §eMYG §f============");
		return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드에 가입된 유저만 사용 가능한 명령어입니다!");
			return;
		}
	}
	public static void ShowBlackList(CommandSender Sender)
	{
		Player player = (Player) Sender;
		File ConfigFile = new File("plugins/rManager/config.yml");
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		if(! PlayerFile.exists())
		{
			player.sendMessage(rManager.Prefix + "§e길드장의 파일 정보를 불어올 수 없습니다!");
			return;
		}
		YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
		YamlConfiguration P = YamlConfiguration.loadConfiguration(PlayerFile);
		rManager.GuildInfo = C.getStringList("Groups");
		String GuildName = P.getString("GuildName");
		String LeaderName = "NONE";
		if(!(GuildName.equalsIgnoreCase("NONE")) || !(GuildName.equalsIgnoreCase("LOAD_FAILED")))
		{
			int j = 0;
			while(j != rManager.GuildInfo.size())
			{
				String[] Cutter = rManager.GuildInfo.get(j).split(",");
				if(Cutter[1].equalsIgnoreCase(GuildName))
				{
					LeaderName = Cutter[2].toString();
					break;
				}
				j++;
			}
			if(LeaderName.equalsIgnoreCase("NONE") || LeaderName.equalsIgnoreCase("UNREGISTERED"))
			{
				player.sendMessage(rManager.Prefix + "§e해당 길드의 리더가 아직 정보를 완벽하게 등록되지 않아");
				player.sendMessage(rManager.Prefix + "§e허가된 유저 목록을 볼 수 없었습니다.");
				player.sendMessage(rManager.Prefix + "§e이 문제가 지속되면 관리자 또는 길드 리더에게 문의하십시오.");
				return;
			}
			File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
			YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
			rManager.PlayerBlacklist = Y.getStringList("Blacklist");
			int i = 0;
			player.sendMessage(rManager.Prefix + "§f============ §eMYG §f============");
			player.sendMessage(rManager.Prefix + "§4현재 길드 월드 블랙리스트 유저들");
			player.sendMessage(rManager.Prefix + "§e길드명 §f:§b " + GuildName);
			if(rManager.PlayerBlacklist.size() == 0)
			{
				player.sendMessage(rManager.Prefix + "");
				player.sendMessage(rManager.Prefix + "§9아무도 블랙리스트로 지정되지 않았습니다.");
				player.sendMessage(rManager.Prefix + "");
			}
			while(i != rManager.PlayerBlacklist.size())
			{
				String[] Cutter = rManager.PlayerBlacklist.get(i).split(",");
				player.sendMessage(rManager.Prefix + "§3[§f" + i + "§3]§c " + Cutter[0]);
				player.sendMessage(rManager.Prefix + "§c사유 §f:§6 " + Cutter[1]);
				i++;
			}
			player.sendMessage(rManager.Prefix + "§6§l길드원이라도 여기 있다면  블랙리스트 포함됨.");
			player.sendMessage(rManager.Prefix + "§f============ §eMYG §f============");
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드에 가입된 유저만 사용 가능한 명령어입니다!");
			return;
		}
	}

	public static void GotoDefaultRespawn(CommandSender Sender)
	{
		Player player = (Player)Sender;
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerFile);
		String GuildName = Y.getString("GuildName");
		if(GuildName.equalsIgnoreCase("NONE") || GuildName.equalsIgnoreCase("LOAD_FAILED"))
		{
			player.sendMessage(rManager.Prefix + "§c길드가 있어야 사용가능한 명령어입니다.");
			return;
		}
		int i = 0;
		while(i != rManager.GuildInfo.size())
		{
			String[] Cutter = rManager.GuildInfo.get(i).split(",");
			if(Cutter[1].equalsIgnoreCase(Y.getString("GuildName")) && !(Cutter[3].equalsIgnoreCase("NONE") || Cutter[3].equalsIgnoreCase("UNREGISTERED")))
			{
				rManagerWarp.PublicWarp(player, GuildName, false);
				return;
			}
			i++;
		}
		player.sendMessage(rManager.Prefix + "§c길드가 있어야 사용가능한 명령어입니다.");
		return;
	}

	public static void SetDefaultRespawn(Player player)
	{
		File ConfigFile = new File("plugins/rManager/config.yml");
		File LeaderFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
		String hasWorldName = "NONE";
		if(Y.getString("GuildLevel").equalsIgnoreCase("LEADER"))
		{
			String GuildName = Y.getString("GuildName");
			hasWorldName = Y.getString("hasWorldName");
			if(hasWorldName.equalsIgnoreCase("NONE") || hasWorldName.equalsIgnoreCase("LOAD_FAILED"))
			{
				player.sendMessage(rManager.Prefix + "§c길드 리더님은 길드 월드를 소유하고 있지 않습니다.");
				player.sendMessage(rManager.Prefix + "§c해당 명령어는 길드 월드를 가지고 있어야 사용할 수 있는 명령어입니다.");
				return;
			}
			Location Loc = player.getLocation();
			String WorldName = Loc.getWorld().getName();
			rManager.GuildInfo = C.getStringList("Groups");
			int i = 0;
			while(i != rManager.GuildInfo.size())
			{
				String[] Cutter = rManager.GuildInfo.get(i).split(",");
				if(Cutter[3].equalsIgnoreCase(WorldName) && GuildName.equalsIgnoreCase(Cutter[1]))
				{
					rManagerWarp.PublicWarp(player, Cutter[1], true);
					return;
				}
				i++;
			}
			player.sendMessage(rManager.Prefix + "§c길드 월드 내에서 해당 명령어를 사용해주시기 바랍니다.");
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c길드 월드를 가지고 있는 길드장만 사용가능 합니다.");
			return;
		}
		
	}
}
