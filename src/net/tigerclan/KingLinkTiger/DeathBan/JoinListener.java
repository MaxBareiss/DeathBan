package net.tigerclan.KingLinkTiger.DeathBan;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener{
	
	/* Starting some derpy global Variables */
	long timestamp = (System.currentTimeMillis()/1000);
	
	Logger log;
	JoinListener(Logger _log){
		log = _log;
	}
	@EventHandler
	public void onPlayerDeath(PlayerJoinEvent event){
		try {
 
			Class.forName("com.mysql.jdbc.Driver");
 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TigerClan_Community_Minecraft_Factions", "TCC_Minecraft", "c8hGZpPZuHRKKsjp");
		} catch (SQLException e) {
			log.info(e.getMessage());
			return;
		}

		if (connection != null) {
			//Shit goes here
		} else {
		}
		//int numlives = 3;
		//event.getPlayer();
		//Player player = (Player)event.getEntity()	
		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName()); 
		
		try {
			Statement stmt = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			//PreparedStatement ps;
			//ps = connection.prepareStatement("SELECT COUNT(*) FROM Deaths WHERE id = (SELECT id FROM Players WHERE username="+event.getPlayer().getDisplayName()+")");
			
			//ResultSet rs = ps.executeQuery();
			//log.info(new Integer(rs.getInt(0)).toString());
			//ps = connection.prepareStatement("SELECT id FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+event.getPlayer().getDisplayName()+"\"");
			log.info("SELECT id FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+event.getPlayer().getDisplayName()+"\"");
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT id FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+event.getPlayer().getDisplayName()+"\"");
			
			rs.last();//Go to the last record
			int rowCount = rs.getRow();//Get the number of the last record
			
			
			if(rowCount == 0){ //If the user is not in the player table
				//Run query to add user to the table
				stmt.execute("INSERT INTO `TigerClan_Community_Minecraft_Factions`.`Players` (`id`, `username`, `deaths`, `kills`, `lastseen`, `lastdeath`) VALUES (NULL, '"+event.getPlayer().getDisplayName()+"', '0', '0', '"+timestamp+"', NULL)");
				//Welcome the player to the server?
				event.getPlayer().sendMessage("Welcome to the TigerClan Factions Server");
			}else{//The player is already in the table and display their ID
				
				//I added this query here because the rs.last I added above may mess this up, it may not.
				rs2 = stmt.executeQuery("SELECT id FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+event.getPlayer().getDisplayName()+"\"");
				while (rs2.next()) {
					event.getPlayer().sendMessage("Your TigerClan ID is " + rs2.getInt("id"));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("EXCEPTION "+e.getMessage());
		}
		//event.getPlayer().sendMessage("You have " + numlives + " lives left");
		//Test comment
		
		
	}

}