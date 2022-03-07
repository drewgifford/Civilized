package com.drewgifford.civilized.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import com.drewgifford.civilized.Civilized;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardInterface {
	
	private Civilized pl;
	private final WorldGuard worldGuard;
	private final WorldGuardPlugin worldGuardPlugin;
	public static StateFlag WORLDGUARD_CLAIMS_FLAG;

    public WorldGuardInterface(Civilized pl) {
    	this.pl = pl;
        worldGuard = WorldGuard.getInstance();
        
        worldGuardPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        
        
        registerFlags();
    }
    
    private void registerFlags() {
    	FlagRegistry registry = worldGuard.getFlagRegistry();
    	
    	StateFlag flag = new StateFlag("allow-civilized-claims", false);
    	registry.register(flag);
    	WORLDGUARD_CLAIMS_FLAG = flag;
    }
    
    public boolean canClaimAt(org.bukkit.Location location) {
    	
    	Location loc = new Location((Extent) location.getWorld(), location.getX(), location.getY(), location.getZ());
    	RegionContainer container = worldGuard.getPlatform().getRegionContainer();
    	RegionQuery query = container.createQuery();
    	
    	boolean hasFlag = query.testState(loc, null, WORLDGUARD_CLAIMS_FLAG);
    	
    	return !hasFlag;
    	
    }
    
    public boolean canClaimAt(Chunk chunk) {
    	RegionManager manager = worldGuard.getPlatform().getRegionContainer().get((World) chunk.getWorld());
    	
    	int bx = chunk.getX() << 4;
    	int bz = chunk.getZ() << 4;
    	
    	BlockVector3 pt1 = BlockVector3.at(bx, 0, bz);
    	BlockVector3 pt2 = BlockVector3.at(bx + 15, 256, bz + 15);
    	
    	ProtectedCuboidRegion region = new ProtectedCuboidRegion("civilized-regionTest", pt1, pt2);
    	ApplicableRegionSet regions = manager.getApplicableRegions(region);
    	
    	return !regions.testState(null, WORLDGUARD_CLAIMS_FLAG);
    }

}
