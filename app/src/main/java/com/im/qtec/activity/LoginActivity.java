package com.im.qtec.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.KeyRequestEntity;
import com.im.qtec.entity.User;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.KeyboardChangeListener;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.UrlHelper;

import okhttp3.Call;

/**
 * @author zhouyanglei
 * @date 2017/11/28
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, HttpEngin.HttpListener<Info> {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtLogin;
    private ProgressBar mProgressBar;
    private String username;
    private String password;
    private boolean hasSaved = false;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setUpView() {
        mEtUsername = findViewById(R.id.mEtUsername);
        mEtPassword = findViewById(R.id.mEtPassword);
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!hasSaved) {
                    Rect r = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                    int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                    Resources resources=getResources();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    screenHeight = displayMetrics.heightPixels;
                    int softInputHeight = screenHeight - r.bottom;
                    SPUtils.saveInt(LoginActivity.this, ConstantValues.KEYBOARD_HEIGHT, softInputHeight);
                    hasSaved = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtLogin = findViewById(R.id.mBtLogin);
        mProgressBar = findViewById(R.id.mProgressBar);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        mBtLogin.setOnClickListener(this);
        int kickOut = getIntent().getIntExtra(ConstantValues.KEY_HOME_ACTION, -1);
        if (kickOut == ConstantValues.ACTION_KICK_OUT) {
            new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("登陆失效，请重新登陆")
                    .setPositiveButton("确定", null).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BarUtils.setStatusBarAlpha(this, 0);
    }

    @Override
    public void onClick(View view) {
        username = mEtUsername.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showLong("账号或密码不能为空");
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            HttpEngin.getInstance().post(UrlHelper.LOGIN_URL,
                    new User(username, EncryptUtils.encryptMD5ToString(password), DeviceUtils.getAndroidID()),
                    Info.class, this);

        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("main", e.toString());
        mProgressBar.setVisibility(View.INVISIBLE);
        ToastUtils.showLong("网络出现异常，请重试");
    }

    @Override
    protected void kickOut() {

    }

    @Override
    protected void checkKickout() {

    }

    @Override
    public void onResponse(Info userInfo, int id) {
        //Info userInfo = new Gson().fromJson(response, Info.class);
        if (userInfo.isFlag()) {
            SPUtils.saveString(LoginActivity.this, ConstantValues.USER_ACCOUNT, username);
            SPUtils.saveString(LoginActivity.this, ConstantValues.USER_PASSWORD, password);
            SPUtils.saveInt(LoginActivity.this, ConstantValues.USER_ID, userInfo.getResData().getId());
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //intent.putExtra(ConstantValues.USER_INFO, userInfo);
            SPUtils.saveString(this, ConstantValues.USER_INFO, new Gson().toJson(userInfo));
            startActivity(intent);
            finish();
        } else {
            ToastUtils.showLong("登陆失败，请重试");
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
