package mv.mossuh.moboosters.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.InvalidBooster;

public class IncreaseTimeBoosterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Booster booster = new InvalidBooster();
    private long duration = 0L;
    private boolean cancelled = false;
    public IncreaseTimeBoosterEvent(Booster booster, Long durationInSeconds) {
        if (booster != null) { this.booster = booster; }
        if (durationInSeconds != null) { this.duration = durationInSeconds; }
    }

    public Booster getBooster() { return booster; }
    public long getTime() { return duration; }

    public void setBooster(Booster booster) { this.booster = booster; }
    public void setTime(long durationInSeconds) { this.duration = durationInSeconds; }



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
