package it.scopped.superhomes;

import co.aikar.commands.PaperCommandManager;
import it.scopped.superhomes.backend.struct.CacheStorage;
import it.scopped.superhomes.backend.struct.DatabaseManager;
import it.scopped.superhomes.commands.AdminHomeCommand;
import it.scopped.superhomes.commands.HomeCommand;
import it.scopped.superhomes.listeners.GeneralListeners;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SuperHomes extends JavaPlugin {

    @Getter
    private static SuperHomes instance;

    private DatabaseManager databaseManager;
    private CacheStorage cacheStorage;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        databaseManager = new DatabaseManager(this);
        cacheStorage = new CacheStorage(this);

        loadCommandsAndListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommandsAndListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        PaperCommandManager paperCommandManager = new PaperCommandManager(instance);

        pluginManager.registerEvents(new GeneralListeners(this), this);
        paperCommandManager.registerCommand(new HomeCommand(this));
        paperCommandManager.registerCommand(new AdminHomeCommand(this));
    }
}
