package pt.com.zBPunish.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.methods.menus.PunishMenu;

public class MuteCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		if (!(s instanceof Player)) {
			if (a.length == 0) {
				s.sendMessage("§cERRO! Use /mute (Jogador).");
				return true;
			}
			return true;
		}
		Player p = (Player)s;
		if (a.length != 1) {
			p.sendMessage("§c§lERRO! §cUse, /mute (Jogador).");
			return true;
		}
		PunishMenu.Open(p, a[0], null, null, null, PunishType.MUTE);
		return true;
	}
}
