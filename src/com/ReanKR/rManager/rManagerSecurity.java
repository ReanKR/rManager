package com.ReanKR.rManager;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class rManagerSecurity extends JavaPlugin
{
	public static void InteractJoinToWorld(PlayerTeleportEvent Event) //���� ����� ���� �� ����
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
		// ���࿡ ���尡 �ٲ���ٸ�
		if(! getFrom.getWorld().getName().equalsIgnoreCase(getTo.getWorld().getName()))
		{
			// ���� �̵��� ���尡 ������ �������� �켱 Ȯ���Ѵ�.
			while(i != rManager.BlockWorlds.size())
			{
				if(getTo.getWorld().getName().equalsIgnoreCase(rManager.BlockWorlds.get(i)))
				{
					found = true;
					break;
				}
				i++;
			}
			// ���� �ش� ���尡 ���� ����Ʈ�� ���� ���忴�ٸ�
			if(found)
			{
				rManager.GuildInfo = C.getStringList("Groups");
				// ��尡 �ִ��� Ȯ���ؾ� �ϴµ� ���� ��尡 ���ٸ�
				if(GuildName.equalsIgnoreCase("NONE") || GuildName.equalsIgnoreCase("LOAD_FAILED"))
				{
					int j = 0;
					// �켱 �� ������ ���� ��尡 � ������� ���� Ȯ���Ѵ�.
					while(j != rManager.GuildInfo.size())
					{
						String[] Cutter = rManager.GuildInfo.get(j).split(",");
						// ���� � ��尡 �����ϰ� �ִ� ���忴�ٸ�
						if(Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
						{
							// �� ���带 ������ �ִ� ��� ������ ������ �ν��Ͽ� �ش� �÷��̾ ����Ǿ����� �����Ѵ�.
							AllowTeleport = ChackAllowByLeader(player, Cutter[2], false);
							break;
						}
						j++;
					}
					if(AllowTeleport == false)// ���࿡ �� ������ ������ �����ٸ�
					{
						if(player.hasPermission("rmanager.teleport.bypass"))
						{
							return;
						}
						Event.setCancelled(true);
						player.sendMessage(rManager.Prefix + "��c�ش� ����� �̵��� �� �������ϴ�.");
						player.sendMessage(rManager.Prefix + "��b���� ��f: ��6�ش� ����� ��� ������ �㰡�� �־�� �մϴ�.");
						return;
					}
				}
				else // ���࿡ ��尡 �ִ� ����̶��
				{
					rManager.GuildInfo = C.getStringList("Groups");
					int j = 0;
					// �켱 �� ������ ���� ��尡 � ������� ���� Ȯ���Ѵ�.
					while(j != rManager.GuildInfo.size())
					{
						String[] Cutter = rManager.GuildInfo.get(j).split(",");
						// ���� �ش� �÷��̾� ��� �Ҽ��̰� ������ ���尡 ��尡 ������ �ִ� ������
						if(Cutter[1].equalsIgnoreCase(GuildName) && Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
						{
							if(Cutter[2].equalsIgnoreCase(player.getName()) && Cutter[3].equalsIgnoreCase(getTo.getWorld().getName())) // ���� �ش� ������ �������̿��ٸ�
							{
								AllowTeleport = true;
								break;
							}
							// �ƴ϶�� 1���� �ϴ�  ���, �׸��� ������ �ش� ������ ������Ʈ�� ����ߴ��� Ȯ���Ѵ�.
							SameGuild = true;
							AllowTeleport = ChackAllowByLeader(player, Cutter[2], true);
							break;
						}
						
						// ���� �ش� �÷��̾� �ش� ��� �Ҽ��� �ƴ����� ������ ���尡 � ��尡 ������ �ִ� ���忴�ٸ� 
						if(!(Cutter[1].equalsIgnoreCase(GuildName)))
						{
							if(Cutter[3].equalsIgnoreCase(getTo.getWorld().getName()))
							{
							// ��忡�� ���ԵǾ� ������ ���� ��� �Ҽ��� �ƴϹǷ� �ܺ������� ������.
								AllowTeleport = ChackAllowByLeader(player, Cutter[2], false);
								break;
							}
						}
						j++;
					}
					if(! AllowTeleport)// ���࿡ ������ �㰡���� �ʾҴٸ�
					{
						if(player.hasPermission("rmanager.teleport.bypass"))
						{
							return;
						}
						if(SameGuild) //�� ����Ʈ�� �����ߴٸ�
						{
							Event.setCancelled(true);
							player.sendMessage(rManager.Prefix + "��c�ش� ����� �̵��� �� �������ϴ�.");
							player.sendMessage(rManager.Prefix + "��b����  ��f: ��c������� �������� ������Ʈ�� ���");
							return;
						}
						else
						{
							Event.setCancelled(true);
							player.sendMessage(rManager.Prefix + "��c�ش� ����� �̵��� �� �������ϴ�.");
							player.sendMessage(rManager.Prefix + "��b���� ��f: ��6�ܺ����̶�� �ش� ����� ��� ������ �㰡�� �־�� �մϴ�.");
							return;
						}
					}
				}
			}
		}
	}

	public static boolean ChackAllowByLeader(Player player, String LeaderName, boolean JoinedGuild) //������ ����� �Ͽ��°�?
	{
		File LeaderFile = new File("plugins/rManager/Players/" + LeaderName + ".yml");
		if(! LeaderFile.exists())
		{
			player.sendMessage(rManager.Prefix + "Unexpected Error. The leader database who has the world does not exist.");
			player.sendMessage(rManager.Prefix + "��b����ġ ���� ������ ���Խ��ϴ�. �ش� ���带 ������ ������� �����͸� �о�� �� �������ϴ�.");
			return false;
		}
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(LeaderFile);
		boolean byPass = Y.getBoolean("Bypass-in-the-World");
		boolean Found = false; // ó������ ��ã�Ҵٰ� ������ �Ѵ�
		if(JoinedGuild) //���� ��忡 ���ԵǾ� �ִ� ����� �˻縦 �䱸�Ѵٸ�
		{
			//�� ����Ʈ �˻�
			rManager.PlayerBlacklist = Y.getStringList("Blacklist");
			int i = 0;
			while(i != rManager.PlayerBlacklist.size())
			{
				String[] Cutter = rManager.PlayerBlacklist.get(i).split(",");
				if(Cutter[0].equalsIgnoreCase(player.getName())) // ������Ʈ ���ٸ�
				{
					return Found;
				}
				i++;
			}
			return !(Found);
		}
		else // �ƴ϶��
		{
			rManager.PlayerAllowPlayers = Y.getStringList("Allow-Players"); // �㰡�Ǿ����� �˻�
			int i = 0;
			while(i != rManager.PlayerAllowPlayers.size() && rManager.PlayerAllowPlayers.size() != 0)
			{
				if(rManager.PlayerAllowPlayers.get(i).equalsIgnoreCase(player.getName())) // �㰡�Ǿ��ٸ�
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
				if(rManager.PlayerBlacklist.get(x).equalsIgnoreCase(player.getName())) // ������Ʈ ���ٸ�
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
