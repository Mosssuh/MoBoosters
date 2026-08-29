package mv.mossuh.moboosters.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

public class RemoveBoostBoosterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private DurationType durationType = DurationType.NONE;
    private Booster booster = new InvalidBooster();
    private double boost = 0;
    private boolean cancelled = false;
    public RemoveBoostBoosterEvent(DurationType durationType, Booster booster, Double boost) {
        if (durationType != null) { this.durationType = durationType; }
        if (booster != null) { this.booster = booster; }
        if (boost != null) { this.boost = boost; }
    }

    public DurationType getDurationType() { return durationType; }
    public Booster getBooster() { return booster; }
    public double getBoost() { return boost; }

    public void setBooster(Booster booster) { this.booster = booster; }
    public void setBoost(double boost) { this.boost = boost; }



    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
