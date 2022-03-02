package com.drewgifford.civilized.command.subcommands.plot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class PlotClaimCommand extends CivilizedSubcommand {

	public PlotClaimCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		Location location = p.getLocation();
		
		City city = CityManager.getCityFromLocation(location);
	
		if (city == null) {
			p.sendMessage(ChatColor.RED + "There is no plot at this location.");
			return false;
		}
		
		Plot plot = city.getChunkPlotMap().get(location.getChunk());
		
		if (!plot.isForSale()) {
			p.sendMessage(ChatColor.RED + "This plot is not for sale.");
			return false;
		}
		
		double price = plot.getPrice();
		double balance = pl.getEconomy().getBalance(p);
		
		if (balance < price) {
			p.sendMessage(ChatColor.RED + "You do not have enough money to claim this plot. You need " + pl.getEconomy().format(price-balance) + "more.");
			return false;
		}
		
		pl.getEconomy().withdrawPlayer(p, price);
		
		if (plot.getOwner() == null) {
			city.addBalance(price);
		} else {
			pl.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(plot.getOwner()), price);
		}
		plot.setOwner(p.getUniqueId());
		plot.setForSale(false);
		
		p.sendMessage(ChatColor.GREEN + "You have purchased this plot!");

		return false;
	}
	

}
