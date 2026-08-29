package mv.mossuh.moboosters.UTILITIES.Enums;

public enum BoosterType {
    PERSONAL(true),
    GLOBAL(false),
    SUPERIORSKYBLOCK2(true),
    NONE(false);


    private boolean hasUUID = false;
    BoosterType(boolean hasUUID) {
        this.hasUUID = hasUUID;
    }

    public boolean hasUUID() {
        return hasUUID;
    }
}
