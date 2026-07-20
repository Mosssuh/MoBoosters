package mv.mossuh.moboosters.MODULES;

import mv.mossuh.moboosters.MANAGERS.IslandModuleManager;
import mv.mossuh.moboosters.MoBoosters;

import java.util.ServiceLoader;

public class ModulesLoader {

    public static void loadIslandBoosterModule(MoBoosters main) {
        ServiceLoader<IslandBoosterModule> loader = ServiceLoader.load(IslandBoosterModule.class, main.getClass().getClassLoader());

        for (IslandBoosterModule module : loader) {
            IslandModuleManager.registerModule(module);
        }
    }
}
