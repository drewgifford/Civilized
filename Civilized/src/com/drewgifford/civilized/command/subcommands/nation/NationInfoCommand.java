package com.drewgifford.civilized.command.subcommands.nation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.CityManager;
import com.drewgifford.civilized.util.NationManager;

import net.md_5.bungee.api.ChatColor;

public class NationInfoCommand extends CivilizedSubcommand {

	public NationInfoCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
			
			if (city == null || city.getNation() == null) {
				
				//TODO: Update with language file
				p.sendMessage(ChatColor.RED + "You are not a member of any nation.");
				return false;
			} else {
				
				sendNationInformation(p, city.getNation());
				
			}
			
			
		} else {
			Nation nation = NationManager.getNationFromName(args[0]);
			if (nation != null) {
				
				sendNationInformation(p, nation);
				
				
			} else {
				
				p.sendMessage(ChatColor.RED + "A nation of that name was not found.");
				return false;
				
			}
			
			
		}
		
		
		
		return false;
	}
	
	public void sendNationInformation(Player p, Nation nation) {
		p.sendMessage(ChatColor.GREEN + "===== The Nation of " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.GREEN + " =====");
		
		String ownerName = Bukkit.getOfflinePlayer(nation.getOwner()).getName();
		
		// TODO: Create Vault bank
		
		List<String> memberNames = new ArrayList<String>();
		List<String> officerNames = new ArrayList<String>();
		
		List<String> cityNames = new ArrayList<String>();
		for (City city : nation.getCities()) {
			cityNames.add(city.getNameWithSpaces());
		}
		
		
		for (UUID member : nation.getPlayers()) {
			String memberName = Bukkit.getOfflinePlayer(member).getName();
			memberNames.add(memberName);
			
			if (nation.getOfficers().contains(member)) {
				officerNames.add(memberName);
			}
		}
		
		Collections.sort(cityNames);
		Collections.sort(officerNames);
		
		String board = nation.getBoard();
		
		
		// TODO: Change to language file
		p.sendMessage(ChatColor.GRAY + board);
		p.sendMessage(ChatColor.AQUA + "Owner: " + ChatColor.DARK_AQUA + ownerName);
		p.sendMessage(ChatColor.AQUA + "Capital: " + ChatColor.DARK_AQUA + nation.getCapital().getNameWithSpaces());
		p.sendMessage(ChatColor.AQUA + "Cities (" + cityNames.size() + "): " + ChatColor.DARK_AQUA + String.join(", ", cityNames));
		p.sendMessage(ChatColor.AQUA + "Officers (" + officerNames.size() + "): " + ChatColor.DARK_AQUA + String.join(", ", officerNames));
		p.sendMessage(ChatColor.AQUA + "Members: " + memberNames.size());
	}

}
