package com.drewgifford.civilized.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

public class CivilizedPlayer {
	
	public static CivilizedPlayer registerCivilizedPlayer(OfflinePlayer p) {
		CivilizedPlayer cp = new CivilizedPlayer(p.getUniqueId());
		Civilized.registeredPlayers.add(cp);
		return cp;
	}
	
	public static CivilizedPlayer getCivilizedPlayer(OfflinePlayer offlinePlayer) {
		for (CivilizedPlayer cp : Civilized.registeredPlayers) {
			if (cp.getUniqueId() == offlinePlayer.getUniqueId()) return cp;
		}
		return registerCivilizedPlayer(offlinePlayer);
	}
	
	private UUID p;
	
	public CivilizedPlayer(UUID p) {
		this.p = p;

	}
	
	public City getCity() {
		for (City city : Civilized.cities) {
			
			if (city.getPlayers().contains(p)) {
				return city;
			}
		}
		return null;
	}
	
	public UUID getUniqueId() {
		return this.p;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(this.getUniqueId());
	}

}
