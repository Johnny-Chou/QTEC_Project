package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/12/20.
 */

public class KeyRequestEntity {

    /**
     * id : 222
     * deviceId : 376D37B2-1834-46CE-9FC0-A1BD24000804
     * length : 1000
     */

    private int id;
    private String deviceId;

    public KeyRequestEntity(int id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
