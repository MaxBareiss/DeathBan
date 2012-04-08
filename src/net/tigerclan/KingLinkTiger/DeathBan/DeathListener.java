//TODO Add the last death to the Players table
//TODO Get the entity
//TODO Increment the death counter
//TODO Increment the kill counter


package net.tigerclan.KingLinkTiger.DeathBan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Logger;

//import org.bukkit.Bukkit;
//import org.bukkit.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerJoinEvent;

public class DeathListener implements Listener{

	long timestamp = (System.currentTimeMillis()/1000);
	
	Logger log;
	public DeathListener(Logger _log) {
		log = _log;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player Player = (Player)event.getEntity();
		Player Killer_p = event.getEntity().getKiller();
		Player Killer_e = event.getEntity().getKiller();

		//Add the mysql driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		Connection connection = null;
		
		
		//Try connecting to the mysql server
		try{
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TigerClan_Community_Minecraft_Factions", "TCC_Minecraft", "c8hGZpPZuHRKKsjp");
		} catch (SQLException e) {
			log.info(e.getMessage());
			return;
		}
		
		
		
		try{
			Statement stmt = null;
			Statement stmt2 = null;
			Statement stmt3 = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			
			stmt = connection.createStatement();
			stmt2 = connection.createStatement();
			stmt3 = connection.createStatement();
			rs = stmt.executeQuery("SELECT id,deaths FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+Player.getDisplayName()+"\"");
			if(event.getEntity().getKiller() != null){
				rs2 = stmt2.executeQuery("SELECT id,kills FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+Killer_p.getDisplayName()+"\"");
				
				while(rs2.next()){//Killer
					int killerid = rs2.getInt("id");
					
					//Get the number of kills the player has
					int numkills = rs2.getInt("kills");
					
					//Increment
					numkills++;
					
					//Update the table with the new number of kills
					stmt3.execute("UPDATE `TigerClan_Community_Minecraft_Factions`.`Players` SET kills = "+numkills+" WHERE id="+killerid+"");
					
					while (rs.next()) {//Person who got killed
						int userid = rs.getInt("id");
						
						//Get the player's number of deaths
						int numdeaths = rs.getInt("deaths");
						
						//Increment it
						numdeaths++;
						
						//Update the player table with the new number of deaths
						stmt3.execute("UPDATE `TigerClan_Community_Minecraft_Factions`.`Players` SET deaths = "+numdeaths+" WHERE id="+userid+"");
						
						//Insert a new death into the Deaths table
						stmt3.execute("INSERT INTO `TigerClan_Community_Minecraft_Factions`.`Deaths` (`ID`, `Player`, `Date`, `Method`, `Killer`) VALUES (NULL, '"+userid+"', '"+timestamp+"', '"+Player.getLastDamageCause().getCause()+"', '"+killerid+"')");
						
						long todaysunix = 0;
						
						try {
							Calendar cal = Calendar.getInstance();
							int day = cal.get(Calendar.DATE);
							int month = cal.get(Calendar.MONTH) + 1;
							int year = cal.get(Calendar.YEAR);
							todaysunix = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(""+day+"/"+month+"/"+year+" 00:00:00").getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						Statement stmt4 = null;
						stmt4 = connection.createStatement();
						rs3 = stmt4.executeQuery("SELECT count(*) AS total FROM TigerClan_Community_Minecraft_Factions.Deaths WHERE Player="+userid+" AND Date >= "+todaysunix+"");
						long num_of_deaths = rs3.getInt("total");
						
						//If the player has died three, or more, times today then kick ban them
						if(num_of_deaths >= 3){
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + Player.getName());
						}
					}
				}
			}else{
				while (rs.next()) {
					int userid = rs.getInt("id");
					
					//Get the player's number of deaths
					int numdeaths = rs.getInt("deaths");
					
					//Increment it
					numdeaths++;
					
					//Update the player table with the new number of deaths
					stmt3.execute("UPDATE `TigerClan_Community_Minecraft_Factions`.`Players` SET deaths = "+numdeaths+" WHERE id="+userid+"");
					
					
					stmt3.execute("INSERT INTO `TigerClan_Community_Minecraft_Factions`.`Deaths` (`ID`, `Player`, `Date`, `Method`, `Killer`, `Entity`) VALUES (NULL, '"+userid+"', '"+timestamp+"', '"+Player.getLastDamageCause().getCause()+"', '0', '"+Killer_e+"')");
								
					long todaysunix = 0;
					
					try {
						Calendar cal = Calendar.getInstance();
						int day = cal.get(Calendar.DATE);
						int month = cal.get(Calendar.MONTH) + 1;
						int year = cal.get(Calendar.YEAR);
						todaysunix = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(""+month+"/"+day+"/"+year+" 00:00:00").getTime();
						todaysunix = todaysunix/1000;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					Statement stmt4 = null;
					stmt4 = connection.createStatement();
					log.info("SELECT count(*) AS total FROM TigerClan_Community_Minecraft_Factions.Deaths WHERE Player="+userid+" AND Date >= "+todaysunix+"");
					rs3 = stmt4.executeQuery("SELECT count(*) AS total FROM TigerClan_Community_Minecraft_Factions.Deaths WHERE Player = "+userid+" AND Date >= "+todaysunix+"");
					while(rs3.next()){
						int num_of_deaths = rs3.getInt("total");
						
						//If the player has died three, or more, times today then kick ban them
						if(num_of_deaths >= 3){
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + Player.getName());
						}
					}
				}
			}
			
			
		} catch (SQLException e) {
			log.severe("EXCEPTION "+e.getMessage());
		}
		
		
		/* Unused code at the momement
		int numrows = 0;
		if(numrows >= 3){//If the player has died three, or more, times today ban them
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + Player.getName());
		}
		*/
	}
}
