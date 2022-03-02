package com.drewgifford.civilized.event;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.permissions.ToggleLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class PistonListener implements Listener {
	
	Civilized pl;
	
	public PistonListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e) {
		boolean hasTrue = false;
		boolean hasFalse = false;
		
		for(Block b : e.getBlocks()) {
			Location locationA = b.getLocation().add(e.getDirection().getDirection());
			Location locationB = b.getLocation();
			
			Location[] locs = new Location[] {locationA, locationB};
			
			for(Location location : locs) {
				
				City city = CityManager.getCityFromLocation(location);
				
				if(city != null) {
					hasFalse = true;
				} else {
					hasTrue = true;
				}
				
			}
			
		}
		
		if (hasTrue && hasFalse) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		
		boolean hasTrue = false;
		boolean hasFalse = false;
		
		for(Block b : e.getBlocks()) {
			
			Location locationA = b.getLocation().add(e.getDirection().getDirection());
			Location locationB = b.getLocation();
			
			Location[] locs = new Location[] {locationA, locationB};
			
			for(Location location : locs) {
				
				City city = CityManager.getCityFromLocation(location);
				
				if(city != null) {
					hasFalse = true;
				} else {
					hasTrue = true;
				}
				
			}
			
		}
		
		if (hasTrue && hasFalse) {
			e.setCancelled(true);
		}
	}

}
