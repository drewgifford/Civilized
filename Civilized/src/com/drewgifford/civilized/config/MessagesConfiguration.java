package com.drewgifford.civilized.config;

import org.bukkit.configuration.file.YamlConfiguration;

import com.drewgifford.civilized.Civilized;

public class MessagesConfiguration extends CivilizedConfiguration {

	public MessagesConfiguration(Civilized pl, String fileName, boolean doAutoSave) {
		super(pl, fileName, doAutoSave);
	}

	@Override
	public CivilizedConfiguration load() {
		
		return this;
	}

	@Override
	public void write() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
		
		
		
	}
	
	

}
