package pt.com.zBPunish.listeners;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Jogador;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.utils.UltimateFancy;

public class MuteListener implements Listener {
	private List<String> cmdBlocked;
	@EventHandler
	void onChat(AsyncPlayerChatEvent e) {
		Jogador j = new Jogador(e.getPlayer().getName());
		for(String s : Main.cache.mutes.keySet()) System.out.println("KEY: " + s);
		System.out.println((Main.cache.mutes.containsKey(j.getNick())) + "");
		if(j.hasPunish() && j.getPunish().getPunishType() == PunishType.MUTE && j.getPunish().isAtiva()) {
			Punish p = j.getPunish();
			UltimateFancy msg = new UltimateFancy();
			e.setCancelled(true);
			e.getPlayer().sendMessage(new String[] {
					"",
					"§cVocê foi mutado por " + p.getMotivo() + " pelo " + p.getPunisher() + ".",
			});
			msg.text("§cPeça uma revisão usando o ID #" + p.getPunishID() + " clicando ").next();
			try {
				msg.text("§c§lAQUI").hoverShowText("§aClique entrar no discord!").clickOpenURL(new URL("https://discord.gg/hypixel")).next();
			} catch (MalformedURLException e1) {}
			msg.text("§r§c.");
			msg.send(e.getPlayer());
			e.getPlayer().sendMessage("");
		}
	}
	
	@EventHandler
	void onCommand(PlayerCommandPreprocessEvent e) {
		Jogador j = new Jogador(e.getPlayer().getName());
		if(j.hasPunish() && j.getPunish().getPunishType() == PunishType.MUTE && j.getPunish().isAtiva() && cmdBlocked.contains(e.getMessage().split(" ")[0].toLowerCase())) {
			Punish p = j.getPunish();
			UltimateFancy msg = new UltimateFancy();
			e.setCancelled(true);
			e.getPlayer().sendMessage(new String[] {
					"",
					"§cVocê foi mutado por " + p.getMotivo() + " pelo " + p.getPunisher() + ".",
			});
			msg.text("§cPeça uma revisão usando o ID #" + p.getPunishID() + " clicando ").next();
			try {
				msg.text("§c§lAQUI").hoverShowText("§aClique entrar no discord!").clickOpenURL(new URL("https://discord.gg/hypixel")).next();
			} catch (MalformedURLException e1) {}
			msg.text("§r§c.");
			msg.send(e.getPlayer());
			e.getPlayer().sendMessage("");
		}
	}
	
	
	public MuteListener() {
		cmdBlocked = Arrays.asList("/g", "/l", "/tell", "/r", "/t", "/global", "/local");
	}
}
