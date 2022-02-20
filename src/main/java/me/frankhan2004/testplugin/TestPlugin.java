package me.frankhan2004.testplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestPlugin extends JavaPlugin implements CommandExecutor {
    private Map<String,LangManager> langs = new HashMap<>();
    public final File langsFolder = new File(getDataFolder(),"langs");
    public List<String> loadList;

    @Override
    public void onEnable() {
        // 保存config.yml
        saveDefaultConfig();
        loadList = getConfig().getStringList("readEntry");
        // 保存另外的文件,目录使用相对目录
        langsFolder.mkdirs();
        saveResource("langs/zh_cn.yml",false);
        loadLang();
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
        }
        return false;
    }
}
