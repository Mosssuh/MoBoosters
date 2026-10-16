package mv.mossuh.moboosters.MODEL.Hook.Team;

import mv.mossuh.moboosters.DATA.Database.TeamData;
import mv.mossuh.mocore.ENUMS.PluginType;

import java.util.Set;
import java.util.UUID;

public interface TeamBoosterHook {
    TeamData getData();

    PluginType getPluginType();
    boolean isTeam(UUID teamUUID);
    boolean isTeam(String teamName);
    boolean hasTeam(UUID player);

    UUID getTeamByName(String teamName);
    UUID getTeamByPlayer(UUID player);


    Set<UUID> getMembers(UUID teamUUID);

}
