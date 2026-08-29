package mv.mossuh.moboosters.MODEL.Booster.BoostTypes;

import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

public class PermanentBoost implements Boost{
    private final DurationType durationType = DurationType.PERM;
    private double boost = 0;
    public PermanentBoost(Double boost) {
        if (boost != null) { this.boost = boost; }
    }

    public double getBoost() { return boost; }

    public void addBoost(double boost) {
        if (boost < 0) { boost = 0; }
        this.boost = this.boost + boost;
    }

    public void setBoost(double boost) {
        if (boost < 0) { boost = 0; }
        this.boost = boost;
    }

    public void removeBoost(double boost) {
        if (boost < 0) { boost = 0; }
        double newBoost = this.boost - boost;
        if (newBoost < 0) { newBoost = 0; }
        this.boost = newBoost;
    }

    public boolean isActive() {
        return boost > 0;
    }

    public void cancel() {
        setBoost(0);
    }

    public DurationType getDurationType() {
        return durationType;
    }
}
