package mv.mossuh.moboosters.MANAGER;

import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.HOOK.ApplicatorHook;
import mv.mossuh.moboosters.HOOK.EventApplicatorHook;
import mv.mossuh.moboosters.HOOK.StateApplicatorHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HooksManager {
    private static final Set<StateApplicatorHook> manualApplicators = new HashSet<>();
    private static final Set<EventApplicatorHook> eventApplicators = new HashSet<>();

    public static void registerApplicator(JavaPlugin plugin, ApplicatorHook module) {
        if (module == null || plugin == null) return;

        if (module instanceof EventApplicatorHook) {
            EventApplicatorHook eventApplicator = (EventApplicatorHook) module;
            if (!eventApplicators.contains(eventApplicator)) {
                eventApplicators.add(eventApplicator);
                Bukkit.getPluginManager().registerEvents(eventApplicator, plugin);
            }
        }
        if (module instanceof StateApplicatorHook) {
            manualApplicators.add((StateApplicatorHook) module);
        }
    }

    public static StateApplicatorHook getStateApplicator(ApplicatorType applicator, String boosted) {
        if (applicator == null || boosted == null) return null;
        for (StateApplicatorHook m : manualApplicators) {
            if (applicator.equals(m.getApplicatorType()) && m.getBoosted().equalsIgnoreCase(boosted)) {
                return m;
            }
        }
        return null;
    }

    public static Set<StateApplicatorHook> getStateApplicators() { return manualApplicators; }

    public static EventApplicatorHook getEventApplicator(ApplicatorType applicator) {
        for (EventApplicatorHook m : eventApplicators) {
            if (applicator.equals(m.getApplicatorType())) {
                return m;
            }
        }
        return null;
    }

    public static Set<EventApplicatorHook> getEventApplicators() { return eventApplicators; }

    public static List<ApplicatorHook> getApplicators() {
        List<ApplicatorHook> applicators = new ArrayList<>();
        applicators.addAll(eventApplicators);
        applicators.addAll(manualApplicators);
        return applicators;
    }
}
