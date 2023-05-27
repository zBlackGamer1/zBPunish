package pt.com.zBPunish.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Jogador;
import pt.com.zBPunish.methods.PunishType;

public class JoinEvent implements Listener {
	@EventHandler
	void onJoin(PlayerJoinEvent e) {
		String name = e.getPlayer().getName().toLowerCase();
		Jogador j = new Jogador(e.getPlayer().getName());
		if(!j.hasPunish() || j.getPunish().getPunishType() != PunishType.BAN) return;
		if(!j.getPunish().isAtiva()) Main.cache.bans.remove(name);
		else e.getPlayer().kickPlayer(Main.manager.getBanKickMessage(j.getPunish()));
	}
}
