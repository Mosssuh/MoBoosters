package mv.mossuh.moboosters.CONFIGS;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.MoBoosters;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.CONFIG.FolderConfigs;
import mv.mossuh.mocore.CONFIG.MoConfig;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfig;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterConfigs;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterInfo;
import mv.mossuh.moboosters.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class Configs {
    private MoBoosters instance = MoBoosters.getInstance();

    private static FolderConfigs boostersConfigs;
    private static MoConfig mainConfig;
    private static MoConfig messagesConfig;
    private static MoConfig personalBoostersData;
    private static MoConfig globalBoostersData;
    private static MoConfig superiorSkyblock2BoostersData;
    private static MoConfig informationBoosters;
    public Configs(MoBoosters main) {
        boostersConfigs = new FolderConfigs(true, main, "boosters");
        mainConfig = new MoConfig("config.yml", main, null);
        messagesConfig = new MoConfig("messages.yml", main, null);
        personalBoostersData = new MoConfig("personal-boosters-data.db", main, "datas");
        globalBoostersData = new MoConfig("global-boosters-data.db", main, "datas");
        superiorSkyblock2BoostersData = new MoConfig("superiorskyblock2-boosters-data.db", main, "datas");
        informationBoosters = new MoConfig("information.yml", main, null);
    }

    public void configure() {
        mainConfig.registerConfig(true);
        messagesConfig.registerConfig(true);
        boostersConfigs.configure("Example1.yml", "Example2.yml");
        informationBoosters.registerConfig(true);

        personalBoostersData.registerConfig(false);
        globalBoostersData.registerConfig(false);
        superiorSkyblock2BoostersData.registerConfig(false);

        configureBoostersItems();
    }
    public MoConfig getConfig() {
        return mainConfig;
    }
    public MoConfig getPersonalBoostersData() { return personalBoostersData; }
    public MoConfig getGlobalBoostersData() { return globalBoostersData; }
    public MoConfig getSuperiorSkyblock2BoostersData() { return superiorSkyblock2BoostersData; }
    public List<MoConfig> getBoosters() {
        List<MoConfig> configs = new ArrayList<>();

        configs.add(mainConfig);
        configs.addAll(boostersConfigs.getConfigs());

        return configs;
    }
    public MoConfig getMessages() { return messagesConfig; }
    private static final List<String> codes = new ArrayList<>();
    public static List<String> getCodes() {
        return codes;
    }

    public void reload(){
        mainConfig.reloadConfig();
        messagesConfig.reloadConfig();
        boostersConfigs.reloadConfigs();
        informationBoosters.reloadConfig();

        BoosterConfigs.clearBoosters();
        configureBoostersItems();
        Config.load(instance);
        Messages.load(instance);
    }

    public void configureBoostersItems() {
        int savedBoostersItems = 0;
        List<MoConfig> configs = getBoosters();

        for (MoConfig configClass : configs) {
            FileConfiguration config = configClass.getConfig();

            if (config.contains("Boosters") && !config.getConfigurationSection("Boosters").getKeys(false).isEmpty()) {
                for (String code : config.getConfigurationSection("Boosters").getKeys(false)) {
                    codes.add(code);
                    if (!config.contains("Boosters." + code + ".item-info.material")) {
                        UtilString.get(Config.PREFIX+" &cThe booster couldn't be loaded in " + code + ", due to possible lack of material.").hex().sendMessageInConsole();
                        continue;
                    }
                    String typeString = config.getString("Boosters." + code + ".type");
                    if (typeString == null) {
                        UtilString.get(Config.PREFIX+" &cThe booster couldn't be loaded in " + code + ", due to possible lack of type.").hex().sendMessageInConsole();
                        continue;
                    }
                    String applicatorString = config.getString("Boosters." + code + ".applicator");
                    if (applicatorString == null) {
                        UtilString.get(Config.PREFIX+" &cThe booster couldn't be loaded in " + code + ", due to possible lack of applicator.").hex().sendMessageInConsole();
                        continue;
                    }
                    String boosted = config.getString("Boosters." + code + ".boosted");
                    if (boosted == null) {
                        UtilString.get(Config.PREFIX+" &cThe booster couldn't be loaded in " + code + ", due to possible lack of boosted.").hex().sendMessageInConsole();
                        continue;
                    }
                    String boost = config.getString("Boosters." + code + ".boost");
                    if (boost == null) {
                        boost = "0";
                    }
                    String duration = config.getString("Boosters." + code + ".duration");
                    if (duration == null) {
                        duration = "PERM";
                    }

                    String identifier = config.getString("Boosters." + code + ".identifier");
                    BoosterType boosterType = UtilMethods.getBoosterType(typeString);
                    ApplicatorType applicatorType = ApplicatorType.convert(applicatorString);

                    String itemMaterial = config.getString("Boosters." + code + ".item-info.material");
                    Byte itemData = (byte) config.getLong("Boosters." + code + ".item-info.data");
                    String itemName = config.getString("Boosters." + code + ".item-info.name");
                    List<String> itemLore = config.getStringList("Boosters." + code + ".item-info.lore");
                    Boolean itemUnbreakable = config.getBoolean("Boosters." + code + ".item-info.unbreakable");
                    Boolean itemUnique = config.getBoolean("Boosters." + code + ".item-info.unique");
                    List<String> itemEnchantments = config.getStringList("Boosters." + code + ".item-info.enchantments");
                    List<String> itemFlags = config.getStringList("Boosters." + code + ".item-info.flags");

                    BoosterIdentifier boosterIdentifier = new BoosterIdentifier(identifier, boosterType, applicatorType, boosted);
                    BoosterInfo itemInfo = new BoosterInfo(itemMaterial, itemData, itemName, itemLore, new Enchantments(itemEnchantments), itemFlags, itemUnbreakable, itemUnique);
                    List<MoAction> actionList = transformToActions(code, config);

                    MoActions actions = new MoActions(actionList);

                    BoosterConfig boosterConfig = new BoosterConfig(code, boost, duration, boosterIdentifier, itemInfo, actions);
                    BoosterConfigs.addBoosterConfig(boosterConfig);
                    savedBoostersItems = savedBoostersItems + 1;
                }
            }
        }
        UtilString.get(Config.PREFIX+" &aLoaded &2" + savedBoostersItems + " &aboosters of the configurations!").hex().sendMessageInConsole();
    }


    private static List<MoAction> transformToActions(String code, FileConfiguration config) {
        List<MoAction> moActions = new ArrayList<>();
        if (config.contains("Boosters." + code + ".actions") && !config.getString("Boosters." + code + ".actions").isEmpty()) {
            for (String actionName : config.getConfigurationSection("Boosters." + code + ".actions").getKeys(false)) {

                List<MoRequirement> requirementList = new ArrayList<>();
                boolean cancelAction = config.getBoolean("Boosters." + code + ".actions." + actionName + ".cancel_action");

                if (config.contains("Boosters." + code + ".actions." + actionName + ".requirements")) {
                    List<String> requirementsAsString = config.getStringList("Boosters." + code + ".actions." + actionName + ".requirements");
                    for (String requirementString : requirementsAsString) {
                        String[] requirementSplit = requirementString.split(" ", 2);
                        String requirementTypeAsString = requirementSplit[0].replace("]", "").replace("[", "").toUpperCase();
                        RequirementType requirementType = RequirementType.valueOf(requirementTypeAsString.toUpperCase());
                        String requirement = requirementSplit[1];
                        MoRequirement requirementClass = new MoRequirement(null, null);
                        if (requirementType.equals(RequirementType.EVAL)){
                            requirementClass = new MoRequirement(requirementType, RequirementEval.separateEvals(requirement));
                        }
                        requirementList.add(requirementClass);
                    }
                }

                List<MoReward> rewardList = new ArrayList<>();

                if (config.contains("Boosters." + code + ".actions." + actionName + ".rewards")) {
                    List<String> rewardsAsString = config.getStringList("Boosters." + code + ".actions." + actionName + ".rewards");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        rewardList.add(reward);
                    }
                }

                List<MoReward> elseRewardList = new ArrayList<>();

                if (config.contains("Boosters." + code + ".actions." + actionName + ".else")) {
                    List<String> rewardsAsString = config.getStringList("Boosters." + code + ".actions." + actionName + ".else");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        elseRewardList.add(reward);
                    }
                }

                MoRequirements moRequirements = new MoRequirements(requirementList, null);
                MoRewards moRewards = new MoRewards(rewardList, null);
                MoRewards moElseRewards = new MoRewards(elseRewardList, null);
                MoAction moAction = new MoAction(actionName, cancelAction, null, null, moRequirements, moRewards, moElseRewards);
                moActions.add(moAction);
            }
        }
        return moActions;
    }

    @NotNull
    private static List<AvailableRewards> transformRandomRewards(String reward) {
        List<AvailableRewards> availableRewards = new ArrayList<>();

        String[] rewardSplitY = reward.split(" && ");

        for (String rewardOptions : rewardSplitY) {
            String[] separatedRandomReward = rewardOptions.split(" \\|\\| ");
            AvailableRewards randomRewards = new AvailableRewards(SelectedReward.transformToRewards(separatedRandomReward));
            availableRewards.add(randomRewards);
        }
        return availableRewards;
    }

}
