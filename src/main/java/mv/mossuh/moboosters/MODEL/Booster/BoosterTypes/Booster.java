package mv.mossuh.moboosters.MODEL.Booster.BoosterTypes;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;

import java.util.UUID;

public interface Booster {
    BoosterType getBoosterType();
    BoosterIdentifier getIdentifier();
    boolean equalsIgnoreIdentifier(Booster booster);
    boolean equals(Booster booster);
    boolean isValid();

    // For booster has UUID
    UUID getUUID();

    // Key
    String toKey();

    static Booster getBooster(BoosterIdentifier identifier, UUID player) {
        Booster booster = new InvalidBooster();
        BoosterType boosterType = identifier.getBoosterType();
        if (boosterType.equals(BoosterType.PERSONAL)) {
            booster = new PersonalBooster(player, identifier);
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            booster = new GlobalBooster(identifier);
        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
            if (PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
                SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
                if (superiorPlayer.hasIsland()) {
                    booster = new SuperiorSkyblock2Booster(superiorPlayer.getIsland().getUniqueId(), identifier);
                }
            }
        }
        return booster;
    }
}
