package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightCountdown;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.event.EventCountDown;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowNoticeCountdown implements Flow<Game> {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(Game game, String[] params) {
        int countdown = Integer.parseInt(params[0]);
        // 发送倒计时
        SC sc = SC.newBuilder().setSCFightCountdown(SCFightCountdown.newBuilder().setCountdown(countdown)).build();

        Broadcast.broadcast(game, sc);

        EventCountDown event = new EventCountDown();
        event.game = game;
        event.sc = sc;

        eventBus.post(event);

        // this.notifyObservers(FightConstant.FIGHT_COUNT_DOWN, sc, game);
    }

}
