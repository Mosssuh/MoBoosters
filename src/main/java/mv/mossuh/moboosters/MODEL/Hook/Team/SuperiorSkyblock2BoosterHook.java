package mv.mossuh.moboosters.MODEL.Hook.Team;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import mv.mossuh.moboosters.DATA.Database.TeamData;
import mv.mossuh.mocore.ENUMS.PluginType;

import java.util.*;

public class SuperiorSkyblock2BoosterHook implements TeamBoosterHook {
    @Override
    public TeamData getData() {
        return new TeamData(getPluginType());
    }

    @Override
    public PluginType getPluginType() {
        return PluginType.SuperiorSkyblock2;
    }

    @Override
    public boolean isTeam(UUID teamUUID) {
        if (teamUUID == null) return false;
        Island island = SuperiorSkyblockAPI.getIslandByUUID(teamUUID);
        if (island == null) return false;
        return true;
    }

    @Override
    public boolean isTeam(String teamName) {
        if (teamName == null) return false;
        Island island = SuperiorSkyblockAPI.getIsland(teamName);
        if (island == null) return false;
        return true;
    }

    @Override
    public boolean hasTeam(UUID player) {
        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(player);
        if (sp == null) return false;
        return sp.hasIsland();
    }

    @Override
    public UUID getTeamByName(String teamName) {
        if (teamName == null) return null;
        Island island = SuperiorSkyblockAPI.getIsland(teamName);
        if (island == null) return null;
        return island.getUniqueId();
    }

    @Override
    public UUID getTeamByPlayer(UUID player) {
        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(player);
        if (sp == null) return null;
        if (!sp.hasIsland()) return null;
        return sp.getIsland().getUniqueId();
    }

    @Override
    public Set<UUID> getMembers(UUID teamUUID) {
        Set<UUID> uuids = new HashSet<>();
        Island island = SuperiorSkyblockAPI.getIslandByUUID(teamUUID);
        if (island == null) return uuids;

        for (SuperiorPlayer p : island.getIslandMembers(true)) {
            uuids.add(p.getUniqueId());
        }
        return uuids;
    }

    public static Island getIslandByUUID(UUID islandUUID) {
        return SuperiorSkyblockAPI.getIslandByUUID(islandUUID);
    }
}
