package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

import net.md_5.bungee.api.ChatColor;

public class CityManager {
	
	public static City getCityFromName(String cityName) {
		if (cityName == null) return null;
		
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
		
		if (city.getNation() != null) {
			city.getNation().getCities().remove(city);
		}
		
		city = null;
		
		pl.citiesConfiguration.write();
		
	}
	
	public static boolean isChunkTooCloseToCity(City city, Chunk chunk) {
		int minDistance = 3;
		for (City c : Civilized.cities) {
			if (c.equals(city)) continue;
			if (c.hasChunk(chunk)) {
				return true;
			}
			if (c.isChunkNearby(chunk, minDistance)) {
				return true;
			}
		}
		return false;
	}

}
