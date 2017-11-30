package com.randioo.majiang_collections_server.module.fight.component.flow;

import com.randioo.mahjong_public_server.protocol.Fight.SCFightNoticeSendCard;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.FightConstant;
import com.randioo.majiang_collections_server.module.fight.component.processor.flow.Flow;
import com.randioo.randioo_server_base.utils.SessionUtils;

public class FlowNoticeSendCard implements Flow<Game> {

    @Override
    public void execute(Game game, String[] params) {
        RoleGameInfo roleGameInfo = this.current(game);
        int index = game.getRoleIdList().indexOf(roleGameInfo.gameRoleId);

        SC noticeSendCard = SC.newBuilder()
                .setSCFightNoticeSendCard(
                        SCFightNoticeSendCard.newBuilder().setSeat(index).setBanCard(roleGameInfo.chiCard))
                .build();
        SessionUtils.sc(roleGameInfo.roleId, noticeSendCard);
        this.notifyObservers(FightConstant.FIGHT_NOTICE_SEND_CARD, noticeSendCard, game, roleGameInfo, index);
    }

}
