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

public class PlotInfoCommand extends CivilizedSubcommand {

	public PlotInfoCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		sendPlotInformation(p, plot);

		return false;
	}
	
	public void sendPlotInformation(Player p, Plot plot) {
		Chunk chunk = plot.getChunk();
		if (plot.getName() == "") {
			p.sendMessage(ChatColor.GREEN + "===== Plot at " + ChatColor.AQUA + chunk.getX() + "," + chunk.getZ() + ChatColor.GREEN + " =====");
		}
		else {
			p.sendMessage(ChatColor.GREEN + "===== " + ChatColor.AQUA + plot.getName() + ChatColor.GREEN + " =====");
		}
		UUID owner = plot.getOwner();
		String ownerName = "None";
		if (owner != null) {
			ownerName = Bukkit.getOfflinePlayer(owner).getName();
		}
		
		String cityName = plot.getCity().getNameWithSpaces();
		
		List<String> trustedNames = new ArrayList<String>();
		
		for (UUID member : plot.getTrusted()) {
			String memberName = Bukkit.getOfflinePlayer(member).getName();
			trustedNames.add(memberName);
			
		}
		
		Collections.sort(trustedNames);
		
		String location = "X:" + chunk.getX() + ", Z:" + chunk.getZ();
		
	
		if (plot.isForSale()) {
			p.sendMessage(ChatColor.GOLD + "FOR SALE: " + pl.getEconomy().format(plot.getPrice()));
			p.sendMessage(ChatColor.YELLOW + "/plot claim to claim!");
		}
		
		// TODO: Change to language file
		p.sendMessage(ChatColor.AQUA + "Owner: " + ChatColor.DARK_AQUA + ownerName);
		p.sendMessage(ChatColor.AQUA + "City: " + ChatColor.DARK_AQUA + cityName);
		p.sendMessage(ChatColor.AQUA + "Location: " + ChatColor.DARK_AQUA + location);
		p.sendMessage(ChatColor.AQUA + "Trusted (" + trustedNames.size() + "): " + ChatColor.DARK_AQUA + String.join(", ", trustedNames));
		
		/*p.sendMessage(ChatColor.GREEN + "Permissions:");
		plot.getPermissions().sendOptionValues(p);
		p.sendMessage(ChatColor.GREEN + "Toggles:");
		plot.getToggles().sendOptionValues(p);*/
		
	}

}
