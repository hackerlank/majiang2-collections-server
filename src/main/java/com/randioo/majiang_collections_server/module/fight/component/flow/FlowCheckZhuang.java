package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.GlobleConstant;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.utils.RandomUtils;

@Component
public class FlowCheckZhuang implements Flow<Game> {

    @Override
    public void execute(Game game, String[] params) {
        if (game.envVars.Boolean(GlobleConstant.ARGS_ZHUANG)) {
            game.setZhuangSeat(0);
        } else {
            int zhuangGameRoleId = game.getZhuangSeat();
            // 如果没有庄家，则随机一个
            if (zhuangGameRoleId == -1) {
                int index = RandomUtils.getRandomNum(game.getRoleIdMap().size());
                game.setZhuangSeat(index);
            }
        }

        // 设置出牌玩家索引
        game.setCurrentRoleIdIndex(game.getZhuangSeat());
    }

}
