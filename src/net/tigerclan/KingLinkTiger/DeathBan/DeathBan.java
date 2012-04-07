    package net.tigerclan.KingLinkTiger.DeathBan;
     
import java.util.logging.Logger;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.plugin.PluginDescriptionFile;
//import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

    public class DeathBan extends JavaPlugin {
    	
    	public static DeathBan plugin;
    	
    	public final Logger logger = Logger.getLogger("Minecraft");
    	//public final PlayerListener playerListener = new PlayerListener(this);
    	
    	public void onDisable(){
    		this.logger.info("DeathBan is now disabled");
    	}
    	
        public void onEnable() {
        	getServer().getPluginManager().registerEvents(new JoinListener(this.getLogger()), this);
        	getServer().getPluginManager().registerEvents(new DeathListener(this.getLogger()), this);
        }
    	/*
     
    	Logger log;
     
    	public void onEnable(){
    		log = this.getLogger();
    		log.info("Your plugin has been enabled!");
    	}
     
    	public void onDisable(){
    		log.info("Your plugin has been disabled.");
    	}
    	
    	*/
    }

