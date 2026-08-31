package mv.mossuh.moboosters.MODEL;

import mv.mossuh.moboosters.DATA.Config.Config.Config;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.*;

public class Identifiers {
    private Set<String> identifiers = new HashSet<>();
    private Set<String> internalIdentifiers = new HashSet<>();

    public Identifiers(List<String> identifiers) {
        if (identifiers == null) return;

        for (String identifier : identifiers) {
            String id = identifier.toLowerCase();
            if (id.startsWith("internal_")) {
                UtilString.get(Config.PREFIX + " &cDon't allowed to register " + id + ". Identifiers beginning with internal are reserved.").hex().sendMessageInConsole();
                continue;
            }
            this.identifiers.add(identifier.toLowerCase());
        }
    }

    public List<String> getIdentifiers() { return new ArrayList<>(identifiers); }

    public void setIdentifiers(List<String> identifiers) {
        Set<String> newIdentifiers = new HashSet<>();
        if (identifiers != null) {
            for (String identifier : identifiers) {
                newIdentifiers.add(identifier.toLowerCase());
            }
        }
        this.identifiers = newIdentifiers;
    }
    public void addIdentifier(String identifier) {
        identifiers.add(identifier.toLowerCase());
    }
    public void addIdentifier(String... identifier) {
        Set<String> newIdentifiers = new HashSet<>();
        if (identifier != null) {
            for (String i : identifier) {
                newIdentifiers.add(i.toLowerCase());
            }
        }
        this.identifiers.addAll(newIdentifiers);
    }

    // Checker for Internal and Config Identifiers
    public boolean hasIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty()) return false;
        if (identifier.equalsIgnoreCase("default")) return true;
        identifier = identifier.toLowerCase();

        return identifiers.contains(identifier) || internalIdentifiers.contains(identifier);
    }

    // Add an Internal Identifier
    public String addInternalIdentifier(String identifier) {
        if (identifier == null) return "";

        String internal = "internal_"+identifier.toLowerCase();
        this.internalIdentifiers.add(internal);
        return internal;
    }

}
