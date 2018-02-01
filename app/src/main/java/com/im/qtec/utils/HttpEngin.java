package com.im.qtec.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by zhouyanglei on 2017/11/29.
 */

public class HttpEngin<T> {
    //private HttpListener<T> mHttpListener;

    private HttpEngin() {
    }

    private static HttpEngin mHttpEngin = null;

    public static HttpEngin getInstance() {
        if (mHttpEngin == null) {
            synchronized (HttpEngin.class) {
                if (mHttpEngin == null) {
                    mHttpEngin = new HttpEngin();
                }
            }
        }
        return mHttpEngin;
    }


    public void post(String url, Object content, final Class<T> clazz, final HttpListener<T> mHttpListener) {
        OkHttpUtils.postString()
                .url(url)
                .content(new Gson().toJson(content))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mHttpListener.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        T t = new Gson().fromJson(response, clazz);
                        mHttpListener.onResponse(t, id);
                    }
                });
    }

    public void postFile(String url, File file, final Class<T> clazz, final FileLoadListener<T> fileLoadListener) {
        OkHttpUtils.post()//
                .addFile("mFile", UUID.randomUUID() + ".amr", file)//
                //.addFile("mFile", "test1.txt", file2)//
                .url(url)
                .params(null)//
                .headers(null)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        fileLoadListener.inProgress(progress, total, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        fileLoadListener.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        T t = new Gson().fromJson(response, clazz);
                        fileLoadListener.onResponse(t, id);
                    }
                });
    }

    public void getFile(String url, String filePath, String fileName, final FileLoadListener<String> fileLoadListener) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(filePath, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        fileLoadListener.inProgress(progress, total, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        fileLoadListener.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        fileLoadListener.onResponse(response.getAbsolutePath(), id);
                    }
                });
    }


    public interface HttpListener<T> {
        void onError(Call call, Exception e, int id);

        void onResponse(T t, int id);
    }

    public interface FileLoadListener<T> {
        void inProgress(float progress, long total, int id);

        void onError(Call call, Exception e, int id);

        void onResponse(T t, int id);
    }
}
