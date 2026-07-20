package mv.mossuh.moboosters.BOOSTERS;

import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.Boosts;
import mv.mossuh.moboosters.ENUMS.DurationType;

public class ActiveBooster {
    private Booster booster = new InvalidBooster();
    private Boosts boosts = new Boosts(null, null);

    public ActiveBooster() {}

    public ActiveBooster(Booster booster, Boosts boosts) {
        if (booster != null) { this.booster = booster; }
        if (boosts != null) { this.boosts = boosts; }
    }

    public boolean isActive() {
        if (booster.getIdentifier().isIdentifier()) {
            return boosts.getPermanent().isActive() || boosts.getTemporary().isActive();
        }
        return false;
    }

    public boolean isActive(DurationType durationType) {
        if (booster.getIdentifier().isIdentifier()) {
            if (durationType.equals(DurationType.TEMP)) {
                return boosts.getTemporary().isActive();
            } else if (durationType.equals(DurationType.PERM)) {
                return boosts.getPermanent().isActive();
            }
        }
        return false;
    }

    public void cancel(DurationType durationType) {
        if (booster.getIdentifier().isIdentifier()) {
            if (durationType.equals(DurationType.TEMP)) {
                boosts.getTemporary().cancel();
            } else if (durationType.equals(DurationType.PERM)) {
                boosts.getPermanent().cancel();
            }
        }
    }

    public boolean isPermActive() {
        if (booster.getIdentifier().isIdentifier()) {
            return boosts.getPermanent().isActive();
        }
        return false;
    }

    public boolean isTempActive() {
        if (booster.getIdentifier().isIdentifier()) {
            return boosts.getTemporary().isActive();
        }
        return false;
    }
    public Boosts getBoosts() { return boosts ;}
    public Booster getBooster() { return booster; }

    public boolean isValid() {
        return booster.isValid();
    }
}
