package com.drewgifford.civilized.command.subcommands.city;

import java.util.regex.Pattern;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.city.City;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.player.CivilizedPlayer;
import net.md_5.bungee.api.ChatColor;

public class CityDepositCommand extends CivilizedSubcommand{

	public CityDepositCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
		super(pl, label, aliases, permission, description);
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
			return false;
		}
		Player p = (Player) sender;
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		if (cp.getCity() == null) {
			p.sendMessage(ChatColor.RED + "You are not a member of any city.");
			return false;
		}
		
		//TODO: Check if member has permissions to claim
		
		City city = cp.getCity();
		double balance = pl.getEconomy().getBalance(p);
		
		/*if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}*/
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide an amount to deposit.");
			return false;
		}
		
		double depositAmount;
		try {
			depositAmount = Double.parseDouble(args[0]);
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "Deposit amount must be a number.");
			return false;
		}
		
		if (balance < depositAmount) {
			p.sendMessage(ChatColor.RED + "You do not have enough money to deposit that amount. You need " + pl.getEconomy().format(depositAmount-balance) + " more.");
			return false;
		}
		
		pl.getEconomy().withdrawPlayer(p, depositAmount);
		city.setBalance(city.getBalance() + depositAmount);
		
		p.sendMessage(ChatColor.GREEN + "You deposited " + pl.getEconomy().format(depositAmount) + " into the city bank.");
		
		return false;
	}
	
	

}
