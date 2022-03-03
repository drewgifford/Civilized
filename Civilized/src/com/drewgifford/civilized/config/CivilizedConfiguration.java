package com.drewgifford.civilized.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.Civilized;

public abstract class CivilizedConfiguration {
	
	protected Civilized pl;
	protected File file;
	protected FileConfiguration config;
	
	@SuppressWarnings("deprecation")
	public CivilizedConfiguration(Civilized pl, String fileName, boolean doAutoSave) {
		this.pl = pl;
		this.file = new File(pl.getDataFolder()+"/"+fileName);
		
		if (!this.file.exists()) {
			this.pl.saveResource(fileName, false);
		}
		
		System.out.println("Loading configuration " + fileName);
			if(doAutoSave) {
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.pl, new BukkitRunnable() {
				@Override
				public void run() {
					write();
				}	
			}, 600L, 600L);
		}
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
	
	public abstract CivilizedConfiguration load();
	public abstract void write();

}
