package com.drewgifford.civilized.permissions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
			"BREAK", "PLACE", "INTERACT", "USE");
	
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

}
