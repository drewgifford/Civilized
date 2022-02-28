package com.drewgifford.civilized.event;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.subcommands.civ.CivMapCommand;

public class PlayerMoveListener implements Listener {
	
	private Civilized pl;
	
	private HashMap<UUID, Chunk> lastChunks;
	private HashMap<UUID, City> lastCity;
	
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
			
			changeChunk(p);
			
			
			// Check if the player entered a city
			City currentCity = null;
			
			// Check if the chunk is in a city
			for (City city : Civilized.cities) {
				if (city.getChunks().contains(chunk)) {
					// Inside a city
					
					currentCity = city;
					break;
				}
			}
			
			if (lastCity.get(uuid) != currentCity) {
				if(currentCity == null) {
					lastCity.remove(uuid);
				} else { 
					lastCity.put(uuid, currentCity);
				}
			}
		}
		
	}
	
	private void changeChunk(Player p) {
		if (Civilized.activeMaps.contains(p.getUniqueId())) {
			CivMapCommand.sendMapUpdate(p);
		}
	}

}
