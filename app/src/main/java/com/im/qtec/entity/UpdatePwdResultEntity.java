package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/12/14.
 */

public class UpdatePwdResultEntity {


    /**
     * flag : true
     * errCode :
     * resData :
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
