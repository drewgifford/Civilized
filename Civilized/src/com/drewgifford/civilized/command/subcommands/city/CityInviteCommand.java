package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;

import net.md_5.bungee.api.ChatColor;

public class CityInviteCommand extends CivilizedSubcommand {

	public CityInviteCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		// Inviting a player
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You must specify a player to invite.");
			return false;
		}
		
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null) {
			p.sendMessage(ChatColor.RED + "Cannot find player " + args[0]);
			return false;
		}
		
		if (target == p) {
			p.sendMessage(ChatColor.RED + "You cannot invite yourself.");
			return false;
		}
		
		CivilizedPlayer targetCp = CivilizedPlayer.getCivilizedPlayer(target);
		
		if(targetCp.getCity() != null) {
			p.sendMessage(ChatColor.RED + args[0] + " is already a member of " + targetCp.getCity().getNameWithSpaces() + ".");
			return false;
		}
		
		// Player found, invite valid.
		
		if (CityInvite.inviteExists(target.getUniqueId(), city)) {
			p.sendMessage(ChatColor.RED + p.getName() + " already has a pending invite to " + city.getNameWithSpaces());
			return false;
		}
		
		CityInvite invite = new CityInvite(pl, p.getUniqueId(), target.getUniqueId(), city);
		Civilized.cityInvites.add(invite);
		p.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GREEN +  " has been invited to " + ChatColor.AQUA + city.getNameWithSpaces());
		
		
		target.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.GREEN +  " has invited you to " + ChatColor.AQUA + city.getNameWithSpaces());
		target.sendMessage(ChatColor.GREEN + "Type " + ChatColor.DARK_GREEN + "/city join " + city.getName() + ChatColor.GREEN + " to accept, or "
				+ ChatColor.RED + "/city deny " + city.getName() + ChatColor.GREEN + " to deny. This request will expire in 5 minutes."
				);
		
		
		
		return false;
	}

}
