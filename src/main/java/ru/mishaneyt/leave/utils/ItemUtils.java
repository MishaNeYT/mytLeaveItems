package ru.mishaneyt.leave.utils;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.mishaneyt.leave.config.ConfigManager;

import java.util.List;

public class ItemUtils {

    public static void get(Player t, int amount, String name_item) {
        ConfigurationSection section = ConfigManager.getConfigItems().getConfigurationSection("Items." + name_item);

        // Material
        Material material = Material.valueOf(section.getString("Material").toUpperCase());

        // Data
        int data = section.getInt("Data");

        // Item
        ItemStack is = new ItemStack(material, amount, (byte) data);
        ItemMeta im = is.getItemMeta();

        // Hide Flag
        boolean hide_flags = section.getBoolean("Options.HideFlags");
        if (hide_flags) {
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            im.addItemFlags(ItemFlag.HIDE_DESTROYS);
        }

        // Name
        String name = section.getString("Name");
        im.setDisplayName(Utils.color(name));

        // Lore
        List<String> l = section.getStringList("Lore");
        List<String> lore = Lists.newArrayList();
        for (String line : l) lore.add(Utils.color(line));

        // Unbreakable
        boolean unbreakable = section.getBoolean("Options.Unbreakable");
        if (unbreakable) im.setUnbreakable(true);

        im.setLore(lore);
        is.setItemMeta(im);

        // Glow
        boolean enchant = section.getBoolean("Options.Glow");
        if (enchant) {
            is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        }

        // Add item in Inventory player
        t.getInventory().addItem(is);
    }
}