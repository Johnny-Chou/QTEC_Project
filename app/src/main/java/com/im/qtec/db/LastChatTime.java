package com.im.qtec.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhouyanglei on 2018/2/8.
 */

public class LastChatTime extends DataSupport{
    private int myid;
    private int yourid;
    private long timestamp;

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getYourid() {
        return yourid;
    }

    public void setYourid(int yourid) {
        this.yourid = yourid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
