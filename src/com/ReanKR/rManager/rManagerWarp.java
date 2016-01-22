package com.ReanKR.rManager;

import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class rManagerWarp
{
	public static void PlayerWarp(Player player, String WarpName)
	{
		File PlayerWarp = new File("plugins/rManager/Warps/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerWarp);
		int i = 0;
		rManager.PrivateWarp = Y.getStringList("Warps");
		boolean found = false;
		Location Loc = null;
		while(i != rManager.PrivateWarp.size())
		{
			String[] Cutter = rManager.PrivateWarp.get(i).split(",");
			if(Cutter[0].equalsIgnoreCase(WarpName))
			{
				World world = rManager.Main.getServer().getWorld(Cutter[1]);
				int x = Integer.parseInt(Cutter[2]);
				int y = Integer.parseInt(Cutter[3]);
				int z = Integer.parseInt(Cutter[4]);
				float pitch = Float.parseFloat(Cutter[5]);
				float yaw = Float.parseFloat(Cutter[6]);
				Loc = new Location(world,x,y,z,pitch,yaw);
				found = true;
				break;
			}
			i++;
		}
		if(found)
		{
			player.sendMessage(rManager.Prefix + "��b�ش� ��f���� ������ �̵��ϱ� ���� �õ��ϰ� �ֽ��ϴ�...");
			player.teleport(Loc);
		}
		else
		{
			player.sendMessage(rManager.Prefix + "��c�ش� ���� �̸��� ã�� ���߽��ϴ�.");
			player.sendMessage(rManager.Prefix + "��c������ ���� ��� ���� /rm warp info");
		}
		return;
	}
	
	public static void ShowWarpList(Player player)
	{
		File PlayerWarp = new File("plugins/rManager/Warps/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerWarp);
		int SetWarpAmount = Y.getInt("Set-Warp-Left-Amount");
		if(! PlayerWarp.exists())
		{
			Y.addDefault("Warps", "");
			try {
				Y.save(PlayerWarp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rManager.PrivateWarp = Y.getStringList("Warps");
		if(rManager.PrivateWarp.size() == 0)
		{
			player.sendMessage(rManager.Prefix + "��6������ ������ �� ���� �����ϴ�.");
			return;
		}
		int i = 0;
		player.sendMessage(rManager.Prefix + "��f============ ��bC��fustom��1P��elayer��aWarp ��f============");
		player.sendMessage(rManager.Prefix + "��b" + player.getName() + "��e���� ������ ��f��� ���� ����Ʈ ����Ʈ");
		if(player.hasPermission("customplayerwarp.unlimited"))
		{
			player.sendMessage(rManager.Prefix + "��a���� ������ ���� ���� : ��f" + "Unlimited" + " ��f/��3 " + "Inf.");
			player.sendMessage(rManager.Prefix + "��9(������ ������ �����ϰ� �����Ƿ� ���� ���� ������ �����ϴ�.");
		}
		else
		{
		player.sendMessage(rManager.Prefix + "��a���� ������ ���� ���� : ��f" + (SetWarpAmount -rManager.PrivateWarp.size()) + "��f/��3" + SetWarpAmount);
		}
		while(i != rManager.PrivateWarp.size())
		{
			String[] Cutter = rManager.PrivateWarp.get(i).split(",");
			player.sendMessage(rManager.Prefix + "��f[��b" + i + "��f]��e " + Cutter[0]);
			i++;
		}
		player.sendMessage(rManager.Prefix + "��f============ ��bC��fustom��1P��elayer��aWarp ��f============");
	}

	public static void PlayerAddWarp(Player player, String WarpName)
	{
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerFile);
		int SetWarpAmount = Y.getInt("Set-Warp-Left-Amount");
		File PlayerWarp = new File("plugins/rManager/Warps/" + player.getName() + ".yml");
		Y = YamlConfiguration.loadConfiguration(PlayerWarp);
		if(! PlayerWarp.exists())
		{
			Y.addDefault("Warps", "");
			try {
				Y.save(PlayerWarp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rManager.PrivateWarp = Y.getStringList("Warps");
		int i = 0;
		boolean found = false;
		while(i != rManager.PrivateWarp.size())
		{
			String[] Cutter = rManager.PrivateWarp.get(i).split(",");
			if(Cutter[0].equalsIgnoreCase(WarpName))
			{
				rManager.PrivateWarp.remove(i);
				found = true;
				break;
			}
			i++;
		}
		if((SetWarpAmount - rManager.PrivateWarp.size()) <= 0  && !(found))
		{
			if(! player.hasPermission("rmanager.warp.unlimited"))
			{
				player.sendMessage(rManager.Prefix + "��6������ ������ �� �ִ� ������ �ʰ��Ͽ� �� �̻� ������ �� �����ϴ�.");
				return;
			}
		}
		Location Loc = player.getLocation();
		String world = Loc.getWorld().getName();
		int x = Loc.getBlockX();
		int y = Loc.getBlockY();
		int z = Loc.getBlockZ();
		float pitch = Loc.getPitch();
		float yaw = Loc.getYaw();
		rManager.PrivateWarp.add(WarpName + "," + world + "," + x + "," + y + "," + z + "," + pitch + "," + yaw);
		Y.set("Warps", rManager.PrivateWarp);
		try {
			Y.save(PlayerWarp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(found)
		{
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "��6�⺻ ���� ��ġ�� �ٲ���ϴ�.");
				return;
			}
			else
			{
				player.sendMessage(rManager.Prefix + "��6�ش� ������ ��ġ�� �ٲ���ϴ�.");
				player.sendMessage(rManager.Prefix + "��b���� �̸� : " + WarpName);
				return;
			}
		}
		else
		{
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "��6�⺻ ���� ��ġ�� ���� �����Ǿ����ϴ�.");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "��6������ ���� �����Ͽ����ϴ�.");
				player.sendMessage(rManager.Prefix + "��b���� �̸� ��f:��a " + WarpName);
			}
			
			if(player.hasPermission("rmanager.warp.unlimited"))
			{
				player.sendMessage(rManager.Prefix + "��e������ �� �ִ� ���� ��f:��3 " + "Unlimited" + " ��f/��3 " + "infinite");
				return;
			}
			else
			{
			player.sendMessage(rManager.Prefix + "��e������ �� �ִ� ���� ��f:��3 " + (SetWarpAmount - rManager.PrivateWarp.size()) + "��f/��3" + SetWarpAmount);
			return;
			}
		}
	}
	
	public static void PlayerDelWarp(Player player, String WarpName)
	{
		File PlayerFile = new File("plugins/rManager/Players/" + player.getName() + ".yml");
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PlayerFile);
		int SetWarpAmount = Y.getInt("Set-Warp-Left-Amount");
		File PlayerWarp = new File("plugins/rManager/Warps/" + player.getName() + ".yml");
		Y = YamlConfiguration.loadConfiguration(PlayerWarp);
		if(! PlayerWarp.exists())
		{
			Y.addDefault("Warps", "");
			try {
				Y.save(PlayerWarp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rManager.PrivateWarp = Y.getStringList("Warps");
		int i = 0;
		int GetCount = -1;
		boolean found = false;
		while(i != rManager.PrivateWarp.size())
		{
			String[] Cutter = rManager.PrivateWarp.get(i).split(",");
			if(Cutter[0].equalsIgnoreCase(WarpName))
			{
				GetCount = i;
				found = true;
				break;
			}
			i++;
		}
		if(rManager.PrivateWarp.size() == 0)
		{
			player.sendMessage(rManager.Prefix + "��6������ ������ �Ѱ��� �����ϴ�!");
			return;
		}
		if(GetCount == -1)
		{
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "��6�⺻ ���� ��ġ�� �������� �ʾҽ��ϴ�.");
				return;
			}
			else
			{
				player.sendMessage(rManager.Prefix + "��6�ش� �̸��� ���� ������ ���� ������ �������� �ʽ��ϴ�!");
				player.sendMessage(rManager.Prefix + "��b������ ������ ������ ��6/rm warp info");
				return;
			}
		}
		
		if(found)
		{
			rManager.PrivateWarp.remove(GetCount);
			Y.set("Warps", rManager.PrivateWarp);
			try {
				Y.save(PlayerWarp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "��6�⺻ ���� ������ �������ϴ�.");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "��6�ش� ������ �������ϴ�.");
				player.sendMessage(rManager.Prefix + "��b���� �̸� ��f:��a " + WarpName);
			}
			if(player.hasPermission("rmanager.warp.unlimited"))
			{
				player.sendMessage(rManager.Prefix + "��e������ �� �ִ� ���� ��f:��3 " + "Unlimited" + " ��f/��3 " + "infinite");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "��e������ �� �ִ� ���� ��f:��3 " + rManager.PrivateWarp.size() + "��f/��3" + SetWarpAmount);
			}
			return;
		}
	}

	public static void PublicWarp(Player player, String GuildName, boolean Registered)
	{
		if(Registered)
		{
		Location setLoc = player.getLocation();
		File PublicWarp = new File("plugins/rManager/Warps/PublicWarp.yml");
		if(! PublicWarp.exists())
		{
			try {
				rManagerConfig.CreatePublicWarp();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		YamlConfiguration Y = YamlConfiguration.loadConfiguration(PublicWarp);
		rManager.PublicWarp = Y.getStringList("Groups");
		int i = 0;
		boolean found = false;
		while(i != rManager.PublicWarp.size())
		{
			String[] Cutter = rManager.PublicWarp.get(i).split(",");
			if(Cutter[1].equalsIgnoreCase(GuildName))
			{
				if(Cutter[0].equalsIgnoreCase("GuildDefaultSpawn"))
				{
					rManager.PublicWarp.remove(i);
					found = true;
					break;
				}
			}
			i++;	
		}
		if(found)
		{
			rManager.PublicWarp.add("GuildDefaultSpawn" + "," + GuildName + "," + setLoc.getWorld().getName()
					+ "," + setLoc.getBlockX() + "," + setLoc.getBlockY() + "," + setLoc.getBlockZ() + "," + setLoc.getPitch() + "," + setLoc.getYaw());
			player.sendMessage(rManager.Prefix + "��b" + GuildName + " ��e��� ������ ��a�⺻ ���� ��ġ�� ���� �����Ͽ����ϴ�.");
		}
		else
		{
			rManager.PublicWarp.add("GuildDefaultSpawn" + "," + GuildName + "," + setLoc.getWorld().getName()
					+ "," + setLoc.getBlockX() + "," + setLoc.getBlockY() + "," + setLoc.getBlockZ() + "," + setLoc.getPitch() + "," + setLoc.getYaw());
			player.sendMessage(rManager.Prefix + "��b" + GuildName + " ��e��� ������ ��a�⺻ ���� ��ġ�� �����Ͽ����ϴ�.");
			player.sendMessage(rManager.Prefix + "��e�������� �������� ��6/rm guild spawn��e�� ��f���������� ����� �� �ֽ��ϴ�.");
		}
		Y.set("Groups", rManager.PublicWarp);
		try {
			Y.save(PublicWarp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
		}
		else
		{
			File PublicWarp = new File("plugins/rManager/Warps/PublicWarp.yml");
			if(! PublicWarp.exists())
			{
				player.sendMessage(rManager.Prefix + "��b" + GuildName + " ��c������ �߻��Ͽ����ϴ�.");
				player.sendMessage(rManager.Prefix + "��b" + GuildName + " ��b���� ��f: ��cPublicWarp.yml ������ ã�� �� ����");
				return;
			}
			YamlConfiguration Y = YamlConfiguration.loadConfiguration(PublicWarp);
			rManager.PublicWarp = Y.getStringList("Groups");
			int i = 0;
			while(i != rManager.PublicWarp.size())
			{
				String[] Cutter = rManager.PublicWarp.get(i).split(",");
				if(Cutter[0].equalsIgnoreCase("GuildDefaultSpawn") && Cutter[1].equalsIgnoreCase(GuildName))
				{
					World world = rManager.Main.getServer().getWorld(Cutter[2]);
					int x = Integer.parseInt(Cutter[3]);
					int y = Integer.parseInt(Cutter[4]);
					int z = Integer.parseInt(Cutter[5]);
					float pitch = Float.parseFloat(Cutter[6]);
					float yaw = Float.parseFloat(Cutter[7]);
					Location getLoc = new Location(world,x,y,z,yaw,pitch);
					player.teleport(getLoc);
					return;
				}
				i++;
			}
			player.sendMessage(rManager.Prefix + "��c��� ������ ��������� ���� ��ġ�� �������� �ʾҽ��ϴ�.");
			player.sendMessage(rManager.Prefix + "��c�����忡�� �����ش޶�� ��û�ϰų� ���� �������� �����Ͻʽÿ�.");
			return;
		}
	}
}
