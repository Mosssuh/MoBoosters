package mv.mossuh.moboosters;

import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.EVENT.Applicators.MinecraftBoost;
import mv.mossuh.moboosters.COMMANDS.Commands;
import mv.mossuh.moboosters.COMMANDS.TabCompleter;
import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.MANAGER.ConfigManager;
import mv.mossuh.moboosters.DATA.Config.Messages;
import mv.mossuh.moboosters.UTILITIES.Cooldown.BoosterCooldown;
import mv.mossuh.moboosters.MANAGER.BoosterDataManager;
import mv.mossuh.moboosters.EVENT.ClaimItemBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.EVENT.Hooks.CentralBoosterListener;
import mv.mossuh.moboosters.UTILITIES.PluginChecker;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.mocore.DATABASE.ConnectionMySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class MoBoosters extends JavaPlugin {

    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();

    private static MoBoosters instance;
    private ConfigManager configManager;
    private CentralBoosterListener centralBoosterListener;
    private ConnectionMySQL connection;

    @Override
    public void onEnable() {
        if (!isPluginEnabled("MoCore")) {
            UtilString.get("&8[&bMoBoosters&8] &cMoCore isn't found on the server, it is required").hex().sendMessageInConsole();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        configManager = new ConfigManager(this);
        configManager.configure();
        Messages.load(this);
        Config.load(this);

        if (Config.MYSQL_ENABLED) {
            this.connection = new ConnectionMySQL(Config.MYSQL_HOST, Config.MYSQL_PORT, Config.MYSQL_DATABASE, Config.MYSQL_USER, Config.MYSQL_PASSWORD);
        }

        registerCommands();
        registerOthers();
        registerApplicators();


        BoosterDataManager.initialize();
        BoosterDataManager.registerDatabaseInMaps(BoosterType.PERSONAL);
        BoosterDataManager.registerDatabaseInMaps(BoosterType.GLOBAL);

        BoosterCooldown.initializeActiveBoosters(BoosterType.PERSONAL);
        BoosterCooldown.initializeActiveBoosters(BoosterType.GLOBAL);

        startSaverTimer();

        UtilString.get(Config.PREFIX+" &aHas been enabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UtilString.get(Config.PREFIX+" &aVersion: " + version).hex().sendMessageInConsole();
    }

    @Override
    public void onDisable() {
        stopTimers();

        saveBoostersData();
        Bukkit.getConsoleSender().sendMessage(UtilString.get(Config.PREFIX+" &aSaving boosters data in files...").hex().apply());

        UtilString.get(Config.PREFIX+" &cHas been disabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UtilString.get(Config.PREFIX+" &cVersion: " + version).hex().sendMessageInConsole();
    }

    public Connection getConnection() {
        if (Config.MYSQL_ENABLED) {
            return connection.getConnection();
        } else {
            try {
                return DriverManager.getConnection("jdbc:sqlite:" + MoBoosters.getInstance().getConfigManager().getMoBoostersData().getFile().getAbsolutePath());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private void registerApplicators() {
        BoostersAPI.registerApplicator(this, new MinecraftBoost());
    }
    private void registerOthers() {
        CentralBoosterListener central = new CentralBoosterListener();
        centralBoosterListener = central;
        this.getServer().getPluginManager().registerEvents(central, this);

        this.getServer().getPluginManager().registerEvents(new PluginChecker(), this);
        this.getServer().getPluginManager().registerEvents(new ClaimItemBooster(), this);
    }

    public CentralBoosterListener getCentralBoosterListener() { return centralBoosterListener; }
    public ConfigManager getConfigManager() { return configManager; }
    public static MoBoosters getInstance() { return instance; }
    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("moboosters")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("moboosters")).setTabCompleter(new TabCompleter());
    }


    private static Timer boostersTimer;
    private static void startSaverTimer() {
        boostersTimer = new Timer();
        boostersTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveBoostersData();
                Bukkit.getConsoleSender().sendMessage(UtilString.get(Config.PREFIX+" &aSaving boosters data in files...").hex().apply());
            }
        }, 60000, Config.SAVE_IN_DATABASE_INTERVAL*1000);
    }

    private static void stopTimers() {
        if (boostersTimer != null) {
            boostersTimer.cancel();
        }
    }

    private static void saveBoostersData() {
        BoosterDataManager.registerMapsInDatabase(BoosterType.PERSONAL);
        BoosterDataManager.registerMapsInDatabase(BoosterType.GLOBAL);
        BoosterDataManager.registerMapsInDatabase(BoosterType.SUPERIORSKYBLOCK2);
    }


    private boolean isPluginEnabled(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}
