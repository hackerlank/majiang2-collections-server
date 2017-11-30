package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventNoticeCallType implements Event {
    public Game game;
    public SC sc;
    public int sendSeat;
    public RoleGameInfo roleGameInfo;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventNoticeCallType [game=")
                .append(game)
                .append(", sc=")
                .append(sc)
                .append(", sendSeat=")
                .append(sendSeat)
                .append(", roleGameInfo=")
                .append(roleGameInfo)
                .append("]");
        return builder.toString();
    }

}
