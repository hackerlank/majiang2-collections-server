package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventAddFlower implements Event {
    public SC sc;
    public SC showSC;
    public Game game;
    public RoleGameInfo roleGameInfo;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventAddFlower [sc=")
                .append(sc)
                .append(", showSC=")
                .append(showSC)
                .append(", game=")
                .append(game)
                .append(", roleGameInfo=")
                .append(roleGameInfo)
                .append("]");
        return builder.toString();
    }

}
