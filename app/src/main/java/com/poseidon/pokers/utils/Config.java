package com.poseidon.pokers.utils;


public class Config {
	//登录服务器ipoker.bbitoo.cn//117.135.163.42  10.242.98.13:8500外网测试 49.213.14.95:20000
	//测试地址：10.242.98.13
	public static Object[] Login_LoginServer = {Env.getServerAddress(), 8500};
	public static String Nick_Name_Query_Server = Env.getNameServerAddress();
	/*文件*/
	public  static String FileServerAddress= Env.getServerAddress();
	public  static int FileServerPort=8502;
	// 牌局回放服务器
	public static String CardReplayURL = Env.getCardReplayAddress();
	// 头像抓取服务器
	public static String HeadPicDownloadURL = Env.getCardReplayAddress() + "dzfile/img/";

	public static final String TWITTER_APP_KEY = "rCHvj8IQ9CUaKmWrYKG8OJCDd";
	public static final String TWITTER_APP_SECRET = "E5NSWvSOHpWGI9v6aY8kVfpMhEvY5IOhsUG43swgJe08ZimZoh";

	//客户端协议头相关信息
	public final static byte PROTOCOL_VERSION = (byte) 0;
	public final static byte LANGUAGE_ID = (byte) 1;//1Chinese ,0 English,255Others
	public final static byte CLIENT_PLATFORM = (byte)1; //客户端使用语言 1java 2ios 3symbian
	public final static byte CLIENT_BUILD_NUMBER = (byte)34; //客户端版本号
	public final static int CUSTOM_ID_1 =1; //abroad
	public final static int CUSTOM_ID_2 =2; //home
	public final static int PRODUCT_ID =1001; //产品唯一标识
	
	public static final byte HEADER_INDICATER_0 = 'D';
	public static final byte HEADER_INDICATER_1 = 'Z';
	public static final byte HEADER_INDICATER_2 = 'P';
	public static final byte HEADER_INDICATER_3 = 'K';
	/**
	 * Client Request Package ******************************
	 */
	public static final int SRP_REQUEST_HIGH = 4;
	public static final int SRP_REQUEST_LOW = 5;
	public static final int SRP_PACKE_LEVEL = 6;
	public static final int SRP_SIZE_HIGH = 7;
	public static final int SRP_SIZE_LOW = 8;
	/**
	 *  Server Response Package
	 */
    public static final int RP_PROTOCOL_VERSION = 4;    
    public static final int RP_USER_ID_1 = 5;
    public static final int RP_USER_ID_2 = 6;
    public static final int RP_USER_ID_3 = 7;
    public static final int RP_USER_ID_4 = 8;    
    public static final int RP_LANGUAGE_ID = 9;    
    public static final int RP_CLIENT_PLATFORM = 10;
    public static final int RP_CLIENT_BUILD_NUMBER = 11;    
    public static final int RP_CUSTOM_ID_1 = 12;
    public static final int RP_CUSTOM_ID_2 = 13;    
    public static final int RP_PRODUCT_ID_HIGH = 14;
    public static final int RP_PRODUCT_ID_LOW = 15;    
    public static final int RP_REQUEST_CODE_HIGH = 16;
    public static final int RP_REQUEST_CODE_LOW = 17;    
    public static final int RP_SIZE_HIGH = 18;
    public static final int RP_SIZE_LOW = 19;	
}

