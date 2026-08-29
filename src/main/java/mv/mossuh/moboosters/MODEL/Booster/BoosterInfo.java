package mv.mossuh.moboosters.MODEL.Booster;

import mv.mossuh.moboosters.MODEL.Booster.Enchantments.Enchantments;
import mv.mossuh.moboosters.UTILITIES.UtilString;

import java.util.ArrayList;
import java.util.List;

public class BoosterInfo {

    private String material = "AIR";
    private byte data = -1;
    private String name;
    private List<String> lore = new ArrayList<>();
    private Enchantments enchantments;
    private List<String> flags = new ArrayList<>();
    private boolean unbreakable = false;
    private boolean unique = false;
    public BoosterInfo(String material, Byte data, String name, List<String> lore, Enchantments enchantments, List<String> flags, Boolean unbreakable, Boolean unique) {
        if (material != null) { this.material = material; }
        if (data != null) { this.data = data; }
        if (name != null) { this.name = UtilString.get(name).hex().apply(); }
        List<String> newLore = new ArrayList<>();
        if (lore != null) { for (String line : lore) { newLore.add(UtilString.get(line).hex().apply()); } }
        this.lore = newLore;
        this.enchantments = enchantments;
        if (flags != null) { this.flags = flags; }
        if (unbreakable != null) { this.unbreakable = unbreakable; }
        if (unique != null) { this.unique = unique; }
    }

    public String getMaterial() {
        return material;
    }
    public byte getMaterialData() {
        return data;
    }
    public String getName() {
        return name;
    }
    public boolean hasName() {
        if (name != null) { return true; }
        return false;
    }
    public List<String> getLore() {
        return lore;
    }
    public boolean hasLore() {
        if (lore != null && !lore.isEmpty()) { return true; }
        return false;
    }
    public Enchantments getEnchantments() {
        return enchantments;
    }
    public List<String> getFlags() {
        return flags;
    }

    public boolean hasFlag() {
        if (flags != null && !flags.isEmpty()) { return true; }
        return false;
    }
    public boolean isUnbreakable() {
        return unbreakable;
    }

    public boolean isUnique() {
        return unique;
    }
}
