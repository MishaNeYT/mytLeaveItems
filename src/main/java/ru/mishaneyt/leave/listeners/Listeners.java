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
import org.bukkit.util.Vector;
import ru.mishaneyt.leave.Main;
import ru.mishaneyt.leave.config.ConfigManager;
import ru.mishaneyt.leave.utils.Cooldown;
import ru.mishaneyt.leave.utils.Logger;
import ru.mishaneyt.leave.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    private final Main main;
    public List<Player> damage = new ArrayList<>();

    public Listeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        ItemStack is = e.getItem();
        if (is == null) return;

        ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
        if (im == null) return;

        for (String items : ConfigManager.getItems().getConfigurationSection("Items").getKeys(false)) {
            ConfigurationSection section = ConfigManager.getItems().getConfigurationSection("Items." + items);
            if (section == null) return;

            String name = section.getString("Name");
            String sound = section.getString("Sound");
            String type = section.getString("Options.Type");
            double height = section.getInt("Height");
            boolean _break = section.getBoolean("Options.Break");

            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (im.getDisplayName() == null) return;
                if (im.getDisplayName().equals(Utils.color(name))) {

                    if (ConfigManager.getConfig().getBoolean("Cooldown.Enable")) {
                        if (this.main.getCooldown().containsKey(p)) {
                            p.sendMessage(Utils.replace(ConfigManager.getMessages().getString("Messages.Others.Cooldown").replace("{time}", String.valueOf(this.main.getCooldown().get(p)))));
                            return;
                        }

                        Cooldown cooldown = new Cooldown(this.main);
                        cooldown.addCooldown(p);
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

                    for (String line : section.getStringList("Options.Commands")) {
                        if (line.startsWith("player:")) {
                            line = line.replaceFirst("player:", "");
                            Bukkit.dispatchCommand(p, line.replace("{player}", p.getName()));
                        }
                        else if (line.startsWith("console:")) {
                            line = line.replaceFirst("console:", "");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("{player}", p.getName()));
                        }
                        else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("{player}", p.getName()));
                    }

                    if (ConfigManager.getConfig().getBoolean("Settings.Title"))
                        p.sendTitle(Utils.color(ConfigManager.getMessages().getString("Messages.First")),
                                Utils.color(ConfigManager.getMessages().getString("Messages.SubTitle")));

                    if (ConfigManager.getConfig().getBoolean("Settings.Chat"))
                        for (String m : ConfigManager.getMessages().getStringList("Messages.Chat.Message"))
                            p.sendMessage(Utils.replace(m));

                    this.damage.add(p);
                }
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            if (ConfigManager.getConfig().getBoolean("Settings.FallDamage")) return;

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                Player p = (Player) e.getEntity();

                if (this.damage.contains(p)) {
                    e.setCancelled(true);
                    this.damage.remove(p);
                }
            }
        }
    }
}