package pt.com.zBPunish.methods.Historico;

import java.util.Date;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Punish;

public class HistoricAction {
	private Punish punish;
	private Date data;
	private HistoricType type;
	private String author;
	
	public Punish getPunish() {
		return this.punish;
	}
	
	public Date getDate() {
		return this.data;
	}
	
	public HistoricType getType() {
		return this.type;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public HistoricAction(Punish punish, Date data, HistoricType type, String author) {
		this.punish = punish;
		this.data = data;
		this.type = type;
		this.author = author;
		
		Main.cache.historico.put(new Date(Main.cache.iniciado.getTime() - data.getTime()), this);
	}
}
