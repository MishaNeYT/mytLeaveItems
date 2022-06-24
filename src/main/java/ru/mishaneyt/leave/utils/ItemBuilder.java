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

public class ItemBuilder {
    private final Player player;
    private final String name_item;
    private final int amount;

    public ItemBuilder(Player player, String name_item, int amount) {
        this.player = player;
        this.name_item = name_item;
        this.amount = amount;
    }

    public void get() {
        ConfigurationSection section = ConfigManager.getConfigItems().getConfigurationSection("Items." + this.name_item);

        // Material
        Material material = Material.valueOf(section.getString("Material").toUpperCase());

        // Data
        int data = section.getInt("Data");

        // Item
        ItemStack is = new ItemStack(material, this.amount, (byte) data);
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
        im.setDisplayName(Utils.replace(name));

        // Lore
        List<String> l = section.getStringList("Lore");
        List<String> lore = Lists.newArrayList();
        for (String line : l) lore.add(Utils.replace(line));

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
        this.player.getInventory().addItem(is);
    }
}