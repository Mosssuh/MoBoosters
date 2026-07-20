package mv.mossuh.moboosters.DEBUG;

import mv.mossuh.moboosters.ENUMS.DebugType;

import java.util.HashSet;
import java.util.Set;

public class Debugs {
    private static final Set<Debug> debugs = new HashSet<>();

    public static Debug getDebug(DebugType debugType) {
        if (debugType != null && debugType != DebugType.NONE) {
            for (Debug debug : debugs) {
                if (debug.getDebugType() == debugType) {
                    return debug;
                }
            }

            Debug debug = new Debug(debugType, true);
            debugs.add(debug);
            return debug;
        }
        return new Debug(null, null);
    }

    public static boolean isActive(DebugType debugType) {
        if (debugType != null && debugType != DebugType.NONE) {
            for (Debug debug : debugs) {
                if (debug.getDebugType() == debugType) {
                    return debug.getStatus();
                }
            }
        }
        return false;
    }

    public static void setStatus(DebugType debugType, Boolean status) {
        if (debugType != null && debugType != DebugType.NONE) {
            for (Debug debug : debugs) {
                if (debug.getDebugType() == debugType) {
                    debug.setStatus(status);
                    return;
                }
            }
            Debug debug = new Debug(debugType, status);
            debugs.add(debug);
        }
    }

    public static boolean changeStatus(DebugType debugType) {
        if (debugType != null && debugType != DebugType.NONE) {
            for (Debug debug : debugs) {
                if (debug.getDebugType() == debugType) {
                    boolean status = debug.getStatus();
                    return debug.setStatus(!status);
                }
            }

            Debug debug = new Debug(debugType, true);
            debugs.add(debug);
            return debug.getStatus();
        }
        return false;
    }
}
