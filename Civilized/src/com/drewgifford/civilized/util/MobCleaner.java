package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.ToggleLevel;
import com.drewgifford.civilized.plot.Plot;

public class MobCleaner {
	
	
	private BukkitTask taskId;
	private Civilized pl;
	
	public MobCleaner(Civilized pl) {
		this.pl = pl;
	}
	
	public void run() {
		
		taskId = new BukkitRunnable() {
			@Override
			public void run() {
				for(World w : Bukkit.getWorlds()) {
					for (Entity entity : w.getEntities()) {
						if (!(entity instanceof Monster)) continue;
						
						Location location = entity.getLocation();
						City city = CityManager.getCityFromLocation(location);
						
						if (city == null) return;
						
						Chunk chunk = location.getChunk();
						Plot plot = city.getChunkPlotMap().get(chunk);
						
						ToggleLevel level;
						
						if (plot.getToggles().mobs == ToggleLevel.DEFAULT) {
							level = city.getToggles().mobs;
						}
						else {
							level = plot.getToggles().mobs;
						}
						
						if(level == ToggleLevel.DISABLED) entity.remove();
					}
				}
			}

		}.runTaskTimer(pl, 20L, 20L);
		
		
	}

}
