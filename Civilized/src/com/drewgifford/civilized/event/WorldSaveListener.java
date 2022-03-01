package com.drewgifford.civilized.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

import com.drewgifford.civilized.Civilized;

public class WorldSaveListener implements Listener {

	Civilized pl;
	
	public WorldSaveListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent e) {
		
		// Save configuration on world save
		pl.citiesConfiguration.write();
		
	}
	
}
