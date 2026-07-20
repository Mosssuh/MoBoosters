package mv.mossuh.moboosters.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.PermanentBoost;

public class ActivatePermanentBoosterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Booster booster = new InvalidBooster();
    private PermanentBoost boost = new PermanentBoost(null);
    private PermanentBoost oldBoost = new PermanentBoost(null);
    private boolean cancelled = false;
    public ActivatePermanentBoosterEvent(Booster booster, PermanentBoost boost, PermanentBoost oldBoost) {
        if (booster != null) { this.booster = booster; }
        if (boost != null) { this.boost = boost; }
        if (oldBoost != null) { this.oldBoost = oldBoost; }
    }

    public Booster getBooster() { return booster; }
    public void setBooster(Booster booster) { this.booster = booster; }

    public PermanentBoost getBoost() { return boost; }
    public void setBoost(PermanentBoost boost) { this.boost = boost; }

    public PermanentBoost getOldBoost() { return oldBoost; }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
