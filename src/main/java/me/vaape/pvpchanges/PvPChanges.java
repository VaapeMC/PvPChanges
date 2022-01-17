package me.vaape.pvpchanges;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
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
        getLogger().info(ChatColor.GREEN + "PvPChanges has been enabled!");
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
            UUID UUID = player.getUniqueId();

            if (!(player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING)) {
                return;
            }

            if (totemCooldown.containsKey(UUID)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Your totem is on cooldown for " + totemCooldown.get(UUID) + " " +
                                           "more seconds.");
            } else {
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
    public void onEndCrystalDamage(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION) {
            if (event.getDamage() > 8) {
                event.setDamage(8);
            }
        }
    }

    //Trident
    @EventHandler
    public void onRiptide (PlayerRiptideEvent event) {
        if (event.getPlayer().hasPermission("pvpchanges.trident")) return;

        UUID UUID = event.getPlayer().getUniqueId();

        if (tridentCooldown.containsKey(UUID)) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().sendMessage(ChatColor.RED + "Your trident is on cooldown for " + tridentCooldown.get(event.getPlayer().getUniqueId()) + " more seconds.");
                    event.getPlayer().setVelocity(new Vector(0,0,0));
                    float randomPitch = (float) ((Math.random() * 180) - 90);
                    float randomYaw = (float) ((Math.random() * 360) - 180);
                    Location discombobulatedLocation = event.getPlayer().getLocation().clone();
                    discombobulatedLocation.setYaw(randomYaw);
                    discombobulatedLocation.setPitch(randomPitch);
                    event.getPlayer().teleport(discombobulatedLocation);
                }
            }.runTaskLater(plugin, 1);
            return;
        }

        tridentCooldown.put(UUID, 30);
        tridentCooldownTask.put(UUID, new BukkitRunnable() {

            @Override
            public void run() {

                tridentCooldown.put(UUID, tridentCooldown.get(UUID) - 1); //Lower cooldown by 1 second
                if (tridentCooldown.get(UUID) == null) {

                }
                if (tridentCooldown.get(UUID) == 0) {
                    tridentCooldown.remove(UUID);
                    tridentCooldownTask.remove(UUID);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Trident cooldown expired");
                    cancel();
                }
            }
        });

        tridentCooldownTask.get(UUID).runTaskTimer(plugin, 20, 20);
    }
}
