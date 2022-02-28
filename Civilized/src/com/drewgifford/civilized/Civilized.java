package com.drewgifford.civilized;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CityCommand;
import com.drewgifford.civilized.command.CivCommand;
import com.drewgifford.civilized.event.InventoryClickListener;
import com.drewgifford.civilized.event.JoinListener;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.requests.CityInvite;

import net.milkbowl.vault.economy.Economy;

public class Civilized extends JavaPlugin {
	
	public static List<City> cities = new ArrayList<City>();
	
	public static List<CivilizedPlayer> registeredPlayers = new ArrayList<CivilizedPlayer>();
	
	public static List<CityInvite> cityInvites = new ArrayList<CityInvite>();
	
	private Economy econ;
	
	public void onEnable() {
		
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		
		CivCommand.registerCommands(this);
		CityCommand.registerCommands(this);
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new InventoryClickListener(this), this);
		
	}
	
	public void onDisable() {
		
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
