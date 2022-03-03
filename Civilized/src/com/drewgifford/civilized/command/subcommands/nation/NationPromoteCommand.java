package com.drewgifford.civilized.command.subcommands.nation;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class NationPromoteCommand extends CivilizedSubcommand{

	public NationPromoteCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide a user.");
			return false;
		}
		
		OfflinePlayer t = null;
		
		for (UUID uuid : nation.getPlayers()) {
			
			OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
			
			if (target.getName().equalsIgnoreCase(args[0])) {
				t = target;
				break;
			}
		}
		
		if (t == null) {
			p.sendMessage(ChatColor.RED + args[0] + " is not apart of your nation.");
			return false;
		}
	
		if (t.getUniqueId().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You cannot promote yourself.");
			return false;
		}
		
		if (nation.getOfficers().contains(t.getUniqueId())) {
			p.sendMessage(ChatColor.RED + t.getName() + " is already a Nation Officer.");
			return false;
		}
		
		System.out.println(t.getUniqueId());
	
		nation.addOfficer(t.getUniqueId());
		
		p.sendMessage(ChatColor.AQUA + t.getName() + ChatColor.GREEN + " has been promoted to " + ChatColor.AQUA + "Nation Officer" + ChatColor.GREEN + ".");
		
		return false;
	}
	
	

}
