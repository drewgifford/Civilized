package com.drewgifford.civilized;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CityCommand;
import com.drewgifford.civilized.command.CivCommand;
import com.drewgifford.civilized.command.PlotCommand;
import com.drewgifford.civilized.config.CitiesConfiguration;
import com.drewgifford.civilized.event.BlockBreakListener;
import com.drewgifford.civilized.event.BlockPlaceListener;
import com.drewgifford.civilized.event.EntitySpawnListener;
import com.drewgifford.civilized.event.ExplodeListener;
import com.drewgifford.civilized.event.InteractListener;
import com.drewgifford.civilized.event.InventoryClickListener;
import com.drewgifford.civilized.event.ItemUseListener;
import com.drewgifford.civilized.event.PistonListener;
import com.drewgifford.civilized.event.PlayerDamageListener;
import com.drewgifford.civilized.event.PlayerJoinListener;
import com.drewgifford.civilized.event.PlayerMoveListener;
import com.drewgifford.civilized.event.WorldSaveListener;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;
import com.drewgifford.civilized.util.MobCleaner;

import net.milkbowl.vault.economy.Economy;

public class Civilized extends JavaPlugin {
	
	public static List<City> cities = new ArrayList<City>();
	
	public static List<CivilizedPlayer> registeredPlayers = new ArrayList<CivilizedPlayer>();
	
	public static List<CityInvite> cityInvites = new ArrayList<CityInvite>();
	
	public static List<UUID> activeMaps = new ArrayList<UUID>();
	
	private Economy econ;
	
	public CitiesConfiguration citiesConfiguration;
	
	public void onEnable() {
		
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		
		citiesConfiguration = new CitiesConfiguration(this).load();
		
		CivCommand.registerCommands(this);
		CityCommand.registerCommands(this);
		PlotCommand.registerCommands(this);
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new InventoryClickListener(this), this);
		pm.registerEvents(new PlayerMoveListener(this), this);
		pm.registerEvents(new WorldSaveListener(this), this);
		pm.registerEvents(new EntitySpawnListener(this), this);
		pm.registerEvents(new BlockBreakListener(this), this);
		pm.registerEvents(new BlockPlaceListener(this), this);
		pm.registerEvents(new InteractListener(this), this);
		pm.registerEvents(new ItemUseListener(this), this);
		pm.registerEvents(new PlayerDamageListener(this), this);
		pm.registerEvents(new ExplodeListener(this), this);
		pm.registerEvents(new PistonListener(this), this);
		
		MobCleaner cleaner = new MobCleaner(this);
		cleaner.run();
		
	}
	
	public void onDisable() {
		citiesConfiguration.write();
	}
	
	private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public Economy getEconomy() {
        return econ;
    }

}
