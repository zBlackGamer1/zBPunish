package pt.com.zBPunish.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Jogador;
import pt.com.zBPunish.methods.PunishType;

public class UnbanCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		if (!(s instanceof Player)) {
			if (a.length == 0) {
				s.sendMessage("§cERRO! Use /unban (Jogador).");
				return true;
			}
			Jogador j = new Jogador(a[0]);
			if (!j.hasPunish() || j.getPunish().getPunishType() != PunishType.BAN) {
				s.sendMessage("§c§lERRO! §cEsse jogador não está banido!");
				return true;
			}
			Main.manager.Unban(j.getPunish(), true, "Console");
			return true;
		}
		Player p = (Player)s;
		if (a.length != 1) {
			p.sendMessage("§c§lERRO! §cUse, /unban (Jogador).");
			return true;
		}
		Jogador j = new Jogador(a[0]);
		if (!j.hasPunish() || j.getPunish().getPunishType() != PunishType.BAN) {
			p.sendMessage("§c§lERRO! §cEsse jogador não está banido!");
			return true;
		}
		Main.manager.Unban(j.getPunish(), true, p.getName());
		return true;
	}
}
