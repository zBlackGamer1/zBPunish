package pt.com.zBPunish;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import pt.com.zBPunish.comands.BanCMD;
import pt.com.zBPunish.comands.KickCMD;
import pt.com.zBPunish.comands.MuteCMD;
import pt.com.zBPunish.comands.PunirCMD;
import pt.com.zBPunish.comands.UnbanCMD;
import pt.com.zBPunish.comands.UnmuteCMD;
import pt.com.zBPunish.comands.ZBPunishCMD;
import pt.com.zBPunish.listeners.ChatEvent;
import pt.com.zBPunish.listeners.JoinEvent;
import pt.com.zBPunish.listeners.MuteListener;
import pt.com.zBPunish.methods.Cache;
import pt.com.zBPunish.methods.ConfigManager;
import pt.com.zBPunish.methods.Manager;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.methods.SQL;
import pt.com.zBPunish.methods.Historico.HistoricMenu;
import pt.com.zBPunish.methods.menus.AtivasMenu;
import pt.com.zBPunish.methods.menus.EscolherTempoMenu;
import pt.com.zBPunish.methods.menus.MainMenu;
import pt.com.zBPunish.methods.menus.PlayerPunishes;
import pt.com.zBPunish.methods.menus.PunirMenu;
import pt.com.zBPunish.methods.menus.PunishMenu;
import pt.com.zBPunish.methods.menus.PunishesMenu;
import pt.com.zBPunish.methods.menus.StaffersMenu;
import pt.com.zBPunish.methods.menus.StaffersPunishesMenu;
import pt.com.zBPunish.methods.menus.TimeMenu;
import pt.com.zBPunish.methods.menus.VerPunishMenu;
import pt.com.zBPunish.utils.CustomConfig;
import pt.com.zBPunish.utils.NumberFormatter;
import pt.com.zBPunish.utils.zBUtils;

public class Main extends JavaPlugin {
	public CustomConfig punirCfg;
	public static Boolean SQLLoaded;
	private static Main instance;
	public static NumberFormatter formatter;
	public static Cache cache;
	public static ConfigManager config;
	public static Manager manager;
	@Override
	public void onEnable() {
		instance = this;
		formatter = new NumberFormatter();
		config = new ConfigManager();
		punirCfg = new CustomConfig("punir.yml");
		punirCfg.saveDefaultConfig();
		cache = new Cache();
		cache.loadPunimentos();
		manager = new Manager();
		loadListeners();
		loadCmds();
		new SQL().Iniciar();
		PunishesCountMessage();
		if(SQLLoaded) zBUtils.ConsoleMsg("&7[&bzBPunish&7] &aO plugin foi iniciado.");
	}
	
	@Override
	public void onDisable() {
		new SQL().Encerrar();
		zBUtils.ConsoleMsg("&7[&bzBPunish&7] &cO plugin foi encerrado.");
	}
	
	private void loadListeners() {
		Bukkit.getPluginManager().registerEvents(new MainMenu(), this);
		Bukkit.getPluginManager().registerEvents(new AtivasMenu(), this);
		Bukkit.getPluginManager().registerEvents(new VerPunishMenu(), this);
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		Bukkit.getPluginManager().registerEvents(new HistoricMenu(), this);
		Bukkit.getPluginManager().registerEvents(new PunishMenu(), this);
		Bukkit.getPluginManager().registerEvents(new TimeMenu(), this);
		Bukkit.getPluginManager().registerEvents(new EscolherTempoMenu(), this);
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MuteListener(), this);
		Bukkit.getPluginManager().registerEvents(new StaffersMenu(), this);
		Bukkit.getPluginManager().registerEvents(new StaffersPunishesMenu(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerPunishes(), this);
		Bukkit.getPluginManager().registerEvents(new PunishesMenu(), this);
		Bukkit.getPluginManager().registerEvents(new PunirMenu(), this);
	}
	
	private void loadCmds() {
 		getCommand("zbpunish").setExecutor(new ZBPunishCMD());	
 		getCommand("zbpunish").setPermission("zbpunish.use");
 		getCommand("kick").setExecutor(new KickCMD());	
 		getCommand("kick").setPermission("zbpunish.kick");	
 		getCommand("ban").setExecutor(new BanCMD());	
 		getCommand("ban").setPermission("zbpunish.ban");
 		getCommand("unban").setExecutor(new UnbanCMD());	
 		getCommand("unban").setPermission("zbpunish.unban");
 		getCommand("mute").setExecutor(new MuteCMD());	
 		getCommand("mute").setPermission("zbpunish.mute");
 		getCommand("unmute").setExecutor(new UnmuteCMD());	
 		getCommand("unmute").setPermission("zbpunish.unmute");
 		getCommand("punir").setExecutor(new PunirCMD());	
 		getCommand("punir").setPermission("zbpunish.punir");
 		
 		String msg = "§c§lERRO! §cVocê não tem permissão para isso!";
 		getCommand("zbpunish").setPermissionMessage(msg);
 		getCommand("kick").setPermissionMessage(msg);
 		getCommand("ban").setPermissionMessage(msg);
 		getCommand("unban").setPermissionMessage(msg);
 		getCommand("mute").setPermissionMessage(msg);
 		getCommand("unmute").setPermissionMessage(msg);
 		getCommand("punir").setPermissionMessage(msg);
 		// PERMS: zbpunish.admin zbpunish.use zbpunish.kick zbpunish.ban zbpunish.unban zbpunish.mute zbpunish.unmute zbpunish.punir
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public void PunishesCountMessage() {
		new BukkitRunnable() {
			@Override
			public void run() {
				int ultimas = 0;
				Date d = new Date(new Date().getTime() - 86400000);
				for(Punish pun : Main.cache.todas.values()) if(pun.getPunishDate().after(d)) ultimas++;
				
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§eNas ultimas 24 horas foram punidos §b" + ultimas + "§e jogadores.");
				Bukkit.broadcastMessage("§eNeste servidor já foram punidos §b" + Main.cache.todasCount() + "§e jogadores.");
				Bukkit.broadcastMessage("");
				
			}
		}.runTaskTimer(getInstance(), 20L*500, 20L*3600);
	}
}
