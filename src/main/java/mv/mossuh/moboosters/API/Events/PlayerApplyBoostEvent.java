package mv.mossuh.moboosters.API.Events;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerApplyBoostEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private UUID uuid;
    private double boost = 0;
    private ApplicatorType applicatorType = ApplicatorType.NONE;
    private String boosted = "";
    private boolean cancelled = false;

    public PlayerApplyBoostEvent(UUID uuid, Double boost, ApplicatorType applicatorType, String boosted) {
        this.uuid = uuid;
        if (boost != null) { this.boost = boost; }
        if (applicatorType != null) { this.applicatorType = applicatorType; }
        if (boosted != null) { this.boosted = boosted; }
    }

    public UUID getUUID() { return uuid; }

    public double getBoost() {
        return boost;
    }

    public void setBoost(Double boost) {
        if (boost != null) {
            if (boost < 0) return;
            this.boost = boost;
        }
    }

    public void addBoost(Double boost) {
        if (boost != null) {
            this.boost += boost;
        }
    }


    public ApplicatorType getApplicatorType() { return applicatorType; }
    public String getBoosted() { return boosted; }


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
