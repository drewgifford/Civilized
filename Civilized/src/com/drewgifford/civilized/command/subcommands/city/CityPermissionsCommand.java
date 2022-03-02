package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class CityPermissionsCommand extends CivilizedSubcommand {
	
	public CityPermissionsCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
			p.sendMessage(ChatColor.GREEN + city.getNameWithSpaces() + " Permissions");
			city.getPermissions().sendOptionValues(p);
			return false;
		}
		
		if (!city.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		if (!CivilizedPermissions.isValidString(args[0])) {
			p.sendMessage(ChatColor.RED + "Invalid permission type.");
			p.sendMessage(ChatColor.GRAY + "Valid permission types: " + CivilizedPermissions.getOptionsString());
			return false;
		}
		
		if (args.length < 2) {
			p.sendMessage(ChatColor.RED + "You need to provide which role to grant the permission to.");
			p.sendMessage(ChatColor.DARK_RED + "Valid roles: " + PermissionLevel.getOptionsString());
			return false;
		}
		
		PermissionLevel level = PermissionLevel.fromString(args[1]);
		
		if(level == PermissionLevel.DEFAULT) {
			level = PermissionLevel.MEMBER;
		}
		
		if (level == null) {
			p.sendMessage(ChatColor.RED + "Invalid role.");
			p.sendMessage(ChatColor.DARK_RED + "Valid roles: " + PermissionLevel.getOptionsString());
			return false;
		}
		
		city.getPermissions().setOption(args[0], level);
		
		p.sendMessage(ChatColor.GREEN + "Set city permission " + ChatColor.AQUA + args[0].toUpperCase() + ChatColor.GREEN + " access to " + ChatColor.AQUA + level.toString());
		
		return false;
	}
}
