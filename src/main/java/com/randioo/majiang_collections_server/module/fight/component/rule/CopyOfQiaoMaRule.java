package com.randioo.majiang_collections_server.module.fight.component.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.randioo.mahjong_public_server.protocol.Entity.GameConfigData;
import com.randioo.mahjong_public_server.protocol.Entity.HuType;
import com.randioo.majiang_collections_server.entity.bo.Game;
import com.randioo.majiang_collections_server.entity.po.CallCardList;
import com.randioo.majiang_collections_server.entity.po.RoleGameInfo;
import com.randioo.majiang_collections_server.module.fight.component.CardChecker;
import com.randioo.majiang_collections_server.module.fight.component.MajiangRule;
import com.randioo.majiang_collections_server.module.fight.component.SeatIndexCalc;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Chi;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Gang;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Hu;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Peng;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Step5Hu;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.baida.BaidaChi;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.baida.BaidaHu;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.baida.BaidaPeng;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.qiaoma.QiaomaGang;
import com.randioo.majiang_collections_server.module.fight.component.manager.RoleGameInfoManager;
import com.randioo.majiang_collections_server.module.fight.component.manager.SeatManager;
import com.randioo.majiang_collections_server.module.fight.component.manager.VerifyManager;
import com.randioo.majiang_collections_server.module.fight.component.score.round.qiaoma.QiaomaHuTypeCalculator;
import com.randioo.majiang_collections_server.module.fight.service.FightService;
import com.randioo.majiang_collections_server.util.Lists;
import com.randioo.randioo_server_base.collections8.Maps8;
import com.randioo.randioo_server_base.utils.ReflectUtils;

/**
 * 敲麻规则
 * 
 * @author wcy 2017年8月21日
 *
 */

@Component
public class CopyOfQiaoMaRule extends MajiangRule {
    private Logger logger = LoggerFactory.getLogger(CopyOfQiaoMaRule.class);

    @Autowired
    private FightService fightService;

    @Autowired
    private RoleGameInfoManager roleGameInfoManager;

    @Autowired
    private CardChecker cardChecker;

    @Autowired
    private SeatIndexCalc seatIndexCalc;

    @Autowired
    private SeatManager seatManager;

    @Autowired
    private QiaomaHuTypeCalculator typeCalc;
    @Autowired
    private BaidaHu baidaHu;

    @Autowired
    private VerifyManager verifyManager;

    public static final int TONG = 1;
    public static final int TIAO = 2;
    public static final int WAN = 3;
    public static final int ZHONG = 8;
    public static final int DONG = 401;
    public static final int BEI = 701;
    public static final int SPRING = 1101;

    public final static List<Integer> NUM_CARDS = Arrays.asList(1, 2, 3);
    public final static List<Integer> HUA_CARDS = Arrays.asList(8, 9, 10, 11);
    public final static List<Integer> Feng_CARDS = Arrays.asList(4, 5, 6, 7);
    // 用于判断跑百搭
    public final static List<Integer> TEST_BAI_DAI = Arrays.asList(101, 102, 103, 104, 105, 106, 107, 108, 109, 201,
            202, 203, 204, 205, 206, 207, 208, 209, 301, 302, 303, 304, 305, 306, 307, 308, 309, 401, 501, 601, 701);
    // 用于产生百搭牌
    public final static List<Integer> CARDS = Arrays.asList(101, 102, 103, 104, 105, 106, 107, 108, 109, 201, 202, 203,
            204, 205, 206, 207, 208, 209, 301, 302, 303, 304, 305, 306, 307, 308, 309, 401, 501, 601, 701, 801, 901,
            1001, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108);
    private List<Integer> fengCards = Arrays.asList(401, 501, 601, 701);
    private List<Integer> flowerCards = Arrays.asList(801, 901, 1001, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108);
    private final List<Integer> cards = Arrays.asList( //
            //
            101, 102, 103, 104, 105, 106, 107, 108, 109, // 条
            101, 102, 103, 104, 105, 106, 107, 108, 109, // 条
            101, 102, 103, 104, 105, 106, 107, 108, 109, // 条
            101, 102, 103, 104, 105, 106, 107, 108, 109, // 条

            201, 202, 203, 204, 205, 206, 207, 208, 209, // 筒
            201, 202, 203, 204, 205, 206, 207, 208, 209, // 筒
            201, 202, 203, 204, 205, 206, 207, 208, 209, // 筒
            201, 202, 203, 204, 205, 206, 207, 208, 209, // 筒

            301, 302, 303, 304, 305, 306, 307, 308, 309, // 万
            301, 302, 303, 304, 305, 306, 307, 308, 309, // 万
            301, 302, 303, 304, 305, 306, 307, 308, 309, // 万
            301, 302, 303, 304, 305, 306, 307, 308, 309, // 万
            401, 401, 401, 401, // 东
            501, 501, 501, 501, // 南
            601, 601, 601, 601, // 西
            701, 701, 701, 701, // 北
            801, 801, 801, 801, // 中
            901, 901, 901, 901, // 发
            1001, 1001, 1001, 1001, // 白
            1101, // 春
            1102, // 夏
            1103, // 秋
            1104, // 冬
            1105, // 梅
            1106, // 兰
            1107, // 竹
            1108// 菊
    // B9,// 财神
    // BA,// 猫
    // BB,// 老鼠
    // BC,// 聚宝盆
    // C1,// 白搭
    // C1,// 白搭
    // C1,// 白搭
    // C1,// 白搭
    );

    /** 已经听了的人的座位 */
    public static String tingSeatTag = "tingSeat";
    /** 杠过的人 */
    public static String gangSeatTag = "gangSeat";

    // @Override
    // public List<MajiangStateEnum> beforeStateExecute(Game ruleableGame,
    // MajiangStateEnum majiangStateEnum,
    // int currentSeat) {
    // Game game = (Game) ruleableGame;
    // Stack<String> operations = ruleableGame.getCmdStack();
    // List<MajiangStateEnum> list = new ArrayList<>();
    // switch (majiangStateEnum) {
    // case STATE_TOUCH_CARD:
    // game.logger.info("流局判断 剩余牌数{}", game.getRemainCards().size());
    // if (game.getRemainCards().size() == 0) {// 流局
    // game.isLiuju = true;
    // operations.clear();
    // if (game.getGameConfig().getHuangFan()) { // 带荒番
    // game.setHuangFanCount(game.getHuangFanCount() + 1);
    // }
    // list.add(MajiangStateEnum.STATE_ROUND_OVER);
    // }
    // break;
    // case STATE_SC_SEND_CARD: {
    // RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
    // if (roleGameInfo.isTing) {
    // Stack<String> stack = game.getCmdStack();
    // game.logger.info("remove previous  {}", stack);
    // stack.remove(MajiangStateEnum.STATE_SC_SEND_CARD);
    // game.logger.info("remove STATE_SC_SEND_CARD");
    // // 自动出牌
    // list.add(MajiangStateEnum.STATE_AUTO_SEND_CARD);
    // }
    // }
    // break;
    // case STATE_ROUND_OVER:
    // operations.clear();
    // list.add(MajiangStateEnum.STATE_ROUND_OVER);
    // break;
    //
    // default:
    // break;
    // }
    // return list;
    // }

    @SuppressWarnings("unchecked")
    @Override
    public void initData(Game game) {
        Maps8.putIfAbsent(game.dataStructure, tingSeatTag, new HashSet<Integer>());
        Maps8.putIfAbsent(game.dataStructure, gangSeatTag, new HashSet<Integer>());

        ((Set<Integer>) game.dataStructure.get(tingSeatTag)).clear();
        ((Set<Integer>) game.dataStructure.get(gangSeatTag)).clear();
    }

    @Override
    public List<String> afterCommandExecute(Game game, String flowName, String[] params) {

        List<String> list = new ArrayList<>();

        switch (flowName) {
        case "FlowReady":
            list.add(fightService.checkAllReady(game) ? "FlowGameStart" : WAIT);
            break;
        case "FlowGameStart": {
            game.getCmdStack().clear();
            if (game.getGameConfig().getRandomSeat() && game.getFinishRoundCount() == 0) {
                list.add("FlowRandomSeat");
            }
            list.add("FlowDices");// 掷骰子
            if (game.getGameConfig().getHuangFan()) {// 是否带荒番
                list.add("FlowNoticeDice");
            }
            list.add("FlowCheckZhuang");
            list.add("FlowDispatchCard " + game.getRoleIdMap().size() + " 13");
            list.add("FlowNoticeGameStart");
        }
            break;

        case "FlowNoticeGameStart": {
            RoleGameInfo currentRoleGameInfo = roleGameInfoManager.current(game);

            if (containsFlowers(currentRoleGameInfo)) {
                list.add("FlowAddFlowers");
                list.add("FlowCheckMineCardListOuter");
            } else {
                list.add("FlowTouchCard");
                list.add("FlowNoticeTouchCard");
                list.add("FlowCheckMineCardList");
                list.add("FlowNoticeSendCard");
                list.add(WAIT);
                list.add("FlowSeat 1");// 下一个人,开始进入循环
            }
        }
            break;

        case "FlowSeat": {
            RoleGameInfo current = roleGameInfoManager.current(game);
            // 如果有花则进行补花
            if (this.containsFlowers(current)) {
                list.add("FlowAddFlowers");// 先尝试补花
                // 补完花之后发现不符合胡牌的数量,这结束游戏
                Set<Integer> permitCardsCount = Sets.newHashSet(2, 5, 8, 11, 14);
                if (permitCardsCount.contains(current.cards.size())) {
                    list.add("FlowCheckMineCardListOuter");// 外检
                } else {
                    list.add("FlowRoundOver");
                }
            }

            if (game.getRemainCards().size() == 0) {// 如果没有牌了,则结束游戏
                list.add("FlowRoundOver");
                break;
            }
            // 摸牌
            list.add("FlowTouchCard");
            list.add("FlowNoticeTouchCard");// 通知别人我摸了牌
            // 检查自己摸得牌是否有牌型
            list.add("FlowCheckMineCardList");
            // 通知出牌
            list.add("FlowNoticeSendCard");
            list.add(WAIT);

            list.add("FlowSeat 1");// 下一个人

        }
            break;
        case "FlowRoleSendCard": { // 出牌后

            list.add("FlowCheckOtherCardList");
            // list.add(MajiangStateEnum.STATE_CHECK_OTHER_CARDLIST);
            // 添加需要检测的人
            game.checkOtherCardListSeats.clear();
            int next = seatManager.next(game);
            // 当前指针还在出牌人上
            int seat = game.currentSeat;
            for (int i = 0; i < game.getRoleIdList().size(); i++) {
                RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, i);
                boolean isContainsFlowers = containsFlowers(roleGameInfo);
                // 如果我是下家并且有花，则跳过我
                if (seat == i || (next == i && isContainsFlowers)) {
                    continue;
                }
                game.checkOtherCardListSeats.add(i);
            }
        }
            break;
        case "FlowNoticeTouchCard": {// 通知摸牌后

            // 通知摸牌后,检查摸到的是否是花
            boolean isFlower = game.touchCardIsFlower;
            if (isFlower) {// 先把通知发出去后再加流程
                list.add("FlowFlowerScore");

                list.add("FlowTouchCard");
                list.add("FlowNoticeTouchCard");
            }

        }
            break;
        case "FlowTouchCard": {// 摸牌后
            RoleGameInfo current = roleGameInfoManager.current(game);
            int card = current.newCard;

            boolean isFlower = this.isFlower(game, card);
            game.touchCardIsFlower = isFlower;
            // 给发牌通知赋值
            for (RoleGameInfo roleGameInfo : game.getRoleIdMap().values()) {
                if (current.gameRoleId.equals(roleGameInfo.gameRoleId) || isFlower) {
                    roleGameInfo.everybodyTouchCard = card;
                } else {
                    roleGameInfo.everybodyTouchCard = 0;
                }
            }
        }
            break;
        case "FlowCheckMineCardList": {// 检查自己
            RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
            if (game.getHuCallCardLists().size() > 0) {// 有胡必胡
                // 获得当前角色的座位号
                List<String> roleIdList = game.getRoleIdList();
                int seat = roleIdList.indexOf(roleGameInfo.gameRoleId);
                game.getAutoHuCallCardList().clear();
                if (roleGameInfo.isTing) {
                    List<CallCardList> huList = game.getHuCallCardLists();
                    // 判断胡
                    for (CallCardList item : huList) {
                        if (seat == item.masterSeat) {
                            game.getAutoHuCallCardList().add(item);
                            list.add("FlowAutoHu");
                            list.add(WAIT);
                            break;
                        }
                    }
                }
            } else {// 没有胡
                if (game.getCallCardLists().size() > 0) {
                    if (roleGameInfo.isTing) {
                        // 删除影响牌型的杠
                        deleteGang(game);
                    }
                    if (game.getCallCardLists().size() > 0) {
                        // list.addAll(noticeCardListProcesses);
                        list.add("FlowNoticeCallTypes");
                        list.add(WAIT);
                    } else {
                        // 补花后自检时杠了，加上别人出的牌检测时又杠了的情况
                        Stack<String> stack = game.getCmdStack();
                        if (stack.size() == 0) {
                            list.add("FlowNoticeSendCard");
                            list.add(WAIT);
                            // list.addAll(sendCardProcesses);
                            // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                            list.add("FlowSeat 1");
                        }
                    }
                } else {
                    // 补花后自检时杠了，加上别人出的牌检测时又杠了的情况
                    Stack<String> stack = game.getCmdStack();
                    if (stack.size() == 0) {
                        // list.addAll(sendCardProcesses);
                        list.add("FlowNoticeSendCard");
                        list.add(WAIT);
                        // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                        list.add("FlowSeat 1");
                    }
                }
            }
        }
            break;
        case "FlowCheckMineCardListOuter": {// 加上别人的牌检查自己
            if (game.getCallCardLists().size() > 0) {
                // list.addAll(noticeCardListProcesses);
                list.add("FlowNoticeCallTypes");
                list.add(WAIT);
            } else {
                // 进入后 以下就是正常流程了
                RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
                roleGameInfo.isAddFlowerState = false;
                int size = roleGameInfo.cards.size();
                int cardListSize = roleGameInfo.showCardLists.size();
                int totalSize = size + cardListSize * 3;
                totalSize += roleGameInfo.newCard == 0 ? 0 : 1;
                // 牌数量不够，就要继续摸牌，否则直接出牌
                if (totalSize < 14) {
                    list.add("FlowTouchCard");
                    list.add("FlowNoticeTouchCard");
                    list.add("FlowCheckMineCardList");
                    list.add("FlowNoticeSendCard");
                    list.add(WAIT);
                    list.add("FlowSeat 1");
                    // list.addAll(touchCardProcesses);
                    // list.addAll(sendCardProcesses);
                    // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                } else {
                    list.add("FlowNoticeSendCard");
                    list.add(WAIT);
                    list.add("FlowSeat 1");
                    // list.addAll(sendCardProcesses);
                    // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                }
            }
        }
            break;
        case "FlowCheckOtherCardList": { // 出牌，检测其他人的杠碰吃胡后
            // 下面的if要不要执行
            boolean isContinue = true;
            if (game.getHuCallCardLists().size() > 0) {
                List<CallCardList> huList = game.getHuCallCardLists();
                game.getAutoHuCallCardList().clear();
                // 遍历huCardList
                Iterator<CallCardList> it = huList.iterator();
                while (it.hasNext()) {
                    CallCardList item = it.next();
                    int masterSeat = item.masterSeat;
                    RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, masterSeat);
                    if (roleGameInfo.isTing) {
                        // 进入自动胡后下面就不用执行了
                        isContinue = false;
                        game.getAutoHuCallCardList().add(item);
                        list.add("FlowAutoHu");
                        list.add(WAIT);
                        // list.add(MajiangStateEnum.STATE_AUTO_HU);
                        // list.add(MajiangStateEnum.STATE_WAIT_OPERATION);
                    } else {
                        // 如果没进入听状态,把该item从HuCallCardLists和CallCardLists删除
                        it.remove();
                        game.getCallCardLists().remove(item);
                    }
                }
            }
            if (isContinue) {
                if (game.getCallCardLists().size() > 0) {
                    // 删除影响牌型的杠
                    deleteGang(game);
                    // 删除杠之后还要在判断一次
                    if (game.getCallCardLists().size() > 0) {
                        list.add("FlowNoticeCallTypes");
                        list.add(WAIT);
                        // list.addAll(noticeCardListProcesses);
                    } else {
                        // 顺到下一家，下一家杠后，出牌，之后断了
                        if (game.getCmdStack().size() == 0) {
                            list.add("FlowSeat 1");
                        }
                    }
                } else {
                    // 顺到下一家，下一家杠后，出牌，之后断了
                    if (game.getCmdStack().size() == 0) {
                        // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                        list.add("FlowSeat 1");
                    }
                }
            }
        }
            break;
        case "FlowRoleChosenCardList": {// 选择后
            // 如有栈中有CHECK_MINE_CARDLIST_OUTER，说明在补花流程中
            boolean isAddFlower = game.getCmdStack().contains("FlowCheckMineCardListOuter");
            RoleGameInfo currentRoleGameInfo = roleGameInfoManager.current(game);

            // 操作过期
            pass(game);

            // 获得第一个人的卡组
            List<CallCardList> callCardLists = game.getCallCardLists();
            if (callCardLists.size() > 0) {
                if (!isAddFlower) {
                    currentRoleGameInfo.isAddFlowerState = false;
                }
                CallCardList callCardList = game.getCallCardLists().get(0);
                RoleGameInfo callRoleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, callCardList.masterSeat);
                CardList cardList = callCardList.cardList;
                if (cardList instanceof Peng) {
                    list.add("FlowPeng");
                    list.add("FlowSeat 3 " + callCardList.masterSeat);

                    list.add("FlowFlowerScore");
                    // list.add(MajiangStateEnum.STATE_PENG);
                    // list.add(MajiangStateEnum.STATE_JUMP_SEAT);
                    // list.add(MajiangStateEnum.STATE_FLOWER_SCORE_CHANGE);
                    // list.add(MajiangStateEnum.STATE_SEND_CHECK_TING);
                    // list.add(MajiangStateEnum.STATE_WAIT_OPERATION);
                    // 如果有花，碰过后补花
                    if (containsFlowers(callRoleGameInfo)) {
                        Stack<String> stack = game.getCmdStack();
                        stack.remove("FlowSeat 1");
                        list.add("FlowAddFlowers");
                        list.add("FlowCheckMineCardListOuter");
                        // list.addAll(addFlowersProcess);
                    } else {
                        list.add("FlowNoticeSendCard");
                        list.add(WAIT);
                        // list.addAll(sendCardProcesses);
                    }
                } else if (cardList instanceof Gang) {
                    Gang gang = (Gang) cardList;

                    int seat = callCardList.masterSeat;
                    game.checkOtherCardListSeats.clear();
                    for (int i = 0; i < game.getRoleIdList().size(); i++) {
                        if (seat != i) {
                            game.checkOtherCardListSeats.add(i);
                        }
                    }

                    boolean hasTingRoleGameInfo = false;
                    int size = game.getRoleIdList().size();
                    for (int i = 0; i < size; i++) {
                        if (seat == i) {
                            continue;
                        }
                        RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, i);
                        if (roleGameInfo.isTing) {
                            hasTingRoleGameInfo = true;
                            break;
                        }
                    }
                    if (hasTingRoleGameInfo && gang.peng != null && fightService.checkQiangGang(game)) {
                        // 显示已经杠了
                        // list.add(MajiangStateEnum.STATE_GANG);
                        list.add("FlowGang");

                        List<CallCardList> huList = game.getHuCallCardLists();
                        game.getAutoHuCallCardList().clear();
                        Iterator<CallCardList> it = huList.iterator();
                        while (it.hasNext()) {
                            CallCardList item = it.next();
                            int masterSeat = item.masterSeat;
                            RoleGameInfo roleGameInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, masterSeat);
                            if (roleGameInfo.isTing) {
                                game.getAutoHuCallCardList().add(item);
                                // list.add(MajiangStateEnum.STATE_AUTO_HU);
                                // list.add(MajiangStateEnum.STATE_WAIT_OPERATION);
                                list.add("FlowAutoHu");
                                list.add(WAIT);
                            } else {
                                // 如果没进入听状态,把该item从HuCallCardLists和CallCardLists删除
                                game.logger.info("没进入听，删除一个CallCardList和对应的HuCallCardList");
                                it.remove();
                                game.getCallCardLists().remove(item);
                                game.qiangGangCallCardList = null;
                            }
                        }

                        // list.addAll(noticeCardListProcesses);
                    } else {
                        // list.add(MajiangStateEnum.STATE_GANG);
                        list.add("FlowGang");
                        // 如果需要跳转,则要填上出牌流程，实质上和下一家的流程差不多
                        if (game.getCurrentRoleIdIndex() != callCardList.masterSeat) {
                            list.add("FlowSeat 3 " + callCardList.masterSeat);
                            list.add("FlowFlowerScore");
                            list.add("FlowGangScore");
                            // list.add(MajiangStateEnum.STATE_JUMP_SEAT);
                            // list.add(MajiangStateEnum.STATE_FLOWER_SCORE_CHANGE);
                            // list.add(MajiangStateEnum.STATE_GANG_CAL_SCORE);
                            // 如果有花，杠过后补花
                            if (containsFlowers(callRoleGameInfo)) {
                                // Stack<MajiangStateEnum> stack =
                                // game.getOperations();
                                Stack<String> stack = game.getCmdStack();
                                stack.remove("FlowSeat 1");
                                // stack.remove(MajiangStateEnum.STATE_NEXT_SEAT);
                                list.add("FlowAddFlowers");
                                list.add("FlowCheckMineCardListOuter");
                                // list.addAll(addFlowersProcess);
                            } else {
                                list.add("FlowTouchCard");
                                list.add("FlowNoticeTouchCard");
                                list.add("FlowCheckMineCardList");
                                // list.addAll(touchCardProcesses);
                                if (!isAddFlower) {
                                    list.add("FlowNoticeSendCard");
                                    list.add(WAIT);
                                    // list.addAll(sendCardProcesses);
                                }
                            }
                        } else {
                            // list.add(MajiangStateEnum.STATE_FLOWER_SCORE_CHANGE);
                            // list.add(MajiangStateEnum.STATE_GANG_CAL_SCORE);
                            // list.addAll(touchCardProcesses);
                            list.add("FlowFlowerScore");
                            list.add("FlowGangScore");
                            list.add("FlowTouchCard");
                            list.add("FlowNoticeTouchCard");
                            list.add("FlowCheckMineCardList");
                        }
                    }
                } else if (cardList instanceof Chi) {
                    list.add("FlowChi");
                    list.add("FlowSeat 3 " + callCardList.masterSeat);
                    list.add("FlowNoticeSendCard");
                    list.add(WAIT);
                    // list.add(MajiangStateEnum.STATE_CHI);
                    // list.add(MajiangStateEnum.STATE_JUMP_SEAT);
                    // list.addAll(sendCardProcesses);
                } else if (cardList instanceof Hu) {
                    // 如果前面是抢杠,则还原杠变为碰
                    if (game.qiangGangCallCardList != null) {
                        list.add("FlowCancelPeng");
                        // list.add(MajiangStateEnum.STATE_RECOVERY_PENG);
                    }
                    list.add("FlowHu");
                    // list.add(MajiangStateEnum.STATE_HU);
                }

            } else {
                // Stack<MajiangStateEnum> stack = game.getOperations();
                Stack<String> stack = game.getCmdStack();
                // 补花流程中,栈中无CHECK_MINE_CARDLIST_OUTER，有操作，但是选择了过
                if (currentRoleGameInfo.isAddFlowerState && !isAddFlower) {
                    currentRoleGameInfo.isAddFlowerState = false;
                    list.add("FlowTouchCard");
                    list.add("FlowNoticeTouchCard");
                    list.add("FlowCheckMineCardList");
                    list.add("FlowNoticeSendCard");
                    list.add(WAIT);
                    list.add("FlowSeat 1");
                    // list.addAll(touchCardProcesses);
                    // list.addAll(sendCardProcesses);
                    // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                }
                if (game.qiangGangCallCardList != null) {
                    // 如果需要跳转,则要填上出牌流程，实质上和下一家的流程差不多
                    list.add("FlowGangScore");
                    list.add("FlowTouchCard");
                    list.add("FlowNoticeTouchCard");
                    list.add("FlowCheckMineCardList");

                    // list.add(MajiangStateEnum.STATE_GANG_CAL_SCORE);
                    // list.addAll(touchCardProcesses);
                    game.qiangGangCallCardList = null;
                }
                // 手里有花 碰之后，出一张牌，下家可以吃，选择过的情况 一定要放在最后
                if (stack.isEmpty() && list.isEmpty()) {
                    // list.add(MajiangStateEnum.STATE_NEXT_SEAT);
                    list.add("FlowSeat 1");
                }
            }
        }
            break;
        case "FlowHu":
            // list.add(MajiangStateEnum.STATE_ROUND_OVER);
            list.add("FlowRoundOver");
            break;
        case "FlowRoundOver":
            if (fightService.isGameOver(game)) {
                // list.add(MajiangStateEnum.STATE_GAME_OVER);
                list.add("FlowGameOver");
            } else {
                // list.add(MajiangStateEnum.STATE_INIT_READY);
                list.add("FlowInitReady");
            }
            break;

        default:
        }
        return list;

    }

    public boolean containsFlowers(RoleGameInfo roleGameInfo) {
        for (int flower : flowerCards) {
            if (roleGameInfo.cards.contains(flower)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除影响牌型的杠
     * 
     * @param game
     */
    public void deleteGang(Game game) {
        // 获得已经听的玩家
        Map<Integer, RoleGameInfo> tingRoleMap = new HashMap<>();
        List<String> roleIdList = game.getRoleIdList();
        for (int i = 0; i < roleIdList.size(); i++) {
            String roleId = roleIdList.get(i);
            RoleGameInfo roleGameInfo = game.getRoleIdMap().get(roleId);
            if (roleGameInfo.isTing) {
                tingRoleMap.put(i, roleGameInfo);
            }
        }
        List<CallCardList> callCardLists = game.getCallCardLists();
        Iterator<CallCardList> it = callCardLists.iterator();
        while (it.hasNext()) {
            CallCardList item = it.next();
            if (item.cardList instanceof Gang) {
                int masterSeat = item.masterSeat;
                Gang gang = (Gang) item.cardList;
                RoleGameInfo roleGameInfo = tingRoleMap.get(masterSeat);
                if (roleGameInfo == null) {
                    continue;
                }
                List<Integer> cards = new ArrayList<>(roleGameInfo.cards);
                while (cards.contains(gang.card)) {
                    cards.remove(Integer.valueOf(gang.card));
                }
                List<Integer> tingCardsResult = baidaHu.checkTingCards(cards);
                game.logger.info("刚之后的tingCards {}  \n 原来的tingCards {}", tingCardsResult, roleGameInfo.tingCards);
                if (!Lists.equals(roleGameInfo.tingCards, tingCardsResult)) {
                    // 检测后不一样，删除这个杠
                    game.logger.info("删除一个影响牌型的杠: {}", gang);
                    it.remove();
                }
            }
        }
    }

    /** 所有的牌型 */
    public Map<Class<? extends CardList>, CardList> allCardListMap = new HashMap<>();

    // /** 别人打牌 不是下一家 花足够时，可以抓胡、杠、碰 */
    // private List<Class<? extends CardList>> otherFlowerMoreCardListSequence =
    // new ArrayList<>();
    // /** 别人打牌 不是下一家 花不够时，只能杠、碰 */
    // private List<Class<? extends CardList>> otherFlowerLessCardListSequence =
    // new ArrayList<>();
    // /** 别人打牌 下一家 花够时 可以抓胡、杠、碰、吃 */
    // private List<Class<? extends CardList>>
    // otherNextAndFlowerMoreCardListSequence = new ArrayList<>();
    // /** 别人打牌 下一家 花够时 可以杠、碰、吃 */
    // private List<Class<? extends CardList>>
    // otherNextAndFlowerLessCardListSequence = new ArrayList<>();
    // /** 进入听状态 别人打的牌都不用检测 */
    // private List<Class<? extends CardList>> otherTingCardListSequence = new
    // ArrayList<>();
    // /** 进入听状态 别人打的牌都不用检测 */
    // /** 自己摸牌 花够时 可以 胡、杠 */
    // private List<Class<? extends CardList>> mineFlowerMoreCardListSequence =
    // new ArrayList<>();
    //
    // /** 自己摸牌 花不够时 可以 杠 */
    // private List<Class<? extends CardList>> mineFlowerLessCardListSequence =
    // new ArrayList<>();

    public CopyOfQiaoMaRule() {
        allCardListMap.put(Gang.class, ReflectUtils.newInstance(QiaomaGang.class));
        allCardListMap.put(Peng.class, ReflectUtils.newInstance(BaidaPeng.class));
        allCardListMap.put(Chi.class, ReflectUtils.newInstance(BaidaChi.class));
        allCardListMap.put(Hu.class, ReflectUtils.newInstance(Step5Hu.class));

        // otherFlowerMoreCardListSequence.add(Hu.class);
        // otherFlowerMoreCardListSequence.add(Gang.class);
        // otherFlowerMoreCardListSequence.add(Peng.class);
        //
        // otherFlowerLessCardListSequence.add(Gang.class);
        // otherFlowerLessCardListSequence.add(Peng.class);
        //
        // otherNextAndFlowerMoreCardListSequence.add(Hu.class);
        // otherNextAndFlowerMoreCardListSequence.add(Gang.class);
        // otherNextAndFlowerMoreCardListSequence.add(Peng.class);
        // otherNextAndFlowerMoreCardListSequence.add(Chi.class);
        //
        // otherNextAndFlowerLessCardListSequence.add(Gang.class);
        // otherNextAndFlowerLessCardListSequence.add(Peng.class);
        // otherNextAndFlowerLessCardListSequence.add(Chi.class);
        //
        // mineFlowerMoreCardListSequence.add(Hu.class);
        // mineFlowerMoreCardListSequence.add(Gang.class);
        //
        // mineFlowerLessCardListSequence.add(Gang.class);
        //
        // otherTingCardListSequence.add(Hu.class);
        // otherTingCardListSequence.add(Gang.class);
    }

    @Override
    public List<Integer> getCards() {
        return cards;
    }

    @Override
    public List<Integer> getFlowers() {
        return flowerCards;
    }

    @Override
    public int getBaidaCard(Game game) {
        return 0;
    }

    @Override
    public void executeRoundOverProcess(Game game, boolean checkHu) {
        fightService.roundOverQiaoMa(game, checkHu);
    }

    @Override
    public void executeGameOverProcess(Game game) {
        // 红中和百搭差不多
        fightService.gameOver(game);
    }

    @Override
    public List<Class<? extends CardList>> getOtherCardListSequence(RoleGameInfo roleGameInfo, Game game) {
        List<Class<? extends CardList>> cardListSequence = new ArrayList<>();
        cardListSequence.add(Gang.class);

        if (roleGameInfo.isTing) {
            GameConfigData gameConfig = game.getGameConfig();
            // 几个花才能抓胡
            int zhuaFlowerCount = gameConfig.getZhuaFlowerCount();
            int totalFlowerCount = roleGameInfo.flowerCount + getDarkFlowerCount(roleGameInfo.cards, game.sendCard);
            // 胡牌牌型计算
            List<HuType> huTypeList = typeCalc.calc(roleGameInfo, game, game.sendCard, false).typeList;
            // 移除垃圾胡
            huTypeList.remove(HuType.LA_JI_HU);
            // 花够或符合牌型能胡
            if (totalFlowerCount >= zhuaFlowerCount || huTypeList.size() > 0) {
                cardListSequence.add(Hu.class);
            }
        } else {
            // 不处于听状态时，都可以碰
            cardListSequence.add(Peng.class);
            // 如果可以吃应该是false,不可以吃应该是true
            if (game.getGameConfig().getNoChi() == false) {
                int nextSeat = seatIndexCalc.getNext(game);
                RoleGameInfo nextRoleInfo = roleGameInfoManager.getRoleGameInfoBySeat(game, nextSeat);
                boolean isNextRole = nextRoleInfo.gameRoleId.equals(roleGameInfo.gameRoleId);
                // 如果是下家可以吃
                if (isNextRole) {
                    cardListSequence.add(Chi.class);
                }
            }
        }
        game.logger.info("返回的cardList: {} ", cardListSequence);
        return cardListSequence;
    }

    @Override
    public List<Class<? extends CardList>> getMineCardListSequence(RoleGameInfo roleGameInfo, Game game) {
        List<Class<? extends CardList>> cardListSequence = new ArrayList<>();
        cardListSequence.add(Gang.class);

        if (roleGameInfo.isTing) {
            GameConfigData gameConfig = game.getGameConfig();
            // 几个花才能抓胡
            int moFlowerCount = gameConfig.getMoFlowerCount();
            int totalFlowerCount = roleGameInfo.flowerCount
                    + getDarkFlowerCount(roleGameInfo.cards, roleGameInfo.newCard);
            // 胡牌牌型计算
            List<HuType> huTypeList = typeCalc.calc(roleGameInfo, game, roleGameInfo.newCard, roleGameInfo.isGang).typeList;
            // 移除垃圾胡
            huTypeList.remove(HuType.LA_JI_HU);
            // 花够或符合牌型能胡
            if (totalFlowerCount >= moFlowerCount || huTypeList.size() > 0) {
                cardListSequence.add(Hu.class);
            }
        }
        game.logger.info("返回的cardList: {} ", cardListSequence);
        return cardListSequence;
    }

    @Override
    public Map<Class<? extends CardList>, CardList> getCardListMap() {
        return allCardListMap;
    }

    public boolean isFlower(Game game, int newCard) {
        RoleGameInfo roleGameInfo = roleGameInfoManager.current(game);
        boolean isFlower = cardChecker.isHua(newCard);
        if (isFlower) {
            // 加入花牌集合
            roleGameInfo.sendFlowrCards.add(roleGameInfo.newCard);
            roleGameInfo.flowerCount++;
            roleGameInfo.isGang = true;
        }
        return isFlower;
    }

    /**
     * 获得当前角色所有牌的数量
     * 
     * @param roleGameInfo
     * @return
     */
    public int getCardCount(RoleGameInfo roleGameInfo) {
        int total = 0;
        int size = roleGameInfo.cards.size();
        if (roleGameInfo.newCard != 0) {
            size++;
        }
        int cardListSize = roleGameInfo.showCardLists.size();
        total = size + cardListSize * 3;
        return total;
    }

    public int getDarkFlowerCount(List<Integer> cards, int newCard) {
        List<Integer> newCards = new ArrayList<>(cards);
        newCards.add(newCard);
        return getDarkFlowerCount(newCards);
    }

    /**
     * 获取手牌里超过3个的风向牌的个数
     * 
     * @param roleGameInfo
     * @return
     */
    public int getDarkFlowerCount(List<Integer> cards) {
        int count = 0;
        for (Integer fengCard : fengCards) {
            if (Collections.frequency(cards, fengCard) >= 3) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isFlower(Integer card) {
        return flowerCards.contains(card);
    }

    private void pass(Game game) {
        for (RoleGameInfo roleGameInfo : game.getRoleIdMap().values()) {
            verifyManager.pass(roleGameInfo.verify);
        }
    }

}
