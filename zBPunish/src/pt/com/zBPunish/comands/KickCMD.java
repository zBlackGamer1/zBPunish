package pt.com.zBPunish.comands;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.methods.Historico.HistoricAction;
import pt.com.zBPunish.methods.Historico.HistoricType;

public class KickCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		s.sendMessage((Main.cache.ativas.containsKey(Punish.getPunish(Integer.parseInt(a[0])).getPunishID())) ? "contem" : "NOT contem");
		if (!(s instanceof Player)) {
			if (a.length == 0) {
				s.sendMessage("§cERRO! Use /kick (Jogador) (Motivo).");
				return true;
			}
			Player t = Bukkit.getPlayer(a[0]);
			if (t == null) {
				s.sendMessage("§c§lERRO! §cEsse jogador não está online.");
				return true;
			}
			new HistoricAction(null, new Date(), HistoricType.KICK, "Console");
			s.sendMessage("§aSucesso! O jogador " + t.getName() + " foi kickado!");
			String motivo = Main.config.kick_motdefault;
			a[0] = "";
			if(a.length > 1) motivo = String.join(" ", a).substring(1);
			if(Main.config.kick_alerta_ativo) for(String st : Main.config.kick_alerta) Bukkit.broadcastMessage(st.replace("%motivo%", motivo).replace("%jogador%", t.getName()).replace("%autor%", "Console")); 
			t.kickPlayer(Main.config.kick_formato.replace("%motivo%", motivo).replace("%autor%", "Console"));
			return true;
		}
		Player p = (Player)s;
		if (a.length == 0) {
			p.sendMessage("§c§lERRO! §cUse /kick (Jogador) (Motivo).");
			return true;
		}
		Player t = Bukkit.getPlayer(a[0]);
		if (t == null) {
			p.sendMessage("§c§lERRO! §cEsse jogador não está online.");
			return true;
		}
		new HistoricAction(null, new Date(), HistoricType.KICK, p.getName());
		p.sendMessage("§a§lSucesso! §aO jogador §a§n" + t.getName() + "§a foi kickado!");
		String motivo = Main.config.kick_motdefault;
		a[0] = "";
		if(Main.config.kick_alerta_ativo) for(String st : Main.config.kick_alerta) Bukkit.broadcastMessage(st.replace("%motivo%", motivo).replace("%jogador%", t.getName()).replace("%autor%", p.getName()));
		if(a.length > 1) motivo = String.join(" ", a).substring(1);
		t.kickPlayer(Main.config.kick_formato.replace("%motivo%", motivo).replace("%autor%", p.getName()));
		return true;
	}
}
