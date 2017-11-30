package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventFightStart implements Event {
    public Game game;
    public SC scHide;
    public SC scShow;
    public RoleGameInfo roleGameInfo;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventFightStart [game=")
                .append(game)
                .append(", scHide=")
                .append(scHide)
                .append(", scShow=")
                .append(scShow)
                .append(", roleGameInfo=")
                .append(roleGameInfo)
                .append("]");
        return builder.toString();
    }

}
