package me.frankhan2004.testplugin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangManager {
    private final String id;
    private final Map<String,String> message;

    public LangManager(String id, YamlConfiguration yaml, List<String> load) {
        this.id = id;
        this.message = new HashMap<>();
        for (String s:load) {
            message.put(s,yaml.getString(s));
        }
    }

    public String getId() {
        return id;
    }

    public String getMessage(String string) {
        return message.get(string);
    }
}
