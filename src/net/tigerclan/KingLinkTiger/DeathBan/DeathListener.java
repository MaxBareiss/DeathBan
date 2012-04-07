package net.tigerclan.KingLinkTiger.DeathBan;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerJoinEvent;

public class DeathListener implements Listener{
	Logger log;
	public DeathListener(Logger _log) {
		log = _log;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = (Player)event.getEntity();
		/*
		player.getName();
		event.getPlayer().sendMessage("Hello World");
		*/
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName()); 
		
	}

}
