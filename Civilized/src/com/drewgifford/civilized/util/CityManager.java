package com.drewgifford.civilized.util;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;

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

}
