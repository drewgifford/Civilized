package com.drewgifford.civilized.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.util.NationManager;

public class CitiesConfiguration extends CivilizedConfiguration {
	
	public CitiesConfiguration(Civilized pl, String fileName, boolean doAutoSave) {
		super(pl, fileName, doAutoSave);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CivilizedConfiguration load() {
		
		this.config = YamlConfiguration.loadConfiguration(this.file);
		
		List<Map<?, ?>> citiesYml = config.getMapList("cities");
		List<Map<?, ?>> nationsYml = config.getMapList("nations");
		List<Map<?, ?>> playersYml = config.getMapList("players");
		
		Civilized.cities.clear();
		Civilized.cityInvites.clear();
		Civilized.nations.clear();
		
		Map<Nation, String> nationCapitalsTemp = new HashMap<Nation, String>();
		
		for (Map<?, ?> nationYml : nationsYml) {
			try {
				
				String name = (String) nationYml.get("name");
				String board = (String) nationYml.get("board");
				
				String capital = (String) nationYml.get("capital");
				
				Set<UUID> officers = (Set<UUID>) stringsToUniqueIds((List<String>) nationYml.get("officers"));
				
				Nation nation = new Nation(new ArrayList<City>(), null, name);
				nation.setBoard(board);
				nation.setOfficers(officers);
				
				Civilized.nations.add(nation);
				
				nationCapitalsTemp.put(nation, capital);
				
			} catch (Exception e) {
				System.out.println("Error loading nation");
				e.printStackTrace();
			}
		}
		
		for (Map<?, ?> cityYml : citiesYml) {
			
			try {
			
				String name = (String) cityYml.get("name");
				String board = (String) cityYml.get("board");
				
				UUID owner = UUID.fromString((String) cityYml.get("owner"));
				Set<UUID> officers = new HashSet<UUID>(stringsToUniqueIds((List<String>) cityYml.get("officers")));
				Set<UUID> players = new HashSet<UUID>(stringsToUniqueIds((List<String>) cityYml.get("players")));
				
				String nationName = (String) cityYml.get("nation");
				
				Nation nation = NationManager.getNationFromName(nationName);
				
				// Loop through chunks and get plot data
				List<Map<?, ?>> chunkData = (List<Map<?, ?>>) cityYml.get("chunks");
				
				Map<Chunk, Plot> chunksWithPlots = new HashMap<Chunk, Plot>();
				
				for(Map<?, ?> chunkYml : chunkData) {
					
					World w = Bukkit.getWorld((String) chunkYml.get("world"));
					int x = (int) chunkYml.get("x");
					int z = (int) chunkYml.get("z");
					
					Chunk c = w.getChunkAt(x, z);
					
					Plot p = Plot.fromMap((Map<?, ?>) chunkYml.get("plot"), c);
					
					chunksWithPlots.put(c, p);
					
				}
				
				CivilizedPermissions permissions = CivilizedPermissions.fromMap((Map<?,?>) cityYml.get("permissions"));
				CivilizedToggles toggles = CivilizedToggles.fromMap((Map<?,?>) cityYml.get("toggles"));
				
				int playerSlots = (int) cityYml.get("playerSlots");
				int maxClaimChunks = (int) cityYml.get("maxClaimChunks");
				
				double balance = (double) cityYml.get("balance");
				
				Location home = deserializeLocation((Map<?, ?>) cityYml.get("home"));
				if (home == null) throw new Exception();
				
				City city = new City(owner, name, home);
				
				city.setOfficers(officers);
				city.setPlayers(players);
				city.setChunks(chunksWithPlots);
				city.setHome(home);
				city.setBoard(board);
				city.setNation(nation);
				
				city.setPlayerSlots(playerSlots);
				city.setMaxClaimChunks(maxClaimChunks);
				city.setBalance(balance);
				
				city.setPermissions(permissions);
				city.setToggles(toggles);
				
				Civilized.cities.add(city);
				
				if (nationCapitalsTemp.get(nation) != null && nationCapitalsTemp.get(nation).equalsIgnoreCase(name)) {
					nation.setCapital(city);
				}
				
				
			
			} catch (Exception e) {
				System.out.println("Error loading city");
				e.printStackTrace();
				
				pl.getPluginLoader().disablePlugin(pl);
				return null;
			}
			
		}
		
		Civilized.registeredPlayers.clear();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			CivilizedPlayer.registerCivilizedPlayer(p);
		}
		
		for (Map<?, ?> playerYml : playersYml) {
			String u = (String) playerYml.get("uuid");
			if (u == null) continue;
			
			UUID uuid = UUID.fromString(u);
			
			Set<UUID> trusted = new HashSet<UUID>();
			if (playerYml.get("trusted") != null) {
				trusted = (Set<UUID>) stringsToUniqueIds((Collection<String>) playerYml.get("trusted"));
			}
			
			CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(uuid);
			if (cp != null) {
				cp.setTrusted(trusted);
			}
		}
		
		return this;
		
	}
	
	@Override
	public void write() {
		
		List<Map<?, ?>> cityData = new ArrayList<Map<?, ?>>();
		List<Map<?, ?>> nationData = new ArrayList<Map<?, ?>>();
		List<Map<?, ?>> playerData = new ArrayList<Map<?, ?>>();
		
		for (City city : Civilized.cities) {
			
			Map<String, Object> cityYml = new HashMap<String, Object>();
			
			cityYml.put("name", city.getName());
			cityYml.put("board", city.getBoard());
			
			cityYml.put("owner", city.getOwner().toString());
			cityYml.put("officers", new ArrayList<String>(uniqueIdsToStrings(city.getOfficers())));
			cityYml.put("players", new ArrayList<String>(uniqueIdsToStrings(city.getPlayers())));
			
			List<Map<?, ?>> chunks = new ArrayList<Map<?, ?>>();
			
			for (Chunk chunk : city.getChunkPlotMap().keySet()) {
				Plot plot = city.getChunkPlotMap().get(chunk);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("world", chunk.getWorld().getName());
				map.put("x", chunk.getX());
				map.put("z", chunk.getZ());
				
				map.put("plot", plot.toMap());
				
				chunks.add(map);
				
			}
			
			cityYml.put("chunks", chunks);
			
			cityYml.put("home", serializeLocation(city.getHome()));
			
			cityYml.put("permissions", city.getPermissions().toMap());
			cityYml.put("toggles", city.getToggles().toMap());
			
			cityYml.put("playerSlots", city.getPlayerSlots());
			cityYml.put("maxClaimChunks", city.getMaxClaimChunks());
			
			cityYml.put("balance", city.getBalance());
			
			if (city.getNation() != null) {
				cityYml.put("nation", city.getNation().getName());
			} else {
				cityYml.put("nation", null);
			}
			// Loop through chunks and get plot data
			
			cityData.add(cityYml);
		}
		
		
		for (Nation nation: Civilized.nations) {
			Map<String, Object> nationYml = new HashMap<String, Object>();
			
			nationYml.put("name", nation.getName());
			nationYml.put("board", nation.getBoard());
			
			nationYml.put("officers", new ArrayList<String>(uniqueIdsToStrings(nation.getOfficers())));
			
			if (nation.getCapital() != null) {
				nationYml.put("capital", nation.getCapital().getName());
			} else {
				nationYml.put("capital", null);
			}
			
			nationData.add(nationYml);
		}
		
		for (CivilizedPlayer cp : Civilized.registeredPlayers) {
			Map<String, Object> playerYml = new HashMap<String, Object>();
			
			playerYml.put("uuid", cp.getPlayer().getUniqueId().toString());
			playerYml.put("trusted", new ArrayList<String>(uniqueIdsToStrings(cp.getTrusted())));
			
			playerData.add(playerYml);
		}
		
		config.set("players", playerData);
		config.set("cities", cityData);
		config.set("nations", nationData);
		
		this.save();
		
	}
	
	
	public static Collection<UUID> stringsToUniqueIds(Collection<String> strings){
		Collection<UUID> uuids = new HashSet<UUID>();
		for(String s : strings) {
			uuids.add(UUID.fromString(s));
		}
		return uuids;
	}
	public static Collection<String> uniqueIdsToStrings(Collection<UUID> uuids){
		Collection<String> strings = new HashSet<String>();
		for(UUID u : uuids) {
			strings.add(u.toString());
		}
		return strings;
	}
	
	private Chunk stringToChunk(String s) {
		if (s == null) return null;
		
		String[] parts = s.split(",");
		
		if (parts.length < 3) return null;
		
		String worldName = parts[0];
		int x = Integer.parseInt(parts[1]);
		int z = Integer.parseInt(parts[2]);
		World w = Bukkit.getWorld(worldName);
		
		if (w == null) return null;
		
		return w.getChunkAt(x, z);
	}
	
	private Collection<Chunk> chunkStringsToChunkList(Collection<String> strings){
		Collection<Chunk> chunks = new HashSet<Chunk>();
		for (String s : strings) {
			// world,x,z
			Chunk chunk = stringToChunk(s);
			if (chunk != null) {
				chunks.add(chunk);
			}
			
		}
		return chunks;
	}
	private String chunkToString(Chunk chunk) {
		if (chunk == null) return null;
		return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
	}
	private Collection<String> chunksToStringList(Collection<Chunk> chunks){
		Collection<String> strings = new HashSet<String>();
		for(Chunk chunk : chunks) {
			if (chunk != null) {
				strings.add(chunkToString(chunk));
			}
		}
		return strings;
	}
	
	private Map<?, ?> serializeLocation(Location location){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("world", location.getWorld().getName());
		map.put("x", location.getX());
		map.put("y", location.getY());
		map.put("z", location.getZ());
		
		map.put("pitch", location.getPitch());
		map.put("yaw", location.getYaw());
		return map;
	}
	
	private Location deserializeLocation(Map<?, ?> map) {
		
		World w = Bukkit.getWorld((String) map.get("world"));
		
		if (w == null) return null;
		
		double x = Double.parseDouble(map.get("x").toString());
		double y = Double.parseDouble(map.get("y").toString());
		double z = Double.parseDouble(map.get("z").toString());
		
		float pitch = Float.parseFloat(map.get("pitch").toString());
		float yaw = Float.parseFloat(map.get("yaw").toString());
		
		Location location = new Location(w, x, y, z, yaw, pitch);
		return location;
	}

}
