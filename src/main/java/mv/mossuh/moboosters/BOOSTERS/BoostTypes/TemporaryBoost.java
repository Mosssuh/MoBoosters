package mv.mossuh.moboosters.BOOSTERS.BoostTypes;

import mv.mossuh.moboosters.BOOSTERS.Duration.BoosterDuration;
import mv.mossuh.moboosters.ENUMS.DurationType;

public class TemporaryBoost implements Boost {
    private final DurationType durationType = DurationType.TEMP;
    private double boost = 0;
    private BoosterDuration duration = new BoosterDuration(null);
    public TemporaryBoost(Double boost, BoosterDuration duration) {
        if (boost != null) { this.boost = boost; }
        if (duration != null) { this.duration = duration; }
    }

    public double getBoost() { return boost; }
    public BoosterDuration getDuration() { return duration; }

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
        return duration.getRemainingTime() > 0 && boost > 0;
    }

    public void cancel() {
        setBoost(0);
        duration.cancel();
    }

    public DurationType getDurationType() {
        return durationType;
    }
}
