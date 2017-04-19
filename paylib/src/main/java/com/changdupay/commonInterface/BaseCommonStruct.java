package com.changdupay.commonInterface;

import android.util.Log;


public class BaseCommonStruct {
    public int iPara;
    public Object obj1;		//通常情况下作为入参使用
    public Object obj2;		//通常情况下作为出参，根据实际情况使用
    

    private void printParaError(String method){
    	StringBuilder builder=new StringBuilder();
    	builder.append(",iPara="+iPara);
    	builder.append(",obj1="+obj1);
    	builder.append(",obj2="+obj2);
    	Log.e("commoninterface","param error,method:"+method+",param:"+builder.toString());
    }
    
    public void showError(int result,String method){
    	switch (result) {
		case CommonConst.COM_RET_PARA_ERROR:
			printParaError("commoninterface."+method);
			break;
		case CommonConst.COM_RET_NO_NETWORK:
			Log.e("commoninterface","no network:"+method);
			break;
		case CommonConst.COM_RET_NO_ID_DIFINE:
			Log.e("commoninterface","id not defined,method:"+method);
			break;
		case CommonConst.COM_RET_NO_MODEL_REGISTER:
			Log.e("commoninterface", "model not registed,method:"+method);
			break;
		case CommonConst.COM_RET_OK:
			break;
		default:
			break;
		}
	}
}
