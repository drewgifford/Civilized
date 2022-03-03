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

public class NationCapitalCommand extends CivilizedSubcommand{

	public NationCapitalCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}
	
	private Map<Player, Map<City, Integer>> queued = new HashMap<Player, Map<City, Integer>>();

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
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You must specify a city to move the capital to.");
			return false;
		}
		
		City city = CityManager.getCityFromName(String.join(" ", args));
		
		if (city == null || city.getNation() == null || !city.getNation().equals(nation)) {
			p.sendMessage(ChatColor.RED + "That city doesn't exist, or is not apart of your nation.");
			return false;
		}
		
		if (!queued.containsKey(p)) {
		
			p.sendMessage(ChatColor.RED + "Are you sure you want do move the capital to " + city.getNameWithSpaces() + "? You will not longer be the owner of the nation.");
			p.sendMessage(ChatColor.RED + "Type /nation capital " + city.getNameWithSpaces() + " again within 30 seconds to confirm deletion.");
			
			
			int task = Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new BukkitRunnable() {

				@Override
				public void run() {
					queued.get(p).remove(city);
					p.sendMessage(ChatColor.RED + "Time has expired to move the capital to " + city.getNameWithSpaces()+".");
				}
				
			}, (long)20*30);
			
			if (!queued.containsKey(p)) {
				queued.put(p, new HashMap<City, Integer>());
			}
			Map<City, Integer> map = queued.get(p);
			map.put(city, task);
			
			
		} else {
			int task = queued.get(p).get(city);
			Bukkit.getScheduler().cancelTask(task);
			
			queued.get(p).remove(city);
			
			NationManager.moveCapital(pl, nation, city);
			
			p.sendMessage(ChatColor.GREEN + "Your nation has moved the capital to " + city.getNameWithSpaces());
			
			pl.citiesConfiguration.write();
			
		}
		
		return false;
	}
	
	

}
