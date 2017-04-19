package com.poseidon.pokers.domain.entity;

/**
 * Created by 42524 on 2017/4/13.
 */

public class LoginResp extends BaseResp {

    public UserInfo userInfo;

    public static class UserInfo {
        public String bduss;
    }
}
