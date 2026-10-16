package mv.mossuh.moboosters.UTILITIES.Enums;

public enum DurationType {
    PERM,
    TEMP,
    NONE;


    public static DurationType getDurationType(String durationTypeString) {
        DurationType durationType = DurationType.NONE;
        if (durationTypeString != null) {
            switch (durationTypeString.toLowerCase()) {
                case "temp":
                case "temporal":
                case "temporally":
                    durationType = DurationType.TEMP;
                    break;
                case "perm":
                case "permanent":
                case "permanently":
                    durationType = DurationType.PERM;
                    break;
            }
        }
        return durationType;
    }
}
