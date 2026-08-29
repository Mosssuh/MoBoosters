package mv.mossuh.moboosters.MODEL.Booster.BoosterTypes;

import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;

import java.util.UUID;

public class GlobalBooster implements Booster {
    private BoosterIdentifier identifier = new BoosterIdentifier(null, null, null, null);
    private BoosterType boosterType = BoosterType.NONE;
    public GlobalBooster(BoosterIdentifier identifier) {
        if (identifier != null) {
            this.identifier = identifier;
            this.boosterType = identifier.getBoosterType();
        }
    }

    public boolean isValid() {
        return identifier.isIdentifier();
    }

    public UUID getUUID() { return null; };

    public BoosterType getBoosterType() { return boosterType; }
    public BoosterIdentifier getIdentifier() { return identifier; }

    public boolean equalsIgnoreIdentifier(Booster booster) {
        return boosterType.equals(booster.getBoosterType()) && identifier.equalsIgnoreIdentifier(booster.getIdentifier());
    }

    public boolean equals(Booster booster) {
        return boosterType.equals(booster.getBoosterType()) && identifier.equals(booster.getIdentifier());
    }

    public String toKey() {
        return identifier.toKey();
    }
}
