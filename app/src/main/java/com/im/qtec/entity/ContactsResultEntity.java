package com.im.qtec.entity;

import com.im.qtec.db.Contact;

import java.util.List;

/**
 * Created by zhouyanglei on 2017/12/14.
 */

public class ContactsResultEntity {


    /**
     * flag :
     * errCode :
     * resData : [{"id":207,"username":"李震宇","password":"","email":"lizhenyu@qtec.cn","sex":1,"status":0,"createTime":1510829914000,"updateTime":1510829914000,"telephone":"0571-86336999","mobilephone":"18606719629","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":208,"username":"李震宇1","password":"","email":"lizhenyu1@qtec.cn","sex":1,"status":0,"createTime":1510830194000,"updateTime":1510830194000,"telephone":"0571-86336999","mobilephone":"18606719620","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":209,"username":"李震宇2","password":"","email":"lizhenyu2@qtec.cn","sex":1,"status":0,"createTime":1510830321000,"updateTime":1510830321000,"telephone":"0571-86336999","mobilephone":"18606719622","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":210,"username":"李震宇3","password":"","email":"lizhenyu3@qtec.cn","sex":1,"status":0,"createTime":1510830330000,"updateTime":1510830330000,"telephone":"0571-86336999","mobilephone":"18606719623","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":211,"username":"李震宇4","password":"","email":"lizhenyu4@qtec.cn","sex":1,"status":0,"createTime":1510830337000,"updateTime":1510830337000,"telephone":"0571-86336999","mobilephone":"18606719624","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":212,"username":"李震宇5","password":"","email":"lizhenyu5@qtec.cn","sex":1,"status":0,"createTime":1510830346000,"updateTime":1510830346000,"telephone":"0571-86336999","mobilephone":"18606719625","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":213,"username":"李震宇6","password":"","email":"lizhenyu6@qtec.cn","sex":1,"status":0,"createTime":1510830356000,"updateTime":1510830356000,"telephone":"0571-86336999","mobilephone":"18606719626","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":214,"username":"李震宇7","password":"","email":"lizhenyu7@qtec.cn","sex":1,"status":0,"createTime":1510830365000,"updateTime":1510830365000,"telephone":"0571-86336999","mobilephone":"18606719627","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"},{"id":215,"username":"李震宇8","password":"","email":"lizhenyu8@qtec.cn","sex":1,"status":0,"createTime":1510830374000,"updateTime":1510830374000,"telephone":"0571-86336999","mobilephone":"18606719628","deviceId":"","role":2,"company":"九州量子","apartment":"研发部","level":"员工","online":1,"logo":"http://192.168.90.79:8080/image/1511355804271.jpg"}]
     */

    private boolean flag;
    private String errCode;
    private List<Contact> resData;

    public boolean getFlag() {
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

    public List<Contact> getResData() {
        return resData;
    }

    public void setResData(List<Contact> resData) {
        this.resData = resData;
    }

}
