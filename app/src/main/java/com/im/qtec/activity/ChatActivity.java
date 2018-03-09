package com.im.qtec.activity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.core.MediaManager;
import com.im.qtec.db.Chat;
import com.im.qtec.db.Contact;
import com.im.qtec.db.LastChatTime;
import com.im.qtec.entity.FileUploadResultEntity;
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
import com.im.qtec.widget.AudioRecorderButton;
import com.im.qtec.widget.EditorMenuView;
import com.im.qtec.widget.EmoNormalGrid;
import com.im.qtec.widget.EmoView;
import com.im.qtec.widget.PortraitView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qtec.qkcl.crypto.enums.EEncrypAlg;
import cn.qtec.qkcl.envelope.EnvelopEnc;
import cn.qtec.qkcl.envelope.EnvelopEncInterface;
import cn.qtec.qkcl.envelope.future.StopableFuture;
import cn.qtec.qkcl.envelope.pojo.QtKey;
import cn.qtec.qkcl.envelope.pojo.QtKeyId;
import cn.qtec.qkcl.envelope.pojo.QtRecipientInfo;
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
    EditorMenuView mEditorMenuView;
    @Bind(R.id.mEmoView)
    EmoView mEmoView;
    @Bind(R.id.mMenuContainer)
    RelativeLayout mMenuContainer;
    @Bind(R.id.mVoiceBtn)
    ImageButton mVoiceBtn;
    @Bind(R.id.mInputEt)
    EditText mInputEt;
    @Bind(R.id.mAudioRecordBtn)
    AudioRecorderButton mAudioRecordBtn;
    @Bind(R.id.mFireBtn)
    ImageButton mFireBtn;
    @Bind(R.id.mEmotionBtn)
    ImageButton mEmotionBtn;
    @Bind(R.id.mSendBtn)
    Button mSendBtn;
    @Bind(R.id.mAddBtn)
    ImageButton mAddBtn;
    private View mAnimViewLabel;
    private Contact contact;
    private List<Chat> chatList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private Info userinfo;
    private MQTTService.MyBinder myBinder;
    private static final int RECEIVE_ITEM = 0;
    //    private static final int RECEIVE_VOICE_ITEM = 1;
//    private static final int RECEIVE_PICTURE_ITEM = 2;
    private static final int SEND_ITEM = 3;
//    private static final int SEND_VOICE_ITEM = 4;
//    private static final int SEND_PICTURE_ITEM = 5;


    private static final int CHAT_TYPE_MESSAGE = 1;//普通文字消息
    private static final int CHAT_TYPE_PICTURE = 2;
    private static final int CHAT_TYPE_VOICE = 3;
    private static final int REQUEST_PHOTO = 100;
    private static final int REQUEST_CAMERA = 200;

    private static final String PICTURE_PATH = "picture_path";

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
        bindService();
        initInputEditor();
        initKeyboard();
        initEmoView();
        initEditorMenuView();
        initAudioRecordBtn();
        setUpContact();
    }

    private void initAudioRecordBtn() {
        mAudioRecordBtn.setOnRecordingListener(new AudioRecorderButton.OnRecordingListener() {
            @Override
            public void onRecording() {
                if (mAnimViewLabel != null && mAnimViewLabel.getTag() != null) {
                    boolean isSend = (boolean) mAnimViewLabel.getTag();
                    mAnimViewLabel.setBackgroundResource(isSend ? R.mipmap.mes_myvoiceplay3 : R.mipmap.mes_oppovoiceplay3);
                }
            }
        });
        mAudioRecordBtn.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(final float seconds, final String filePath) {
                HttpEngin.getInstance().post(UrlHelper.GET_CODE,
                        new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID()),
                        KeyResultEntity.class, new HttpEngin.HttpListener<KeyResultEntity>() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showLong("获取密钥失败，请重试");
                            }

                            @Override
                            public void onResponse(KeyResultEntity resultEntity, int id) {
                                if (resultEntity.isFlag()) {
                                    String keyId = resultEntity.getResData().getId();
                                    String key = resultEntity.getResData().getValue();
                                    String encryptPath = encryptFile(keyId, key, filePath);
                                    sendVoice(seconds, null, filePath, keyId);
                                    HttpEngin.getInstance().postFile(UrlHelper.UPLOAD_FILE + "?id=" + userinfo.getResData().getId() + "&deviceId=" + DeviceUtils.getAndroidID(),
                                            new File(encryptPath), FileUploadResultEntity.class, new HttpEngin.FileLoadListener<FileUploadResultEntity>() {
                                                @Override
                                                public void inProgress(float progress, long total, int id) {

                                                }

                                                @Override
                                                public void onError(Call call, Exception e, int id) {
                                                    e.printStackTrace();
                                                }

                                                @Override
                                                public void onResponse(FileUploadResultEntity entity, int id) {
                                                    if (entity.isFlag()) {
                                                        sendVoice(seconds, entity.getResData(), null, keyId);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }

    private String encryptFile(String keyId, String key, String filePath) {
        EnvelopEncInterface envelopEnc = EnvelopEnc.getInstance();
        //模拟创建接收者信息
        List<QtRecipientInfo> list = new ArrayList<>();
        QtRecipientInfo qtRecipientInfo = new QtRecipientInfo();
        //设置接收者的算法
        qtRecipientInfo.seteEncrypAlg(EEncrypAlg.AES256_CBC_PKCS5PADDING);
        //模拟生成一个接收者的共享密钥
        QtKey qtKey = new QtKey();
//        qtKey.randomValue();
//        qtRecipientInfo.setKey(qtKey);
        //模拟生成一个接收者的密钥id
        QtKeyId qtKeyId = new QtKeyId();
        for (byte i = 0; i < qtKey.getBuffer().length; i++) {
            qtKey.getBuffer()[i] = key.getBytes()[i];
        }
        for (byte i = 0; i < qtKeyId.getBuffer().length; i++) {
            qtKeyId.getBuffer()[i] = keyId.getBytes()[i];
        }
        qtRecipientInfo.setKey(qtKey);
        qtRecipientInfo.setKeyId(qtKeyId);
        list.add(qtRecipientInfo);

        try {
            File file = new File(filePath);
            int lastIndex = filePath.lastIndexOf(".");
            String suffix = filePath.substring(lastIndex);
            String encryptPath = filePath.substring(0, lastIndex) + "encrypt" + suffix;
            StopableFuture future = envelopEnc.encryptFile(file.getAbsolutePath(), list, EEncrypAlg.AES256_CBC_PKCS5PADDING, encryptPath);
            //在1000毫秒内获取加密结果，如果获取不到结果则返回
            future.get();
            //获取加解密结果
            //future.get();
            return encryptPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void sendVoice(float seconds, String yourPath, String myPath, String keyId) {
        byte[] yourEncryptedMessage = null;
        byte[] myEncryptedMessage = null;
        if (yourPath != null) {
            yourEncryptedMessage = MessageUtils.getMessage((char) 53, keyId.getBytes(), userinfo.getResData().getId(),
                    contact.getUid(), (byte) CHAT_TYPE_VOICE, (byte) 0,
                    (int) (System.currentTimeMillis() / 1000), Math.round(seconds), yourPath.getBytes());
        }
        if (myPath != null) {
            myEncryptedMessage = MessageUtils.getMessage((char) 53, keyId.getBytes(), userinfo.getResData().getId(),
                    contact.getUid(), (byte) CHAT_TYPE_VOICE, (byte) 0,
                    (int) (System.currentTimeMillis() / 1000), Math.round(seconds), myPath.getBytes());
        }
        myBinder.publishFile(yourEncryptedMessage, contact.getUid(), myEncryptedMessage);
    }

    private void initEditorMenuView() {
        mEditorMenuView.setListener(new EditorMenuView.EditorMenuListener() {
            @Override
            public void onClick(int id) {
                switch (id) {
                    case R.id.mPhotoBtn:
                        //startActivityForResult(new Intent(ChatActivity.this, PhotoActivity.class), REQUEST_PHOTO);选择图片
                        ImageSelectorUtils.openPhoto(ChatActivity.this, REQUEST_PHOTO, true, 0);
                        break;
                    case R.id.mCameraBtn:
                        startActivityForResult(new Intent(ChatActivity.this, CameraActivity.class), REQUEST_CAMERA);
                        break;
                    case R.id.mCallBtn:
                        break;
                    case R.id.mLocationBtn:
                        break;
                    case R.id.mPersonalBtn:
                        break;
                }
            }
        });
    }

    private void setUpContact() {
        contact = (Contact) getIntent().getSerializableExtra(ConstantValues.CONTACT_INFO);
        setUpTitle(contact.getUsername());
        userinfo = new Gson().fromJson(SPUtils.getString(this, ConstantValues.USER_INFO, ""), Info.class);
        pickChats();
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
                    mAudioRecordBtn.setVisibility(View.GONE);
                    mInputEt.setVisibility(View.VISIBLE);
                    mVoiceBtn.setSelected(false);
                }
            }
        });
    }

    private void pickChats() {
        chatList = new ArrayList<>();
        List<Chat> allChats = DataSupport.where("uid = ?", contact.getUid() + "").find(Chat.class);
        for (int i = 0; i < allChats.size(); i++) {
            Chat chat = allChats.get(i);
            byte[] message = chat.getMessage();
            int senderid = MessageUtils.getSenderid(message);
            int receiverid = MessageUtils.getReceiverid(message);
            if (senderid == userinfo.getResData().getId() || receiverid == userinfo.getResData().getId()) {
                chatList.add(chat);
            }
        }
    }

    private void initEmoView() {
        mEmoView.initializeData(new EmoNormalGrid.OnEmoClickListener() {
            @Override
            public void onDeleteButtonClick() {
                mInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                mInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            @Override
            public void onNormalEmoClick(String emo, int resId) {
//                emo = "[" + emo + "]";
                if (!emo.contains("smile")) {
                    return;
                }
                int i = emo.indexOf("_");
                int j = Integer.parseInt(emo.substring(i + 1));
                if (j >= EmoParser.emotions.length) {
                    return;
                }
                emo = EmoParser.emotions[j];
                SpannableStringBuilder parseEmo = EmoParser.parseEmo(ChatActivity.this, emo, 25);
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
    }

    private void initKeyboard() {
        keyboardChangeListener = new KeyboardChangeListener(this);
        keyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                //Log.d(TAG, "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
                keyboardIsShown = isShow;
                if (isShow && keyboardHeight > 0) {
                    SPUtils.saveInt(ChatActivity.this, ConstantValues.KEYBOARD_HEIGHT, keyboardHeight);
                }
            }
        });
    }

    private void initInputEditor() {
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
    }

    private void bindService() {
        Intent serviceIntent = new Intent(this, MQTTService.class);
        //startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollToBottom();
        MediaManager.resume();
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
                mVoiceBtn.setSelected(!mVoiceBtn.isSelected());
                ifShowAudioRecorderButton(mVoiceBtn.isSelected());
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
            default:
        }
    }

    private void ifShowAudioRecorderButton(boolean isSelected) {
        if (isSelected) {
            if (mMenuContainer.getVisibility() == View.VISIBLE) {
                hideMenuContainer(false);
                mMenuContainer.setVisibility(View.GONE);
            }
            KeyboardUtils.hideSoftInput(this);
            mInputEt.setVisibility(View.GONE);
            mAudioRecordBtn.setVisibility(View.VISIBLE);
        } else {
            KeyboardUtils.showSoftInput(this);
            mInputEt.setVisibility(View.VISIBLE);
            mAudioRecordBtn.setVisibility(View.GONE);
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
            int keyboardHeight = SPUtils.getInt(this, ConstantValues.KEYBOARD_HEIGHT, 1108);
            layoutParams.height = mEditorBar.getHeight() + keyboardHeight;
            mMenuContainer.setVisibility(View.VISIBLE);
            if (!isEmo) {
                mEditorMenuView.setVisibility(View.VISIBLE);
                mEmoView.setVisibility(View.GONE);
            } else {
                mEditorMenuView.setVisibility(View.GONE);
                mEmoView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sendMessage() {
//        String allKeys = SPUtils.getString(this, ConstantValues.KEYS, "");
//        String key = "";
//        if (allKeys.length() >= 16) {
//            key = allKeys.substring(0, 16);
//            String remainedKeys = allKeys.substring(16);
//            SPUtils.saveString(this, ConstantValues.KEYS, remainedKeys);
//            if (remainedKeys.length() < 10 * 16) {
//                HttpEngin.getInstance().post(UrlHelper.GET_KEY,
//                        new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID(), 50 * 16),
//                        KeyResultEntity.class, this);
//            }
//        }
        HttpEngin.getInstance().post(UrlHelper.GET_CODE,
                new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID()),
                KeyResultEntity.class, new HttpEngin.HttpListener<KeyResultEntity>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("获取密钥失败，请重试");
                    }

                    @Override
                    public void onResponse(KeyResultEntity resultEntity, int id) {
                        if (resultEntity.isFlag()) {
                            String keyId = resultEntity.getResData().getId();
                            String key = resultEntity.getResData().getValue();
                            byte[] encryptedMessage = MessageUtils.getEncryptMessage((char) 52, keyId, key.getBytes(), userinfo.getResData().getId(),
                                    contact.getUid(), (byte) CHAT_TYPE_MESSAGE, (byte) 0,
                                    (int) (System.currentTimeMillis() / 1000), mInputEt.getText().toString().getBytes());
                            myBinder.publishMessage(encryptedMessage, contact.getUid());
                            myBinder.savePlainMessage(MessageUtils.setMessageContent(encryptedMessage, mInputEt.getText().toString().getBytes()), contact.getUid());
                            mInputEt.setText("");
                            EventBus.getDefault().post(new MessageEvent());
                        }
                    }
                });

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
                receiveViewHolder.bindData(position, false);
            } else {
                SendViewHolder sendViewHolder = (SendViewHolder) holder;
                sendViewHolder.bindData(position, true);
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
        @Bind(R.id.mPictureView)
        ImageView mPictureView;
        @Bind(R.id.mAnimView)
        View mAnimView;
        @Bind(R.id.mTimeLengthView)
        TextView mTimeLengthView;
        @Bind(R.id.mMessageContainer)
        FrameLayout mMessageContainer;

        public BaseHolder(View itemView) {
            super(itemView);
            SupportMultipleScreensUtil.scale(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(int position, final boolean isSend) {
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
            final String path = new String(MessageUtils.getMessageContent(chat.getMessage()));
            //final String path = MessageUtils.getPath(chat.getMessage());
            ViewGroup.LayoutParams layoutParams = mMessageContainer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            switch (messageType) {
                case CHAT_TYPE_MESSAGE:
                    mAnimView.setVisibility(View.GONE);
                    mPictureView.setVisibility(View.GONE);
                    mChatTv.setVisibility(View.VISIBLE);
                    mTimeLengthView.setVisibility(View.GONE);
                    String messageContent = new String(MessageUtils.getMessageContent(chat.getMessage()));
                    mChatTv.setText(EmoParser.parseEmo(ChatActivity.this, messageContent, 25));
                    break;
                case CHAT_TYPE_VOICE:
                    mAnimView.setVisibility(View.VISIBLE);
                    mPictureView.setVisibility(View.GONE);
                    mChatTv.setVisibility(View.GONE);
                    mTimeLengthView.setVisibility(View.VISIBLE);
                    byte voiceLength = MessageUtils.getVoiceLength(chat.getMessage());

                    mTimeLengthView.setText(voiceLength + "\"");
                    int widthPixels = SupportMultipleScreensUtil.getWidthPixels(ChatActivity.this);
                    int minWidth = (int) (widthPixels * 0.15f);
                    int maxWidth = (int) (widthPixels * 0.45f);
                    layoutParams.width = (int) (minWidth + maxWidth * (voiceLength * 1.0f / 60));
                    mMessageContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mAnimViewLabel != null) {
                                boolean isSend = (boolean) mAnimViewLabel.getTag();
                                mAnimViewLabel.setBackgroundResource(isSend ? R.mipmap.mes_myvoiceplay3 : R.mipmap.mes_oppovoiceplay3);
                                //mAnimViewLabel.setBackgroundResource(getAnimBackground());
                                mAnimViewLabel = null;
                            }
                            mAnimViewLabel = mAnimView;
                            mAnimViewLabel.setTag(isSend);
                            mAnimViewLabel.setBackgroundResource(getAnimDrawable());
                            AnimationDrawable anim = (AnimationDrawable) mAnimViewLabel.getBackground();
                            anim.start();
                            MediaManager.playSound(path, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mAnimViewLabel.setBackgroundResource(getAnimBackground());
                                }
                            });
                        }
                    });

                    break;
                case CHAT_TYPE_PICTURE:
                    mAnimView.setVisibility(View.GONE);
                    mPictureView.setVisibility(View.VISIBLE);
                    mChatTv.setVisibility(View.GONE);
                    mTimeLengthView.setVisibility(View.GONE);
                    Glide.with(ChatActivity.this).load(path).into(mPictureView);
                    break;
            }

        }

        protected abstract int getAnimBackground();

        public abstract int getAnimDrawable();

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
        protected int getAnimBackground() {
            return R.mipmap.mes_oppovoiceplay3;
        }

        @Override
        public int getAnimDrawable() {
            return R.drawable.your_play_anim;
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
        protected int getAnimBackground() {
            return R.mipmap.mes_myvoiceplay3;
        }

        @Override
        public int getAnimDrawable() {
            return R.drawable.my_play_anim;
        }

        @Override
        protected void setPortrait() {
            mPortraitView.setPortrait(ChatActivity.this, userinfo.getResData().getLogo(), userinfo.getResData().getUsername());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideMenuView();
        List<LastChatTime> lastChatTimeList = DataSupport.where("myid = ? and yourid = ?", userinfo.getResData().getId() + "", contact.getUid() + "").find(LastChatTime.class);
        if (lastChatTimeList.size() == 0) {
            LastChatTime lastChatTime = new LastChatTime();
            lastChatTime.setMyid(userinfo.getResData().getId());
            lastChatTime.setYourid(contact.getUid());
            lastChatTime.setTimestamp(System.currentTimeMillis());
            lastChatTime.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("timestamp", System.currentTimeMillis());
            DataSupport.updateAll(LastChatTime.class, values, "myid = ? and yourid = ?", userinfo.getResData().getId() + "", contact.getUid() + "");
        }
        EventBus.getDefault().postSticky(new MessageEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        MediaManager.release();
        keyboardChangeListener.destroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMqttMessage(MessageEvent event) {
        //chatList = DataSupport.where("uid = ?", contact.getUid() + "").find(Chat.class);
        pickChats();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = "";
        if (requestCode == REQUEST_PHOTO && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if (images != null && images.size() > 0) {
                path = images.get(0);

            }
        } else if (requestCode == REQUEST_CAMERA && data != null) {
            path = data.getStringExtra(PICTURE_PATH);
        }

        String finalPath = path;
        HttpEngin.getInstance().post(UrlHelper.GET_CODE,
                new KeyRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID()),
                KeyResultEntity.class, new HttpEngin.HttpListener<KeyResultEntity>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("获取密钥失败，请重试");
                    }

                    @Override
                    public void onResponse(KeyResultEntity resultEntity, int id) {
                        if (resultEntity.isFlag()) {
                            String keyId = resultEntity.getResData().getId();
                            String key = resultEntity.getResData().getValue();
                            sendFile(null, finalPath, keyId, key);

                            String encryptFile = encryptFile(keyId, key, finalPath);
                            HttpEngin.getInstance().postFile(UrlHelper.UPLOAD_FILE + "?id=" + userinfo.getResData().getId() + "&deviceId=" + DeviceUtils.getAndroidID(),
                                    new File(encryptFile), FileUploadResultEntity.class, new HttpEngin.FileLoadListener<FileUploadResultEntity>() {
                                        @Override
                                        public void inProgress(float progress, long total, int id) {

                                        }

                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(FileUploadResultEntity entity, int id) {
                                            if (entity.isFlag()) {
                                                sendFile(entity.getResData(), null, keyId, key);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void sendFile(String yourPath, String myPath, String keyId, String key) {
        byte[] yourEncryptedMessage = null;
        byte[] myEncryptedMessage = null;
        if (yourPath != null) {
            yourEncryptedMessage = MessageUtils.getMessage((char) 52, keyId.getBytes(), userinfo.getResData().getId(),
                    contact.getUid(), (byte) CHAT_TYPE_PICTURE, (byte) 0,
                    (int) (System.currentTimeMillis() / 1000), yourPath.getBytes());
        }
        if (myPath != null) {
            myEncryptedMessage = MessageUtils.getMessage((char) 52, keyId.getBytes(), userinfo.getResData().getId(),
                    contact.getUid(), (byte) CHAT_TYPE_PICTURE, (byte) 0,
                    (int) (System.currentTimeMillis() / 1000), myPath.getBytes());
        }
        myBinder.publishFile(yourEncryptedMessage, contact.getUid(), myEncryptedMessage);
    }
}
