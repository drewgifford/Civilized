package com.drewgifford.civilized.command.subcommands.nation;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.requests.NationInvite;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class NationKickCommand extends CivilizedSubcommand {

	public NationKickCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (cp.getCity() == null || cp.getCity().getNation() == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any nation.");
			return false;
		}
		
		Nation nation = cp.getCity().getNation();
		
		if (!nation.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the nation to do that.");
			return false;
		}
		
		// Inviting a player
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You must specify a City to kick.");
			return false;
		}
		
		
		City target = CityManager.getCityFromName(args[0]);
		
		if (target == null) {
			p.sendMessage(ChatColor.RED + "Cannot find city " + args[0]);
			return false;
		}
		
		if (target.getNation() != nation) {
			p.sendMessage(ChatColor.RED + "That city is not a member of " + nation.getNameWithSpaces()+".");
			return false;
		}
		
		if (nation.getCapital().equals(target)) {
			p.sendMessage(ChatColor.RED + "You cannot kick the capital city from the nation.");
			return false;
		}
		
		/*if (city.getPlayers().size() >= city.getPlayerSlots()) {
			p.sendMessage(ChatColor.RED + "There are not enough player slots to invite more players. Consider buying more with /city shop");
			return false;
		}*/
		
		target.setNation(null);
		
		p.sendMessage(ChatColor.AQUA + target.getNameWithSpaces() + " has been kicked from " + ChatColor.AQUA + nation.getNameWithSpaces());
		
		pl.citiesConfiguration.write();
		
		
		
		return false;
	}

}
