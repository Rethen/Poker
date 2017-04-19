package com.changdupay.commonInterface;



public class CommonConst {
	
	//主工程：1：支付SDK
	public static final int MAIN_MODELID = 1;
	public static final int PAY_MODELID = 2;
	
	//接口返回定义
	//共通用这个范围段：成功0~999，失敗-1~-999
    //其他模块用模块定义要加上模块id前缀   例如聊天id为2，则成功范围是2000~2999，失败范围是-2000~-2999
	public static final int COM_RET_OK = 0;  //返回成功
	public static final int COM_RET_NO_MODEL_REGISTER = -1000; //模块未注册
	public static final int COM_RET_NO_ID_DIFINE = -1001;  //模块内ID未找到
	public static final int COM_RET_PARA_ERROR = -1;  //参数错误
	public static final int COM_RET_ERROR = -2;  //返回错误
	public static final int COM_RET_NO_NETWORK = -3;  //无网络
	
	//ID分配说明：
	//1.大多数接口, ID从 n0001开始  (n为模块ID)
	//2.接口实现中 有耗时或者通信, ID从 n1001开始
	//3.接口实现中 需要跳转界面等， ID从 n2001开始
	
	//主工程接口id定义 :10001~19999
	public static final int MAIN_CHECK_VIEW_CLICK_10001 = 10001;//两次点击时间判断响应
	public static final int MAIN_CLEAR_PULLDATA_10002 = 10002;//清理异步请求数据
	
	
	//耗时业务
	public static final int MAIN_REFRESH_COIN_11001 = 11001; //刷新金币
	public static final int MAIN_CHANGE_URL_10003 = 10003; //替换URL地址
	public static final int MAIN_SEND_UMENG_EVENT_10004 = 10004; //替换URL地址
	
	
}
