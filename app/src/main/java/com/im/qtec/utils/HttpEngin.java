package com.im.qtec.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class HttpEngin<T> {
    //private HttpListener<T> mHttpListener;

    private HttpEngin() {
    }

    private static HttpEngin mHttpEngin = null;

    public static HttpEngin getInstance(){
        if (mHttpEngin == null){
            synchronized (HttpEngin.class){
                if (mHttpEngin == null){
                    mHttpEngin = new HttpEngin();
                }
            }
        }
        return mHttpEngin;
    }



    public void post(String url, Object content, final Class<T> clazz, final HttpListener<T> mHttpListener){
        OkHttpUtils.postString()
                .url(url)
                .content(new Gson().toJson(content))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mHttpListener.onError(call,e,id);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        T t = new Gson().fromJson(response, clazz);
                        mHttpListener.onResponse(t,id);
                    }
                });
    }

    public  interface HttpListener<T>{
        public void onError(Call call, Exception e, int id);

        public void onResponse(T t, int id);
    }
}
