package com.drewgifford.civilized.command.subcommands.city;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

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
	
		double price = SettingsConfiguration.PRICE_PER_CLAIM;
		
		/*if (args.length > 0) {
			Integer radius = Integer.valueOf(args[0]);
			if (radius != null) {
				return attemptClaim(pl, p, city, (int) radius);
			} else {
				p.sendMessage(ChatColor.RED + "Invalid radius.");
				return false;
			}
		}*/
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("on")) {
				p.sendMessage(ChatColor.GREEN + "Automatic claiming enabled. Type /city claim off to disable.");
				Civilized.activeAutoClaims.add(p.getUniqueId());
			}
			if (args[0].equalsIgnoreCase("off")) {
				p.sendMessage(ChatColor.RED + "Automatic claiming disabled. Type /city claim on to enable.");
				Civilized.activeAutoClaims.remove(p.getUniqueId());
				return false;
				
			}
		}

		
		if(attemptClaim(pl, p, city, 0)) {
			Chunk chunk = p.getLocation().getChunk();
			
			if (price > 0) {
				// Check bank account
				double balance = city.getBalance();
				
				if (balance < price) {
					p.sendMessage(ChatColor.RED + "Your city does not have enough money in the bank. You need " + pl.getEconomy().format(price-balance) + " more.");
					return false;
				}
			}
			
			city.setBalance(city.getBalance()-price);
			
			p.sendMessage(ChatColor.GREEN + "Your city has claimed the chunk at [X:" + chunk.getX() + ", Z:" + chunk.getZ() + "]");
		}
		
		
		return false;
	}
	
	public boolean removeMoney(Player p, double amount) {
		Economy economy = pl.getEconomy();
		
		double balance = economy.getBalance(p);
		
		if (balance > amount) {
			economy.withdrawPlayer(p, amount);
			return true;
		}
		return false;
	}
	public static boolean removeMoneyCity(City city, double amount) {
		
		double balance = city.getBalance();
		
		if (balance >= amount) {
			city.setBalance(city.getBalance() - amount);
			return true;
		}
		return false;
	}
	
	public static boolean attemptClaim(Civilized pl, Player p, City city, int radius) {
		return attemptClaim(pl, p, city, radius, p.getLocation());
	}
	
	public static boolean attemptClaim(Civilized pl, Player p, City city, int radius, Location location) {
		
		//TODO: Check if member has permissions to claim
		
		System.out.println("MAXCLAIMS: " + city.getMaxClaimChunks());
		
		if (city.getChunks().size() >= city.getMaxClaimChunks()) {
			p.sendMessage(ChatColor.RED + "Your city has reached the maximum allowed claim chunks.");
			return false;
		}
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		
		/*if (radius > 0) {
			if (radius > 6) radius = 6;
			
			radius = radius * 2 + 1;

			
			Chunk middle = location.getChunk();
			int chunkX = middle.getX();
			int chunkZ = middle.getZ();
			
			int successful = 0;
			
			// (di, dj) is a vector - direction in which we move right now
		    int di = 1;
		    int dj = 0;
		    // length of current segment
		    int segment_length = 1;

		    // current position (i, j) and how much of current segment we passed
		    int i = 0;
		    int j = 0;
		    int segment_passed = 0;
		    for (int k = 0; k < Math.pow(radius, 2); ++k) {
		        // make a step, add 'direction' vector (di, dj) to current position (i, j)
		        i += di;
		        j += dj;
		        ++segment_passed;
		        
		        // DO STUFF
		        Chunk chunk = location.getWorld().getChunkAt(chunkX - i, chunkZ - j);
				p.sendMessage(ChatColor.GRAY + "Trying to claim " + i + ", " + j);
				if (city.isChunkAdjacent(chunk) && !city.getChunks().contains(chunk) && !CityManager.isChunkTooCloseToCity(city, chunk)){
					
					if (city.getChunks().size() >= city.getMaxClaimChunks()) {
						p.sendMessage(ChatColor.RED + "Failed due to max chunk limit");
						break;
					}
					if(!removeMoneyCity(city, SettingsConfiguration.PRICE_PER_CLAIM)) {
						p.sendMessage(ChatColor.RED + "Failed due to balance");
						break;
					}
					city.addChunk(chunk);
					successful++;
				} else {
					p.sendMessage(ChatColor.RED + "Failed due to checks");
				}
				
				//END STUFF

		        if (segment_passed == segment_length) {
		            // done with current segment
		            segment_passed = 0;

		            // 'rotate' directions
		            int buffer = di;
		            di = -dj;
		            dj = buffer;

		            // increase segment length if necessary
		            if (dj == 0) {
		                ++segment_length;
		            }
		        }
		    }
			
			

			double price = SettingsConfiguration.PRICE_PER_CLAIM * successful;
			p.sendMessage(ChatColor.GREEN + String.valueOf(successful) + " chunks were claimed for " + pl.getEconomy().format(price));
			return false;
		}*/
		
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
		
		if (!CityManager.canClaimAt(chunk)) {
			p.sendMessage(ChatColor.RED + "You cannot claim a chunk there.");
			return false;
		}
		
		city.addChunk(chunk);
		
		return true;
	}
	
	

}
