package com.poseidon.pokers.domain.entity;

/**
 * Created by 42524 on 2017/4/13.
 */

public class BaseResp {

    public Message message;

    public static class Message{
        public int errorCode;
    }
}
