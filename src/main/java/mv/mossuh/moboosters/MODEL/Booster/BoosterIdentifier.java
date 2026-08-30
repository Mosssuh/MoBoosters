package mv.mossuh.moboosters.MODEL.Booster;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.DATA.Config.Config.Config;

import java.util.Objects;

public class BoosterIdentifier {

    private String identifier = "";
    private BoosterType boosterType = BoosterType.NONE;
    private ApplicatorType applicatorType = ApplicatorType.NONE;
    private String boosted = "";
    public BoosterIdentifier(String identifier, BoosterType boosterType, ApplicatorType applicatorType, String boosted) {
        if (identifier != null && !identifier.isEmpty()) { this.identifier = identifier.toLowerCase(); }
        if (boosterType != null) { this.boosterType = boosterType; }
        if (applicatorType != null) { this.applicatorType = applicatorType; }
        if (boosted != null) { this.boosted = boosted.toLowerCase(); }
    }
    public BoosterIdentifier() {}

    public String getIdentifier() { return  identifier; }
    public BoosterType getBoosterType() { return boosterType; }
    public ApplicatorType getApplicatorType() { return applicatorType; }
    public String getBoosted() { return boosted; }

    public boolean isIdentifier() {
        return Config.IDENTIFIERS.hasIdentifier(identifier) && boosterType != BoosterType.NONE && !Objects.equals(applicatorType, ApplicatorType.NONE) && !boosted.isEmpty();
    }

    public boolean equalsIgnoreIdentifier(BoosterIdentifier boosterIdentifier) {
        BoosterType bt = boosterIdentifier.getBoosterType();
        ApplicatorType at = boosterIdentifier.getApplicatorType();
        String b = boosterIdentifier.getBoosted();
        return bt.equals(boosterType) && at.equals(applicatorType) && b.equals(boosted);
    }

    public boolean equals(BoosterIdentifier boosterIdentifier) {
        String i = boosterIdentifier.getIdentifier();
        BoosterType bt = boosterIdentifier.getBoosterType();
        ApplicatorType at = boosterIdentifier.getApplicatorType();
        String b = boosterIdentifier.getBoosted();
        return i.equals(identifier) && bt.equals(boosterType) && at.equals(applicatorType) && b.equals(boosted);
    }

    public String toKey() {
        return identifier+"::"+boosterType.name()+"::"+applicatorType+"::"+boosted;
    }
}
