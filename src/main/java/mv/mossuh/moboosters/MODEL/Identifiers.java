package mv.mossuh.moboosters.MODEL;

import java.util.*;

public class Identifiers {
    private Set<String> identifiers = new HashSet<>();

    public Identifiers(List<String> identifiers) {
        if (identifiers != null) {
            for (String identifier : identifiers) {
                this.identifiers.add(identifier.toLowerCase());
            }
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
    public boolean hasIdentifier(String identifier) {
        if (identifier != null) {
            if (!identifier.isEmpty()) {
                if (identifier.equalsIgnoreCase("default")) {
                    return true;
                }
                for (String i : identifiers) {
                    if (i.equalsIgnoreCase(identifier)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
