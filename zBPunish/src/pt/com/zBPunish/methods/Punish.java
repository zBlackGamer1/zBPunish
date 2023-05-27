package pt.com.zBPunish.methods;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import pt.com.zBPunish.Main;

public class Punish {
	private Integer punishID;
	private PunishType type;
	private Date punishDate;
	private Date expireTime;
	private String motivo;
	private String punisher;
	private String punished;
	private String prova;
	private boolean unpunished;
	
	public Integer getPunishID() {
		return punishID;
	}
	
	public PunishType getPunishType() {
		return type;
	}
	
	public Date getPunishDate() {
		return punishDate;
	}
	
	public Date getExpireDate() {
		return expireTime;
	}
	
	public String getMotivo() {
		return motivo;
	}
	
	public String getPunisher() {
		return punisher;
	}
	
	public boolean isAtiva() {
		if(isUnpunished()) return false;
		if(getExpireDate() == null) return true;
		Date agora = new Date();
		return getExpireDate().after(agora);
	}
	
	public boolean isUnpunished() {
		return unpunished;
	}
	
	public void setUnpunished(boolean b) {
		unpunished = b;
	}
	
	@SuppressWarnings("deprecation")
	public String getPunished() {
		String name = punished;
		OfflinePlayer offp = Bukkit.getOfflinePlayer(name);
		if(offp != null) name = offp.getName();
		return name;
	}
	
	public String getProva() {
		return prova;
	}
	
	public void setProva(String s) {
		this.prova = s;
	}
	
	public String getTimeToExpire() {
		if(getExpireDate() == null) return " Permanente";
		Long tempo = getExpireDate().getTime() - new Date().getTime(); 
		if(tempo == 0) return "§a Expirou Agora";
		String f = "§7";
		if(tempo < 0) {
			tempo = new Date().getTime() - getExpireDate().getTime();
			f = "§a Expirou há";
		}
		long t = tempo;
		long days = 0;
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		
		if(t >= 86400000) {
			days = t / 86400000;
			t -= days * 86400000;
		}
		
		if(t >= 3600000) {
			hours = t / 3600000;
			t -= hours * 3600000;
		}
		
		if(t >= 60000) {
			minutes = t / 60000;
			t -= minutes * 60000;
		}
		
		if(t >= 1000) {
			seconds = t / 1000;
			t -= seconds * 1000;
		}
		
		if(days != 0) f = f + " " + days + "d";
		if(hours != 0) f = f + " " + hours + "h";
		if(minutes != 0) f = f + " " + minutes + "m";
		if(seconds != 0) f = f + " " + seconds + "s";
		
		return f;
	}
	
	public static Punish getPunish(Integer punishID) {
		if(Main.cache.todas.containsKey(punishID)) return Main.cache.todas.get(punishID);
		return null;
	}
	
	public Punish(Integer punishID, PunishType punishtype, Date punishDate, Date expireTime, String motivo, String punisher, String punished, String prova, Boolean unpunished) {
		this.punishID = punishID;
		this.type = punishtype;
		this.punishDate = punishDate;
		if(expireTime.getTime() - 1 == punishDate.getTime()) this.expireTime = null;
		else this.expireTime = expireTime;
		this.motivo = motivo;
		this.punisher = punisher;
		this.punished = punished.toLowerCase();
		this.prova = prova;
		this.unpunished = unpunished;
		
		Main.cache.todas.put(punishID, this);
		if (this.unpunished) {
			addOnStaffers(punisher, this);
			return;
		}
		if(this.expireTime == null || expireTime.after(new Date())) {
			Main.cache.ativas.put(punishID, this);
			if(punishtype == PunishType.BAN) Main.cache.bans.put(punished.toLowerCase(), this);
			if(punishtype == PunishType.MUTE) Main.cache.mutes.put(punished.toLowerCase(), this);
		}
		addOnStaffers(punisher, this);
	}
	private void addOnStaffers(String staffer, Punish pun) {
		List<Punish> punList = (Main.cache.staffers.containsKey(staffer)) ? Main.cache.staffers.get(staffer) : new ArrayList<>();
		punList.add(pun);
		Main.cache.staffers.put(staffer, punList);
	}
}
