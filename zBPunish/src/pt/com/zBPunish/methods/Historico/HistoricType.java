package pt.com.zBPunish.methods.Historico;

public enum HistoricType {
	KICK("Kickou"), BAN("Baniu"), MUTE("Mutou"), UNBAN("Desbaniu"), UNMUTE("Desmutou"), EDIT("Editou Provas");

	public String name;
	HistoricType(String string) {
		this.name = string;
	}
	
	public String getName() {
		return name;
	}
}
