package com.randioo.majiang_collections_server.module.fight.component.flow;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.randioo_server_base.processor.Flow;

public class FlowInitReady implements Flow<Game> {

    @Override
    public void execute(Game game, String[] params) {
        // 除npc外所有玩家重置准备
        for (RoleGameInfo info : game.getRoleIdMap().values()) {
            if (info.roleId == 0) {
                continue;
            }
            info.ready = false;
        }
    }

}
