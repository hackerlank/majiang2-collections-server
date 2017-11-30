package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.randioo.majiang_collections_server.GlobleConstant;
import com.randioo.majiang_collections_server.comparator.CardComparator;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.FightConstant;
import com.randioo.majiang_collections_server.module.fight.component.MajiangRule;
import com.randioo.majiang_collections_server.module.fight.component.dispatch.CardPart;
import com.randioo.majiang_collections_server.module.fight.component.dispatch.ClientDispatcher;
import com.randioo.majiang_collections_server.module.fight.component.dispatch.DebugDispatcher;
import com.randioo.majiang_collections_server.module.fight.component.dispatch.Dispatcher;
import com.randioo.majiang_collections_server.module.fight.component.dispatch.RandomDispatcher;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.processor.Flow;

public class FlowDispatchCard implements Flow<Game> {

    @Autowired
    private ClientDispatcher clientDispatcher;
    @Autowired
    private DebugDispatcher debugDispatcher;
    @Autowired
    private RandomDispatcher randomDispatcher;
    @Autowired
    private CardComparator cardComparator;
    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Override
    public void execute(Game game, String[] params) {
        int partCount = Integer.parseInt(params[0]);
        int everyPartCount = Integer.parseInt(params[1]);
        // 赋值所有牌,然后随机一个个取
        List<Integer> remainCards = game.getRemainCards();
        MajiangRule rule = game.rule;
        List<Integer> allCards = rule.getCards();
        remainCards.addAll(allCards);

        // 选用指定的分牌器
        Dispatcher dispatcher = null;
        // if (GlobleMap.Boolean(GlobleConstant.ARGS_DISPATCH)) {
        if (game.envVars.Boolean(GlobleConstant.ARGS_DISPATCH)) {
            // if (GlobleMap.Boolean(GlobleConstant.ARGS_CLIENT_DISPATCH)) {
            if (game.envVars.Boolean(GlobleConstant.ARGS_CLIENT_DISPATCH)) {
                dispatcher = clientDispatcher;
            } else {
                dispatcher = debugDispatcher;
            }
        } else {
            dispatcher = randomDispatcher;
        }

        List<CardPart> cardParts = dispatcher.dispatch(game, remainCards, partCount, everyPartCount);

        for (int i = 0; i < game.getRoleIdMap().size(); i++) {
            RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, i);
            CardPart cardPart = cardParts.get(i);
            roleGameInfo.cards.addAll(cardPart);
        }

        // 每个玩家卡牌排序
        int baidaCard = rule.getBaidaCard(game);
        cardComparator.getBaidaCardSet().add(baidaCard);
        for (RoleGameInfo roleGameInfo : game.getRoleIdMap().values()) {
            Collections.sort(roleGameInfo.cards, cardComparator);
            game.logger.info("玩家手牌 {} {}", roleGameInfo.gameRoleId, roleGameInfo.cards);
        }
        game.logger.info("剩余没有摸的牌 {}", game.getRemainCards());

    }

}
