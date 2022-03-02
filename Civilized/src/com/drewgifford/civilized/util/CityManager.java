package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

import net.md_5.bungee.api.ChatColor;

public class CityManager {
	
	public static City getCityFromName(String cityName) {
		cityName = cityName.replace(' ', '_');
		
		for (City city : Civilized.cities) {
			if (city.getName().equalsIgnoreCase(cityName)) {
				return city;
			}
		}
		return null;
	}
	
	public static City getCityFromLocation(Location location) {
		return getCityFromLocation(location.getChunk());
	}
	
	public static City getCityFromLocation(Chunk chunk) {
		for (City city: Civilized.cities) {
			if (city.getChunks().contains(chunk)) {
				return city;
			}
		}
		return null;
	}
	
	public static boolean nameExists(String name) {
		for (City city : Civilized.cities) {
			if (city.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static void deleteCity(Civilized pl, City city) {
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "The city of " + city.getNameWithSpaces() + " has been disbanded.");
		Civilized.cities.remove(city);
		city = null;
		
		pl.citiesConfiguration.write();
		
	}

}
