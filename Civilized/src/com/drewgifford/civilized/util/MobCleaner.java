package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

public class MobCleaner {
	
	
	private int taskId;
	private Civilized pl;
	
	public MobCleaner(Civilized pl) {
		this.pl = pl;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new BukkitRunnable() {

			@Override
			public void run() {
				
				for(World w : Bukkit.getWorlds()) {
					for (Entity entity : w.getEntities()) {
						if (!(entity instanceof Monster)) continue;
						
						Location location = entity.getLocation();
						City city = CityManager.getCityFromLocation(location);
						
						if (city == null) continue;
						
						if (city.getToggles().mobs == false) {
							entity.remove();
						}
					}
				}
				
			}
			
		}, 0L, 20L);
		
		
	}

}
