package com.poseidon.pokers.domain.entity.socket;

/**
 * Created by 42524 on 2017/4/13.
 */

public class Code {

    public final static int REQ_Login_LoginServer = 1;
    public final static int REQ_Login_ResourceServer = 2;
    public final static int REQ_Check_Version = 3;
    public final static int REQ_Enter_Room = 44;
    public final static int REQ_Seat_Action = 45;
    public final static int REQ_Send_Action = 47;
    public final static int REQ_Recv_Action = 49;
    public final static int REQ_Recv_Leave = 50;
    public final static int REQ_Recv_Sit_Down = 51;

    public final static int REQ_Recv_System_Cards = 52;

    public final static int REQ_Recv_Winner = 53;

    public final static int REQ_Recv_Readytime = 55;

    public final static int REQ_Recv_Start_Infor = 56;

    public final static int REQ_Add_Chips = 57;

    public final static int REQ_GET_PEOPLE_DATA = 66;

    public final static int REQ_ROOM_START_GAME = 67;

    public final static int REQ_ROOM_GAME_ENDED = 68;

    public final static int REQ_ROOM_TAKE_IN_CONTROL_SWITCH = 69;

    public final static int REQ_ROOM_TAKE_IN_CONTROL_PERMIT = 70;

    public final static int REQ_ROOM_TAKE_IN_RANGE_CHANGE = 71;

    public final static int REQ_Recv_Player_Cards = 72;
    //申请亮牌
    public final static int REQ_SHOWDOWN = 73;

    public final static int REQ_Offline = 99;

    // 开始发牌之前，有前注和straddle
    public final static int REQ_Start_Game_Before = 156;

    // straddle request
    public final static int REQ_Straddle_Request = 157;

    // 托管消息
    public final static int REQ_Trust_Status = 158;

    // 解散牌局消息
    public final static int REQ_Dismiss_Message = 159;

    //房主解散房间
    public final static int REQ_ROOM_STATUS = 182;

    //请求实时战况
    public final static int REQ_SITUATION_INFO = 185;

    //牌桌请求延时
    public final static int REQ_ADD_TIME = 186;

    // 牌桌请求暂停
    public final static int REQ_PAUSE_GAME = 187;

    // 上局回顾
    public final static int REQ_GAME_PREV_GAME_REVIEW = 191;

    // 房间设置
    public final static int REQ_ROOM_CONTROL = 197;

    // 有人申请了带入，并且此带入有时限（仅通知房主）
    public final static int REQ_NEW_ADD_IN_WITH_TIMEOUT = 200;

    // 获取带有时限的申请带入列表（仅给房主）
    public final static int REQ_GET_ADD_IN_WITH_TIMEOUT = 201;

    // 带有时限的申请带入请求已超时（仅通知房主）
    public final static int REQ_ADD_IN_TIMEOUT = 202;

    // 已阅带时限的申请带入（仅房主可用）
    public final static int REQ_ADD_IN_LIST_READ = 203;

    // 同意/拒绝 带时限的请求（仅房主可用）
    public final static int REQ_PERMIT_ADD_IN_LIST = 204;

    // 获取消息数量
    public final static int REQ_MESSAGE_COUNT = 205;

    // 获取消息列表
    public final static int REQ_MESSAGE_LIST = 206;

    // 消息已读回应
    public final static int REQ_MESSAGE_READ = 207;

    // 单个消息推送
    public final static int REQ_MESSAGE_SEND = 208;

    // 在牌局中收藏牌谱
    public final static int REQ_SET_SPECTRUM_FAVORITE = 217;

    // 收集筹码
    public final static int REQ_COLLECT_USERS_CHIPS = 220;

    // 过庄或补盲的请求
    public final static int REQ_WAIT_OR_POST = 222;

    // 控制带入普通推送
    public final static int REQ_CLIENT_CTRL_COMMON_PUSH = 230;

    // 请求控制带入记录
    public final static int REQ_GET_CLIENT_REQ_RECORD = 231;

    // 控制带入过期通知
    public final static int REQ_GAME_OVER_NOTICE = 232;

    // 已阅上报，由房主上报
    public final static int REQ_CLIENT_READ_ALREADY = 233;

    // 房主同意/拒绝请求
    public final static int REQ_PORCESS_MESSAGE = 234;

    //-----请求方
    // 自己控制带入的请求结果推送
    public final static int REQ_MINE_REQ_RESULT_PUSH = 235;

    // 请求自己控制带入记录
    public final static int REQ_GET_MINE_REQ_RECORD = 236;

    /* 保险 */
    // 触发保险
    public final static int REQ_INSURANCE_BEGIN_BUY = 400;

    // 投保及反馈
    public final static int REQ_INSURANCE_BUY = 401;

    // 赔付下发
    public final static int REQ_GET_INSURANCE_PAY = 402;

    // 投保加时
    public final static int REQ_INSURANCE_ADD_TIME = 403;

    // 保险模式无法触发（OUTS==0 || OUTS>14)
    public final static int REQ_INSURANCE_TRIGGER_ERROR = 404;

    // 请求下发下张公共牌
    public static final int REQ_PAY_NEXT_PUBLIC_CARD = 410;

    // 系统通知清空牌桌
    public static final int REQ_CLEAR_GAME_TABLE = 411;

    // 请求离座留桌
    public static final int REQ_KEEP_SEAT = 412;

    // 请求延长牌桌时间
    public static final int REQ_DELAY_ROOM_TIME = 413;

    // mtt比赛提醒
    public final static int REQ_MTT_REMIND = 504;

    // 服务器主动下发的mtt排名
    public final static int REQ_MTT_Self_Ranking = 505;

    // mtt的排名统计
    public final static int REQ_MTT_Situation_Ranking = 506;

    // mtt的盲注统计
    public final static int REQ_MTT_Situation_Blind = 507;

    // mtt的奖励统计
    public final static int REQ_MTT_Situation_Reward = 508;

    // 参赛mtt所有人员名单
    public final static int REQ_MTT_All_People = 509;

    // mtt牌局的系统消息
    public final static int REQ_MTT_System_Notify = 510;

    // mtt牌局中重购请求
    public final static int REQ_MTT_Rebuy_Score = 511;

    // mtt牌局中重购钻石兑换
    public final static int REQ_MTT_Jewel_Convert_Score = 512;

    // mtt牌局中增购请求
    public final static int REQ_MTT_Append_Score = 513;

    // mtt牌局中房主的通知
    public final static int REQ_MTT_Master_Notify = 514;

    // 进入mtt的比赛
    public final static int REQ_MTT_Enter_Room = 544;

    // mtt每局开局前的消息
    public final static int REQ_MTT_Recv_Start_Infor = 556;

    // mtt比赛开始的消息
    public final static int REQ_MTT_ROOM_START_GAME = 567;

    // 请求MTT报名接口
    public final static int REQ_MTT_APPLY_JOIN = 521;

    // MTT管理页管理玩家列表
    public final static int REQ_MTT_MANAGE_PLAYER = 522;

    // 获取MTT比赛审核结果推送
    public final static int REQ_DISCOVER_MTT_CHECK_RESULT = 601;

    // 报名
    public final static int REQ_SNG_PLAYER_APPLY = 704;
    // sng牌局请求实时战况
    public final static int REQ_SNG_SITUATION_RANKING = 705;
    // sng升盲消息
    public final static int REQ_SNG_RAISE_BLIND = 706;
    // sng个人排名
    public final static int REQ_SNG_SELF_RANKING = 707;

    // sng消息通知
    public final static int REQ_SNG_NOTICE_REMIND = 711;
    // 进入sng桌面
    public final static int REQ_SNG_ENTER_ROOM = 744;
    // sng托管消息
    public final static int REQ_SNG_TRUST_STATUS = 758;
    // sng结束战绩
    public final static int REQ_SNG_ROOM_GAME_ENDED = 768;
    // 牌桌请求延时
    public final static int REQ_SNG_ADD_TIME = 786;

    // omaha进入enterroom
    public final static int REQ_OMAHA_ENTER_ROOM = 844;
    // omaha赢牌下发
    public final static int REQ_OMAHA_RECV_WINNER = 853;
    // omaha发牌通知
    public final static int REQ_OMAHA_RECV_START_INFOR = 856;
    // omaha allin下发手牌
    public final static int REQ_RECV_PLAYER_CARDS = 872;
    // omaha申请亮牌
    public final static int REQ_OMAHA_SHOWDOWN = 873;

    // 大菠萝进入enterroom
    public final static int REQ_PINEAPPLE_ENTER_ROOM = 944;
    // 系统首次发牌，5张或范特西
    public final static int REQ_PINEAPPLE_RECV_START_INFOR = 956;
    // 接收每轮发牌，3张
    public final static int REQ_PINEAPPLE_RECV_CARDS = 957;
    // 玩家点击确定,确认摆好
    public final static int REQ_PINEAPPLE_CONFIRM_CLICK = 958;
    // 接收其它玩家的摆牌
    public final static int REQ_PINEAPPLE_RECV_RESULT = 959;
    // 每局下发的结果
    public final static int REQ_PINEAPPLE_GAME_RESULT = 960;
    // 开始下一局的按钮点击
    public final static int REQ_PINEAPPLE_START_NEXT_HAND = 962;
    // 范特西摆牌展示
    public final static int REQ_PINEAPPLE_FANTASY_CARDS = 963;
    // 牌局结束页
    public final static int REQ_PINEAPPLE_ROOM_GAME_ENDED = 968;
}
