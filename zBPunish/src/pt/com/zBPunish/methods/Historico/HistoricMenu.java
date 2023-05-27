package pt.com.zBPunish.methods.Historico;

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
import pt.com.zBPunish.methods.menus.MainMenu;
import pt.com.zBPunish.methods.menus.VerPunishMenu;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;
import pt.com.zBPunish.utils.zBUtils;

public class HistoricMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Punições - Histórico")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 45:
			if(getPage(e.getInventory()) == 1) MainMenu.Open(p);
			else Open(p, getPage(e.getInventory()) - 1);
			break;
		case 53:
			Open(p, getPage(e.getInventory()) + 1);
			break;
		default:
			if (NBTAPI.getNBT(e.getCurrentItem()).hasKey("punish_id")) {
				VerPunishMenu.Open(p, Punish.getPunish(NBTAPI.getNBT(e.getCurrentItem()).getInt("punish_id")));
			}
			break;
		}
	}
	
	public static void Open(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 6*9, "§7Punições - Histórico");
		List<String> lore = new ArrayList<>();
		String name = (page == 1) ? "§cSair" : "§cPágina Anterior";
		NBTAPI voltar = NBTAPI.getNBT(new ItemBuilder(Material.ARROW).setName(name).toItemStack());
		voltar.setInt("ativas_page", page);
		inv.setItem(45, voltar.getItem());
		if (Main.cache.historico.size() == 0) {
			lore = Arrays.asList("§7Não exite nenhuma ", "§7punição ativa no momento.");
			inv.setItem(22, new ItemBuilder(Material.WEB).setName("§cNada").setLore(lore).toItemStack());
			p.openInventory(inv);
			return;
		}
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		
		if(Main.cache.historico.size() > fim) inv.setItem(53, new ItemBuilder(Material.ARROW).setName("§aPróxima Página").toItemStack());
		for(HistoricAction action : Main.cache.historico.values()) {
			if (i < inicio) {
				i++;
				continue;
			}
			if(i >= fim) break;
			slot = nextSlot(slot);
			ItemBuilder head = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(action.getAuthor());
			if(action.getType() != HistoricType.KICK) lore = Arrays.asList("", "§7Ação: §e" + action.getType().getName(),"§7Autor: §e" + action.getAuthor(), "§7Realizado há:§e" + zBUtils.TimeToString(action.getDate()), "", "§aClique para ver detalhes!");
			else lore = Arrays.asList("", "§7Ação: §e" + action.getType().getName(),"§7Autor: §e" + action.getAuthor(), "§7Realizado há:§e" + zBUtils.TimeToString(action.getDate()), "");
			head.setName("§eHistórico").setLore(lore);
			if (action.getType() != HistoricType.KICK) {
				Punish pun = action.getPunish();
				NBTAPI nbt = NBTAPI.getNBT(head.toItemStack());
				nbt.setInt("punish_id", pun.getPunishID());
				inv.setItem(slot, nbt.getItem());
			} else inv.setItem(slot, head.toItemStack());
			i++;
		}
		
		p.openInventory(inv);
	}
	
	private static Integer getPage(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(45)).getInt("ativas_page");
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
