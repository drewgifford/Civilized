package com.drewgifford.civilized.permissions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

public class CivilizedPermissions {
	
	public PermissionLevel block_break;
	public PermissionLevel block_place;
	public PermissionLevel interact;
	public PermissionLevel item_use;
	
	public CivilizedPermissions() {
		block_break = PermissionLevel.MEMBER;
		block_place = PermissionLevel.MEMBER;
		interact = PermissionLevel.MEMBER;
		item_use = PermissionLevel.MEMBER;
	}
	
	public CivilizedPermissions(PermissionLevel level) {
		block_break = level;
		block_place = level;
		interact = level;
		item_use = level;
	}
	
	private static List<String> valid = Arrays.asList(
			"BREAK", "PLACE", "INTERACT", "USE", "ALL");
	
	public static boolean isValidString(String string) {
		return valid.contains(string.toUpperCase());
	}
	
	public static String getOptionsString() {
		return String.join(", ", valid);
	}
	
	public void setOption(String option, PermissionLevel level) {
		switch(option.toUpperCase()) {
			case "BREAK":
				this.block_break = level;
				break;
			case "PLACE":
				this.block_place = level;
				break;
			case "INTERACT":
				this.interact = level;
				break;
			case "USE":
				this.item_use = level;
				break;
			case "ALL":
				this.item_use = level;
				this.interact = level;
				this.block_break = level;
				this.block_place = level;
				break;
			default:
				break;
		}
	}
	
	public PermissionLevel getOption(String option) {
		switch(option.toUpperCase()) {
			case "BREAK":
				return this.block_break;
			case "PLACE":
				return this.block_place;
			case "INTERACT":
				return this.interact;
			case "USE":
				return this.item_use;
			default:
				return null;
		}
	}
	
	public void sendOptionValues(Player p) {
		for(String s : valid) {
			PermissionLevel option = getOption(s);
			if (option == null) continue;
			p.sendMessage(ChatColor.AQUA + s + ": " + ChatColor.DARK_AQUA + option.toString().toLowerCase());
		}
	}
	
	public Map<?, ?> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("block_break", this.block_break.toString());
		map.put("block_place", this.block_place.toString());
		map.put("interact", this.interact.toString());
		map.put("item_use", this.item_use.toString());
		return map;
	}
	
	public static CivilizedPermissions fromMap(Map<?, ?> map) {
		CivilizedPermissions permissions = new CivilizedPermissions();
		
		permissions.block_break = PermissionLevel.valueOf((String) map.get("block_break"));
		permissions.block_place = PermissionLevel.valueOf((String) map.get("block_place"));
		permissions.interact = PermissionLevel.valueOf((String) map.get("interact"));
		permissions.item_use = PermissionLevel.valueOf((String) map.get("item_use"));
		
		return permissions;
	}
	
	public static PermissionLevel determinePriority(PermissionLevel plotPermission, PermissionLevel cityPermission) {
		PermissionLevel priority;
		if(plotPermission == PermissionLevel.DEFAULT) {
			priority = cityPermission;
		} else {
			priority = plotPermission;
		}
		return priority;
	}
	
	public static boolean hasPermissionInPlot(Player p, PermissionLevel level, PermissionLevel cityLevel, Plot plot) {
		
		City city = plot.getCity();
		Chunk chunk = plot.getChunk();
		UUID owner = plot.getOwner();
		Set<UUID> trusted = plot.getTrusted();
		
		OfflinePlayer ownerPlayer = null;
		if(owner != null) {
			ownerPlayer = Bukkit.getOfflinePlayer(owner);
			if (owner.equals(p.getUniqueId())) return true;
		}
		
		boolean hasPermission = false;
		
		for(PermissionLevel l : PermissionLevel.values()) {
			
			int priority = l.getPriority();
			
			
			
			if (cityLevel.getPriority() <= 7) {
				if (city.getOfficers().contains(p.getUniqueId())) return true;
			}
			if (cityLevel.getPriority() == 8) {
				if(city.getOwner().equals(p.getUniqueId())) return true;
			}
			
			
			if (priority < level.getPriority()) continue;
			
			
			
			switch(level) {
			
				case OUTSIDER:
					return true;
				case MEMBER:
					if (city.getPlayers().contains(p.getUniqueId())) return true;
					break;
				case TRUSTED:

					if (trusted.contains(p.getUniqueId())) return true;
					if (owner != null) {
						CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(owner);
						if (cp != null) {
							if (cp.getTrusted().contains(p.getUniqueId())) return true;
						}
					}
					break;	
				case ALLY:
					// TODO: Add ally check
					if (city.getNation() != null && city.getNation().getPlayers().contains(p.getUniqueId())) return true;
					break;
				case NATION_MEMBER:
					if (city.getNation() != null && city.getNation().getPlayers().contains(p.getUniqueId())) return true;
					break;
				case NATION_OFFICER:
					if (city.getNation() != null && (city.getNation().getOfficers().contains(p.getUniqueId()) || city.getNation().getOwner().equals(p.getUniqueId()))) return true;
					break;
				case NATION_OWNER:
					if (city.getNation() != null && city.getNation().getOwner().equals(p.getUniqueId())) return true;
					break;
				case OFFICER:
					if (city.getOfficers().contains(p.getUniqueId())) return true;
					break;
				case OWNER:
					if (city.getOfficers().contains(p.getUniqueId()) || plot.getOwner().equals(p.getUniqueId())) return true;
					break;
				default:
					continue;
		
			}
		}
		return hasPermission;
		
	}

}
