package me.vaape.acgeneral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.lmax.disruptor.EventSequencer;

import de.ancash.actionbar.ActionBarAPI;
import me.vaape.rewards.Rewards;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.server.level.EntityPlayer;

public class ACGeneral extends JavaPlugin implements Listener{
	
	public static ACGeneral plugin;
	private FileConfiguration config = this.getConfig();
	
	//For wild tp
	public int number;
	public static int wild;
	public static Map<String, Integer> tpTasks = new HashMap<String, Integer>();
	public List<String> noFallDamagePlayers = new ArrayList<String>();
	public List<UUID> playersInTutorial = new ArrayList<UUID>();
	
	public void onEnable() {
		plugin = this;
		getLogger().info(ChatColor.GREEN + "ACGernal has been enabled!");
		getServer().getPluginManager().registerEvents(this, this);
		loadConfiguration();
	}
	
	public void loadConfiguration() {
		final FileConfiguration config = this.getConfig();
		config.set(("start time of server"), new Date());
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void onDisable(){
		plugin = null;
	}
	
	//Set health to 10 hearts
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			switch (label.toLowerCase()) {
			case "pl":
			case "plugins":
				player.sendMessage(ChatColor.BLUE + "Almost all plugins here are custom coded for the best experience. Core plugins include:");
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "ACGeneral, CustomGod, Guilds, CombatLog, EnderDragons, ACEvents, CrateDrops, ACVote, Essentials, SilkSpawners");
				break;
			case "help":
				if (args.length == 0) {
					player.performCommand("kit help"); //Give help book
					player.sendMessage("");
					player.sendMessage(""
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x--------" 
							+ ChatColor.BLUE +  "" + ChatColor.BOLD + " Help page 1 of 3 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "--------x\n"
							+ ChatColor.BLUE + "/help " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Displays help menu\n"
							+ ChatColor.BLUE + "/warp " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Teleports you to a location\n"
							+ ChatColor.BLUE + "/home " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Teleports you to your home\n"
							+ ChatColor.BLUE + "/sethome " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Sets your home\n"
							+ ChatColor.BLUE + "/delhome " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Removes a home\n"
							+ ChatColor.BLUE + "/msg " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Sends a private msg\n"
							+ ChatColor.BLUE + "/msgtoggle " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Toggles receiving private msgs\n"
							+ ChatColor.BLUE + "/ignore " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Ignores chat from specific players\n"
							+ ChatColor.BLUE + "/helpop " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Leaves a message for staff\n"
							+ ChatColor.BLUE + "/g " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Guilds base command\n"
							+ ChatColor.BLUE + "/vote " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets list of vote links\n"
							+ ChatColor.BLUE + "/collect " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Collect vote rewards\n"
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x-------" 
							+ ChatColor.BLUE + ChatColor.BOLD + " /help 2 for page 2 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "-------x"
							);
				}
				else if (args[0].equals("1")) {
					player.performCommand("kit help"); //Give help book
					player.sendMessage("");
					player.sendMessage(""
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x--------" 
							+ ChatColor.BLUE +  "" + ChatColor.BOLD + " Help page 1 of 3 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "--------x\n"
							+ ChatColor.BLUE + "/help " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Displays help menu\n"
							+ ChatColor.BLUE + "/warp " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Teleports you to a location\n"
							+ ChatColor.BLUE + "/home " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Teleports you to your home\n"
							+ ChatColor.BLUE + "/sethome " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Sets your home\n"
							+ ChatColor.BLUE + "/delhome " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Removes a home\n"
							+ ChatColor.BLUE + "/msg " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Sends a private msg\n"
							+ ChatColor.BLUE + "/msgtoggle " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Toggles receiving private msgs\n"
							+ ChatColor.BLUE + "/ignore " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Ignores chat from specific players\n"
							+ ChatColor.BLUE + "/helpop " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Leaves a message for staff\n"
							+ ChatColor.BLUE + "/g " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Guilds base command\n"
							+ ChatColor.BLUE + "/vote " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets list of vote links\n"
							+ ChatColor.BLUE + "/collect " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Collect vote rewards\n"
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x-------" 
							+ ChatColor.BLUE + ChatColor.BOLD + " /help 2 for page 2 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "-------x"
							);
				}
				else if (args[0].equals("2")) {
					player.sendMessage("");
					player.sendMessage(""
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x--------" 
							+ ChatColor.BLUE +  "" + ChatColor.BOLD + " Help page 2 of 3 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "--------x\n"
							+ ChatColor.BLUE + "/votestreak " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- See when you last voted and your daily vote streak\n"
							+ ChatColor.BLUE + "/tpa " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Requests to teleport to a player\n"
							+ ChatColor.BLUE + "/tpacancel " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Cancels tpa request\n"
							+ ChatColor.BLUE + "/tpaccept " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Accepts tpa request\n"
							+ ChatColor.BLUE + "/gw " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets info on Guild Wars\n"
							+ ChatColor.BLUE + "/fish " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets info on Fishing Tournament\n"
							+ ChatColor.BLUE + "/scoreboard " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Toggles event scoreboards\n"
							+ ChatColor.BLUE + "/refer " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Refers a new player\n"
							+ ChatColor.BLUE + "/suggestion " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets suggestions form link\n"
							+ ChatColor.BLUE + "/reddit " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets Reddit link\n"
							+ ChatColor.BLUE + "/discord " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Gets join link for our Discord\n"
							+ ChatColor.BLUE + "/discord link " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Links your MC account with Discord\n"
							+ ChatColor.BLUE + "/buy " + ChatColor.of("#9e9eff") + ChatColor.ITALIC + "- Opens server shop\n"
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "x-------" 
							+ ChatColor.BLUE + ChatColor.BOLD + " /help 3 for page 3 "
							+ ChatColor.of("#8b8bcb") + ChatColor.STRIKETHROUGH + "-------x"
							);
				}
				else if (args[0].equals("3")) {
					player.sendMessage("");
					player.sendMessage(""
							+ ChatColor.of("#ffc266") + ChatColor.STRIKETHROUGH + "x--------" + ChatColor.of("#ff9900") + ChatColor.BOLD + " Premium Commands " + ChatColor.of("#ffc266") + ChatColor.STRIKETHROUGH + "--------x\n"
							+ ChatColor.of("#bd52ff") + "/tpahere " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Requests for a player to teleport to you\n"
							+ ChatColor.of("#bd52ff") + "/tpauto " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Auto accepts tpa requests\n"
							+ ChatColor.of("#bd52ff") + "/seen " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- See when a player was last seen\n"
							+ ChatColor.of("#bd52ff") + "/nickname " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Changes your nickname\n"
							+ ChatColor.of("#bd52ff") + "/realname " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- See someone's realname behind nickname\n"
							+ ChatColor.of("#bd52ff") + "/kit " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Gives daily Feathers of Knowledge/Dragon eggs\n"
							+ ChatColor.of("#bd52ff") + "/nickcolor " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Changes color of nickname\n"
							+ ChatColor.of("#bd52ff") + "/hat " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Puts an item on your head\n"
							+ ChatColor.of("#bd52ff") + "/back " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Teleports you to previous location\n"
							+ ChatColor.of("#bd52ff") + "/grepair " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Checks the status of your God Repair\n"
							+ ChatColor.of("#bd52ff") + "/workbench " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a portable workbench\n"
							+ ChatColor.of("#bd52ff") + "/anvil " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a portable anvil\n"
							+ ChatColor.of("#bd52ff") + "/grindstone " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a grindstone\n"
							+ ChatColor.of("#bd52ff") + "/loom " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a portable loom\n"
							+ ChatColor.of("#bd52ff") + "/smithingtable " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a smithingtable\n"
							+ ChatColor.of("#bd52ff") + "/stonecutter " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a stonecutter\n"
							+ ChatColor.of("#bd52ff") + "/cartographytable " + ChatColor.of("#dca3ff") + ChatColor.ITALIC + "- Opens a cartographytable\n"
							+ ChatColor.of("#ffc266") + ChatColor.STRIKETHROUGH + "x-----------------------------------x"
							);
				}
				else {
					player.sendMessage(ChatColor.RED + "Wrong usage. Try /help [1/2/3].");
				}
				break;
			case "fixhealth":
		    	  if (!player.hasPermission("acgeneral.fixhealth")) {
						player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
						return false;
					}
					player.setMaxHealth(20);
		        break;
		    case "reddit":
		    	player.sendMessage(ChatColor.of("#6464f2") + "Here's the link to our reddit: " + ChatColor.ITALIC + "www.reddit.com/r/AnarchyCraftMC/");
		        break;
		        
		    case "suggestion":
		        player.sendMessage(ChatColor.of("#6464f2") + "Here's the link to the suggestions form: " + ChatColor.ITALIC + "www.forms.gle/C8JV7jgot3eHh2vG7");
		        break;

		    case "wild":
		    	ItemStack hand = player.getInventory().getItemInMainHand();
		    	
		    	//If first join and doesn't have leaves, give leaves to player
		    	
		    	if (!player.hasPlayedBefore()) {
    				
		    		boolean hasLeaves = false;
		    		
		    		ItemStack[] inventoryItems = player.getInventory().getContents();

    				for (ItemStack item : inventoryItems) {
    					if (item != null && item.getType() != Material.AIR) {
    						if (item.getLore() != null) {
    							if (item.getLore().contains(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use /wild to tp")) {
    								hasLeaves = true;
    							}
    						}
    					}
    				}
    				
    				if (hasLeaves == false) {
    		    		Rewards.plugin.giveReward("wild_leaves", player, false);
    		    		player.sendMessage(ChatColor.RED + "You must be holding Wild Leaves to use /wild. They have been added to your inventory.");
    		    		return false;
    		    	}
    			}
		    	
		    	
		    	if (hand != null && hand.getType() != Material.AIR) {
		    		if (hand.getItemMeta().getLore() == null) {
		    			player.sendMessage(ChatColor.RED + "You must hold Wild Leaves to use /wild. They are included in your starter items when you first join.");
		    			return false;
		    		}
		    		if (hand.getItemMeta().getLore().contains(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use /wild to tp")) {

		    			String UUID = player.getUniqueId().toString();
		    			
						player.sendMessage(ChatColor.BLUE + "Teleporting to the wild in 5 seconds, don't move...");
						cancelRunnable(player);
						tpTasks.remove(UUID);
						wild = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								if (hand.getAmount() > 0) {
									player.sendMessage(ChatColor.BLUE + "Teleporting to the wild...");
									tpTasks.remove(UUID);
									player.teleport(getRandomLocation(), TeleportCause.COMMAND);
									hand.setAmount(hand.getAmount() - 1);
									
									//Add player to noFallDamagePlayers for 10 seconds
									noFallDamagePlayers.add(UUID);
									Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										
										@Override
										public void run() {
											noFallDamagePlayers.remove(UUID);
											Bukkit.getServer().dispatchCommand(player, "sethome");
										}
									}, 10 * 20);
								}
								else {
									player.sendMessage(ChatColor.RED + "You must hold Wild Leaves when teleporting to the wild.");
									tpTasks.remove(UUID);
								}
							}
						}, 5 * 20); //Count down from 10
						tpTasks.put(UUID, wild);
		    		}
		    		else {
		    			player.sendMessage(ChatColor.RED + "You must hold Wild Leaves to use /wild. They are included in your starter items when you first join.");
		    		}
				}
				else {
					player.sendMessage(ChatColor.RED + "You must hold Wild Leaves to use /wild. They are included in your starter items when you first join.");
				}
		    	break;
		    	
		    case "wildleaves":
		    	if (player.hasPermission("acgeneral.wildleaves")) {
		    		Rewards.getInstance().giveReward("wild_leaves", player, false);
		    	}
		    	else {
		    		player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
		    	}
		    	break;
		    }
		}
		else {
			sender.sendMessage(ChatColor.RED + "You must be a player.");
		}
		return true;
	}
	
	@EventHandler
	public void onCommandSend (PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		//
		int spaceIndex = event.getMessage().indexOf(" ");
		String label = "";
		if (spaceIndex != -1) {
			label = event.getMessage().substring(spaceIndex);
		}
		else {
			label = event.getMessage();
		}
		
		if (playersInTutorial.contains(player.getUniqueId())) {
			if (!label.toLowerCase().equals("help") && !label.toLowerCase().equals("msg") || !label.toLowerCase().equals("r") || !label.toLowerCase().equals("g")) {
				player.sendMessage(ChatColor.RED + "Please answer the 3 tutorial questions first by pressing the wooden buttons.");
				event.setCancelled(true);
			}
		}
	}
	
	//Wild leaves and first join things
	@EventHandler
	public void onJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!(player.hasPlayedBefore())) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta removeprefix 2");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta addprefix 2 &r");
			Rewards.plugin.giveReward("wild_leaves", player, false);
			playersInTutorial.add(player.getUniqueId());
		}
	}
	
	//Tutorial quiz
	@EventHandler
	public void buttonPress (PlayerInteractEvent event) {
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				event.getClickedBlock().getType() == Material.BIRCH_BUTTON) {
				
				Player player = event.getPlayer();
				
				//Q1
				//Correct (/g create)
				if (event.getClickedBlock().getX() == 107 && event.getClickedBlock().getY() == 253 && event.getClickedBlock().getZ() == 190) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), 24.5, 251, 281.5, 90, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 1);
					player.sendMessage("");
					player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Correct! Try /g for a full list of guild commands");
					player.sendMessage("");
				}
				//Incorrect (/suicide)
				else if (event.getClickedBlock().getX() == 99 && event.getClickedBlock().getY() == 253 && event.getClickedBlock().getZ() == 190) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), 103.5, 252, 162.5, 0, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 0.1f);
					player.sendMessage("");
					player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "That's not it... Hope you're doing okay x");
					player.sendMessage("");
				}
				
				//Q2
				//Correct (Tuesdays and Saturdays at 8pm EST)
				else if (event.getClickedBlock().getX() == -5 && event.getClickedBlock().getY() == 252 && event.getClickedBlock().getZ() == 277) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), 153.5, 251, 98.5, -90, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 1);
					player.sendMessage("");
					player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Correct! Try /gw to see when the next attack is");
					player.sendMessage("");
				}
				//Incorrect (5am on Martin Luther King Day)
				else if (event.getClickedBlock().getX() == -5 && event.getClickedBlock().getY() == 252 && event.getClickedBlock().getZ() == 285) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), 24.5, 251, 281.5, 90, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 0.1f);
					player.sendMessage("");
					player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Um no that's wrong... Like why would it be that?");
					player.sendMessage("");
				}
				
				//Q3
				//Correct (Dragon eggs and Nether stars)
				else if (event.getClickedBlock().getX() == 181 && event.getClickedBlock().getY() == 252 && event.getClickedBlock().getZ() == 94) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), -57, 105, 181, -180, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 1);
					player.sendMessage("");
					player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Correct, check out some of the unique items at /warp Traders");
					player.sendMessage("");
					playersInTutorial.remove(player.getUniqueId());
					
					new BukkitRunnable() {
						
						int counter = 0;
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							counter ++;
							
							if (counter == 2) {
								player.sendMessage("");
								player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "When you are ready to leave spawn, use /wild");
								player.sendMessage("");
							}
							else if (counter == 5) {
								player.sendMessage("");
								player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Get dragon eggs and nether stars from:");
								player.sendMessage(ChatColor.GOLD  + "1. " + ChatColor.GRAY + "/vote");
								player.sendMessage(ChatColor.GOLD  + "2. " + ChatColor.GRAY + "Crate Drops (drops 6 times per day)");
								player.sendMessage(ChatColor.GOLD  + "3. " + ChatColor.GRAY + "Killing elemental dragons in the end");
								player.sendMessage(ChatColor.GOLD  + "4. " + ChatColor.GRAY + "Fishing Tournament (/fish)");
								player.sendMessage(ChatColor.GOLD  + "5. " + ChatColor.GRAY + "Guild Wars (/gw)");
								player.sendMessage("");
								cancel();
							}							
						}
					}.runTaskTimer(plugin, 150, 150); //7.5 seconds
					
					//First join music
//					Location location = new Location(Bukkit.getServer().getWorld("world"), 90, 124, 188);
//					player.playSound(location, Sound.MUSIC_MENU, 10, 1);
				}
				//Incorrect (Cocain and prostitutes)
				else if (event.getClickedBlock().getX() == 181 && event.getClickedBlock().getY() == 252 && event.getClickedBlock().getZ() == 102) {
					player.teleport(new Location(Bukkit.getServer().getWorld("world"), 153.5, 251, 98.5, -90, 0), TeleportCause.PLUGIN);
					player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_FALL, 10, 0.1f);
					player.sendMessage("");
					player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "I wish...");
					player.sendMessage("");
				}
			}
	}
	
	//Don't drop wild leaves
	public void onDropItem (PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().getItemMeta().getLore().contains(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use /wild to tp")) {
			event.setCancelled(true);
		}
	}

	//For wild
	public Location getRandomLocation() {
		
		Random randomX = new Random();
		Random randomZ = new Random();
		
		int x = randomX.nextInt(20000) - 10000; //From -10k to +10k
		int z = randomZ.nextInt(20000) - 10000;
		
		if ((z > -5000 && z < 5000) && (x > -5000 && x < 5000)) {
			return getRandomLocation();
		}
		else {
			Location location = new Location(Bukkit.getServer().getWorld("world"), x, 256, z);
			return location;
		}
	}
	
	//Wild 5 second teleport
	public void cancelRunnable(Player player) {
		String UUID = player.getUniqueId().toString();
		if (tpTasks.containsKey(UUID)) {
			Bukkit.getScheduler().cancelTask(tpTasks.get(UUID));
			tpTasks.remove(UUID);
		}
	}
	
	@EventHandler
	public void onMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		if ((Math.round(from.getX() * 100) / 100) == (Math.round(to.getX() * 100) /100) && //they can move 0.001 blocks
			(Math.round(from.getY() * 100) / 100) == (Math.round(to.getY() * 100) / 100) && 
			(Math.round(from.getZ() * 100) / 100) == (Math.round(to.getZ() * 100) / 100)) {
			return;
		}
		else {
			String UUID = player.getUniqueId().toString();
			if (tpTasks.containsKey(UUID)) {
				cancelRunnable(player);
				tpTasks.remove(UUID);
				player.sendMessage(ChatColor.RED + "Teleport cancelled...");
			}
		}
	}
	
	@EventHandler
	public void onDamage (EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String UUID = player.getUniqueId().toString();
			if (tpTasks.containsKey(UUID)) {
				cancelRunnable(player);
				tpTasks.remove(UUID);
				player.sendMessage(ChatColor.RED + "Teleport cancelled...");
			}
			if (noFallDamagePlayers.contains(UUID)) {
				event.setCancelled(true);
			}
		}
	}
	
	//Can mine end portal frame
	@EventHandler
	public void onMine (PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
				int x = event.getClickedBlock().getX();
				int z = event.getClickedBlock().getZ();
				if (x > -100 && x < 250 && z > -100 && z < 350) { //spawn
					return;
				}
				else {
					event.getClickedBlock().breakNaturally();
					event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.END_PORTAL_FRAME));
				}
			}
		}
	}
}
