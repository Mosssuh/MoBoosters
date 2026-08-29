package mv.mossuh.moboosters.UTILITIES.Enums;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ApplicatorType {
    private static final Set<ApplicatorType> convertedApplicators = new HashSet<>();

    public static final ApplicatorType MINECRAFT = new ApplicatorType("MINECRAFT");
    public static final ApplicatorType MOPETS = new ApplicatorType("MOPETS");
    public static final ApplicatorType MOMOMENTUM = new ApplicatorType("MOMOMENTUM");
    public static final ApplicatorType MOARMORS = new ApplicatorType("MOARMORS");
    public static final ApplicatorType ESSENTIALSX = new ApplicatorType("ESSENTIALSX");
    public static final ApplicatorType JOBSREBORN = new ApplicatorType("JOBSREBORN");
    public static final ApplicatorType PLAYERPOINTS = new ApplicatorType("PLAYERPOINTS");
    public static final ApplicatorType NONE = new ApplicatorType("NONE");

    private final String applicator;

    private ApplicatorType(String applicator) {
        this.applicator = applicator.toUpperCase();
    }

    public String name() {
        return applicator;
    }

    public static ApplicatorType convert(String applicator) {
        if (applicator == null) return NONE;
        ApplicatorType app = new ApplicatorType(applicator);
        for (ApplicatorType type : values()) {
            if (type.equals(app)) {
                return type;
            }
        }

        for (ApplicatorType a : convertedApplicators) {
            if (a.equals(app)) {
                return a;
            }
        }
        convertedApplicators.add(app);
        return app;
    }

    public static ApplicatorType[] values() {
        List<ApplicatorType> list = new ArrayList<>();

        for (Field field : ApplicatorType.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isFinal(field.getModifiers())) continue;
            if (!Modifier.isPublic(field.getModifiers())) continue;

            try {
                if (field.getType() == ApplicatorType.class) {
                    list.add((ApplicatorType) field.get(null));
                }
            } catch (IllegalAccessException ignored) {

            }
        }
        return list.toArray(new ApplicatorType[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicatorType that = (ApplicatorType) o;
        return Objects.equals(applicator.toUpperCase(), that.applicator.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicator.toUpperCase());
    }

    @Override
    public String toString() {
        return applicator;
    }
}

