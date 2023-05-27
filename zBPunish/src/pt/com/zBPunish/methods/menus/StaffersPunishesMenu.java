package pt.com.zBPunish.methods.menus;

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

public class StaffersPunishesMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§e§0§7Punições - ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		int page = getPage(e.getInventory());
		String staffer = NBTAPI.getNBT(e.getInventory().getItem(49)).getString("staffer");
		switch (e.getSlot()) {
		case 53:
			Open(p, staffer, page + 1);
			break;
		case 45:
			if(page == 1) StaffersMenu.Open(p, 1);
			else Open(p, staffer, page - 1);
			break;

		default:
			if (NBTAPI.getNBT(e.getCurrentItem()).hasKey("punish_id")) {
				VerPunishMenu.Open(p, Punish.getPunish(NBTAPI.getNBT(e.getCurrentItem()).getInt("punish_id")));
			}
			break;
		}
	}
	
	public static void Open(Player p, String staffer, int page) {
		Inventory inv = Bukkit.createInventory(null, 6*9, "§e§0§7Punições - " + staffer);
		List<String> lore;
		lore = Arrays.asList("§7Estas são todas as punições", "§7aplicadas por " + staffer + "!");
		ItemStack item = new ItemBuilder("f59eaaf0107a3ca8ad55780bf68bc8fd1cd9c28b095d6d97fc066ba2043c175a").setLore(lore).setName("§eSobre").toItemStack();
		NBTAPI nbt = NBTAPI.getNBT(item);
		nbt.setInt("staffers_page", page);
		nbt.setString("staffer", staffer);
		inv.setItem(49, nbt.getItem());
		String name = (page == 1) ? "§cVoltar" : "§cPágina Anterior";
		inv.setItem(45, new ItemBuilder(Material.ARROW).setName(name).toItemStack());
		if (Main.cache.staffers.size() == 0) {
			lore = Arrays.asList("§7Não foi encontrada nenhuma punição.");
			inv.setItem(22, new ItemBuilder(Material.WEB).setName("§cNada").setLore(lore).toItemStack());
			p.openInventory(inv);
			return;
		}
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		if(Main.cache.staffers.size() > fim) inv.setItem(53, new ItemBuilder(Material.ARROW).setName("§aPróxima Página").toItemStack());
		
		for(Punish pun : Main.cache.staffers.get(staffer)) {
			if (i < inicio) {
				i++;
				continue;
			}
			if(i >= fim) break;
			slot = nextSlot(slot);
			ItemBuilder head = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(pun.getPunished());
			lore = Arrays.asList("", "§7Jogador punido: §e" + pun.getPunished(), "§7Tipo de Punição: §e" + pun.getPunishType().getName(), "", "§aClique para ver detalhes!");
			head.setName("§e" + staffer).setLore(lore);
			NBTAPI nbt1 = NBTAPI.getNBT(head.toItemStack());
			nbt1.setInt("punish_id", pun.getPunishID());
			inv.setItem(slot, nbt1.getItem());
			i++;
		}
		
		p.openInventory(inv);
	}
	
	private static Integer getPage(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(49)).getInt("staffers_page");
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
