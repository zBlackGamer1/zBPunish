package pt.com.zBPunish.methods;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.utils.zBUtils;

public class ConfigManager {
	private FileConfiguration c;
	public String kick_formato;
	public String kick_motdefault;
	public Boolean kick_alerta_ativo;
	public List<String> kick_alerta;
	
	public String ban_formato;
	
	public ConfigManager() {
		Main.getInstance().saveDefaultConfig();
		c = Main.getInstance().getConfig();
		loadKickFormato();
		kick_motdefault = c.getString("Kick.Motivo_default");
		kick_alerta_ativo = c.getBoolean("Kick.Alerta");
		kick_alerta = r(c.getStringList("Kick.Alerta_mensagem"));
		loadBanFormato();
	}
	
	private void loadKickFormato() {
		List<String> sl = c.getStringList("Kick.Formato");
		String formato = "";
		int i = 0;
		for(String s : sl) {
			if(i == 0) formato = s;
			else formato = formato + "\n" + s;
			i++;
		}
		kick_formato = r(formato);
	}
	
	private void loadBanFormato() {
		List<String> sl = c.getStringList("Ban.Formato");
		String formato = "";
		int i = 0;
		for(String s : sl) {
			if(i == 0) formato = s;
			else formato = formato + "\n" + s;
			i++;
		}
		ban_formato = r(formato);
	}
	
	private String r(String s) {
		return s.replace("&", "§");
	}
	
	private List<String> r(List<String> sl) {
		return zBUtils.replaceList(sl, "&", "§");
	}
}
