package com.changdupay.app;

import android.graphics.Bitmap;

import com.changdupay.protocol.base.PayConst;

public class iCDPayOrderInfo {
	//商品ID
	public long MerchantID = 100001L;
	
	//商品ID
	public long MerchandiseID = PayConst.MERCHANDISEID;
	
	//商品名称
	public String MerchandiseName = PayConst.MERCHANDISENAME;
	
	//商户订单号
	public String CooperatorOrderSerial = java.util.UUID.randomUUID().toString();
	
	//用户ID
	public long UserID = 0;
	
	//用户名
	public String UserName = "";
	
	//用户昵称
	public String NickName = "nickname";
	
	//登录token
	public String LoginToken = "";
	
	//畅读币
	public long ChangduCoins = 0;
	
	//礼币
	public long GiftCoins = 0;
	
	public String serverId = "0";
	
	//VIP URL
	public Bitmap UserVipLevelBitmap = null;
	
	//头像URL
	public Bitmap UserHeadImgBitmap = null;

	//请求用户信息的url
	public String RequestUserInfoUrl = "";
	
	public String WxKey = "";
	public int nPayMoney = 0;
	public String shopItemId = "";
	
	//换肤颜色矩阵
	public float[] ColorMatrix;
}
