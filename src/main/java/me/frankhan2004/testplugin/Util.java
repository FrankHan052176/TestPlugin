package me.frankhan2004.testplugin;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Util {
    public static String itemStackSerialize(ItemStack itemStack) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(TestPlugin.getPlugin().dataFile);
        yml.set("item", itemStack);
        return yml.saveToString();
    }
    public static ItemStack itemStackDeserialize(String str) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(TestPlugin.getPlugin().dataFile);
        ItemStack item;
        try {
            yml.loadFromString(str);
            item = yml.getItemStack("item");
        } catch (InvalidConfigurationException ex) {
            item = new ItemStack(Material.AIR, 1);
        }
        return item;
    }
}
