package mv.mossuh.moboosters.BOOSTERS.BoosterTypes;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.UUID;

public class PersonalBooster implements Booster {
    private UUID uuid = null;
    private BoosterIdentifier identifier = new BoosterIdentifier(null, null, null, null);
    private BoosterType boosterType = BoosterType.NONE;
    public PersonalBooster(UUID uuid, BoosterIdentifier identifier) {
        this.uuid = uuid;
        if (identifier != null) {
            this.identifier = identifier;
            this.boosterType = identifier.getBoosterType();
        }
    }

    public boolean isValid() {
        return uuid != null && identifier.isIdentifier();
    }
    public UUID getUUID() { return uuid; }
    public BoosterType getBoosterType() { return boosterType; }
    public BoosterIdentifier getIdentifier() { return identifier; }

    public boolean equalsIgnoreIdentifier(Booster booster) {
        BoosterType bType = booster.getBoosterType();
        if (boosterType.equals(booster.getBoosterType()) && identifier.equalsIgnoreIdentifier(booster.getIdentifier())) {
            if (bType.equals(BoosterType.PERSONAL)) {
                PersonalBooster comparedBooster = (PersonalBooster) booster;
                return comparedBooster.getUUID().equals(uuid);
            }
        }
        return false;
    }

    public boolean equals(Booster booster) {
        BoosterType bType = booster.getBoosterType();
        if (boosterType.equals(booster.getBoosterType()) && identifier.equals(booster.getIdentifier())) {
            if (bType.equals(BoosterType.PERSONAL)) {
                PersonalBooster comparedBooster = (PersonalBooster) booster;
                return comparedBooster.getUUID().equals(uuid);
            }
        }
        return false;
    }

    public String toKey() {
        return uuid+"::"+identifier.toKey();
    }
}
