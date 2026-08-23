package mv.mossuh.moboosters.MANAGERS;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.MODULES.ApplicatorModule;
import mv.mossuh.moboosters.MODULES.EventApplicatorModule;
import mv.mossuh.moboosters.MODULES.ManualApplicatorModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModulesManager {
    private static final Set<ManualApplicatorModule> manualApplicators = new HashSet<>();
    private static final Set<EventApplicatorModule> eventApplicators = new HashSet<>();

    public static void registerApplicator(JavaPlugin plugin, ApplicatorModule module) {
        if (module == null || plugin == null) return;

        if (module instanceof EventApplicatorModule) {
            EventApplicatorModule eventApplicator = (EventApplicatorModule) module;
            if (!eventApplicators.contains(eventApplicator)) {
                eventApplicators.add(eventApplicator);
                Bukkit.getPluginManager().registerEvents(eventApplicator, plugin);
            }
        }
        if (module instanceof ManualApplicatorModule) {
            manualApplicators.add((ManualApplicatorModule) module);
        }
    }

    public static ManualApplicatorModule getManualApplicator(ApplicatorType applicator, String boosted) {
        for (ManualApplicatorModule m : manualApplicators) {
            if (applicator.equals(m.getApplicatorType()) && m.getBoosted().equalsIgnoreCase(boosted)) {
                return m;
            }
        }
        return null;
    }

    public static Set<ManualApplicatorModule> getManualApplicators() { return manualApplicators; }

    public static EventApplicatorModule getEventApplicator(ApplicatorType applicator) {
        for (EventApplicatorModule m : eventApplicators) {
            if (applicator.equals(m.getApplicatorType())) {
                return m;
            }
        }
        return null;
    }

    public static Set<EventApplicatorModule> getEventApplicators() { return eventApplicators; }

    public static List<ApplicatorModule> getApplicators() {
        List<ApplicatorModule> applicators = new ArrayList<>();
        applicators.addAll(eventApplicators);
        applicators.addAll(manualApplicators);
        return applicators;
    }
}
