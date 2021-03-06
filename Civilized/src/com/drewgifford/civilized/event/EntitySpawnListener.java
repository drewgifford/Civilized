package com.drewgifford.civilized.event;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.ToggleLevel;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

public class EntitySpawnListener implements Listener {
	
	Civilized pl;
	
	public EntitySpawnListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		
		if (!(e.getEntity() instanceof Monster)) return;
		
		Location loc = e.getLocation();
		
		City city = CityManager.getCityFromLocation(loc);
		if (city == null) return;
		
		Chunk chunk = loc.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		ToggleLevel level;
		
		if (plot.getToggles().mobs == ToggleLevel.DEFAULT) {
			level = city.getToggles().mobs;
		}
		else {
			level = plot.getToggles().mobs;
		}
		
		if(level == ToggleLevel.DISABLED) e.setCancelled(true);
		
		
		
	}

}
