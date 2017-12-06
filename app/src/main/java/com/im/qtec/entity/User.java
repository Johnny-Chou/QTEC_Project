package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/11/22.
 */

public class User {
    public String mobilephone;

    public String password;

    public String deviceId;

    public User(String mobilephone, String password, String deviceId) {
        this.mobilephone = mobilephone;
        this.password = password;
        this.deviceId = deviceId;
    }
}
