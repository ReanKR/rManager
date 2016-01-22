package com.ReanKR.rTutorial;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.connorlinfoot.titleapi.TitleAPI;

public class rTutorialCommands implements CommandExecutor
{
	public rTutorial plugin;
	PluginCommand pluginCommand;

	public rTutorialCommands(rTutorial Main)
	{
		this.plugin = Main;
	}
	@Override
	public boolean onCommand(CommandSender Sender, Command command, String string, String[] args)
	{
		Player player = (Player)Sender;
		ConsoleCommandSender Console = Bukkit.getConsoleSender();
		String cmd = command.getName();
		if(cmd.equalsIgnoreCase("tutorial"))
		{
			if(! rTutorial.EditComplete)
			{
				player.sendMessage(rTutorial.Prefix + "§b튜토리얼 구성이 완료되지 않았습니다.");
				player.sendMessage(rTutorial.Prefix + "§c튜토리얼을 시작할 수 없었습니다.");
			}
			else
			{
				player.sendMessage(rTutorial.Prefix + "");
			}
			
		}
		if(cmd.equalsIgnoreCase("rtutorial.main"))
		{
				if(args.length < 1)
				{
					player.sendMessage(rTutorial.Prefix + "§9r§aT§futorial v" + plugin.getDescription().getVersion());
					player.sendMessage(rTutorial.Prefix + "");
					player.sendMessage(rTutorial.Prefix + "/tutorial");
					player.sendMessage(rTutorial.Prefix + "Start tutorial.");
					player.sendMessage(rTutorial.Prefix + "");
					player.sendMessage(rTutorial.Prefix + "/rt | rtutorial set <Message> [SubMessage] [Fadein] [FadeOut] [Duration]");
					player.sendMessage(rTutorial.Prefix + "(If your server not use TitleAPI, Unavailable all arguments.)");
					player.sendMessage(rTutorial.Prefix + "");
					player.sendMessage(rTutorial.Prefix + "/rt | rtutorial delete <index>");
					player.sendMessage(rTutorial.Prefix + "Delete loacation information <index>.");
					player.sendMessage(rTutorial.Prefix + "");
					return true;
				}
				if(args[0].equalsIgnoreCase("set"))
				{
					if(args.length < 2)
					{
						player.sendMessage(rTutorial.Prefix + "§c/rt | rtutorial set <Message> [SubMessage] [Fadein] [FadeOut] [Duration]");
						return false;
					}
					if(args.length >= 2)
					{
						try
						{
							if(args.length == 2)
							{
								rTutorialRegister.LocationRegister(player.getLocation(), "&eThis is World", "NONE", 0, 0, 0, 0);
								return true;
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
		}
		return false;
	}

}
