package com.im.qtec.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.LogoutRequestEntity;
import com.im.qtec.entity.LogoutResultEntity;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.UrlHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author zhouyanglei
 * @date 2017/12/13
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.mAccountSafeItem)
    RelativeLayout mAccountSafeItem;
    @Bind(R.id.mNewMessageItem)
    RelativeLayout mNewMessageItem;
    @Bind(R.id.mGeneralItem)
    RelativeLayout mGeneralItem;
    @Bind(R.id.mAboutAndHelpItem)
    RelativeLayout mAboutAndHelpItem;
    @Bind(R.id.mLogoutBtn)
    TextView mLogoutBtn;
    private Info userinfo;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_setting, R.string.setting, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        userinfo = new Gson().fromJson(SPUtils.getString(this, ConstantValues.USER_INFO, ""), Info.class);
    }

    @OnClick({R.id.mAccountSafeItem, R.id.mNewMessageItem, R.id.mGeneralItem, R.id.mAboutAndHelpItem, R.id.mLogoutBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mAccountSafeItem:
                startActivity(new Intent(this, AccountSafetyActivity.class));
                break;
            case R.id.mNewMessageItem:
                startActivity(new Intent(this, NewMessageActivity.class));
                break;
            case R.id.mGeneralItem:
                startActivity(new Intent(this, GeneralActivity.class));
                break;
            case R.id.mAboutAndHelpItem:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.mLogoutBtn:
                showLogout();
                break;
        }
    }

    private void showLogout() {
        new AlertDialog.Builder(this)
                .setMessage("确定要注销账号吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void logout() {
        HttpEngin.getInstance().post(UrlHelper.LOGOUT_URL,
                new LogoutRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID())
                , LogoutResultEntity.class, new HttpEngin.HttpListener<LogoutResultEntity>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("网络异常，请重试");
                    }

                    @Override
                    public void onResponse(LogoutResultEntity entity, int id) {
                        boolean flag = entity.isFlag();
                        if (flag){
                            Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                            intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_LOGOUT);
                            startActivity(intent);
                            finish();
                        }
                    }

                });
    }
}
