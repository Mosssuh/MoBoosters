package mv.mossuh.moboosters.MANAGERS;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import mv.mossuh.moboosters.BOOSTERS.ActiveBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.Booster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.Boost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.PermanentBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.BOOSTERS.Duration.Boosts;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.ENUMS.DurationType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActiveBoosterManager {
    private static final Map<BoosterType, List<ActiveBooster>> activeBoostersMap = new ConcurrentHashMap<>();

    // getBoostsCached()
    private static final Map<String, Double> cachedBoosts = new ConcurrentHashMap<>();
    private static final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION = 1000L;

    public static ActiveBooster setActiveBooster(Booster booster, Boost boost) {
        if (booster != null && boost != null) {
            DurationType durationType = boost.getDurationType();
            BoosterType boosterType = booster.getBoosterType();
            List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (aBooster.equals(booster)) {
                    ab.getBoosts().setBoost(boost);
                    return ab;
                }
            }

            ActiveBooster activeBooster = new ActiveBooster();
            if (durationType.equals(DurationType.TEMP)) {
                activeBooster = new ActiveBooster(booster, new Boosts(null, (TemporaryBoost) boost));
                activeBoosters.add(activeBooster);
            } else if (durationType.equals(DurationType.PERM)) {
                activeBooster = new ActiveBooster(booster, new Boosts((PermanentBoost) boost, null));
                activeBoosters.add(activeBooster);
            }
            return activeBooster;
        }
        return new ActiveBooster();
    }

    public static ActiveBooster removeActiveBooster(DurationType durationType, Booster booster) {
        BoosterType boosterType = booster.getBoosterType();
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());

        for (ActiveBooster ab : activeBoosters) {
            Booster aBooster = ab.getBooster();
            if (aBooster.equals(booster)) {
                if (durationType.equals(DurationType.TEMP)) {
                    ab.getBoosts().setTemporary(new TemporaryBoost(null, null));
                } else if (durationType.equals(DurationType.PERM)) {
                    ab.getBoosts().setPermanent(new PermanentBoost(null));
                }
                return ab;
            }
        }
        return new ActiveBooster();
    }

    public static ActiveBooster removeActiveBooster(Booster booster) {
        BoosterType boosterType = booster.getBoosterType();
        Iterator<ActiveBooster> iterator = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>()).iterator();
        while (iterator.hasNext()) {
            ActiveBooster ab = iterator.next();
            Booster aBooster = ab.getBooster();
            if (aBooster.equals(booster)) {
                iterator.remove();
                return ab;
            }
        }
        return new ActiveBooster();
    }

    public static List<ActiveBooster> getActiveBoosters() {
        List<ActiveBooster> boosters = new CopyOnWriteArrayList<>();
        for (List<ActiveBooster> b : activeBoostersMap.values()) {
            boosters.addAll(b);
        }
        return boosters;
    }

    public static List<ActiveBooster> getActiveBoosters(BoosterType boosterType) {
        return activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
    }

    public static ActiveBooster getActiveBooster(Booster booster) {
        BoosterType boosterType = booster.getBoosterType();
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (aBooster.equals(booster)) {
                    return ab;
                }
            }
        }
        return new ActiveBooster(null, null);
    }

    public static boolean hasActiveBooster(Booster booster) {
        BoosterType boosterType = booster.getBoosterType();
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (aBooster.equals(booster)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getBoosts(DurationType durationType, Booster booster, boolean ignoreIdentifier) {
        double boost = 0;
        BoosterType boosterType = booster.getBoosterType();
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                if (ignoreIdentifier) {
                    if (aBooster.equalsIgnoreIdentifier(booster)) {
                        boost += ab.getBoosts().getBoost(durationType).getBoost();
                    }
                } else {
                    if (aBooster.equals(booster)) {
                        boost += ab.getBoosts().getBoost(durationType).getBoost();
                        break;
                    }
                }
            }
        }
        return boost;
    }

    public static double getBoosts(Booster booster, boolean ignoreIdentifier) {
        double boost = 0;
        BoosterType boosterType = booster.getBoosterType();
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(boosterType, k -> new CopyOnWriteArrayList<>());
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                Booster aBooster = ab.getBooster();
                Boosts boosts = ab.getBoosts();
                if (ignoreIdentifier) {
                    if (aBooster.equalsIgnoreIdentifier(booster)) {
                        boost += boosts.getTemporary().getBoost() + boosts.getPermanent().getBoost();
                    }
                } else {
                    if (aBooster.equals(booster)) {
                        boost += boosts.getTemporary().getBoost() + boosts.getPermanent().getBoost();
                        break;
                    }
                }
            }
        }
        return boost;
    }

    public static double getBoostsCached(Booster booster, boolean ignoreIdentifier) {
        String key = booster.toKey()+"::"+ignoreIdentifier;
        long now = System.currentTimeMillis();

        if (cachedBoosts.containsKey(key) && (now - cacheTimestamps.getOrDefault(key, 0L)) < CACHE_EXPIRATION) {
            return cachedBoosts.get(key);
        }

        double boost = getBoosts(booster, ignoreIdentifier);
        cachedBoosts.put(key, boost);
        cacheTimestamps.put(key, now);

        return boost;
    }

    public static Map<BoosterType, Double> getTotalBoostCached(UUID uuid, ApplicatorType applicator, String boosted) {
        String identifier = "total";
        Map<BoosterType, Double> result = new HashMap<>();
        PersonalBooster personalBooster = new PersonalBooster(uuid, new BoosterIdentifier(identifier, BoosterType.PERSONAL, applicator, boosted));
        GlobalBooster globalBooster = new GlobalBooster(new BoosterIdentifier(identifier, BoosterType.GLOBAL, applicator, boosted));

        double personal = getBoostsCached(personalBooster, true);
        double global = getBoostsCached(globalBooster, true);
        double superiorSkyblock2 = 0;
        if (PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(uuid);
            if (superiorPlayer != null && superiorPlayer.hasIsland()) {
                UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                SuperiorSkyblock2Booster superiorSkyblock2Booster = new SuperiorSkyblock2Booster(islandUUID, new BoosterIdentifier(identifier, BoosterType.SUPERIORSKYBLOCK2, applicator, boosted));
                superiorSkyblock2 = getBoostsCached(superiorSkyblock2Booster, true);
            }
        }

        result.put(BoosterType.PERSONAL, personal);
        result.put(BoosterType.GLOBAL, global);
        result.put(BoosterType.SUPERIORSKYBLOCK2, superiorSkyblock2);
        return result;
    }


    public static Map<BoosterType, Double> getTotalBoost(UUID uuid, ApplicatorType applicator, String boosted) {
        String identifier = "total";
        Map<BoosterType, Double> result = new HashMap<>();
        PersonalBooster personalBooster = new PersonalBooster(uuid, new BoosterIdentifier(identifier, BoosterType.PERSONAL, applicator, boosted));
        GlobalBooster globalBooster = new GlobalBooster(new BoosterIdentifier(identifier, BoosterType.GLOBAL, applicator, boosted));

        double personalBoost = ActiveBoosterManager.getBoosts(personalBooster, true);
        double globalBoost = ActiveBoosterManager.getBoosts(globalBooster, true);
        double superiorSkyblock2Boost = 0;
        if (PluginsChecker.isPluginEnabled(PluginType.SuperiorSkyblock2)) {
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(uuid);
            if (superiorPlayer != null && superiorPlayer.hasIsland()) {
                UUID islandUUID = superiorPlayer.getIsland().getUniqueId();
                SuperiorSkyblock2Booster superiorSkyblock2Booster = new SuperiorSkyblock2Booster(islandUUID, new BoosterIdentifier(identifier, BoosterType.SUPERIORSKYBLOCK2, applicator, boosted));
                superiorSkyblock2Boost = ActiveBoosterManager.getBoosts(superiorSkyblock2Booster, true);
            }
        }
        result.put(BoosterType.PERSONAL, personalBoost);
        result.put(BoosterType.GLOBAL, globalBoost);
        result.put(BoosterType.SUPERIORSKYBLOCK2, superiorSkyblock2Boost);
        return result;
    }

    public static double getIslandBoosts(UUID islandUUID, ApplicatorType applicatorType, String boosted) {
        double boost = 0;
        List<ActiveBooster> activeBoosters = activeBoostersMap.computeIfAbsent(BoosterType.SUPERIORSKYBLOCK2, k -> new CopyOnWriteArrayList<>());
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster ab : activeBoosters) {
                SuperiorSkyblock2Booster aBooster = (SuperiorSkyblock2Booster) ab.getBooster();
                BoosterIdentifier identifier = aBooster.getIdentifier();
                if (aBooster.getUUID().equals(islandUUID) && identifier.getApplicatorType().equals(applicatorType) && identifier.getBoosted().equals(boosted)) {
                    boost += ab.getBoosts().getTotalBoost();
                }
            }
        }
        return boost;
    }
}
