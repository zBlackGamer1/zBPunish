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
import pt.com.zBPunish.methods.PunishType;
import pt.com.zBPunish.utils.ItemBuilder;
import pt.com.zBPunish.utils.NBTAPI;
import pt.com.zBPunish.utils.zBUtils;

public class PunishMenu implements Listener {
	@EventHandler
	void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("§0§7Punindo ")) return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		NBTAPI nbt = NBTAPI.getNBT(e.getInventory().getItem(32));
		switch (e.getSlot()) {
		case 11:
			TimeMenu.Open(p, nbt.getString("nome"), (nbt.hasKey("motivo") ? nbt.getString("motivo") : null), (nbt.hasKey("provas") ? nbt.getString("provas") : null), PunishType.valueOf(nbt.getString("puntype")));
			break;
			
		case 13:
			p.closeInventory();
			Main.cache.digit1.put(p.getName(), nbt);
			p.sendMessage(new String[] {
					"",
					"§7Digite o motivo pela qual vai punir §a" + nbt.getString("nome") + "§7.",
					"§7Para cancelar digite §ccancelar§7.",
					""
			});
			break;
			
		case 15:
			p.closeInventory();
			Main.cache.digit2.put(p.getName(), nbt);
			p.sendMessage(new String[] {
					"",
					"§7Digite as provas da punição de §a" + nbt.getString("nome") + "§7.",
					"§7Para cancelar digite §ccancelar§7.",
					""
			});
			break;
			
		case 32:
			if (!nbt.hasKey("motivo") && nbt.hasKey("provas") && nbt.hasKey("tempo")) return;
			p.closeInventory();
			PunishType punType = PunishType.valueOf(nbt.getString("puntype"));
			if(punType == PunishType.BAN) Main.manager.Ban(p.getName(), nbt.getString("nome"), nbt.getString("motivo"), nbt.getString("provas"), nbt.getLong("tempo"));
			if(punType == PunishType.MUTE) Main.manager.Mute(p.getName(), nbt.getString("nome"), nbt.getString("motivo"), nbt.getString("provas"), nbt.getLong("tempo"));
			break;
		default:
			break;
		}
	}

	
	@SuppressWarnings("deprecation")
	public static void Open(Player p, String target, String motivo, String provas, Long time, PunishType type) {
		String name = (Bukkit.getOfflinePlayer(target) != null) ? Bukkit.getOfflinePlayer(target).getName() : target;
		Inventory inv = Bukkit.createInventory(null, 5*9, "§0§7Punindo " + name);
		List<String> lore = new ArrayList<>();
		ItemBuilder punishedhead = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(name);
		lore = Arrays.asList("§7" + name);
		inv.setItem(30, punishedhead.setName("§eJogador").setLore(lore).toItemStack());
		
		lore = (provas != null) ? Arrays.asList("§a ➥§7 " + provas) : Arrays.asList("§cEste campo não está preenchido!");
		inv.setItem(15, new ItemBuilder(Material.PAPER).setLore(lore).setName("§aProvas").toItemStack());
		
		lore = (motivo != null) ? Arrays.asList("§a ➥§7 " + motivo) : Arrays.asList("§cEste campo não está preenchido!");
		inv.setItem(13, new ItemBuilder(Material.NAME_TAG).setLore(lore).setName("§aMotivo").toItemStack());
		
		
		String duracao = "";
		if(time != null) duracao = (time == (long) 1) ? " Permanente" : "" + zBUtils.TimeToString(time);
		lore = (time != null) ? Arrays.asList("§a ➥§7" + duracao) : Arrays.asList("§cEste campo não está preenchido!");
		inv.setItem(11, new ItemBuilder(Material.WATCH).setLore(lore).setName("§aDuração").toItemStack());
		
		lore = new ArrayList<>();
		String url;
		if (motivo != null && provas != null && time != null) {
			url = "22d145c93e5eac48a661c6f27fdaff5922cf433dd627bf23eec378b9956197";
		} else {
			lore.add("§7Para avançar você precisa de");
			lore.add("§7preencher todos os campos.");
			lore.add("§c§l EM FALTA:");
			url = "7af6fab767ca4d7df6217b895b667bcacc524d407068619f819a070f3f629ce0";
		}
		
		String punishName = (type == PunishType.BAN) ? "Banir" : "Mutar";
		ItemBuilder ib = new ItemBuilder(url).setName("§a" + punishName);
		if (motivo != null && provas != null && time != null) {
			lore.add("§7Ao clicar você irá");
			lore.add("§7punir o §7§n" + name + ".");
			lore.add("");
			lore.add("§aClique para punir!");
		}
		if(time != null) ib.setNBT("tempo", time);
		else lore.add("   §a➥ §7Duração");
		if(motivo != null) ib.setNBT("motivo", motivo);
		else lore.add("   §a➥ §7Motivo");
		if(provas != null) ib.setNBT("provas", provas);
		else lore.add("   §a➥ §7Provas");
		ib.setNBT("nome", name);
		ib.setNBT("puntype", type.toString());
		inv.setItem(32, ib.setLore(lore).toItemStack());
		p.openInventory(inv);
	}

	
	public static void Open(Player p, NBTAPI nbt) {
		Open(p, nbt.getString("nome"), (nbt.hasKey("motivo") ? nbt.getString("motivo") : null),
				(nbt.hasKey("provas") ? nbt.getString("provas") : null), (nbt.hasKey("tempo") ? nbt.getLong("tempo") : null), PunishType.valueOf(nbt.getString("puntype")));
	}
}
