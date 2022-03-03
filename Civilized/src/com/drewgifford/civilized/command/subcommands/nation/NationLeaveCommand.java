package com.drewgifford.civilized.command.subcommands.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class NationLeaveCommand extends CivilizedSubcommand {
	
	public NationLeaveCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		City city = cp.getCity();
		Nation nation = city.getNation();
		
		if (!cp.getCity().getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permissions within your town to do that.");
			return false;
		}
		
		if (nation.getCapital().equals(city)) {
			p.sendMessage(ChatColor.RED + "You cannot leave the nation without transferring the capital.");
			return false;
		}
		
		
		city.setNation(null);
		
		p.sendMessage(ChatColor.GREEN + "Your town has left " + ChatColor.AQUA + nation.getNameWithSpaces());
		
		pl.citiesConfiguration.write();
		
		return false;
	}

}
