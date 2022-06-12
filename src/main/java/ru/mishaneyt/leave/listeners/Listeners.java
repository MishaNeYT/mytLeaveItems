package ru.mishaneyt.leave.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.config.ConfigUtils;
import ru.mishaneyt.leave.utils.Cooldown;
import ru.mishaneyt.leave.utils.Logger;
import ru.mishaneyt.leave.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    public static List<Player> damage = new ArrayList<>();

    public Listeners(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() == null) return;
            Player p = e.getPlayer();

            ItemStack is = e.getItem();
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();

            for (String items : ConfigManager.getConfigItems().getConfigurationSection("Items").getKeys(false)) {
                ConfigurationSection section = ConfigManager.getConfigItems().getConfigurationSection("Items." + items);

                String name = section.getString("Name");
                String sound = section.getString("Sound");
                String type = section.getString("Options.Type");
                double height = section.getInt("Height");
                boolean _break = section.getBoolean("Options.Break");

                if (is == null) return;
                if (im.getDisplayName().equals(Utils.color(name))) {

                    if (ConfigUtils.ENABLE_COOLDOWN) {
                        if (Main.getCooldown().containsKey(p)) {
                            p.sendMessage(ConfigUtils.COOLDOWN.replace("{time}", String.valueOf(Main.getCooldown().get(p))));
                            return;
                        }

                        Cooldown.addCooldown(p);
                    }

                    if (_break)
                        is.setAmount(is.getAmount() - 1);

                    if (!section.isBoolean("Options.Break"))
                        is.setAmount(is.getAmount() - 1);

                    if (type.equalsIgnoreCase("JUMP"))
                        p.setVelocity(new Vector(0, height / 10, 0));

                    else if (type.equalsIgnoreCase("TELEPORT"))
                        p.teleport(p.getLocation().clone().add(0, height, 0), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    else {
                        p.sendMessage("§c[§4LeaveItems§c] §cПроизошла ошибка! Тип этого предмета введён не правильно.");
                        Logger.error("Тип предмета " + p.getName() + " введён не правильно. Исправьте это в конфиге items.yml");
                        return;
                    }

                    if (sound != null)
                        p.playSound(p.getLocation(), Sound.valueOf(sound), 1, 1);

                    for (String line : section.getStringList("Options.Commands"))
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("{player}", p.getName()));

                    if (ConfigUtils.ENABLE_TITLE)
                        p.sendTitle(ConfigUtils.TITLE, ConfigUtils.SUBTITLE);

                    if (ConfigUtils.ENABLE_CHAT)
                        p.sendMessage(ConfigUtils.CHAT);

                    damage.add(p);
                }
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (ConfigUtils.FALL_DAMAGE) return;
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                if (damage.contains(p)) {
                    e.setCancelled(true);
                    damage.remove(p);
                }
        }
    }
}