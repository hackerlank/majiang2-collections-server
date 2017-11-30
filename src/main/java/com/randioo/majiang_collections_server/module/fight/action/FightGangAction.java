package com.randioo.majiang_collections_server.module.fight.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.randioo.mahjong_public_server.protocol.Fight.FightGangRequest;
import com.randioo.majiang_collections_server.entity.bo.Role;
import com.randioo.majiang_collections_server.module.fight.service.FightService;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.template.IActionSupport;

@Controller
@PTAnnotation(FightGangRequest.class)
public class FightGangAction implements IActionSupport {

    @Autowired
    private FightService fightService;

    @Override
    public void execute(Object data, Object session) {
        FightGangRequest request = (FightGangRequest) data;
        Role role = (Role) RoleCache.getRoleBySession(session);
        fightService.gang(role, request.getTempGameCount(), request.getCallCardListId());
    }
}
