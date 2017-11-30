package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventPeng implements Event {
    public Game game;
    public SC sc;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventPeng [game=").append(game).append(", sc=").append(sc).append("]");
        return builder.toString();
    }

}
