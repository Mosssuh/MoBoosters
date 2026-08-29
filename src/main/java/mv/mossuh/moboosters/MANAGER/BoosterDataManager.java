package mv.mossuh.moboosters.MANAGER;

import mv.mossuh.moboosters.DATA.Database.GlobalData;
import mv.mossuh.moboosters.DATA.Database.PersonalData;
import mv.mossuh.moboosters.DATA.Database.SuperiorSkyblock2Data;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoosterDataManager {

    private static PersonalData personalData;
    private static GlobalData globalData;
    private static SuperiorSkyblock2Data superiorSkyblock2Data;

    // NUEVO: Inicializar con DatabaseConnection en vez de ConfigManager
    public static void initialize() {
        personalData = new PersonalData();
        globalData = new GlobalData();
        superiorSkyblock2Data = new SuperiorSkyblock2Data();
    }

    public static void registerDatabaseInMaps(BoosterType boosterType) {
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.registerDatabaseInMaps();
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.registerDatabaseInMaps();
        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
            superiorSkyblock2Data.registerDatabaseInMaps();
        }
    }

    public static void registerMapsInDatabase(BoosterType boosterType) {
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.registerMapsInDatabase();
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.registerMapsInDatabase();
        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
            superiorSkyblock2Data.registerMapsInDatabase();
        }
    }

    public static void registerActiveBooster(Booster booster, Boost boost) {
        BoosterType boosterType = booster.getBoosterType();
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.saveActiveBooster((PersonalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.saveActiveBooster((GlobalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
            superiorSkyblock2Data.saveActiveBooster((SuperiorSkyblock2Booster) booster, boost);
        }
    }

    public static void removeActiveBooster(DurationType durationType, Booster booster) {
        removeActiveBoosterMethod(durationType, new ArrayList<>(Collections.singleton(booster)));
    }

    public static void removeActiveBooster(DurationType durationType, List<Booster> boosters) {
        removeActiveBoosterMethod(durationType, boosters);
    }

    private static void removeActiveBoosterMethod(DurationType durationType, List<Booster> boosters) {
        List<PersonalBooster> pBoosters = new ArrayList<>();
        List<GlobalBooster> gBoosters = new ArrayList<>();
        List<SuperiorSkyblock2Booster> ssb2Boosters = new ArrayList<>();

        for (Booster booster : boosters) {
            BoosterType boosterType = booster.getBoosterType();
            if (boosterType.equals(BoosterType.PERSONAL)) {
                pBoosters.add((PersonalBooster) booster);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                gBoosters.add((GlobalBooster) booster);
            } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                ssb2Boosters.add((SuperiorSkyblock2Booster) booster);
            }
        }

        if (!pBoosters.isEmpty()) {
            personalData.removeActiveBooster(durationType, pBoosters);
        }
        if (!gBoosters.isEmpty()) {
            globalData.removeActiveBooster(durationType, gBoosters);
        }
        if (!ssb2Boosters.isEmpty()) {
            superiorSkyblock2Data.removeActiveBooster(durationType, ssb2Boosters);
        }
    }

    public static void removeActiveBooster(List<Booster> boosters) {
        removeActiveBoosterMethod(boosters);
    }

    public static void removeActiveBooster(Booster booster) {
        removeActiveBoosterMethod(new ArrayList<>(Collections.singleton(booster)));
    }

    private static void removeActiveBoosterMethod(List<Booster> boosters) {
        List<PersonalBooster> pBoosters = new ArrayList<>();
        List<GlobalBooster> gBoosters = new ArrayList<>();
        List<SuperiorSkyblock2Booster> ssb2Boosters = new ArrayList<>();

        for (Booster booster : boosters) {
            BoosterType boosterType = booster.getBoosterType();
            if (boosterType.equals(BoosterType.PERSONAL)) {
                pBoosters.add((PersonalBooster) booster);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                gBoosters.add((GlobalBooster) booster);
            } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                ssb2Boosters.add((SuperiorSkyblock2Booster) booster);
            }
        }

        if (!pBoosters.isEmpty()) {
            personalData.removeActiveBooster(pBoosters);
        }
        if (!gBoosters.isEmpty()) {
            globalData.removeActiveBooster(gBoosters);
        }
        if (!ssb2Boosters.isEmpty()) {
            superiorSkyblock2Data.removeActiveBooster(ssb2Boosters);
        }
    }
}
