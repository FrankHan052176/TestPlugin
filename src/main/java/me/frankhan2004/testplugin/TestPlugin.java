package me.frankhan2004.testplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestPlugin extends JavaPlugin implements CommandExecutor {
    private static TestPlugin plugin;
    private final Map<String,LangManager> langs = new HashMap<>();
    public final File langsFolder = new File(getDataFolder(),"langs");
    public final File dataFile = new File(getDataFolder(),"data.yml");
    public List<String> loadList;

    public static TestPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        // 保存config.yml
        saveDefaultConfig();
        loadList = getConfig().getStringList("readEntry");
        // 保存另外的文件,目录使用相对目录
        langsFolder.mkdirs();
        saveResource("langs/zh_cn.yml",false);
        saveResource("data.yml",false);
        loadLang();
        getCommand("test").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadLang() {
        File[] files = langsFolder.listFiles();
        if (files != null) for (File lang:files) {
            String id = lang.getName().split("\\.")[0];
            langs.put(id,new LangManager(
                    id,
                    YamlConfiguration.loadConfiguration(lang),
                    loadList));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2) {
            if (langs.containsKey(args[0])) {
                LangManager lang = langs.get(args[0]);
                if (loadList.contains(args[1])) {
                    sender.sendMessage(lang.getMessage(args[1]));
                }else {
                    sender.sendMessage("所需要的文本不在读取列表中");
                }
            }else {
                sender.sendMessage("所需要的语言没有被读取");
            }
        }else if (args.length == 1) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(dataFile);
            if (args[0].equals("serialize")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    yaml.set("item",Util.itemStackSerialize(player.getInventory().getItemInMainHand()));
                }
            }else if (args[0].equals("deserialize")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.getInventory().setItemInMainHand(Util.itemStackDeserialize(yaml.getString("item")));
                }
            }
        }
        return false;
    }
}
