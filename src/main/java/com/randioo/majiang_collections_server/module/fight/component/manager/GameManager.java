package com.randioo.majiang_collections_server.module.fight.component.manager;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.cache.local.GameCache;
import com.randioo.majiang_collections_server.entity.bo.Game;

@Component
public class GameManager {
    public Game getGameById(int gameId) {
        return GameCache.getGameMap().get(gameId);
    }

    public Game getGameByRoomId(String roomId) {
        Integer gameId = GameCache.getGameLockStringMap().get(roomId);
        if (gameId == null) {
            return null;
        }
        return getGameById(gameId);
    }
}
