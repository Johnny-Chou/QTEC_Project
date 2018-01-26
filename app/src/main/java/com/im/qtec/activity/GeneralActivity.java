package com.im.qtec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.im.qtec.R;
import com.im.qtec.core.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouyanglei on 2018/1/18.
 */

public class GeneralActivity extends BaseActivity {
    @Bind(R.id.mIfReadToggle)
    ImageView mIfReadToggle;
    @Bind(R.id.mShowIfReadItem)
    RelativeLayout mShowIfReadItem;
    @Bind(R.id.mStorageSpaceItem)
    RelativeLayout mStorageSpaceItem;
    @Bind(R.id.mClearRecordBtn)
    TextView mClearRecordBtn;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_general, R.string.general, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {

    }


    @OnClick({R.id.mShowIfReadItem, R.id.mStorageSpaceItem, R.id.mClearRecordBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mShowIfReadItem:
                mIfReadToggle.setSelected(!mIfReadToggle.isSelected());
                break;
            case R.id.mStorageSpaceItem:
                break;
            case R.id.mClearRecordBtn:
                break;
        }
    }
}
