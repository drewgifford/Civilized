package com.drewgifford.civilized.city;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.util.ChunkCoordinates;

public class City {
	
	private List<UUID> players;
	private List<UUID> officers;
	private UUID owner;
	private String name;
	private String board;
	
	private List<Chunk> chunks;
	private Chunk homeChunk;
	private Nation nation;
	private int playerSlots;
	
	private double balance;
	private int maxClaimChunks;
	
	
	public City(UUID owner, String name) {
		
		this.owner = owner;
		this.players = new ArrayList<UUID>();
		this.officers = new ArrayList<UUID>();
		
		
		this.addPlayer(owner);
		this.addOfficer(owner);
		
		this.chunks = new ArrayList<Chunk>();
		
		this.nation = null;
		this.balance = 0;
		this.name = name;
		this.playerSlots = 3;
		this.maxClaimChunks = 4;
		this.board = "Change this with /city board <message>";
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public List<UUID> getPlayers() {
		return this.players;
	}
	public List<UUID> getOfficers() {
		return this.officers;
	}
	
	public List<Chunk> getChunks(){
		return this.chunks;
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
		for (Chunk coords : chunks) {
			if (loc.getChunk().equals(coords)) return true;
		}
		return false;
	}
	
	public Chunk getHomeChunk(){
		return this.homeChunk;
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
	
	public void setHomeChunk(Chunk homeChun) {
		this.homeChunk = homeChun;
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
		removeOfficer(uuid);
	}
	
	public boolean hasChunk(Chunk chunk) {
		return this.chunks.contains(chunk);
	}
	
	public void addChunk(Chunk chunk) {
		this.chunks.add(chunk);
	}
	
	public void removeChunk(Chunk chunk) {
		this.chunks.remove(chunk);
	}
	
	public boolean hasOfficerPermission(UUID uuid) {
		System.out.println(officers);
		return (this.officers.contains(uuid) || this.owner.equals(uuid));
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
		
		List<Chunk> updatedChunks = new ArrayList<Chunk>(getChunks());
		
		List<Chunk> checkedChunks = new ArrayList<Chunk>();
		
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
	
	private List<Chunk> checkAdjacentChunks(Chunk chunk, List<Chunk> validChunks, List<Chunk> checkedChunks) {

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

	public void setChunks(List<Chunk> chunks) {
		this.chunks = chunks;
	}

	public void setPlayers(List<UUID> players) {
		this.players = players;
	}

	public void setOfficers(List<UUID> officers) {
		this.officers = officers;
	}

}
