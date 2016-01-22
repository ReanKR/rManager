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
			Sender.sendMessage(rManager.Prefix + "§c등록 실패");
			Sender.sendMessage(rManager.Prefix + "§b사유 §f:§6해당 플레이어는 아무런 길드 자료가 없습니다.");
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
					Sender.sendMessage(rManager.Prefix + "§a등록된 월드 중 금지된 월드를 찾음. 삭제합니다.");
					rManager.BlockWorlds.remove(i);
					break;
				}
				i++;
			}
			Sender.sendMessage(rManager.Prefix + "§a정상 등록 완료됨");
			Sender.sendMessage(rManager.Prefix + "§f기존에 소유하고 있던 월드는 이제부터 누구나 입장할 수 있습니다.");
			Sender.sendMessage(rManager.Prefix + "§b리더 이름 §f:§e " + PlayerName);
			Sender.sendMessage(rManager.Prefix + "§b리더가 이끄는 길드 §f:§6 " + y.getString("GuildName"));
			Sender.sendMessage(rManager.Prefix + "§b소유하는 월드명 §f:§e " + WorldName);
			Sender.sendMessage(rManager.Prefix + "");
			Sender.sendMessage(rManager.Prefix + "§f해당 길드원들은 별도의 허락없이 해당 월드의 입장이 가능합니다.");
			Sender.sendMessage(rManager.Prefix + "");
			RegisterGuild(PlayerName);
			rManager.BlockWorlds.add(WorldName);
			c.set("Block-Worlds", rManager.BlockWorlds);
			plugin.saveConfig();
			Sender.sendMessage(rManager.Prefix + "§c해당 월드는 이제부터  관계자가 아닌 사람이 마음대로 들어갈 수 없습니다.");
			try
			{
				if(rManager.UsingBarAPI == true)
				{
					if(rManager.Main.getServer().getPlayer(PlayerName).isOnline())
					{
						String Message = "§a월드 §e" + WorldName + " §f관리 권한을 부여받음. §6/rm guild world§f로 길드 월드에 간다음, §d스폰 위치를 먼저 지정하세요!";
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
			Sender.sendMessage(rManager.Prefix + "§c기입하신 플레이어는 리더가 아닙니다. 등록할 수 없습니다.");
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
