package com.drewgifford.civilized.nation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

import net.md_5.bungee.api.ChatColor;

public class Nation {

	private Set<UUID> officers;
	private City capital;
	private String board;
	private String name;
	
	public Nation(List<City> cities, City capital, String name) {
		this.capital = capital;
		
		if (capital != null) {
			capital.setNation(this);
		}
		
		this.allies = new HashSet<Nation>();
		this.officers = new HashSet<UUID>();
		this.board = "Change this message with /nation board <message>";
		this.name = name;
	}
	
	private Set<Nation> allies;
	
	public Set<Nation> getAllies(){
		return this.allies;
	}
	public void setAllies(Set<Nation> allies) {
		this.allies = allies;
	}
	
	public UUID getOwner() {
		return this.capital.getOwner();
	}
	public Set<City> getCities(){
		Set<City> cities = new HashSet<City>();
		for (City city : Civilized.cities) {
			if (city.getNation() != null && city.getNation().equals(this)) {
				cities.add(city);
			}
		}
		return cities;
	}
	
	public City getCapital() {
		return this.capital;
	}
	public void setCapital(City capital) {
		this.capital = capital;
		this.capital.setNation(this);
	}
	
	public Set<UUID> getPlayers(){
		
		Set<UUID> players = new HashSet<UUID>();
		
		for (City city : getCities()) {
			players.addAll(city.getPlayers());
		}
		return players;
		
	}
	
	public Set<UUID> getOfficers() {
		return this.officers;
	}
	public void setOfficers(Set<UUID> officers) {
		this.officers = officers;
	}
	
	public String getBoard() {
		return this.board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameWithSpaces() {
		return this.name.replace('_', ' ');
	}
	
	public boolean hasOfficerPermission(UUID uuid) {
		return (this.officers.contains(uuid) || this.getCapital().getOwner().equals(uuid));
	}
	
	public void addOfficer(UUID uuid) {
		if (!this.getOfficers().contains(uuid)) this.officers.add(uuid);
	}
	public void removeOfficer(UUID uuid) {
		this.getOfficers().remove(uuid);
	}
	public void setName(String name) {
		this.name = name;
	}
	public void removeOfficers(Set<UUID> officers) {
		this.officers.removeAll(officers);
	}
	
	public Location getHome() {
		return this.capital.getHome();
	}

}
