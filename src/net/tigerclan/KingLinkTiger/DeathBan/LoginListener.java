package net.tigerclan.KingLinkTiger.DeathBan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class LoginListener implements Listener {
	
	Logger log;
	//JoinListener(Logger _log){
	//	log = _log;
	//}
	@EventHandler
	public void onPlayerLogin(PlayerPreLoginEvent event){
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
		ResultSet rs2 = null;
		Statement stmt = null;
		stmt = connection.createStatement();
		rs2 = stmt.executeQuery("SELECT id FROM TigerClan_Community_Minecraft_Factions.Players WHERE username=\""+event.getName()+"\"");
		while (rs2.next()) {
		int userid = rs2.getInt("id");
					
		long todaysunix = 0;
		ResultSet rs3 = null;
		
		try {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DATE);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			todaysunix = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(""+month+"/"+day+"/"+year+" 00:00:00").getTime();
			todaysunix = todaysunix/1000;
		} catch (ParseException e) {
			log.severe(e.getMessage());
		}
		
			Statement stmt4 = null;
			stmt4 = connection.createStatement();
			log.info("SELECT count(*) AS total FROM TigerClan_Community_Minecraft_Factions.Deaths WHERE Player="+userid+" AND Date >= "+todaysunix+"");
			rs3 = stmt4.executeQuery("SELECT count(*) AS total FROM TigerClan_Community_Minecraft_Factions.Deaths WHERE Player = "+userid+" AND Date >= "+todaysunix+"");
			while(rs3.next()){
				int num_of_deaths = rs3.getInt("total");
				
				//If the player has died three, or more, times today then kick ban them
				if(num_of_deaths < 3){
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + event.getName());
				}//End if
			}//End While
		}
		} catch (SQLException e) {
			log.severe("EXCEPTION "+e.getMessage());
		}
	}//End Event
}//End class