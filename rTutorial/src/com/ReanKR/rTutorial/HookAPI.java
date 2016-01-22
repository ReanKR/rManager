package com.ReanKR.rTutorial;

import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;

public class HookAPI
{
	
	public static Economy getEconomy()
	{
		Economy ecoHook = null;
		RegisteredServiceProvider<Economy> economyProvider;
		economyProvider = rTutorial.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(economyProvider != null)
		{
			ecoHook = economyProvider.getProvider();
		}
		return ecoHook;
	}
}
