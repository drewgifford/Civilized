package com.drewgifford.civilized.event;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class BlockPlaceListener implements Listener {
	
	Civilized pl;
	
	public BlockPlaceListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		Location location = e.getBlock().getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().block_place;
		PermissionLevel cityPermission = city.getPermissions().block_place;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
		
		if(!hasPermission) {
			p.sendMessage(ChatColor.RED + "You do not have permission to place blocks here.");
			e.setCancelled(true);
		}
		
	}

}
