package com.drewgifford.civilized.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.permissions.CivilizedPermissions;
import com.drewgifford.civilized.permissions.CivilizedToggles;
import com.drewgifford.civilized.permissions.PermissionLevel;
import com.drewgifford.civilized.plot.Plot;

import net.md_5.bungee.api.ChatColor;

public class City {
	
	private Set<UUID> players;
	private Set<UUID> officers;
	private UUID owner;
	private String name;
	private String board;
	
	private Map<Chunk, Plot> chunks;
	private Location home;
	private Nation nation;
	private int playerSlots;
	
	private double balance;
	private int maxClaimChunks;
	
	private CivilizedPermissions permissions;
	private CivilizedToggles toggles;
	
	
	public City(UUID owner, String name, Location home) {
		
		this.owner = owner;
		this.players = new HashSet<UUID>();
		this.officers = new HashSet<UUID>();
		
		this.home = home;
		
		
		this.players.add(owner);
		this.addOfficer(owner);
		
		this.chunks = new HashMap<Chunk, Plot>();
		
		this.nation = null;
		this.balance = 0;
		this.name = name;
		this.playerSlots = SettingsConfiguration.INITIAL_PLAYER_SLOTS;
		this.maxClaimChunks = SettingsConfiguration.INITIAL_MAXCLAIMS;
		this.board = "Change this message with /city board <message>";
		
		this.permissions = new CivilizedPermissions(PermissionLevel.MEMBER);
		this.toggles = new CivilizedToggles();
	}
	
	public Nation getNation() {
		return this.nation;
	}
	public void setNation(Nation nation) {
		for (Nation n : Civilized.nations) {
			n.getCities().remove(this);
			n.removeOfficers(this.getPlayers());
		}
		
		this.nation = nation;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public Set<UUID> getPlayers() {
		return this.players;
	}
	public Set<UUID> getOfficers() {
		return this.officers;
	}
	
	public Set<Chunk> getChunks(){
		return new HashSet<Chunk>(this.chunks.keySet());
	}
	public Set<Plot> getPlots(){
		return new HashSet<Plot>(this.chunks.values());
	}
	
	public int getPlayerSlots() {
		return this.playerSlots;
	}
	public void setPlayerSlots(int playerSlots) {
		this.playerSlots = playerSlots;
	}
	
	public int getMaxClaimChunks() {
		return this.maxClaimChunks;
	}
	public void setMaxClaimChunks(int maxClaimChunks) {
		this.maxClaimChunks = maxClaimChunks;
	}
	
	
	
	public boolean locationInTown(Location loc) {
		for (Chunk coords : chunks.keySet()) {
			if (loc.getChunk().equals(coords)) return true;
		}
		return false;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void addBalance(double add) {
		this.balance += add;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameWithSpaces() {
		return this.getName().replace('_', ' ');
	}
	
	public Chunk getHomeChunk() {
		return this.home.getChunk();
	}
	
	public boolean addPlayer(UUID uuid) {
		
		if (!this.getPlayers().contains(uuid)) {
			if (this.getPlayers().size() >= this.getPlayerSlots()) {
				return false;
			}
			
			this.players.add(uuid);
			return true;
		}
		return true;
	}
	
	public void addOfficer(UUID uuid) {
		if (!this.getOfficers().contains(uuid)) this.officers.add(uuid);
		addPlayer(uuid);
	}
	
	public void removeOfficer(UUID uuid) {
		this.getOfficers().remove(uuid);
	}
	
	public void removePlayer(UUID uuid) {
		this.players.remove(uuid);
		
		if (this.getNation() != null) {
			this.getNation().getOfficers().remove(uuid);
		}
		
		removeOfficer(uuid);
	}
	
	public boolean hasChunk(Chunk chunk) {
		return this.chunks.keySet().contains(chunk);
	}
	
	public void addChunk(Chunk chunk) {
		this.chunks.put(chunk, new Plot(chunk));
	}
	public void addChunk(Chunk chunk, Plot plot) {
		this.chunks.put(chunk, plot);
	}
	
	public void removeChunk(Chunk chunk) {
		this.chunks.remove(chunk);
	}
	
	public boolean hasOfficerPermission(UUID uuid) {
		return (this.officers.contains(uuid) || this.owner.equals(uuid));
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
	
	public boolean isChunkNearby(Chunk chunk) {
		return isChunkNearby(chunk, 1);
	}
	public boolean isChunkNearby(Chunk chunk, int distance) {
		
		if (distance <= 0) return false;
		
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
		for (Chunk c : getChunks()) {
			if (Math.abs(c.getX() - chunkX) <= distance && Math.abs(c.getZ() - chunkZ) <= distance) {
				return true;
			}
		}
		return false;
		
	}
	
	public boolean isChunkAdjacent(Chunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
		for (Chunk c : getChunks()) {
			int delX = Math.abs(c.getX() - chunkX);
			int delZ = Math.abs(c.getZ() - chunkZ);
			if ((delX == 0) ^ (delZ == 0) && ((delX == 1) || (delZ == 1))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean willChunkDisconnect(Chunk chunk) {
		
		Set<Chunk> updatedChunks = new HashSet<Chunk>(getChunks());
		
		Set<Chunk> checkedChunks = new HashSet<Chunk>();
		
		updatedChunks.remove(chunk);
		
		boolean toBreak = false;
		
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {		
				if ((x == 0) ^ (z == 0)) {
					
					Chunk c = chunk.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z);
					
					System.out.println("Checking " + c.getX() + "," + c.getZ());
					
					if (updatedChunks.contains(c)) {
						System.out.println("Found chunk " + c.getX() + "," + c.getZ());
						checkedChunks = checkAdjacentChunks(c, updatedChunks, checkedChunks);
						toBreak = true;
						break;
					}
					
				}
			}
			if (toBreak) break;
		}
		
		// In one direction, the chunks are disconnected.
		return checkedChunks.size() != updatedChunks.size();
		
	}
	
	private Set<Chunk> checkAdjacentChunks(Chunk chunk, Set<Chunk> validChunks, Set<Chunk> checkedChunks) {

		World w = chunk.getWorld();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if ( (x == 0) ^ (z == 0) ) {
					Chunk c = w.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
					
					System.out.println("[" + c.getX() + "," + c.getZ() + "]");
					
					if (!checkedChunks.contains(c) && validChunks.contains(c)) {
						System.out.println("Valid");
						checkedChunks.add(c);
						checkedChunks = checkAdjacentChunks(c, validChunks, checkedChunks);
					}
					
				}
			}
		}
		return checkedChunks;
	}

	public String getBoard() {
		return this.board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChunks(Map<Chunk, Plot> chunks) {
		this.chunks = chunks;
	}

	public void setPlayers(Set<UUID> players) {
		this.players = players;
	}

	public void setOfficers(Set<UUID> officers) {
		this.officers = officers;
	}
	
	public Map<Chunk, Plot> getChunkPlotMap(){
		return this.chunks;
	}

	public void setHome(Location home) {
		this.home = home;
	}
	public Location getHome() {
		return this.home;
	}

	public String getHomeString() {
		return "[X: " + this.home.getBlockX() + ", Y:" + this.home.getBlockY() + ", Z:" + this.home.getBlockZ() + "]";
	}

}
