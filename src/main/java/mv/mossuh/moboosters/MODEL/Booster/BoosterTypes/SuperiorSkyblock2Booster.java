package mv.mossuh.moboosters.MODEL.Booster.BoosterTypes;

import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;

import java.util.UUID;

public class SuperiorSkyblock2Booster implements Booster {
    private UUID uuid = null;
    private BoosterIdentifier identifier = new BoosterIdentifier(null, null, null, null);
    private BoosterType boosterType = BoosterType.NONE;
    public SuperiorSkyblock2Booster(UUID uuid, BoosterIdentifier identifier) {
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
            if (bType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                SuperiorSkyblock2Booster comparedBooster = (SuperiorSkyblock2Booster) booster;
                return comparedBooster.getUUID().equals(uuid);
            }
        }
        return false;
    }

    public boolean equals(Booster booster) {
        BoosterType bType = booster.getBoosterType();
        if (boosterType.equals(booster.getBoosterType()) && identifier.equals(booster.getIdentifier())) {
            if (bType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
                SuperiorSkyblock2Booster comparedBooster = (SuperiorSkyblock2Booster) booster;
                return comparedBooster.getUUID().equals(uuid);
            }
        }
        return false;
    }


    public String toKey() {
        return uuid+"::"+identifier.toKey();
    }
}
