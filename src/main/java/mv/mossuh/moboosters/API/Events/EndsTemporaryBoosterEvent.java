package mv.mossuh.moboosters.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.InvalidBooster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;

public class EndsTemporaryBoosterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Booster booster = new InvalidBooster();
    private TemporaryBoost boost = new TemporaryBoost(null, null);
    public EndsTemporaryBoosterEvent(Booster booster, TemporaryBoost boost) {
        if (booster != null) { this.booster = booster; }
        if (boost != null) { this.boost = boost; }
    }

    public Booster getBooster() { return booster; }
    public TemporaryBoost getBoost() { return boost; }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
