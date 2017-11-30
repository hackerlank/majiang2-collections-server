package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Entity.CardListData;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightCardList;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Peng;
import com.randioo.majiang_collections_server.module.fight.component.event.EventPeng;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.majiang_collections_server.module.fight.component.parser.PengConverter;
import com.randioo.majiang_collections_server.util.Lists;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowPeng implements Flow<Game> {
    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private PengConverter pengConverter;

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(Game game, String[] params) {
        // 获得第一个人的卡组
        CallCardList callCardList = game.getCallCardLists().get(0);
        int seat = callCardList.masterSeat;
        RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, seat);
        // 牌归自己
        Peng peng = (Peng) callCardList.cardList;

        roleGameInfo.cards.add(peng.card);

        // 移除手牌
        Lists.removeElementByList(roleGameInfo.cards, peng.getCards());

        // 显示到我方已碰的桌面上
        roleGameInfo.showCardLists.add(peng);

        CardListData pengData = pengConverter.parse(peng);

        SC sc = SC.newBuilder()
                .setSCFightCardList(SCFightCardList.newBuilder().setCardListData(pengData).setSeat(seat))
                .build();

        // 通知其他玩家自己碰
        Broadcast.broadcast(game, sc);

        EventPeng event = new EventPeng();
        event.game = game;
        event.sc = sc;

        eventBus.post(event);
        // // 发送通知
        // this.notifyObservers(FightConstant.FIGHT_PENG, sc, game);
    }

}
