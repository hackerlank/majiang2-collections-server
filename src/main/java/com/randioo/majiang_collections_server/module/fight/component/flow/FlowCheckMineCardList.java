package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.CardSort;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Hu;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowCheckMineCardList implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Override
    public void execute(Game game, String[] params) {
        int seat = game.getCurrentRoleIdIndex();
        RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, seat);

        // 清空临时卡牌
        game.getCallCardLists().clear();
        game.getHuCallCardLists().clear();

        if ((roleGameInfo.cards.size() - 2) % 3 != 0) {
            logger.error("牌的数量不够,不能自检");
        }

        List<Class<? extends CardList>> list = game.rule.getMineCardListSequence(roleGameInfo, game);
        // 检查杠胡卡牌
        this.checkMineCallCardList(game, game.getCurrentRoleIdIndex(), roleGameInfo.newCard, list);

        // this.noticeCountDown(game, 10);
    }

    /**
     * 检查叫碰杠胡的动作
     * 
     * @param game
     * @param hasGangPengHuSeatedIndex
     * @param card
     * @param list 需要获得的牌绿型
     * @author wcy 2017年6月14日
     */
    private void checkMineCallCardList(Game game, int hasGangPengHuSeatedIndex, int card,
            List<Class<? extends CardList>> list) {
        int currentRoleIdSeat = game.getCurrentRoleIdIndex();
        // 获得该卡组的人
        RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);

        game.logger.info("自己的手牌{},摸到的牌是{}", roleGameInfo.cards, roleGameInfo.newCard);
        // 填充卡组
        CardSort cardSort = new CardSort(5);
        List<CardList> cardLists = new ArrayList<>();

        List<Integer> cards = new ArrayList<>(roleGameInfo.cards);
        cards.add(card);

        cardSort.fillCardSort(cards);

        List<CallCardList> callCardLists = game.getCallCardLists();
        List<CallCardList> huCallCardLists = game.getHuCallCardLists();
        Map<Class<? extends CardList>, CardList> cardListMap = game.rule.getCardListMap();
        for (Class<? extends CardList> clazz : list) {

            CardList templateCardList = cardListMap.get(clazz);
            templateCardList.check(game, cardLists, cardSort, card, roleGameInfo.showCardLists, true);

            for (CardList cardList : cardLists) {
                cardList.setTargetSeat(currentRoleIdSeat);

                CallCardList callCardList = new CallCardList();
                callCardList.cardListId = callCardLists.size() + 1;
                callCardList.masterSeat = hasGangPengHuSeatedIndex;
                callCardList.cardList = cardList;

                callCardLists.add(callCardList);
                // 如果是胡放到另一个数组中
                if (cardList instanceof Hu)
                    huCallCardLists.add(callCardList);
            }

            cardLists.clear();
        }
        if (game.tingCardList != null) {
            callCardLists.add(game.tingCardList);
            game.tingCardList = null;
        }
    }

}
