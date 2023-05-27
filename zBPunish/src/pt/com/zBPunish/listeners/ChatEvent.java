package pt.com.zBPunish.listeners;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.methods.Historico.HistoricAction;
import pt.com.zBPunish.methods.Historico.HistoricType;
import pt.com.zBPunish.methods.menus.PlayerPunishes;
import pt.com.zBPunish.methods.menus.PunishMenu;
import pt.com.zBPunish.methods.menus.VerPunishMenu;
import pt.com.zBPunish.utils.NBTAPI;

public class ChatEvent implements Listener {
	@EventHandler
	void onChat(AsyncPlayerChatEvent e) {
		if(!Main.cache.digit.containsKey(e.getPlayer().getName())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		Punish pun = Main.cache.digit.get(p.getName());
		Main.cache.digit.remove(p.getName());
		if (m.equalsIgnoreCase("cancelar")) {
			p.sendMessage("§cA ação foi cancelada com sucesso!");
			return;
		}
		pun.setProva(m);
		Date agora = new Date();
		new HistoricAction(pun, agora, HistoricType.EDIT, p.getName());
		p.sendMessage("§aAs provas da punição §e§n#" + pun.getPunishID() + "§a foram alteradas!");
	}
	
	@EventHandler
	void onChat1(AsyncPlayerChatEvent e) {
		if(!Main.cache.digitID.contains(e.getPlayer().getName())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		Main.cache.digitID.remove(p.getName());
		
		try {
			Integer id = Integer.parseInt(m.split(" ")[0]);
			Punish pun = Punish.getPunish(id);
			if(pun == null) p.sendMessage("§c§lERRO! §cPunição com ID #" + id + " não encontrada!");
			else VerPunishMenu.Open(p, pun);
		} catch (NumberFormatException ex) {
			p.sendMessage("§cO ID poderá ser apenas um número!");
		}
	}
	
	@EventHandler
	void onChat7(AsyncPlayerChatEvent e) {
		if(!Main.cache.digitPlayer.contains(e.getPlayer().getName())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		Main.cache.digitPlayer.remove(p.getName());
		PlayerPunishes.Open(p, m, 1);
	}
	
	@EventHandler
	void onChat2(AsyncPlayerChatEvent e) {
		if(!Main.cache.digit1.containsKey(e.getPlayer().getName())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		NBTAPI nbt = Main.cache.digit1.get(p.getName());
		Main.cache.digit1.remove(p.getName());
		
		if(m.equalsIgnoreCase("cancelar")) {
			PunishMenu.Open(p, nbt);
			return;
		}
		nbt.setString("motivo", m);
		PunishMenu.Open(p, nbt);
	}
	
	@EventHandler
	void onChat3(AsyncPlayerChatEvent e) {
		if(!Main.cache.digit2.containsKey(e.getPlayer().getName())) return;
		e.setCancelled(true);
		String m = e.getMessage();
		Player p = e.getPlayer();
		NBTAPI nbt = Main.cache.digit2.get(p.getName());
		
		if(m.equalsIgnoreCase("cancelar")) {
			Main.cache.digit2.remove(p.getName());
			PunishMenu.Open(p, nbt);
			return;
		}
		
		if (!m.startsWith("https://")) {
			p.sendMessage("§c§lERRO! §cAs provas deverão começar com: \"https://\"");
			return;
		}
		Main.cache.digit2.remove(p.getName());
		nbt.setString("provas", m);
		PunishMenu.Open(p, nbt);
	}
}
