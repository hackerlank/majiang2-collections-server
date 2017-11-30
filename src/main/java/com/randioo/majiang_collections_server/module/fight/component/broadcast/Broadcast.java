package com.randioo.majiang_collections_server.module.fight.component.broadcast;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.protobuf.Message;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.randioo_server_base.utils.SessionUtils;

public class Broadcast {
    /**
     * 通知游戏内所有人
     * 
     * @param game
     * @param message
     * @author wcy 2017年11月28日
     */
    public static void broadcast(Game game, Message message) {
        Map<String, RoleGameInfo> map = game.getRoleIdMap();
        for (Map.Entry<String, RoleGameInfo> entrySet : map.entrySet()) {
            RoleGameInfo roleGameInfo = entrySet.getValue();

            SessionUtils.sc(roleGameInfo.roleId, message);
        }
    }

    /**
     * 通知游戏内除了roleId的所有人
     * 
     * @param game
     * @param message
     * @param roleId
     * @author wcy 2017年11月28日
     */
    public static void broadcastBesides(Game game, Message message, int... roleId) {
        Set<Integer> besideSet = new HashSet<>(roleId.length);
        for (int i = 0; i < roleId.length; i++) {
            besideSet.add(roleId[i]);
        }

        Map<String, RoleGameInfo> map = game.getRoleIdMap();
        for (Map.Entry<String, RoleGameInfo> entrySet : map.entrySet()) {
            RoleGameInfo roleGameInfo = entrySet.getValue();

            if (besideSet.contains(roleGameInfo.roleId)) {
                continue;
            }

            SessionUtils.sc(roleGameInfo.roleId, message);
        }
    }
}
