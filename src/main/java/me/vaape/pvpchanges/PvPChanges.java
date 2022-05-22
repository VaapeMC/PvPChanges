package me.vaape.pvpchanges;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class PvPChanges extends JavaPlugin implements Listener {

    public static PvPChanges plugin;

    public HashMap<UUID, Integer> totemCooldown = new HashMap<UUID, Integer>();
    private HashMap<UUID, BukkitRunnable> totemCooldownTask = new HashMap<UUID, BukkitRunnable>();
    public HashMap<UUID, Integer> tridentCooldown = new HashMap<UUID, Integer>();
    private HashMap<UUID, BukkitRunnable> tridentCooldownTask = new HashMap<UUID, BukkitRunnable>();

    public void onEnable() {
        plugin = this;
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "PvPChanges has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        plugin = null;
    }

    //Totem cooldown
    @EventHandler
    public void onResurrent(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasCooldown(Material.TOTEM_OF_UNDYING)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Your totem of undying was on cooldown.");
            }
            else player.setCooldown(Material.TOTEM_OF_UNDYING, 30 * 20);
        }
    }

    //End crystal
    @EventHandler
    public void onEndCrystalDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {
            if (!(event.getEntity() instanceof LivingEntity entity)) return;
            if (event.getDamager().getType() != EntityType.ENDER_CRYSTAL) return;
            Location crystalLoc = event.getDamager().getLocation();
            Location entityLoc = entity.getLocation();
            int distance = (int) getDistanceBetween(crystalLoc, entityLoc);
            int finalDamage = 10 - distance; //finalDamage higher when distance smaller
            if (finalDamage > 8) {
                finalDamage = 8;
            }

            if (entity.getHealth() <= finalDamage) {
                entity.damage(1000);
            }
            else {
                entity.setHealth(entity.getHealth() - finalDamage);
            }
            event.setDamage(0);
        }
    }

    //Trident
    @EventHandler
    public void onIntertact (PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("pvpchanges.trident.bypass")) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() != Material.TRIDENT) return;
        if (player.hasCooldown(Material.TRIDENT)) return;
        for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
            if (entity instanceof Player) {
                event.getPlayer().setCooldown(Material.TRIDENT, 30 * 20);
                return;
            }
        }
    }

    //Gapple nerf
    @EventHandler
    public void onEat (PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {

            Player player = event.getPlayer();

            //Check if has infinite regen
            boolean hadInfiniteRegen = false;

            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                if (player.getPotionEffect(PotionEffectType.REGENERATION).getDuration() >= 9999) {
                    hadInfiniteRegen = true;
                }
            }


            boolean finalhadInfiniteRegen = hadInfiniteRegen;
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (finalhadInfiniteRegen) {
                        player.removePotionEffect(PotionEffectType.REGENERATION); //Remove gapple regen
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)); //Reapply infinite regen
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5*20, 1)); //Add nerfed gapple regen
                        }
                    else {
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5*20, 1), true); //Add nerfed gapple regen
                    }

                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.removePotionEffect(PotionEffectType.ABSORPTION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30*20, 0), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120*20, 0), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15*20, 0), true);
                }
            }.runTaskLater(plugin, 1);
        }
    }

    public boolean inSpawn(Location location) {
        if (location.getWorld() == Bukkit.getWorld("world")) {
            if (location.getX() > -100 && location.getX() < 250 &&
                    location.getZ() > -100 && location.getZ() < 350) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public double getDistanceBetween(Location location1, Location location2) {
        double x1 = location1.getX();
        double y1 = location1.getY();
        double z1 = location1.getZ();
        double x2 = location2.getX();
        double y2 = location2.getY();
        double z2 = location2.getZ();

        double x = x2 - x1;
        double y = y2 - y1;
        double z = z2 - z1;

        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));

    }
}
