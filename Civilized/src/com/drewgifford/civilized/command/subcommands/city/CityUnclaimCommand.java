package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.ChunkCoordinates;

import net.md_5.bungee.api.ChatColor;

public class CityUnclaimCommand extends CivilizedSubcommand {

	public CityUnclaimCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		//TODO: Check if member has permissions to claim
		
		Location location = p.getLocation();
		
		if (!city.locationInTown(location)) {
			p.sendMessage(ChatColor.RED + "Your city doesn't own this chunk.");
			return false;
		}
		
		Chunk chunk = location.getChunk();
		
		if (city.willChunkDisconnect(chunk) && city.getChunks().size() > 0) {
			p.sendMessage(ChatColor.RED + "Unclaiming that chunk would make your city disconnected.");
			return false;
		}
		
		city.removeChunk(chunk);
		
		p.sendMessage(ChatColor.GREEN + "Your town has unclaimed the chunk at [" + chunk.getX() + ", " + chunk.getZ() + "]");
		
		
		return false;
	}

}
