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

public class CityClaimCommand extends CivilizedSubcommand{

	public CityClaimCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		City city = cp.getCity();
		
		if (city == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any city.");
			return false;
		}
		
		if(attemptClaim(p, city)) {
			Chunk chunk = p.getLocation().getChunk();
			p.sendMessage(ChatColor.GREEN + "Your town has claimed the chunk at [" + chunk.getX() + ", " + chunk.getZ() + "]");
		}
		
		
		return false;
	}
	
	public static boolean attemptClaim(Player p, City city) {
		
		//TODO: Check if member has permissions to claim
		
		Location location = p.getLocation();
		
		if (city.getChunks().size() >= city.getMaxClaimChunks()) {
			p.sendMessage(ChatColor.RED + "Your city has reached the maximum allowed claim chunks.");
			return false;
		}
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		if (city.locationInTown(location)) {
			p.sendMessage(ChatColor.RED + "Your city already owns this chunk.");
			return false;
		}
		
		Chunk chunk = location.getChunk();
		
		if (!city.isChunkAdjacent(chunk) && city.getChunks().size() > 0) {
			p.sendMessage(ChatColor.RED + "New claims must be adjacent to existing ones.");
			return false;
		}
		
		int minDistance = 3;
		for (City c : Civilized.cities) {
			if (c.equals(city)) continue;
			if (c.hasChunk(chunk)) {
				p.sendMessage(ChatColor.RED + "A city already owns this chunk.");
				return false;
			}
			if (c.isChunkNearby(chunk, minDistance)) {
				p.sendMessage(ChatColor.RED + "New claims must be at least "+minDistance + " chunks away from another town.");
				return false;
			}
		}
		
		city.addChunk(chunk);
		
		return true;
	}
	
	

}
