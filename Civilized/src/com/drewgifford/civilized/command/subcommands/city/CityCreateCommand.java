package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class CityCreateCommand extends CivilizedSubcommand {

	public CityCreateCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
	
		Player p = (Player) sender;
		
		
		if (args.length < 1) {
			//TODO: Update with language file
			p.sendMessage(ChatColor.RED + "You need to specify a city name.");
			return false;
		}
		
		String cityName = String.join(" ", args).replace(' ', '_');
		
		
	
		Location location = p.getLocation();
		
		int chunkX = location.getChunk().getX();
		int chunkZ = location.getChunk().getZ();
		
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		if (cp.getCity() != null) {
			// TODO: Update with language file
			p.sendMessage(ChatColor.RED + "You are already apart of a city!");
			return false;
		}
		
		if (CityManager.getCityFromName(cityName) != null) {
			p.sendMessage(ChatColor.RED + "A city of that name already exists.");
			return false;
		}
		
		if (CityManager.nameExists(cityName)) {
			p.sendMessage(ChatColor.RED + "A city with that name already exists.");
			return false;
		}
		
		else {
			
			City city = new City(p.getUniqueId(), cityName);
			
			if(!CityClaimCommand.attemptClaim(p, city)) {
				return false;
			}
			
			Civilized.cities.add(city);
			
			p.sendMessage(ChatColor.GREEN + "You have founded the city " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.GREEN + "!");
			
			pl.citiesConfiguration.write();
			
		}
		
		
		return false;
	}

}
