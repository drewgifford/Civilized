package com.drewgifford.civilized.command.subcommands.civ;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

import net.md_5.bungee.api.ChatColor;

public class CivBypassCommand extends CivilizedSubcommand {

	public CivBypassCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}
	
	public static Set<UUID> bypassing = new HashSet<UUID>();

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		
		boolean bypass = bypassing.contains(p.getUniqueId());
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("on")) {
				bypass = true;
			}
			else if (args[0].equalsIgnoreCase("off")) {
				bypass = false;
			} else {
				bypass = !bypass;
			}
		} else {
			bypass = !bypass;
		}
		
		if (bypass) {
			p.sendMessage(ChatColor.GREEN + "You are now bypassing claim permission. Type /civ bypass off to disable.");
			bypassing.add(p.getUniqueId());
		} else {
			p.sendMessage(ChatColor.RED + "You are no longer bypassing claim permission. Type /civ bypass on to enable.");
			bypassing.remove(p.getUniqueId());
		}
		
		
		
		return false;
	}
	

}
