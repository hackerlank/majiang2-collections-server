package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventCountDown implements Event {
    public Game game;
    public SC sc;
}
