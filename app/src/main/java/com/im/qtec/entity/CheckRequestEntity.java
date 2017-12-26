package com.im.qtec.entity;

import com.google.gson.JsonElement;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class CheckRequestEntity {

    public int id;

    public String deviceId;

    public CheckRequestEntity(int id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }
}
