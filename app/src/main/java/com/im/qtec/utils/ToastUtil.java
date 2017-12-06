package com.im.qtec.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/2.
 */

public class ToastUtil {
    private  static Toast toast;
    public static void show(Context context, String msg){
        if (toast==null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
        }
        toast.show();
    }
}
