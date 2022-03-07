package com.drewgifford.civilized.command.subcommands.city;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class CityKickCommand extends CivilizedSubcommand {
	
	public CityKickCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide a user.");
			return false;
		}
		
		OfflinePlayer t = null;
		
		for (UUID uuid : city.getPlayers()) {
			
			OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
			
			if (target.getName().equalsIgnoreCase(args[0])) {
				t = target;
				break;
			}
		}
		
		if (t == null) {
			p.sendMessage(ChatColor.RED + args[0] + " is not apart of your town.");
			return false;
		}
		
		if (t.getUniqueId().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You cannot kick yourself.");
			return false;
		}
		
		if (city.getOfficers().contains(t.getUniqueId())) {
			if (!city.getOwner().equals(p.getUniqueId())) {
				p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
				return false;
			}
		} else {
			if (!city.getOfficers().contains(p.getUniqueId())){
				p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
				return false;
			}
		}
		
		city.removePlayer(p.getUniqueId());
		
		p.sendMessage(ChatColor.AQUA + t.getName() + ChatColor.GREEN + " has been kicked from " + ChatColor.AQUA + city.getNameWithSpaces());
		
		pl.citiesConfiguration.write();
		
		return false;
	}

}
