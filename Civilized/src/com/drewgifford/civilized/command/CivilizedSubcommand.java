package com.drewgifford.civilized.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;

public abstract class CivilizedSubcommand {
	
	protected Civilized pl;
	private String label;
	private String[] aliases;
	private String permission;
	private String description;
	
	public CivilizedSubcommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		this.pl = pl;
		this.label = label;
		this.aliases = aliases;
		this.permission = permission;
		this.description = description;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String[] getAliases() {
		return this.aliases;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public boolean canUse(Player p) {
		if(getPermission() == "") return true;
		return p.hasPermission(getPermission());
	}
	
	public boolean hasLabel(String l) {
		
		boolean returnValue = getLabel().equalsIgnoreCase(l);
		
		if (returnValue) return true;
		for (String s : getAliases()) {
			if (s.equalsIgnoreCase(l)) return true;
		}
		return false;
	}
	
	public abstract boolean run(CommandSender sender, String[] args);
	
	
	
}