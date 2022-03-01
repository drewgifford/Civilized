package com.drewgifford.civilized.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.plot.Plot;

public class CitiesConfiguration {
	
	private Civilized pl;
	private File file;
	private FileConfiguration config;
	
	@SuppressWarnings("deprecation")
	public CitiesConfiguration(Civilized pl) {
		this.pl = pl;
		this.file = new File(pl.getDataFolder()+"/cities.yml");
		
		if (!this.file.exists()) {
			this.pl.saveResource("cities.yml", false);
		}
		
		System.out.println("Loading configuration...");
		
		this.config = YamlConfiguration.loadConfiguration(this.file);
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.pl, new BukkitRunnable() {
			@Override
			public void run() {
				write();
			}	
		}, 1200L, 1200L);
	}
	
	public void save() {
		try {
			this.config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile() {
		return this.file;
	}
	
	public FileConfiguration getConfiguration() {
		return this.config;
	}
	
	@SuppressWarnings("unchecked")
	public CitiesConfiguration load() {
		
		List<Map<?, ?>> citiesYml = config.getMapList("cities");
		
		Map<UUID, City> playerPrimaryCities = new HashMap<UUID, City>();
		
		Civilized.cities = new ArrayList<City>();
		
		for (Map<?, ?> cityYml : citiesYml) {
			
			try {
			
				String name = (String) cityYml.get("name");
				String board = (String) cityYml.get("board");
				
				UUID owner = UUID.fromString((String) cityYml.get("owner"));
				List<UUID> officers = stringsToUniqueIds((List<String>) cityYml.get("officers"));
				List<UUID> players = stringsToUniqueIds((List<String>) cityYml.get("players"));
				
				Chunk homeChunk = stringToChunk((String) cityYml.get("homeChunk"));
				
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
				
				City city = new City(owner, name);
				
				city.setOfficers(officers);
				city.setPlayers(players);
				city.setChunks(chunksWithPlots);
				city.setHomeChunk(homeChunk);
				city.setBoard(board);
				
				city.setPlayerSlots(playerSlots);
				city.setMaxClaimChunks(maxClaimChunks);
				city.setBalance(balance);
				
				city.setPermissions(permissions);
				city.setToggles(toggles);
				
				Civilized.cities.add(city);
				
				
			
			} catch (Exception e) {
				System.out.println("Error loading city");
				e.printStackTrace();
			}
			
		}
		
		Civilized.registeredPlayers = new ArrayList<CivilizedPlayer>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			CivilizedPlayer.registerCivilizedPlayer(p);
		}
		
		return this;
		
	}
	
	public void write() {
		
		List<Map<?, ?>> cityData = new ArrayList<Map<?, ?>>();
		
		for (City city : Civilized.cities) {
			
			Map<String, Object> cityYml = new HashMap<String, Object>();
			
			cityYml.put("name", city.getName());
			cityYml.put("board", city.getBoard());
			
			cityYml.put("owner", city.getOwner().toString());
			cityYml.put("officers", uniqueIdsToStrings(city.getOfficers()));
			cityYml.put("players", uniqueIdsToStrings(city.getPlayers()));
			
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
			cityYml.put("homeChunk", chunkToString(city.getHomeChunk()));
			
			cityYml.put("permissions", city.getPermissions().toMap());
			cityYml.put("toggles", city.getToggles().toMap());
			
			cityYml.put("playerSlots", city.getPlayerSlots());
			cityYml.put("maxClaimChunks", city.getMaxClaimChunks());
			
			cityYml.put("balance", city.getBalance());
			
			
			
			// Loop through chunks and get plot data
			
			cityData.add(cityYml);
		}
		config.set("cities", cityData);
		
		this.save();
		
	}
	
	
	public static List<UUID> stringsToUniqueIds(List<String> strings){
		List<UUID> uuids = new ArrayList<UUID>();
		for(String s : strings) {
			uuids.add(UUID.fromString(s));
		}
		return uuids;
	}
	public static List<String> uniqueIdsToStrings(List<UUID> uuids){
		List<String> strings = new ArrayList<String>();
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
	
	private List<Chunk> chunkStringsToChunkList(List<String> strings){
		List<Chunk> chunks = new ArrayList<Chunk>();
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
	private List<String> chunksToStringList(List<Chunk> chunks){
		List<String> strings = new ArrayList<String>();
		for(Chunk chunk : chunks) {
			if (chunk != null) {
				strings.add(chunkToString(chunk));
			}
		}
		return strings;
	}

}
