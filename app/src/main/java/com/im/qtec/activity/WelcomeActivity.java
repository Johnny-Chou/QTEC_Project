package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.blankj.utilcode.util.DeviceUtils;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.entity.CheckRequestEntity;
import com.im.qtec.entity.CheckResult;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author zhouyanglei
 * @date 2017/11/27
 */

public class WelcomeActivity extends BaseActivity implements HttpEngin.HttpListener<CheckResult> {
    private ImageView mIvSlogan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_OFFLINE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void setUpView() {
        mIvSlogan = findViewById(R.id.mIvSlogan);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        welcomeAnim();
        super.onResume();
    }

    private void welcomeAnim() {
        final AlphaAnimation startAnimation = new AlphaAnimation(0, 1);
        final AlphaAnimation endAnimation = new AlphaAnimation(1, 0);
        startAnimation.setFillAfter(true);
        endAnimation.setFillAfter(true);
        startAnimation.setDuration(1000);
        endAnimation.setDuration(1000);
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mIvSlogan.startAnimation(startAnimation);
                    }

                    @Override
                    public void onCompleted() {
                        if (SPUtils.getInt(WelcomeActivity.this, ConstantValues.USER_ID, -1) == -1) {
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            checkLogin();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1) {
                            mIvSlogan.startAnimation(endAnimation);
                        }
                    }
                });
    }

    private void goHome() {
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
    }

    private void checkLogin() {
       HttpEngin.getInstance().post(UrlHelper.CHECK_LOGIN_URL,
                new CheckRequestEntity(SPUtils.getInt(this, ConstantValues.USER_ID, -1), DeviceUtils.getAndroidID()),
                CheckResult.class,this);
    }

    @Override
    protected void checkKickout() {

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        goHome();
        finish();
    }

    @Override
    public void onResponse(CheckResult checkResult, int id) {
        if (checkResult.isFlag()){
            goHome();
        }else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
        finish();
    }
}
