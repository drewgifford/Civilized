package com.drewgifford.civilized.plot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;

import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.config.CitiesConfiguration;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.permissions.ToggleLevel;
import com.drewgifford.civilized.util.CityManager;

public class Plot {
	
	private Chunk chunk;
	private UUID owner;
	private Set<UUID> trusted;
	private CivilizedPermissions permissions;
	private CivilizedToggles toggles;
	private String name;
	
	private boolean forSale;
	private double price;
	
	public Plot(Chunk chunk) {
		this.owner = null;
		this.chunk = chunk;
		
		this.trusted = new HashSet<UUID>();
		this.permissions = new CivilizedPermissions(PermissionLevel.DEFAULT);
		this.toggles = new CivilizedToggles(ToggleLevel.DEFAULT);
		this.forSale = false;
		this.price = 0.0;
		this.name = "";
		
	}
	
	public City getCity() {
		return CityManager.getCityFromLocation(this.chunk);
	}
	
	public boolean isForSale() {
		return this.forSale;
	}
	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}
	
	public Chunk getChunk() {
		return this.chunk;
	}
	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public CivilizedPermissions getPermissions() {
		return this.permissions;
	}
	public void setPermissions(CivilizedPermissions permissions) {
		this.permissions = permissions;
	}
	
	public CivilizedToggles getToggles() {
		return this.toggles;
	}
	public void setToggles(CivilizedToggles toggles) {
		this.toggles = toggles;
	}
	
	public Set<UUID> getTrusted(){
		return this.trusted;
	}
	public void setTrusted(Set<UUID> trusted) {
		this.trusted = trusted;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<?, ?> toMap(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (owner == null) { map.put("owner", null); } else {
			map.put("owner", this.owner.toString());
		}
		map.put("trusted", CitiesConfiguration.uniqueIdsToStrings(this.trusted));
		map.put("permissions", this.permissions.toMap());
		map.put("toggles", this.toggles.toMap());
		map.put("forSale", this.forSale);
		map.put("price", this.price);
		map.put("name", this.name);
		
		return map;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Plot fromMap(Map<?, ?> map, Chunk chunk) {
		
		Plot plot = new Plot(chunk);
		
		if (map.get("owner") == null) {
			plot.setOwner(null);
		} else {
			plot.setOwner(UUID.fromString((String) map.get("owner")));
		}
		plot.setTrusted(new HashSet<UUID>(CitiesConfiguration.stringsToUniqueIds((Collection<String>) map.get("trusted"))));
		plot.setPermissions(CivilizedPermissions.fromMap((Map<?, ?>) map.get("permissions")));
		plot.setToggles(CivilizedToggles.fromMap((Map<?,?>) map.get("toggles")));
		plot.setForSale((boolean) map.get("forSale"));
		plot.setPrice((double) map.get("price"));
		plot.setName((String) map.get("name"));

		
		return plot;
	}

}
