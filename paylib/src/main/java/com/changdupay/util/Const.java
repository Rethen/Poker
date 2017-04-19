package com.changdupay.util;

import android.app.Activity;



public interface Const {
	public static String INTENT_PRE = "com.changdupay.cn.action.";
	
	public static String VerifyCodePhoneNumbers[] = {"10658080773"};//{"10656666", "1065800885799", "106902692902", "106571203858160" }; 
	
	public static String VIP_SERVICE_PHONE 		= "0591-87085779";
	public static String NOVIP_SERVICE_PHONE 	= "0591-87085787";
	public static String ALIPAY_PACKAGE_NAME 	= "com.alipay.android.app";
	public static String ALIPAY_PACKAGE_NAME2 	= "com.eg.android.AlipayGphone";	
	public static String ONECLCIK_PACKAGE_NAME 	= "com.oneclick.android.app";
	public static String PAY_PACKAGE_NAME 	= "com.changdupay.android.app";
	public static String PAY_DEFAULT_CONFIG_FILENAME 	= "defaultconfig.xml";
	//合作方银行代码列表查询接口
//	public static String ONECLICK_BANKCONFIG_URL = "https://cl.tenpay.com/cgi-bin/clientv1.0/freepay_banklist.cgi";
	
//	public static String APP_KEY 	= "463045314432433342344135393638373738363932393532";
	public static String RSA_PUBLIC_KEY = "e9fcb7f959934c422f6f178c608db9809a17d885f45d90351d2c1d4a5f27e83374bca21c36035208b2b24645bd72a3165449351e0e7d1783ab624b9c866ce583";
	
	public static int CHANGDU_RATIO 	= 100;
	public static int SORT_ID = 9999;
	public interface Action {
		String login_activity 	= INTENT_PRE + "login";//
		String phone_register	= INTENT_PRE + "phone_register";
		String email_regisert	= INTENT_PRE + "email_register";
		String forget_login_pwd = INTENT_PRE + "forget_login_pwd";
	}

	public interface ParamType {
		String TypePassword 		= "pwdType";
		String TypeAcount 			= "accountType";
		String TypeModifyPwd		= "isModifyPwd";//是否修改密码
		String TypeTitle			= "title";
		String TypeUrl				= "url";
		String TypeHideTopBar		= "hideTopBar";//是否隐藏标题栏
		String TypePostData			= "postData";
		String TypeRequestContent	= "requestContent";
		String TypeMobilePhone		= "mobilePhone";
		String TypeVerifyCode		= "verifyCode";
		String TypeUserID			= "userid";
		String TypeUserName			= "userName";
		String TypeLoginToken		= "logintoken";
		String TypeUserPassword		= "userpassword";
		String TypeCanGoBack		= "canGoBack";
		String TypeBillData			= "billData";
		String TypePayResult		= "bSuccess";
		String TypePayMoney			= "paymoney";
		String TypePayMerchandise	= "paymerchandise";
		String TypePayOrderNumber	= "payordernumber";
		String TypePayMerchandiseID	= "paymerchandiseid";
		String TypePayPhoneNumber	= "payphonenumber";
		String TypePayOperators		= "payphoneoperators";
		String TypePaySmsOperators	= "paysmsoperators";
		String TypePaySmsReceiver	= "paysmsreceiver";
		String TypePaySendMessage	= "paysendmessage";
		String TypeBillType			= "billType";
		String TypeOneClickParam	= "oneclickparams";
		String TypeNeedQuitOrNot	= "needQuitOrNot";//是否需要退出
		String TypeSafelyQuit		= "safelyquit";//是否是安全退出
		String TypeHideWaitCursor	= "hideWaitCursor";//是否隐藏载入光标
		String TypePayName			= "TypePayName";//支付方式
		String TypeStepLevel		= "StepLevel";//第几步
		String TypeJumpUnicomShop	= "JumpUnicomShop";//第几步
		String TypePayViewType		= "TypePayViewType";//viewType
		String TypePayChannelItem	= "PayChannelItem";
		String TypePhoneCardType	= "phonecardtype";
		String TypePayResultMsg		= "payResultMsg";//支付结果详细信息
		String TypePayType			= "payType";//支付方式
		String RequestContent		= "requestcontent";
	}
	
	public interface AccountType {
		int TypePhone = 0;
		int TypeEmail = 1;
	}

	public interface PasswordType {
		int TypeLoginPwd = 0;
		int TypePayPwd = 1;
	}
	
	public interface RequestCode {
		int Login 			= 9100;
		int PhoneRegisterStep2 = 9101;
		int GoPay 			= 9102;
		int Register 		= 9103;
		int EmailRegister 	= 9104;
		int PhoneRegister	= 9105;
		int SetNewPwd		= 9106;
		int CodeCheck		= 9107;
		int AutoLogin		= 9108;
		int Loading			= 9109;
		int BillType		= 9110;
		int CreateOrder		= 9111;
		int PayResult		= 9112;
		int RunCDPay		= 9113;
		int ChinaMobileSms	= 9114;
		int SendSms			= 9115;
	}
	
	public interface ResultCode {
		int RESULT_CLOSE = Activity.RESULT_FIRST_USER + 100;
	}
	
	public interface ConfigKeys {
		String UserName = "userName";
		String Password = "password";
		String LastPaytype = "lastpayid";
		String LastPhoneNumber = "lastphonenumber";
		String ClientVer = "clientver";
		String ConfigVer = "configver";
		String UpgradeUrl = "upgradeurl";
	}
	
	public interface OrderStatus {
		int UnPay		= 0;
		int Handling	= 1;
		int Shipping	= 2;
		int Shipped		= 3;
	}
	
//	public interface PayTypeName {
//		String rechargecard 	= "手机充值卡";
//		String alipay 			= "支付宝";
//		String oneclick 		= "财付通";
//		String sms 				= "话费充值";
//		String savingscard 		= "储蓄卡";
//		String visacard 		= "信用卡";
//		String unicom_quick 	= "联通快捷支付";
//		String unicom_woshop    = "联通沃阅读";
//		String mobileMM			= "移动话费充值";
//		String web 				= "畅读网站";
//		String unknow 			= "";
//	}
	
	public interface PayCode {
		int rechargecard = 2;//"手机充值卡";
		int alipay = 3;//"支付宝";
		int oneclick = 4;//"财付通";
		int sms = 5;//"话费充值";
		int savingscard = 6;//"储蓄卡";
		int visacard 	=7;// "信用卡";
//		String unicom_quick 	= "联通快捷支付";
		int unicom_woshop  = 10;  //"联通沃阅读";
		int mobileMM =  11;//"移动话费充值";
		int uppay =13;//银联
		int weixin =14;//微信
//		int web 				= "畅读网站";
}
	
//	public interface CardTypeName {
//		String Shengzhouxing 	= "移动";
//		String unicomcard 		= "联通";
//		String telcomcard 		= "电信";
//	}
	
	public interface CardType {
		String mobilecard  		= "mobile";
		String unicomcard 		= "unicomcard";
		String telcomcard 		= "telcomcard";
	}
	
	public interface SimTypeName {
		String simmobile 	= "移动话费充值";
		String simunicom 	= "联通话费充值";
		String simtelcom 	= "电信话费充值";
		String simunknow 	= "未知";
	}
		
//	public interface SimOperator{
//		int Mobile1 = 46000;
//		int Mobile2 = 46002;
//		int Unicom = 46001;
//		int Telcom = 46003;
//		int Unknow = -1;
//	}
	
	public interface SimType {
		String simmobile 	= "simmobile";
		String simunicom 	= "simunicom";
		String simtelcom 	= "simtelcom";
		String simunknow 	= "simunknow";
	}
	
	public interface AlipayTypeName {
		String wap 	= "支付宝WAP支付";
		String sdk 	= "支付宝快捷支付";
	}
	
	public interface OneClickTypeName {
		String wap 	= "财付通WAP";
		String sdk 	= "财付通安全支付";
	}
	
	public interface BillType {
		int OUTLAY	= 0;	//支出
		int INCOME	= 1;	//收入
	}
	
	public interface BankType {
		String DEBIT	= "DEBIT";	//储蓄卡
		String CREDIT	= "CREDIT";	//信用卡
	}
	
//	public interface BankName {
//		String DEBIT	= "储蓄卡";	//储蓄卡
//		String CREDIT	= "信用卡";	//信用卡
//	}

	public interface StepLevel {
		int Step1 = 0;
		int Step2 = 1;
		int Step3 = 2;
	}
	
	//界面展现类型
	public interface UIType {
		int Bean = 1;	//
		int RechargeCard = 2;	//充值卡
		int SDKpay = 3;	//SDK支付
		int Web = 4;	//Web
		int Sms = 5;	//短信
		int SmsWithverycode = 6;	//短信-带验证码
		int UPPaySDK = 11;//银联SDK 
		int common = 0;	//综合支付
	}
	
	//商户类型
	public interface ChannelType {
//		ChannelPayType bean = new ChannelPayType(100, 1);
//		ChannelPayType mobilecard = new ChannelPayType(1, 1027);
//		ChannelPayType unicomcard = new ChannelPayType(2, 1029);
//		ChannelPayType telecomcard = new ChannelPayType(3, 1030);
//		ChannelPayType credit = new ChannelPayType(103, 1131);
//		ChannelPayType visa = new ChannelPayType(102, 1130);
//		ChannelPayType mobilephone = new ChannelPayType(10013, 10013); //话费充值：移动话费充值
//		ChannelPayType telecomphone = new ChannelPayType(1002, 1149);
//		ChannelPayType unicomphone = new ChannelPayType(1003, 1148);
//		ChannelPayType alipaywap = new ChannelPayType(10001, 10001);
//		ChannelPayType alipaysdk = new ChannelPayType(10005, 10005);
//		ChannelPayType oneclickwap = new ChannelPayType(10002, 10002);  //财付通WAP
//		ChannelPayType oneclicksdk = new ChannelPayType(107, 1142);
	}
	public interface SMSPayViewType {
		int toRequestSendSMS = 5;//服务端接口请求，发短信
		int toGetSmsVerifyCodeActivity = 6;//跳转 iCDPayChinaMobileSmsVerifyCodeActivity，  获取验证码
	}
}
