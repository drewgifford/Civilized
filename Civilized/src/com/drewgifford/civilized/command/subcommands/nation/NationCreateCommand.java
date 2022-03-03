package com.drewgifford.civilized.command.subcommands.nation;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drewgifford.civilized.Civilized;
import com.drewgifford.civilized.command.CivilizedSubcommand;
import com.drewgifford.civilized.config.SettingsConfiguration;
import com.drewgifford.civilized.nation.Nation;
import com.drewgifford.civilized.player.CivilizedPlayer;
import com.drewgifford.civilized.util.NationManager;
import com.drewgifford.civilized.util.StringManager;

import net.md_5.bungee.api.ChatColor;

public class NationCreateCommand extends CivilizedSubcommand {

	public NationCreateCommand(Civilized pl, String label, String[] aliases, String permission, String description) {
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
			//TODO: Update with language file
			p.sendMessage(ChatColor.RED + "You need to specify a nation name.");
			return false;
		}
		
		String nationName = String.join(" ", args).replace(' ', '_');
		
		CivilizedPlayer cp = CivilizedPlayer.getCivilizedPlayer(p);
		
		if (cp.getCity() == null || !(cp.getCity().getOwner().equals(p.getUniqueId()))) {
			// TODO: Update with language file
			p.sendMessage(ChatColor.RED + "You are not an owner of a city!");
			return false;
		}
		
		if (cp.getCity().getNation() != null) {
			p.sendMessage(ChatColor.RED + "You already in a nation!");
			return false;
		}
		
		if (NationManager.nameExists(nationName)) {
			p.sendMessage(ChatColor.RED + "A nation of that name already exists.");
			return false;
		}
		
		if (!StringManager.isValidName(nationName)) {
			p.sendMessage(ChatColor.RED + "Nation names must be alphanumeric and 20 characters or less.");
			return false;
		}
		
		else {
			
			double price = SettingsConfiguration.NEW_NATION_COST;
			if (price > 0) {
				// Check bank account
				double balance = cp.getCity().getBalance();
				
				if (balance < price) {
					p.sendMessage(ChatColor.RED + "Your city does not have enough money in the bank. You need " + pl.getEconomy().format(price-balance) + " more.");
					return false;
				}
			}
			
			cp.getCity().setBalance(cp.getCity().getBalance() - price);
			
			Nation nation = new Nation(Arrays.asList(cp.getCity()), cp.getCity(), nationName);
			
			Civilized.nations.add(nation);
			
			p.sendMessage(ChatColor.GREEN + "You have founded the nation " + ChatColor.AQUA + nation.getNameWithSpaces() + ChatColor.GREEN + "!");
			
			pl.citiesConfiguration.write();
			
		}
		
		
		return false;
	}

}
