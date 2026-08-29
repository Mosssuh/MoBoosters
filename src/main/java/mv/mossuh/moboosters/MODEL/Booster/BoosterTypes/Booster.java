package mv.mossuh.moboosters.MODEL.Booster.BoosterTypes;

import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;

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
