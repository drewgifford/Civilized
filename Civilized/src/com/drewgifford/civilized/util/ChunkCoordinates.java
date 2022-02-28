package com.drewgifford.civilized.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkCoordinates {
	
	private int x;
	private int z;
	private World world;
	
	public ChunkCoordinates(World world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	public ChunkCoordinates(Location location) {
		this.world = location.getWorld();
		this.x = location.getChunk().getX();
		this.z = location.getChunk().getZ();
	}

	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public Chunk getChunk() {
		return getWorld().getChunkAt(x, z);
	}
	
	public boolean locationInChunk(Location location) {
		
		return location.getChunk() == getChunk();

	}

}
