package pt.com.zBPunish.methods;

public enum PunishType {
	BAN("Ban"), MUTE("Mute");

	public String name;
	PunishType(String string) {
		this.name = string;
	}
	
	public String getName() {
		return name;
	}
}
