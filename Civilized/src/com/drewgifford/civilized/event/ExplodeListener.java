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

public class ExplodeListener implements Listener {
	
	Civilized pl;
	
	public ExplodeListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(EntityExplodeEvent e) {
		
		for(Block b : e.blockList().toArray(new Block[e.blockList().size()])) {
			Location location = b.getLocation();
			Chunk chunk = b.getChunk();
			
			City city = CityManager.getCityFromLocation(location);
			if (city == null) continue;
			
			Plot plot = city.getChunkPlotMap().get(chunk);
			
			ToggleLevel level;
			
			if (plot.getToggles().explosion == ToggleLevel.DEFAULT) {
				level = city.getToggles().explosion;
			}
			else {
				level = plot.getToggles().explosion;
			}
			
			if(level == ToggleLevel.DISABLED) e.blockList().remove(b);
			
		}
		
		
		
	}

}
