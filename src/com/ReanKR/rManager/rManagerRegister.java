package com.ReanKR.rManager;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.confuser.barapi.BarAPI;

public class rManagerRegister
{
	static rManager plugin = rManager.Main;
	public static void RegisterWorld(CommandSender Sender, String PlayerName, String WorldName)
	{
		FileConfiguration c = plugin.getConfig();
		File file = new File("plugins/rManager/Players/" + PlayerName + ".yml");
		if(! file.exists())
		{
			Sender.sendMessage(rManager.Prefix + "��c��� ����");
			Sender.sendMessage(rManager.Prefix + "��b���� ��f:��6�ش� �÷��̾�� �ƹ��� ��� �ڷᰡ �����ϴ�.");
			return;
		}
		YamlConfiguration y = YamlConfiguration.loadConfiguration(file);
		String GuildLevel = y.getString("GuildLevel");
		String BeforehasWorldName = y.getString("hasWorldName");
		if(GuildLevel.equalsIgnoreCase("LEADER") && GuildLevel !=  null)
		{
			y.set("hasWorldName", WorldName);
			try
			{
			y.save(file);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			int i = 0;
			while(i != rManager.BlockWorlds.size())
			{
				if(BeforehasWorldName.equalsIgnoreCase(rManager.BlockWorlds.get(i)))
				{
					Sender.sendMessage(rManager.Prefix + "��a��ϵ� ���� �� ������ ���带 ã��. �����մϴ�.");
					rManager.BlockWorlds.remove(i);
					break;
				}
				i++;
			}
			Sender.sendMessage(rManager.Prefix + "��a���� ��� �Ϸ��");
			Sender.sendMessage(rManager.Prefix + "��f������ �����ϰ� �ִ� ����� �������� ������ ������ �� �ֽ��ϴ�.");
			Sender.sendMessage(rManager.Prefix + "��b���� �̸� ��f:��e " + PlayerName);
			Sender.sendMessage(rManager.Prefix + "��b������ �̲��� ��� ��f:��6 " + y.getString("GuildName"));
			Sender.sendMessage(rManager.Prefix + "��b�����ϴ� ����� ��f:��e " + WorldName);
			Sender.sendMessage(rManager.Prefix + "");
			Sender.sendMessage(rManager.Prefix + "��f�ش� �������� ������ ������� �ش� ������ ������ �����մϴ�.");
			Sender.sendMessage(rManager.Prefix + "");
			RegisterGuild(PlayerName);
			rManager.BlockWorlds.add(WorldName);
			c.set("Block-Worlds", rManager.BlockWorlds);
			plugin.saveConfig();
			Sender.sendMessage(rManager.Prefix + "��c�ش� ����� ��������  �����ڰ� �ƴ� ����� ������� �� �� �����ϴ�.");
			try
			{
				if(rManager.UsingBarAPI == true)
				{
					if(rManager.Main.getServer().getPlayer(PlayerName).isOnline())
					{
						String Message = "��a���� ��e" + WorldName + " ��f���� ������ �ο�����. ��6/rm guild world��f�� ��� ���忡 ������, ��d���� ��ġ�� ���� �����ϼ���!";
						BarAPI.setMessage(rManager.Main.getServer().getPlayer(PlayerName), Message, 15);
					}
				}
			}
			catch(NullPointerException e)
			{
				
			}
			return;
		}
		else
		{
			Sender.sendMessage(rManager.Prefix + "��c�����Ͻ� �÷��̾�� ������ �ƴմϴ�. ����� �� �����ϴ�.");
			return;
		}
	}

	public static void RegisterGuild(String PlayerName)
	{
		File file = new File("plugins/rManager/Players/" + PlayerName + ".yml");
		YamlConfiguration y = YamlConfiguration.loadConfiguration(file);
		String GuildName = y.getString("GuildName");
		FileConfiguration c = plugin.getConfig();
		String GuildLevel = y.getString("GuildLevel");
		String hasWorldName = y.getString("hasWorldName");
		rManager.GuildInfo = c.getStringList("Groups");
		if(GuildLevel.equalsIgnoreCase("LEADER"))
		{
			int i = 0;
			String Line = null;
			boolean found = false;
			while(i != rManager.GuildInfo.size())
			{
				Line = rManager.GuildInfo.get(i).toString();
				String[] Cutter = Line.split(",");
				if(Cutter[1].equalsIgnoreCase(GuildName))
				{
					found = true;
					if((Cutter[3].equalsIgnoreCase("NONE") || Cutter[3].equalsIgnoreCase("UNREGISTERED")) && (!(Cutter[3].equalsIgnoreCase(hasWorldName)) && (!(hasWorldName.equalsIgnoreCase("NONE")))))
					{
						rManager.GuildInfo.set(i, rManager.RegisteredAmount + "," + GuildName + "," + PlayerName + "," + hasWorldName);
					}
					
					if(! Cutter[2].equalsIgnoreCase(PlayerName))
					{
						rManager.GuildInfo.set(i, rManager.RegisteredAmount + "," + GuildName + "," + PlayerName + "," + hasWorldName);
						break;
					}
				}
				i++;
			}
			if(! found)
			{
			rManager.RegisteredAmount++;
			rManager.GuildInfo.add(rManager.RegisteredAmount + "," + GuildName + "," + PlayerName + "," + hasWorldName);
			}
		}
		else
		{
			int i = 0;
			String Line = null;
			boolean found = false;
			while(i != rManager.GuildInfo.size())
			{
				Line = rManager.GuildInfo.get(i).toString();
				String[] Cutter = Line.split(",");
				if(Cutter[1].equalsIgnoreCase(GuildName))
				{
					found = true;
					break;
				}
				i++;
			}
			if(!(found))
			{
				if(!(GuildName.equalsIgnoreCase("NONE") || GuildName.equalsIgnoreCase("LOAD_FAILED")))
				{
				rManager.RegisteredAmount++;
				rManager.GuildInfo.add(rManager.RegisteredAmount + "," + GuildName + ",UNREGISTERED" + ",UNREGISTERED");
				}
			}
		}
		c.set("Group-Registered-Amounts", rManager.RegisteredAmount);
		c.set("Groups", rManager.GuildInfo);
		plugin.saveConfig();
	}
}
