package mv.mossuh.moboosters.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;

public class ActivateTemporaryBoosterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Booster booster = new InvalidBooster();
    private TemporaryBoost boost = new TemporaryBoost(null, null);
    private TemporaryBoost oldBoost = new TemporaryBoost(null, null);
    private boolean cancelled = false;
    public ActivateTemporaryBoosterEvent(Booster booster, TemporaryBoost boost, TemporaryBoost oldBoost) {
        if (booster !=  null) { this.booster = booster; }
        if (boost != null) { this.boost = boost; }
        if (oldBoost != null) { this.oldBoost = oldBoost; }
    }

    public Booster getBooster() { return booster; }
    public void setBooster(Booster booster) { this.booster = booster; }

    public TemporaryBoost getBoost() { return boost; }
    public void setBoost(TemporaryBoost boost) { this.boost = boost; }

    public TemporaryBoost getOldBoost() { return oldBoost; }

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
