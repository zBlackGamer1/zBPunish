package pt.com.zBPunish.methods;

import pt.com.zBPunish.Main;

public class Punir {
	private PunishType type;
	private Long time;
	private String name;
	
	public PunishType getType() {
		return this.type;
	}
	
	public Long getTime() {
		return this.time;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Punir(PunishType type, Long time, String name) {
		this.type = type;
		this.time = time;
		this.name = name;
		Main.cache.punimentos.put(name, this);
	}
}
