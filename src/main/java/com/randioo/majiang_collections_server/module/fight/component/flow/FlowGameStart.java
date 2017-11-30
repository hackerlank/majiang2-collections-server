package com.randioo.majiang_collections_server.module.fight.component.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.randioo.mahjong_public_server.protocol.Entity.GameConfigData;
import com.randioo.mahjong_public_server.protocol.Entity.GameState;
import com.randioo.majiang_collections_server.dao.GameRecordDao;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.bo.GameRecordData;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.event.EventGameStart;
import com.randioo.majiang_collections_server.module.fight.component.score.round.GameOverResult;
import com.randioo.randioo_server_base.processor.Flow;
import com.randioo.randioo_server_base.utils.TimeUtils;

@Component
public class FlowGameStart implements Flow<Game> {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private GameRecordDao gameRecordDao;

    @Override
    public void execute(Game game, String[] params) {
        // this.notifyObservers(FightConstant.GAME_START, null, game);
        game.logger.info("游戏开始");
        game.rule.initData(game);

        game.setGameState(GameState.GAME_START_START);
        game.beginNextRound = true;// 游戏开始时标记为开始,本回合结束时标记为结束
        GameConfigData config = game.getGameConfig();
        game.sendCard = 0;
        game.sendCardSeat = -1;
        game.roundStartTime = TimeUtils.getNowTime();

        // 卡牌初始化
        for (RoleGameInfo info : game.getRoleIdMap().values()) {
            // 杠标记附空
            info.isGang = false;
            // 清除手牌
            info.cards.clear();
            // 听牌清理
            info.tingCards.clear();
            // 清空已经组成的牌组
            info.showCardLists.clear();
            // 新拿的牌清空
            info.newCard = 0;
            // 清空每个人的花牌
            info.sendFlowrCards.clear();
            // 花计数清空
            info.flowerCount = 0;
            // 每个人摸得牌
            info.everybodyTouchCard = 0;
            // 清空听状态
            info.isTing = false;
            // 重置碰
            info.pengGuoCard = 0;
            // 吃碰的牌重置
            info.chiCard = 0;
            // 上一把的牌型list
            info.roundOverResult.huTypeList.clear();
            info.isAddFlowerState = false;
            // 胡牌数据清空
            info.roundCardsData = null;

            // 如果该玩家没有结果集,则创建结果集
            Map<String, GameOverResult> resultMap = game.getStatisticResultMap();
            if (!resultMap.containsKey(info.gameRoleId)) {
                GameOverResult result = new GameOverResult();
                resultMap.put(info.gameRoleId, result);
            }
            // 不是机器人要游戏记录
            if (info.roleId != 0) {
                gameRecordDao.insert(new GameRecordData(info.roleId));
            }

            // 重置发牌数据
            this.resetSendCard(game);
            // 剩余牌清空
            game.getRemainCards().clear();

            game.qiangGangCallCardList = null;
            game.checkOtherCardListSeats.clear();
            game.isLiuju = false;
            game.touchCardIsFlower = false;
            game.tingCardList = null;

            // 清空桌上的牌
            Map<Integer, List<Integer>> sendDesktopCardMap = game.getSendDesktopCardMap();
            for (int i = 0; i < config.getMaxCount(); i++) {
                if (!sendDesktopCardMap.containsKey(i)) {
                    List<Integer> sendList = new ArrayList<>();
                    sendDesktopCardMap.put(i, sendList);
                }
                sendDesktopCardMap.get(i).clear();
            }

            // 临时列表清空
            game.getCallCardLists().clear();
            game.getHuCallCardLists().clear();
            game.getAutoHuCallCardList().clear();

        }

        EventGameStart event = new EventGameStart();
        event.game = game;

        eventBus.post(game);
        // this.notifyObservers(FightConstant.FIGHT_GAME_START, game);
    }

    private void resetSendCard(Game game) {
        if (game.sendCardSeat == -1 && game.sendCard == 0) {
            return;
        }

        List<Integer> desktopCardList = game.getSendDesktopCardMap().get(game.sendCardSeat);
        desktopCardList.remove(desktopCardList.size() - 1);

        game.sendCard = 0;
        game.sendCardSeat = -1;
    }

}
