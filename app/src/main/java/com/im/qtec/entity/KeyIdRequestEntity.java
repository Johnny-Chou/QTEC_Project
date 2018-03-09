package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2018/2/28.
 */

public class KeyIdRequestEntity {

    /**
     * id : 222
     * deviceId : 376D37B2-1834-46CE-9FC0-A1BD24000804
     * keyId : 45717d76-8f7e-4d49-b6c0-21184281da2a
     */

    private int id;
    private String deviceId;
    private String keyId;

    public KeyIdRequestEntity(int id, String deviceId, String keyId) {
        this.id = id;
        this.deviceId = deviceId;
        this.keyId = keyId;
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

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
