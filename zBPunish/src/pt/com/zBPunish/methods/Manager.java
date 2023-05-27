package pt.com.zBPunish.methods;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Historico.HistoricAction;
import pt.com.zBPunish.methods.Historico.HistoricType;
import pt.com.zBPunish.utils.zBUtils;

public class Manager {
	public void Ban(String stafferName, String TargetName, String Motivo, String Provas, Long tempo) {
		Jogador j = new Jogador(TargetName);
		if (j.hasPunish() && j.getPunish().getPunishType() == PunishType.BAN) {
			Bukkit.getPlayer(stafferName).sendMessage("§c§lERRO! §cEsse jogador já está banido!");;
			return;
		}
		String tempoS = (tempo == 1) ? " Permanente" : zBUtils.TimeToString(tempo);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§cO jogador " + TargetName + " foi banido por " + stafferName + "!");
		Bukkit.broadcastMessage("§cBanido por: " + Motivo + " - " + Provas);
		Bukkit.broadcastMessage("§cDuração:" + tempoS);
		Bukkit.broadcastMessage("");
		Date agora = new Date();
		Punish pun = new Punish(Main.cache.gerarID(), PunishType.BAN, agora, new Date(agora.getTime() + tempo),
				Motivo, stafferName, TargetName.toLowerCase(), Provas, false);
		new HistoricAction(pun, new Date(), HistoricType.BAN, stafferName);
		Player target = Bukkit.getPlayer(TargetName);
		if(target != null && target.isOnline()) {
			target.kickPlayer(getBanKickMessage(pun));
		}
	}
	
	public void Unban(Punish punish, Boolean forced, String stafferDesbanidor) {
		Main.cache.ativas.remove(punish.getPunishID());
		Main.cache.bans.remove(punish.getPunished().toLowerCase());
		if (forced) {
			punish.setUnpunished(true);
			new HistoricAction(punish, new Date(), HistoricType.UNBAN, stafferDesbanidor);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§eO jogador " + punish.getPunished() + " foi desbanido por " + stafferDesbanidor + "!");
			Bukkit.broadcastMessage("");
		}
	}
	
	public void Mute(String stafferName, String TargetName, String Motivo, String Provas, Long tempo) {
		Jogador j = new Jogador(TargetName);
		if (j.hasPunish() && j.getPunish().getPunishType() == PunishType.MUTE) {
			Bukkit.getPlayer(stafferName).sendMessage("§c§lERRO! §cEsse jogador já está mutado!");;
			return;
		}
		String tempoS = (tempo == 1) ? " Permanente" : zBUtils.TimeToString(tempo);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§cO jogador " + TargetName + " foi mutado por " + stafferName + "!");
		Bukkit.broadcastMessage("§cMutado por: " + Motivo + " - " + Provas);
		Bukkit.broadcastMessage("§cDuração:" + tempoS);
		Bukkit.broadcastMessage("");
		Date agora = new Date();
		Punish pun = new Punish(Main.cache.gerarID(), PunishType.MUTE, agora, new Date(agora.getTime() + tempo),
				Motivo, stafferName, TargetName.toLowerCase(), Provas, false);
		new HistoricAction(pun, new Date(), HistoricType.MUTE, stafferName); 
	}
	
	public void Unmute(Punish punish, Boolean forced, String stafferDesbanidor) {
		Main.cache.ativas.remove(punish.getPunishID());
		Main.cache.mutes.remove(punish.getPunished().toLowerCase());
		if (forced) {
			punish.setUnpunished(true);
			new HistoricAction(punish, new Date(), HistoricType.UNMUTE, stafferDesbanidor);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§eO jogador " + punish.getPunished() + " foi desmutado por " + stafferDesbanidor + "!");
			Bukkit.broadcastMessage("");
		}
	}
	
	public String getBanKickMessage(Punish pun) {
		SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
		return Main.cache.banKickMessage.replace("%data%", format.format(pun.getPunishDate())).replace("%autor%", pun.getPunisher()).replace("%motivo%", pun.getMotivo())
				.replace("%provas%", pun.getProva()).replace("%id%", "#" + pun.getPunishID()).replace("%expire%", pun.getTimeToExpire()).replace("%discord%", "discord.gg/hypixel");
	}
	
}
