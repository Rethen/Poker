package com.poseidon.pokers.data.repository;

import com.google.gson.Gson;
import com.poseidon.pokers.data.net.HttpApiManager;
import com.poseidon.pokers.domain.entity.LoginResp;
import com.poseidon.pokers.domain.interactor.Login;
import com.poseidon.pokers.domain.repository.LoginRespositroy;
import com.poseidon.pokers.domain.repository.RemoteRespositroy;

import java.net.InetSocketAddress;
import java.net.Socket;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by 42524 on 2017/4/12.
 */
@Singleton
public class LoginDataRespositroy extends BaseDataRespositroy implements LoginRespositroy {

    @Inject
    LoginDataRespositroy(Gson gson, HttpApiManager httpApiManager) {
        super(gson, httpApiManager);
    }

    @Override
    public Observable<LoginResp> login(Login.AccountLoginReq req) {
         httpApiManager.getRemoteRespositroy(RemoteRespositroy.class)
                .phoneLogin(createRequestBoday(req))
                .map(new Function<LoginResp, Socket>() {
                    @Override
                    public Socket apply(LoginResp loginResp) throws Exception {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress("125.77.25.68",8500),15000);
                        socket.setSoTimeout(10000); // set read time out
                        return socket;
                    }
                });
        return  null;
    }

}
