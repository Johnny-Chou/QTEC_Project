package com.im.qtec.activity;

import android.os.Bundle;

import com.im.qtec.R;
import com.im.qtec.core.BaseActivity;

/**
 * Created by zhouyanglei on 2018/1/18.
 */

public class AboutActivity extends BaseActivity{
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_about,R.string.about_help,MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {

    }
}
