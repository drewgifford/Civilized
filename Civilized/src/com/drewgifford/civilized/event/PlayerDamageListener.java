package com.drewgifford.civilized.event;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

public class PlayerDamageListener implements Listener {
	
	Civilized pl;
	
	public PlayerDamageListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		
		if (!(e.getDamager() instanceof Player && e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getDamager();
		

		Location[] locations = new Location[] {e.getEntity().getLocation(), e.getDamager().getLocation()};
		
		for (Location location : locations) {
			
			City city = CityManager.getCityFromLocation(location);
			
			if (city == null) continue;
			
			Chunk chunk = location.getChunk();
			Plot plot = city.getChunkPlotMap().get(chunk);
			
			ToggleLevel allowed = plot.getToggles().pvp;
			
			if (allowed.equals(ToggleLevel.DEFAULT)) {
				allowed = city.getToggles().pvp;
			}
			
			if(allowed.equals(ToggleLevel.DISABLED)) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You do not have permission PVP here.");
				return;
			}
			
		}
		
	}

}
