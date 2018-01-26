package com.im.qtec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.im.qtec.R;
import com.im.qtec.core.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouyanglei on 2018/1/18.
 */

public class NewMessageActivity extends BaseActivity {
    @Bind(R.id.mReceiveMessageToggle)
    ImageView mReceiveMessageToggle;
    @Bind(R.id.mReceiveMessageNotificationItem)
    RelativeLayout mReceiveMessageNotificationItem;
    @Bind(R.id.mReceiveCallToggle)
    ImageView mReceiveCallToggle;
    @Bind(R.id.mReceiveCallNotificationItem)
    RelativeLayout mReceiveCallNotificationItem;
    @Bind(R.id.mRingbellToggle)
    ImageView mRingbellToggle;
    @Bind(R.id.mCallRingbellItem)
    RelativeLayout mCallRingbellItem;
    @Bind(R.id.mShowDetailToggle)
    ImageView mShowDetailToggle;
    @Bind(R.id.mShowNotificationDetailItem)
    RelativeLayout mShowNotificationDetailItem;
    @Bind(R.id.mVoiceToggle)
    ImageView mVoiceToggle;
    @Bind(R.id.mVoiceItem)
    RelativeLayout mVoiceItem;
    @Bind(R.id.mVibrationToggle)
    ImageView mVibrationToggle;
    @Bind(R.id.mVibrationItem)
    RelativeLayout mVibrationItem;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_newmessage, R.string.new_message, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {

    }


    @OnClick({R.id.mReceiveMessageNotificationItem, R.id.mReceiveCallNotificationItem, R.id.mCallRingbellItem, R.id.mShowNotificationDetailItem, R.id.mVoiceItem, R.id.mVibrationItem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mReceiveMessageNotificationItem:
                mReceiveMessageToggle.setSelected(!mReceiveMessageToggle.isSelected());
                break;
            case R.id.mReceiveCallNotificationItem:
                mReceiveCallToggle.setSelected(!mReceiveCallToggle.isSelected());
                break;
            case R.id.mCallRingbellItem:
                mRingbellToggle.setSelected(!mRingbellToggle.isSelected());
                break;
            case R.id.mShowNotificationDetailItem:
                mShowDetailToggle.setSelected(!mShowDetailToggle.isSelected());
                break;
            case R.id.mVoiceItem:
                mVoiceToggle.setSelected(!mVoiceToggle.isSelected());
                break;
            case R.id.mVibrationItem:
                mVibrationToggle.setSelected(!mVibrationToggle.isSelected());
                break;
        }
    }
}
