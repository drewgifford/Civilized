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

public class PlotNotForSaleCommand extends CivilizedSubcommand {

	public PlotNotForSaleCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (!((plot.getOwner() == null || plot.getOwner().equals(p.getUniqueId())) || city.getOfficers().contains(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "You must either own the plot or be a city Officer to unlist a plot.");
			return false;
		}
	
		
		plot.setForSale(false);
		plot.setPrice(0.0);
		
		p.sendMessage(ChatColor.GREEN + "This plot is no longer for sale.");

		return false;
	}
	

}
