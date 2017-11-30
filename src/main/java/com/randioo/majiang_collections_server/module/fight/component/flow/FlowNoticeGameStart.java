package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Entity.GameConfigData;
import com.randioo.mahjong_public_server.protocol.Entity.ScoreData;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightScore;
import com.randioo.mahjong_public_server.protocol.Fight.SCFightStart;
import com.randioo.mahjong_public_server.protocol.ServerMessage.SC;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.FightConstant;
import com.randioo.majiang_collections_server.module.fight.component.broadcast.Broadcast;
import com.randioo.majiang_collections_server.module.fight.component.event.EventFightStart;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.majiang_collections_server.module.fight.component.score.round.GameOverResult;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.utils.SessionUtils;

@Component
public class FlowNoticeGameStart implements Flow<Game> {

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(Game game, String[] params) {
        GameConfigData gameConfigData = game.getGameConfig();
        // 获得百搭牌
        int baida = game.rule.getBaidaCard(game);
        // 每个人的分数
        SCFightScore.Builder scFightScoreBuilder = SCFightScore.newBuilder();
        // 设置每个人的座位和卡牌的数量
        SCFightStart.Builder scFightStartBuilder = SCFightStart.newBuilder();
        scFightStartBuilder.setRemainCardCount(game.getRemainCards().size());
        scFightStartBuilder.setZhuangSeat(game.getZhuangSeat());
        scFightStartBuilder.setMaxRound(gameConfigData.getRoundCount());
        scFightStartBuilder.setCurrentRoundNum(game.getFinishRoundCount());
        scFightStartBuilder.setBaidaCard(baida);
        scFightStartBuilder.setFirstBaiDaCard(game.getFristBaidaCard());
        scFightStartBuilder.setRemainHuangFan(game.getHuangFanCount());

        for (int i = 0; i < gameConfigData.getMaxCount(); i++) {
            RoleGameInfo gameRoleInfo = game.getRoleIdMap().get(game.getRoleIdList().get(i));

            // 准备一下所有人的分数
            GameOverResult gameOverResult = game.getStatisticResultMap().get(gameRoleInfo.gameRoleId);
            scFightScoreBuilder.addScoreData(ScoreData.newBuilder().setScore(gameOverResult.score).setSeat(i));
        }
        SCFightStart scFightStart = scFightStartBuilder.build();

        // 发给玩家别人的卡组
        List<Integer> hideCards = new ArrayList<>(FightConstant.EVERY_INIT_CARD_COUNT);

        // 发送给每个玩家
        for (int i = 0; i < gameConfigData.getMaxCount(); i++) {
            RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, seat);
            SCFightStart.Builder hideBuilder = scFightStart.toBuilder();
            SCFightStart.Builder showBuilder = scFightStart.toBuilder();
            // 隐藏别人的卡组
            this.hideOtherGameRoleInfoCards(roleGameInfo, hideCards, hideBuilder, game);
            this.playbackGameInfoCards(showBuilder, game);

            SC scHide = SC.newBuilder().setSCFightStart(hideBuilder).build();
            SC scShow = SC.newBuilder().setSCFightStart(showBuilder).build();
            // 通知所有人游戏开始，并把自己的牌告诉场上的玩家
            SessionUtils.sc(roleGameInfo.roleId, scHide);

//            this.notifyObservers(FightConstant.FIGHT_START, scHide, game, roleGameInfo, scShow);
            
            EventFightStart e = new EventFightStart();
            e.game = game;
            e.roleGameInfo = roleGameInfo;
            e.scShow = scShow;
            e.scHide = scHide;

            eventBus.post(e);

            // 每次的隐藏卡组都不一样
            hideCards.clear();
        }
        // 通知一下所有人的分数
        SC fightScoreSC = SC.newBuilder().setSCFightScore(scFightScoreBuilder).build();
        noticePointSeat(game, game.getZhuangSeat());

        Broadcast.broadcast(game, fightScoreSC);
        this.notifyObservers(FightConstant.FIGHT_SCORE, fightScoreSC, game);
    }
}
