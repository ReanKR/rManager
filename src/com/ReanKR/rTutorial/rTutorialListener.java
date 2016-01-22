package com.ReanKR.rTutorial;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class rTutorialListener implements Listener
{
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		rTutorialMessage m = new rTutorialMessage();
		Player player = event.getPlayer();
		m.SubMsg("FirstJoinMessage", player);
	}
	
	@EventHandler
	public void PlayerMove(PlayerTeleportEvent event)
	{
		Player player = event.getPlayer();
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
	}
	
	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent event)
	{
		event.getPlayer().sendMessage(rTutorial.Prefix + "§c튜토리얼을 진행하고 있을 때는 대화를 할 수 없습니다.");
		event.setCancelled(true);
	}
}
