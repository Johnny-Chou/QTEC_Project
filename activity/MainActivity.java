package com.im.qtec.myapplication.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.im.qtec.myapplication.entity.Person;
import com.example.zhouyanglei.myapplication.R;
import com.google.gson.Gson;
import com.im.qtec.myapplication.service.MQTTService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.Call;
import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {
    private MQTTService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MQTTService.MyBinder) service;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Intent serviceIntent = new Intent(this, MQTTService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String id = telephonyManager.getSubscriberId();
        OkHttpUtils
                .postString()
                .url("http://192.168.90.79:8080/talkserver/login.do")
                .content(new Gson().toJson(new Person("15528327788", "327788")))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("main", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("main", response);
                    }
                });
        findViewById(R.id.publishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBinder.publishMessage("CSDN 一口仨馍");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(MqttMessage mqttMessage) {
        Log.i(MQTTService.TAG, "get message:" + mqttMessage.getPayload());
        Toast.makeText(this, new String(mqttMessage.getPayload()), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        unbindService(connection);
    }

}
