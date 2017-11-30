package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.randioo.majiang_collections_server.comparator.CallCardListComparator;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Peng;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.processor.Flow;

public class FlowCheckOtherCardList implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private CallCardListComparator callCardListComparator;

    @Override
    public void execute(Game game, String[] params) {
        int card = game.sendCard;

        game.getCallCardLists().clear();
        game.getHuCallCardLists().clear();

        for (int seat : game.checkOtherCardListSeats) {

            RoleGameInfo info = roleGameInfoManager.getRoleGameInfoBySeat(game, seat);
            // 如果有百搭
            List<Class<? extends CardList>> checkList = game.rule.getOtherCardListSequence(info, game);

            game.logger.info("别人出一张牌，其他人要检测的牌: {}", info.cards);
            game.logger.info("要检测的: {}", checkList);
            this.checkOtherCallCardList(game, seat, card, checkList);
        }

        // 叫牌排序
        Collections.sort(game.getCallCardLists(), callCardListComparator);

        // 删除不能碰的CallCardList
        Iterator<CallCardList> it = game.getCallCardLists().iterator();
        while (it.hasNext()) {
            CallCardList item = it.next();
            if (item.cardList instanceof Peng) {
                Peng peng = (Peng) item.cardList;
                RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, item.masterSeat);
                if (peng.card == roleGameInfo.pengGuoCard) {
                    game.logger.info("删除了一个碰  {}", item.cardList);
                    it.remove();
                }
            }
        }
    }
}
