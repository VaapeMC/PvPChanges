package me.vaape.pvpchanges;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PvPChanges extends JavaPlugin implements Listener {

    public static PvPChanges plugin;

    public HashMap<UUID, Integer> totemCooldown = new HashMap<UUID, Integer>();
    private HashMap<UUID, BukkitRunnable> totemCooldownTask = new HashMap<UUID, BukkitRunnable>();

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
}
