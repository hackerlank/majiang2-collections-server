package com.randioo.majiang_collections_server.module.fight.component.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Fight.SCFightPointSeat;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.manager.SeatManager;
import com.randioo.randioo_server_base.processor.Flow;

@Component
public class FlowSeat implements Flow<Game> {

    @Autowired
    private SeatManager seatManager;

    @Override
    public void execute(Game game, String[] params) {
        int type = Integer.parseInt(params[0]);
        if (type == 1 || type == 2) {
            changeSeat(game, type);
        } else if (type == 3) {
            int targetSeat = Integer.parseInt(params[1]);
            game.currentSeat = targetSeat;
        }
        SC sc = SC.newBuilder().setSCFightPointSeat(SCFightPointSeat.newBuilder().setSeat(game.currentSeat)).build();
        // 通知其他人
        Broadcast.broadcast(game, sc);
        game.logger.info("当前指向: {}", game.currentSeat);
    }

    /**
     * @param game
     * @param type 1 下一个人 2 上一个人 3当前
     * @param targetSeat 目标座位
     * @author wcy 2017年10月29日
     */
    public void changeSeat(Game game, int type) {
        switch (type) {
        case 1:
            this.nextSeat(game);
            break;
        case 2:
            this.previousSeat(game);
            break;
        default:
            break;
        }
    }

    private void nextSeat(Game game) {
        int nextSeat = seatManager.next(game);
        game.currentSeat = nextSeat;
    }

    private void previousSeat(Game game) {
        int prevSeat = seatManager.previous(game);
        game.currentSeat = prevSeat;
    }

}
