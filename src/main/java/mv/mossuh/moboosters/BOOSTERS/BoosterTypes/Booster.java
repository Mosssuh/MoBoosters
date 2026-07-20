package mv.mossuh.moboosters.BOOSTERS.BoosterTypes;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.BoosterType;

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
}
