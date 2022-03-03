package com.drewgifford.civilized.command.subcommands.nation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;
import com.drewgifford.civilized.util.NationManager;

import net.md_5.bungee.api.ChatColor;

public class NationDisbandCommand extends CivilizedSubcommand{

	public NationDisbandCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}
	
	private Map<Player, Integer> queued = new HashMap<Player, Integer>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		if (cp.getCity() == null || cp.getCity().getNation() == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any nation.");
			return false;
		}
		
		//TODO: Check if member has permissions to claim
		
		Nation nation = cp.getCity().getNation();
		
		if (!nation.getOwner().equals(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the nation to do that.");
			return false;
		}
		
		if (!queued.containsKey(p)) {
		
			p.sendMessage(ChatColor.RED + "Are you sure you want do disband your nation?");
			p.sendMessage(ChatColor.RED + "Type /nation disband again within 30 seconds to confirm deletion.");
			
			
			queued.put(p, Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new BukkitRunnable() {

				@Override
				public void run() {
					queued.remove(p);
					p.sendMessage(ChatColor.RED + "Time has expired to disband your nation.");
				}
				
			}, (long)20*30));
			
		} else {
			int task = queued.get(p);
			Bukkit.getScheduler().cancelTask(task);
			
			queued.remove(p);
			
			NationManager.deleteNation(pl, nation);
			
			pl.citiesConfiguration.write();
			
		}
		
		return false;
	}
	
	

}
