package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2018/1/11.
 */

public class FileUploadResultEntity {

    /**
     * flag : true
     * errCode :
     * resData : http://192.168.90.79:8080/image/1513307010696_2014-07-21181757.jpg
     */

    private boolean flag;
    private String errCode;
    private String resData;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }
}
