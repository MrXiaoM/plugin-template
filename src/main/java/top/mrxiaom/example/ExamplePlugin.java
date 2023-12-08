package top.mrxiaom.example;

import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.example.func.AbstractPluginHolder;
import top.mrxiaom.example.func.DatabaseManager;
import top.mrxiaom.example.func.GuiManager;
import top.mrxiaom.example.hooks.Placeholder;
import top.mrxiaom.example.commands.CommandMain;
import top.mrxiaom.example.utils.Util;

@SuppressWarnings({"unused"})
public class ExamplePlugin extends JavaPlugin implements Listener, TabCompleter {
    private static ExamplePlugin instance;
    public static ExamplePlugin getInstance() {
        return instance;
    }
    private GuiManager guiManager = null;
    Placeholder papi = null;
    Economy economy;
    public GuiManager getGuiManager() {
        return guiManager;
    }
    @NotNull
    public Economy getEconomy() {
        return economy;
    }
    @NotNull
    public Placeholder getPapi() {
        return papi;
    }
    @Override
    public void onEnable() {
        instance = this;

        loadHooks();

        loadFunctions();
        reloadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("插件加载完毕");
    }

    public void loadFunctions() {
        try {
            for (Class<?> clazz : Lists.newArrayList(
                    CommandMain.class, DatabaseManager.class
            )) {
                clazz.getDeclaredConstructor(getClass()).newInstance(this);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        guiManager = new GuiManager(this);
    }

    public void loadHooks() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        Util.registerPlaceholder(getLogger(), papi = new Placeholder(this));
    }

    @Override
    public void onDisable() {
        AbstractPluginHolder.disableAllModule();
        getLogger().info("插件已卸载");
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();

        FileConfiguration config = getConfig();
        AbstractPluginHolder.reloadAllConfig(config);
    }
}
