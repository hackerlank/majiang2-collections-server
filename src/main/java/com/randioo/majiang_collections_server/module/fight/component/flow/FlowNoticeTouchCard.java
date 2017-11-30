package com.randioo.majiang_collections_server.module.fight.component.flow;

import com.randioo.mahjong_public_server.protocol.Fight.SCFightTouchCard;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.FightConstant;
import com.randioo.majiang_collections_server.module.fight.component.processor.flow.Flow;
import com.randioo.randioo_server_base.utils.SessionUtils;

public class FlowNoticeTouchCard implements Flow<Game> {

    @Override
    public void execute(Game game, String[] params) {
        int seat = game.getCurrentRoleIdIndex();

        // boolean isFlower = game.getRule().isFlower(game,
        // roleGameInfo.newCard);
        // for (RoleGameInfo info : game.getRoleIdMap().values()) {
        // if (info.gameRoleId.equals(roleGameInfo.gameRoleId) || isFlower) {
        //
        // } else {
        // info.everybodyTouchCard = 0;
        // }
        // }

        int showTouchCard = 0;
        for (RoleGameInfo info : game.getRoleIdMap().values()) {
            if (info.everybodyTouchCard != 0) {
                showTouchCard = info.everybodyTouchCard;
                break;
            }
        }

        // 通知该玩家摸到的是什么牌
        for (RoleGameInfo info : game.getRoleIdMap().values()) {
            int touchCard = info.everybodyTouchCard;

            SCFightTouchCard scFightTouchCard = SCFightTouchCard.newBuilder()
                    .setSeat(seat)
                    .setIsFlower(game.touchCardIsFlower)
                    .setRemainCardCount(game.getRemainCards().size())
                    .setTouchCard(touchCard)
                    .build();
            SC sc = SC.newBuilder().setSCFightTouchCard(scFightTouchCard).build();

            SC showSC = SC.newBuilder()
                    .setSCFightTouchCard(scFightTouchCard.toBuilder().setTouchCard(showTouchCard))
                    .build();

            SessionUtils.sc(info.roleId, sc);

            notifyObservers(FightConstant.FIGHT_TOUCH_CARD, sc, game, info, showSC);
        }
    }

}
