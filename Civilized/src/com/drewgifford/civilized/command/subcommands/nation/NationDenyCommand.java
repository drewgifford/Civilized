package com.drewgifford.civilized.command.subcommands.nation;

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
import com.drewgifford.civilized.util.NationManager;

import net.md_5.bungee.api.ChatColor;

public class NationDenyCommand extends CivilizedSubcommand {
	
	public NationDenyCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (cp.getCity() == null || !cp.getCity().getOwner().equals(p.getUniqueId())) {
			sender.sendMessage(ChatColor.RED + "You must be the owner of a city to run this command.");
			return false;
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "You must specify which nation to deny an invite from.");
			return false;
		}
		
		Nation targetNation = NationManager.getNationFromName(String.join(" ", args));
		
		if (targetNation == null) {
			sender.sendMessage(ChatColor.RED + "Nation " + String.join(" ", args) + "not found.");
			return false;
		}
		
		
		NationInvite activeInvite = NationInvite.getInvite(cp.getCity(), targetNation);
		if (activeInvite == null) {
			sender.sendMessage(ChatColor.RED + "You do not have an active invite from " + targetNation.getNameWithSpaces());
			return false;
		}
		
		activeInvite.deny();
		
		return false;
	}

}
