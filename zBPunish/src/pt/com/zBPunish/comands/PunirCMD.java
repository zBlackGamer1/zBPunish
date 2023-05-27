package pt.com.zBPunish.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.methods.menus.PunirMenu;

public class PunirCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		if (!(s instanceof Player)) {
			s.sendMessage("§cERRO! Comando apenas para jogadores.");
		}
		Player p = (Player)s;
		if (a.length != 1) {
			p.sendMessage("§c§lERRO! §cUse /punir (Jogador).");
			return true;
		}
		String target = a[0];
		
		if (p.hasPermission("zbpunish.admin")) {
			PunirMenu.Open(p, target, 1, null);
			return true;
		}
		if (p.hasPermission("zbpunish.ban") && p.hasPermission("zbpunish.mute")) {
			PunirMenu.Open(p, target, 1, null);
			return true;
		}
		if (p.hasPermission("zbpunish.mute") && !p.hasPermission("zbpunish.ban")) {
			PunirMenu.Open(p, target, 1, PunishType.MUTE);
			return true;
		}
		return true;
	}

}
