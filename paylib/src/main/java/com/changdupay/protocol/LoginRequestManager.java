package com.changdupay.protocol;

import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;

import com.changdupay.encrypt.MD5Utils;
import com.changdupay.encrypt.RSAUtils;
import com.changdupay.net.netengine.BufferData;
import com.changdupay.net.netengine.CNetHttpTransfer;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.base.PostStruct;
import com.changdupay.protocol.base.RequestHandlerManager;
import com.changdupay.protocol.base.SessionManager;
import com.changdupay.protocol.login.CheckEmailValidRequestInfo;
import com.changdupay.protocol.login.CheckVerifyCodeRequestInfo;
import com.changdupay.protocol.login.EmailRegisterRequestInfo;
import com.changdupay.protocol.login.GetAccountInfoRequestInfo;
import com.changdupay.protocol.login.GetCerifyCodeRequestInfo;
import com.changdupay.protocol.login.MobilePhoneRegisterRequestInfo;
import com.changdupay.protocol.login.SmallPayRequestInfo;
import com.changdupay.util.Const;
import com.changdupay.util.PayConfigReader;

public class LoginRequestManager {
	private static LoginRequestManager mLoginRequestManager = null;
	
	public static LoginRequestManager getInstance()
	{
		if (mLoginRequestManager == null)
		{
			mLoginRequestManager = new LoginRequestManager();
		}
		
		return mLoginRequestManager;
	}
	
	public LoginRequestManager()
	{
		
	}
	
	private String mRandomKey = "";
	private String getRandomKey() {
		String strResult = mRandomKey;
		
		if (TextUtils.isEmpty(strResult) || TextUtils.isEmpty(PayConfigReader.getInstance().getPayConfigs().LocalKey)) {
			Random random = new Random();
			String strKeyEnd = String.valueOf(random.nextLong());
			strKeyEnd = strKeyEnd.length() > 4 ? strKeyEnd.substring(0, 4) : "xxxx";
			String publicKey = "";
			strResult = PayConst.APPKEY.substring(0, 20) + strKeyEnd;
			String strHexKey = RSAUtils.bytesToHexString(strResult.getBytes());
			PayConfigReader.getInstance().getPayConfigs().LocalKey = strHexKey;
			try {  
	            Map<String, Object> keyMap = RSAUtils.genKeyPair();  
	            publicKey = RSAUtils.getPublicKey(keyMap);  
	            System.err.println("公钥: \n\r" + publicKey);
	            
				strResult = RSAUtils.bytesToHexString(RSAUtils.encryptByModule(strHexKey.getBytes(), Const.RSA_PUBLIC_KEY));
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 			
		}
		mRandomKey = strResult;
        
        return strResult;
	}
	
	//获取动态密钥及配置信息
	public int requestGetDynamicKey(Context ctx, long merchantID) {
		String strKey = getRandomKey();
		MD5Utils md5 = new MD5Utils();
		String sign = md5.MD5_Encode(PayConfigReader.getInstance().getConfigBase64Content() + PayConfigReader.getInstance().getPayConfigs().LocalKey);
		String url = String.format(PayConst.URL_DYNAMIC_KEY, merchantID, PayConst.APPID, PayConst.PARSER_VER, PayConst.VER, PayConst.SessionID, strKey, sign);
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(PayConst.DYNAMICKEY_ACTION, bufferData);		
		int sessionid = CNetHttpTransfer.getInstance().netGetData(url, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		SessionManager.getInstance().addSession(sessionid, ps);		
		return sessionid;
	}
	
//	//登录接口
//	public int requestLogin(LoginRequestInfo loginRequestInfo, Context ctx)
//	{
//		byte[] uploadBytesBuffer = loginRequestInfo.GetPostData();		
//		
//		BufferData bufferData = new BufferData();
//		PostStruct ps = new PostStruct(PayConst.LOGIN_ACTION, bufferData);
//		
//		int sessionid = CNetHttpTransfer.getInstance().netPostGetData(PayConst.URL_COMMON_REQUEST, uploadBytesBuffer, bufferData, null, RequestHandlerManager.getInstance().getMessageHandler(), ctx);
//		
//		SessionManager.getInstance().addSession(sessionid, ps);
//		
//		return sessionid;
//	}
//
//	//令牌登录接口
//	public int requestTokenLogin(TokenLoginRequestInfo tokenLoginRequestInfo, Context ctx)
//	{
//		return requestByPostData(tokenLoginRequestInfo, ctx);
//	}
	
	//通用请求接口
	public int requestByPostData(BaseRequestInfo info, Context ctx) {
		byte[] uploadBytesBuffer = info.GetPostData();		
		
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(info.getActionID(), bufferData);
		
		int sessionid = CNetHttpTransfer.getInstance().netPostGetData(
				PayConst.URL_COMMON_REQUEST, uploadBytesBuffer, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		
		SessionManager.getInstance().addSession(sessionid, ps);
		
		return sessionid;
	}
	
	//110010-加载帐户信息
	public int requestAccountInfo(GetAccountInfoRequestInfo accountInfoRequestInfo, Context ctx)
	{
		return requestByPostData(accountInfoRequestInfo, ctx);
	}
	
	//手机帐号注册
	public int requestPhoneRegister(MobilePhoneRegisterRequestInfo mobilePhoneRegisterRequestInfo, Context ctx)
	{
		return requestByPostData(mobilePhoneRegisterRequestInfo, ctx);
	}
	
	//获取手机验证码
	public int requestGetPhoneVerifyCode(GetCerifyCodeRequestInfo getCerifyCodeRequestInfo, Context ctx)
	{
		return requestByPostData(getCerifyCodeRequestInfo, ctx);
	}
		
	//邮箱帐号注册
	public int requestEmailRegister(EmailRegisterRequestInfo emailRegisterRequestInfo, Context ctx)
	{
		return requestByPostData(emailRegisterRequestInfo, ctx);
	}
	
	//检测邮箱是否可以有效
	public int requestCheckEmailValid(CheckEmailValidRequestInfo info, Context ctx){
		return requestByPostData(info, ctx);
	}
	
	//小额支付免密设置
	public int requestSmallPay(SmallPayRequestInfo smallPayRequestInfo, Context ctx)
	{
		return requestByPostData(smallPayRequestInfo, ctx);
	}

	
	//校验手机验证码
	public int requestCheckVerifyCode(CheckVerifyCodeRequestInfo info, Context ctx)
	{
		return requestByPostData(info, ctx);
	}

	//获取动态密钥及配置信息
	public int requestChangduReaderUserInfo(Context ctx, String strUrl) {
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(PayConst.GET_CHANGDUREADER_USERINFO_ACTION, bufferData);		
		int sessionid = CNetHttpTransfer.getInstance().netGetData(strUrl, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		SessionManager.getInstance().addSession(sessionid, ps);		
		return sessionid;
	}
}
