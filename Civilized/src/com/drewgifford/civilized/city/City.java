package com.drewgifford.civilized.city;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.util.ChunkCoordinates;

public class City {
	
	private List<UUID> players;
	private List<UUID> officers;
	private UUID owner;
	private String name;
	
	private List<Chunk> chunks;
	private ChunkCoordinates homeChunk;
	private Nation nation;
	private int playerSlots;
	
	private double balance;
	private int maxClaimChunks;
	
	
	public City(UUID owner, String name) {
		
		this.owner = owner;
		this.players = new ArrayList<UUID>();
		this.officers = new ArrayList<UUID>();
		
		this.addMember(owner);
		
		this.chunks = new ArrayList<Chunk>();
		
		this.nation = null;
		this.balance = 0;
		this.name = name;
		this.playerSlots = 3;
		this.maxClaimChunks = 4;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public List<UUID> getPlayers() {
		return this.players;
	}
	public List<UUID> getOfficers() {
		return this.players;
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
	
	public ChunkCoordinates getHomeChunk(){
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
	
	public void setHomeChunk(ChunkCoordinates chunk) {
		this.homeChunk = chunk;
	}
	
	public void addMember(UUID uuid) {
		if (!this.getPlayers().contains(uuid)) this.players.add(uuid);
	}
	
	public void addOfficer(UUID uuid) {
		if (!this.getOfficers().contains(uuid)) this.officers.add(uuid);
	}
	
	public void removeOfficer(UUID uuid) {
		this.officers.remove(uuid);
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
		return (this.officers.contains(uuid) || this.owner.equals(uuid));
	}

}
