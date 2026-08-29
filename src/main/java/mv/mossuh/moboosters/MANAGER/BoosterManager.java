package mv.mossuh.moboosters.MANAGER;

import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.Boost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import org.bukkit.Bukkit;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.Duration.BoosterDuration;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.API.Events.*;

import mv.mossuh.moboosters.UTILITIES.Cooldown.BoosterCooldown;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.Enums.DurationType;

import java.util.*;

public class BoosterManager {

    public ActiveBooster setTempBoost(Booster booster, Double boost, Long duration) {
        // DurationType durationType = DurationType.TEMP;
        TemporaryBoost startedBoost = new TemporaryBoost(boost, new BoosterDuration(duration));

        ActiveBooster oldBooster = ActiveBoosterManager.getActiveBooster(booster);
        TemporaryBoost oldBoost = oldBooster.getBoosts().getTemporary();

        ActivateTemporaryBoosterEvent startsEvent = new ActivateTemporaryBoosterEvent(booster, startedBoost, oldBoost);
        Bukkit.getPluginManager().callEvent(startsEvent);

        if (startsEvent.isCancelled()) { return new ActiveBooster(); }

        Booster newBooster = startsEvent.getBooster();
        TemporaryBoost newStartedBoost = startsEvent.getBoost();

        ActiveBoosterManager.createActiveBooster(newBooster, newStartedBoost);
        ActiveBooster activeBooster = ActiveBoosterManager.getActiveBooster(newBooster);
        BoosterCooldown.startCooldown(activeBooster);

        // BoosterDataManager.registerActiveBooster(durationType, newBooster, newStartedBoost);
        return activeBooster;
    }

    public ActiveBooster setPermBoost(Booster booster, Double boost) {
        PermanentBoost startedBoost = new PermanentBoost(boost);

        ActiveBooster oldBooster = ActiveBoosterManager.getActiveBooster(booster);
        PermanentBoost oldBoost = oldBooster.getBoosts().getPermanent();

        ActivatePermanentBoosterEvent startsEvent = new ActivatePermanentBoosterEvent(booster, startedBoost, oldBoost);
        Bukkit.getPluginManager().callEvent(startsEvent);

        if (startsEvent.isCancelled()) { return new ActiveBooster(); }

        Booster newBooster = startsEvent.getBooster();
        PermanentBoost newStartedBoost = startsEvent.getBoost();

        return ActiveBoosterManager.createActiveBooster(newBooster, newStartedBoost);
    }

    public ActiveBooster addBoost(DurationType durationType, Booster booster, Double boost) {
        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
        BoosterType boosterType = boosterIdentifier.getBoosterType();

        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(boosterType);
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (booster.equals(aBooster)) {
                    IncreaseBoostBoosterEvent increaseEvent = new IncreaseBoostBoosterEvent(durationType, aBooster, boost);
                    Bukkit.getPluginManager().callEvent(increaseEvent);

                    if (increaseEvent.isCancelled()) { return new ActiveBooster(); }

                    ab.getBoosts().getBoost(durationType).addBoost(increaseEvent.getBoost());
                    return ab;
                }
            }
        }
        return new ActiveBooster();
    }

    public ActiveBooster removeBoost(DurationType durationType, Booster booster, Double boost) {
        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
        BoosterType boosterType = boosterIdentifier.getBoosterType();

        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(boosterType);
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (booster.equals(ab.getBooster())) {
                    RemoveBoostBoosterEvent removeEvent = new RemoveBoostBoosterEvent(durationType, aBooster, boost);
                    Bukkit.getPluginManager().callEvent(removeEvent);

                    if (removeEvent.isCancelled()) { return new ActiveBooster(); }

                    Boost aBoost = ab.getBoosts().getBoost(durationType);
                    aBoost.removeBoost(removeEvent.getBoost());

                    if (!aBoost.isActive() && aBoost.getDurationType().equals(DurationType.TEMP)) {
                        BoosterCooldown.cancelCooldown(aBooster, (TemporaryBoost) aBoost);
                    }
                    return ab;
                }
            }
        }
        return new ActiveBooster();
    }


    public ActiveBooster addTime(Booster booster, Long durationInSeconds) {
        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
        BoosterType boosterType = boosterIdentifier.getBoosterType();

        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(boosterType);
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (booster.equals(aBooster)) {
                    IncreaseTimeBoosterEvent increaseEvent = new IncreaseTimeBoosterEvent(aBooster, durationInSeconds);
                    Bukkit.getPluginManager().callEvent(increaseEvent);

                    if (increaseEvent.isCancelled()) {
                        return new ActiveBooster();
                    }

                    ab.getBoosts().getTemporary().getDuration().addDuration(increaseEvent.getTime());
                    BoosterCooldown.startCooldown(ab);
                    return ab;
                }
            }
        }
        return new ActiveBooster();
    }

    public ActiveBooster removeTime(Booster booster, Long durationInSeconds) {
        BoosterIdentifier boosterIdentifier = booster.getIdentifier();
        BoosterType boosterType = boosterIdentifier.getBoosterType();

        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(boosterType);
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (booster.equals(aBooster)) {
                    RemoveTimeBoosterEvent increaseEvent = new RemoveTimeBoosterEvent(aBooster, durationInSeconds);
                    Bukkit.getPluginManager().callEvent(increaseEvent);

                    if (increaseEvent.isCancelled()) {
                        return new ActiveBooster();
                    }

                    TemporaryBoost aBoost = ab.getBoosts().getTemporary();
                    ab.getBoosts().getTemporary().getDuration().removeDuration(increaseEvent.getTime());

                    if (aBoost.isActive()) {
                        BoosterCooldown.startCooldown(ab);
                    } else {
                        BoosterCooldown.cancelCooldown(aBooster, aBoost);
                    }
                    return ab;
                }
            }
        }
        return new ActiveBooster();
    }

    public void removeBooster(DurationType durationType, Booster booster) {
        if (booster == null) return;

        ActiveBooster active = ActiveBoosterManager.removeActiveBooster(durationType, booster);
        if (durationType.equals(DurationType.TEMP)) {
            if (active.isActive(DurationType.TEMP)) {
                EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, active.getBoosts().getTemporary());
                Bukkit.getPluginManager().callEvent(tempEvent);
            }
            BoosterCooldown.removeCooldown(booster);
        } else {
            if (active.isActive(DurationType.PERM)) {
                RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, booster, active.getBoosts().getPermanent().getBoost());
                Bukkit.getPluginManager().callEvent(permEvent);
            }
        }
    }

    public static void removeBooster(String identifier, BoosterType boosterType, DurationType durationType, UUID uuid) {
        boolean hasUUID = boosterType.hasUUID();
        for (ActiveBooster ab : ActiveBoosterManager.getActiveBoosters(boosterType)) {
            Booster booster = ab.getBooster();
            BoosterIdentifier bi = booster.getIdentifier();
            if (hasUUID) {
                if (booster.getUUID().equals(uuid) && bi.getIdentifier().equals(identifier) && ab.isActive(durationType)) {
                    switch (durationType) {
                        case TEMP:
                            BoosterCooldown.removeCooldown(booster);

                            EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, ab.getBoosts().getTemporary());
                            Bukkit.getPluginManager().callEvent(tempEvent);
                            break;
                        case PERM:
                            RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(durationType, booster, ab.getBoosts().getPermanent().getBoost());
                            Bukkit.getPluginManager().callEvent(permEvent);
                            break;
                    }
                    ab.cancel(durationType);
                }
            } else {
                if (bi.getIdentifier().equals(identifier) && ab.isActive(durationType)) {
                    switch (durationType) {
                        case TEMP:
                            BoosterCooldown.removeCooldown(booster);

                            EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, ab.getBoosts().getTemporary());
                            Bukkit.getPluginManager().callEvent(tempEvent);
                            break;
                        case PERM:
                            RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(durationType, booster, ab.getBoosts().getPermanent().getBoost());
                            Bukkit.getPluginManager().callEvent(permEvent);
                            break;
                    }
                    ab.cancel(durationType);
                }
            }
        }
    }

    public static void removeBooster(String identifier, BoosterType boosterType, UUID uuid) {
        List<Booster> removed = new ArrayList<>();

        boolean hasUUID = boosterType.hasUUID();
        Iterator<ActiveBooster> iterator = ActiveBoosterManager.getActiveBoosters(boosterType).iterator();
        while (iterator.hasNext()) {
            ActiveBooster ab = iterator.next();
            Booster booster = ab.getBooster();
            BoosterIdentifier bi = booster.getIdentifier();
            if (hasUUID) {
                if (booster.getUUID().equals(uuid) && bi.getIdentifier().equals(identifier))  {
                    if (ab.isActive(DurationType.TEMP)) {
                        BoosterCooldown.removeCooldown(booster);

                        EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, ab.getBoosts().getTemporary());
                        Bukkit.getPluginManager().callEvent(tempEvent);
                    }

                    if (ab.isActive(DurationType.PERM)) {
                        RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, booster, ab.getBoosts().getPermanent().getBoost());
                        Bukkit.getPluginManager().callEvent(permEvent);
                    }
                    removed.add(booster);
                    iterator.remove();
                }
            } else {
                if (bi.getIdentifier().equals(identifier))  {
                    if (ab.isActive(DurationType.TEMP)) {
                        BoosterCooldown.removeCooldown(booster);

                        EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, ab.getBoosts().getTemporary());
                        Bukkit.getPluginManager().callEvent(tempEvent);
                    }

                    if (ab.isActive(DurationType.PERM)) {
                        RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, booster, ab.getBoosts().getPermanent().getBoost());
                        Bukkit.getPluginManager().callEvent(permEvent);
                    }

                    removed.add(booster);
                    iterator.remove();
                }
            }
        }
    }

    public void removeBooster(Booster booster) {
        ActiveBooster active = ActiveBoosterManager.removeActiveBooster(booster);

        if (active.isActive(DurationType.TEMP)) {
            EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, active.getBoosts().getTemporary());
            Bukkit.getPluginManager().callEvent(tempEvent);
        }

        if (active.isActive(DurationType.PERM)) {
            RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, booster, active.getBoosts().getPermanent().getBoost());
            Bukkit.getPluginManager().callEvent(permEvent);
        }

        BoosterCooldown.removeCooldown(booster);
    }

    public void removeBooster(Collection<Booster> boosters) {
        if (boosters == null || boosters.isEmpty()) return;
        for (Booster booster : boosters) {
            ActiveBooster active = ActiveBoosterManager.removeActiveBooster(booster);
            if (active.isActive(DurationType.TEMP)) {
                EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(booster, active.getBoosts().getTemporary());
                Bukkit.getPluginManager().callEvent(tempEvent);
            }
            if (active.isActive(DurationType.PERM)) {
                RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, booster, active.getBoosts().getPermanent().getBoost());
                Bukkit.getPluginManager().callEvent(permEvent);
            }
            BoosterCooldown.removeCooldown(booster);
        }
        // BoosterDataManager.removeActiveBooster(boosters);
    }

    public double getBoost(Booster booster, boolean ignoreIdentifier) {
        return ActiveBoosterManager.getBoost(booster, ignoreIdentifier);
    }
    public double getBoost(DurationType durationType, Booster booster, boolean ignoreIdentifier) {
        return ActiveBoosterManager.getBoost(durationType, booster, ignoreIdentifier);
    }

    public List<ActiveBooster> getBoosters(BoosterType boosterType) {
        return ActiveBoosterManager.getActiveBoosters(boosterType);
    }
    public ActiveBooster getBooster(Booster booster) {
        return ActiveBoosterManager.getActiveBooster(booster);
    }
}
