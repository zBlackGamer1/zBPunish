package pt.com.zBPunish.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Historico.HistoricAction;
import pt.com.zBPunish.utils.CustomConfig;
import pt.com.zBPunish.utils.NBTAPI;

public class Cache {
	public Date iniciado;
	public String banKickMessage;
	public List<String> naoBaniveis;
	public Map<Integer, Punish> todas;
	public Map<Integer, Punish> ativas;
	public Map<String, Punish> digit;
	public List<String> digitID;
	public List<String> digitPlayer;
	public Map<Date, HistoricAction> historico;
	public Map<String, Punish> bans;
	public Map<String, Punish> mutes;
	public Map<String, List<Punish>> staffers;
	
	public Map<String, NBTAPI> digit1;
	public Map<String, NBTAPI> digit2;
	
	public Map<String, Punir> punimentos;
	
	public Integer ativasCount() {
		return this.ativas.size();
	}
	
	public Integer todasCount() {
		return this.todas.size();
	}
	
	public Integer gerarID() {
		return todasCount() + 1;
	}
	
	public Cache() {
		iniciado = new Date();
		this.todas = new TreeMap<>();
		this.ativas = new TreeMap<>();
		this.digit = new HashMap<>();
		this.digitID = new ArrayList<>();
		this.digitPlayer = new ArrayList<>();
		this.historico = new TreeMap<>();
		this.bans = new HashMap<>();
		this.mutes = new HashMap<>();
		this.staffers = new TreeMap<>();
		this.digit1 = new HashMap<>();
		this.digit2 = new HashMap<>();
		this.punimentos = new TreeMap<>();
		loadMessage();
		loadNaoBaniveis();
	}
	
	public void loadPunimentos() {
		CustomConfig punircfg = Main.getInstance().punirCfg;
		for(String s : punircfg.getConfig().getKeys(false)) {
			String timeS = punircfg.getConfig().getString(s + ".Tempo");
			Long time = 0L;
			if(timeS.contains("d")) time = Integer.parseInt(timeS.replace("d", "")) * 86400000L;
			if(timeS.contains("h")) time = Integer.parseInt(timeS.replace("h", "")) * 3600000L;
			if(timeS.contains("m")) time = Integer.parseInt(timeS.replace("m", "")) * 60000L;
			new Punir(PunishType.valueOf(punircfg.getConfig().getString(s + ".Tipo").toUpperCase()), time, s.replace("_", " "));
		}
	}
	
	private void loadMessage() {
		banKickMessage = "§cA sua conta está banida!\n§7%data% por §a%autor%\n\n§cMotivo: §7%motivo%\n§cExpira em:§7%expire%"
				+ "\n\n§7Saiba mais em §b%discord%§7 usando o ID §c%id%";
	}
	
	private void loadNaoBaniveis() {
		naoBaniveis = Arrays.asList("zBlackGamer");
	}
}
