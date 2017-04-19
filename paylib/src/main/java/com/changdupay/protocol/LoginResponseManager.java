package com.changdupay.protocol;

import android.os.Message;

import com.changdupay.net.netengine.BufferData;
import com.changdupay.net.netengine.Constant;
import com.changdupay.net.netengine.NetReader;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.base.IBaseResponseInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.base.PostStruct;
import com.changdupay.protocol.base.ResponseHandler;

public class LoginResponseManager {
	private static LoginResponseManager mLoginResponseManager = null;
	private BaseProtocalData mBaseProtocalData = null;
	private IBaseResponseInfo mResponseInfo = null;
	private ResponseHandler mResponseHandler = new ResponseHandler();
	
	public static LoginResponseManager getInstance()
	{
		if (mLoginResponseManager == null)
		{
			mLoginResponseManager = new LoginResponseManager();
		}
		
		return mLoginResponseManager;
	}
	
	public LoginResponseManager()
	{
		
	}
	
	public void processResponse(PostStruct postData)
	{	
		if (postData == null){
			return;
		}
		
		//网络请求失败
		int actionid = postData.mActionID;
		if (postData.errorCode != Constant.CORRECT)
		{
			sendMessage(actionid, postData.errorCode);
			return;
		}

		if (postData.mData == null) {
			sendMessage(actionid, Constant.ERROR);
			return;
		}
		
		byte[] data = ((BufferData)postData.mData).getByteBuffer();
		if (data == null) {
			if (PayConst.DYNAMICKEY_ACTION == actionid)
			{
				sendMessage(actionid, null);
			}
			else
				sendMessage(actionid, Constant.ERROR);
			return;
		}
		
		//“获取动态秘钥”单独处理
		if (PayConst.DYNAMICKEY_ACTION == actionid) {
			String strDataString = new String(data);
			sendMessage(actionid, strDataString);
			return;
		}
		if (PayConst.ONECLICK_BANKLIST_ACTION == actionid)
		{
			String strDataString = new String(data);
			sendMessage(actionid, strDataString);
			return;
		}
		if (PayConst.PREFERENTIAL_ICON_ACTION == actionid)
		{
			sendMessage(actionid, postData);
			return;
		}
		if (PayConst.GET_CHANGDUREADER_USERINFO_ACTION == actionid) {
			NetReader reader = new NetReader(data);
			BaseProtocalData resultData = ProtocolParser.parseProtocal_ChangduReader(reader);
			if (resultData != null) {
				resultData.result = reader.getResult();
				resultData.errorMsg = reader.getErrorMsg();
				resultData.ActionID = String.valueOf(reader.getActionId());
				resultData.ApplicationID = String.valueOf(reader.getAppId());
				resultData.NextUpdateTimeSpan = System.currentTimeMillis();
			}
			mBaseProtocalData = resultData;
		
			sendMessage(actionid, mBaseProtocalData);
			return;
		}
		
		NetReader reader = new NetReader(data);
		//reader.openDebugMode();
		BaseProtocalData resultData = ProtocolParser.parseProtocal(reader);
		if (resultData != null) {
			resultData.result = reader.getResult();
			resultData.errorMsg = reader.getErrorMsg();
			resultData.ActionID = String.valueOf(reader.getActionId());
			resultData.ApplicationID = String.valueOf(reader.getAppId());
			resultData.NextUpdateTimeSpan = System.currentTimeMillis();
		}
		mBaseProtocalData = resultData;
	
		sendMessage(actionid, mBaseProtocalData);
	}
	
	public IBaseResponseInfo getResponseInfo()
	{
		return mResponseInfo;
	}
	
	public ResponseHandler getResponseHandler()
	{
		return mResponseHandler;
	}

	
	private void sendMessage(int what, Object responseInfo) 
	{
    	if(mResponseHandler != null){
    		Message msg = mResponseHandler.obtainMessage(what, 0, 0, responseInfo);

    		mResponseHandler.sendMessageDelayed(msg, 10);
    	} 
    } 
}
