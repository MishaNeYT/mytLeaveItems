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
import ru.mishaneyt.leave.config.ConfigUtils;
import ru.mishaneyt.leave.utils.Cooldown;
import ru.mishaneyt.leave.logger.Logger;
import ru.mishaneyt.leave.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    private final Main main;
    public List<Player> damage = new ArrayList<>();

    public Listeners(Main main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null) return;

        Player p = e.getPlayer();

        ItemStack is = e.getItem();
        if (is == null) return;

        ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
        if (im == null) return;

        for (String items : ConfigManager.getConfigItems().getConfigurationSection("Items").getKeys(false)) {
            ConfigurationSection section = ConfigManager.getConfigItems().getConfigurationSection("Items." + items);

            String name = section.getString("Name");
            String sound = section.getString("Sound");
            String type = section.getString("Options.Type");
            double height = section.getInt("Height");
            boolean _break = section.getBoolean("Options.Break");

            if (im.getDisplayName().equals(Utils.replace(name))) {

                if (ConfigUtils.ENABLE_COOLDOWN) {
                    if (this.main.getCooldown().containsKey(p)) {
                        p.sendMessage(ConfigUtils.COOLDOWN.replace("{time}", String.valueOf(this.main.getCooldown().get(p))));
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

                for (String line : section.getStringList("Options.Commands"))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("{player}", p.getName()));

                if (ConfigUtils.ENABLE_TITLE)
                    p.sendTitle(ConfigUtils.TITLE, ConfigUtils.SUBTITLE);

                if (ConfigUtils.ENABLE_CHAT)
                    for (String m : ConfigManager.getConfigMessages().getStringList("Messages.Chat.Message"))
                        p.sendMessage(Utils.replace(m));

                this.damage.add(p);
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            if (ConfigUtils.FALL_DAMAGE) return;

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