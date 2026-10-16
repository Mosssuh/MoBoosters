package mv.mossuh.moboosters.UTILITIES.Enums;

import mv.mossuh.mocore.ENUMS.PluginType;

import java.util.ArrayList;
import java.util.List;

public enum BoosterType {
    PERSONAL(true, PluginType.None),
    GLOBAL(false, PluginType.None),
    TEAM(true, PluginType.None),
    // SUPERIORSKYBLOCK2(true),
    NONE(false, PluginType.None);


    private boolean hasUUID;
    private PluginType hook;
    BoosterType(boolean hasUUID, PluginType hook) {
        this.hasUUID = hasUUID;
        this.hook = hook;
    }

    public void setHook(PluginType type) { this.hook = type; }
    public boolean hasHook() { return !hook.equals(PluginType.None); }
    public PluginType getHook() { return hook; }

    public boolean hasUUID() {
        return hasUUID;
    }


    public static List<BoosterType> list() {
        List<BoosterType> list = new ArrayList<>();
        for (BoosterType type : BoosterType.values()) {
            if (type.equals(BoosterType.NONE)) continue;

            list.add(type);
        }
        return list;
    }

    public static BoosterType getBoosterType(String boosterTypeString) {
        BoosterType boosterType = BoosterType.NONE;
        if (boosterTypeString != null) {
            switch (boosterTypeString.toLowerCase()) {
                case "personal":
                case "player":
                    boosterType = BoosterType.PERSONAL;
                    break;
                case "global":
                case "players":
                case "all":
                    boosterType = BoosterType.GLOBAL;
                    break;
                case "team":
                case "teams":
                    boosterType = BoosterType.TEAM;
                    break;
            }
        }
        return boosterType;
    }
}
