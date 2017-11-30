package com.randioo.majiang_collections_server.module.fight.component.processor;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.randioo_server_base.processor.AbstractProcessor;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.processor.ICommandCallback;

/**
 * 麻将处理器
 * 
 * @author wcy 2017年11月28日
 *
 */
public class MajiangProcessor extends AbstractProcessor<Game, ICommandCallback<Game>> {

    @Override
    public ICommandCallback<Game> getICommandCallback(Game game) {
        return game.rule;
    }

    @Override
    public boolean forceExitLoop(Flow<Game> flow, String executedFlowCmd) {
        return executedFlowCmd.equals("FlowGameOver");
    }

}
