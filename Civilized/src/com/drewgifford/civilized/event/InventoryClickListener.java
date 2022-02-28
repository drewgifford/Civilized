package com.drewgifford.civilized.event;

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
				city.setPlayerSlots(city.getPlayerSlots() + 1);
				
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
			if (e.getSlot() == 15) { // Add chunks
				city.setMaxClaimChunks(city.getMaxClaimChunks() + 8);
				
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
			
			CityShopCommand.openCityMenu(pl, city, p);
		}
		
		
	}

}
