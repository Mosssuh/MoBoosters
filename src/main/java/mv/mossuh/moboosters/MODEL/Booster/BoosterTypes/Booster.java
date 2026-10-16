package mv.mossuh.moboosters.MODEL.Booster.BoosterTypes;

import mv.mossuh.moboosters.MANAGER.HooksManager;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
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
        } else if (boosterType.equals(BoosterType.TEAM)) {
            if (PluginsChecker.isPluginEnabled(BoosterType.TEAM.getHook())) {
                UUID teamUUID = HooksManager.getTeamBoosterHook().getTeamByPlayer(player);
                booster = new TeamBooster(teamUUID, identifier);
            }
        }
        return booster;
    }
}
