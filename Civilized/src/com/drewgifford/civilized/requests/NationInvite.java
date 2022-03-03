package com.drewgifford.civilized.requests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;

import net.md_5.bungee.api.ChatColor;

public class NationInvite {
	
	private City target;
	private Nation nation;
	private BukkitTask expirationTask;
	private Civilized pl;
	private boolean expired;
	
	public static boolean inviteExists(City target, Nation nation) {
		
		return getInvite(target, nation) != null;
		
	}
	
	public static NationInvite getInvite(City target, Nation nation) {
		
		for (NationInvite invite : Civilized.nationInvites) {
			if (!invite.expired && invite.target.equals(target) && invite.getNation() == nation) {
				return invite;
			}
		}
		return null;
		
	}
	
	public NationInvite(Civilized pl, City target, Nation nation) {
		this.target = target;
		this.nation = nation;
		this.pl = pl;
		this.expired = false;
		
		double minutes = 200;
		
		
		this.expirationTask = new BukkitRunnable() {

			@Override
			public void run() {
				
				if(this.isCancelled()) return;
				
				if (getTarget().isOnline()) {
					
					Player targetOnline = getTarget().getPlayer();
					targetOnline.sendMessage(ChatColor.RED + "Your city's pending invite to " + getNation().getNameWithSpaces() + " has expired or has been revoked.");
					
				}
				
				
				cancelInvite();
				
			}
			
			
			
		}.runTaskLater(this.pl, (long) minutes);
	}
	
	public UUID getSenderUniqueId() {
		return this.nation.getOwner();
	}
	public UUID getTargetUniqueId() {
		return this.target.getOwner();
	}
	public OfflinePlayer getSender() {
		return Bukkit.getOfflinePlayer(this.getSenderUniqueId());
	}
	public OfflinePlayer getTarget() {
		return Bukkit.getOfflinePlayer(this.getTargetUniqueId());
	}
	
	public Nation getNation() {
		return this.nation;
	}
	
	public void cancelInvite() {
		
		this.expired = true;
		
		this.expirationTask.cancel();
		
		Civilized.cityInvites.remove(this);
	}
	
	public void accept() {
		
		//boolean canJoin = this.city.addPlayer(this.getTargetUniqueId());
		boolean canJoin = true;
		
		if (!canJoin) {
			
			/*if (getSender().isOnline()) {
				Player senderOnline = this.getSender().getPlayer();
				senderOnline.sendMessage(ChatColor.AQUA + getTarget().getName() + ChatColor.RED + " could not accept your invite, there are not enough player slots open.");
			}
			
			if (getTarget().isOnline()) {
				Player targetOnline = this.getTarget().getPlayer();
				targetOnline.sendMessage(ChatColor.RED + "There are not enough player slots in " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.RED + " to join.");
			}*/
			
		} else {
			if (getSender().isOnline()) {
				Player senderOnline = this.getSender().getPlayer();
				senderOnline.sendMessage(ChatColor.AQUA + this.target.getNameWithSpaces() + ChatColor.GREEN + " has accepted your invite to join " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.GREEN + "!");
			}
			
			if (getTarget().isOnline()) {
				Player targetOnline = this.getTarget().getPlayer();
				targetOnline.sendMessage(ChatColor.GREEN + "You accepted " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.GREEN + "'s invite to join their nation!");
			}
		}
		
		this.target.setNation(this.nation);
		
		this.cancelInvite();
	}
	
	public void deny() {
		if (getSender().isOnline()) {
			Player senderOnline = this.getSender().getPlayer();
			senderOnline.sendMessage(ChatColor.AQUA + this.target.getNameWithSpaces() + ChatColor.RED + " has denied your invite to join " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.RED + ".");
		}
		
		if (getTarget().isOnline()) {
			Player targetOnline = this.getTarget().getPlayer();
			targetOnline.sendMessage(ChatColor.RED + "You denied " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.RED + "'s invite to join their nation.");
		}
		
		this.cancelInvite();
	}


}
