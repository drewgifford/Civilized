package com.drewgifford.civilized.permissions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CivilizedToggles {
	
	public ToggleLevel pvp, explosion, fire, mobs;
	
	public CivilizedToggles() {
		this.pvp = ToggleLevel.DISABLED;
		this.explosion = ToggleLevel.DISABLED;
		this.fire = ToggleLevel.DISABLED;
		this.mobs = ToggleLevel.DISABLED;
	}
	
	public CivilizedToggles(ToggleLevel defaultOption) {
		this.pvp = defaultOption;
		this.explosion = defaultOption;
		this.fire = defaultOption;
		this.mobs = defaultOption;
	}
	
	private static List<String> valid = Arrays.asList(
			"PVP", "EXPLOSION", "FIRE", "MOBS");
	
	public static boolean isValidString(String string) {
		return valid.contains(string.toUpperCase());
	}
	
	public static String getOptionsString() {
		return String.join(", ", valid);
	}
	
	public void setOption(String option, ToggleLevel level) {
		switch(option.toUpperCase()) {
			case "PVP":
				this.pvp = level;
				break;
			case "EXPLOSION":
				this.explosion = level;
				break;
			case "FIRE":
				this.fire = level;
				break;
			case "MOBS":
				this.mobs = level;
				break;
			default:
				break;
		}
	}
	
	public void sendOptionValues(Player p) {
		for(String s : valid) {
			ToggleLevel option = getOption(s);
			p.sendMessage(ChatColor.AQUA + s + ": " + ChatColor.DARK_AQUA + option.toString().toLowerCase());
		}
	}
	
	public ToggleLevel getOption(String option) {
		switch(option.toUpperCase()) {
			case "PVP":
				return this.pvp;
			case "EXPLOSION":
				return this.explosion;
			case "FIRE":
				return this.fire;
			case "MOBS":
				return this.mobs;
			default:
				return ToggleLevel.DISABLED;
		}
	}
	
	public Map<?, ?> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("pvp", this.pvp.toString());
		map.put("explosion", this.explosion.toString());
		map.put("fire", this.fire.toString());
		map.put("mobs", this.mobs.toString());
		return map;
	}
	
	public static CivilizedToggles fromMap(Map<?, ?> map) {
		CivilizedToggles toggles = new CivilizedToggles();
		
		toggles.pvp = ToggleLevel.valueOf((String) map.get("pvp"));
		toggles.explosion = ToggleLevel.valueOf((String) map.get("explosion"));
		toggles.fire = ToggleLevel.valueOf((String) map.get("fire"));
		toggles.mobs = ToggleLevel.valueOf((String) map.get("mobs"));
		
		return toggles;
	}
}
