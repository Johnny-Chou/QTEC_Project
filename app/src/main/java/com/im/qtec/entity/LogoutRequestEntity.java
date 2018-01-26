package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class LogoutRequestEntity {

    public int id;

    public String deviceId;

    public LogoutRequestEntity(int id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }
}
