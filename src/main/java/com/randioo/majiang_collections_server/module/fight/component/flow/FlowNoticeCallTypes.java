package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Entity.GameState;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightNoticeChooseCardList;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.cache.local.GameCache;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.event.EventNoticeCallType;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.majiang_collections_server.module.fight.component.manager.VerifyManager;
import com.randioo.majiang_collections_server.module.fight.component.parser.CardListPrototype;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.template.Function;
import com.randioo.randioo_server_base.utils.SessionUtils;

@Component
public class FlowNoticeCallTypes implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private CardListPrototype cardListPrototype;

    @Autowired
    private VerifyManager verifyManager;

    @Override
    public void execute(Game game, String[] params) {
        Map<Integer, SCFightNoticeChooseCardList.Builder> map = new HashMap<>();

        List<CallCardList> callCardLists = game.getCallCardLists();
        for (CallCardList callCardList : callCardLists) {
            SCFightNoticeChooseCardList.Builder builder = map.get(callCardList.masterSeat);
            if (builder == null) {
                builder = SCFightNoticeChooseCardList.newBuilder();
                map.put(callCardList.masterSeat, builder);
            }

            CardList cardList = callCardList.cardList;

            Class<? extends CardList> clazz = cardListPrototype.parse(cardList);

            Function parseCardListToProtoFunction = GameCache.getParseCardListToProtoFunctionMap().get(clazz);
            Function addProtoFunction = GameCache.getNoticeChooseCardListFunctionMap().get(clazz);

            Object cardListProtoData = parseCardListToProtoFunction.apply(callCardList.cardList);
            addProtoFunction.apply(builder, callCardList.cardListId, cardListProtoData);
        }
        // 校验器调整

        // 发送给对应的人
        for (Map.Entry<Integer, SCFightNoticeChooseCardList.Builder> entrySet : map.entrySet()) {
            int sendSeat = entrySet.getKey();
            SCFightNoticeChooseCardList.Builder builder = entrySet.getValue();

            RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, sendSeat);
            int roleId = roleGameInfo.roleId;
            SCFightNoticeChooseCardList scFightNoticeChooseCardList = builder.setTempGameCount(game.getSendCardCount())
                    .build();

            SC sc = SC.newBuilder().setSCFightNoticeChooseCardList(scFightNoticeChooseCardList).build(); // PengGangHu
                                                                                                         // SC

            // 校验器重置
            verifyManager.reset(roleGameInfo.verify);

            SessionUtils.sc(roleId, sc);

            if (game.getGameState() != GameState.GAME_START_START)
                break;

            EventNoticeCallType event = new EventNoticeCallType();
            event.game = game;
            event.roleGameInfo = roleGameInfo;
            event.sc = sc;
            event.sendSeat = sendSeat;

            eventBus.post(event);

            // this.notifyObservers(FightConstant.FIGHT_GANG_PENG_HU, game,
            // sendSeat, sc, roleGameInfo);
        }

    }

}
