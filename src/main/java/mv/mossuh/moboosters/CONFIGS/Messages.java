package mv.mossuh.moboosters.CONFIGS;

import mv.mossuh.moboosters.MoBoosters;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private static final List<String> messagesConfigList = new ArrayList<>(Arrays.asList(
            "INVALID_CODE", "INVALID_AMOUNT", "INVALID_PLAYER", "INVALID_BOOSTER_TYPE", "INVALID_APPLICATOR_TYPE", "INVALID_BOOST", "INVALID_DURATION",
            "INVALID_DURATION_TYPE", "INVALID_IDENTIFIER", "INVALID_ISLAND_NAME",

            "NO_PERMISSION",
            "ACTIVATE_BOOSTER_SENDER",

            "GIVE_BOOSTER_ITEM_RECEIVER", "GIVE_BOOSTER_ITEM_SENDER",
            "GIVE_ALL_BOOSTER_ITEM_SENDER",

            "COOLDOWN",

            "SET_TEMP_BOOSTER_SENDER", "SET_PERM_BOOSTER_SENDER",

            "REMOVE_BOOSTER_SENDER",

            "PERSONAL_TEMP_BOOSTER_START", "PERSONAL_TEMP_BOOSTER_END", "PERSONAL_TEMP_BOOSTER_ADD_TIME", "PERSONAL_TEMP_BOOSTER_REMOVE_TIME",
            "PERSONAL_BOOSTER_SET_BOOST", "PERSONAL_BOOSTER_ADD_BOOST", "PERSONAL_BOOSTER_REMOVE_BOOST", "PERSONAL_BOOSTER_ALREADY_ACTIVE",

            "GLOBAL_TEMP_BOOSTER_START", "GLOBAL_TEMP_BOOSTER_END", "GLOBAL_TEMP_BOOSTER_ADD_TIME", "GLOBAL_TEMP_BOOSTER_REMOVE_TIME",
            "GLOBAL_BOOSTER_SET_BOOST", "GLOBAL_BOOSTER_ADD_BOOST", "GLOBAL_BOOSTER_REMOVE_BOOST", "GLOBAL_BOOSTER_ALREADY_ACTIVE",

            "SUPERIORSKYBLOCK2_TEMP_BOOSTER_START", "SUPERIORSKYBLOCK2_TEMP_BOOSTER_END", "SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME",
            "SUPERIORSKYBLOCK2_BOOSTER_SET_BOOST", "SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME", "SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST", "SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST", "SUPERIORSKYBLOCK2_BOOSTER_ALREADY_ACTIVE",
            "",

            "BOOSTERS"
    ));
    private static final Map<String, String> messagesMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> messagesListMap = new ConcurrentHashMap<>();

    private String messagePosition;
    private Messages(String messagePosition) {
        this.messagePosition = messagePosition;
    }
    private String get() {
        return messagesMap.get(messagePosition);
    }
    public List<String> getStringList() {
        return messagesListMap.get(messagePosition);
    }


    public static void load(MoBoosters instance) {
        FileConfiguration messagesConfig = instance.getConfigs().getMessages().getConfig();

        for (String messageConfig : messagesConfigList) {
            if (messagesConfig.isString(messageConfig)) {
                String message = messagesConfig.getString(messageConfig);
                if (message != null) {
                    messagesMap.put(messageConfig, message);
                    continue;
                }
                messagesMap.put(messageConfig, "");
            } else if (messagesConfig.isList(messageConfig)) {
                List<String> message = messagesConfig.getStringList(messageConfig);
                if (!message.isEmpty()) {
                    messagesListMap.put(messageConfig, message);
                    continue;
                }
                messagesListMap.put(messageConfig, new ArrayList<>());
            }
        }

        loadMessages();
    }

    private static void loadMessages() {
        BOOSTERS = new Messages("BOOSTERS").getStringList();

        INVALID_CODE = new Messages("INVALID_CODE").get();
        INVALID_AMOUNT = new Messages("INVALID_AMOUNT").get();
        INVALID_PLAYER = new Messages("INVALID_PLAYER").get();
        INVALID_BOOSTER_TYPE = new Messages("INVALID_BOOSTER_TYPE").get();
        INVALID_APPLICATOR_TYPE = new Messages("INVALID_APPLICATOR_TYPE").get();
        INVALID_BOOST = new Messages("INVALID_BOOST").get();
        INVALID_DURATION = new Messages("INVALID_DURATION").get();
        INVALID_IDENTIFIER = new Messages("INVALID_IDENTIFIER").get();
        INVALID_DURATION_TYPE = new Messages("INVALID_DURATION_TYPE").get();
        INVALID_ISLAND_NAME = new Messages("INVALID_ISLAND_NAME").get();
        NO_PERMISSION = new Messages("NO_PERMISSION").get();
        COOLDOWN = new Messages("COOLDOWN").get();

        GIVE_BOOSTER_ITEM_RECEIVER = new Messages("GIVE_BOOSTER_ITEM_RECEIVER").get();
        GIVE_BOOSTER_ITEM_SENDER = new Messages("GIVE_BOOSTER_ITEM_SENDER").get();
        GIVE_ALL_BOOSTER_ITEM_SENDER = new Messages("GIVE_ALL_BOOSTER_ITEM_SENDER").get();
        PERSONAL_TEMP_BOOSTER_START = new Messages("PERSONAL_TEMP_BOOSTER_START").get();
        PERSONAL_TEMP_BOOSTER_END = new Messages("PERSONAL_TEMP_BOOSTER_END").get();
        PERSONAL_TEMP_BOOSTER_ADD_TIME = new Messages("PERSONAL_TEMP_BOOSTER_ADD_TIME").get();
        PERSONAL_TEMP_BOOSTER_REMOVE_TIME = new Messages("PERSONAL_TEMP_BOOSTER_REMOVE_TIME").get();
        PERSONAL_BOOSTER_SET_BOOST = new Messages("PERSONAL_BOOSTER_SET_BOOST").get();
        PERSONAL_BOOSTER_ADD_BOOST = new Messages("PERSONAL_BOOSTER_ADD_BOOST").get();
        PERSONAL_BOOSTER_REMOVE_BOOST = new Messages("PERSONAL_BOOSTER_REMOVE_BOOST").get();
        PERSONAL_BOOSTER_ALREADY_ACTIVE = new Messages("PERSONAL_BOOSTER_ALREADY_ACTIVE").get();

        GLOBAL_TEMP_BOOSTER_START = new Messages("GLOBAL_TEMP_BOOSTER_START").get();
        GLOBAL_TEMP_BOOSTER_END = new Messages("GLOBAL_TEMP_BOOSTER_END").get();
        GLOBAL_TEMP_BOOSTER_ADD_TIME = new Messages("GLOBAL_TEMP_BOOSTER_ADD_TIME").get();
        GLOBAL_TEMP_BOOSTER_REMOVE_TIME = new Messages("GLOBAL_TEMP_BOOSTER_REMOVE_TIME").get();
        GLOBAL_BOOSTER_SET_BOOST = new Messages("GLOBAL_BOOSTER_SET_BOOST").get();
        GLOBAL_BOOSTER_ADD_BOOST = new Messages("GLOBAL_BOOSTER_ADD_BOOST").get();
        GLOBAL_BOOSTER_REMOVE_BOOST = new Messages("GLOBAL_BOOSTER_REMOVE_BOOST").get();
        GLOBAL_BOOSTER_ALREADY_ACTIVE = new Messages("GLOBAL_BOOSTER_ALREADY_ACTIVE").get();

        SUPERIORSKYBLOCK2_TEMP_BOOSTER_START = new Messages("SUPERIORSKYBLOCK2_TEMP_BOOSTER_START").get();
        SUPERIORSKYBLOCK2_TEMP_BOOSTER_END = new Messages("SUPERIORSKYBLOCK2_TEMP_BOOSTER_END").get();
        SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME = new Messages("SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME").get();
        SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME = new Messages("SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME").get();
        SUPERIORSKYBLOCK2_BOOSTER_SET_BOOST = new Messages("SUPERIORSKYBLOCK2_BOOSTER_SET_BOOST").get();
        SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST = new Messages("SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST").get();
        SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST = new Messages("SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST").get();
        SUPERIORSKYBLOCK2_BOOSTER_ALREADY_ACTIVE = new Messages("SUPERIORSKYBLOCK2_BOOSTER_ALREADY_ACTIVE").get();

        SET_TEMP_BOOSTER_SENDER = new Messages("SET_TEMP_BOOSTER_SENDER").get();
        SET_PERM_BOOSTER_SENDER = new Messages("SET_PERM_BOOSTER_SENDER").get();
        ACTIVATE_BOOSTER_SENDER = new Messages("ACTIVATE_BOOSTER_SENDER").get();
        REMOVE_BOOSTER_SENDER = new Messages("REMOVE_BOOSTER_SENDER").get();
    }

    public static List<String> BOOSTERS;
    public static String INVALID_CODE;
    public static String INVALID_AMOUNT;
    public static String INVALID_PLAYER;
    public static String INVALID_BOOSTER_TYPE;
    public static String INVALID_APPLICATOR_TYPE;
    public static String INVALID_BOOST;
    public static String INVALID_DURATION;
    public static String INVALID_IDENTIFIER;
    public static String INVALID_DURATION_TYPE;
    public static String INVALID_ISLAND_NAME;
    public static String NO_PERMISSION;
    public static String COOLDOWN;
    public static String GIVE_BOOSTER_ITEM_RECEIVER;
    public static String GIVE_BOOSTER_ITEM_SENDER;
    public static String GIVE_ALL_BOOSTER_ITEM_SENDER;
    public static String PERSONAL_TEMP_BOOSTER_START;
    public static String PERSONAL_TEMP_BOOSTER_END;
    public static String PERSONAL_TEMP_BOOSTER_ADD_TIME;
    public static String PERSONAL_TEMP_BOOSTER_REMOVE_TIME;
    public static String PERSONAL_BOOSTER_SET_BOOST;
    public static String PERSONAL_BOOSTER_ADD_BOOST;
    public static String PERSONAL_BOOSTER_REMOVE_BOOST;


    public static String PERSONAL_BOOSTER_ALREADY_ACTIVE;

    public static String GLOBAL_TEMP_BOOSTER_START;
    public static String GLOBAL_TEMP_BOOSTER_END;
    public static String GLOBAL_TEMP_BOOSTER_ADD_TIME;
    public static String GLOBAL_TEMP_BOOSTER_REMOVE_TIME;
    public static String GLOBAL_BOOSTER_SET_BOOST;
    public static String GLOBAL_BOOSTER_ADD_BOOST;
    public static String GLOBAL_BOOSTER_REMOVE_BOOST;

    public static String GLOBAL_BOOSTER_ALREADY_ACTIVE;


    public static String SUPERIORSKYBLOCK2_TEMP_BOOSTER_START;
    public static String SUPERIORSKYBLOCK2_TEMP_BOOSTER_END;
    public static String SUPERIORSKYBLOCK2_TEMP_BOOSTER_ADD_TIME;
    public static String SUPERIORSKYBLOCK2_TEMP_BOOSTER_REMOVE_TIME;
    public static String SUPERIORSKYBLOCK2_BOOSTER_SET_BOOST;
    public static String SUPERIORSKYBLOCK2_BOOSTER_ADD_BOOST;
    public static String SUPERIORSKYBLOCK2_BOOSTER_REMOVE_BOOST;


    public static String SUPERIORSKYBLOCK2_BOOSTER_ALREADY_ACTIVE;

    public static String SET_TEMP_BOOSTER_SENDER;
    public static String SET_PERM_BOOSTER_SENDER;
    public static String ACTIVATE_BOOSTER_SENDER;
    public static String REMOVE_BOOSTER_SENDER;

}
