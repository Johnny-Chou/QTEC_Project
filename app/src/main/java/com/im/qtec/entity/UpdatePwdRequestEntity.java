package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class UpdatePwdRequestEntity {

    public int id;

    public String deviceId;
    public String oldPassword;
    public String newPassword;



    public UpdatePwdRequestEntity(int id, String deviceId,String oldPassword,String newPassword) {
        this.id = id;
        this.deviceId = deviceId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
