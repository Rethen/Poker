package com.poseidon.pokers.utils;

import android.content.Context;

import com.elvishew.xlog.XLog;
import com.poseidon.pokers.BuildConfig;

import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

/**
 * 环境变量
 * Created by douzifly on 2016/7/19.
 */
public class Env {

    private static final String TAG = "PokerEnv";

    private  Context context;

    private HashMap<String, EnvConfig> sEnvs = new HashMap<>();

    private String sCurEnvName = "build";

    private  String sNameServerAdderss;

    private  boolean configLoaded = false;

    private  String UPLOAD_BASE_ADDRESS;
    private  String CARD_REPLAY_ADDRESS;
    private  String HTTP_API_SERVER_BASE;
    private  String SERVER_ADDRESS;
    private  String SERVER_ADDRESS_PAY;
	private  String UPLOAD_HEAD_ADDRESS;
    private  String UTS_REPORT_ADDRESS;

    @Inject
    Env(){

    }

    public  String getUploadBaseAddress() {
        loadConfigIfNeeded();
        return UPLOAD_BASE_ADDRESS;
    }

    public  String getCardReplayAddress() {
        loadConfigIfNeeded();
        return CARD_REPLAY_ADDRESS;
    }

    public  String getHttpApiServerBase() {
        loadConfigIfNeeded();
        return HTTP_API_SERVER_BASE;
    }

    public  String getServerAddress() {
        loadConfigIfNeeded();
        return SERVER_ADDRESS;
    }

    public  String getServerAddressPay() {
        loadConfigIfNeeded();
        return SERVER_ADDRESS_PAY;
    }

    public  String getUploadHeadAddress() {
        loadConfigIfNeeded();
        return UPLOAD_HEAD_ADDRESS;
    }

    public  String getUtsReportAddress() {
        loadConfigIfNeeded();
        return UTS_REPORT_ADDRESS;
    }


    public  String getNameServerAddress() {
        if (sNameServerAdderss == null) {
            loadConfigIfNeeded();
            sNameServerAdderss = HTTP_API_SERVER_BASE + "api/customer/checknickname";
        }
        return sNameServerAdderss;
    }

    private  void  initConfig(){
        sEnvs.put("build", new EnvConfig() {
            @Override
            void load() {
                UPLOAD_BASE_ADDRESS = BuildConfig.UPLOAD_BASE_ADDRESS;
                CARD_REPLAY_ADDRESS = BuildConfig.CARD_REPLAY_ADDRESS;
                HTTP_API_SERVER_BASE = BuildConfig.HTTP_API_SERVER_BASE;
                SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS;
                SERVER_ADDRESS_PAY = BuildConfig.SERVER_ADDRESS_PAY;
                UPLOAD_HEAD_ADDRESS = BuildConfig.UPLOAD_HEAD_ADDRESS;
                UTS_REPORT_ADDRESS = BuildConfig.UTS_REPORT_ADDRESS;
            }
        });

        sEnvs.put("offline", new EnvConfig() {
            @Override
            void load() {
                UPLOAD_BASE_ADDRESS = BuildConfig.UPLOAD_BASE_ADDRESS_OFFLINE;
                CARD_REPLAY_ADDRESS = BuildConfig.CARD_REPLAY_ADDRESS_OFFLINE;
                HTTP_API_SERVER_BASE = BuildConfig.HTTP_API_SERVER_BASE_OFFLINE;
                SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS_OFFLINE;
                SERVER_ADDRESS_PAY = BuildConfig.SERVER_ADDRESS_PAY_OFFLINE;
                UPLOAD_HEAD_ADDRESS = BuildConfig.UPLOAD_HEAD_OFFLINE;
                UTS_REPORT_ADDRESS = BuildConfig.UTS_REPORT_ADDRESS_OFFLINE;
            }
        });

        sEnvs.put("onlineHome", new EnvConfig() {
            @Override
            void load() {
                UPLOAD_BASE_ADDRESS = BuildConfig.UPLOAD_BASE_ADDRESS_ONLINE_HOME;
                CARD_REPLAY_ADDRESS = BuildConfig.CARD_REPLAY_ADDRESS_ONLINE_HOME;
                HTTP_API_SERVER_BASE = BuildConfig.HTTP_API_SERVER_BASE_ONLINE_HOME;
                SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS_ONLINE_HOME;
                SERVER_ADDRESS_PAY = BuildConfig.SERVER_ADDRESS_PAY_ONLINE_HOME;
                UPLOAD_HEAD_ADDRESS = BuildConfig.UPLOAD_HEAD_ONLINE_HOME;
                UTS_REPORT_ADDRESS = BuildConfig.UTS_REPORT_ADDRESS_ONLINE_HOME;
            }
        });

        sEnvs.put("onlineAbroad", new EnvConfig() {
            @Override
            void load() {
                UPLOAD_BASE_ADDRESS = BuildConfig.UPLOAD_BASE_ADDRESS_ONLINE_ABOARD;
                CARD_REPLAY_ADDRESS = BuildConfig.CARD_REPLAY_ADDRESS_ONLINE_ABOARD;
                HTTP_API_SERVER_BASE = BuildConfig.HTTP_API_SERVER_BASE_ONLINE_ABOARD;
                SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS_ONLINE_ABOARD;
                SERVER_ADDRESS_PAY = BuildConfig.SERVER_ADDRESS_PAY_ONLINE_ABOARD;
                UPLOAD_HEAD_ADDRESS = BuildConfig.UPLOAD_HEAD_ONLINE_ABOARD;
                UTS_REPORT_ADDRESS = BuildConfig.UTS_REPORT_ADDRESS_ONLINE_ABOARD;
            }
        });

        sEnvs.put("onlineTest", new EnvConfig() {
            @Override
            void load() {
                UPLOAD_BASE_ADDRESS = BuildConfig.UPLOAD_BASE_ADDRESS_ONLINE_TEST;
                CARD_REPLAY_ADDRESS = BuildConfig.CARD_REPLAY_ADDRESS_ONLINE_TEST;
                HTTP_API_SERVER_BASE = BuildConfig.HTTP_API_SERVER_BASE_ONLINE_TEST;
                SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS_ONLINE_TEST;
                SERVER_ADDRESS_PAY = BuildConfig.SERVER_ADDRESS_PAY_ONLINE_TEST;
                UPLOAD_HEAD_ADDRESS = BuildConfig.UPLOAD_HEAD_ONLINE_TEST;
                UTS_REPORT_ADDRESS = BuildConfig.UTS_REPORT_ADDRESS_ONLINE_TEST;
            }
        });

    }

    public  boolean isAbroadVersion() {
        loadConfigIfNeeded();
        if (sCurEnvName.equals("build")) {
            return BuildConfig.IS_ABOARD_VERSION;
        } else {
            return sCurEnvName.equals("onlineAbroad");
        }
    }

    public  String getCurEnvName() {
        return sCurEnvName;
    }

    public  Set<String> listEnvNames() {
        return sEnvs.keySet();
    }

    public  void switchEnv(Context context, String envName, boolean restartApp) {
        if (sEnvs.containsKey(envName) && !sCurEnvName.equals(envName)) {
            XLog.d(TAG, "switchEnv : " + envName);
            sCurEnvName = envName;
            saveConfig(context);
            if (restartApp) {
//                Utils.restartApplication(context);
            }
        }
    }

    private  void loadConfigIfNeeded() {
        loadConfig(context);
    }


    public  void loadConfig(Context context) {
        if (configLoaded) {
            return;
        }
        configLoaded = true;
        // 如果没有配置，那么使用包默认的
//        String env = SpHelper.getString(context, "env", BuildConfig.DEFAULT_ENV_NAME);
//        Log.d(TAG, "loadConfig env:" + env);
//        sCurEnvName = env;
//        sEnvs.get(env).load();
    }

    private static void saveConfig(Context context) {
//        SpHelper.putString(context, "env", sCurEnvName);
    }

    public abstract static class EnvConfig {

        abstract void load();

    }

}
