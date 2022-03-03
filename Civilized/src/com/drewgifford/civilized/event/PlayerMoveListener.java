package com.drewgifford.civilized.event;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.subcommands.city.CityClaimCommand;
import com.drewgifford.civilized.command.subcommands.civ.CivMapCommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerMoveListener implements Listener {
	
	private Civilized pl;
	
	private HashMap<UUID, Chunk> lastChunks = new HashMap<UUID, Chunk>();
	private HashMap<UUID, City> lastCities = new HashMap<UUID, City>();
	private HashMap<UUID, Plot> lastPlot = new HashMap<UUID, Plot>();
	
	public PlayerMoveListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		
		Chunk chunk = e.getTo().getChunk();
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (lastChunks.get(uuid) != chunk) {
			// Player entered a new chunk
			lastChunks.put(uuid, chunk);
			
			changeChunk(p, e.getTo());
			
			
			// Check if the player entered a city
			City currentCity = null;
			Plot currentPlot = null;
			
			// Check if the chunk is in a city
			for (City city : Civilized.cities) {
				if (city.getChunks().contains(chunk)) {
					// Inside a city
					currentPlot = city.getChunkPlotMap().get(chunk);
					currentCity = city;
					break;
				}
			}
			
			if (lastCities.get(uuid) != currentCity) {
				lastCities.put(uuid, currentCity);
				
				changeCity(p, currentCity);
			}
			if (lastPlot.get(uuid) != currentPlot) {
				lastPlot.put(uuid, currentPlot);
				changePlot(p, currentPlot);
			}
		}
		
	}
	
	private void changeChunk(Player p, Location loc) {
		if (Civilized.activeAutoClaims.contains(p.getUniqueId())) {
			Chunk chunk = loc.getChunk();
			if (!CivilizedPlayer.getCivilizedPlayer(p).getCity().getChunks().contains(chunk)) {
				p.sendMessage(ChatColor.GRAY + "Attempting automatic claim. Type /city claim off to disable.");
				CityClaimCommand.attemptClaim(pl, p, CivilizedPlayer.getCivilizedPlayer(p).getCity(), 0, loc);
			}
		}
		if (Civilized.activeMaps.contains(p.getUniqueId())) {
			CivMapCommand.sendMapUpdate(p, loc);
		}
	}
	
	private void changeCity(Player p, City city) {
		
		String name = city == null ? (ChatColor.DARK_GREEN + "Wilderness") : (ChatColor.GREEN + city.getNameWithSpaces());
		System.out.println("Changed city");
		p.sendTitle(
			ChatColor.WHITE + "",
			name,
			5,
			20,
			5
		);
	}
	
	private void changePlot(Player p, Plot plot) {
		
		if(plot == null) return;
		
		UUID owner = plot.getOwner();
		boolean forSale = plot.isForSale();
		
		boolean isHome = plot.getCity().getHomeChunk().equals(plot.getChunk());
		
		String home = isHome ? (ChatColor.AQUA + "[HOME] ") : "";
		
		if(owner == null && !forSale) {
			if(isHome) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(home));
			}
			return;
		}
		String ownerName = "Unowned";
		if (owner != null) {
			ownerName = Bukkit.getOfflinePlayer(owner).getName() + "'s plot";
		}
		String forSaleText = ChatColor.GOLD + "";
		
		if (forSale) {
			forSaleText += "FOR SALE: " + pl.getEconomy().format(plot.getPrice());
		}
		
		
		
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + ownerName + " " + home + forSaleText));
	}

}
