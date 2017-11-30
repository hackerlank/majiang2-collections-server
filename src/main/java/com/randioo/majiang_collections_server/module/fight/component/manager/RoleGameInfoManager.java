package com.randioo.majiang_collections_server.module.fight.component.manager;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;

@Component
public class RoleGameInfoManager {

    /** 游戏玩家id格式 */
    public static final String GAME_ROLE_ID_FORMAT = "{0}_{1}_0";
    /** 机器人玩家的格式 */
    public static final String AI_GAME_ROLE_ID_FORMAT = "{0}_0_{1}";

    /**
     * 获得当前玩家的信息
     * 
     * @param gameId
     * @return
     * @author wcy 2017年6月2日
     */
    public RoleGameInfo current(Game game) {
        int index = game.getCurrentRoleIdIndex();
        RoleGameInfo roleGameInfo = this.getRoleGameInfoBySeat(game, index);
        return roleGameInfo;
    }

    public RoleGameInfo getRoleGameInfoBySeat(Game game, int seat) {
        String gameRoleId = game.getRoleIdList().get(seat);
        return game.getRoleIdMap().get(gameRoleId);
    }

    /**
     * 获得玩家id
     * 
     * @param game
     * @param roleId
     * @return
     * @author wcy 2017年11月7日
     */
    public String getGameRoleId(Game game, int roleId) {
        String gameRoleId = MessageFormat.format(GAME_ROLE_ID_FORMAT, String.valueOf(game.getGameId()),
                String.valueOf(roleId));
        return gameRoleId;
    }

    /**
     * @param gameId
     * @param roleId
     * @return
     * @author wcy 2017年5月24日
     */
    public String getAIGameRoleId(Game game) {
        int aiCount = 0;
        for (RoleGameInfo roleGameInfo : game.getRoleIdMap().values()) {
            if (roleGameInfo.roleId == 0) {
                aiCount++;
            }
        }
        return MessageFormat.format(AI_GAME_ROLE_ID_FORMAT, String.valueOf(game.getGameId()), String.valueOf(aiCount));
    }
}
