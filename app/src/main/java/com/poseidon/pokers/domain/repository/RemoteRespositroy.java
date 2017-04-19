package com.poseidon.pokers.domain.repository;

import com.poseidon.pokers.domain.entity.LoginResp;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 42524 on 2017/4/12.
 */

public interface RemoteRespositroy {

    @POST("api/regist/phoneLogin")
    Observable<LoginResp> phoneLogin(@Body RequestBody content);
}
