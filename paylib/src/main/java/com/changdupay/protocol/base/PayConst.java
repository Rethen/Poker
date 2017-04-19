package com.changdupay.protocol.base;

public class PayConst {
	/* 需要主工程传入，不同项目可变 */
	// 应用ID
	public static long APPID = 3333;
	// 版本
	public static String VER = "1"; // 6 充值推荐
									// 7 联通沃充值
	// 商品ID
	public static long MERCHANDISEID = 3333;
	// 商品名称
	public static String MERCHANDISENAME = "元宝充值";
	// 移动MM
	public static String MobileMMAPPID = "300008992436";
	public static String MobileMMAPPKEY = "932370E160FB5C2AF011749F83AE88F4";

	public static String COIN_NAME = "元宝";

	/* 需要主工程传入 结束 */
	public static boolean isShowMobileMM = true;
	public static final int SINGLE_BOOK_NO_SHOW_CODE = 11; // 单行本不显示移动MM
	public static final String SHOW_CODE_PACKAGE_NAME_CHANGDU = "net.zy.farm"; // 畅读包名
	public static final String SHOW_CODE_PACKAGE_NAME_ANDREAD = "com.jiasoft.swreader"; // 安卓读书包名

	// 平台类型
	public static final int PLAT_ANDROID = 4;// android平台
	public static final int PLAT_IPHONE = 1;// iphone平台
	public static final int PLAT_IPAD = 7;// ipad平台

	public interface DataFormat {
		int JSON = 1; // json
		int BINARY = 2; // 二进制
	}

	public static String SessionID = java.util.UUID.randomUUID().toString();

	// 通用请求数据url
	//public static String URL_COMMON_REQUEST = "http://mpay.51ttnc.cn/paysdk.ashx";
	public static String URL_COMMON_REQUEST = "http://mpay.hipokers.com/PaySdk.ashx";
	// 配置信息url
	public static String URL_DYNAMIC_KEY = URL_COMMON_REQUEST + "?MerchantID=%d&AppID=%d&ParserVer=%s&Ver=%s&SessionID=%s&DynamicAppKey=%s&Sign=%s";
	// 商户信息
	public static long MERCHANTID = 1001;

	// 应用程序Key
	public static String APPKEY = "463045314432433342344135393638373738363932393532";

	// 支付密码变更KEY
	public static String PAY_PASSWORD_KEY = "zk.~,q5[%tWt-u%m3=#Vrg(er1vu[tqnd555550]";

	// Content格式
	public static final int FORMAT = DataFormat.BINARY;
	// 加密类型 1:MD5 2:RSA 3:3DES
	public static final int SIGNTYPE = 1;
	// 平台类型
	public static final int OSTYPE = PLAT_ANDROID;
	// 返回数据类型
	public static final int ReturnFormat = DataFormat.BINARY;
	// 是否压缩
	public static final int HAS_COMPRESSED = 0;
	// 用户 IP地址
	public static final int IPADDRESS = 0;
	// 解析器版本号
	public static final String PARSER_VER = "3";

	// 优惠活动列表图标
	public static final int PREFERENTIAL_ICON_ACTION = 9998;
	// 财付通银行列表
	public static final int ONECLICK_BANKLIST_ACTION = 9999;
	// Action
	// 获取动态秘钥
	public static final int DYNAMICKEY_ACTION = 10000;
	// 支付
	public static final int PAY_ACTION = 11000;

	// 登陆
	public static final int LOGIN_ACTION = 110001;

	// 令牌登陆
	public static final int TOKEN_LOGIN_ACTION = 110002;

	// 验证手机号是否可用
	public static final int MOBILEPHONECHECK_ACTION = 110003;

	// 验证邮箱是否可用
	public static final int EMAILCHECK_ACTION = 110004;

	// 验证密码是否可用
	public static final int PASSWORDCHECK_ACTION = 110005;

	// 获取手机验证码
	public static final int VERIFYCODE_ACTION = 110006;

	// 手机注册
	public static final int MOBILEPHONEREGISTER_ACTION = 110007;

	// 邮箱注册
	public static final int EMAILREGISTER_ACTION = 110008;

	// 校验手机验证码
	public static final int CHECK_VERIFYCODE_ACTION = 110009;

	// 加载帐户信息
	public static final int GET_ACCOUNTINFO_ACTION = 110010;

	// 小额支付免密设置
	public static final int SMALLPAY_ACTION = 120003;

	// 创建订单
	public static final int ORDERCREATE_ACTION = 120006;

	// 创建订单
	public static final int RECHARGE_ACTION = 120007;

	// 获取账单
	public static final int GETBILL_ACTION = 120008;

	// 加载订单详情
	public static final int GETBILL_DETAIL_ACTION = 120009;

	// 获取收入列表
	public static final int GET_INCOME_ACTION = 120010;

	// 加载收入详情
	public static final int GET_INCOME_DETAIL_ACTION = 120011;

	// 数月支出订单列表
	public static final int GET_MONTHS_OUTLAY_ACTION = 120012;

	// 数月收入订单列表
	public static final int GET_MONTHS_INCOME_ACTION = 120013;

	// 应用账单列表
	public static final int GET_APPBILL_ACTION = 120014;

	// 数月应用账单列表
	public static final int GET_MONTHS_APPBILL_ACTION = 120015;

	// 校验中国移动的短信验证码
	public static final int CHECK_CHINAMOBILE_VERIFYCODE_ACTION = 120016;

	// 推送服务
	public static final int PUSH_SERVICE_ACTION = 130001;

	// 反馈
	public static final int FEEDBACK_ACTION = 130002;

	// 获取用户头像+用户等级图片等用户信息
	public static final int GET_CHANGDUREADER_USERINFO_ACTION = 1017;

	// 支付返回执行方式
	public interface PayExecType {
		int Jump = 1;
		int LaunchApp = 2;
		int SendSms = 3;
	}

	// 判断是否是安卓读书的包
	// 暂时根据AppID判断
	public static boolean isAndReader() {
		if (APPID >= 5000) {
			return true;
		}
		return false;
	}

}
