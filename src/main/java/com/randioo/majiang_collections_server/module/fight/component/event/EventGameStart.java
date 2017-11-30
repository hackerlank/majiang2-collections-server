package com.randioo.majiang_collections_server.module.fight.component.event;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.eventbus.Event;

public class EventGameStart implements Event {
    public Game game;
}
