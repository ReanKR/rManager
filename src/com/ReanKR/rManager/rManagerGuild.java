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
				player.sendMessage(rManager.Prefix + "��b���ݺ��� �ٸ� ����� ��� ���带 �����Ӱ� �� �� �����ϴ�.");
			}
			else
			{
				Y.set("Bypass-in-the-World", true);
				player.sendMessage(rManager.Prefix + "��f[��4���!��f]��c ���ݺ��� �ٸ� ����� ��� ���带 �����Ӱ� �� �� �ֽ��ϴ�!");
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
			player.sendMessage(rManager.Prefix + "��c��� ���带 �����ϰ� �ִ� ����常 ����� �� �ֽ��ϴ�.");
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
					player.sendMessage(rManager.Prefix + "��e�÷��̾� �̸� ��f:��b " + OtherPlayer);
					player.sendMessage(rManager.Prefix + "��a���� ��f: ��a�㰡�� ��9-> ��c�㰡 �ȵ�");
					player.sendMessage(rManager.Prefix + "��c�ش� �÷��̾��� ��� ���� ���� ������ �ź��Ͽ����ϴ�.");
					found = true;
					break;
				}
				i++;
			}
			if(! found)
			{
				rManager.PlayerAllowPlayers.add(OtherPlayer);
				player.sendMessage(rManager.Prefix + "��e�÷��̾� �̸� ��f:��b " + OtherPlayer);
				player.sendMessage(rManager.Prefix + "��a���� ��f: ��c�㰡 �ȵ� ��9-> ��a�㰡��");
				player.sendMessage(rManager.Prefix + "��b�ش� �÷��̾�� ��� ���� ���� ������ �ο��߽��ϴ�.");
				player.sendMessage(rManager.Prefix + "��f[��b����!��f] ��6�����̳� ������ ���� �߰����� �ʾƵ� �˴ϴ�.");
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
			player.sendMessage(rManager.Prefix + "��c��� ������ ��� ������ ��ɾ��Դϴ�.");
		}
	}

	public static void SetBlacklist(CommandSender Sender, String MisconductUser, String Reason)
	{
		String Message = null;
		if(Reason == null) Message = "���� �̱���";
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
				player.sendMessage(rManager.Prefix + "��c������� ������Ʈ�� �߰��� �� �����ϴ�.");
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
				player.sendMessage(rManager.Prefix + "��e�÷��̾� �̸� ��f:��b " + MisconductUser);
				player.sendMessage(rManager.Prefix + "��c������Ʈ ��ܿ��� �����Ͽ����ϴ�.");
				player.sendMessage(rManager.Prefix + "��f[��b����!��f] ��6������Ʈ�� �����ߴٰ� �ص� ���� �Ǵ� Ư��");
				player.sendMessage(rManager.Prefix + "��6�� �㰡�� ����� ��� ���忡 ������ �� �ֽ��ϴ�.");
				found = true;
				break;
			}
			i++;
		}
		if(! found)
		{
			rManager.PlayerBlacklist.add(MisconductUser + "," + Message);
			player.sendMessage(rManager.Prefix + "��e�÷��̾� �̸� ��f:��b " + MisconductUser);
			player.sendMessage(rManager.Prefix + "��6������Ʈ�� �߰��Ǿ����ϴ�.");
			player.sendMessage(rManager.Prefix + "��f[��b����!��f] ��6������Ʈ�� ������ �߰��� �� �ֽ��ϴ�!");
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
			player.sendMessage(rManager.Prefix + "��c��� ������ ��� ������ ��ɾ��Դϴ�.");
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
				player.sendMessage(rManager.Prefix + "��e�ش� ����� ������ ���� ������ �Ϻ��ϰ� ��ϵ��� �ʾ�");
				player.sendMessage(rManager.Prefix + "��e�㰡�� ���� ����� �� �� �������ϴ�.");
				player.sendMessage(rManager.Prefix + "��e�� ������ ���ӵǸ� ������ �Ǵ� ��� �������� �����Ͻʽÿ�.");
				return;
			}
		
		File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		rManager.PlayerAllowPlayers = Y.getStringList("Allow-Players");
		player.sendMessage(rManager.Prefix + "��f============ ��eMYG ��f============");
		player.sendMessage(rManager.Prefix + "��d���� ��� ������ ������ ������ �㰡�� �÷��̾��");
		player.sendMessage(rManager.Prefix + "��e���� ��f:��b " + GuildName);
		if(rManager.PlayerAllowPlayers.size() == 0)
		{
			player.sendMessage(rManager.Prefix + "");
			player.sendMessage(rManager.Prefix + "��9�÷��̾ ��ϵ��� �ʾҽ��ϴ�.");
			player.sendMessage(rManager.Prefix + "");
		}
		int i = 0;
		while(i != rManager.PlayerAllowPlayers.size())
		{
			player.sendMessage(rManager.Prefix + "��3[��f" + i + "��3]��b " + rManager.PlayerAllowPlayers.get(i));
			i++;
		}
		player.sendMessage(rManager.Prefix + "��6��l��� ���� �� �������� �ڵ����� ����.");
		player.sendMessage(rManager.Prefix + "��f============ ��eMYG ��f============");
		return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "��c��忡 ���Ե� ������ ��� ������ ��ɾ��Դϴ�!");
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
			player.sendMessage(rManager.Prefix + "��e������� ���� ������ �Ҿ�� �� �����ϴ�!");
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
				player.sendMessage(rManager.Prefix + "��e�ش� ����� ������ ���� ������ �Ϻ��ϰ� ��ϵ��� �ʾ�");
				player.sendMessage(rManager.Prefix + "��e�㰡�� ���� ����� �� �� �������ϴ�.");
				player.sendMessage(rManager.Prefix + "��e�� ������ ���ӵǸ� ������ �Ǵ� ��� �������� �����Ͻʽÿ�.");
				return;
			}
			File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
			YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
			rManager.PlayerBlacklist = Y.getStringList("Blacklist");
			int i = 0;
			player.sendMessage(rManager.Prefix + "��f============ ��eMYG ��f============");
			player.sendMessage(rManager.Prefix + "��4���� ��� ���� ������Ʈ ������");
			player.sendMessage(rManager.Prefix + "��e���� ��f:��b " + GuildName);
			if(rManager.PlayerBlacklist.size() == 0)
			{
				player.sendMessage(rManager.Prefix + "");
				player.sendMessage(rManager.Prefix + "��9�ƹ��� ������Ʈ�� �������� �ʾҽ��ϴ�.");
				player.sendMessage(rManager.Prefix + "");
			}
			while(i != rManager.PlayerBlacklist.size())
			{
				String[] Cutter = rManager.PlayerBlacklist.get(i).split(",");
				player.sendMessage(rManager.Prefix + "��3[��f" + i + "��3]��c " + Cutter[0]);
				player.sendMessage(rManager.Prefix + "��c���� ��f:��6 " + Cutter[1]);
				i++;
			}
			player.sendMessage(rManager.Prefix + "��6��l�����̶� ���� �ִٸ�  ������Ʈ ���Ե�.");
			player.sendMessage(rManager.Prefix + "��f============ ��eMYG ��f============");
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "��c��忡 ���Ե� ������ ��� ������ ��ɾ��Դϴ�!");
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
			player.sendMessage(rManager.Prefix + "��c��尡 �־�� ��밡���� ��ɾ��Դϴ�.");
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
		player.sendMessage(rManager.Prefix + "��c��尡 �־�� ��밡���� ��ɾ��Դϴ�.");
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
				player.sendMessage(rManager.Prefix + "��c��� �������� ��� ���带 �����ϰ� ���� �ʽ��ϴ�.");
				player.sendMessage(rManager.Prefix + "��c�ش� ��ɾ�� ��� ���带 ������ �־�� ����� �� �ִ� ��ɾ��Դϴ�.");
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
			player.sendMessage(rManager.Prefix + "��c��� ���� ������ �ش� ��ɾ ������ֽñ� �ٶ��ϴ�.");
			return;
		}
		else
		{
			player.sendMessage(rManager.Prefix + "��c��� ���带 ������ �ִ� ����常 ��밡�� �մϴ�.");
			return;
		}
		
	}
}
