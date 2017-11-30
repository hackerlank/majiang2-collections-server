package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.processor.flow.Flow;

@Component
public class FlowRoundOver implements Flow<Game> {
    
    @Override
    public void execute(Game game, String[] params) {

    }
}
