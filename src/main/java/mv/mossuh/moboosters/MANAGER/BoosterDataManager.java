package mv.mossuh.moboosters.MANAGER;

import mv.mossuh.moboosters.DATA.Database.BoosterData;
import mv.mossuh.moboosters.DATA.Database.GlobalData;
import mv.mossuh.moboosters.DATA.Database.PersonalData;
import mv.mossuh.moboosters.DATA.Database.TeamData;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.TeamBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoosterDataManager {

    private static PersonalData personalData = new PersonalData();
    private static GlobalData globalData = new GlobalData();

    // NUEVO: Inicializar con DatabaseConnection en vez de ConfigManager
    public static void initialize() {
        personalData = new PersonalData();
        globalData = new GlobalData();
    }

    public static void registerDatabaseInMaps(BoosterType boosterType) {
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.registerDatabaseInMaps();
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.registerDatabaseInMaps();
        } else if (boosterType.equals(BoosterType.TEAM)) {
            HooksManager.getTeamBoosterHook().getData().registerDatabaseInMaps();
        }
    }

    public static void registerMapsInDatabase(BoosterType boosterType) {
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.registerMapsInDatabase();
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.registerMapsInDatabase();
        } else if (boosterType.equals(BoosterType.TEAM)) {
            HooksManager.getTeamBoosterHook().getData().registerMapsInDatabase();
        }
    }

    public static void registerActiveBooster(Booster booster, Boost boost) {
        BoosterType boosterType = booster.getBoosterType();
        if (boosterType.equals(BoosterType.PERSONAL)) {
            personalData.saveActiveBooster((PersonalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            globalData.saveActiveBooster((GlobalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.TEAM)) {
            HooksManager.getTeamBoosterHook().getData().saveActiveBooster((TeamBooster) booster, boost);
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
        List<TeamBooster> teamBoosters = new ArrayList<>();

        for (Booster booster : boosters) {
            BoosterType boosterType = booster.getBoosterType();
            if (boosterType.equals(BoosterType.PERSONAL)) {
                pBoosters.add((PersonalBooster) booster);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                gBoosters.add((GlobalBooster) booster);
            } else if (boosterType.equals(BoosterType.TEAM)) {
                teamBoosters.add((TeamBooster) booster);
            }
        }

        if (!pBoosters.isEmpty()) {
            personalData.removeActiveBooster(durationType, pBoosters);
        }
        if (!gBoosters.isEmpty()) {
            globalData.removeActiveBooster(durationType, gBoosters);
        }
        if (!teamBoosters.isEmpty()) {
            HooksManager.getTeamBoosterHook().getData().removeActiveBooster(durationType, teamBoosters);
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
        List<TeamBooster> teamBoosters = new ArrayList<>();

        for (Booster booster : boosters) {
            BoosterType boosterType = booster.getBoosterType();
            if (boosterType.equals(BoosterType.PERSONAL)) {
                pBoosters.add((PersonalBooster) booster);
            } else if (boosterType.equals(BoosterType.GLOBAL)) {
                gBoosters.add((GlobalBooster) booster);
            } else if (boosterType.equals(BoosterType.TEAM)) {
                teamBoosters.add((TeamBooster) booster);
            }
        }

        if (!pBoosters.isEmpty()) {
            personalData.removeActiveBooster(pBoosters);
        }
        if (!gBoosters.isEmpty()) {
            globalData.removeActiveBooster(gBoosters);
        }
        if (!teamBoosters.isEmpty()) {
            HooksManager.getTeamBoosterHook().getData().removeActiveBooster(teamBoosters);
        }
    }
}
