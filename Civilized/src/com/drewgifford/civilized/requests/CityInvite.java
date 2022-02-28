package com.drewgifford.civilized.requests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class CityInvite {
	
	private UUID sender;
	private UUID target;
	private City city;
	private BukkitTask expirationTask;
	private Civilized pl;
	private boolean expired;
	
	public static boolean inviteExists(UUID target, City city) {
		
		return getInvite(target, city) != null;
		
		
	}
	
	public static CityInvite getInvite(UUID target, City city) {
		
		for (CityInvite invite : Civilized.cityInvites) {
			if (!invite.expired && invite.getTargetUniqueId().equals(target) && invite.getCity() == city) {
				return invite;
			}
		}
		return null;
		
	}
	
	public CityInvite(Civilized pl, UUID sender, UUID target, City city) {
		this.sender = sender;
		this.target = target;
		this.city = city;
		this.pl = pl;
		this.expired = false;
		
		double minutes = 200;
		
		
		this.expirationTask = new BukkitRunnable() {

			@Override
			public void run() {
				
				if(this.isCancelled()) return;
				
				if (getTarget().isOnline()) {
					
					Player targetOnline = getTarget().getPlayer();
					targetOnline.sendMessage(ChatColor.RED + "Your pending invite to " + getCity().getNameWithSpaces() + " has expired or has been revoked.");
					
				}
				
				
				cancelInvite();
				
			}
			
			
			
		}.runTaskLater(this.pl, (long) minutes);
	}
	
	public UUID getSenderUniqueId() {
		return this.sender;
	}
	public UUID getTargetUniqueId() {
		return this.target;
	}
	public OfflinePlayer getSender() {
		return Bukkit.getOfflinePlayer(this.sender);
	}
	public OfflinePlayer getTarget() {
		return Bukkit.getOfflinePlayer(this.target);
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void cancelInvite() {
		
		this.expired = true;
		
		this.expirationTask.cancel();
		
		Civilized.cityInvites.remove(this);
	}
	
	public void accept() {
		if (getSender().isOnline()) {
			Player senderOnline = this.getSender().getPlayer();
			senderOnline.sendMessage(ChatColor.AQUA + getTarget().getName() + ChatColor.GREEN + " has accepted your invite to join " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.GREEN + "!");
		}
		
		if (getTarget().isOnline()) {
			Player targetOnline = this.getTarget().getPlayer();
			targetOnline.sendMessage(ChatColor.GREEN + "You accepted " + ChatColor.AQUA + getSender().getName() + ChatColor.GREEN + "'s invite to join " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.GREEN + "!");
		}
		
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(getTarget());
		
		cp.setCity(city);
		
		this.cancelInvite();
	}
	
	public void deny() {
		if (getSender().isOnline()) {
			Player senderOnline = this.getSender().getPlayer();
			senderOnline.sendMessage(ChatColor.AQUA + getTarget().getName() + ChatColor.RED + " has denied your invite to join " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.RED + ".");
		}
		
		if (getTarget().isOnline()) {
			Player targetOnline = this.getTarget().getPlayer();
			targetOnline.sendMessage(ChatColor.RED + "You denied " + ChatColor.AQUA + getSender().getName() + ChatColor.RED + "'s invite to join " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.RED + ".");
		}
		
		this.cancelInvite();
	}


}
