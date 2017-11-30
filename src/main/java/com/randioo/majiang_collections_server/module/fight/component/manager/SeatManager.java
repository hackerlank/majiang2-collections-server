package com.randioo.majiang_collections_server.module.fight.component.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;

@Component
public class SeatManager {
    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    /**
     * 获得下一个座位号
     * 
     * @param game
     * @param currentSeat
     * @return
     */
    public int next(Game game, int currentSeat) {
        int maxCount = game.getRoleIdList().size();
        int seat = currentSeat + 1;
        return seat == maxCount ? 0 : seat;
    }

    public int next(Game game) {
        return next(game, game.currentSeat);
    }

    /**
     * 获得前一个座位号
     * 
     * @param game
     * @param currentSeat
     * @return
     * @author wcy 2017年10月10日
     */
    public int previous(Game game, int currentSeat) {
        int maxCount = game.getRoleIdList().size();
        currentSeat--;
        return currentSeat < 0 ? maxCount - 1 : currentSeat;
    }

    public int previous(Game game) {
        return previous(game, game.currentSeat);
    }

    public int getSeatByRoleId(Game game, int roleId) {
        String gameRoleId = roleGameInfoManager.getGameRoleId(game, roleId);
        RoleGameInfo roleGameInfo = game.getRoleIdMap().get(gameRoleId);
        return roleGameInfo.seat;
    }

}
