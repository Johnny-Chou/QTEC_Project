package com.im.qtec.db;

import com.im.qtec.utils.PinYinUtil;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author zhouyanglei
 * @date 2017/12/14
 */

public class Contact extends DataSupport implements Comparable<Contact>, Serializable {


    /**
     * id : 245
     * username : 7
     * password :
     * email : 7@qq.com
     * sex : 9
     * status : 1
     * createTime : 1512462783000
     * updateTime : 1512462783000
     * telephone :
     * mobilephone : 15738857507
     * deviceId :
     * deviceToken :
     * voipToken :
     * role : 2
     * company :
     * apartment :
     * level : 员工
     * online : 0
     * logo : http://192.168.90.79:8080/image/1512462783158.jpg
     * uid : 245
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
    private String voipToken;
    private int role;
    private String company;
    private String apartment;
    private String level;
    private int online;
    private String logo;
    private int uid;

    @Override
    public int compareTo(Contact another) {
        String pinyin = PinYinUtil.getPinyin(username);
        if (PinYinUtil.getFirstLetter(this) == '#' && PinYinUtil.getFirstLetter(another) != '#') {
            return 1;
        } else if (PinYinUtil.getFirstLetter(this) != '#' && PinYinUtil.getFirstLetter(another) == '#') {
            return -1;
        }
        return pinyin.toUpperCase().compareTo(PinYinUtil.getPinyin(another.getUsername().toUpperCase()));
    }

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

    public String getVoipToken() {
        return voipToken;
    }

    public void setVoipToken(String voipToken) {
        this.voipToken = voipToken;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
