package com.drewgifford.civilized.permissions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CivilizedToggles {
	
	public boolean pvp, explosion, fire, mobs;
	
	public CivilizedToggles() {
		this.pvp = true;
		this.explosion = false;
		this.fire = false;
		this.mobs = false;
	}
	
	private static List<String> valid = Arrays.asList(
			"PVP", "EXPLOSION", "FIRE", "MOBS");
	
	public static boolean isValidString(String string) {
		return valid.contains(string.toUpperCase());
	}
	
	public static String getOptionsString() {
		return String.join(", ", valid);
	}
	
	public void setOption(String option, boolean level) {
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
			boolean option = getOption(s);
			p.sendMessage(ChatColor.AQUA + s + ": " + ChatColor.DARK_AQUA + option);
		}
	}
	
	public boolean getOption(String option) {
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
				return false;
		}
	}
	
	public Map<?, ?> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("pvp", this.pvp);
		map.put("explosion", this.explosion);
		map.put("fire", this.fire);
		map.put("mobs", this.mobs);
		return map;
	}
	
	public static CivilizedToggles fromMap(Map<?, ?> map) {
		CivilizedToggles toggles = new CivilizedToggles();
		
		toggles.pvp = (boolean) map.get("pvp");
		toggles.explosion = (boolean) map.get("explosion");
		toggles.fire = (boolean) map.get("fire");
		toggles.mobs = (boolean) map.get("mobs");
		
		return toggles;
	}
}
