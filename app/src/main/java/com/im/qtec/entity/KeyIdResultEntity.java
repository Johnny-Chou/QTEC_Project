package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2018/2/28.
 */

public class KeyIdResultEntity {

    /**
     * flag : true
     * errCode :
     * resData : {"id":"45717d76-8f7e-4d49-b6c0-21184281da2a","value":"yi0240tGQQvj6S6EFDr9GlV2s0Z0pjvn+4ssjOJ5wZg="}
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
        /**
         * id : 45717d76-8f7e-4d49-b6c0-21184281da2a
         * value : yi0240tGQQvj6S6EFDr9GlV2s0Z0pjvn+4ssjOJ5wZg=
         */

        private String id;
        private String value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
