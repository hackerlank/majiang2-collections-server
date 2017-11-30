package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;

public class EventNoticeFlowerCount {
    public SC sc;
    public Game game;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventNoticeFlowerCount [sc=").append(sc).append(", game=").append(game).append("]");
        return builder.toString();
    }

}
