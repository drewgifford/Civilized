package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class CityHomeCommand extends CivilizedSubcommand {

	public CityHomeCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (cp.getCity() == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any city.");
			return false;
		}
		
		City city = cp.getCity();
		
		boolean canTeleport = SettingsConfiguration.ALLOW_HOME_TELEPORT;
		
		if (canTeleport == false) {
			p.sendMessage(ChatColor.RED + "City teleportation has been disabled.");
			return false;
		}
		
		p.teleport(city.getHome(), TeleportCause.PLUGIN);
		p.sendMessage(ChatColor.GREEN + "You have been teleported to your city's home.");
		
		
		return false;
	}

}
