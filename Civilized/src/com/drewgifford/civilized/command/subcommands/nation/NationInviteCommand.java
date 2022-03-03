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

public class NationInviteCommand extends CivilizedSubcommand {

	public NationInviteCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (!nation.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the nation to do that.");
			return false;
		}
		
		// Inviting a player
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You must specify a City to invite.");
			return false;
		}
		
		
		City target = CityManager.getCityFromName(args[0]);
		
		if (target == null) {
			p.sendMessage(ChatColor.RED + "Cannot find city " + args[0]);
			return false;
		}
		
		if (target.getNation() != null) {
			p.sendMessage(ChatColor.RED + "That city already has a member of " + target.getNation().getNameWithSpaces()+".");
			return false;
		}
		
		/*if (city.getPlayers().size() >= city.getPlayerSlots()) {
			p.sendMessage(ChatColor.RED + "There are not enough player slots to invite more players. Consider buying more with /city shop");
			return false;
		}*/
		
		// Player found, invite valid.
		
		if (NationInvite.inviteExists(target, nation)) {
			p.sendMessage(ChatColor.RED + target.getNameWithSpaces() + " already has a pending invite to " + nation.getNameWithSpaces());
			return false;
		}
		
		NationInvite invite = new NationInvite(pl, target, nation);
		
		Civilized.nationInvites.add(invite);
		p.sendMessage(ChatColor.AQUA + target.getNameWithSpaces() + ChatColor.GREEN +  " has been invited to " + ChatColor.AQUA + nation.getNameWithSpaces());
		
		if (Bukkit.getPlayer(target.getOwner()) != null) {
			Player t = Bukkit.getPlayer(target.getOwner());
			
			t.sendMessage(ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.GREEN +  " has invited your town to join their nation.");
			t.sendMessage(ChatColor.GREEN + "Type " + ChatColor.DARK_GREEN + "/nation join " + nation.getName() + ChatColor.GREEN + " to accept, or "
					+ ChatColor.RED + "/nation deny " + nation.getName() + ChatColor.GREEN + " to deny. This request will expire in 5 minutes."
					);
		}
		
		
		
		return false;
	}

}
