package com.poseidon.pokers.domain.entity.socket;


import com.poseidon.pokers.domain.entity.socket.annotation.CommClass;
import com.poseidon.pokers.domain.entity.socket.annotation.CommColumn;

/**
 *
 * Created by longyi01 on 2016/6/13.
 */
public class CommClasses {

    public static final int PUMPING_INVALID = -1;

    @CommClass(Code.REQ_Enter_Room)
    public static class EnterRoomData extends BaseCommData {
        @CommColumn(60) public byte succeed; //返回状态 0成功 1 失败 2 房间已解散 3 房间已满
        @CommColumn(value = 61, required = false) public byte gameStatus; //游戏状态  0:倒计时中  1:游戏中
        @CommColumn(value = 62, required = false) public byte countDownSeconds; //倒计时间  s
        @CommColumn(value = 65, required = false) public byte bigBlindSeatId; //大盲注座位编号
        @CommColumn(value = 66, required = false) public byte smallBlindSeatId; //小盲注座位编号
        @CommColumn(value = 67, required = false) public byte dealerSeatId;//庄家座位编号
        @CommColumn(value = 68, required = false) public byte curOperationSeatId; //当前操作玩家编号
        @CommColumn(value = 69, required = false) public byte operationSeconds; //房间允许操作时间
        @CommColumn(value = 70, required = false) public byte playerSeatId; //服务器分配座位id
        @CommColumn(value = 71, required = false) public byte allowCheckCountIfTimeOut; // 允许最大的超时跟牌次数
        @CommColumn(value = 72, required = false) public byte checkCountTimedOut; // 已经利用了多少次超时跟牌的机会
        @CommColumn(value = 131, required = false) public byte[] publicCards; //已经翻了的牌
        @CommColumn(value = 132, required = false) public int[] playerIds; //玩家id
        // 玩家状态 1:未操作过 2:跟注  3:加注  4:全下 5:让牌  6:弃牌 7托管 8等待下一局 9 等待入座 10 空坐（不展示可坐）11 留座离桌
        @CommColumn(value = 133, required = false) public int[] playersStatus;
        @CommColumn(value = 134, required = false) public int[] playersCurRoundAntes; //本轮 每个玩家的下注筹码数
        @CommColumn(value = 135, required = false) public String playerNicks; //玩家昵称
        @CommColumn(value = 136, required = false) public int[] playersChipsInDesk; //玩家桌上未下的筹码
        @CommColumn(value = 137, required = false) public String playerHeads; //玩家头像
        @CommColumn(value = 138, required = false) public byte[] playerFirstCards; //玩家第一张牌
        @CommColumn(value = 139, required = false) public byte[] playerSecondCards; //玩家第二张牌
        @CommColumn(value = 140, required = false) public int bigBlindAnte; //大盲注
        @CommColumn(value = 141, required = false) public int smallBlindAnte; //小盲注
        @CommColumn(value = 142, required = false) public int potChips; //奖池筹码数
        @CommColumn(value = 143, required = false) public int curRoundMaxAnte; //本轮最大下注
        @CommColumn(value = 144, required = false) public int ownerId; //本房间的房主ID
        @CommColumn(value = 145, required = false) public String startTime; //本房间开始计时的时间，0表示尚未开始计时
        @CommColumn(value = 146, required = false) public int totalTimeMinutes; // 本房间的总有效时间（单位：分钟）
        @CommColumn(value = 147, required = false) public byte takeInControl; // 是否开启了带入控制，0: 关闭 1: 开启
        @CommColumn(value = 148, required = false) public int playerChipsOutRoom; // 用户房间外的筹码
        @CommColumn(value = 149, required = false) public int playerChipsInRoom; // 用户房间内剩余的筹码
        // 用户房间内剩余的时间(秒），未开始则返回-1, -2无限局
        @CommColumn(value = 150, required = false) public int remainTime;
        @CommColumn(value = 151, required = false) public int pausedTime; // 暂停时间，0不暂停，大于0暂停
        @CommColumn(value = 152, required = false) public int minTakeInRateNextRound; //下次生效的最小带入倍数
        @CommColumn(value = 153, required = false) public int maxTakeInRateNextRound; //下次生效的最大带入倍数
        @CommColumn(value = 154, required = false) public int minTakeInRateCurRound; //当前牌局的最小带入倍数
        @CommColumn(value = 155, required = false) public int maxTakeInRateCurRound; //当前牌局的最大带入倍数
        @CommColumn(value = 156, required = false) public int operatorRemainTime; //当前操作的人剩余多少秒
        @CommColumn(value = 157, required = false) public byte[] playersSex; // 所有用户的性别
        @CommColumn(value = 158, required = false) public int pokerStates; //亮牌状态，0不亮,1亮第一张,2亮第二种,3两张都亮
        @CommColumn(value = 159, required = false) public int groupBet; // 前注
        @CommColumn(value = 160, required = false) public int lastRequestAddInRemainTime; // 上次申请带入还剩多少时间超时（0表示没有待审批的申请）
        @CommColumn(value = 161, required = false) public int[] chipsPool; // 各筹码池数量，可能为null
        @CommColumn(value = 162, required = false) public int minRaise; // 最小加注金额
        @CommColumn(value = 163, required = false) public int canRaise; // 是否能加注,0为不可以，1为可以
        @CommColumn(value = 164, required = false) public int insuranceMode; // 1 开启保险，0关闭
        @CommColumn(value = 165, required = false) public int[] canPlayArray; // 是否打牌，0为不可以，1为可以
        @CommColumn(value = 166, required = false) public int[] seatStatusArray; // 0为刚坐下，>=1显示气泡
        @CommColumn(value = 167, required = false) public int isNeedTip; // 是否需要弹过庄补盲选择框 0不需要 1需要
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 168, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        // 房间类型（1：普通房 2：俱乐部有限时长 3：俱乐部无限时长）
        @CommColumn(value = 169, required = false) public int roomType;
        @CommColumn(value = 170, required = false) public int needBring; // 需要首次带入的筹码数（只在roomType为2时使用）
        @CommColumn(value = 171, required = false) public int isTrusted; // 是否有被托管
        @CommColumn(value = 172, required = false) public int callChips; // 如果是自己操作，表示跟注所需筹码，0表示可以看牌
        // 公共牌中哪些牌是用户花钱翻开的，翻开标记为1
        @CommColumn(value = 173, required = false) public byte[] publicCardsType;
        @CommColumn(value = 174, required = false) public int keepSeatInOtherRoom; // 0没有在其它房间留座离桌，1有
        // 留座离桌的人的倒计时剩余秒数，-1表示无效
        @CommColumn(value = 175, required = false) public int[] keepSeatRemainSeconds;
        @CommColumn(value = 176, required = false) public String userPayToShowNextCard; // 为""时表示没有人
        @CommColumn(value = 177, required = false) public int ipRestriction;  // 0关闭 1开启
        @CommColumn(value = 178, required = false) public int gpsRestriction; // gps控制
    }

    @CommClass(Code.REQ_Recv_Winner)
    public static class GameWinData extends BaseCommData {
        @CommColumn(130) public byte[] winnerSeatIds; //收筹码玩家的座位编号
        @CommColumn(131) public int[] winnerPlayerIds; //玩家id
        @CommColumn(132) public int[] winChips; //收筹码数
        @CommColumn(133) public byte[] winTypes; //赢的牌类型	1:皇家同花顺  2:同花顺  3:4条 4:葫芦 5:同花 6:顺子 7:三张 8:两对 9:一对 10:高牌
        @CommColumn(134) public byte[] winnerFlag; //是否赢家 0：是 1：不是
        @CommColumn(135) public int[] realWinChips; //扣除下的筹码最终赢得的筹码数
        @CommColumn(136) public byte[] winCards; //哪几张牌赢的 	系统牌 0-4 底牌 5-6
        @CommColumn(137) public int[] playerLastChips; //每个玩家最后显示筹码
        @CommColumn(138) public int[] playerIds; //每个玩家id
        @CommColumn(140) public byte[] firstCards; //玩家第一张牌	有牌 0-51 无牌 -1
        @CommColumn(141) public byte[] secondCards; //玩家第二张牌	有牌 0-51 无牌 -1
        @CommColumn(142) public byte[] biggestWinner; //是否取得最大手牌 	0：是，1：不是
        @CommColumn(143) public int chipsInNextGame;   // 玩家下一局将会拥有的筹码（包括了下局生效的带入请求）
        @CommColumn(value = 144, required = false) public byte[] muckStatus; // 玩家是否是盖牌  0：不是  1：是
    }

    @CommClass(Code.REQ_Recv_Start_Infor)
    public static class StartInfoData extends BaseCommData {
        @CommColumn(60) public byte dealerIndex; //庄家编号
        @CommColumn(61) public byte bigBlindIndex; //大盲注编号
        @CommColumn(62) public byte smallBlindIndex; //小盲注编号
        @CommColumn(63) public byte nextOperatorSeatId; //下一位操作玩家的座位编号
        @CommColumn(130) public byte[] playerFirstCards; //玩家第一张牌
        @CommColumn(131) public byte[] playerSecondCards; //玩家第二张牌
        @CommColumn(132) public int smallBlindAnte; //小盲注
        @CommColumn(133) public int bigBlindAnte; //大盲注
        @CommColumn(134) public int[] playerChipsInDesk; //每个座位上玩家的筹码
        @CommColumn(135) public int[] missionCountDown; //任务倒计时 -1，完成
        @CommColumn(136) public int[] missionRewards; //任务奖励筹码数
        @CommColumn(137) public int playerChipsOutRoom; // 当前用户最新的房间外筹码
        @CommColumn(138) public int minTakeInRate; // 最小带入倍数
        @CommColumn(139) public int maxTakeInRate; // 最大带入倍数
        @CommColumn(140) public int minRaise; // 最小加注金额
        @CommColumn(141) public int canRaise; // 是否能加注
        @CommColumn(142) public int[] canPlayArray; // 是否打牌，0为不可以，1为可以
        @CommColumn(143) public int[] seatStatusArray; // 0为刚坐下，>=1显示气泡
        @CommColumn(144) public int pokerHand; // 牌局第几手
        @CommColumn(145) public byte[] isNeedPost; // 是否要补盲 0否，1是
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 146, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        @CommColumn(value = 147, required = false) public int[] straddlePop; // 显示straddle气泡
        // 0straddle成功， 1straddle失败，2不处理
        @CommColumn(value = 148, required = false) public int isCurStraddle;
        @CommColumn(value = 149, required = false) public int callChips; // 跟牌所需筹码
        @CommColumn(value = 150, required = false) public int[] initBetChip; // 起始玩家下注筹码,（sng使用）
    }

    @CommClass(Code.REQ_Start_Game_Before)
    public static class StartGameBefore extends BaseCommData {
        @CommColumn(60) public byte dealerIndex; // 庄家编号
        @CommColumn(130) public int isStraddle; // 牌局是否有straddle 0否 1是
        @CommColumn(131) public int isCurStraddle; // 当前用户是否straddle 0否 1是
        @CommColumn(132) public int[] canPlayArray; // 是否打牌，0为不可以，1为可以
        @CommColumn(133) public int[] seatStatusArray; // 0为刚坐下，>=1显示气泡 2下一局才可以打
        @CommColumn(134) public int[] playerChipsInDesk; // 每个座位上玩家的筹码
        @CommColumn(135) public int straddleCost; // straddle用户消耗的筹码
    }

    @CommClass(Code.REQ_Straddle_Request)
    public static class StraddleRequest extends BaseCommData {
        @CommColumn(60) public byte succeed; // 返回状态 0成功 1 失败
    }

    @CommClass(Code.REQ_Recv_Player_Cards)
    public static class PlayerCardsData extends BaseCommData {
        @CommColumn(130) public byte[] playerSeatIds;
        @CommColumn(131) public byte[] firstCards;
        @CommColumn(132) public byte[] secondCards;
    }

    @CommClass(Code.REQ_ADD_TIME)
    public static class AddTimeData extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 失败 2钻石不足 3非当前操作玩家
        @CommColumn(value = 130, required = false) public int playerId;
        @CommColumn(value = 131, required = false) public int addTime;
        @CommColumn(value = 132, required = false) public int costJewel;
        @CommColumn(value = 133, required = false) public int costChips;
    }

    @CommClass(Code.REQ_PAUSE_GAME)
    public static class PauseGameData extends BaseCommData {
        @CommColumn(60) public byte status; // 0 continue 1 pause 2 failed
        @CommColumn(value = 130, required = false) public int pauseRemainTime;
    }

    @CommClass(Code.REQ_Recv_Action)
    public static class PlayerActionData extends BaseCommData {
        @CommColumn(61) public byte seatId;
        @CommColumn(62) public byte action; //1:下注  2:跟注  3:加注  4:全下 5:让牌  6:弃牌 7:超时
        @CommColumn(64) public byte nextOperatorSeatId; // 下一个操作玩家 座位id -1等待下一指令
        @CommColumn(130) public int antes;
        @CommColumn(131) public int playerId;
        @CommColumn(132) public int minRaise; // 最小加注金额
        @CommColumn(133) public int canRaise; // 是否能加注
        @CommColumn(134) public int callChip; // 跟注所需筹码
    }

    @CommClass(Code.REQ_Recv_Sit_Down)
    public static class SitDownData extends BaseCommData {
        @CommColumn(61) public byte seatId;
        @CommColumn(62) public byte sex; // 性别 -1 未知 0 男 1 女
        @CommColumn(130) public String headPic;
        @CommColumn(131) public String nick;
        @CommColumn(132) public int userId;
        @CommColumn(133) public int chips;
    }

    @CommClass(Code.REQ_Seat_Action) // 玩家自己的请求返回，不涉及第三个人的广播
    public static class SeatActionData extends BaseCommData {
        // 返回状态 0成功 1 失败 2 ip相同 3牌局已解散 4信用额度不足而无法坐下 5正在参与牌桌中，无法站起 6正在结算过程中，不允许坐下
        // 7 gps限制无法入座 8筹码低于80%
        @CommColumn(60) public byte success;
        @CommColumn(value = 61, required = false) public byte action; //1:坐下 2:站起 3:离开房间
        @CommColumn(value = 130, required = false) public int chipsInDesk; //牌桌上筹码
        @CommColumn(value = 131, required = false) public int chipsRemain; //剩余筹码
        @CommColumn(value = 132, required = false) public byte seatId; //座位号
        @CommColumn(value = 133, required = false) public int isNeedTip; // 是否需要弹过庄补盲选择框 0不需要 1需要
    }

    @CommClass(Code.REQ_Recv_System_Cards)
    public static class SystemCardsData extends BaseCommData {
        @CommColumn(60) public byte nextOperatorSeatID; // 下一个玩家操作的座位编号
        @CommColumn(130) public byte[] cards; // 本轮服务器发放的牌 0-51
        @CommColumn(131) public int minRaise; // 最小加注金额
        @CommColumn(132) public int canRaise; // 是否能加注
        @CommColumn(value = 133, required = false) public String userCostMoney; // 花钱看牌的人名，为空则表示系统发牌
        @CommColumn(value = 134, required = false) public int pokerType; // 牌型提示
    }

    @CommClass(Code.REQ_Add_Chips)
    public static class AddChipsData extends BaseCommData {
        // 0:成功  1:失败  2:等待房主审批 3:已拥有足够的筹码数量，请求的数量超过了最大允许带入 6:ip相同 7:信用额度不足
        // 8:正在俱乐部结算中，不允许带入 // 9:牌局已解散 10 gps限制无法入座
        @CommColumn(61) public byte success;
        @CommColumn(value = 62, required = false) public byte seatId = -1;
        @CommColumn(value = 130, required = false) public int chips; //chips 61字段为0或3时使用，表示现在用户已经拥有多少筹码
        @CommColumn(value = 131, required = false) public int playerId;
        @CommColumn(value = 132, required = false) public int chipsOutRoom;//用户剩下多少筹码在房间外（非本人时不要使用）
        @CommColumn(value = 133, required = false) public int waitingRemainSecs = 180; // 等待审批还剩多少时间
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制），只在成功且当前用户为自己时有用，其它情况不应使用此字段
        @CommColumn(value = 134, required = false) public int remainCreditValue = Integer.MAX_VALUE;
    }

    @CommClass(Code.REQ_ROOM_TAKE_IN_RANGE_CHANGE)
    public static class TakeInRangeData extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 异常
        @CommColumn(value = 130, required = false) public int minTakeInRate;
        @CommColumn(value = 131, required = false) public int maxTakeInRate;
    }

    @CommClass(Code.REQ_Check_Version)
    public static class CheckVersionData extends BaseCommData {
        @CommColumn(60) public byte status; //  0:不需要更新1:有新版本更新2：强制更新
        @CommColumn(value = 61, required = false) public byte versionCode;
        @CommColumn(value = 130, required = false) public String versionName;
        @CommColumn(value = 131, required = false) public String updateMessage; // 更新内容
        @CommColumn(value = 132, required = false) public String versionSize;
        @CommColumn(value = 133, required = false) public String downloadURL;
        @CommColumn(value = 134, required = false) public String md5;
    }

    @CommClass(Code.REQ_GAME_PREV_GAME_REVIEW)
    public static class GamePrevCardInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 失败
        @CommColumn(value = 61, required = false) public byte favorite; // 0 未收藏 1 已收藏
        @CommColumn(value = 130, required = false) public int[] userIds;
        @CommColumn(value = 131, required = false) public String userNicks;
        @CommColumn(value = 132, required = false) public String headPaths;
        @CommColumn(value = 133, required = false) public byte[] firstCards;
        @CommColumn(value = 134, required = false) public byte[] secondCards;
        @CommColumn(value = 135, required = false) public byte[] publicCards;
        @CommColumn(value = 136, required = false) public byte[] biggestCardsIndex;
        @CommColumn(value = 137, required = false) public byte[] cardType; // 最大牌类型, -1:无牌型 0:弃牌 1:皇家同花顺  2:同花顺  3:4条 4:葫芦 5:同花 6:顺子 7:三张 8:两对 9:一对 10:高牌
        @CommColumn(value = 138, required = false) public int[] chipsGain; //输赢筹码数
        @CommColumn(value = 139,required = false) public int insurancePool; // 保险池积分
        @CommColumn(value = 140, required = false) public int[] insuranceGain; // 每个人的保险收益
        // 每轮的操作(4 * n个数)，每个数的最高1位表示本轮是否弃牌，剩余7位（0表示直接弃牌，1表示看牌，2表示跟注，3表示加注，4表示ALL IN）
        @CommColumn(value = 141, required = false) public byte[] operations;
        @CommColumn(value = 142, required = false) public int[] operationChips; // 每轮操作的总筹码数
        @CommColumn(value = 143, required = false) public byte[] userType; // 用户类型，0普通人，1庄家，2小盲，4大盲（可组合）
        @CommColumn(value = 144, required = false) public int totalRounds; // 总局数
        @CommColumn(value = 145, required = false) public int curRound; // 当前数据所表示的第几局
        @CommColumn(value = 146, required = false) public String replayId; // 当前局的回放地址
        // 抽水总和，PUMPING_INVALID表示无数据，不要展示
        @CommColumn(value = 147, required = false) public int pumpingSum = PUMPING_INVALID;
        @CommColumn(value = 148, required = false) public byte[] thirdCards;
        @CommColumn(value = 149, required = false) public byte[] fourthCards;
    }

    @CommClass(Code.REQ_GET_PEOPLE_DATA)
    public static class RoomOnlinePeopleInfo extends BaseCommData {
        @CommColumn(60) public byte status; //返回状态 0成功 1 失败 2解散 3人数已满
        @CommColumn(value = 132, required = false) public int[] playerIds;
        @CommColumn(value = 135, required = false) public String playerNicks;
        @CommColumn(value = 137, required = false) public String playerHeads;
        @CommColumn(value = 138, required = false) public byte[] playerSex;
    }

    @CommClass(Code.REQ_ROOM_GAME_ENDED)
    public static class RoomEndInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 异常
        @CommColumn(value = 130, required = false) public String bigNicks; // nick 土豪/鲨鱼/大鱼
        @CommColumn(value = 131, required = false) public String bigHeads; // 头像 土豪/鲨鱼/大鱼
        @CommColumn(value = 132, required = false) public int[] playerIds;
        @CommColumn(value = 133, required = false) public int[] scores; // 房间所有用户总战绩
        @CommColumn(value = 134, required = false) public int[] handCounts; // 房间所有用户总手数
        @CommColumn(value = 135, required = false) public int[] takeIns; // 房间所有用户总带入
        @CommColumn(value = 136, required = false) public int takeInSum; // 房间所有用户带入总和
        @CommColumn(value = 137, required = false) public String playerNicks;
        @CommColumn(value = 138, required = false) public int maxPOT; // 该局最大池底
        @CommColumn(value = 139, required = false) public int insuranceMode; // 保险模式 1为开启保险
        @CommColumn(value = 140, required = false) public int insurancePool; // 保险池收支情况
    }

    @CommClass(Code.REQ_ROOM_TAKE_IN_CONTROL_PERMIT)  // 房主收到用户带入请求
    public static class RoomTakeInRequest extends BaseCommData {
        @CommColumn(130) public int requestUserId;
        @CommColumn(131) public String requestUserName;
        @CommColumn(132) public int roomPath;
        @CommColumn(133) public int roomId;
        @CommColumn(134) public int requestTakeIn; // 请求带入筹码数量
    }

    @CommClass(Code.REQ_SITUATION_INFO)
    public static class RoomStatuesInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 异常
        @CommColumn(130) public int[] playerIds;
        @CommColumn(131) public String playerHeads;
        @CommColumn(132) public String playerNicks;
        @CommColumn(133) public int[] playerGains; // 净胜筹码
        @CommColumn(134) public int[] playerTakeIns; // 总带入
        @CommColumn(135) public int[] observersIds;
        @CommColumn(136) public String observersHeads;
        @CommColumn(137) public String observerNicks;
        @CommColumn(138) public byte[] playerStatus; //player的状态，0在玩，1没在玩，2无限局中已经离开的玩家
        @CommColumn(139) public byte[] playersSex;
        @CommColumn(value = 140, required = false) public int insurancePool; // 保险池收支
        // 抽水总和，PUMPING_INVALID表示无数据，不要展示
        @CommColumn(value = 141, required = false) public int pumpingSum = PUMPING_INVALID;
        @CommColumn(value = 142, required = false) public int hideInsurance; // 隐藏保险条目（0:不隐藏 1:隐藏）
    }

    @CommClass(Code.REQ_Recv_Readytime)
    public static class CountDownInfo extends BaseCommData {
        @CommColumn(60) public byte seconds;
    }

    @CommClass(Code.REQ_Recv_Leave)
    public static class PlayerLeaveInfo extends BaseCommData {
        @CommColumn(60) public byte seatId;
        @CommColumn(130) public int playerId;
    }

    @CommClass(Code.REQ_Send_Action)
    public static class SendActionInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 失败
    }

    @CommClass(Code.REQ_ROOM_START_GAME)
    public static class RoomStartGame extends BaseCommData {
        @CommColumn(60) public byte success; // 0 表示开始成功，1表示失败
    }

    @CommClass(Code.REQ_SHOWDOWN)
    public static class ShowPokerInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功
        @CommColumn(value = 61, required = false) public byte status; // 0不亮牌，1亮第一张，2亮第二张，3都亮
    }

    @CommClass(Code.REQ_ROOM_STATUS)
    public static class RoomStatusInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0成功 1失败
        @CommColumn(value = 61, required = false) public byte status; // 0:close 1:create 2:pending 3:playing
    }

    @CommClass(Code.REQ_ROOM_TAKE_IN_CONTROL_SWITCH)
    public static class RoomTakeInControlSwitchInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0成功
        @CommColumn(value = 61, required = false) public byte status; // 0 关闭 1 开启
    }

    @CommClass(Code.REQ_Login_LoginServer)
    public static class LoginLoginServerInfo extends BaseCommData {
        @CommColumn(60) public byte success; //返回状态 0成功 1用户名错误 2密码错误 3数据库错误 4版本过低 5 TOKEN已失效
        @CommColumn(value = 61, required = false) public byte userType; //0:游客 1:qq 2:新浪 3:91 4:盛大 5:龙旗 6：当乐
        @CommColumn(value = 62, required = false) public byte firstLogin; //0首次登录 1不是首次登录
        @CommColumn(value = 134, required = false) public int serverId; //服务器编号
        @CommColumn(value = 130, required = false) public String resServerIp; //资源服务器ip
        @CommColumn(value = 131, required = false) public String key;
        @CommColumn(value = 132, required = false) public int userId;
        @CommColumn(value = 133, required = false) public int resServerPort;
        @CommColumn(value = 135, required = false) public String quickToken; // 服务器反馈的TOKEN，下次可直接用TOKEN登陆
    }

    @CommClass(Code.REQ_Login_ResourceServer)
    public static class LoginResourceServerInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 状态 0成功 1key超时 2找到该KEY 3其他错误
    }

    @CommClass(Code.REQ_Offline)
    public static class OfflineInfo extends BaseCommData {
        @CommColumn(60) public byte status; // 0:注销 1:其他地方登录
    }

    @CommClass(Code.REQ_ROOM_CONTROL)
    public static class RoomSetingCtrl extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(61) public byte action;
        @CommColumn(62) public byte extVal1;
        @CommColumn(63) public byte extVal2;
        @CommColumn(130) public int roomId;
    }

    @CommClass(Code.REQ_NEW_ADD_IN_WITH_TIMEOUT)
    public static class AddInWithTimeOutData extends BaseCommData {
        @CommColumn(130) public String takeInJsonString;
    }

    @CommClass(Code.REQ_GET_ADD_IN_WITH_TIMEOUT)
    public static class AddInWithTimeOutListData extends BaseCommData {
        @CommColumn(130) public String takeInArrayJsonString; // 带入请求单条数据
    }

    @CommClass(Code.REQ_ADD_IN_TIMEOUT)
    public static class AddInTimeOutData extends BaseCommData {
        @CommColumn(130) public String expireUidList; // 带入请求列表数据，以%@分隔
    }

    @CommClass(Code.REQ_ADD_IN_LIST_READ)
    public static class AddInListReadData extends BaseCommData {
        @CommColumn(60) public byte result; // 0 成功
        @CommColumn(130) public String lastUID; // 此UID前的UID都可以清除
    }

    @CommClass(Code.REQ_PERMIT_ADD_IN_LIST)
    public static class PermitAddInList extends BaseCommData {
        @CommColumn(60) public byte result; // 0 允许 1 拒绝
        @CommColumn(value = 130, required = false) public String permitUidList; // 哪些操作被允许或者拒绝，以@%分隔
        @CommColumn(value = 131, required = false) public String expiredUidList; // 哪些消息已过期，操作失败，以@%分隔
    }

    @CommClass(Code.REQ_COLLECT_USERS_CHIPS)
    public static class CollectUsersChipsData extends BaseCommData {
        @CommColumn(130) public int[] pools; // 不为空，长度至少为1，依次表示主池、第一分池、第二分池……
    }

    @CommClass(Code.REQ_INSURANCE_BEGIN_BUY) // 保险模式触发
    public static class InsuranceBuyBeginData extends BaseCommData {
        @CommColumn(60) public byte triggerCount; // 第几次触发（1开始）
        @CommColumn(130) public int[] playerIds; // 能投保的用户ID
        @CommColumn(value = 131, required = false) public int[] playerStatus; // 各保险人状态（0 正在购买 1 已购买）
        @CommColumn(value = 132, required = false) public int[] insuranceCost; // 各可参与保险的人当前已经购买的投保额（在中途进房间的时候有用）
        @CommColumn(value = 133, required = false) public int[] remainSeconds; // 各投保人剩下的时间
        @CommColumn(value = 134, required = false) public byte[] chipsPoolIndexes; // 可参与的保险池索引
        @CommColumn(value = 135, required = false) public int[] outsCards; // 所有的OUTS，用-1分隔每个池对应的牌
        @CommColumn(value = 136, required = false) public String rates; // 各池赔率，","隔开
        @CommColumn(value = 137, required = false) public int[] minInsuranceCosts; // 各池最低投保额
        @CommColumn(value = 138, required = false) public int[] maxInsuranceCosts; // 各池最高投保额
        @CommColumn(value = 139, required = false) public int[] costCoverageCosts; // 各池的保本额度
        @CommColumn(value = 140, required = false) public byte[] partialBuyEnable; // 是否允许购买部分牌（0表示不允许，其他值表示允许）
        @CommColumn(value = 141, required = false) public int[] costToCover; // 已投保险额+本池数量（保险需要赔付到多少才能保本）
        @CommColumn(value = 142, required = false) public String userNames; // @% 分隔的所有用户名 @%@% 分隔不同池
        @CommColumn(value = 143, required = false) public int[] outsPerUser; // 各对应用户对于玩家的OUTS（不包含自己，与用户数一致）
        @CommColumn(value = 144, required = false) public int[] myChipsInPool; // 自己在各个池中有多少钱，与可投保保险池数一致
        // 各玩家手牌（不包含自己，为用户数的2倍或4倍），依次表示第一个玩家第一张牌、第二张牌……第二个玩家第一张牌……
        @CommColumn(value = 145, required = false) public int[] playerCards;
    }

    @CommClass(Code.REQ_INSURANCE_BUY) // 用户购买保险的情况
    public static class InsuranceBuyData extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败 2 强制背保
        @CommColumn(61) public byte seatId;
        @CommColumn(130) public int playerId;
        @CommColumn(131) public int insuranceCost; // 投保额
    }

    @CommClass(Code.REQ_GET_INSURANCE_PAY) // 用户理赔结果
    public static class InsurancePay extends BaseCommData {
        @CommColumn(130) public int[] seatIds;
        @CommColumn(131) public int[] playerIds;
        @CommColumn(132) public int[] paid; // 赔付额（0表示不中）
    }

    @CommClass(Code.REQ_INSURANCE_ADD_TIME)
    public static class InsuranceAddTimeData extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 失败 2钻石不足 3非当前操作玩家
        @CommColumn(value = 130, required = false) public int playerId;
        @CommColumn(value = 131, required = false) public int addTime;
        @CommColumn(value = 132, required = false) public int costJewel;
        @CommColumn(value = 133, required = false) public int costChips;
    }

    @CommClass(Code.REQ_INSURANCE_TRIGGER_ERROR)
    public static class InsuranceTriggerError extends BaseCommData {
        @CommColumn(60) public byte unused;
    }

    @CommClass(Code.REQ_WAIT_OR_POST)
    public static class ReqWaitOrPost extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败
        @CommColumn(61) public byte type;  // 0过庄 1补盲
    }

    // 房主方-----
    // 控制带入普通推送
    @CommClass(Code.REQ_CLIENT_CTRL_COMMON_PUSH)
    public static class ClientCommonPush extends BaseCommData {
        @CommColumn(130)
        public String message;
    }

    // 请求控制带入记录
    @CommClass(Code.REQ_GET_CLIENT_REQ_RECORD)
    public static class ClientReqRecord extends BaseCommData {
        @CommColumn(130)
        public String message;
        @CommColumn(131)
        public int src;
    }

    // 控制带入过期通知
    @CommClass(Code.REQ_GAME_OVER_NOTICE)
    public static class SetReqTimeOutNotice extends BaseCommData {
        @CommColumn(130)
        public int roomId;
        @CommColumn(131)
        public String roomName;
    }

    // 已阅上报，由房主上报
    @CommClass(Code.REQ_CLIENT_READ_ALREADY)
    public static class SetReqReaded extends BaseCommData {
        @CommColumn(60)
        public byte result;
        @CommColumn(130)
        public int uid;
    }

    // 房主同意/拒绝请求
    @CommClass(Code.REQ_PORCESS_MESSAGE)
    public static class SetCtrlReqProcessResult extends BaseCommData {
        @CommColumn(60)
        public byte permit; // 0表示允许 1表示拒绝
        @CommColumn(value = 130, required = false)
        public String successIds; // 包含一个成功处理的消息UID串 多个消息用@%隔开
        @CommColumn(value = 131, required = false)
        public String failedIds; // 包含失败的ID串(包含过期的请求 超过名额导致报名失败的请求) 多个消息用@%隔开
        @CommColumn(value = 132, required = false) // 包含一个房间ID - 剩余名额的映射关系
        public String leftIds;
    }

    // 请求方---
    // 自己控制带入的请求结果推送
    @CommClass(Code.REQ_MINE_REQ_RESULT_PUSH)
    public static class MineReqResultPush extends BaseCommData {
        @CommColumn(130)
        public String message;
    }

    // 请求自己控制带入记录
    @CommClass(Code.REQ_GET_MINE_REQ_RECORD)
    public static class GetMineReqRecord extends BaseCommData {
        @CommColumn(130)
        public String message;
        @CommColumn(131)
        public int unReadCount;
        @CommColumn(132)
        public int src;
    }

    @CommClass(Code.REQ_Trust_Status)
    public static class ReqTrustOption extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败
        // 0取消托管 1托管 2进入请求时间 3托管时间到 4其它房间状态查询 5刷新其他人消息
        @CommColumn(61) public byte option;
        @CommColumn(130) public int trustTime; // 倒计时时间，单位S
        @CommColumn(131) public int firstSit; // 是否第一次坐下，0不是，1是
        @CommColumn(132) public int[] trustStatus; // 托管状态数组 0没托管 1托管
        @CommColumn(133) public int isTrusted; // 是否有被托管过
        @CommColumn(134) public String roomId; // 托管中的房间号
    }

    // 解散牌局
    @CommClass(Code.REQ_Dismiss_Message)
    public static class ReqDismissMessage extends BaseCommData {
        @CommColumn(60) public byte status; // 0成功 1失败 2无限局结束 3服务器将在5分钟后重启
        @CommColumn(value = 130, required = false) public String clubName; // 俱乐部名称
    }

    // MTT消息系列
    @CommClass(Code.REQ_MTT_Enter_Room)
    public static class MttEnterRoomData extends BaseCommData {
        @CommColumn(60) public byte succeed; //返回状态 0成功 1 失败 2 房间已解散 3 房间已满
        @CommColumn(value = 61, required = false) public byte gameStatus; //游戏状态  0:倒计时中  1:游戏中
        @CommColumn(value = 62, required = false) public byte countDownSeconds; //倒计时间  s
        @CommColumn(value = 65, required = false) public byte bigBlindSeatId; //大盲注座位编号
        @CommColumn(value = 66, required = false) public byte smallBlindSeatId; //小盲注座位编号
        @CommColumn(value = 67, required = false) public byte dealerSeatId;//庄家座位编号
        @CommColumn(value = 68, required = false) public byte curOperationSeatId; //当前操作玩家编号
        @CommColumn(value = 69, required = false) public byte operationSeconds; //房间允许操作时间
        @CommColumn(value = 70, required = false) public byte playerSeatId; //服务器分配座位id
        @CommColumn(value = 71, required = false) public byte allowCheckCountIfTimeOut; // 允许最大的超时跟牌次数
        @CommColumn(value = 72, required = false) public byte checkCountTimedOut; // 已经利用了多少次超时跟牌的机会
        @CommColumn(value = 131, required = false) public byte[] publicCards; //已经翻了的牌
        @CommColumn(value = 132, required = false) public int[] playerIds; //玩家id
        @CommColumn(value = 133, required = false) public int[] playersStatus; //玩家状态 1、让牌2、加注 3、跟注 4 、等待下一局
        @CommColumn(value = 134, required = false) public int[] playersCurRoundAntes; //本轮 每个玩家的下注筹码数
        @CommColumn(value = 135, required = false) public String playerNicks; //玩家昵称
        @CommColumn(value = 136, required = false) public int[] playersChipsInDesk; //玩家桌上未下的筹码
        @CommColumn(value = 137, required = false) public String playerHeads; //玩家头像
        @CommColumn(value = 138, required = false) public byte[] playerFirstCards; //玩家第一张牌
        @CommColumn(value = 139, required = false) public byte[] playerSecondCards; //玩家第二张牌
        @CommColumn(value = 140, required = false) public int bigBlindAnte; //大盲注
        @CommColumn(value = 141, required = false) public int smallBlindAnte; //小盲注
        @CommColumn(value = 142, required = false) public int potChips; //奖池筹码数
        @CommColumn(value = 143, required = false) public int curRoundMaxAnte; //本轮最大下注
        @CommColumn(value = 144, required = false) public int ownerId; //本房间的房主ID
        //        @CommColumn(value = 145, required = false) public String startTime; //本房间开始计时的时间，0表示尚未开始计时
        //        @CommColumn(value = 146, required = false) public int totalTimeMinutes; // 本房间的总有效时间（单位：分钟）
        @CommColumn(value = 147, required = false) public byte takeInControl; // 是否开启了带入控制，0: 关闭 1: 开启
        @CommColumn(value = 148, required = false) public int playerChipsOutRoom; // 用户房间外的筹码
        @CommColumn(value = 149, required = false) public int playerChipsInRoom; // 用户房间内剩余的筹码
        //        @CommColumn(value = 150, required = false) public int remainSeconds; // 用户房间内剩余的时间，未开始则返回-1
        //        @CommColumn(value = 151, required = false) public int pausedTime; // 暂停时间，0不暂停，大于0暂停
        //        @CommColumn(value = 152, required = false) public int minTakeInRateNextRound; //下次生效的最小带入倍数
        //        @CommColumn(value = 153, required = false) public int maxTakeInRateNextRound; //下次生效的最大带入倍数
        //        @CommColumn(value = 154, required = false) public int minTakeInRateCurRound; //当前牌局的最小带入倍数
        //        @CommColumn(value = 155, required = false) public int maxTakeInRateCurRound; //当前牌局的最大带入倍数
        @CommColumn(value = 156, required = false) public int operatorRemainTime; //当前操作的人剩余多少秒
        @CommColumn(value = 157, required = false) public byte[] playersSex; // 所有用户的性别
        @CommColumn(value = 158, required = false) public int pokerStates; //亮牌状态，0不亮,1亮第一张,2亮第二种,3两张都亮
        @CommColumn(value = 159, required = false) public int groupBet; // 前注
        @CommColumn(value = 160, required = false) public int resideMinTime; // 倒计时时间，还剩1min内，一开始为-1
        @CommColumn(value = 161, required = false) public int blindType; // 盲注表0为A表，1为B表
        @CommColumn(value = 162, required = false) public int rebuyLevel; // 重购的级别, 0为关
        @CommColumn(value = 163, required = false) public int rebuyCount; // 重购的次数, 0为关
        @CommColumn(value = 164, required = false) public int isAppend; // 增购开关0为关，1为开
        @CommColumn(value = 165, required = false) public int raiseBlindTime; // 升盲的时间，单位分钟
        @CommColumn(value = 166, required = false) public int matchCost; // 参赛的费用
        @CommColumn(value = 167, required = false) public int rebuyResideCount; // 重购剩余的次数
    }

    // 服务器主动下发的开始
    @CommClass(Code.REQ_MTT_ROOM_START_GAME)
    public static class MttRoomStartGame extends BaseCommData {
        @CommColumn(60) public byte success; // 0 表示开始成功，1表示失败
    }

    // 服务器的1min提醒
    @CommClass(Code.REQ_MTT_REMIND)
    public static class MttRemindCtrl extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int mttId; // Mtt id
        @CommColumn(131) public int mttType; // Mtt type 6/9人局
        @CommColumn(132) public String mttName; // Mtt name
        @CommColumn(133) public String ip; // game server ip
        @CommColumn(134) public int port; // game server port
    }

    // 每局开局收到的消息
    @CommClass(Code.REQ_MTT_Recv_Start_Infor)
    public static class MttStartInfoData extends BaseCommData {
        @CommColumn(60) public byte dealerIndex; //庄家编号
        @CommColumn(61) public byte bigBlindIndex; //大盲注编号
        @CommColumn(62) public byte smallBlindIndex; //小盲注编号
        @CommColumn(63) public byte nextOperatorSeatId; //下一位操作玩家的座位编号
        @CommColumn(130) public byte[] playerFirstCards; //玩家第一张牌
        @CommColumn(131) public byte[] playerSecondCards; //玩家第二张牌
        @CommColumn(132) public int smallBlindAnte; //小盲注
        @CommColumn(133) public int bigBlindAnte; //大盲注
        @CommColumn(134) public int[] playerChipsInDesk; //每个座位上玩家的筹码
        @CommColumn(135) public int[] missionCountDown; //任务倒计时 -1，完成
        @CommColumn(136) public int[] missionRewards; //任务奖励筹码数
        @CommColumn(137) public int playerChipsOutRoom; // 当前用户最新的房间外筹码
        @CommColumn(138) public int minTakeInRate; // 最小带入倍数
        @CommColumn(139) public int maxTakeInRate; // 最大带入倍数
        @CommColumn(140) public int isRebuy; // 是否重购0为否，1为是
        @CommColumn(141) public int isAppend; // 是否增购0为否，1为是
    }

    // 服务器下发的排名
    @CommClass(Code.REQ_MTT_Self_Ranking)
    public static class MttSelfRanking extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int totalRank; // 总排名
        @CommColumn(131) public int currentRank; // 参赛者的排名
    }

    // 获取统计的排名
    @CommClass(Code.REQ_MTT_Situation_Ranking)
    public static class MttSituationRanking extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int currentRank; // 参赛者自己的排名
        @CommColumn(131) public int[] allRank; // 所有参赛者的排名
        @CommColumn(132) public int[] allUserId;  // 所有用户的列表
        @CommColumn(133) public int[] allCoin;  // 所有用户记分牌
        @CommColumn(134) public int allLoser;  // 已经淘汰的用户
    }

    // 获取统计的盲注
    @CommClass(Code.REQ_MTT_Situation_Blind)
    public static class MttSituationBlind extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int currentLevel; // 当前的级别
    }

    // 获取统计的奖励
    @CommClass(Code.REQ_MTT_Situation_Reward)
    public static class MttSituationReward extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int totalReward; // 总奖池
        @CommColumn(131) public int[] singleReward; // 单个奖励
    }

    // 一次性获取所有参赛人员列表
    @CommClass(Code.REQ_MTT_All_People)
    public static class MttMatchAllPeople extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int[] allUserId; // 所有参赛者UserId
        @CommColumn(131) public String allUserNick; // 所有参赛者nick,对应userid数组
    }

    // 获取统计的奖励
    @CommClass(Code.REQ_DISCOVER_MTT_CHECK_RESULT)
    public static class MttSignCheckResult extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int totalReward; // 总奖池
        @CommColumn(131) public int[] singleReward; // 单个奖励
    }

    // 获取消息数量
    @CommClass(Code.REQ_MESSAGE_COUNT)
    public static class MessageCount extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int count; // 消息数量
    }

    // 获取消息列表
    @CommClass(Code.REQ_MESSAGE_LIST)
    public static class MessageList extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public String message;
    }

    // 消息已读响应
    @CommClass(Code.REQ_MESSAGE_READ)
    public static class MessageReadResponse extends BaseCommData {
        @CommColumn(60) public byte status;
    }

    // 消息推送（单条）
    @CommClass(Code.REQ_MESSAGE_SEND)
    public static class MessageSend extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public String message;
    }

    @CommClass(Code.REQ_SET_SPECTRUM_FAVORITE)
    public static class SetSpectrumFavoriteInfo extends BaseCommData {
        @CommColumn(61) public byte result; // 0: 成功 1: 超出收藏最大限制
        @CommColumn(62) public byte operation; // 0：取消收藏 1：收藏
        @CommColumn(130) public int round; // 第几手
    }

    @CommClass(Code.REQ_CLEAR_GAME_TABLE)
    public static class ClearGameTableInfo extends BaseCommData {
        @CommColumn(60) public byte unused; // 暂未使用此字段
    }

    @CommClass(Code.REQ_PAY_NEXT_PUBLIC_CARD)
    public static class PayNextPublicCardInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0成功 1失败 2请求失效 3筹码不足
        @CommColumn(130) public int chipsOutRoom; // 房间外剩余的筹码
    }

    @CommClass(Code.REQ_KEEP_SEAT)
    public static class KeepSeatInfo extends BaseCommData {
        @CommColumn(60) public byte status; // 0马上成功 1失败 2未达到最短时间 3下一手生效
        @CommColumn(130) public int userId;
        @CommColumn(131) public byte action; // 1申请离桌 2查询其它房间占座 3广播离桌开始生效
        @CommColumn(value = 132, required = false) public int seatId;
        @CommColumn(value = 133, required = false) public int remainSeconds;
        @CommColumn(value = 134, required = false) public String roomName;
    }

    @CommClass(Code.REQ_DELAY_ROOM_TIME)
    public static class RoomDelayTimeInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0成功 1失败
        @CommColumn(value = 130, required = false) public int addTimeMinutes;
        @CommColumn(value = 131, required = false) public int remainJewels = -1; // 剩余钻石，-1表示无效
    }

    // 牌局内的系统消息
    @CommClass(Code.REQ_MTT_System_Notify)
    public static class MttSystemNotify extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int type; // 消息类型1、拆分桌；2、升盲；3、奖池变动；4、实时战况；5、人员加入；6、人员离开
        @CommColumn(131) public String content; // 消息内容1、空；2、盲注级别；3、奖池总值；4、分钟和人数；5、昵称；6、昵称
    }

    // MTT报名反馈消息
    @CommClass(Code.REQ_MTT_APPLY_JOIN)
    public static class ApplyJoinData extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(61) public byte result;
        @CommColumn(value = 130, required = false) public String ipString;
        @CommColumn(value = 131, required = false) public String port;
    }

    // MTT申请重购记分牌结果
    @CommClass(Code.REQ_MTT_Rebuy_Score)
    public static class MttRebuyScore extends BaseCommData {
        @CommColumn(60) public byte status; // 0表示重购成功， 1服务器异常，2前一个重购申请中，3钻石充足，是否购买，4去商城，5正在审核
        @CommColumn(value = 130, required = false) public int resideCount; // 增购剩余次数
        @CommColumn(value = 131, required = false) public int jewel; // 兑换的钻石数
        @CommColumn(value = 132, required = false) public int chip; // 兑换的筹码数
    }

    // MTT钻石充足，申请重购
    @CommClass(Code.REQ_MTT_Jewel_Convert_Score)
    public static class MttJewelConvertScore extends BaseCommData {
        @CommColumn(60) public byte status; // 0表示重购成功， 1服务器异常，2前一个重购申请中，3正在审核
        @CommColumn(130) public int resideCount; // 增购剩余次数
    }

    // MTT接收房主通知的结果
    @CommClass(Code.REQ_MTT_Master_Notify)
    public static class MttMasterNotifyResult extends BaseCommData {
        @CommColumn(60) public byte status; // 0表示审核同意， 1服务器异常，2审核拒绝
        @CommColumn(130) public int type; // 0为重购通知，1为增购通知
        @CommColumn(131) public int resideCount; // 增购剩余次数
    }

    // MTT申请增购记分牌结果
    @CommClass(Code.REQ_MTT_Append_Score)
    public static class MttAppendScore extends BaseCommData {
        @CommColumn(60) public byte status; // 0成功，1失败
    }

    // MTT玩家管理列表
    @CommClass(Code.REQ_MTT_MANAGE_PLAYER)
    public static class ManagePlayerData extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public String playerList;
    }

    /**
     * SNG消息系列
     */
    // SNG获取统计的排名
    @CommClass(Code.REQ_SNG_SITUATION_RANKING)
    public static class SngSituationRanking extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int currentRank; // 参赛者自己的排名
        @CommColumn(131) public int[] allRank; // 所有参赛者的排名
        @CommColumn(132) public int[] allUserId;  // 所有用户的列表
        @CommColumn(133) public String allNickName;  // 所有用户的昵称
        @CommColumn(134) public int[] allCoin;  // 所有用户记分牌
        @CommColumn(135) public int allLoser;  // 已经淘汰的用户
        @CommColumn(136) public int[] observersIds; // 观众id
        @CommColumn(137) public String observersHeads; // 观众头像
        @CommColumn(138) public String observerNicks; // 观众昵称
        @CommColumn(139) public int currentBlind; // 当前盲注级别
        @CommColumn(140) public int currentHand; // 当前手数
    }

    @CommClass(Code.REQ_SNG_ENTER_ROOM)
    public static class SngEnterRoomData extends BaseCommData {
        @CommColumn(60) public byte succeed; // 返回状态 0成功 1 失败 2 房间已解散 3 房间已满
        @CommColumn(value = 61, required = false) public byte gameStatus; // 游戏状态  0:倒计时中  1:游戏中
        @CommColumn(value = 62, required = false) public byte countDownSeconds; // 倒计时间  s
        @CommColumn(value = 65, required = false) public byte bigBlindSeatId; // 大盲注座位编号
        @CommColumn(value = 66, required = false) public byte smallBlindSeatId; // 小盲注座位编号
        @CommColumn(value = 67, required = false) public byte dealerSeatId; // 庄家座位编号
        @CommColumn(value = 68, required = false) public byte curOperationSeatId; // 当前操作玩家编号
        @CommColumn(value = 69, required = false) public byte operationSeconds; // 房间允许操作时间
        @CommColumn(value = 70, required = false) public byte playerSeatId; // 服务器分配座位id
        @CommColumn(value = 71, required = false) public byte allowCheckCountIfTimeOut; // 允许最大的超时跟牌次数
        @CommColumn(value = 72, required = false) public byte checkCountTimedOut; // 已经利用了多少次超时跟牌的机会
        @CommColumn(value = 131, required = false) public byte[] publicCards; // 已经翻了的牌
        @CommColumn(value = 132, required = false) public int[] playerIds; // 玩家id
        @CommColumn(value = 133, required = false) public int[] playersStatus; // 玩家状态 1、让牌2、加注 3、跟注 4 、等待下一局
        @CommColumn(value = 134, required = false) public int[] playersCurRoundAntes; // 本轮 每个玩家的下注筹码数
        @CommColumn(value = 135, required = false) public String playerNicks; // 玩家昵称
        @CommColumn(value = 136, required = false) public int[] playersChipsInDesk; // 玩家桌上未下的筹码
        @CommColumn(value = 137, required = false) public String playerHeads; // 玩家头像
        @CommColumn(value = 138, required = false) public byte[] playerFirstCards; // 玩家第一张牌
        @CommColumn(value = 139, required = false) public byte[] playerSecondCards; // 玩家第二张牌
        @CommColumn(value = 140, required = false) public int bigBlindAnte; // 大盲注
        @CommColumn(value = 141, required = false) public int smallBlindAnte; // 小盲注
        @CommColumn(value = 142, required = false) public int potChips; // 奖池筹码数
        @CommColumn(value = 143, required = false) public int curRoundMaxAnte; // 本轮最大下注
        @CommColumn(value = 144, required = false) public int ownerId; // 本房间的房主ID
        @CommColumn(value = 145, required = false) public int totalTime; // 本房间进行的时间（单位：s）
        @CommColumn(value = 146, required = false) public byte takeInControl; // 是否开启了带入控制，0: 关闭 1: 开启
        @CommColumn(value = 147, required = false) public int playerChipsOutRoom; // 用户房间外的筹码
        @CommColumn(value = 148, required = false) public int playerChipsInRoom; // 用户房间内剩余的筹码
        @CommColumn(value = 149, required = false) public int playerRank; // 玩家当前排名
        @CommColumn(value = 150, required = false) public int pausedTime; // 暂停时间，0不暂停，大于0暂停
        @CommColumn(value = 151, required = false) public int[] loserRankArray; // 淘汰玩家排名 -1表示未淘汰
        @CommColumn(value = 152, required = false) public int operatorRemainTime; // 当前操作的人剩余多少秒
        @CommColumn(value = 153, required = false) public byte[] playersSex; // 所有用户的性别
        @CommColumn(value = 154, required = false) public int pokerStates; // 亮牌状态，0不亮,1亮第一张,2亮第二种,3两张都亮
        @CommColumn(value = 155, required = false) public int groupBet; // 前注
        @CommColumn(value = 156, required = false) public int[] chipsPool; // 各筹码池数量，可能为null
        @CommColumn(value = 157, required = false) public int minRaise; // 最小加注金额
        @CommColumn(value = 158, required = false) public int canRaise; // 是否能加注,0为不可以，1为可以
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 159, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        @CommColumn(value = 160, required = false) public int isTrusted; // 是否有被托管
        @CommColumn(value = 161, required = false) public int callChips; // 如果是自己操作，表示跟注所需筹码，0表示可以看牌
        @CommColumn(value = 162, required = false) public int raiseBlindTime; // 下级盲注倒计时（s）
        @CommColumn(value = 163, required = false) public int ipRestriction; // IP限制 0关闭 1开启
        @CommColumn(value = 164, required = false) public int gpsRestriction; // gps控制
    }

    // sng托管消息
    @CommClass(Code.REQ_SNG_TRUST_STATUS)
    public static class SngReqTrustOption extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败
        // 0取消托管 1托管 2进入请求时间 3托管时间到 4其它房间状态查询 5刷新其他人消息
        @CommColumn(61) public byte option;
        @CommColumn(130) public int[] trustStatus; // 托管状态数组 0没托管 1托管
    }

    // sng升盲消息
    @CommClass(Code.REQ_SNG_RAISE_BLIND)
    public static class SngRaiseBlind extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败
        @CommColumn(130) public int blindLevel; // 盲注级别
    }

    // sng个人排名
    @CommClass(Code.REQ_SNG_SELF_RANKING)
    public static class SngSelfRanking extends BaseCommData {
        @CommColumn(60) public byte status; // 0 成功 1 失败
        @CommColumn(130) public int[] rankArray; // 所有参赛者的排名
        @CommColumn(131) public int[] loserArray; // 已经淘汰玩家排名 0未淘汰 1淘汰
    }

    // sng结束战绩
    @CommClass(Code.REQ_SNG_ROOM_GAME_ENDED)
    public static class SngRoomEndInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 异常
        @CommColumn(value = 130, required = false) public int time; // 比赛时间
        @CommColumn(value = 131, required = false) public int hands; // 总手数
        @CommColumn(value = 132, required = false) public String bigNicks; // nick 第一名/第二名/第三名
        @CommColumn(value = 133, required = false) public String bigHeads; // 头像 第一名/第二名/第三名
        @CommColumn(value = 134, required = false) public String nick; // 昵称排名
        @CommColumn(value = 135, required = false) public int rank; // 自己的排名
        @CommColumn(value = 136, required = false) public String master; // 房主名称
    }

    // sng牌局本手延时
    @CommClass(Code.REQ_SNG_ADD_TIME)
    public static class SngAddTimeData extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 失败 2钻石不足 3非当前操作玩家 4次数超过限制
        @CommColumn(value = 130, required = false) public int playerId;
        @CommColumn(value = 131, required = false) public int addTime;
        @CommColumn(value = 132, required = false) public int costJewel;
        @CommColumn(value = 133, required = false) public int costChips;
        @CommColumn(value = 134, required = false) public int delayCount; // 本手累计使用延时次数
    }

    // SNG报名
    @CommClass(Code.REQ_SNG_PLAYER_APPLY)
    public static class SngPlayerApply extends BaseCommData {
        @CommColumn(60) public byte status; // 0成功 1失败 2筹码不足 3ip相同 4报名人数已满 5房间不存在
        @CommColumn(130) public int applyStatus; // 报名状态 0未报名 1审核中 2成功
        @CommColumn(131) public int applyPlayer; // 报名成功人数
    }

    // SNG消息通知
    @CommClass(Code.REQ_SNG_NOTICE_REMIND)
    public static class SngNoticeRemind extends BaseCommData {
        @CommColumn(60) public byte status;
        @CommColumn(130) public int type; // 通知类型 1开赛通知 2主动解散 3被动解散 4审核通过 5审核未通过
        @CommColumn(131) public int sngId; // sng room id
        @CommColumn(132) public String sngName; // sng name
        @CommColumn(133) public String ip; // game server ip
        @CommColumn(134) public int port; // game server port
    }

    /**
     * OMAHA消息系列
     */
    // omaha进入房间消息
    @CommClass(Code.REQ_OMAHA_ENTER_ROOM)
    public static class OmahaEnterRoomData extends BaseCommData {
        @CommColumn(60) public byte succeed; // 返回状态 0成功 1 失败 2 房间已解散 3 房间已满
        @CommColumn(value = 61, required = false) public byte gameStatus; // 游戏状态  0:倒计时中  1:游戏中
        @CommColumn(value = 62, required = false) public byte countDownSeconds; // 倒计时间  s
        @CommColumn(value = 65, required = false) public byte bigBlindSeatId; // 大盲注座位编号
        @CommColumn(value = 66, required = false) public byte smallBlindSeatId; // 小盲注座位编号
        @CommColumn(value = 67, required = false) public byte dealerSeatId; // 庄家座位编号
        @CommColumn(value = 68, required = false) public byte curOperationSeatId; // 当前操作玩家编号
        @CommColumn(value = 69, required = false) public byte operationSeconds; // 房间允许操作时间
        @CommColumn(value = 70, required = false) public byte playerSeatId; // 服务器分配座位id
        @CommColumn(value = 71, required = false) public byte allowCheckCountIfTimeOut; // 允许最大的超时跟牌次数
        @CommColumn(value = 72, required = false) public byte checkCountTimedOut; // 已经利用了多少次超时跟牌的机会
        @CommColumn(value = 131, required = false) public byte[] publicCards; // 已经翻了的牌
        @CommColumn(value = 132, required = false) public int[] playerIds; // 玩家id
        // 玩家状态 1:未操作过 2:跟注  3:加注  4:全下 5:让牌  6:弃牌 7托管 8等待下一局 9 等待入座 10 空坐（不展示可坐）11 留座离桌
        @CommColumn(value = 133, required = false) public int[] playersStatus;
        @CommColumn(value = 134, required = false) public int[] playersCurRoundAntes; // 本轮 每个玩家的下注筹码数
        @CommColumn(value = 135, required = false) public String playerNicks; // 玩家昵称
        @CommColumn(value = 136, required = false) public int[] playersChipsInDesk; // 玩家桌上未下的筹码
        @CommColumn(value = 137, required = false) public String playerHeads; // 玩家头像
        @CommColumn(value = 138, required = false) public byte[] playerFirstCards; // 玩家第一张牌
        @CommColumn(value = 139, required = false) public byte[] playerSecondCards; // 玩家第二张牌
        @CommColumn(value = 140, required = false) public int bigBlindAnte; // 大盲注
        @CommColumn(value = 141, required = false) public int smallBlindAnte; // 小盲注
        @CommColumn(value = 142, required = false) public int potChips; // 奖池筹码数
        @CommColumn(value = 143, required = false) public int curRoundMaxAnte; // 本轮最大下注
        @CommColumn(value = 144, required = false) public int ownerId; // 本房间的房主ID
        @CommColumn(value = 145, required = false) public String startTime; // 本房间开始计时的时间，0表示尚未开始计时
        @CommColumn(value = 146, required = false) public int totalTimeMinutes; // 本房间的总有效时间（单位：分钟）
        @CommColumn(value = 147, required = false) public byte takeInControl; // 是否开启了带入控制，0: 关闭 1: 开启
        @CommColumn(value = 148, required = false) public int playerChipsOutRoom; // 用户房间外的筹码
        @CommColumn(value = 149, required = false) public int playerChipsInRoom; // 用户房间内剩余的筹码
        // 用户房间内剩余的时间(秒），未开始则返回-1, -2无限局
        @CommColumn(value = 150, required = false) public int remainTime;
        @CommColumn(value = 151, required = false) public int pausedTime; // 暂停时间，0不暂停，大于0暂停
        @CommColumn(value = 152, required = false) public int minTakeInRateNextRound; // 下次生效的最小带入倍数
        @CommColumn(value = 153, required = false) public int maxTakeInRateNextRound; // 下次生效的最大带入倍数
        @CommColumn(value = 154, required = false) public int minTakeInRateCurRound; // 当前牌局的最小带入倍数
        @CommColumn(value = 155, required = false) public int maxTakeInRateCurRound; // 当前牌局的最大带入倍数
        @CommColumn(value = 156, required = false) public int operatorRemainTime; // 当前操作的人剩余多少秒
        @CommColumn(value = 157, required = false) public byte[] playersSex; // 所有用户的性别
        @CommColumn(value = 158, required = false) public byte[] pokerStates; // 0不亮牌，1亮牌
        @CommColumn(value = 159, required = false) public int groupBet; // 前注
        @CommColumn(value = 160, required = false) public int lastRequestAddInRemainTime; // 上次申请带入还剩多少时间超时（0表示没有待审批的申请）
        @CommColumn(value = 161, required = false) public int[] chipsPool; // 各筹码池数量，可能为null
        @CommColumn(value = 162, required = false) public int minRaise; // 最小加注金额
        @CommColumn(value = 163, required = false) public int canRaise; // 是否能加注,0为不可以，1为可以
        @CommColumn(value = 164, required = false) public int insuranceMode; // 1 开启保险，0关闭
        @CommColumn(value = 165, required = false) public int[] canPlayArray; // 是否打牌，0为不可以，1为可以
        @CommColumn(value = 166, required = false) public int[] seatStatusArray; // 0为刚坐下，>=1显示气泡
        @CommColumn(value = 167, required = false) public int isNeedTip; // 是否需要弹过庄补盲选择框 0不需要 1需要
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 168, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        // 房间类型（1：普通房 2：俱乐部有限时长 3：俱乐部无限时长）
        @CommColumn(value = 169, required = false) public int roomType;
        @CommColumn(value = 170, required = false) public int needBring; // 需要首次带入的筹码数（只在roomType为2时使用）
        @CommColumn(value = 171, required = false) public int isTrusted; // 是否有被托管
        @CommColumn(value = 172, required = false) public int callChips; // 如果是自己操作，表示跟注所需筹码，0表示可以看牌
        // 公共牌中哪些牌是用户花钱翻开的，翻开标记为1
        @CommColumn(value = 173, required = false) public byte[] publicCardsType;
        @CommColumn(value = 174, required = false) public int keepSeatInOtherRoom; // 0没有在其它房间留座离桌，1有
        // 留座离桌的人的倒计时剩余秒数，-1表示无效
        @CommColumn(value = 175, required = false) public int[] keepSeatRemainSeconds;
        @CommColumn(value = 176, required = false) public String userPayToShowNextCard; // 为""时表示没有人
        @CommColumn(value = 177, required = false) public int ipRestriction; // 0关闭 1开启
        @CommColumn(value = 178, required = false) public byte[] playerThirdCards; // 玩家第三张牌
        @CommColumn(value = 179, required = false) public byte[] playerFourthCards; // 玩家第四张牌
        @CommColumn(value = 180, required = false) public int pokerType; // 牌型提示
        @CommColumn(value = 181, required = false) public int gpsRestriction; // 0关闭 1开启
    }

    // omaha赢牌下发
    @CommClass(Code.REQ_OMAHA_RECV_WINNER)
    public static class OmahaGameWinData extends BaseCommData {
        @CommColumn(130) public byte[] winnerSeatIds; // 收筹码玩家的座位编号
        @CommColumn(131) public int[] winnerPlayerIds; // 玩家id
        @CommColumn(132) public int[] winChips; // 收筹码数
        // 赢的牌类型	1:皇家同花顺  2:同花顺  3:4条 4:葫芦 5:同花 6:顺子 7:三张 8:两对 9:一对 10:高牌
        @CommColumn(133) public byte[] winTypes;
        @CommColumn(134) public byte[] winnerFlag; // 是否赢家 0：是 1：不是
        @CommColumn(135) public int[] realWinChips; // 扣除下的筹码最终赢得的筹码数
        @CommColumn(136) public byte[] winCards; // 哪几张牌赢的 	系统牌 0-4 底牌 5-8
        @CommColumn(137) public int[] playerLastChips; // 每个玩家最后显示筹码
        @CommColumn(138) public int[] playerIds; // 每个玩家id
        @CommColumn(140) public byte[] firstCards; // 玩家第一张牌	有牌 0-51 无牌 -1
        @CommColumn(141) public byte[] secondCards; // 玩家第二张牌	有牌 0-51 无牌 -1
        @CommColumn(142) public byte[] biggestWinner; // 是否取得最大手牌 	0：是，1：不是
        @CommColumn(143) public int chipsInNextGame;   // 玩家下一局将会拥有的筹码（包括了下局生效的带入请求）
        @CommColumn(value = 144, required = false) public byte[] muckStatus; // 玩家是否是盖牌  0：不是  1：是
        @CommColumn(value = 145, required = false) public byte[] thirdCards; // 玩家第三张牌
        @CommColumn(value = 146, required = false) public byte[] fourthCards; // 玩家第四张牌
    }

    // omaha开局消息
    @CommClass(Code.REQ_OMAHA_RECV_START_INFOR)
    public static class OmahaStartInfoData extends BaseCommData {
        @CommColumn(60) public byte dealerIndex; // 庄家编号
        @CommColumn(61) public byte bigBlindIndex; // 大盲注编号
        @CommColumn(62) public byte smallBlindIndex; // 小盲注编号
        @CommColumn(63) public byte nextOperatorSeatId; // 下一位操作玩家的座位编号
        @CommColumn(130) public byte[] playerFirstCards; // 玩家第一张牌
        @CommColumn(131) public byte[] playerSecondCards; // 玩家第二张牌
        @CommColumn(132) public int smallBlindAnte; // 小盲注
        @CommColumn(133) public int bigBlindAnte; // 大盲注
        @CommColumn(134) public int[] playerChipsInDesk; // 每个座位上玩家的筹码
        @CommColumn(135) public int[] missionCountDown; // 任务倒计时 -1，完成
        @CommColumn(136) public int[] missionRewards; // 任务奖励筹码数
        @CommColumn(137) public int playerChipsOutRoom; // 当前用户最新的房间外筹码
        @CommColumn(138) public int minTakeInRate; // 最小带入倍数
        @CommColumn(139) public int maxTakeInRate; // 最大带入倍数
        @CommColumn(140) public int minRaise; // 最小加注金额
        @CommColumn(141) public int canRaise; // 是否能加注
        @CommColumn(142) public int[] canPlayArray; // 是否打牌，0为不可以，1为可以
        @CommColumn(143) public int[] seatStatusArray; // 0为刚坐下，>=1显示气泡
        @CommColumn(144) public int pokerHand; // 牌局第几手
        @CommColumn(145) public byte[] isNeedPost; // 是否要补盲 0否，1是
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 146, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        @CommColumn(value = 147, required = false) public int[] straddlePop; // 显示straddle气泡
        // 0straddle成功， 1straddle失败，2不处理
        @CommColumn(value = 148, required = false) public int isCurStraddle;
        @CommColumn(value = 149, required = false) public int callChips; // 跟牌所需筹码
        @CommColumn(value = 150, required = false) public byte[] playerThirdCards; // 玩家第三张牌
        @CommColumn(value = 151, required = false) public byte[] playerFourthCards; // 玩家第四张牌
    }

    // omaha亮牌消息
    @CommClass(Code.REQ_OMAHA_SHOWDOWN)
    public static class OmahaShowPokerInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功
        @CommColumn(value = 61, required = false) public byte[] status; // 0不亮牌，1亮牌
    }

    // omaha allin下发牌
    @CommClass(Code.REQ_RECV_PLAYER_CARDS)
    public static class OmahaPlayerCardsData extends BaseCommData {
        @CommColumn(130) public byte[] playerSeatIds;
        @CommColumn(131) public byte[] firstCards;
        @CommColumn(132) public byte[] secondCards;
        @CommColumn(133) public byte[] thirdCards;
        @CommColumn(134) public byte[] fourthCards;
    }

    /**
     * 大菠萝消息系列
     */
    // 大菠萝进入房间消息
    @CommClass(Code.REQ_PINEAPPLE_ENTER_ROOM)
    public static class PineappleEnterRoomData extends BaseCommData {
        @CommColumn(60) public byte succeed; // 返回状态 0成功 1 失败 2 房间已解散 3 房间已满
        @CommColumn(value = 61, required = false) public byte gameStatus; // 游戏状态  0:倒计时中  1:游戏中
        @CommColumn(value = 62, required = false) public byte countDownSeconds; // 倒计时间  s
        @CommColumn(value = 65, required = false) public byte bigBlindSeatId; // 大盲注座位编号
        @CommColumn(value = 66, required = false) public byte smallBlindSeatId; // 小盲注座位编号
        @CommColumn(value = 67, required = false) public byte dealerSeatId; // 庄家座位编号
        @CommColumn(value = 68, required = false) public byte curOperationSeatId; // 当前操作玩家编号
        @CommColumn(value = 69, required = false) public byte operationSeconds; // 房间允许操作时间
        @CommColumn(value = 70, required = false) public byte playerSeatId; // 服务器分配座位id
        @CommColumn(value = 71, required = false) public byte allowCheckCountIfTimeOut; // 允许最大的超时跟牌次数
        @CommColumn(value = 72, required = false) public byte checkCountTimedOut; // 已经利用了多少次超时跟牌的机会
        @CommColumn(value = 131, required = false) public byte[] publicCards; // 已经翻了的牌
        @CommColumn(value = 132, required = false) public int[] playerIds; // 玩家id
        // -1:无人 0准备摆牌/或开始状态 1玩家确认了但未轮到该玩家操作 2玩家确认了 8等待下一盘 15占桌
        @CommColumn(value = 133, required = false) public int[] playersStatus;
        @CommColumn(value = 134, required = false) public int[] playersCurRoundAntes; // 本轮 每个玩家的下注筹码数
        @CommColumn(value = 135, required = false) public String playerNicks; // 玩家昵称
        @CommColumn(value = 136, required = false) public int[] playersChipsInDesk; // 玩家桌上未下的筹码
        @CommColumn(value = 137, required = false) public String playerHeads; // 玩家头像
        @CommColumn(value = 138, required = false) public String lastHandCards; // 最近一次手牌
        @CommColumn(value = 139, required = false) public String threeHandCards; // 3道手牌
        @CommColumn(value = 140, required = false) public int[] fantasyArray; // 玩家范特西次数
        @CommColumn(value = 141, required = false) public int smallBlindAnte; // 小盲注
//        @CommColumn(value = 142, required = false) public int potChips; // 奖池筹码数
        @CommColumn(value = 143, required = false) public int curRoundMaxAnte; // 本轮最大下注
        @CommColumn(value = 144, required = false) public int ownerId; // 本房间的房主ID
        @CommColumn(value = 145, required = false) public int playTime; // 本房间进行的时间（单位：s）
        @CommColumn(value = 146, required = false) public byte takeInControl; // 是否开启了带入控制，0: 关闭 1: 开启
        @CommColumn(value = 147, required = false) public int playerChipsOutRoom; // 用户房间外的筹码
        @CommColumn(value = 148, required = false) public int playerChipsInRoom; // 用户房间内剩余的筹码
        @CommColumn(value = 150, required = false) public int pausedTime; // 暂停时间，0不暂停，大于0暂停
        @CommColumn(value = 152, required = false) public int operatorRemainTime; // 当前操作的人剩余多少秒
        @CommColumn(value = 153, required = false) public byte[] playersSex; // 所有用户的性别
        @CommColumn(value = 154, required = false) public int pokerStates; // 亮牌状态，0不亮,1亮第一张,2亮第二种,3两张都亮
        @CommColumn(value = 157, required = false) public int minRaise; // 最小加注金额
        @CommColumn(value = 158, required = false) public int canRaise; // 是否能加注,0为不可以，1为可以
        // 当前剩余信用额度（Integer.MAX_VALUE表示没有限制）
        @CommColumn(value = 159, required = false) public int remainCreditValue = Integer.MAX_VALUE;
        @CommColumn(value = 160, required = false) public int isTrusted; // 是否有被托管
        @CommColumn(value = 163, required = false) public int ipRestriction; // IP限制 0关闭 1开启
        @CommColumn(value = 164, required = false) public int gpsRestriction; // gps控制
        @CommColumn(value = 165, required = false) public int minTakeInRateCurRound; // 当前最小带入倍数
        @CommColumn(value = 166, required = false) public int maxTakeInRateCurRound; // 当前最大带入倍数
        @CommColumn(value = 167, required = false) public String foldArray; // 玩家的弃牌区
        @CommColumn(value = 168, required = false) public int fantasyTotalSeconds; // 允许范特西的总共操作时间
        @CommColumn(value = 169, required = false) public int[] userOperationRemainSecs; // 用户操作剩余时间
        @CommColumn(value = 175, required = false) public int[] keepSeatRemainSeconds; // 剩余留座时间 s
        @CommColumn(value = 176, required = false) public int nextStartSeconds; // 再来一局按钮剩余时间 s
    }

    // 系统首次发牌
    @CommClass(Code.REQ_PINEAPPLE_RECV_START_INFOR)
    public static class PineappleStartInfoData extends BaseCommData {
        @CommColumn(60) public byte dealerIndex; // 庄家编号
        @CommColumn(61) public byte nextOperatorSeatId; // 下一个操作的玩家
        @CommColumn(130) public String playerFirstCards; // 第一轮手牌
        @CommColumn(131) public int[] playerChipsInDesk; // 玩家桌面筹码数组
        @CommColumn(132) public int[] fantasyArray; // 玩家是否为范特西 0否 1是
        @CommColumn(133) public int residueChip; // 玩家剩余筹码
        @CommColumn(134) public int minTakeInRate; // 最小带入倍数
        @CommColumn(135) public int maxTakeInRate; // 最大带入倍数
        @CommColumn(136) public int pokerHand; // 牌局第几手
        @CommColumn(137) public int fantasySeconds; // 范特西操作的秒数
        @CommColumn(138) public int[] canPlayArray; // 能否打牌 0否 1是
    }

    // 接收每轮发牌，3张
    @CommClass(Code.REQ_PINEAPPLE_RECV_CARDS)
    public static class PineappleRecvCards extends BaseCommData {
        @CommColumn(130) public int nextOperatorSeatId; // 下一个操作的玩家
        @CommColumn(131) public String cards; // 本轮发牌
    }

    // 玩家点击确定,确认摆好
    @CommClass(Code.REQ_PINEAPPLE_CONFIRM_CLICK)
    public static class PineappleConfirmClick extends BaseCommData {
        @CommColumn(60) public byte success; // 返回状态 0成功 1 失败
    }

    // 玩家点击确定后，分发给自己其他人的摆牌结果
    @CommClass(Code.REQ_PINEAPPLE_RECV_RESULT)
    public static class PineappleRecvCardsResult extends BaseCommData {
        @CommColumn(61) public byte seatId; // 座位ID
        @CommColumn(62) public byte nextOperatorSeatId; // 下一个操作玩家id
        @CommColumn(131) public int userId; // 当前操作玩家id
        @CommColumn(132) public int[] cardArray; // 所有已经摆好的牌
        @CommColumn(133) public int[] localArray; // 本轮摆好牌的位置
        @CommColumn(134) public int foldCard; // 本轮弃掉的牌，没有为-1
        @CommColumn(135) public int isConfirm; // 是不是玩家主动摆牌 1否 0是
    }

    // 每局赢牌的结果消息
    @CommClass(Code.REQ_PINEAPPLE_GAME_RESULT)
    public static class PineappleGameWinResult extends BaseCommData {
        @CommColumn(130) public int[] totalScoreArray; // 玩家总分数组
        @CommColumn(131) public int[] totalChipArray; // 玩家输赢的筹码
        @CommColumn(132) public int[] detailScoreArray; // 每到的详细分
        @CommColumn(133) public int[] resultArray; // 玩家摆牌的结果（-1空位 0普通 1爆牌 2范特西）
        @CommColumn(134) public int[] cardTypeArray; // 每道的牌型
    }

    // 展示范特西玩家的手牌
    @CommClass(Code.REQ_PINEAPPLE_FANTASY_CARDS)
    public static class PineappleFantasyResult extends BaseCommData {
        @CommColumn(130) public String cards; // '@,'分隔各个人, '@%'分隔牌
    }

    // 牌局结束页
    @CommClass(Code.REQ_PINEAPPLE_ROOM_GAME_ENDED)
    public static class PineappleRoomEndInfo extends BaseCommData {
        @CommColumn(60) public byte success; // 0 成功 1 异常
        @CommColumn(value = 130, required = false) public String bigNicks; // nick 土豪/鲨鱼/大鱼
        @CommColumn(value = 131, required = false) public String bigHeads; // 头像 土豪/鲨鱼/大鱼
        @CommColumn(value = 132, required = false) public int[] playerIds;
        @CommColumn(value = 133, required = false) public int[] scores; // 房间所有用户总战绩
        @CommColumn(value = 134, required = false) public int[] handCounts; // 房间所有用户总手数
        @CommColumn(value = 135, required = false) public int[] takeIns; // 房间所有用户总带入
        @CommColumn(value = 136, required = false) public int takeInSum; // 房间所有用户带入总和
        @CommColumn(value = 137, required = false) public String playerNicks;
        @CommColumn(value = 138, required = false) public int maxPOT; // 该局最大池底
        @CommColumn(value = 139, required = false) public int insuranceMode; // 保险模式 1为开启保险
        @CommColumn(value = 140, required = false) public int insurancePool; // 保险池收支情况
    }

    // 玩家点击再来一局的返回
    @CommClass(Code.REQ_PINEAPPLE_START_NEXT_HAND)
    public static class PineappleStartNextHand extends BaseCommData {
        @CommColumn(60) public byte success; // 返回状态 0成功 1 失败
    }
}
