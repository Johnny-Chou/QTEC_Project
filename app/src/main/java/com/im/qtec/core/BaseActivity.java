package com.im.qtec.core;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.im.qtec.R;
import com.im.qtec.activity.HomeActivity;
import com.im.qtec.activity.LoginActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.entity.CheckRequestEntity;
import com.im.qtec.entity.CheckResult;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.L;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;

import okhttp3.Call;


/**
 * Created by Stay on 2/2/16.
 * Powered by www.stay4it.com
 */
public abstract class BaseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar toolbar;
    protected TextView toolbar_title;
    public static final int MODE_BACK = 0;
    public static final int MODE_DRAWER = 1;
    public static final int MODE_NONE = 2;
    public static final int MODE_HOME = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (AppStatusTracker.getInstance().getAppStatus()) {
            case ConstantValues.STATUS_FORCE_KILLED:
                protectApp();
                break;
            case ConstantValues.STATUS_KICK_OUT:
                kickOut();
                break;
            case ConstantValues.STATUS_LOGOUT:
            case ConstantValues.STATUS_OFFLINE:
            case ConstantValues.STATUS_ONLINE:
                setUpContentView();
                setUpView();
                setUpData(savedInstanceState);
                break;
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.bar_green));
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(getApplication());
        SupportMultipleScreensUtil.scale(rootView);
    }

    protected abstract void setUpContentView();

    protected abstract void setUpView();

    protected abstract void setUpData(Bundle savedInstanceState);


    protected void protectApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_RESTART_APP);
        startActivity(intent);
    }

    protected void kickOut() {
//        TODO show dialog to confirm
        Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
        intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_KICK_OUT);
        startActivity(intent);
    }



    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, MODE_NONE);
    }

    public void setContentView(int layoutResID, int titleResId) {
        setContentView(layoutResID, titleResId, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, int titleResId, int mode) {
        setContentView(layoutResID, titleResId, -1, mode);
    }

    public void setContentView(int layoutResID, int titleResId, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleResId, menuId, mode);
    }

    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        if (mode != MODE_NONE) {
            toolbar = findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            toolbar.setTitle("");
            toolbar_title = findViewById(R.id.toolbar_title);
            if (mode == MODE_BACK) {
                //todo
                toolbar.setNavigationIcon(R.mipmap.left_arrow);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpTitle(titleResId);
            setUpMenu(menuId);
        }
    }

    protected void setUpMenu(int menuId) {
        if (toolbar != null) {
            toolbar.getMenu().clear();
            if (menuId > 0) {
                toolbar.inflateMenu(menuId);
                toolbar.setOnMenuItemClickListener(this);
            }
        }
    }

    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && toolbar_title != null) {
            toolbar_title.setText(titleResId);
        }
    }

    protected void setUpTitle(String title) {
        if (title != null && toolbar_title != null) {
            toolbar_title.setText(title);
        }
    }

    protected void onNavigationBtnClicked() {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onStart() {
        if (AppStatusTracker.getInstance().checkIfShowGesture()) {
            L.d("need to show gesture");
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkKickout();
    }

    protected void checkKickout() {
        HttpEngin.getInstance().post(UrlHelper.CHECK_LOGIN_URL,
                new CheckRequestEntity(SPUtils.getInt(this, ConstantValues.USER_ID, -1), DeviceUtils.getAndroidID()),
                CheckResult.class, new HttpEngin.HttpListener<CheckResult>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("网络发生异常，请检查网络");
                    }

                    @Override
                    public void onResponse(CheckResult checkResult, int id) {
                        if (!checkResult.isFlag()) {
                            AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_KICK_OUT);
                            kickOut();
                        }
                    }
                });
    }

}
