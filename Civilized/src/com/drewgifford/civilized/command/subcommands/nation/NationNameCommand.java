package com.drewgifford.civilized.command.subcommands.nation;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;
import com.drewgifford.civilized.util.NationManager;
import com.drewgifford.civilized.util.StringManager;

import net.md_5.bungee.api.ChatColor;

public class NationNameCommand extends CivilizedSubcommand{

	public NationNameCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		//TODO: Check if member has permissions to claim
		
		Nation nation = cp.getCity().getNation();
		
		if (!nation.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the nation to do that.");
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide a name.");
			return false;
		}
		
		String name = String.join(" ", args);
		
		name = name.replace(' ', '_');
		
		if (NationManager.nameExists(name)) {
			p.sendMessage(ChatColor.RED + "A nation with that name already exists.");
			return false;
		}
		
		if (!StringManager.isValidName(name)) {
			p.sendMessage(ChatColor.RED + "Nation names must be alphanumeric and 20 characters or less.");
			return false;
		}
		
		nation.setName(name);
		
		p.sendMessage(ChatColor.GREEN + "Set your city name to: " + ChatColor.AQUA + nation.getNameWithSpaces());
		
		pl.citiesConfiguration.write();
		
		return false;
	}
	
	

}
