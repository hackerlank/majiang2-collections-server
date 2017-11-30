package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowCancelPeng implements Flow<Game>{

    @Override
    public void execute(Game game, String[] params) {
        
    }

}
