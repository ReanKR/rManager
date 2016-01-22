package com.ReanKR.rTutorial;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class rTutorialFileLoader
{
	public static rTutorial plugin;

	public rTutorialFileLoader(rTutorial Main)
	{
		rTutorialFileLoader.plugin = Main;
	}
	@SuppressWarnings("deprecation")
	public static void LoadCfg()
	{
		 FileConfiguration ConfigFile = plugin.getConfig();	 
		 Set<String> Keyword = ConfigFile.getKeys(false);
		 try
		 {
		 for(String Str : Keyword)
		 {
			 ConfigurationSection MainNode = ConfigFile.getConfigurationSection(Str);
			 try
			 {
				 boolean ProblemNotFound = true;
				 if(Str.equalsIgnoreCase("Main"))
				 {
					 if(! MainNode.isSet("Config-Version"))
					 {
						rTutorial.ErrorReporting.add(Str + ",MISSING_CONFIG_VERSION");
				 		// Send to ErrorReport : MISSING_CONFIG_VERSION
						 ProblemNotFound = false;
					 }
				 
					 if(! MainNode.isSet("Run-First-Join-Player"))
					 {
						 rTutorial.ErrorReporting.add(Str + ",MISSING_VALUE_RUN_FIRST_JOIN_PLAYER");
				 		// Send to ErrorReport : MISSING_VALUE_RUN_FIRST
						 ProblemNotFound = false;
					 }
					 
					 if(MainNode.isSet("Block-Movement"))
					 {
						 rTutorial.BlockMovement = MainNode.getBoolean("Block-Movement");
					 }
					 
					 if(MainNode.isSet("Block-All-Commands"))
					 {
						 rTutorial.BlockAllCommands = MainNode.getBoolean("Block-All-Commands");
					 }
					 
					 if(MainNode.isSet("Broadcast-Complete-Tutorial"))
					 {
						 rTutorial.CompleteBroadcast = MainNode.getBoolean("Broadcast-Complete-Tutorial");
					 }
					 
					 if(! MainNode.isSet("Edit-Complete"))
					 {
						 rTutorial.ErrorReporting.add(Str + ",MISSING_VALUE_EDIT_COMPLETE");
				 		// Send to ErrorReport : MISSING_VALUE_EDIT_COMPLETE
						 ProblemNotFound = false;
					 } 
					 
					 if(! MainNode.isSet("Command"))
					 {
						 rTutorial.ErrorReporting.add(Str + ",MISSING_COMMAND");
				 		// Send to ErrorReport : MISSING_COMMAND
						 ProblemNotFound = false;
					 }
					 rTutorial.ConfigVersion = MainNode.getInt("Config-Version");
					 rTutorial.RunFirstJoinPlayer = MainNode.getBoolean("Run-First-Join-Player");
					 rTutorial.EditComplete = MainNode.getBoolean("Edit-Complete");
				 }
				 if(ProblemNotFound)
				 {
					 if(Str.equalsIgnoreCase("Compatibles"))
					 {
						 rTutorial.CompatiblePlugins[0] = MainNode.getBoolean("TitleAPI");
						 rTutorial.CompatiblePlugins[1] = MainNode.getBoolean("Vault");
						 rTutorial.CompatiblePlugins[2] = MainNode.getBoolean("Economy");
					 }

					 if(Str.equalsIgnoreCase("Result"))
					 {
						rTutorial.ResultCommands.addAll(MainNode.getStringList("Commands"));
					 
						 ConfigurationSection ResultNode = PlusSelect(MainNode, "Items");
						 Set<String> Results = ResultNode.getKeys(false);
						 for(String ResultKeyword : Results)
						 {
							if(isNumber(ResultKeyword))
							{
								int ID = 0;
								int Amounts = 0;
								byte Data = 0;
								ConfigurationSection ResultNode2 = PlusSelect(ResultNode, ResultKeyword);
								try
								{
									ID = Integer.valueOf(ResultNode2.getInt("ID"));
									Data = Byte.parseByte(ResultNode2.getString("DATA-VALUE"));
									Amounts = Integer.parseInt(ResultNode2.getString("Amounts"));
								}
								catch(NullPointerException e)
								{
									rTutorial.ErrorReporting.add(Str + "," + ResultKeyword + ",LOAD_FAILED_ITEM_INFO");
									continue;
								}
								ItemStack item = new MaterialData(ID, Data).toItemStack(Amounts);
								rTutorial.ResultItems.add(item);
							}
										 
							else // Send Error. Reason : 'Number' is not Number
							{
								rTutorial.ErrorReporting.add(Str + "," + ResultKeyword + ",MUST_INTEGER_METHOD");
							}
						 }
					 }
				 }
				 else // Send Error. Reason : Missing Required Value
				 {
					 rTutorial.ErrorReporting.add(Str + ",MISSING_REQUIED_CONFIG_VALUE");
					 return;
				 }
			 }
			 catch(NullPointerException e)
			 {
				 e.printStackTrace();
				 rTutorial.ErrorReporting.add(Str + ",MISSING_CONFIG_VALUE");
				// Send Error. Reason : Missing Config Value
			 }
		 }
		 }
		 catch(NullPointerException e)
		 {
			 rTutorial.ErrorReporting.add("ConfigFile,MISSING_KEY_VALUE");
		 }
	}
	public static void PlayerCfg()
	{
		File YamlPlayers = new File(plugin.getDataFolder().getAbsolutePath() + "/Players.yml");
		if(! YamlPlayers.exists())
		{
			try {
				Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + "Create New File " + YamlPlayers.getAbsolutePath());
				YamlPlayers.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			FileConfiguration PlayerYaml = rTutorial.LoadFile("Players");
			Set<String> PlayerNode = PlayerYaml.getKeys(false);
			if(PlayerNode.size() == 0)
			{
				return;
			}
			for(String Keyword : PlayerNode)
			{
				try
				{
				ConfigurationSection PlayerInformation = PlayerYaml.getConfigurationSection(Keyword);
				 if(Keyword.equalsIgnoreCase("Players"))
				 {
					 String UniqueID = PlayerInformation.getString(Keyword);
					 ConfigurationSection UUIDInformation = PlusSelect(PlayerInformation, "UUID");
					 rTutorial.CompleteTutorial.put(UniqueID,UUIDInformation.getBoolean("Completed"));
					 rTutorial.StatusTutorial.put(UniqueID,UUIDInformation.getString("Status"));
				 }
				}
				catch(NullPointerException e)
				{
					rTutorial.ErrorReporting.add(Keyword + ",MISSING_VALUE");
				}
			}
	}
	
	public static void LocationCfg()
	{
		File YamlLocation = new File(plugin.getDataFolder().getAbsolutePath() + "/Location.yml");
		if(! YamlLocation.exists())
		{
			try {
				Bukkit.getConsoleSender().sendMessage(rTutorial.Prefix + "Create New File " + YamlLocation.getAbsolutePath());
				YamlLocation.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int i = 0;
		FileConfiguration LocationYaml = rTutorial.LoadFile("Location");
		ConfigurationSection CS = LocationYaml.getConfigurationSection("Locations");
		Set<String> Information = CS.getKeys(false);
		try
		{
		for(String Loc : Information)
		{
			String Coordinate = null, Angle = null, Msg = null, SubMsg = "NONE";
			int FadeIn = 0, FadeOut = 0, Duration = 0;
			boolean LocationFound = false, MessageFound = false;
			ConfigurationSection LocAmounts = PlusSelect(CS, Loc);
			Set<String> LocMethod = LocAmounts.getKeys(false);
			for(String Method : LocMethod)
			{
				if(Method.equalsIgnoreCase("Location"))
				{
					ConfigurationSection LocInformation = PlusSelect(LocAmounts, Method);
					Set<String> LocDetailed = LocInformation.getKeys(false);
					boolean CF = false, AF = false;
					for(String Detailed : LocDetailed)
					{
						try
						{
							if(Detailed.equalsIgnoreCase("Coordinate"))
							{
								Coordinate = LocInformation.getString("Coordinate");
								CF = true;
							}
							else if(Detailed.equalsIgnoreCase("Angle"))
							{
								Angle = LocInformation.getString("Angle");
								AF = true;
							}
						}
						catch(NullPointerException e)
						{
							rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + Detailed + ",VAULE_NOT_EXIST");
							continue;
						}
						if(CF && AF) LocationFound = true;
					}
					rTutorial.LocationMethod.add(Coordinate + "," + Angle);
				}
				if(Method.equalsIgnoreCase("Message"))
				{
					ConfigurationSection LocInformation = PlusSelect(LocAmounts, Method);
					Set<String> MsgDetailed = LocInformation.getKeys(false);
					for(String Detailed : MsgDetailed)
					{
						if(Detailed.equalsIgnoreCase("Time"))
						{
							ConfigurationSection TimeInformation = PlusSelect(LocInformation, Detailed);
							Set<String> TimeDetailed = TimeInformation.getKeys(false);
							for(String TimeMethod : TimeDetailed)
							{
									if(TimeMethod.equalsIgnoreCase("FadeIn"))
									{
										if(rTutorial.CompatiblePlugins[0] == true)
										{
											FadeIn = TimeInformation.getInt(TimeMethod);
										}
										else
										{
											rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + TimeMethod + "NOT_USE_TITLEAPI");
										}
									}
									
									if(TimeMethod.equalsIgnoreCase("FadeOut"))
									{
										if(rTutorial.CompatiblePlugins[0] == true)
										{
										FadeOut = TimeInformation.getInt(TimeMethod);
										}
								
										else
										{
											rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + TimeMethod + ",NOT_USE_TITLEAPI");
										}
									}
									
									if(TimeMethod.equalsIgnoreCase("Duration"))
									{
										if(rTutorial.CompatiblePlugins[0] == true)
										{
											Duration = TimeInformation.getInt(TimeMethod);
										}
										else
										{
											rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + TimeMethod + ",NOT_USE_TITLEAPI");
										}
									}
							}
						}
						if(Detailed.equalsIgnoreCase("Main"))
						{
							Msg = LocInformation.getString(Detailed);
							if(Msg == null)
							{
								rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + Detailed + ",NOT_EXIST_VALUE");
							}
							else
							{
								MessageFound = true;
							}
						}
						
						if(Detailed.equalsIgnoreCase("Sub"))
						{
							if(rTutorial.CompatiblePlugins[0] == true)
							{
								SubMsg = LocInformation.getString(Detailed);
							}
							else
							{
								rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + Detailed + ",NOT_USE_TITLEAPI");
							}
						}
					}
				}
			}
			if(LocationFound && MessageFound)
			{
				try
				{
					int j = 0;
					String[] Chack = (Coordinate + "," + Angle).split(",");
					while(Chack[j] != null)
					{
						if(isNumber(Chack[j]))
						{
							j++;
							continue;
						}
						else
						{
							rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + "INCORRECT_LOCATION_VALUE");
							return;
						}
					}
				}
				catch(NullPointerException e)
				{
					rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + "MISSING_LOCATION_VALUE");
					return;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					
				}
				rTutorial.LocationMethod.add(Coordinate + "," + Angle);
				rTutorial.MessageMethod.add(Msg + "," + SubMsg);
				rTutorial.TimeMethod.add(FadeIn + "," + FadeOut + "," + Duration);
				i++;
			}
			else
			{
				Bukkit.getConsoleSender().sendMessage(String.valueOf(LocationFound) + String.valueOf(MessageFound));
				rTutorial.ErrorReporting.add("LocationFile," + Loc + "," + "CONFIG_INVAILED");
				return;
			}
		}
		}
		catch(NullPointerException e)
		{
			rTutorial.ErrorReporting.add("LocationFile,FILE_INVAILED");
		}
		rTutorial.MethodAmount = i;
	}
	
	public static ConfigurationSection PlusSelect(ConfigurationSection CS, String Name)
	{
		return CS.getConfigurationSection(Name);
	}

	public static boolean isNumber(String Str)
	{
		try
		{
			Double.parseDouble(Str);
		}
		catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
}
