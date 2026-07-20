package mv.mossuh.moboosters.CONFIGS.Booster;


import java.util.ArrayList;
import java.util.List;

public class BoosterConfigs {
    private static List<BoosterConfig> boosterConfigList = new ArrayList<>();

    public static List<BoosterConfig> getBoosterConfigs() { return boosterConfigList; }
    public static BoosterConfig getBoosterConfig(String code) {
        for (BoosterConfig boosterConfig : boosterConfigList) {
            if (boosterConfig.getCode().equalsIgnoreCase(code)) {
                return boosterConfig;
            }
        }
        return new BoosterConfig();
    }

    public static boolean exist(String code) {
        for (BoosterConfig boosterConfig : boosterConfigList) {
            if (boosterConfig.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public static void addBoosterConfig(BoosterConfig boosterConfig) {
        boosterConfigList.add(boosterConfig);
    }

    public static void clearBoosters() {
        boosterConfigList.clear();
    }
}
