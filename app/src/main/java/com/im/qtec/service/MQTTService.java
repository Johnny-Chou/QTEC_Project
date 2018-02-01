package com.im.qtec.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.im.qtec.activity.MainActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.db.Chat;
import com.im.qtec.db.Contact;
import com.im.qtec.db.Conversation;
import com.im.qtec.event.MessageEvent;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.MessageUtils;
import com.im.qtec.utils.SPUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

import okhttp3.Call;

public class MQTTService extends Service {
    private static final int CHAT_TYPE_MESSAGE = 1;
    private static final int CHAT_TYPE_PICTURE = 2;
    private static final int CHAT_TYPE_VOICE = 3;

    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private String host = "tcp://192.168.91.137:1883";
    private String userName;
    private String passWord = "password";
    private static String myTopic;
    private String clientId = DeviceUtils.getAndroidID();
    private int myid;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userName = myid + "";
        myid = SPUtils.getInt(this, ConstantValues.USER_ID, -1);
        myTopic = "single/" + myid + "/#";
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    public void publish(byte[] msg, int userid) {
        String topic = "single/" + userid + "/" + myid;
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(topic, msg, qos.intValue(), retained.booleanValue());
            saveMessage(msg, 1, userid);
            EventBus.getDefault().post(new MessageEvent());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(byte[] yourMsg, int userid, byte[] myMsg) {
        String topic = "single/" + userid + "/" + myid;
        Integer qos = 0;
        Boolean retained = false;
        try {
            if (yourMsg != null) {
                client.publish(topic, yourMsg, qos.intValue(), retained.booleanValue());
            }
            if (myMsg != null) {
                saveMessage(myMsg, 1, userid);
                EventBus.getDefault().post(new MessageEvent());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId, new MemoryPersistence());
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(60);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        String topic = "single/" + myid;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {
            doClientConnection();
        }

    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!client.isConnected()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
                //client.unsubscribe(myTopic,this, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
                //if (!isSubscribed) {
                client.subscribe(myTopic, 1);
                //   isSubscribed = true;
                // }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
            doClientConnection();
        }
    };

    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

//            String str1 = new String(message.getPayload());
//            MqttMessage msg = new MqttMessage();
//            msg.setMessage(str1);
            if (!TextUtils.isEmpty(topic) && topic.contains("/")) {
                int lastIndex = topic.lastIndexOf("/");
                String userid = topic.substring(lastIndex + 1);
                final int uid = Integer.valueOf(userid);

                final byte[] payload = message.getPayload();
                if (MessageUtils.getMessageType(payload) == CHAT_TYPE_VOICE) {
                    String voiceUrl = new String(MessageUtils.getMessageContent(payload));
                    HttpEngin.getInstance().getFile(voiceUrl, getFilesDir().getAbsolutePath(), generateFileName(), new HttpEngin.FileLoadListener<String>() {
                        @Override
                        public void inProgress(float progress, long total, int id) {

                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showLong("接收语音失败");
                        }

                        @Override
                        public void onResponse(String s, int id) {
                            saveMessage(MessageUtils.setMessageContent(payload, s.getBytes()), 0, uid);
                            EventBus.getDefault().post(new MessageEvent());
                        }
                    });
                } else {
                    saveMessage(payload, 0, uid);
                    EventBus.getDefault().post(new MessageEvent());
                }
            }
//            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//            Log.i(TAG, "messageArrived:" + str1);
//            Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            doClientConnection();
        }
    };

    private void saveMessage(byte[] message, int isSend, int id) {
        List<Contact> contacts = DataSupport.where("uid = ?", id + "").find(Contact.class);
        if (contacts.size() != 0) {
            Chat chat = new Chat();
            chat.setUid(id);
            chat.setIsSend(isSend);
            chat.setMessage(message);
            chat.save();
            List<Conversation> conversationList = DataSupport.where("uid = ?", id + "").find(Conversation.class);
            if (conversationList.size() == 0) {
                Conversation conversation = new Conversation();
                conversation.setUid(id);
                conversation.setLastMessage(message);
                conversation.save();
            } else {
                ContentValues values = new ContentValues();
                values.put("lastMessage", message);
                DataSupport.updateAll(Conversation.class, values, "uid = ?", id + "");
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    public class MyBinder extends Binder {

        public void publishMessage(byte[] msg, int userid) {
            publish(msg, userid);
        }

        public void publishVoice(byte[] yourMsg, int userid, byte[] myMsg) {
            publish(yourMsg, userid, myMsg);
        }

    }
}
