package com.poseidon.pokers.domain.repository;

import com.poseidon.pokers.domain.entity.LoginResp;
import com.poseidon.pokers.domain.interactor.Login;

import io.reactivex.Observable;

/**
 * Created by 42524 on 2017/4/12.
 */

public interface LoginRespositroy {

    Observable<LoginResp> login(Login.AccountLoginReq req);

}
