package com.im.qtec.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhouyanglei on 2017/12/19.
 */

public class Chat extends DataSupport {
    //    ChatType_CHAT_TYPE_MESSAGE = 1,//普通文字消息
//    ChatType_CHAT_TYPE_PICTURE=2,//图片消息
//    ChatType_CHAT_TYPE_AUDIO=3,//语音消息
//    ChatType_CHAT_TYPE_VIDEO,//视频消息
//    ChatType_CHAT_TYPE_FILE,//文件类型
//    ChatType_CHAT_TYPE_OTHER,//其他文件
//    ChatType_CHAT_TYPE_COUNT,//所有单聊
//    ChatType_GROUP_TYPE_MESSAGE = 50,//群聊普通文字消息
//    ChatType_GROUP_TYPE_PICTURE,//群聊图片消息
//    ChatType_GROUP_TYPE_AUDIO,//群聊语音消息
//    ChatType_GROUP_TYPE_VIDEO,//群聊视频消息
//    ChatType_GROUP_TYPE_FILE,//群聊文件类型
//    ChatType_GROUP_TYPE_OTHER,//群聊其他文件
//    ChatType_GROUP_TYPE_COUNT,//所有群聊
//    [data appendBytes:&headerSize length:sizeof(headerSize)]; //定义协议头长度     2 字节
//    [data appendBytes:key.UTF8String length:16];    //密钥                       36 字节
//    [data appendBytes:&fromUserId length:sizeof(fromUserId)];//发送者ID           4 字节
//    [data appendBytes:&toUserId length:sizeof(toUserId)];//接收者ID               4 字节
//    [data appendBytes:&sendType length:sizeof(sendType)];//消息类型                1 字节
//    [data appendBytes:&needDestroy length:sizeof(needDestroy)];//是否阅后即焚       1 字节
//    [data appendBytes:&time length:sizeof(time)];//                              4 字节
//    语音消息的长度                              1 字节
    private int uid;

    private byte[] message;

    private int isSend;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
}
