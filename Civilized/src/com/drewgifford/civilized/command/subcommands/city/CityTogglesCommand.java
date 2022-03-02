package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.permissions.ToggleLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class CityTogglesCommand extends CivilizedSubcommand {
	
	public CityTogglesCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		City city = cp.getCity();
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.GREEN + city.getNameWithSpaces() + " Toggles");
			city.getToggles().sendOptionValues(p);
			return false;
		}
		
		if (!city.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		if (!CivilizedToggles.isValidString(args[0])) {
			p.sendMessage(ChatColor.RED + "Invalid toggle type.");
			p.sendMessage(ChatColor.DARK_RED + "Valid toggle types: " + CivilizedToggles.getOptionsString());
			return false;
		}
		
		ToggleLevel current = city.getToggles().getOption(args[0]);
		ToggleLevel setTo = ToggleLevel.DISABLED;
		
		if (args.length < 2) {
			setTo = ToggleLevel.opposite(current);
		}
		else {
			
			switch(args[1].toUpperCase()) {
				case "YES":
				case "TRUE":
				case "Y":
				case "ENABLED":
				case "T":
					setTo = ToggleLevel.ENABLED;
					break;
				case "NO":
				case "FALSE":
				case "N":
				case "DISABLED":
				case "F":
					setTo = ToggleLevel.DISABLED;
					break;
				case "TOGGLE":
				case "OPPOSITE":
					setTo = ToggleLevel.opposite(current);
					break;
				default:
					p.sendMessage(ChatColor.RED + "Invalid toggle option, it must be set to TRUE or FALSE.");
					return false;
			}
			
			
		}
		
		
		city.getToggles().setOption(args[0], setTo);
		
		p.sendMessage(ChatColor.GREEN + "Set city toggle " + ChatColor.AQUA + args[0].toUpperCase() + ChatColor.GREEN + " to " + ChatColor.AQUA + setTo.toString().toLowerCase());
		
		return false;
	}
}
