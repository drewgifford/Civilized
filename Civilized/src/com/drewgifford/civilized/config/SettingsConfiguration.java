package com.drewgifford.civilized.config;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.drewgifford.civilized.Civilized;

public class SettingsConfiguration extends CivilizedConfiguration {

	public SettingsConfiguration(Civilized pl, String fileName, boolean doAutoSave) {
		super(pl, fileName, doAutoSave);
	}

	@Override
	public CivilizedConfiguration load() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
		
		CONFIG_VERSION = getValue(config.getInt("configVersion"), -1);
		
		System.out.println("CONFIG VERSION: " + CONFIG_VERSION);
		
		if (CONFIG_VERSION < 1) {
			System.out.println("ERROR LOADING CIVILIZED: Invalid Configuration version");
			pl.getPluginLoader().disablePlugin(pl);
			return null;
		}
		

		
		ConfigurationSection prices = config.getConfigurationSection("prices");
		
		NEW_CITY_COST = getValue(prices.getDouble("newCity"), 0.0);
		NEW_NATION_COST = getValue(prices.getDouble("newNation"), 0.0);
		PRICE_PER_CLAIM = getValue(prices.getDouble("claim"), 0.0);
	
		
		ALLOW_HOME_TELEPORT = getValue(config.getBoolean("allowHomeTeleport"), true);
		
		// -------------------------- //
		
		ConfigurationSection playerSlotOptions = config.getConfigurationSection("playerSlots");
		ConfigurationSection maxClaimOptions = config.getConfigurationSection("maxClaims");
		
		PRICE_PER_MAXCLAIM = getValue(maxClaimOptions.getDouble("price"), 0.0);
		PRICE_PER_PLAYER_SLOT = getValue(playerSlotOptions.getDouble("price"), 0.0);
		
		MAXCLAIM_PURCHASE_SIZE = getValue(maxClaimOptions.getInt("purchaseAmount"), 8);
		PLAYER_SLOT_PURCHASE_SIZE = getValue(playerSlotOptions.getInt("purchaseAmount"), 1);
		
		INITIAL_MAXCLAIMS = getValue(maxClaimOptions.getInt("initialAmount"), 8);
		INITIAL_PLAYER_SLOTS = getValue(playerSlotOptions.getInt("initialAmount"), 3);
		
		MAXCLAIM_PRICE_EXP_FACTOR = getValue(maxClaimOptions.getDouble("increaseFactor"), 1.0);
		PLAYER_SLOT_PRICE_EXP_FACTOR = getValue(playerSlotOptions.getDouble("increaseFactor"), 1.0);
		return this;
	}
	
	public static int CONFIG_VERSION;
	
	public static double NEW_CITY_COST;
	public static double NEW_NATION_COST;
	
	public static double PRICE_PER_MAXCLAIM;
	public static int MAXCLAIM_PURCHASE_SIZE;
	public static int INITIAL_MAXCLAIMS;
	
	public static boolean MAXCLAIM_PRICE_EXP;
	public static double MAXCLAIM_PRICE_EXP_FACTOR;
	
	public static double PRICE_PER_CLAIM;
	
	public static double PRICE_PER_PLAYER_SLOT;
	public static int PLAYER_SLOT_PURCHASE_SIZE;
	public static int INITIAL_PLAYER_SLOTS;
	
	public static boolean PLAYER_SLOT_PRICE_EXP;
	public static double PLAYER_SLOT_PRICE_EXP_FACTOR;
	
	public static boolean ALLOW_HOME_TELEPORT;
	
	
	@Override
	public void write() {
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getValue(Object setTo, T fallback) {
		//return setTo != null ? (T) setTo : fallback;
		return (T) setTo;
	}
	
	@SuppressWarnings("unchecked")
	private <T, K> HashMap<T, K> getHashMap(Object o){
		if (o == null || !(o instanceof Dictionary)) return new HashMap<T, K>();
		
		return (HashMap<T, K>) o;
	}
	
	

}
