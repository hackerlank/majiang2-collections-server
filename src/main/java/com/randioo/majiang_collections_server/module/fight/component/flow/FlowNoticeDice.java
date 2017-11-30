package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightCastDices;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.event.EventDices;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowNoticeDice implements Flow<Game> {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(Game game, String[] params) {
        SC sc = SC.newBuilder().setSCFightCastDices(SCFightCastDices.newBuilder().addAllDices(game.dice)).build();

        Broadcast.broadcast(game, sc);
        
        EventDices event = new EventDices();
        event.game = game;
        event.sc = sc;

        eventBus.post(event);
    }

}
