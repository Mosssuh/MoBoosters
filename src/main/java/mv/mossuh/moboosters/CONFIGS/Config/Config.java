package mv.mossuh.moboosters.CONFIGS.Config;

import mv.mossuh.moboosters.MoBoosters;
import mv.mossuh.mocore.UTILITIES.TimeFormat;
import org.bukkit.configuration.file.FileConfiguration;
import mv.mossuh.moboosters.BOOSTERS.AvailableBoosters.Identifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static final Map<String, String> intervalStringMap = new ConcurrentHashMap<>();
    private static final Map<String, Double> intervalDoubleMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> intervalLongMap = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> intervalBooleanMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> intervalListStringMap = new ConcurrentHashMap<>();
    private static final List<String> intervalsList = new ArrayList<>(Arrays.asList(
            "Config.save-in-database-interval",
            "Config.claim-booster-item-cooldown",
            "Config.time-format.year", "Config.time-format.month",
            "Config.time-format.week", "Config.time-format.day",
            "Config.time-format.hour", "Config.time-format.minute",
            "Config.time-format.second",
            "Config.identifiers", "Config.accumulative-boosters"
    ));

    public static long SAVE_IN_DATABASE_INTERVAL;
    public static int CLAIM_BOOSTER_ITEM_COOLDOWN;
    public static String TIME_FORMAT_YEAR;
    public static String TIME_FORMAT_MONTH;
    public static String TIME_FORMAT_WEEK;
    public static String TIME_FORMAT_DAY;
    public static String TIME_FORMAT_HOUR;
    public static String TIME_FORMAT_MINUTE;
    public static String TIME_FORMAT_SECOND;
    public static TimeFormat TIME_FORMAT;
    public static Identifiers IDENTIFIERS;
    public static boolean ACCUMULATIVE_BOOSTERS;
    public static String PREFIX = "&8[&bMoBoosters&8]";
    public static String PLUGIN_NAME = "MoBoosters";

    private static void loadConfig() {
        SAVE_IN_DATABASE_INTERVAL = new Config("Config.save-in-database-interval").getOrDefault(300L);
        CLAIM_BOOSTER_ITEM_COOLDOWN = new Config("Config.claim-booster-item-cooldown").getOrDefault(2);
        TIME_FORMAT_YEAR = new Config("Config.time-format.year").getOrDefault("");
        TIME_FORMAT_MONTH = new Config("Config.time-format.month").getOrDefault("");
        TIME_FORMAT_WEEK = new Config("Config.time-format.week").getOrDefault("");
        TIME_FORMAT_DAY = new Config("Config.time-format.day").getOrDefault("");
        TIME_FORMAT_HOUR = new Config("Config.time-format.hour").getOrDefault("");
        TIME_FORMAT_MINUTE = new Config("Config.time-format.minute").getOrDefault("");
        TIME_FORMAT_SECOND = new Config("Config.time-format.second").getOrDefault("");
        TIME_FORMAT = new TimeFormat(TIME_FORMAT_YEAR, TIME_FORMAT_MONTH, TIME_FORMAT_WEEK, TIME_FORMAT_DAY, TIME_FORMAT_HOUR, TIME_FORMAT_MINUTE, TIME_FORMAT_SECOND);
        IDENTIFIERS = new Identifiers(new Config("Config.identifiers").getOrDefault(new ArrayList<>()));
        ACCUMULATIVE_BOOSTERS = new Config("Config.accumulative-boosters").getOrDefault(false);
    }

    private final String intervalPosition;

    public Config(String intervalPosition) {
        this.intervalPosition = intervalPosition;
    }


    public String getOrDefault(String defaultValue) {
        return intervalStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public int getOrDefault(int defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return Math.toIntExact(intervalLongMap.get(intervalPosition));
        }
        return defaultValue;
    }

    public double getOrDefault(double defaultValue) {
        if (intervalDoubleMap.containsKey(intervalPosition)) {
            return intervalDoubleMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public long getOrDefault(long defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return intervalLongMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public List<String> getOrDefault(List<String> defaultValue) {
        return intervalListStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public boolean getOrDefault(boolean defaultValue) {
        return intervalBooleanMap.getOrDefault(intervalPosition, defaultValue);
    }
    public static void load(MoBoosters instance) {
        FileConfiguration mainConfig = instance.getConfigs().getConfig().getConfig();

        for (String intervalConfig : intervalsList) {
            if (mainConfig.isString(intervalConfig)) {
                intervalStringMap.put(intervalConfig, mainConfig.getString(intervalConfig));
            } else if (mainConfig.isList(intervalConfig)) {
                intervalListStringMap.put(intervalConfig, mainConfig.getStringList(intervalConfig));
            } else if (mainConfig.isBoolean(intervalConfig)) {
                intervalBooleanMap.put(intervalConfig, mainConfig.getBoolean(intervalConfig));
            } else if (mainConfig.isLong(intervalConfig) || mainConfig.isInt(intervalConfig)) {
                intervalLongMap.put(intervalConfig, mainConfig.getLong(intervalConfig));
            } else if (mainConfig.isDouble(intervalConfig)) {
                intervalDoubleMap.put(intervalConfig, mainConfig.getDouble(intervalConfig));
            }
        }

        loadConfig();
    }
}