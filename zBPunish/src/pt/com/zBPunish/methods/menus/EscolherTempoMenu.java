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
import pt.com.zBPunish.utils.zBUtils;

public class EscolherTempoMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§d§7Punindo ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		NBTAPI nbt = NBTAPI.getNBT(e.getInventory().getItem(22));
		int slot = e.getSlot();
		
		if (slot == 22) {
			switch (e.getAction()) {
			case PICKUP_ALL:
				if(!nbt.hasKey("tempo") || nbt.getLong("tempo") == 0) return;
				PunishMenu.Open(p, nbt);
				break;
			case PICKUP_HALF:
				nbt.setLong("tempo", (long) 0);
				Open(p, nbt);
				break;
			default: break;
			}
			return;
		}
		
		if(check(slot)){
			switch (e.getAction()) {
			case PICKUP_ALL:
				nbt.setLong("tempo", TimeFinal(nbt, getTime(slot, 1), toAdd(slot)));
				Open(p, nbt);
				break;
			case PICKUP_HALF:
				nbt.setLong("tempo", TimeFinal(nbt, getTime(slot, 10), toAdd(slot)));
				Open(p, nbt);
				break;
			case DROP_ONE_SLOT:
				if(slot == 11 || slot == 15) nbt.setLong("tempo", TimeFinal(nbt, getTime(slot, 30), toAdd(slot)));
				else nbt.setLong("tempo", TimeFinal(nbt, getTime(slot, 60), toAdd(slot)));
				Open(p, nbt);
				break;
			default: break;
			}
			return;
		}
		
	}
	@SuppressWarnings("deprecation")
	private static void Open(Player p, String target, String motivo, String provas, Long time, PunishType type) {
		String name = (Bukkit.getOfflinePlayer(target) != null) ? Bukkit.getOfflinePlayer(target).getName() : target;
		Inventory inv = Bukkit.createInventory(null, 5*9, "§d§7Punindo " + name);
		List<String> lore = new ArrayList<>();
		
		if(time != 0) lore = Arrays.asList("", "§a │§e Tempo:§7" + zBUtils.TimeToString(time), "", "§eBotão Esquerdo: §7Setar Tempo", "§eBotão Direito: §7Resetar Tempo");
		else lore = Arrays.asList("§cAdicione tempo para prosseguir!");
		ItemBuilder ib = new ItemBuilder(Material.WATCH).setName("§eTempo de Punição");
		if(motivo != null) ib.setNBT("motivo", motivo);
		if(provas != null) ib.setNBT("provas", provas);
		if(time != null) ib.setNBT("tempo", time);
		ib.setNBT("nome", name);
		ib.setNBT("puntype", type.toString());
		inv.setItem(22, ib.setLore(lore).toItemStack());
		
		lore = Arrays.asList("§a │ §e Botão Esquerdo: §71 Dia", "§a │ §e Botão Direito: §710 Dias", "§a │ §e Botão Q: §730 Dias");
		ItemBuilder ib1 = new ItemBuilder("4e4b8b8d2362c864e062301487d94d3272a6b570afbf80c2c5b148c954579d46").setLore(lore);
		ItemBuilder ib2 = new ItemBuilder("b056bc1244fcff99344f12aba42ac23fee6ef6e3351d27d273c1572531f").setLore(lore);
		inv.setItem(11, ib1.setName("§cDiminuir Dias").toItemStack());
		inv.setItem(15, ib2.setName("§aAumentar Dias").toItemStack());
		
		lore = Arrays.asList("§a │ §e Botão Esquerdo: §71 Hora", "§a │ §e Botão Direito: §710 Horas", "§a │ §e Botão Q: §760 Horas");
		ib1.setLore(lore);
		ib2.setLore(lore);
		
		inv.setItem(20, ib1.setName("§cDiminuir Horas").toItemStack());
		inv.setItem(24, ib2.setName("§aAumentar Horas").toItemStack());
		
		lore = Arrays.asList("§a │ §e Botão Esquerdo: §71 Minuto", "§a │ §e Botão Direito: §710 Minutos", "§a │ §e Botão Q: §760 Minutos");
		ib1.setLore(lore);
		ib2.setLore(lore);
		
		inv.setItem(29, ib1.setName("§cDiminuir Minutos").toItemStack());
		inv.setItem(33, ib2.setName("§aAumentar Minutos").toItemStack());
		p.openInventory(inv);
	}
	
	private Long getTime(int slot, int multiply) {
		long l = 0;
		switch (slot) {
		case 11:
		case 15:
			l = 86400000;
			break;
		case 20:
		case 24:
			l = 3600000;
			break;
		case 29:
		case 33:
			l = 60000;
			break;

		default:
			break;
		}
		return l * multiply;
	}
	
	private Long TimeFinal(NBTAPI nbt, Long time, Boolean toAdd) {
		Long time1 = nbt.getLong("tempo");
		if(toAdd) return time1 + time;
		if(time > time1) return (long) 0;
		return time1 - time;
	}
	
	private boolean toAdd(int slot) {
		return Arrays.asList(15, 24, 33).contains(slot);
	}
	
	private boolean check(int slot) {
		return Arrays.asList(11, 15, 20, 24, 29, 33).contains(slot);
	}
	
	public static void Open(Player p, NBTAPI nbt) {
		Open(p, nbt.getString("nome"), (nbt.hasKey("motivo") ? nbt.getString("motivo") : null),
				(nbt.hasKey("provas") ? nbt.getString("provas") : null), (nbt.hasKey("tempo") ? nbt.getLong("tempo") : 0), PunishType.valueOf(nbt.getString("puntype")));
	}
}
