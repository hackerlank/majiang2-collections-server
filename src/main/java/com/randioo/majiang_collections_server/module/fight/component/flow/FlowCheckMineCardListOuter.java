package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Chi;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.majiang_collections_server.module.fight.component.manager.SeatManager;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowCheckMineCardListOuter implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private SeatManager seatManager;

    @Override
    public void execute(Game game, String[] params) {
        // 清空临时卡牌
        game.getCallCardLists().clear();
        game.getHuCallCardLists().clear();

        if (game.sendCard == 0) {
            return;
        }
        RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
        int seat = game.getCurrentRoleIdIndex();
        int sendCard = game.sendCard;
        List<Class<? extends CardList>> list = game.rule.getOtherCardListSequence(roleGameInfo, game);
        List<Class<? extends CardList>> copyList = new ArrayList<>(list);
        // 暗杠时，sendcardseat不重置
        if (game.sendCardSeat != -1 && seatManager.next(game.sendCardSeat) == seat) {
            if (game.getGameConfig().getNoChi() == false) {
                copyList.add(Chi.class);
            }
        }
        game.logger.info("别人出的牌: {}", game.sendCard);
        game.logger.info("补花之后加上别人出的牌检测: {}", roleGameInfo.cards);
        game.logger.info("要检测的: {}", copyList);
        this.checkOtherCallCardList(game, seat, sendCard, copyList);
    }

}
