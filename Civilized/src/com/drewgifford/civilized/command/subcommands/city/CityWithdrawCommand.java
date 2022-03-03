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

public class CityWithdrawCommand extends CivilizedSubcommand{

	public CityWithdrawCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
		
		City city = cp.getCity();
		double balance = cp.getCity().getBalance();
		
		if (!city.hasOfficerPermission(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You do not have permission within the city to do that.");
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "You need to provide an amount to withdraw.");
			return false;
		}
		
		double withdrawAmount;
		try {
			withdrawAmount = Double.parseDouble(args[0]);
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "Withdraw amount must be a number.");
			return false;
		}
		
		if (balance < withdrawAmount) {
			p.sendMessage(ChatColor.RED + "The city does not have enough money to withdraw that amount. It needs " + pl.getEconomy().format(withdrawAmount-balance) + " more.");
			return false;
		}
		
		pl.getEconomy().depositPlayer(p, withdrawAmount);
		city.setBalance(city.getBalance() - withdrawAmount);
		
		p.sendMessage(ChatColor.GREEN + "You withdrew " + pl.getEconomy().format(withdrawAmount) + " from the city bank.");
		
		return false;
	}
	
	

}
