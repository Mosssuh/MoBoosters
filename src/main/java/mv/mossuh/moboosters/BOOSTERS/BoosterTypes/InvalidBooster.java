package mv.mossuh.moboosters.BOOSTERS.BoosterTypes;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.UUID;

public class InvalidBooster implements Booster {
    private final BoosterIdentifier identifier;
    private final BoosterType boosterType;
    public InvalidBooster() {
        this.identifier = new BoosterIdentifier(null, null, null, null);
        this.boosterType = identifier.getBoosterType();
    }

    public boolean isValid() {
        return identifier.isIdentifier();
    }

    public UUID getUUID() { return null; }

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
