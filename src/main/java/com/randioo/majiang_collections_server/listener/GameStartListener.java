package com.randioo.majiang_collections_server.listener;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.randioo.majiang_collections_server.module.fight.component.event.EventGameStart;
import com.randioo.randioo_server_base.eventbus.Listener;

@Component
public class GameStartListener implements Listener {
    @Subscribe
    public void listenGameStart(EventGameStart event) {

    }
}
