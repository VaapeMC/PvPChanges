package me.vaape.pvpchanges;

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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityResurrectEvent;
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

public class PvPChanges extends JavaPlugin implements Listener{
	
	public static PvPChanges plugin;
	
	public HashMap<UUID, Integer> totemCooldown = new HashMap<UUID, Integer>();
	private HashMap<UUID, BukkitRunnable> totemCooldownTask = new HashMap<UUID, BukkitRunnable>();
	
	public void onEnable() {
		plugin = this;
		getLogger().info(ChatColor.GREEN + "PvPChanges has been enabled!");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
		plugin = null;
	}
	
	//Totem cooldown
	@EventHandler
	public void onResurrent (EntityResurrectEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			UUID UUID = player.getUniqueId();
			
			if (!(player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING)) {
				return;
			}
			
			if (totemCooldown.containsKey(UUID)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Your totem is on cooldown for " + totemCooldown.get(UUID) + " more seconds.");
			}
			else {
				totemCooldown.put(UUID, 30);
				totemCooldownTask.put(UUID, new BukkitRunnable() {
					
					@Override
					public void run() {
						
						totemCooldown.put(UUID, totemCooldown.get(UUID) - 1); //Lower cooldown by 1 second
						if (totemCooldown.get(UUID) == null) {
							
						}
						if (totemCooldown.get(UUID) == 0) {
							totemCooldown.remove(UUID);
							totemCooldownTask.remove(UUID);
							player.sendMessage(ChatColor.GREEN + "Totem of Undying cooldown expired");
							cancel();
						}
					}
				});
				
				totemCooldownTask.get(UUID).runTaskTimer(plugin, 20, 20);
			}
		}
	}
	
	//End crystal
	@EventHandler
	public void onEndCrystalDamage (EntityDamageEvent event) {
		if (event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION) {
			if (event.getDamage() > 8) {
				event.setDamage(8);
			}
		}
	}
}
