package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;

import com.randioo.mahjong_public_server.protocol.Fight.SCFightTingCheck;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.RoleGameInfoGetter;
import com.randioo.majiang_collections_server.module.fight.component.processor.flow.Flow;
import com.randioo.randioo_server_base.utils.SessionUtils;

public class FlowNoticeTing implements Flow<Game> {

    @Autowired
    private RoleGameInfoGetter roleGameInfoGetter;

    @Override
    public void execute(Game game, String[] params) {
        RoleGameInfo roleGameInfo = roleGameInfoGetter.current(game);
        int roleId = roleGameInfo.roleId;
        SC sc = SC.newBuilder().setSCFightTingCheck(SCFightTingCheck.newBuilder()).build();
        SessionUtils.sc(roleId, sc);
    }

}
