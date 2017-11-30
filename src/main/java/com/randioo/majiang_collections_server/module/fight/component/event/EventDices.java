package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventDices implements Event {
    public SC sc;
    public Game game;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventDices [sc=").append(sc).append(", game=").append(game).append("]");
        return builder.toString();
    }

}
