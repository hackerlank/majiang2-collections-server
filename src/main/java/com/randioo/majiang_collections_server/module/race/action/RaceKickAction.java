package com.randioo.majiang_collections_server.module.race.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.randioo.majiang_collections_server.module.race.service.RaceService;
import com.randioo.randioo_server_base.annotation.PTStringAnnotation;
import com.randioo.randioo_server_base.template.IActionSupport;
import com.sun.net.httpserver.HttpExchange;

@Controller
@PTStringAnnotation("race_kick")
public class RaceKickAction implements IActionSupport {

	@Autowired
	private RaceService raceService;

	@Override
	public void execute(Object data, Object session) {
		HttpExchange request = (HttpExchange) data;
		String roleIdStr = (String) request.getAttribute("raceId");
		int raceId = Integer.parseInt(roleIdStr);
		String account = (String) request.getAttribute("account");
		raceService.kickRace(raceId, account);
	}

}
