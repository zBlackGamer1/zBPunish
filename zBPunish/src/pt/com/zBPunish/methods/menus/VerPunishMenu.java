package pt.com.zBPunish.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;
import pt.com.zBPunish.utils.UltimateFancy;
import pt.com.zBPunish.utils.zBUtils;

public class VerPunishMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§7Punições - ID ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 33:
			Punish pun = Punish.getPunish(NBTAPI.getNBT(e.getCurrentItem()).getInt("punish_id"));
			if(e.getAction() == InventoryAction.PICKUP_ALL) {
				p.closeInventory();
				p.sendMessage("§eProvas da Punição #" + pun.getPunishID() + ":§7 " + pun.getProva());
			}
			if(e.getAction() == InventoryAction.PICKUP_HALF) {
				p.closeInventory();
				if(pun.getPunisher().equals(p.getName())) {
					Main.cache.digit.put(p.getName(), pun);
					p.sendMessage(new String[] {
							"",
							"§7Você está alterando as provas da punição §a§n#" + pun.getPunishID(),
							"§7Digite as novas provas, digite §c§ncancelar§7 para sair.",
							""
					});
				} else p.sendMessage("§cApenas o responsável por esta punição pode edita-la!");
			}
			break;
			
		case 31:
			Punish pun1 = Punish.getPunish(NBTAPI.getNBT(e.getCurrentItem()).getInt("punish_id"));
			UltimateFancy msg = new UltimateFancy();
			p.sendMessage("§7Você está prestes a perdoar a punição de §e" + pun1.getPunished() + "§7!");
			msg.text("§aClique ").next();
			String cmd = (pun1.getPunishType() == PunishType.BAN) ? "/unban " : "/unmute ";
			msg.text("§7§lAQUI").hoverShowText("§aClique para perdoar §7" + pun1.getPunished()).clickRunCmd(cmd + pun1.getPunished()).next();
			msg.text("§r§a para confirmar.");
			msg.send(p);
			p.closeInventory();
			break;
			
		default:
			break;
		}
	}
	
	public static void Open(Player p, Punish punish) {
		Inventory inv = Bukkit.createInventory(null, 5*9, "§7Punições - ID " + punish.getPunishID());
		List<String> lore = new ArrayList<>();
		ItemBuilder punishedhead = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(punish.getPunished());
		lore = Arrays.asList("§7" + punish.getPunished());
		inv.setItem(10, punishedhead.setName("§eJogador Punido").setLore(lore).toItemStack());
		
		if (punish.getPunisher().equalsIgnoreCase("console")) {
			lore = Arrays.asList("§7" + punish.getPunisher());
			inv.setItem(16, new ItemBuilder("bd9f18c9d85f92f72f864d67c1367e9a45dc10f371549c46a4d4dd9e4f13ff4").setName("§eStaffer Responsável").setLore(lore).toItemStack());
		} else {
			ItemBuilder punisherhead = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(punish.getPunisher());
			lore = Arrays.asList("§7" + punish.getPunisher());
			inv.setItem(16, punisherhead.setName("§eStaffer Responsável").setLore(lore).toItemStack());
		}
		
		lore = Arrays.asList("§7" + punish.getMotivo());
		inv.setItem(14, new ItemBuilder("fbeb2546564af4df7f7f589423f68102dea69cd4466b0583c474e5ac693b2b99").setLore(lore).setName("§eMotivo").toItemStack());
		
		lore = Arrays.asList("§7" + punish.getPunishType().getName());
		inv.setItem(12, new ItemBuilder("abbe86f0040cb46de53c0cfe1ffecd4fa11f804e26f8c2ce6d864d0020027009").setLore(lore).setName("§eTipo de Punição").toItemStack());
		
		if(!punish.isUnpunished())lore = Arrays.asList("§a│ §ePunido há:§7" + zBUtils.TimeToString(punish.getPunishDate()), "§a│ §eExpira em:§7" + punish.getTimeToExpire());
		else lore = Arrays.asList("§a│ §ePunido há:§7" + zBUtils.TimeToString(punish.getPunishDate()), "§a│ §eExpira em:§7" + punish.getTimeToExpire(), "§c§lOBS: §cEsta punição foi perdoada");
		inv.setItem(29, new ItemBuilder(Material.WATCH).setLore(lore).setName("§eTempo").toItemStack());
		
		lore = Arrays.asList("§7Perdoe esta punição.", "", "§cClique para perdoar.");
		NBTAPI nbt = NBTAPI.getNBT(new ItemBuilder("d65c06e3b9f1fff95431b31a8c0551b046ee0a5defd7f01fa51d4db0531c2b74").setLore(lore).setName("§ePerdoar").toItemStack());
		nbt.setInt("punish_id", punish.getPunishID());
		inv.setItem(31, nbt.getItem());
		
		lore = Arrays.asList("§7Botão Esquerdo §eVer Provas", "§7Botão Direito §eEditar Provas");
		NBTAPI nbt1 = NBTAPI.getNBT(new ItemBuilder(Material.PAPER).setLore(lore).setName("§eProvas").toItemStack());
		nbt1.setInt("punish_id", punish.getPunishID());
		inv.setItem(33, nbt1.getItem());
		
		p.openInventory(inv);
	}
}