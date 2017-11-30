package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.bo.Role;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowAutoHu implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Override
    public void execute(Game game, String[] params) {
        for (CallCardList item : game.getAutoHuCallCardList()) {
            int cardListId = item.cardListId;
            int seat = item.masterSeat;
            RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, seat);
            int roleId = roleGameInfo.roleId;
            Role role = (Role) RoleCache.getRoleById(roleId);
            hu(role, game.getSendCardCount(), cardListId);
        }
    }

}
