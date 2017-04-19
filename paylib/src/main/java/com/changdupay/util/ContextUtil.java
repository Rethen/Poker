package com.changdupay.util;
import android.app.Application;
import android.content.Context;

public class ContextUtil extends Application {
    private static ContextUtil instance;
    private static Context mContext;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
    
    public static Context getContext() {
    	return mContext;
    }
    
    public static void setContext(Context context) {
		mContext = context;
	}
}