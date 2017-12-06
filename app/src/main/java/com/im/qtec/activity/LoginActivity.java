package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.im.qtec.R;
import com.im.qtec.autolayout.AutoLayoutActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.User;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.ToastUtil;
import com.im.qtec.utils.UrlHelper;

import okhttp3.Call;

/**
 * @author zhouyanglei
 * @date 2017/11/28
 */

public class LoginActivity extends AutoLayoutActivity implements View.OnClickListener, HttpEngin.HttpListener<Info> {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtLogin;
    private ProgressBar mProgressBar;
    private String username;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEtUsername = findViewById(R.id.mEtUsername);
        mEtPassword = findViewById(R.id.mEtPassword);
        mBtLogin = findViewById(R.id.mBtLogin);
        mProgressBar = findViewById(R.id.mProgressBar);

        mBtLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        username = mEtUsername.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtil.show(this, "账号或密码不能为空");
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            HttpEngin<Info> httpEngin = HttpEngin.getInstance();
            httpEngin.post(UrlHelper.LOGIN_URL,
                    new User(username, EncryptUtils.encryptMD5ToString(password), DeviceUtils.getAndroidID()),
                    Info.class,this);

        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("main", e.toString());
        mProgressBar.setVisibility(View.INVISIBLE);
        ToastUtil.show(LoginActivity.this, "网络出现异常，请重试");
    }


    @Override
    public void onResponse(Info userInfo, int id) {
        //Info userInfo = new Gson().fromJson(response, Info.class);
        if (userInfo.isFlag()) {
            SPUtils.saveString(LoginActivity.this, ConstantValues.USER_ACCOUNT, username);
            SPUtils.saveString(LoginActivity.this, ConstantValues.USER_PASSWORD, password);
            SPUtils.saveString(LoginActivity.this, ConstantValues.USER_ID, userInfo.getResData().getId() + "");
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(ConstantValues.USER_INFO, userInfo);
            startActivity(intent);
            finish();
        } else {
            ToastUtil.show(LoginActivity.this, "登陆失败，请重试");
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
