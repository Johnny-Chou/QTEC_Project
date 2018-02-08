package com.im.qtec.entity;

import com.im.qtec.db.Contact;

import java.util.List;

/**
 * Created by zhouyanglei on 2017/12/14.
 */

public class ContactsResultEntity {


    /**
     * resData : {"versionData":"2018-02-06 17:18:48","contact":[{"id":245,"username":"7","password":"","email":"7@qq.com","sex":9,"status":1,"createTime":1512462783000,"updateTime":1512462783000,"telephone":"","mobilephone":"15738857507","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"员工","online":0,"logo":"http://192.168.90.79:8080/image/1512462783158.jpg","uid":245},{"id":246,"username":"8","password":"","email":"8@qq.com","sex":9,"status":1,"createTime":1512462791000,"updateTime":1512462791000,"telephone":"","mobilephone":"15738857508","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"员工","online":0,"logo":"http://192.168.90.79:8080/image/1512462791414.jpg","uid":246},{"id":300,"username":"qwqsqws","password":"","email":"158888@qq.com","sex":1,"status":1,"createTime":1517453449000,"updateTime":1517453449000,"telephone":"","mobilephone":"13785552222","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"","online":0,"logo":"","uid":300}]}
     */

    private ResDataBean resData;

    public ResDataBean getResData() {
        return resData;
    }

    public void setResData(ResDataBean resData) {
        this.resData = resData;
    }

    public static class ResDataBean {
        /**
         * versionData : 2018-02-06 17:18:48
         * contact : [{"id":245,"username":"7","password":"","email":"7@qq.com","sex":9,"status":1,"createTime":1512462783000,"updateTime":1512462783000,"telephone":"","mobilephone":"15738857507","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"员工","online":0,"logo":"http://192.168.90.79:8080/image/1512462783158.jpg","uid":245},{"id":246,"username":"8","password":"","email":"8@qq.com","sex":9,"status":1,"createTime":1512462791000,"updateTime":1512462791000,"telephone":"","mobilephone":"15738857508","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"员工","online":0,"logo":"http://192.168.90.79:8080/image/1512462791414.jpg","uid":246},{"id":300,"username":"qwqsqws","password":"","email":"158888@qq.com","sex":1,"status":1,"createTime":1517453449000,"updateTime":1517453449000,"telephone":"","mobilephone":"13785552222","deviceId":"","deviceToken":"","voipToken":"","role":2,"company":"","apartment":"","level":"","online":0,"logo":"","uid":300}]
         */

        private String versionData;
        private List<Contact> contact;

        public String getVersionData() {
            return versionData;
        }

        public void setVersionData(String versionData) {
            this.versionData = versionData;
        }

        public List<Contact> getContact() {
            return contact;
        }

        public void setContact(List<Contact> contact) {
            this.contact = contact;
        }
    }
}
