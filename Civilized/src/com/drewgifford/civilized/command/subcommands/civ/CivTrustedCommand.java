package com.drewgifford.civilized.command.subcommands.civ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

import net.md_5.bungee.api.ChatColor;

public class CivTrustedCommand extends CivilizedSubcommand {

	public CivTrustedCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		
		Set<UUID> trusted = cp.getTrusted();
		List<String> trustedNames = new ArrayList<String>();
		for (UUID uuid : trusted) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
			if (op != null) {
				trustedNames.add(op.getName());
			}
		}
		Collections.sort(trustedNames);

		
		p.sendMessage(ChatColor.AQUA + "Trusted Players (" + trustedNames.size() + "): " + ChatColor.DARK_AQUA + String.join(", " , trustedNames));
		
		return false;
	}

}
