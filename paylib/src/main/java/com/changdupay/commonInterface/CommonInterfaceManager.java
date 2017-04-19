package com.changdupay.commonInterface;

import java.util.HashMap;

import android.util.Log;

public enum CommonInterfaceManager {
	INSTANCE;
//	private static CommonInterfaceManager mInstance;

	private HashMap<Integer,ICommonInterObserver> mCIObservers=new HashMap<Integer,ICommonInterObserver>();
	
	public void registStateObserver(int moderKey, ICommonInterObserver observer){
		if (!mCIObservers.containsKey(moderKey)){
			//多线程下小概率
			try{
				mCIObservers.put(moderKey, observer);
			}
			catch(Exception e){
				e.printStackTrace();
				Log.w("CommonInterfaceManager", "registStateObserver put error:" + moderKey);
			}
		}
		else{
			Log.w("CommonInterfaceManager", "registStateObserver contain key:" + moderKey);
		}
	}
	
	public void unrigestStateObserver(int moderKey){
		mCIObservers.remove(moderKey);
	}
	
	public int CommonInterfaceID(int moderKey, int id, BaseCommonStruct msg){
		ICommonInterObserver modelObs = mCIObservers.get(moderKey);
		if (null != modelObs){
			return modelObs.onCommonInterModelProc(id, msg);	
		}
		return CommonConst.COM_RET_NO_MODEL_REGISTER;   
	}
}
