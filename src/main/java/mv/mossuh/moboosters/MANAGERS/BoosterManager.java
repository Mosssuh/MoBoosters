package mv.mossuh.moboosters.MANAGERS;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoosterDuration;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfigs;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.API.Events.*;

import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItem;
import mv.mossuh.moboosters.BOOSTERS.Items.BoosterItems;
import mv.mossuh.moboosters.COOLDOWN.BoosterCooldown;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

        ActiveBoosterManager.setActiveBooster(newBooster, newStartedBoost);
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

        return ActiveBoosterManager.setActiveBooster(newBooster, newStartedBoost);
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
        ActiveBooster active = ActiveBoosterManager.removeActiveBooster(durationType, booster);
        // BoosterDataManager.removeActiveBooster(durationType, booster);
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
    public void removeBooster(DurationType durationType, List<Booster> boosters) {
        for (Booster booster : boosters) {
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
        // BoosterDataManager.removeActiveBooster(durationType, boosters);
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

        /*
        if (!removed.isEmpty()) {
            BoosterDataManager.removeActiveBooster(removed);
        }

         */
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
        // BoosterDataManager.removeActiveBooster(booster);
    }

    public void removeBooster(List<Booster> boosters) {
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
        return ActiveBoosterManager.getBoosts(booster, ignoreIdentifier);
    }
    public double getBoost(DurationType durationType, Booster booster, boolean ignoreIdentifier) {
        return ActiveBoosterManager.getBoosts(durationType, booster, ignoreIdentifier);
    }

    public List<ActiveBooster> getBoosters(BoosterType boosterType) {
        return ActiveBoosterManager.getActiveBoosters(boosterType);
    }
    public ActiveBooster getBooster(Booster booster) {
        return ActiveBoosterManager.getActiveBooster(booster);
    }

    public static List<ActiveBooster> getBoosters(BoosterType boosterType, ApplicatorType applicatorType) {
        List<ActiveBooster> selected = new ArrayList<>();
        List<ActiveBooster> boosters = ActiveBoosterManager.getActiveBoosters();
        for (ActiveBooster booster : boosters) {
            BoosterIdentifier identifier = booster.getBooster().getIdentifier();
            if (identifier.getBoosterType().equals(boosterType) && identifier.getApplicatorType().equals(applicatorType)) {
                selected.add(booster);
            }
        }
        return selected;
    }

    public static void removeIslandBoosters(UUID islandUUID, ApplicatorType applicatorType) {
        List<Booster> removed = new ArrayList<>();
        List<ActiveBooster> boosters = ActiveBoosterManager.getActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
        for (ActiveBooster booster : boosters) {
            SuperiorSkyblock2Booster b = (SuperiorSkyblock2Booster) booster.getBooster();
            BoosterIdentifier identifier = booster.getBooster().getIdentifier();
            if (b.getUUID().equals(islandUUID) && identifier.getApplicatorType().equals(applicatorType)) {
                ActiveBoosterManager.removeActiveBooster(b);
                if (booster.isActive(DurationType.TEMP)) {
                    EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(b, booster.getBoosts().getTemporary());
                    Bukkit.getPluginManager().callEvent(tempEvent);
                }
                if (booster.isActive(DurationType.PERM)) {
                    RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, b, booster.getBoosts().getPermanent().getBoost());
                    Bukkit.getPluginManager().callEvent(permEvent);
                }
                removed.add(b);
            }
        }
        // BoosterDataManager.removeActiveBooster(removed);
    }

    public static void removeIslandBoosters(UUID islandUUID, ApplicatorType applicatorType, String boosted) {
        List<Booster> removed = new ArrayList<>();
        List<ActiveBooster> boosters = ActiveBoosterManager.getActiveBoosters(BoosterType.SUPERIORSKYBLOCK2);
        for (ActiveBooster booster : boosters) {
            SuperiorSkyblock2Booster b = (SuperiorSkyblock2Booster) booster.getBooster();
            BoosterIdentifier identifier = booster.getBooster().getIdentifier();
            if (b.getUUID().equals(islandUUID) && identifier.getApplicatorType().equals(applicatorType) && identifier.getBoosted().equals(boosted)) {
                ActiveBoosterManager.removeActiveBooster(b);
                if (booster.isActive(DurationType.TEMP)) {
                    EndsTemporaryBoosterEvent tempEvent = new EndsTemporaryBoosterEvent(b, booster.getBoosts().getTemporary());
                    Bukkit.getPluginManager().callEvent(tempEvent);
                }
                if (booster.isActive(DurationType.PERM)) {
                    RemoveBoostBoosterEvent permEvent = new RemoveBoostBoosterEvent(DurationType.PERM, b, booster.getBoosts().getPermanent().getBoost());
                    Bukkit.getPluginManager().callEvent(permEvent);
                }
                removed.add(b);
            }
        }
        // BoosterDataManager.removeActiveBooster(removed);
    }



    public List<BoosterConfig> getBoosterConfigs() {
        return BoosterConfigs.getBoosterConfigs();
    }
    public BoosterConfig getBoosterConfig(String code) {
        return BoosterConfigs.getBoosterConfig(code);
    }
    public BoosterItem getBoosterItem(ItemStack itemStack) {
        return BoosterItems.get(itemStack);
    }
}
