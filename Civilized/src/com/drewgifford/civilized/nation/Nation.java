package com.drewgifford.civilized.nation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.drewgifford.civilized.city.City;

public class Nation {
	
	private List<City> cities;
	private City capital;
	
	public Nation(List<City> cities, City capital) {
		this.cities = cities;
		this.capital = capital;
	}
	
	public UUID getOwner() {
		return this.capital.getOwner();
	}
	public List<City> getCities(){
		return this.cities;
	}
	public City getCapital() {
		return this.capital;
	}
	public List<UUID> getPlayers(){
		
		List<UUID> players = new ArrayList<UUID>();
		
		for (City city : getCities()) {
			players.addAll(city.getPlayers());
		}
		return players;
		
	}

}
