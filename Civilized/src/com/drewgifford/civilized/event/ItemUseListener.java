package com.drewgifford.civilized.event;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class ItemUseListener implements Listener {
	
	Civilized pl;
	
	public ItemUseListener(Civilized pl) {
		this.pl = pl;
	}
	
	private void handle(Cancellable e, Player p) {
		
		Location location = p.getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().item_use;
		PermissionLevel cityPermission = city.getPermissions().item_use;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
		
		if(!hasPermission) {
			p.sendMessage(ChatColor.RED + "You do not have permission to use items here.");
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onBowShoot(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		handle(e, p);
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		Player p = e.getPlayer();
		handle(e, p);
	}
	
	@EventHandler
	public void onThrow(ProjectileLaunchEvent e) {
		if (!(e.getEntity().getShooter() instanceof Player)) return;
		Player p = (Player) e.getEntity().getShooter();
		handle(e, p);
	}

}
