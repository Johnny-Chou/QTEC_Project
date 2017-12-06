package com.im.qtec.entity;

import com.google.gson.JsonElement;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class CheckEntity{

    public String id;

    public String deviceId;

    public CheckEntity(String id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }
}
