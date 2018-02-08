package com.im.qtec.utils;

/**
 * Created by zhouyanglei on 2017/11/28.
 */

public class UrlHelper {
    public static String BASE_IP = "192.168.91.137:8080";
    public static String MQTT_IP = "192.168.91.137:1883";
    public static String BASE_URL = "http://" + BASE_IP;
    public static String LOGIN_URL = BASE_URL + "/talkserver/login.do";
    public static String CHECK_LOGIN_URL = BASE_URL +"/talkserver/checkvalid.do";
    public static String INFO_DETAIL = BASE_URL +"/talkserver//detail.do";
    public static String GET_CONTACTS = BASE_URL +"/talkserver//contact.do";
    public static String GET_TOPIC = BASE_URL +"/talkserver/getConfig.do";
    public static String GET_KEY = BASE_URL +"/talkserver/valid.do";
    public static String UPLOAD_FILE = BASE_URL +"/talkserver/uploadConfig.do";
    public static String LOGOUT_URL = BASE_URL +"/talkserver/logout.do";
    public static String UPDATE_PASSWORD = BASE_URL +"/talkserver/updatePasswd.do";
}
