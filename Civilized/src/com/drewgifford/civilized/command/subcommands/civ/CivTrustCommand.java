package com.drewgifford.civilized.command.subcommands.civ;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
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

public class CivTrustCommand extends CivilizedSubcommand {

	public CivTrustCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You must specify a player to trust.");
			return false;
		}
		
		Player t = Bukkit.getPlayer(args[0]);
		
		if (t == null) {
			p.sendMessage(ChatColor.RED + "That player was not found.");
			return false;
		}
		
		Set<UUID> trusted = cp.getTrusted();
		
		if (trusted.contains(t.getUniqueId())) {
			p.sendMessage(ChatColor.RED + t.getName() + " is already trusted.");
			return false;
		}
		
		trusted.add(t.getUniqueId());
		p.sendMessage(ChatColor.GREEN + t.getName() + " is now trusted in your claims.");
		
		pl.citiesConfiguration.write();
		
		return false;
	}

}
