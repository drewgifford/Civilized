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

public class CityJoinCommand extends CivilizedSubcommand {
	
	public CityJoinCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "You must specify which city to accept an invite from.");
			return false;
		}
		
		City targetCity = CityManager.getCityFromName(String.join(" ", args));
		
		if (targetCity == null) {
			sender.sendMessage(ChatColor.RED + "City " + String.join(" ", args) + "not found.");
			return false;
		}
		
		
		CityInvite activeInvite = CityInvite.getInvite(p.getUniqueId(), targetCity);
		if (activeInvite == null) {
			sender.sendMessage(ChatColor.RED + "You do not have an active invite from " + targetCity.getNameWithSpaces());
			return false;
		}
		
		if (cp.getCity() != null) {
			sender.sendMessage(ChatColor.RED + "You are already a member of a city!");
			return false;
		}
		
		activeInvite.accept();
		
		return false;
	}

}
