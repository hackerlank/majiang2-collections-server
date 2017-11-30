package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Fight.SCFightNoticeSeat;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowRandomSeat implements Flow<Game> {

    @Override
    public void execute(Game game, String[] params) {
        List<String> roleList = game.getRoleIdList();
        // 原来的list
        List<String> originalList = new ArrayList<>(roleList);
        // 打乱
        Collections.shuffle(roleList);
        SCFightNoticeSeat.Builder builder = SCFightNoticeSeat.newBuilder();
        for (int i = 0; i < roleList.size(); i++) {
            // 原来的seat
            int originalSeat = originalList.indexOf(roleList.get(i));
            builder.addSeat(originalSeat);
        }
        game.logger.info("座位号打乱前:   {}", originalList);
        game.logger.info("座位号打乱后:   {}", game.getRoleIdList());
        // 索引是现在的座位号，值是之前的座位号
        Broadcast.broadcast(game, SC.newBuilder().setSCFightNoticeSeat(builder).build());
    }

}
