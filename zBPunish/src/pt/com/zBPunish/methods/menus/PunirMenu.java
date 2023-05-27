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
import pt.com.zBPunish.methods.Punir;
import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;
import pt.com.zBPunish.utils.zBUtils;

public class PunirMenu implements Listener {
	
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§a§r§d§7Punir - ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		switch (e.getSlot()) {
		case 45:
			Open(p, getTarget(inv), getPage(inv) - 1, getType(inv));
			break;
		case 53:
			Open(p, getTarget(inv), getPage(inv) + 1, getType(inv));
			break;
			
		case 48:
			if(getType(inv) != null) Open(p, getTarget(inv), 1, null);
			break;
		case 49:
			if(getType(inv) == null || getType(inv) != PunishType.MUTE) Open(p, getTarget(inv), 1, PunishType.MUTE);
			break;
		case 50:
			if(getType(inv) == null || getType(inv) != PunishType.BAN) Open(p, getTarget(inv), 1, PunishType.BAN);
			break;
		default:
			if (NBTAPI.getNBT(e.getCurrentItem()).hasKey("punir_nome")) {
				NBTAPI nbt = NBTAPI.getNBT(e.getCurrentItem());
				Long time = nbt.getLong("punir_tempo");
				if(time == 0) time = 1L;
				PunishMenu.Open(p, getTarget(inv), nbt.getString("punir_nome"), null, time, PunishType.valueOf(nbt.getString("punir_tipo")));
			}
			break;
		}
	}
	
	public static void Open(Player p, String target, int page, PunishType type) {
		if(!verifyPerm(p, type)) {
			p.closeInventory();
			p.sendMessage("§cVocê não tem permissão para isso!");
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 6*9, "§a§r§d§7Punir - " + target);
		List<String> lore = new ArrayList<>();
		ItemBuilder todos = new ItemBuilder("be5a01fb26a10afd4742bdf6a1e360da010040ac4338371165309d4cea6ddbdf").setName("§aTodas");
		ItemBuilder mutes = new ItemBuilder("63517bc3ec32869d939c89d2961e30b7161eacd9255bec313be23960221d41b4").setName("§aMutes");
		ItemBuilder bans = new ItemBuilder("9e42f682e430b55b61204a6f8b76d5227d278ed9ec4d98bda4a7a4830a4b6").setName("§aBans");
		lore = Arrays.asList("§7Veja todas as punições  ", "§7predefinidas existentes.  ", "", "§aClique para ir.");
		inv.setItem(48, todos.setLore(lore).toItemStack());
		lore = Arrays.asList("§7Filtre as punições predefinidas  ", "§7para ver apenas mutes.  ", "", "§aClique para ir.");
		inv.setItem(49, mutes.setLore(lore).toItemStack());
		lore = Arrays.asList("§7Filtre as punições predefinidas  ", "§7para ver apenas bans.  ", "", "§aClique para ir.");
		inv.setItem(50, bans.setLore(lore).toItemStack());
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		boolean full = false;
		if (type == null) {
			for(Punir punir : Main.cache.punimentos.values()) {
				if (i < inicio) {
					i++;
					continue;
				}
				if(i >= fim) {
					full = true;
					break;
				}
				slot = nextSlot(slot);
				inv.setItem(slot, getItem(punir));
			}
			inv.setItem(48, todos.removeLoreLine("§aClique para ir.").addLoreLine("§aVocê está aqui!").toItemStack());
		}
		if (type != null && type == PunishType.BAN) {
			for(Punir punir : Main.cache.punimentos.values()) {
				if(punir.getType() != PunishType.BAN) continue;
				if (i < inicio) {
					i++;
					continue;
				}
				if(i >= fim) {
					full = true;
					break;
				}
				slot = nextSlot(slot);
				inv.setItem(slot, getItem(punir));
			}
			inv.setItem(50, bans.removeLoreLine("§aClique para ir.").addLoreLine("§aVocê está aqui!").toItemStack());
		}
		if (type != null && type == PunishType.MUTE) {
			for(Punir punir : Main.cache.punimentos.values()) {
				if(punir.getType() != PunishType.MUTE) continue;
				if (i < inicio) {
					i++;
					continue;
				}
				if(i >= fim) {
					full = true;
					break;
				}
				slot = nextSlot(slot);
				inv.setItem(slot, getItem(punir));
			}
			inv.setItem(49, mutes.removeLoreLine("§aClique para ir.").addLoreLine("§aVocê está aqui!").toItemStack());
		}
		if(full) inv.setItem(53, new ItemBuilder(Material.ARROW).setName("§aPróxima Página").toItemStack());
		if(page > 1) inv.setItem(45, new ItemBuilder(Material.ARROW).setName("§cPágina Anterior").toItemStack());
		
		ItemBuilder head = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(target);
		lore = Arrays.asList("§7Você está punindo " + target + ".  ");
		head.setName("§aSobre").setLore(lore);
		NBTAPI nbt = NBTAPI.getNBT(head.toItemStack());
		if(type != null) nbt.setString("punir_type", type.toString());
		else nbt.setString("punir_type", "all");
		nbt.setInt("punir_page", page);
		nbt.setString("punir_target", target);
		inv.setItem(4, nbt.getItem());
		
		p.openInventory(inv);
	}
	
	private static Integer getPage(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(4)).getInt("punir_page");
	}
	
	private static String getTarget(Inventory inv) {
		return NBTAPI.getNBT(inv.getItem(4)).getString("punir_target");
	}
	
	private static PunishType getType(Inventory inv) {
		String s = NBTAPI.getNBT(inv.getItem(4)).getString("punir_type");
		if(s.equalsIgnoreCase("all")) return null;
		return PunishType.valueOf(s);
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
	
	private static ItemStack getItem(Punir punir) {
		List<String> lore = Arrays.asList("", "§7 Tipo de Punição: §e" + punir.getType().name, "§7 Duração:§e" + 
	zBUtils.TimeToString(punir.getTime()).replace("§cNão encontrado", " Permanente"), "", "§aClique para escolher!   ");
		int durability = (punir.getType() == PunishType.BAN) ? 14 : 4;
		ItemBuilder i = new ItemBuilder(Material.STAINED_CLAY, 1, durability).setName("§e" + punir.getName()).setLore(lore);
		NBTAPI nbt = NBTAPI.getNBT(i.toItemStack());
		nbt.setString("punir_nome", punir.getName());
		nbt.setString("punir_tipo", punir.getType().toString());
		nbt.setLong("punir_tempo", punir.getTime());
		return nbt.getItem();
	}
	
	private static boolean verifyPerm(Player p, PunishType type) {
		if(p.hasPermission("zbpunish.admin") || p.hasPermission("zbpunish.ban")) return true;
		if(type != null && type == PunishType.MUTE) return p.hasPermission("zbpunish.mute");
		return false;
	}
}
