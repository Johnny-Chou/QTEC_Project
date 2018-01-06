package com.im.qtec.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.KeyboardUtils;
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
import com.im.qtec.utils.EmoParser;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.KeyboardChangeListener;
import com.im.qtec.utils.MessageUtils;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;
import com.im.qtec.widget.EmoNormalGrid;
import com.im.qtec.widget.EmoView;
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
    @Bind(R.id.mEditorContainer)
    LinearLayout mEditorContainer;
    @Bind(R.id.mEditorBar)
    LinearLayout mEditorBar;
    @Bind(R.id.mEditorMenuView)
    LinearLayout mEditorMenuView;
    @Bind(R.id.mEmoView)
    EmoView mEmoView;
    @Bind(R.id.mMenuContainer)
    RelativeLayout mMenuContainer;
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
    private KeyboardChangeListener keyboardChangeListener;
    private boolean keyboardIsShown;
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
        // mEditorMenuView.
        mInputEt.addTextChangedListener(this);
        mInputEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mMenuContainer.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    //hideEmotionLayout(true);//隐藏表情布局，显示软件盘
//                    mEditorMenuView.setVisibility(View.GONE);
//                    KeyboardUtils.showSoftInput(ChatActivity.this);
                    hideMenuContainer(true);
                    //软件盘显示后，释放内容高度
                    unlockContentHeightDelayed();
                }
                return false;
            }
        });
        keyboardChangeListener = new KeyboardChangeListener(this);
        keyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                //Log.d(TAG, "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
                keyboardIsShown = isShow;
                if (isShow) {
                    SPUtils.saveInt(ChatActivity.this, ConstantValues.KEYBOARD_HEIGHT, keyboardHeight);
                }
            }
        });
        mEmoView.initializeData(new EmoNormalGrid.OnEmoClickListener() {
            @Override
            public void onDeleteButtonClick() {
                mInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                mInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            @Override
            public void onNormalEmoClick(String emo, int resId) {
                emo = "[" + emo + "]";
                SpannableStringBuilder parseEmo = EmoParser.parseEmo(ChatActivity.this, emo,25);
                Editable editable = mInputEt.getText();
                int index = mInputEt.getSelectionEnd();
                if (index < mInputEt.length()) {
                    editable.insert(index, parseEmo);
                } else {
                    editable.append(parseEmo);
                }
                mInputEt.setSelection(index + emo.length());
            }
        });
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
                showEmo();
                break;
            case R.id.mSendBtn:
                sendMessage();
                break;
            case R.id.mAddBtn:
                showMenu();
                break;
        }
    }

    private void showEmo() {
        if (mMenuContainer.isShown()) {
            if (!mEditorMenuView.isShown()) {
                lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                hideMenuContainer(true);//隐藏表情布局，显示软件盘
                unlockContentHeightDelayed();//软件盘显示后，释放内容高度
            } else {
                mEmoView.setVisibility(View.VISIBLE);
                mEditorMenuView.setVisibility(View.GONE);
            }
        } else {
            if (keyboardIsShown) {//同上
                lockContentHeight();
                showMenuContainer(true);
                unlockContentHeightDelayed();
            } else {
                showMenuContainer(true);//两者都没显示，直接显示表情布局
            }
        }
    }

    private void showMenu() {
        if (mMenuContainer.isShown()) {
            if (!mEmoView.isShown()) {
                lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                hideMenuContainer(true);//隐藏表情布局，显示软件盘
                unlockContentHeightDelayed();//软件盘显示后，释放内容高度
            } else {
                mEmoView.setVisibility(View.GONE);
                mEditorMenuView.setVisibility(View.VISIBLE);
            }
        } else {
            if (keyboardIsShown) {//同上
                lockContentHeight();
                showMenuContainer(false);
                unlockContentHeightDelayed();
            } else {
                showMenuContainer(false);//两者都没显示，直接显示表情布局
            }
        }
    }

    private void showMenuContainer(boolean isEmo) {
        if (!mMenuContainer.isShown()) {
            KeyboardUtils.hideSoftInput(this);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mEditorContainer.getLayoutParams();
            layoutParams.height = mEditorBar.getHeight() + SPUtils.getInt(this, ConstantValues.KEYBOARD_HEIGHT, 1108);
            mMenuContainer.setVisibility(View.VISIBLE);
            if (!isEmo) {
                mEditorMenuView.setVisibility(View.VISIBLE);
                mEmoView.setVisibility(View.GONE);
            }else {
                mEditorMenuView.setVisibility(View.GONE);
                mEmoView.setVisibility(View.VISIBLE);
            }
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
            mTimeTv.setText(TimeUtils.millis2String((long) MessageUtils.getTime(chat.getMessage()) * 1000, simpleDateFormat));
            setPortrait();
            byte messageType = MessageUtils.getMessageType(chat.getMessage());
            if (messageType == CHAT_TYPE_MESSAGE) {
//                mChatTv.setText(MessageUtils.getPlainMessage(chat.getMessage()));
                mChatTv.setText(EmoParser.parseEmo(ChatActivity.this, MessageUtils.getPlainMessage(chat.getMessage()),25));
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
        //keyboardChangeListener.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMqttMessage(MessageEvent event) {
        chatList = DataSupport.where("uid = ?", contact.getUid() + "").find(Chat.class);
        scrollToBottom();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //View v = getCurrentFocus();
            if (isShouldHideKeyboard(mEditorBar, ev)) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                KeyboardUtils.hideSoftInput(this);
                //mEditorMenuView.setVisibility(View.GONE);
                //unlockContentHeightDelayed();
                hideMenuView();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideMenuView() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mEditorContainer.getLayoutParams();
        layoutParams.height = mEditorBar.getHeight();
        mMenuContainer.setVisibility(View.GONE);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return event.getY() < top;
        }
        return false;
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
        params.height = mRecyclerView.getHeight();
        params.weight = 0.0F;
    }


    private void unlockContentHeightDelayed() {
        mInputEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
                layoutParams.height = 0;
                layoutParams.weight = 1.0F;
            }
        }, 10L);
    }

    private void hideMenuContainer(boolean showSoftInput) {
        if (mMenuContainer.isShown()) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mEditorContainer.getLayoutParams();
            layoutParams.height = mEditorBar.getHeight();
            mMenuContainer.setVisibility(View.GONE);
            if (showSoftInput) {
                KeyboardUtils.showSoftInput(this);
            }
        }
    }
}
