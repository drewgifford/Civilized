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

import net.md_5.bungee.api.ChatColor;

public class NationBoardCommand extends CivilizedSubcommand{

	public NationBoardCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (!nation.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the nation to do that.");
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide a message.");
			return false;
		}
		
		String board = String.join(" ", args);
		
		nation.setBoard(board);
		
		p.sendMessage(ChatColor.GREEN + "Set your nation board to: " + ChatColor.GRAY + board);
		
		return false;
	}
	
	

}
