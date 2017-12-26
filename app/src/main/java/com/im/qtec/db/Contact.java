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
     * uid : 207
     * username : 李震宇
     * password :
     * email : lizhenyu@qtec.cn
     * sex : 1
     * status : 0
     * createTime : 1510829914000
     * updateTime : 1510829914000
     * telephone : 0571-86336999
     * mobilephone : 18606719629
     * deviceId :
     * role : 2
     * company : 九州量子
     * apartment : 研发部
     * level : 员工
     * online : 1
     * logo : http://192.168.90.79:8080/image/1511355804271.jpg
     */

    private int uid;
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
    private int role;
    private String company;
    private String apartment;
    private String level;
    private int online;
    private String logo;

    public int getUid() {
        return uid;
    }

    public void setUid(int id) {
        this.uid = id;
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
}
