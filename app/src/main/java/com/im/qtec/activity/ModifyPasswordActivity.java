package com.im.qtec.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.UpdatePwdRequestEntity;
import com.im.qtec.entity.UpdatePwdResultEntity;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.UrlHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by zhouyanglei on 2018/1/17.
 */

public class ModifyPasswordActivity extends BaseActivity {
    @Bind(R.id.mOldPasswordEt)
    EditText mOldPasswordEt;
    @Bind(R.id.mNewPasswordEt)
    EditText mNewPasswordEt;
    @Bind(R.id.mConfirmPasswordEt)
    EditText mConfirmPasswordEt;
    private Info userinfo;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_modify_password, R.string.modify_password, R.menu.menu_complete, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        userinfo = new Gson().fromJson(SPUtils.getString(this, ConstantValues.USER_INFO,""),Info.class);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String oldPassword = mOldPasswordEt.getText().toString().trim();
        String newPassword = mNewPasswordEt.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)) {
            ToastUtils.showLong("原密码和新密码均不能为空");
            return true;
        }
        if (TextUtils.equals(newPassword,confirmPassword)){
            HttpEngin.getInstance().post(UrlHelper.UPDATE_PASSWORD,
                    new UpdatePwdRequestEntity(userinfo.getResData().getId(), DeviceUtils.getAndroidID(), EncryptUtils.encryptMD5ToString(oldPassword), EncryptUtils.encryptMD5ToString(newPassword))
                    , UpdatePwdResultEntity.class, new HttpEngin.HttpListener<UpdatePwdResultEntity>() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showLong("网络异常，请重试");
                        }

                        @Override
                        public void onResponse(UpdatePwdResultEntity updatePwdResultEntity, int id) {
                            if (updatePwdResultEntity.isFlag()){
                                ToastUtils.showLong("修改密码成功");
                                finish();
                            }else {
                                ToastUtils.showLong("修改密码失败，请重试");
                            }
                        }
                    });
        }else {
            ToastUtils.showLong("两次输入密码不一致，请重试");
        }

        return super.onMenuItemClick(item);
    }
}
