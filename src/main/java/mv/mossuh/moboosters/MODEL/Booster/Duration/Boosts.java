package mv.mossuh.moboosters.MODEL.Booster.Duration;

import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.InvalidBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

public class Boosts {

    private PermanentBoost permanent = new PermanentBoost(null);
    private TemporaryBoost temporary = new TemporaryBoost(null, new BoosterDuration(null));

    public Boosts(PermanentBoost permanent, TemporaryBoost temporary) {
        if (permanent != null) { this.permanent = permanent; }
        if (temporary !=  null) { this.temporary = temporary; }
    }

    public Boost getBoost(DurationType durationType) {
        if (durationType.equals(DurationType.TEMP)) {
            return temporary;
        } else if (durationType.equals(DurationType.PERM)) {
            return permanent;
        }
        return new InvalidBoost();
    }
    public PermanentBoost getPermanent() { return permanent; }
    public TemporaryBoost getTemporary() { return temporary; }

    public double getTotalBoost() {
        return permanent.getBoost() + temporary.getBoost();
    }

    public void setPermanent(PermanentBoost boost) {
        if (boost != null) {
            this.permanent = boost;
        }
    }
    public void setTemporary(TemporaryBoost boost) {
        if (boost != null) {
            this.temporary = boost;
        }
    }

    public void setBoost(Boost boost) {
        if (boost != null) {
            DurationType durationType = boost.getDurationType();

            if (durationType.equals(DurationType.TEMP)) {
                this.temporary = (TemporaryBoost) boost;
            } else if (durationType.equals(DurationType.PERM)) {
                this.permanent = (PermanentBoost) boost;
            }
        }
    }
}
