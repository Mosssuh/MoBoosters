package mv.mossuh.moboosters.MODULES;

import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.UUID;

public interface IslandBoosterModule {

    BoosterType type = BoosterType.SUPERIORSKYBLOCK2;

    void onIslandDisband(UUID islandUUID);
    void onIslandQuit(UUID islandUUID, UUID playerUUID);
    void onIslandKick(UUID islandUUID, UUID playerUUID);
    void onIslandJoin(UUID islandUUID, UUID playerUUID);

    default BoosterType getType() { return type; }
}

