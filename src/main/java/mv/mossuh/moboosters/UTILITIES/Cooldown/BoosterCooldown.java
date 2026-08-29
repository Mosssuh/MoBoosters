package mv.mossuh.moboosters.UTILITIES.Cooldown;

import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.moboosters.MODEL.Booster.BoostTypes.TemporaryBoost;
import mv.mossuh.moboosters.MoBoosters;
import org.bukkit.Bukkit;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MANAGER.ActiveBoosterManager;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.MODEL.Booster.Duration.BoosterDuration;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.DATA.Config.Messages;
import mv.mossuh.moboosters.API.Events.EndsTemporaryBoosterEvent;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoosterCooldown {

    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Map<Booster, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private static final Map<Booster, AtomicBoolean> verification = new ConcurrentHashMap<>();

    public static void startCooldown(ActiveBooster activeBooster) {
        Booster booster = activeBooster.getBooster();
        BoosterType boosterType = booster.getIdentifier().getBoosterType();
        TemporaryBoost boost = activeBooster.getBoosts().getTemporary();
        if (boosterType.equals(BoosterType.PERSONAL)) {
            startCooldown((PersonalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.GLOBAL)) {
            startCooldown((GlobalBooster) booster, boost);
        } else if (boosterType.equals(BoosterType.SUPERIORSKYBLOCK2)) {
            startCooldown((SuperiorSkyblock2Booster) booster, boost);
        }
    }

    private static void startCooldown(PersonalBooster activeBooster, TemporaryBoost booster) {
        UUID uuid = activeBooster.getUUID();
        BoosterIdentifier boosterIdentifier = activeBooster.getIdentifier();
        BoosterDuration boosterDuration = booster.getDuration();
        long cooldown = boosterDuration.getDuration();

        ScheduledFuture<?> task = tasks.get(activeBooster);
        AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(activeBooster, k -> new AtomicBoolean(false));


        if (task != null) {
            task.cancel(true);
            tasks.remove(activeBooster);
            isVerificationInProgress.set(false);
        }

        if (isVerificationInProgress.compareAndSet(false, true)) {
            TemporaryBoost copyBooster  = new TemporaryBoost(booster.getBoost(), boosterDuration);
            task = executor.schedule(() -> {
                Bukkit.getScheduler().runTask(MoBoosters.getInstance(), () -> {
                    EndsTemporaryBoosterEvent endsEvent = new EndsTemporaryBoosterEvent(activeBooster, copyBooster);
                    Bukkit.getPluginManager().callEvent(endsEvent);
                });

                if (boosterIdentifier.getIdentifier().equalsIgnoreCase("default")) {
                    UtilString.get(Messages.PERSONAL_TEMP_BOOSTER_END).setVariables(uuid).setVariables(boosterIdentifier).setVariables(copyBooster)
                            .setPlaceholders(uuid).hex().sendMessage(uuid);
                }

                booster.cancel();

                isVerificationInProgress.set(false);
            }, cooldown, TimeUnit.SECONDS);

            tasks.put(activeBooster, task);
        }
    }

    private static void startCooldown(GlobalBooster activeBooster, TemporaryBoost booster) {
        BoosterIdentifier boosterIdentifier = activeBooster.getIdentifier();
        BoosterDuration boosterDuration = booster.getDuration();
        long cooldown = boosterDuration.getDuration();

        ScheduledFuture<?> task = tasks.get(activeBooster);
        AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(activeBooster, k -> new AtomicBoolean(false));


        if (task != null) {
            task.cancel(true);
            tasks.remove(activeBooster);
            isVerificationInProgress.set(false);
        }

        if (isVerificationInProgress.compareAndSet(false, true)) {
            TemporaryBoost copyBooster  = new TemporaryBoost(booster.getBoost(), boosterDuration);
            task = executor.schedule(() -> {
                Bukkit.getScheduler().runTask(MoBoosters.getInstance(), () -> {
                    EndsTemporaryBoosterEvent endsEvent = new EndsTemporaryBoosterEvent(activeBooster, copyBooster);
                    Bukkit.getPluginManager().callEvent(endsEvent);
                });


                if (boosterIdentifier.getIdentifier().equalsIgnoreCase("default")) {
                    UtilString.get(Messages.GLOBAL_TEMP_BOOSTER_END).setVariables(boosterIdentifier).setVariables(copyBooster).hex().sendMessageToOnlinePlayers();
                }
                booster.cancel();

                isVerificationInProgress.set(false);
            }, cooldown, TimeUnit.SECONDS);

            tasks.put(activeBooster, task);
        }
    }

    private static void startCooldown(SuperiorSkyblock2Booster activeBooster, TemporaryBoost booster) {
        UUID uuid = activeBooster.getUUID();
        BoosterIdentifier boosterIdentifier = activeBooster.getIdentifier();
        BoosterDuration boosterDuration = booster.getDuration();
        long cooldown = boosterDuration.getDuration();

        ScheduledFuture<?> task = tasks.get(activeBooster);
        AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(activeBooster, k -> new AtomicBoolean(false));


        if (task != null) {
            task.cancel(true);
            tasks.remove(activeBooster);
            isVerificationInProgress.set(false);
        }

        if (isVerificationInProgress.compareAndSet(false, true)) {
            TemporaryBoost copyBooster  = new TemporaryBoost(booster.getBoost(), boosterDuration);
            task = executor.schedule(() -> {
                Bukkit.getScheduler().runTask(MoBoosters.getInstance(), () -> {
                    EndsTemporaryBoosterEvent endsEvent = new EndsTemporaryBoosterEvent(activeBooster, copyBooster);
                    Bukkit.getPluginManager().callEvent(endsEvent);
                });

                Island island = UtilMethods.getIslandByUUID(uuid);
                if (island != null) {
                    List<SuperiorPlayer> members = island.getIslandMembers(true);
                    for (SuperiorPlayer member : members) {
                        if (member.isOnline()) {
                            UUID memberUUID = member.getUniqueId();
                            if (boosterIdentifier.getIdentifier().equalsIgnoreCase("default")) {
                                UtilString.get(Messages.SUPERIORSKYBLOCK2_TEMP_BOOSTER_END).setVariables(boosterIdentifier).setVariables(copyBooster).hex().sendMessage(memberUUID);
                            }
                        }
                    }
                }

                booster.cancel();

                isVerificationInProgress.set(false);
            }, cooldown, TimeUnit.SECONDS);

            tasks.put(activeBooster, task);
        }
    }


    public static void cancelCooldown(Booster booster, TemporaryBoost boost) {
        ScheduledFuture<?> task = tasks.get(booster);
        AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(booster, k -> new AtomicBoolean(false));

        if (task != null) {
            TemporaryBoost copyBooster  = new TemporaryBoost(boost.getBoost(), boost.getDuration());
            EndsTemporaryBoosterEvent endsEvent = new EndsTemporaryBoosterEvent(booster, copyBooster);
            Bukkit.getPluginManager().callEvent(endsEvent);

            boost.cancel();

            task.cancel(true);
            tasks.remove(booster);
            isVerificationInProgress.set(false);

        }
    }

    public static void removeCooldown(Booster booster) {
        ScheduledFuture<?> task = tasks.get(booster);
        AtomicBoolean isVerificationInProgress = verification.get(booster);

        if (task != null) {
            tasks.remove(booster);
        }
        if (isVerificationInProgress != null) {
            verification.remove(booster);
        }
    }


    public static void initializeActiveBoosters(BoosterType boosterType) {
        List<ActiveBooster> activeBoosters = ActiveBoosterManager.getActiveBoosters(boosterType);
        if (!activeBoosters.isEmpty()) {
            for (ActiveBooster activeBooster : activeBoosters) {
                startCooldown(activeBooster);
            }
        }
    }
}
