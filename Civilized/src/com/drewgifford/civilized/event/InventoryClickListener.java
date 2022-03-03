package com.drewgifford.civilized.event;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.subcommands.city.CityShopCommand;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class InventoryClickListener implements Listener{
	
Civilized pl;
	
	public InventoryClickListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		Player p = (Player)e.getWhoClicked();
		
		if (e.getView().getTitle().contains(ChatColor.BOLD + " Menu")) {
			e.setCancelled(true);
			
			CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
			City city = cp.getCity();
			
			if (e.getSlot() == 11) { // Add slots
				
				int amount = SettingsConfiguration.PLAYER_SLOT_PURCHASE_SIZE;
				
				double slotsCost = CityShopCommand.calculateScaledCost(
						SettingsConfiguration.INITIAL_PLAYER_SLOTS,
						cp.getCity().getPlayerSlots(),
						SettingsConfiguration.PRICE_PER_PLAYER_SLOT,
						SettingsConfiguration.PLAYER_SLOT_PRICE_EXP_FACTOR,
						SettingsConfiguration.PLAYER_SLOT_PURCHASE_SIZE
				);
				
				double balance = city.getBalance();
				
				if (balance < slotsCost) {
					p.sendMessage(ChatColor.RED + "You cannot afford more player slots. You need " + pl.getEconomy().format(slotsCost - balance) + " more.");
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
					return;
				}
				
				city.setBalance(city.getBalance() - slotsCost);
				
				
				
				city.setPlayerSlots(city.getPlayerSlots() + amount);
				
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
			if (e.getSlot() == 15) { // Add chunks
				
				double chunksCost = CityShopCommand.calculateScaledCost(
						SettingsConfiguration.INITIAL_MAXCLAIMS,
						cp.getCity().getMaxClaimChunks(),
						SettingsConfiguration.PRICE_PER_MAXCLAIM,
						SettingsConfiguration.MAXCLAIM_PRICE_EXP_FACTOR,
						SettingsConfiguration.MAXCLAIM_PURCHASE_SIZE
				);
				
				double balance = city.getBalance();
				
				if (balance < chunksCost) {
					p.sendMessage(ChatColor.RED + "You cannot afford more maximum claim chunks. You need " + pl.getEconomy().format(chunksCost - balance) + " more.");
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
					
					return;
				}
				
				city.setBalance(city.getBalance() - chunksCost);
				
				
				
				int amount = SettingsConfiguration.MAXCLAIM_PURCHASE_SIZE;
				
				city.setMaxClaimChunks(city.getMaxClaimChunks() + amount);
				
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
			Bukkit.getScheduler().runTask(pl, () -> { CityShopCommand.openCityMenu(pl, city, p); p.updateInventory(); });
		}
		
		
	}

}
