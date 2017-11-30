package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.GlobleConstant;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.randioo_server_base.config.GlobleMap;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowTouchCard implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Override
    public void execute(Game game, String[] params) {
        RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);

        List<Integer> remainCards = game.getRemainCards();

        if (GlobleMap.Boolean(GlobleConstant.ARGS_ARTIFICIAL)) {
            // final Game finalGame = game;
            // final RoleGameInfo finalRoleGameInfo = roleGameInfo;
            // Thread t = new Thread(new Runnable() {
            // public void run() {
            // input_TouchCard(finalGame, finalRoleGameInfo);
            // noticeTouchCard(finalGame);
            // };
            //
            // });
            // t.start();

            input_TouchCard(game, roleGameInfo);
            // noticeTouchCard(game);
        } else {
            // 如果客户端要求自己摸牌
            // if (GlobleMap.Boolean(GlobleConstant.ARGS_CLIENT_TOUCH_CARD)) {

            roleGameInfo.newCard = remainCards.remove(0);

            // this.noticeTouchCard(game);
        }

        // 每个人都存一下
        for (RoleGameInfo info : game.getRoleIdMap().values()) {
            info.everybodyTouchCard = roleGameInfo.newCard;
        }
    }

}
