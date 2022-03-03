package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.nation.Nation;

import net.md_5.bungee.api.ChatColor;

public class NationManager {
	
	public static Nation getNationFromName(String nationName) {
		if (nationName == null) return null;
		
		nationName = nationName.replace(' ', '_');
		
		for (Nation nation : Civilized.nations) {
			if (nation.getName().equalsIgnoreCase(nationName)) {
				return nation;
			}
		}
		return null;
	}
	
	public static boolean nameExists(String name) {
		for (Nation nation : Civilized.nations) {
			if (nation.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static void deleteNation(Civilized pl, Nation nation) {
		
		for (City city : nation.getCities()) {
			city.setNation(null);
		}
		
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "The nation of " + nation.getNameWithSpaces() + " has been disbanded.");
		Civilized.nations.remove(nation);
		nation = null;
		
		pl.citiesConfiguration.write();
		
	}

	public static void moveCapital(Civilized pl, Nation nation, City city) {
		
		nation.setCapital(city);
		
	}

}
