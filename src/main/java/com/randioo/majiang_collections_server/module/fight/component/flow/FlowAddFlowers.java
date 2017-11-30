package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightFillFlower;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightFlowerCount;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.FillFlowerBox;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.FillFlower;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.event.EventAddFlower;
import com.randioo.majiang_collections_server.module.fight.component.event.EventNoticeFlowerCount;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.utils.SessionUtils;

@Component
public class FlowAddFlowers implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private FillFlower fillFlower;

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(Game game, String[] params) {
        RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
        roleGameInfo.isAddFlowerState = true;

        FillFlowerBox flowerBox = fillFlower.fill(game.getRemainCards(), roleGameInfo);
        // 设置补花后的手牌
        roleGameInfo.cards.addAll(flowerBox.getNomalCards());
        // roleGameInfo.cards.addAll(Arrays.asList(301));
        roleGameInfo.sendFlowrCards.addAll(flowerBox.getFlowerCards());
        roleGameInfo.flowerCount += flowerBox.getFlowerCards().size();
        // 发送通知
        List<List<Integer>> cardList = flowerBox.getCardList();
        for (int i = 0; i < cardList.size(); i++) {
            for (RoleGameInfo info : game.getRoleIdMap().values()) {
                SCFightFillFlower.Builder scFightFillFlower = SCFightFillFlower.newBuilder();

                scFightFillFlower.addAllCards(info.gameRoleId.equals(roleGameInfo.gameRoleId) ? flowerBox.getLine(i) : flowerBox.getHideCards(i));
                scFightFillFlower.setSeat(game.getCurrentRoleIdIndex());
                SC sc = SC.newBuilder().setSCFightFillFlower(scFightFillFlower).build();

                SCFightFillFlower.Builder showScFightFillFlower = SCFightFillFlower.newBuilder();
                showScFightFillFlower.addAllCards(flowerBox.getLine(i));
                showScFightFillFlower.setSeat(game.getCurrentRoleIdIndex());
                SC showSC = SC.newBuilder().setSCFightFillFlower(showScFightFillFlower).build();
                // 发送通知
                SessionUtils.sc(info.roleId, sc);
                // notifyObservers(FightConstant.FIGHT_ADD_FLOWER, sc, game,
                // info, showSC);

                EventAddFlower event = new EventAddFlower();
                event.game = game;
                event.sc = sc;
                event.showSC = showSC;
                event.roleGameInfo = info;

                eventBus.post(event);

            }
        }
        SC sc = SC.newBuilder()
                .setSCFightFlowerCount(
                        SCFightFlowerCount.newBuilder()
                                .setFlowerCount(roleGameInfo.flowerCount)
                                .setSeat(game.getCurrentRoleIdIndex()))
                .build();
        // 通知其他玩家花的变化
        Broadcast.broadcast(game, sc);
        // this.sendAllSeatSC(game, sc);

        EventNoticeFlowerCount event = new EventNoticeFlowerCount();
        event.game = game;
        event.sc = sc;
        
        eventBus.post(event);
//        this.notifyObservers(FightConstant.FIGHT_FLOWER_COUNT, sc, game);
    }

}
