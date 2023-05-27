package pt.com.zBPunish.methods;

import pt.com.zBPunish.Main;

public class Jogador {
	private String nick;
	
	public boolean hasPunish() {
		if(Main.cache.bans.containsKey(nick) || Main.cache.mutes.containsKey(nick)) return true;
		return false;
	}
	
	public Punish getPunish() {
		if(!hasPunish()) return null;
		if(Main.cache.bans.containsKey(nick)) return Main.cache.bans.get(nick);
		if(Main.cache.mutes.containsKey(nick)) return Main.cache.mutes.get(nick);
		return null;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public Jogador(String nick) {
		this.nick = nick.toLowerCase();
	}
}
