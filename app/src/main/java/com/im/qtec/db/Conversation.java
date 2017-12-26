package com.im.qtec.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhouyanglei on 2017/12/19.
 */

public class Conversation extends DataSupport{
    private int uid;
    private byte[] lastMessage;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(byte[] lastMessage) {
        this.lastMessage = lastMessage;
    }

}
