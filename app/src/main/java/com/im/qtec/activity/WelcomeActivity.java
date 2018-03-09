package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.im.qtec.utils.UrlHelper;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qtec.qkcl.crypto.enums.EEncrypAlg;
import cn.qtec.qkcl.envelope.EnvelopEnc;
import cn.qtec.qkcl.envelope.EnvelopEncInterface;
import cn.qtec.qkcl.envelope.future.StopableFuture;
import cn.qtec.qkcl.envelope.pojo.QtKey;
import cn.qtec.qkcl.envelope.pojo.QtKeyId;
import cn.qtec.qkcl.envelope.pojo.QtRecipientInfo;
import cn.qtec.qkcl.envelope.pojo.ResultInfo;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


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
        new Thread(){
            @Override
            public void run() {
                super.run();
                haha();
            }
        }.start();
    }

    private void haha() {
        decryptFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/qtec/1encrypt.amr",Environment.getExternalStorageDirectory().getAbsolutePath()+"/qtec/1decrypt.amr",
                "111111111111111111111111111111111111111111111","111111111111111111111111111111111111111111111");
    }

    private void decryptFile(String path, String decryptPath, String keyId, String key) {
        EnvelopEncInterface envelopEnc = EnvelopEnc.getInstance();
        //模拟创建接收者信息
        List<QtRecipientInfo> list = new ArrayList<>();
        QtRecipientInfo qtRecipientInfo = new QtRecipientInfo();
        //设置接收者的算法
        qtRecipientInfo.seteEncrypAlg(EEncrypAlg.AES256_CBC_PKCS5PADDING);
        //模拟生成一个接收者的共享密钥
        QtKey qtKey = new QtKey();
        //模拟生成一个接收者的密钥id
        QtKeyId qtKeyId = new QtKeyId();
        for (int i = 0; i < qtKeyId.getBuffer().length; i++) {
            qtKeyId.getBuffer()[i] = keyId.getBytes()[i];
        }
        for (int i = 0; i < qtKey.getBuffer().length; i++) {
            qtKey.getBuffer()[i] = key.getBytes()[i];
        }

        qtRecipientInfo.setKeyId(qtKeyId);
        qtRecipientInfo.setKey(qtKey);
        list.add(qtRecipientInfo);

        try {
            StopableFuture future = envelopEnc.decryptFile(path, qtKeyId, qtKey, decryptPath);
            float processPercent = future.getProcessPercent();
            Thread.sleep(1000);
            processPercent = future.getProcessPercent();
            future.get();
            File file = new File(decryptPath);
            boolean exists = file.exists();
            System.out.println("a");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter){
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mIvSlogan.startAnimation(startAnimation);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1) {
                            mIvSlogan.startAnimation(endAnimation);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (SPUtils.getInt(WelcomeActivity.this, ConstantValues.USER_ID, -1) == -1) {
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            checkLogin();
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
