package com.ReanKR.rManager;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class rManagerSecurity extends JavaPlugin
{
	public static void InteractJoinToWorld(PlayerTeleportEvent Event) //금지 월드로 들어가는 거 방지
	{
		rManagerPlayerFile.LoadPlayer(Event.getPlayer());
		Location getFrom = Event.getFrom();
		Location getTo = Event.getTo();
		Player player = Event.getPlayer();
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		File ConfigFile = new File("plugins/rManager/config.yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerFile);
		YamlConfiguration C = YamlConfiguration.loadConfiguration(ConfigFile);
		String GuildName = Y.getString("GuildName");
		boolean found = false;
		boolean AllowTeleport = false;
		boolean SameGuild = false;
		int i = 0;
		rManager.BlockWorlds = C.getStringList("Block-Worlds");
		// 만약에 월드가 바뀌었다면
		if(! getFrom.getWorld().getName().equalsIgnoreCase(getTo.getWorld().getName()))
		{
			// 현재 이동한 월드가 금지된 월드인지 우선 확인한다.
			while(i != rManager.BlockWorlds.size())
			{
				if(getTo.getWorld().getName().equalsIgnoreCase(rManager.BlockWorlds.get(i)))
				{
					found = true;
					break;
				}
				i++;
			}
			// 만약 해당 월드가 금지 리스트에 오른 월드였다면
			if(found)
			{
				rManager.GuildInfo = C.getStringList("Groups");
				// 길드가 있는지 확인해야 하는데 만약 길드가 없다면
				if(GuildName.equalsIgnoreCase("NONE") || GuildName.equalsIgnoreCase("LOAD_FAILED"))
				{
					int j = 0;
					// 우선 그 월드의 소유 길드가 어떤 길드인지 부터 확인한다.
					while(j != rManager.GuildInfo.size())
					{
						String[] Cutter = rManager.GuildInfo.get(j).split(",");
						// 만약 어떤 길드가 소유하고 있는 월드였다면
						if(Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
						{
							// 그 월드를 가지고 있는 길드 리더의 정보를 인식하여 해당 플레이어가 허락되었는지 학인한다.
							AllowTeleport = ChackAllowByLeader(player, Cutter[2], false);
							break;
						}
						j++;
					}
					if(AllowTeleport == false)// 만약에 그 월드의 소유가 없었다면
					{
						if(player.hasPermission("rmanager.teleport.bypass"))
						{
							return;
						}
						Event.setCancelled(true);
						player.sendMessage(rManager.Prefix + "§c해당 월드로 이동할 수 없었습니다.");
						player.sendMessage(rManager.Prefix + "§b사유 §f: §6해당 월드는 길드 관계자 허가가 있어야 합니다.");
						return;
					}
				}
				else // 만약에 길드가 있는 사람이라면
				{
					rManager.GuildInfo = C.getStringList("Groups");
					int j = 0;
					// 우선 그 월드의 소유 길드가 어떤 길드인지 부터 확인한다.
					while(j != rManager.GuildInfo.size())
					{
						String[] Cutter = rManager.GuildInfo.get(j).split(",");
						// 만약 해당 플레이어 길드 소속이고 가려는 월드가 길드가 가지고 있는 월드라면
						if(Cutter[1].equalsIgnoreCase(GuildName) && Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
						{
							if(Cutter[2].equalsIgnoreCase(player.getName()) && Cutter[3].equalsIgnoreCase(getTo.getWorld().getName())) // 만약 해당 월드의 리더장이였다면
							{
								AllowTeleport = true;
								break;
							}
							// 아니라면 1차는 일단  통과, 그리고 리더가 해당 유저를 블랙리스트로 등록했는지 확인한다.
							SameGuild = true;
							AllowTeleport = ChackAllowByLeader(player, Cutter[2], true);
							break;
						}
						
						// 만약 해당 플레이어 해당 길드 소속이 아니지만 가려는 월드가 어떤 길드가 가지고 있는 월드였다면 
						if(!(Cutter[1].equalsIgnoreCase(GuildName)))
						{
							if(Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
							{
							// 길드에는 가입되어 있지만 같은 길드 소속이 아니므로 외부인으로 간주함.
								AllowTeleport = ChackAllowByLeader(player, Cutter[2], false);
								break;
							}
						}
						j++;
					}
					if(! AllowTeleport)// 만약에 리더가 허가하지 않았다면
					{
						if(player.hasPermission("rmanager.teleport.bypass"))
						{
							return;
						}
						if(SameGuild) //블랙 리스트로 지정했다면
						{
							Event.setCancelled(true);
							player.sendMessage(rManager.Prefix + "§c해당 월드로 이동할 수 없었습니다.");
							player.sendMessage(rManager.Prefix + "§b사유  §f: §c길드장이 유저님을 블랙리스트로 등록");
							return;
						}
						else
						{
							Event.setCancelled(true);
							player.sendMessage(rManager.Prefix + "§c해당 월드로 이동할 수 없었습니다.");
							player.sendMessage(rManager.Prefix + "§b사유 §f: §6외부인이라면 해당 월드는 길드 관계자 허가가 있어야 합니다.");
							return;
						}
					}
				}
			}
		}
	}

	public static boolean ChackAllowByLeader(Player player, String LeaderName, boolean JoinedGuild) //리더가 허락을 하였는가?
	{
		File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
		if(! LeaderFile.exists())
		{
			player.sendMessage(rManager.Prefix + "Unexpected Error. The leader database who has the world does not exist.");
			player.sendMessage(rManager.Prefix + "§b예상치 못한 오류가 나왔습니다. 해당 월드를 소유한 길드장의 데이터를 읽어올 수 없었습니다.");
			return false;
		}
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		boolean byPass = Y.getBoolean("Bypass-in-the-World");
		boolean Found = false; // 처음에는 못찾았다고 가정을 한다
		if(JoinedGuild) //같은 길드에 가입되어 있는 사람이 검사를 요구한다면
		{
			//블랙 리스트 검사
			rManager.PlayerBlacklist = Y.getStringList("Blacklist");
			int i = 0;
			while(i != rManager.PlayerBlacklist.size())
			{
				String[] Cutter = rManager.PlayerBlacklist.get(i).split(",");
				if(Cutter[0].equalsIgnoreCase(player.getName())) // 블랙리스트 였다면
				{
					return Found;
				}
				i++;
			}
			return !(Found);
		}
		else // 아니라면
		{
			rManager.PlayerAllowPlayers = Y.getStringList("Allow-Players"); // 허가되었는지 검사
			int i = 0;
			while(i != rManager.PlayerAllowPlayers.size() && rManager.PlayerAllowPlayers.size() != 0)
			{
				if(rManager.PlayerAllowPlayers.get(i).equalsIgnoreCase(player.getName())) // 허가되었다면
				{
					Found = true;
					break;
				}
				i++;
			}
			int x = 0;
			rManager.PlayerBlacklist = Y.getStringList("Blacklist");
			while(x != rManager.PlayerBlacklist.size())
			{
				if(rManager.PlayerBlacklist.get(x).equalsIgnoreCase(player.getName())) // 블랙리스트 였다면
				{
					Found = false;
					break;
				}
				x++;
			}
		}
		if(! Found)
		{
			try
			{
				if(! byPass)
				{
					Found = false;
				}
				
				else
				{
					Found = true;
				}
		}
		
		catch(NullPointerException e)
		{
			Found = false;
		}
		}
		return Found;
	}
}
