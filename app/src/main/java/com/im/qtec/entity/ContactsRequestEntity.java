package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/12/14.
 */

public class ContactsRequestEntity {


    /**
     * id : 1
     * versionDate : 2017-11-16 15:53:41
     * deviceId  : 123429649327497329234hsfshdflsfsd
     */

    private int id;
    private String versionDate;
    private String deviceId;

    public ContactsRequestEntity(int id, String versionDate, String deviceId) {
        this.id = id;
        this.versionDate = versionDate;
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
