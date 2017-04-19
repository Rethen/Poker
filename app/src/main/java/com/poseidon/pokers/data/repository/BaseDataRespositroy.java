package com.poseidon.pokers.data.repository;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.poseidon.pokers.data.net.HttpApiManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 42524 on 2017/4/12.
 */

public abstract class BaseDataRespositroy {

    protected HttpApiManager httpApiManager;

    protected Gson gson;

    protected BaseDataRespositroy(Gson gson, HttpApiManager httpApiManager) {
        this.httpApiManager = httpApiManager;
        this.gson = gson;
    }

    protected RequestBody createRequestBoday(Object o){
        XLog.d("Send json is:"+gson.toJson(o));
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),gson.toJson(o));
        return requestBody;
    }
}
