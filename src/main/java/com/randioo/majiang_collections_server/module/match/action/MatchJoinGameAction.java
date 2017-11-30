package com.randioo.majiang_collections_server.module.match.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.randioo.mahjong_public_server.protocol.Match.MatchJoinGameRequest;
import com.randioo.majiang_collections_server.entity.bo.Role;
import com.randioo.majiang_collections_server.module.match.service.MatchService;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.template.IActionSupport;

@Controller
@PTAnnotation(MatchJoinGameRequest.class)
public class MatchJoinGameAction implements IActionSupport {

    @Autowired
    private MatchService matchService;

    @Override
    public void execute(Object data, Object session) {
        MatchJoinGameRequest request = (MatchJoinGameRequest) data;
        Role role = (Role) RoleCache.getRoleBySession(session);
        String roomId = request.getRoomId();
        matchService.joinGame(role, roomId);
    }

}
