package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.blankj.utilcode.util.DeviceUtils;
import com.im.qtec.R;
import com.im.qtec.autolayout.AutoLayoutActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.entity.CheckEntity;
import com.im.qtec.entity.CheckResult;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.L;
import com.im.qtec.utils.SPUtils;
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

public class WelcomeActivity extends AutoLayoutActivity implements HttpEngin.HttpListener<CheckResult> {
    private ImageView mIvSlogan;
    private boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_welcome);

        mIvSlogan = findViewById(R.id.mIvSlogan);

    }

    @Override
    protected void onResume() {
        super.onResume();
        welcomeAnim();
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
                        L.d(System.currentTimeMillis() + "");
                        mIvSlogan.startAnimation(startAnimation);
                    }

                    @Override
                    public void onCompleted() {
                        if (TextUtils.isEmpty(SPUtils.getString(WelcomeActivity.this, ConstantValues.USER_ID, ""))) {
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_OFFLINE);
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
        HttpEngin<CheckResult> httpEngin = HttpEngin.getInstance();
        httpEngin.post(UrlHelper.CHECK_LOGIN_URL,
                new CheckEntity(SPUtils.getString(this, ConstantValues.USER_ID, ""), DeviceUtils.getAndroidID()),
                CheckResult.class,this);
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
