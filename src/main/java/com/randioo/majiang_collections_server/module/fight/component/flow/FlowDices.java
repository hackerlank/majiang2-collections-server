package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.HuangFanSetter;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowDices implements Flow<Game> {

    @Autowired
    private HuangFanSetter huangFanSetter;

    @Override
    public void execute(Game game, String[] params) {
        huangFanSetter.set(game);
    }

}
