package com.drewgifford.civilized.event;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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

public class InteractListener implements Listener {
	
	Civilized pl;
	
	public InteractListener(Civilized pl) {
		this.pl = pl;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onInteract(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
		
		Action a = e.getAction();
		
		if (!a.equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (!e.getClickedBlock().getType().isInteractable()) return;
		
		if (e.getClickedBlock() == null) return;
		Location location = e.getClickedBlock().getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().interact;
		PermissionLevel cityPermission = city.getPermissions().interact;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
	
		if(!hasPermission) {
			e.setUseItemInHand(Result.DENY);
			e.setUseInteractedBlock(Result.DENY);
			p.sendMessage(ChatColor.RED + "You do not have permission to interact with blocks here.");
		}
		
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e) {
		
		Player p = e.getPlayer();
		
		if (e.getRightClicked() == null) return;
		Location location = e.getRightClicked().getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().interact;
		PermissionLevel cityPermission = city.getPermissions().interact;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
	
		if(!hasPermission) {
			p.sendMessage(ChatColor.RED + "You do not have permission to interact with entities here.");
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onAtEntityInteract(PlayerInteractAtEntityEvent e) {
		
		Player p = e.getPlayer();
		
		if (e.getRightClicked() == null) return;
		Location location = e.getRightClicked().getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().interact;
		PermissionLevel cityPermission = city.getPermissions().interact;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
	
		if(!hasPermission) {
			p.sendMessage(ChatColor.RED + "You do not have permission to interact with entities here.");
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e) {
		
		if (!(e.getDamager() instanceof Player)) return;
		if (e.getEntity() instanceof Player) return;
		
		Player p = (Player) e.getDamager();
		
		Location location = e.getEntity().getLocation();
		
		City city = CityManager.getCityFromLocation(location);
		
		if (city == null) return;
		
		Chunk chunk = location.getChunk();
		Plot plot = city.getChunkPlotMap().get(chunk);
		
		PermissionLevel plotPermission = plot.getPermissions().interact;
		PermissionLevel cityPermission = city.getPermissions().interact;
		
		boolean hasPermission = CivilizedPermissions.hasPermissionInPlot(p, CivilizedPermissions.determinePriority(plotPermission, cityPermission), cityPermission, plot);
	
		if(!hasPermission) {
			p.sendMessage(ChatColor.RED + "You do not have permission to interact with entities here.");
			e.setCancelled(true);
		}
		
	}

}
