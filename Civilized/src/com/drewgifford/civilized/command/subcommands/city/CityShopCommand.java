package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.InventoryItems;

import net.md_5.bungee.api.ChatColor;

public class CityShopCommand extends CivilizedSubcommand {

	public CityShopCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		if (cp.getCity() == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any city.");
			return false;
		}
		
		City city = cp.getCity();
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		
		openCityMenu(pl, city, p);
		return false;
	}
	
	public static double calculateScaledCost(int initialAmount, int currentAmount, double pricePer, double factor, int purchaseAmount) {
		System.out.println("Price per pop: " + pricePer);
		return pricePer * Math.pow((currentAmount - (initialAmount) + purchaseAmount) / purchaseAmount , factor);
		
	}
	
	public static void openCityMenu(Civilized pl, City city, Player p) {
		
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		if (!cp.getCity().equals(city)) return;
		
		Inventory i = Bukkit.createInventory(null, 27, ChatColor.BOLD + city.getNameWithSpaces() + ChatColor.BOLD + " Menu");
		
	
		
		double slotsCost = calculateScaledCost(
				SettingsConfiguration.INITIAL_PLAYER_SLOTS,
				cp.getCity().getPlayerSlots(),
				SettingsConfiguration.PRICE_PER_PLAYER_SLOT,
				SettingsConfiguration.PLAYER_SLOT_PRICE_EXP_FACTOR,
				SettingsConfiguration.PLAYER_SLOT_PURCHASE_SIZE
		);
		double chunksCost = calculateScaledCost(
				SettingsConfiguration.INITIAL_MAXCLAIMS,
				cp.getCity().getMaxClaimChunks(),
				SettingsConfiguration.PRICE_PER_MAXCLAIM,
				SettingsConfiguration.MAXCLAIM_PRICE_EXP_FACTOR,
				SettingsConfiguration.MAXCLAIM_PURCHASE_SIZE
		);
		
		ItemStack addSlots = InventoryItems.PLAYER_SLOTS(pl, city.getPlayerSlots(), slotsCost);
		i.setItem(11, addSlots);
		
		ItemStack addChunks = InventoryItems.CLAIM_CHUNKS(pl, city.getMaxClaimChunks(), chunksCost);
		i.setItem(15, addChunks);
		
		ItemStack balanceItem = InventoryItems.CITY_BALANCE(pl, city.getBalance());
		i.setItem(13, balanceItem);
		
		p.openInventory(i);
	}

}
