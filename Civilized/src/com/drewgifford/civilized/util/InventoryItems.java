package com.drewgifford.civilized.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.config.SettingsConfiguration;

import net.md_5.bungee.api.ChatColor;

public class InventoryItems {
	
	public static ItemStack PLAYER_SLOTS(Civilized pl, int slots, double cost) {
		
		ItemStack skull = new ItemStack(
				Material.PLAYER_HEAD);
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Add Player Slot");
		
		List<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.AQUA + "Current amount: " + ChatColor.DARK_AQUA + (slots));
		lore.add(ChatColor.AQUA + "New amount: " + ChatColor.DARK_AQUA + (slots + SettingsConfiguration.PLAYER_SLOT_PURCHASE_SIZE));
		lore.add(ChatColor.YELLOW + "Cost: " + ChatColor.GOLD + pl.getEconomy().format(cost));
		
		meta.setLore(lore);
		
		skull.setItemMeta(meta);
		return skull;
		
	}
	
	public static ItemStack CLAIM_CHUNKS(Civilized pl, int chunks, double cost) {
		
		ItemStack skull = new ItemStack(
				Material.GOLDEN_SHOVEL);
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Add Maximum Chunks");
		
		List<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.AQUA + "Current amount: " + ChatColor.DARK_AQUA + (chunks));
		lore.add(ChatColor.AQUA + "New amount: " + ChatColor.DARK_AQUA + (chunks + SettingsConfiguration.MAXCLAIM_PURCHASE_SIZE));
		lore.add(ChatColor.YELLOW + "Cost: " + ChatColor.GOLD + pl.getEconomy().format(cost));
		
		meta.setLore(lore);
		
		skull.setItemMeta(meta);
		return skull;
		
	}
	
public static ItemStack CITY_BALANCE(Civilized pl, double balance) {
		
		ItemStack skull = new ItemStack(
				Material.GOLD_INGOT);
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Balance: " + ChatColor.GOLD + pl.getEconomy().format(balance));
		
		skull.setItemMeta(meta);
		return skull;
		
	}

}
