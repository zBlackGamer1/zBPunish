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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Punish;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;

public class PlayerPunishes implements Listener {
	
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§5§d§7Punições - ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 45:
			if(getPage(e.getInventory()) == 1) MainMenu.Open(p);
			else Open(p, getTarget(e.getInventory()), getPage(e.getInventory()) - 1);
			break;
		case 53:
			Open(p, getTarget(e.getInventory()), getPage(e.getInventory()) + 1);
			break;
		default:
			if (NBTAPI.getNBT(e.getCurrentItem()).hasKey("punish_id")) {
				VerPunishMenu.Open(p, Punish.getPunish(NBTAPI.getNBT(e.getCurrentItem()).getInt("punish_id")));
			}
			break;
		}
	}
	
	
	public static void Open(Player p, String target, int page) {
		List<Punish> playerPunishes = new ArrayList<>();
		for(Punish pun : Main.cache.todas.values()) if(pun.getPunished().equalsIgnoreCase(target)) playerPunishes.add(pun);
		
		Inventory inv = Bukkit.createInventory(null, 6*9, "§5§d§7Punições - " + target);
		List<String> lore = new ArrayList<>();
		String name = (page == 1) ? "§cSair" : "§cPágina Anterior";
		NBTAPI voltar = NBTAPI.getNBT(new ItemBuilder(Material.ARROW).setName(name).toItemStack());
		voltar.setInt("playerpunishes_page", page);
		voltar.setString("playerpunishes_target", target);
		inv.setItem(45, voltar.getItem());
		
		lore = Arrays.asList("§7Você está vendo o histórico", "§7de punições de §7§n" + target + "§7!");
		ItemStack item = new ItemBuilder("f59eaaf0107a3ca8ad55780bf68bc8fd1cd9c28b095d6d97fc066ba2043c175a").setLore(lore).setName("§eSobre").toItemStack();
		inv.setItem(49, item);
		if (playerPunishes.size() == 0) {
			lore = Arrays.asList("§7Este jogador não tem ", "§7antecedentes de punições!");
			inv.setItem(22, new ItemBuilder(Material.WEB).setName("§cNada").setLore(lore).toItemStack());
			p.openInventory(inv);
			return;
		}
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		
		if(Main.cache.ativasCount() > fim) inv.setItem(53, new ItemBuilder(Material.ARROW).setName("§aPróxima Página").toItemStack());
		
		for(Punish pun : playerPunishes) {
			if (i < inicio) {
				i++;
				continue;
			}
			if(i >= fim) break;
			slot = nextSlot(slot);
			ItemBuilder head = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(pun.getPunished());
			String ativa = (pun.isAtiva()) ? "§aSim" : "§cNão";
			lore = Arrays.asList("", "§7 Motivo: §e" + pun.getMotivo(), "§7 Punidor: §e" + pun.getPunisher(), "§7 Ativa: " + ativa, "", "§aClique para ver detalhes!");
			head.setName("§ePunição #" + pun.getPunishID()).setLore(lore);
			NBTAPI nbt = NBTAPI.getNBT(head.toItemStack());
			nbt.setInt("punish_id", pun.getPunishID());
			inv.setItem(slot, nbt.getItem());
			i++;
		}
		p.openInventory(inv);
	}
	
	private static Integer getPage(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(45)).getInt("playerpunishes_page");
	}
	
	private static String getTarget(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(45)).getString("playerpunishes_target");
	}
	
	private static Integer nextSlot(int i) {
		switch (i) {
		case 16:
			return 19;
		case 25:
			return 28;
		case 34:
			return null;

		default:
			return i + 1;
		}
	}
}
