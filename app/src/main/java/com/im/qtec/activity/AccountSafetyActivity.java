package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.im.qtec.R;
import com.im.qtec.core.BaseActivity;

/**
 * Created by zhouyanglei on 2018/1/17.
 */

public class AccountSafetyActivity extends BaseActivity{

    private RelativeLayout mModifyPasswordItem;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_accountsafety,R.string.account_safe,MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mModifyPasswordItem = findViewById(R.id.mModifyPasswordItem);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        mModifyPasswordItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSafetyActivity.this, ModifyPasswordActivity.class));
            }
        });
    }
}
