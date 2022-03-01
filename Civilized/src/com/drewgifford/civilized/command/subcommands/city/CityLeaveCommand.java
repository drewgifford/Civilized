package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class CityLeaveCommand extends CivilizedSubcommand {
	
	public CityLeaveCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (p.getUniqueId().equals(city.getOwner())) {
			p.sendMessage(ChatColor.RED + "You must disband the city or transfer ownership to leave.");
			return false;
		}
		
		city.removePlayer(p.getUniqueId());
		
		p.sendMessage(ChatColor.GREEN + "You have left " + ChatColor.AQUA + city.getNameWithSpaces());
		
		return false;
	}

}
