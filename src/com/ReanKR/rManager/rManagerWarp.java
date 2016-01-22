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
			player.sendMessage(rManager.Prefix + "§b해당 §f개인 워프로 이동하기 위해 시도하고 있습니다...");
			player.teleport(Loc);
		}
		else
		{
			player.sendMessage(rManager.Prefix + "§c해당 워프 이름을 찾지 못했습니다.");
			player.sendMessage(rManager.Prefix + "§c지정한 워프 목록 보기 /rm warp info");
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
			player.sendMessage(rManager.Prefix + "§6지정한 워프가 한 개도 없습니다.");
			return;
		}
		int i = 0;
		player.sendMessage(rManager.Prefix + "§f============ §bC§fustom§1P§elayer§aWarp §f============");
		player.sendMessage(rManager.Prefix + "§b" + player.getName() + "§e님이 지정한 §f모든 워프 포인트 리스트");
		if(player.hasPermission("customplayerwarp.unlimited"))
		{
			player.sendMessage(rManager.Prefix + "§a지정 가능한 남은 갯수 : §f" + "Unlimited" + " §f/§3 " + "Inf.");
			player.sendMessage(rManager.Prefix + "§9(무제한 권한을 소지하고 있으므로 남은 갯수 제한이 없습니다.");
		}
		else
		{
		player.sendMessage(rManager.Prefix + "§a지정 가능한 남은 갯수 : §f" + (SetWarpAmount -rManager.PrivateWarp.size()) + "§f/§3" + SetWarpAmount);
		}
		while(i != rManager.PrivateWarp.size())
		{
			String[] Cutter = rManager.PrivateWarp.get(i).split(",");
			player.sendMessage(rManager.Prefix + "§f[§b" + i + "§f]§e " + Cutter[0]);
			i++;
		}
		player.sendMessage(rManager.Prefix + "§f============ §bC§fustom§1P§elayer§aWarp §f============");
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
				player.sendMessage(rManager.Prefix + "§6워프를 지정할 수 있는 갯수를 초과하여 더 이상 지정할 수 없습니다.");
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
				player.sendMessage(rManager.Prefix + "§6기본 워프 위치를 바꿨습니다.");
				return;
			}
			else
			{
				player.sendMessage(rManager.Prefix + "§6해당 워프의 위치를 바꿨습니다.");
				player.sendMessage(rManager.Prefix + "§b워프 이름 : " + WarpName);
				return;
			}
		}
		else
		{
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "§6기본 워프 위치가 새로 설정되었습니다.");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "§6워프를 새로 지정하였습니다.");
				player.sendMessage(rManager.Prefix + "§b워프 이름 §f:§a " + WarpName);
			}
			
			if(player.hasPermission("rmanager.warp.unlimited"))
			{
				player.sendMessage(rManager.Prefix + "§e지정할 수 있는 갯수 §f:§3 " + "Unlimited" + " §f/§3 " + "infinite");
				return;
			}
			else
			{
			player.sendMessage(rManager.Prefix + "§e지정할 수 있는 갯수 §f:§3 " + (SetWarpAmount - rManager.PrivateWarp.size()) + "§f/§3" + SetWarpAmount);
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
			player.sendMessage(rManager.Prefix + "§6지정된 워프가 한개도 없습니다!");
			return;
		}
		if(GetCount == -1)
		{
			if(WarpName.equalsIgnoreCase("Default"))
			{
				player.sendMessage(rManager.Prefix + "§6기본 워프 위치가 지정되지 않았습니다.");
				return;
			}
			else
			{
				player.sendMessage(rManager.Prefix + "§6해당 이름을 가진 워프에 관한 정보는 존재하지 않습니다!");
				player.sendMessage(rManager.Prefix + "§b지정한 워프를 보려면 §6/rm warp info");
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
				player.sendMessage(rManager.Prefix + "§6기본 설정 워프를 지웠습니다.");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "§6해당 워프를 지웠습니다.");
				player.sendMessage(rManager.Prefix + "§b워프 이름 §f:§a " + WarpName);
			}
			if(player.hasPermission("rmanager.warp.unlimited"))
			{
				player.sendMessage(rManager.Prefix + "§e지정할 수 있는 갯수 §f:§3 " + "Unlimited" + " §f/§3 " + "infinite");
			}
			else
			{
				player.sendMessage(rManager.Prefix + "§e지정할 수 있는 갯수 §f:§3 " + rManager.PrivateWarp.size() + "§f/§3" + SetWarpAmount);
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
			player.sendMessage(rManager.Prefix + "§b" + GuildName + " §e길드 월드의 §a기본 스폰 위치를 새로 지정하였습니다.");
		}
		else
		{
			rManager.PublicWarp.add("GuildDefaultSpawn" + "," + GuildName + "," + setLoc.getWorld().getName()
					+ "," + setLoc.getBlockX() + "," + setLoc.getBlockY() + "," + setLoc.getBlockZ() + "," + setLoc.getPitch() + "," + setLoc.getYaw());
			player.sendMessage(rManager.Prefix + "§b" + GuildName + " §e길드 월드의 §a기본 스폰 위치를 지정하였습니다.");
			player.sendMessage(rManager.Prefix + "§e이제부터 길드원둘이 §6/rm guild spawn§e을 §f정상적으로 사용할 수 있습니다.");
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
				player.sendMessage(rManager.Prefix + "§b" + GuildName + " §c오류가 발생하였습니다.");
				player.sendMessage(rManager.Prefix + "§b" + GuildName + " §b사유 §f: §cPublicWarp.yml 파일을 찾을 수 없음");
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
			player.sendMessage(rManager.Prefix + "§c길드 정보를 등록했으나 스폰 위치가 지정되지 않았습니다.");
			player.sendMessage(rManager.Prefix + "§c리더장에게 지정해달라고 요청하거나 관련 유저에게 문의하십시오.");
			return;
		}
	}
}
