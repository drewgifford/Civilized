package com.drewgifford.civilized.command.subcommands.city;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;

import net.md_5.bungee.api.ChatColor;

public class CityInfoCommand extends CivilizedSubcommand {

	public CityInfoCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		
		if (args.length < 1) {
			// Trying their own city...
			
			CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
			
			City city = cp.getCity();
			
			if (city == null) {
				
				//TODO: Update with language file
				p.sendMessage(ChatColor.RED + "You are not a member of any city.");
				return false;
			} else {
				
				sendCityInformation(p, city);
				
			}
			
			
		} else {
			City city = CityManager.getCityFromName(args[0]);
			if (city != null) {
				
				sendCityInformation(p, city);
				
				
			} else {
				
				p.sendMessage(ChatColor.RED + "A city of that name was not found.");
				return false;
				
			}
			
			
		}
		
		
		
		return false;
	}
	
	public void sendCityInformation(Player p, City city) {
		p.sendMessage(ChatColor.GREEN + "===== The City of " + ChatColor.AQUA + city.getNameWithSpaces() + ChatColor.GREEN + " =====");
		
		String ownerName = Bukkit.getOfflinePlayer(city.getOwner()).getName();
		
		// TODO: Create Vault bank
		String balance = this.pl.getEconomy().format(city.getBalance());
		
		List<String> memberNames = new ArrayList<String>();
		
		for (UUID member : city.getPlayers()) {
			String memberName = Bukkit.getOfflinePlayer(member).getName();
			memberNames.add(memberName);
		}
		
		
		// TODO: Change to language file
		p.sendMessage(ChatColor.AQUA + "Owner: " + ChatColor.DARK_AQUA + ownerName);
		p.sendMessage(ChatColor.AQUA + "Balance: " + ChatColor.DARK_AQUA + balance);
		p.sendMessage(ChatColor.AQUA + "Chunks: " + ChatColor.DARK_AQUA + "(" + city.getChunks().size() + "/" + city.getMaxClaimChunks() + ")");
		p.sendMessage(ChatColor.AQUA + "Members (" + memberNames.size() + "/" + city.getPlayerSlots() + "): " + ChatColor.DARK_AQUA + String.join(", ", memberNames));
	}

}
