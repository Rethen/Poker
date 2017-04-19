package com.poseidon.pokers.domain.interactor;

import com.poseidon.pokers.domain.executor.PostExecutionThread;
import com.poseidon.pokers.domain.executor.ThreadExecutor;
import com.poseidon.pokers.domain.repository.LoginRespositroy;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by 42524 on 2017/4/10.
 */

public class Login extends UseCase<Void,Login.AccountLoginReq> {

    private LoginRespositroy respositroy;

    @Inject
    Login(LoginRespositroy respositroy, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.respositroy = respositroy;
    }

    @Override
    protected Observable buildUseCaseObservable(AccountLoginReq req) {
        return respositroy.login(req);
    }

    public static class AccountLoginReq {
        public String pnum;
        public String passwd;
        public String country;
    }


}
