package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class CheckResult {


    /**
     * flag : false
     * errCode : 100007
     * resData : {}
     */

    private boolean flag;
    private String errCode;
    private ResDataBean resData;

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

    public ResDataBean getResData() {
        return resData;
    }

    public void setResData(ResDataBean resData) {
        this.resData = resData;
    }

    public static class ResDataBean {
    }
}
