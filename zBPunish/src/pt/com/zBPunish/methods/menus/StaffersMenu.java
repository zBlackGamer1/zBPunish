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
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;

public class StaffersMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Punições - Staffers")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		int page = getPage(e.getInventory());
		switch (e.getSlot()) {
		case 53:
			Open(p, page + 1);
			break;
		case 45:
			if(page == 1) MainMenu.Open(p);
			else Open(p, page - 1);
			break;

		default:
			if (NBTAPI.getNBT(e.getCurrentItem()).hasKey("staffer_name")) {
				StaffersPunishesMenu.Open(p, NBTAPI.getNBT(e.getCurrentItem()).getString("staffer_name"), 1);
			}
			break;
		}
	}
	
	public static void Open(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 6*9, "§7Punições - Staffers");
		List<String> lore;
		lore = Arrays.asList("§7Nesta página serão exibidos", "§7todos os staffers que já", "§7fizeram pelo menos 1 punição.");
		ItemStack item = new ItemBuilder("f59eaaf0107a3ca8ad55780bf68bc8fd1cd9c28b095d6d97fc066ba2043c175a").setLore(lore).setName("§eSobre").toItemStack();
		NBTAPI nbt = NBTAPI.getNBT(item);
		nbt.setInt("staffers_page", page);
		inv.setItem(49, nbt.getItem());
		String name = (page == 1) ? "§cVoltar" : "§cPágina Anterior";
		inv.setItem(45, new ItemBuilder(Material.ARROW).setName(name).toItemStack());
		if (Main.cache.staffers.size() == 0) {
			lore = Arrays.asList("§7Não foi encontrado nenhum staffer.");
			inv.setItem(22, new ItemBuilder(Material.WEB).setName("§cNada").setLore(lore).toItemStack());
			p.openInventory(inv);
			return;
		}
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		if(Main.cache.staffers.size() > fim) inv.setItem(53, new ItemBuilder(Material.ARROW).setName("§aPróxima Página").toItemStack());
		
		for(String staffer : Main.cache.staffers.keySet()) {
			if (i < inicio) {
				i++;
				continue;
			}
			if(i >= fim) break;
			slot = nextSlot(slot);
			ItemBuilder head = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(staffer);
			lore = Arrays.asList("§7Punições aplicadas: §e" + Main.cache.staffers.get(staffer).size(), "", "§aClique para ver punições!");
			head.setName("§e" + staffer).setLore(lore);
			NBTAPI nbt1 = NBTAPI.getNBT(head.toItemStack());
			nbt1.setString("staffer_name", staffer);
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
