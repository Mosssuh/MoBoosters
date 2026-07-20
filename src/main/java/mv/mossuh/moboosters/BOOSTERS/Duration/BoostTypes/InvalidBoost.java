package mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes;

import mv.mossuh.moboosters.ENUMS.DurationType;

public class InvalidBoost implements Boost {
    private final DurationType durationType = DurationType.NONE;
    private double boost;
    public InvalidBoost() {
        this.boost = 0;
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
        this.boost = 0;
    }
    public DurationType getDurationType() {
        return durationType;
    }
}
