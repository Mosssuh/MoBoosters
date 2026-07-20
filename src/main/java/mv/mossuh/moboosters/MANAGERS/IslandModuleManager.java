package mv.mossuh.moboosters.MANAGERS;

import mv.mossuh.moboosters.MODULES.IslandBoosterModule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandModuleManager {
    private static final List<IslandBoosterModule> modules = new ArrayList<>();

    public static void registerModule(IslandBoosterModule module) {
        modules.add(module);
    }

    public static void onIslandDisband(UUID islandUUID) {
        for (IslandBoosterModule module : modules) {
            module.onIslandDisband(islandUUID);
        }
    }

    public static void onIslandQuit(UUID islandUUID, UUID playerUUID) {
        for (IslandBoosterModule module : modules) {
            module.onIslandQuit(islandUUID, playerUUID);
        }
    }

    public static void onIslandKick(UUID islandUUID, UUID playerUUID) {
        for (IslandBoosterModule module : modules) {
            module.onIslandKick(islandUUID, playerUUID);
        }
    }

    public static void onIslandJoin(UUID islandUUID, UUID playerUUID) {
        for (IslandBoosterModule module : modules) {
            module.onIslandJoin(islandUUID, playerUUID);
        }
    }
}
