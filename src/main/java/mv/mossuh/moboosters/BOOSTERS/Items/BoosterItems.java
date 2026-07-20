package mv.mossuh.moboosters.BOOSTERS.Items;

import de.tr7zw.nbtapi.NBTItem;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfigs;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterInfo;
import mv.mossuh.moboosters.UTILITIES.Enchantments.Enchant;
import mv.mossuh.moboosters.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.NBT.NBTBooster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoosterItems {

    public static BoosterItem get(ItemStack itemStack) {
        String code = NBTBooster.getCode(itemStack);
        if (code != null) {
            String identifier = NBTBooster.getIdentifier(itemStack);
            BoosterType boosterType = NBTBooster.getBoosterType(itemStack);
            ApplicatorType applicatorType = NBTBooster.getApplicator(itemStack);
            String boosted = NBTBooster.getBoosted(itemStack);
            BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
            String boost = NBTBooster.getBoost(itemStack);
            String duration = NBTBooster.getDuration(itemStack);
            String argsAsString = NBTBooster.getArgs(itemStack);
            CommandArgs args = CommandArgs.getCommandArgs(argsAsString);
            BoosterConfig boosterConfig = BoosterConfigs.getBoosterConfig(code);
            return new BoosterItem(code, boost, duration, boosterIdentifier, boosterConfig, args, itemStack);
        }
        return new BoosterItem(null, null, null, null, null, null, null);
    }

    public static BoosterItem create(String code, UUID uuid, CommandArgs args, int amount) {
        BoosterConfig boosterConfig = BoosterConfigs.getBoosterConfig(code);
        if (boosterConfig.isBooster()) {
            String material = boosterConfig.getInfo().getMaterial();
            if (material.startsWith("basehead-")) {
                return createFromHead(uuid, boosterConfig, args, amount);
            } else {
                return createFromMaterial(uuid, boosterConfig, args, amount);
            }
        }
        return new BoosterItem(null, null, null, null, null, null, null);
    }
    public static BoosterItem create(BoosterConfig boosterConfig, UUID uuid, CommandArgs args, int amount) {
        if (boosterConfig != null && boosterConfig.isBooster()) {
            String material = boosterConfig.getInfo().getMaterial();
            if (material.startsWith("basehead-")) {
                return createFromHead(uuid, boosterConfig, args, amount);
            } else {
                return createFromMaterial(uuid, boosterConfig, args, amount);
            }
        }
        return new BoosterItem(null, null, null, null, null, null, null);
    }

    private static BoosterItem createFromMaterial(UUID uuid, BoosterConfig boosterConfig, CommandArgs args, int amount) {
        BoosterIdentifier boosterIdentifier = boosterConfig.getIdentifier();
        BoosterInfo info = boosterConfig.getInfo();
        String material = info.getMaterial();
        byte data = info.getMaterialData();

        String code = boosterConfig.getCode();
        String identifier = UtilString.get(boosterIdentifier.getIdentifier()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        String boost = UtilString.get(boosterConfig.getBoost()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        String duration = UtilString.get(boosterConfig.getDuration()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        BoosterType boosterType = boosterIdentifier.getBoosterType();
        ApplicatorType applicatorType = boosterIdentifier.getApplicatorType();
        String boosted = UtilString.get(boosterIdentifier.getBoosted()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();

        BoosterIdentifier newBoosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
        BoosterConfig newBoosterConfig = new BoosterConfig(boosterConfig.getCode(), boost, duration, newBoosterIdentifier, boosterConfig.getInfo(), boosterConfig.getActions());

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (material.contains("LEATHER")) {
            String[] itemTypeSeparate = material.replace(" ", "").split("->", 2);
            Material itemMaterial = Material.valueOf(itemTypeSeparate[0]);

            if (itemTypeSeparate.length > 1 && !itemTypeSeparate[1].isEmpty()) {
                String armorColor = itemTypeSeparate[1];

                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                    itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                Color color = Color.fromRGB(Integer.parseInt(armorColor, 16));
                leatherArmorMeta.setColor(color);
                itemStack.setItemMeta(leatherArmorMeta);
            } else {
                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                    itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
            }
        } else {
            if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                itemStack = new ItemStack(Material.valueOf(material), 1);
            } else {
                if (data != -1) {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, data);
                } else {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, (byte) 0);
                }
            }
        }
        ItemMeta itemMeta = itemStack.getItemMeta();

        Enchantments enchantments = info.getEnchantments();
        List<Enchant> enchants = info.getEnchantments().getEnchants();

        List<String> flags = info.getFlags();

        if (info.hasName()) {
            String name = UtilString.get(info.getName()).hex().setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable().setArgs(args).setDefaultBoosterVariables(newBoosterConfig).setPlaceholders(uuid)
                    .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();
            itemMeta.setDisplayName(name);
        }
        if (info.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : info.getLore()) {
                lore.add(UtilString.get(line).hex().setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable().setArgs(args).setDefaultBoosterVariables(newBoosterConfig).setPlaceholders(uuid).setChangeOutputPlaceholder()
                        .setMathPlaceholder().setTimeFormatter().apply());
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (info.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        if (info.isUnbreakable()) {
            NBTItem NBTItem = new NBTItem(itemStack);
            NBTItem.setBoolean("Unbreakable", true);
            itemStack = NBTItem.getItem();
        }

        itemStack.setItemMeta(itemMeta);

        if (info.isUnique()) {
            NBTBooster.setUnique(itemStack);
        }

        if (boosterIdentifier.isIdentifier()) {
            NBTBooster.setCode(itemStack, code);
            NBTBooster.setIdentifier(itemStack, identifier);
            NBTBooster.setBoost(itemStack, boost);
            NBTBooster.setDuration(itemStack, duration);
            NBTBooster.setBoosterType(itemStack, boosterType);
            NBTBooster.setApplicator(itemStack, applicatorType);
            NBTBooster.setBoosted(itemStack, boosted);
        }
        itemStack.setAmount(Math.max(amount, 1));
        return new BoosterItem(code, boost, duration, boosterIdentifier, boosterConfig, args, itemStack);
    }


    private static BoosterItem createFromHead(UUID uuid, BoosterConfig boosterConfig, CommandArgs args, int amount) {
        BoosterIdentifier boosterIdentifier = boosterConfig.getIdentifier();
        BoosterInfo info = boosterConfig.getInfo();
        String material = info.getMaterial();
        String code = boosterConfig.getCode();
        String identifier = UtilString.get(boosterIdentifier.getIdentifier()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        String boost = UtilString.get(boosterConfig.getBoost()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        String duration = UtilString.get(boosterConfig.getDuration()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();
        BoosterType boosterType = boosterIdentifier.getBoosterType();
        ApplicatorType applicatorType = boosterIdentifier.getApplicatorType();
        String boosted = UtilString.get(boosterIdentifier.getBoosted()).setArgs(args).setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable()
                .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().apply();

        BoosterIdentifier newBoosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
        BoosterConfig newBoosterConfig = new BoosterConfig(boosterConfig.getCode(), boost, duration, newBoosterIdentifier, boosterConfig.getInfo(), boosterConfig.getActions());

        Material skullType;
        if (!ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            skullType = Material.valueOf("SKULL_ITEM");
        } else {
            skullType = Material.PLAYER_HEAD;
        }

        String value = material.replace("basehead-", "");
        ItemStack itemStack;
        if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            itemStack = new ItemStack(skullType, 1);
        } else {
            itemStack =  new ItemStack(skullType, 1, (short) 0, (byte) 3);
        }
        NBTBooster.setHeadTexture(itemStack, value);

        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

        Enchantments enchantments = info.getEnchantments();
        List<Enchant> enchants = info.getEnchantments().getEnchants();

        List<String> flags = info.getFlags();

        if (info.hasName()) {
            String name = UtilString.get(info.getName()).hex().setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable().setArgs(args).setDefaultBoosterVariables(newBoosterConfig).setPlaceholders(uuid)
                    .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();
            itemMeta.setDisplayName(name);
        }
        if (info.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : info.getLore()) {
                lore.add(UtilString.get(line).hex().setDefaultPlayerVariables(uuid).setDefaultNumberRandomVariable().setArgs(args).setDefaultBoosterVariables(newBoosterConfig).setPlaceholders(uuid).setChangeOutputPlaceholder()
                        .setMathPlaceholder().setTimeFormatter().apply());
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (info.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        if (info.isUnbreakable()) {
            NBTItem NBTItem = new NBTItem(itemStack);
            NBTItem.setBoolean("Unbreakable", true);
            itemStack = NBTItem.getItem();
        }

        itemStack.setItemMeta(itemMeta);

        if (info.isUnique()) {
            NBTBooster.setUnique(itemStack);
        }

        if (boosterIdentifier.isIdentifier()) {
            NBTBooster.setCode(itemStack, code);
            NBTBooster.setIdentifier(itemStack, identifier);
            NBTBooster.setBoost(itemStack, boost);
            NBTBooster.setDuration(itemStack, duration);
            NBTBooster.setBoosterType(itemStack, boosterType);
            NBTBooster.setApplicator(itemStack, applicatorType);
            NBTBooster.setBoosted(itemStack, boosted);
        }
        itemStack.setAmount(Math.max(amount, 1));
        return new BoosterItem(code, boost, duration, boosterIdentifier, boosterConfig, args, itemStack);
    }
}
