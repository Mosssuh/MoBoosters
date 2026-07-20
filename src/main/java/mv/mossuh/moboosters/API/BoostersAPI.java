package mv.mossuh.moboosters.API;

import mv.mossuh.moboosters.MANAGERS.BoosterManager;

public class BoostersAPI {
    private static final BoosterManager boosterManager = new BoosterManager();
    public static BoosterManager getManager() {
        return boosterManager;
    }
}
