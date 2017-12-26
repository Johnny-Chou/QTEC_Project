package com.im.qtec.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.db.Chat;
import com.im.qtec.db.Contact;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.KeyRequestEntity;
import com.im.qtec.entity.KeyResultEntity;
import com.im.qtec.event.MessageEvent;
import com.im.qtec.service.MQTTService;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.MessageUtils;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;
import com.im.qtec.widget.PortraitView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * @author zhouyanglei
 * @date 2017/12/18
 */

public class ChatActivity extends BaseActivity implements TextWatcher, HttpEngin.HttpListener<KeyResultEntity> {
    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.mVoiceBtn)
    ImageButton mVoiceBtn;
    @Bind(R.id.mInputEt)
    EditText mInputEt;
    @Bind(R.id.mFireBtn)
    ImageButton mFireBtn;
    @Bind(R.id.mEmotionBtn)
    ImageButton mEmotionBtn;
    @Bind(R.id.mSendBtn)
    Button mSendBtn;
    @Bind(R.id.mAddBtn)
    ImageButton mAddBtn;
    private Contact contact;
    private List<Chat> chatList;
    private ChatAdapter chatAdapter;
    private Info userinfo;
    private MQTTService.MyBinder myBinder;
    private static final int SEND_ITEM = 1;
    private static final int RECEIVE_ITEM = 0;
    private static final int CHAT_TYPE_MESSAGE = 1;//普通文字消息

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MQTTService.MyBinder) service;
        }
    };
    //    ChatType_CHAT_TYPE_PICTURE,//图片消息
//    ChatType_CHAT_TYPE_AUDIO,//语音消息
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

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_chat, -1, R.menu.menu_friend_detail, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        setUpMenu(R.menu.menu_chat_detail);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent serviceIntent = new Intent(this, MQTTService.class);
        //startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        mInputEt.addTextChangedListener(this);
        contact = (Contact) getIntent().getSerializableExtra(ConstantValues.CONTACT_INFO);
        setUpTitle(contact.getUsername());
        userinfo = new Gson().fromJson(SPUtils.getString(this, ConstantValues.USER_INFO, ""), Info.class);
        chatList = DataSupport.where("uid = ?", contact.getUid() + "").find(Chat.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(chatAdapter);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }, 10);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollToBottom();
    }

    private void scrollToBottom() {
        chatAdapter.notifyDataSetChanged();
        if (chatList.size() > 0) {
            mRecyclerView.smoothScrollToPosition(chatList.size() - 1);
        }
    }

    @OnClick({R.id.mVoiceBtn, R.id.mFireBtn, R.id.mEmotionBtn, R.id.mSendBtn, R.id.mAddBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mVoiceBtn:
                break;
            case R.id.mFireBtn:
                break;
            case R.id.mEmotionBtn:
                break;
            case R.id.mSendBtn:
                sendMessage();
                break;
            case R.id.mAddBtn:
                break;
        }
    }

    private void sendMessage() {
        String allKeys = SPUtils.getString(this, ConstantValues.KEYS, "");
        String key = "";
        if (allKeys.length() >= 16) {
            key = allKeys.substring(0, 16);
            String remainedKeys = allKeys.substring(16);
            SPUtils.saveString(this, ConstantValues.KEYS, remainedKeys);
            if (remainedKeys.length() < 10 * 16) {
                HttpEngin.getInstance().post(UrlHelper.GET_KEY,
                        new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID(), 50 * 16),
                        KeyResultEntity.class, this);
            }
        } else {
            key = "0000111122223333";
            HttpEngin.getInstance().post(UrlHelper.GET_KEY,
                    new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID(), 50 * 16),
                    KeyResultEntity.class, this);
        }
        byte[] encryptedMessage = MessageUtils.getEncryptedMessage((char) 32, key.getBytes(), userinfo.getResData().getId(),
                contact.getUid(), (byte) CHAT_TYPE_MESSAGE, (byte) 0,
                (int) (System.currentTimeMillis() / 1000), mInputEt.getText().toString().getBytes());
        myBinder.publishMessage(encryptedMessage, contact.getUid());
        mInputEt.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 0) {
            mAddBtn.setVisibility(View.GONE);
            mSendBtn.setVisibility(View.VISIBLE);
        } else {
            mAddBtn.setVisibility(View.VISIBLE);
            mSendBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        ToastUtils.showLong("获取密钥失败，请检查网络");
    }

    @Override
    public void onResponse(KeyResultEntity result, int id) {
        if (result.isFlag()) {
            String value = result.getResData().getValue();
            String remainedKeys = SPUtils.getString(this, ConstantValues.KEYS, "");
            SPUtils.saveString(this, ConstantValues.KEYS, remainedKeys + value);
        }
    }

    class ChatAdapter extends RecyclerView.Adapter {
        @Override
        public int getItemViewType(int position) {
            Chat chat = chatList.get(position);
            if (chat.getIsSend() == 0) {
                return RECEIVE_ITEM;
            } else {
                return SEND_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == RECEIVE_ITEM) {
                view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item_receive, parent, false);
                return new ReceiveViewHolder(view);
            } else {
                view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item_send, parent, false);
                return new SendViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == RECEIVE_ITEM) {
                ReceiveViewHolder receiveViewHolder = (ReceiveViewHolder) holder;
                receiveViewHolder.bindData(position);
            } else {
                SendViewHolder sendViewHolder = (SendViewHolder) holder;
                sendViewHolder.bindData(position);
            }

        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }
    }

    abstract class BaseHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.mTimeTv)
        TextView mTimeTv;
        @Bind(R.id.mPortraitView)
        PortraitView mPortraitView;
        @Bind(R.id.mChatTv)
        TextView mChatTv;

        public BaseHolder(View itemView) {
            super(itemView);
            SupportMultipleScreensUtil.scale(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(int position) {
            Chat chat = chatList.get(position);
            if (position == 0) {
                mTimeTv.setVisibility(View.VISIBLE);
            } else {
                Chat lastChat = chatList.get(position - 1);
                if (MessageUtils.getTime(chat.getMessage()) - MessageUtils.getTime(lastChat.getMessage()) > 5 * 60) {
                    mTimeTv.setVisibility(View.VISIBLE);
                } else {
                    mTimeTv.setVisibility(View.GONE);
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss", Locale.getDefault());
            mTimeTv.setText(TimeUtils.millis2String((long) MessageUtils.getTime(chat.getMessage()) * 1000,simpleDateFormat));
            setPortrait();
            byte messageType = MessageUtils.getMessageType(chat.getMessage());
            if (messageType == CHAT_TYPE_MESSAGE) {
                mChatTv.setText(MessageUtils.getPlainMessage(chat.getMessage()));
            }
        }

        /**
         * 根据不同的角色设置不同的头像
         */
        protected abstract void setPortrait();
    }


    class ReceiveViewHolder extends BaseHolder {

        public ReceiveViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setPortrait() {
            mPortraitView.setPortrait(ChatActivity.this, contact.getLogo(), contact.getUsername());
        }
    }

    class SendViewHolder extends BaseHolder {

        public SendViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setPortrait() {
            mPortraitView.setPortrait(ChatActivity.this, userinfo.getResData().getLogo(), userinfo.getResData().getUsername());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMqttMessage(MessageEvent event) {
        chatList = DataSupport.where("uid = ?", contact.getUid() + "").find(Chat.class);
        scrollToBottom();
    }
}
