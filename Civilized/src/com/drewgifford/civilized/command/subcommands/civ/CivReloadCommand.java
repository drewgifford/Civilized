package com.drewgifford.civilized.command.subcommands.civ;

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

public class CivReloadCommand extends CivilizedSubcommand {

	public CivReloadCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		
		
		if (!(sender instanceof Player) || ((Player) sender).isOp()) {
			pl.citiesConfiguration.write();
			sender.sendMessage(ChatColor.YELLOW + "Reloading Civilized configuration...");
			pl.citiesConfiguration.load();
			pl.settingsConfiguration.load();
			pl.messagesConfiguration.load();
			sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
			
			return false;
		}
		((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		return false;
	}

}
