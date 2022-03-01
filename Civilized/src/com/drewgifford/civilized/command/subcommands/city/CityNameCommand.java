package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.ChunkCoordinates;

import net.md_5.bungee.api.ChatColor;

public class CityNameCommand extends CivilizedSubcommand{

	public CityNameCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		//TODO: Check if member has permissions to claim
		
		Location location = p.getLocation();
		City city = cp.getCity();
		
		if (!city.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide a name.");
			return false;
		}
		
		String name = String.join(" ", args);
		
		name = name.replace(' ', '_');
		
		city.setName(name);
		
		p.sendMessage(ChatColor.GREEN + "Set your city name to: " + ChatColor.AQUA + city.getNameWithSpaces());
		
		return false;
	}
	
	

}
