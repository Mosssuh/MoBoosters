package mv.mossuh.moboosters.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;

public class ClaimItemBoosterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Player player;
    private BoosterItem boosterItem = new BoosterItem(null, null, null, null, null, null, null);

    public ClaimItemBoosterEvent(Player player, BoosterItem boosterItem) {
        this.player = player;
        if (boosterItem != null) { this.boosterItem = boosterItem; }
    }

    public Player getPlayer() { return player; }
    public BoosterItem getBoosterItem() { return boosterItem; }


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
