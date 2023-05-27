package pt.com.zBPunish.methods.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Historico.HistoricMenu;
import pt.com.zBPunish.utils.ItemBuilder;


public class MainMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase("§7Punições - Principal")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 11:
			AtivasMenu.Open(p, 1);
			break;
			
		case 12:
			HistoricMenu.Open(p, 1);
			break;
			
		case 13:
			p.closeInventory();
			if(e.getAction() == InventoryAction.PICKUP_ALL) {
				Main.cache.digitID.add(p.getName());
				p.sendMessage("§eDigite o ID da punição que procura!");
			}
			if (e.getAction() == InventoryAction.PICKUP_HALF) {
				Main.cache.digitPlayer.add(p.getName());
				p.sendMessage("§eDigite o nome do jogador que procura!");
			}
			break;
			
		case 14:
			StaffersMenu.Open(p, 1);
			break;
			
		case 31:
			p.closeInventory();
			break;

		default:
			break;
		}
	}
	
	public static void Open(Player p) {
		int online = 0;
		for(Player staff : Bukkit.getOnlinePlayers()) if(staff.hasPermission("zbpunish.use") || staff.hasPermission("zbpunish.mute") ||
				staff.hasPermission("zbpunish.ban") || staff.hasPermission("zbpunish.kick")) online++;
		Inventory inv = Bukkit.createInventory(null, 4*9, "§7Punições - Principal");
		List<String> lore = Arrays.asList("§7Veja todas as punições ", "§7ativas neste momento.  ", "", "§aClique para ver!  ");
		inv.setItem(11, new ItemBuilder("4cd3c45d7b8384e8a1963e4da0ae6b2daeb2a3e97ac7a28f9eb3d3959725799f").setLore(lore).setName("§ePunições").toItemStack());
		
		lore = Arrays.asList("§7Controle o histórico ", "§7das punições executadas.  ", "", "§aClique para ver!  ");
		inv.setItem(12, new ItemBuilder("3ca93cc078115209a50b63f958197ac7a73fad3609ebd7ef6601f3ffd4166c68").setLore(lore).setName("§eHistórico").toItemStack());
		
		lore = Arrays.asList("§7Procure uma punição pelo ID  ", "§7ou procure pelo histórico  ", "§7de punições de um jogador!   ",
				"", "§7Clique Direito: §eProcurar Jogador  ", "§7Clique Esquerdo: §eProcurar por ID  ");
		inv.setItem(13, new ItemBuilder(Material.EYE_OF_ENDER).setLore(lore).setName("§ePesquisar").toItemStack());
		
		lore = Arrays.asList("§7Veja todos os staffers  ", "§7e respectivas punições.  ", "", "§aClique para ver!  ");
		inv.setItem(14, new ItemBuilder("9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f").setLore(lore).setName("§eStaffers").toItemStack());
		
		lore = Arrays.asList("§e │  §fTotal de Punições: §e" + Main.cache.todasCount(), "§e │  §fPunições Ativas: §e" + Main.cache.ativasCount(), "§e │  §fStaffers Online: §e" + online);
		inv.setItem(15, new ItemBuilder("f59eaaf0107a3ca8ad55780bf68bc8fd1cd9c28b095d6d97fc066ba2043c175a").setLore(lore).setName("§eInformações").toItemStack());
		
		inv.setItem(31, new ItemBuilder(Material.ARROW).setName("§cSair").toItemStack());
		p.openInventory(inv);
	}
}
