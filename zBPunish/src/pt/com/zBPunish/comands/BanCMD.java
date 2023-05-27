package pt.com.zBPunish.comands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.methods.menus.PunishMenu;

public class BanCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {
		if (!(s instanceof Player)) {
			if (a.length == 0) {
				s.sendMessage("§cERRO! Use /ban (Jogador).");
				return true;
			}
			return true;
		}
		Player p = (Player)s;
		if (a.length != 1) {
			p.sendMessage("§c§lERRO! §cUse, /ban (Jogador).");
			return true;
		}
		
		if (a[0].equalsIgnoreCase(p.getName())) {
			p.sendMessage("§c§lERRO! §cVocê não se pode banir a si mesmo!");
			return true;
		}
		
		if (Main.cache.naoBaniveis.contains(a[0])) {
			p.sendMessage("§c§lERRO! §cEsse jogador não pode ser banido!");
			for(Player superiores : Bukkit.getOnlinePlayers()) {
				if(superiores.hasPermission("zbpunish.admin")) p.sendMessage("§c§lzBPunish! §cO jogador §c§n" + p.getName() + "§c tentou banir §c§n" + a[0] + "§c!");
			}
			return true;
		}
		PunishMenu.Open(p, a[0], null, null, null, PunishType.BAN);
		return true;
	}
}
