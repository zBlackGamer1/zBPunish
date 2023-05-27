package pt.com.zBPunish.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;

public class TimeMenu implements Listener {
	@EventHandler
	void onClick1(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§6§7Punindo ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		NBTAPI nbt = NBTAPI.getNBT(e.getInventory().getItem(15));
		switch (e.getSlot()) {
		case 11:
			EscolherTempoMenu.Open(p, nbt);
			break;
		case 15:
			nbt.setLong("tempo", 1L);
			PunishMenu.Open(p, nbt);
			break;
			
		default:
			break;
		}
	}
	
	public static void Open(Player p, String target, String motivo, String provas, PunishType type) {
		Inventory inv = Bukkit.createInventory(null, 3*9, "§6§7Punindo " + target);
		List<String> lore = new ArrayList<>();
		lore = Arrays.asList();
		inv.setItem(11, new ItemBuilder(Material.SIGN).setLore(lore).setName("§aEscolher Duração").toItemStack());
		
		lore = Arrays.asList();
		ItemBuilder ib = new ItemBuilder(Material.ANVIL).setLore(lore).setName("§aPermanente");
		if(motivo != null) ib.setNBT("motivo", motivo);
		if(provas != null) ib.setNBT("provas", provas);
		ib.setNBT("nome", target);
		ib.setNBT("puntype", type.toString());
		inv.setItem(15, ib.toItemStack());
		
		p.openInventory(inv);
	}
}
