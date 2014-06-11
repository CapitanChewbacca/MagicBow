package com.capitanchewbacca.magicbow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Created by Lorenzo on 10/06/2014.
 */
public class Main extends JavaPlugin implements Listener {

    EntityType[] animals = new EntityType[] {EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN, EntityType.OCELOT, EntityType.WOLF, EntityType.HORSE};

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        if (this.getConfig().get("enabled") == null) this.getConfig().set("enabled", false);
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("magicbow")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD+"------------ MagicBow -----------------");
                sender.sendMessage(ChatColor.DARK_AQUA+"Status: "+((this.getConfig().getBoolean("enabled")) ? ChatColor.GREEN+"ENABLED" : ChatColor.RED+"DISABLED"));
                return true;
            }
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                this.getConfig().set("enabled", Boolean.valueOf(args[0]));
                this.saveConfig();
                sender.sendMessage(ChatColor.AQUA+"Status of MagicBow changed to "+((Boolean.valueOf(args[0])) ? ChatColor.GREEN+"ENABLED" : ChatColor.RED+"DISABLED"));
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        Random r = new Random();
        if (e.getProjectile() instanceof Arrow && ((Arrow) e.getProjectile()).getShooter() instanceof Player) {
            if (!this.getConfig().getBoolean("enabled")) return;
            Player shooter = (Player) ((Arrow) e.getProjectile()).getShooter();
            Arrow projectile = (Arrow) e.getProjectile();
            if (shooter.hasPermission("magicbow.use")) {
                ItemStack bow = e.getBow();
                if (bow.getItemMeta().getDisplayName() != null && bow.getItemMeta().getDisplayName().startsWith("§r§bMagicBow")) {
                    if (bow.getItemMeta().getLore() != null && bow.getItemMeta().getLore().get(0).toLowerCase().contains("random")) {
                        Entity ent = projectile.getWorld().spawnEntity(projectile.getLocation(), animals[r.nextInt(animals.length - 1)]);
                        ent.setVelocity(projectile.getVelocity());
                        if (r.nextInt(100) < 20) ((Animals) ent).setBaby();
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
