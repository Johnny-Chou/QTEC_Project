package com.im.qtec.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouyanglei on 2017/11/28.
 */

public class Info implements Parcelable {


    /**
     * flag : true
     * errCode :
     * resData : {"id":224,"username":"周阳雷","password":"","email":"zhouyl@qtec.cn","sex":1,"status":1,"createTime":1511318476000,"updateTime":1511318476000,"telephone":"","mobilephone":"15528327788","deviceId":"903E91C1-0531-4CB1-9662-47B09B1134D2","deviceToken":"","role":2,"company":"九州量子","apartment":"研发部","level":7,"online":1,"logo":"http://192.168.90.79:8080/image/1511318476764.jpg"}
     */

    private boolean flag;
    private String errCode;
    private ResDataBean resData;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Info() {
    }

    protected Info(Parcel in) {
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

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
         * id : 224
         * username : 周阳雷
         * password :
         * email : zhouyl@qtec.cn
         * sex : 1
         * status : 1
         * createTime : 1511318476000
         * updateTime : 1511318476000
         * telephone :
         * mobilephone : 15528327788
         * deviceId : 903E91C1-0531-4CB1-9662-47B09B1134D2
         * deviceToken :
         * role : 2
         * company : 九州量子
         * apartment : 研发部
         * level : 7
         * online : 1
         * logo : http://192.168.90.79:8080/image/1511318476764.jpg
         */

        private int id;
        private String username;
        private String password;
        private String email;
        private int sex;
        private int status;
        private long createTime;
        private long updateTime;
        private String telephone;
        private String mobilephone;
        private String deviceId;
        private String deviceToken;
        private int role;
        private String company;
        private String apartment;
        private int level;
        private int online;
        private String logo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getApartment() {
            return apartment;
        }

        public void setApartment(String apartment) {
            this.apartment = apartment;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
