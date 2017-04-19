package com.changdupay.protocol.pay;

import java.io.Serializable;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.changdupay.app.UserInfo;
import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.PayConst;

/**公共Content
 * 
 * @author Administrator
 *
 */
public class RequestContent extends BaseContent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//登录令牌
	public String SessionID = "";
	
	//支付方式
	public int PayType = -1;
	
	//支付渠道
	public int PayId = 0;
	
	//用户ID
	public long UserID = 0;
	
	//登录token
	public String LoginToken = "";
	
	//用户名
	public String UserName = "";
	
	//用户昵称
	public String NickName = "";
	
	//VIP URL
	public Bitmap UserVipLevelBitmap = null;
	
	//头像URL
	public Bitmap UserHeadImgBitmap = null;
	
	//商户ID
	public long MerchantID = PayConst.MERCHANTID;
	
	//商品ID
	public long MerchandiseID = PayConst.MERCHANDISEID;
	
	//商品名称
	public String MerchandiseName = PayConst.MERCHANDISENAME;
	
	//商户订单号
	public String CooperatorOrderSerial = "";
	
	//充值卡卡号
	public String CardNumber = "";
	
	//充值卡密码
	public String CardPassword = "";
	
	//手机号码
	public String PhoneNumber = "";
	
	//银行代码
	public long BankCode = 0;
	
	//小额免密
	public int CheckPayPassword = 0;
	
	//支付密码
	public String PayPassword = "";
		
	//订单金额(非必填)
	public String OrderMoney = "";
	
	//平台类型
	public short OsType = PayConst.PLAT_ANDROID;
		
	//同步通知地址
	public String ReturnUrl = "";
	
	//异步通知地址
	public String NotifyUrl = "";
	
	//扩展信息,支付成功后原样返回给商户
	public String ExtInfo = "";
	
	//客户端 IP地址
	public long IPAddress = 19216801;
	
	//备注
	public String Remark = "";	
	
	//畅读币
	public long ChangduCoins = 0;
	
	//礼币
	public long GiftCoins = 0;
	
	//请求用户信息的url
	public String RequestUserInfoUrl;
	
	//换肤颜色矩阵
	public float[] ColorMatrix;
	
	//应用ID
	public long AppID = PayConst.APPID;
	
	//版本号
	public String Ver = PayConst.VER;
	
	public String ShopItemId = "";
	
	public RequestContent()
	{
		super();

		UserID = TextUtils.isEmpty(UserInfo.getInstance().mUserID) ? 0 : Long.parseLong(UserInfo.getInstance().mUserID);
		UserName = UserInfo.getInstance().mUserName;
	}
	
	public String toString()
	{
		try{
			StringBuilder str = new StringBuilder();
			str.append("SessionID:");
			str.append(SessionID);
			str.append(",");
			
			str.append("PayType:");
			str.append(PayType);
			str.append(",");
			
			str.append("PayId:");
			str.append(PayId);
			str.append(",");
			
			str.append("UserID:");
			str.append(UserID);
			str.append(",");
			
			str.append("UserName:");
			str.append(UserName);
			str.append(",");
			
			str.append("MerchandiseID:");
			str.append(MerchandiseID);
			str.append(",");
			
			str.append("MerchandiseName:");
			str.append(MerchandiseName);
			str.append(",");
			
			str.append("CooperatorOrderSerial:");
			str.append(CooperatorOrderSerial);
			str.append(",");
			
			str.append("CardNumber:");
			str.append(CardNumber);
			str.append(",");
			
			str.append("CardPassword:");
			str.append(CardPassword);	
			str.append(",");
			
			str.append("PhoneNumber:");
			str.append(PhoneNumber);
			str.append(",");
			
			str.append("BankCode:");
			str.append(BankCode);	
			str.append(",");
			
			str.append("CheckPayPassword:");
			str.append(CheckPayPassword);
			str.append(",");
			
			str.append("PayPassword:");
			str.append(PayPassword);	
			str.append(",");
			
			str.append("OrderMoney:");
			str.append(OrderMoney);
			str.append(",");

			str.append("ReturnUrl:");
			str.append(ReturnUrl);
			str.append(",");
			
			str.append("NotifyUrl:");
			str.append(NotifyUrl);
			str.append(",");
			
			str.append("ExtInfo:");
			str.append(ExtInfo);
			str.append(",");
			
			str.append("IPAddress:");
			str.append(IPAddress);
			str.append(",");

			str.append("Remark:");
			str.append(Remark);

			String content = str.toString();//.replaceAll(" ", "");
			JSONObject jsonObj = JsonUtils.string2JSON(content, ",");

			str = null;
			return jsonObj.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
