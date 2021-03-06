package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.core.BaseFragment;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.KeyRequestEntity;
import com.im.qtec.entity.KeyResultEntity;
import com.im.qtec.event.MessageEvent;
import com.im.qtec.event.UnreadnumberEvent;
import com.im.qtec.fragment.CallFragment;
import com.im.qtec.fragment.ContactsFragment;
import com.im.qtec.fragment.ConversationFragment;
import com.im.qtec.fragment.MineFragment;
import com.im.qtec.service.MQTTService;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.UrlHelper;
import com.im.qtec.widget.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by zhouyanglei on 2017/11/24.
 */

public class HomeActivity extends BaseActivity implements TabLayout.OnTabClickListener, HttpEngin.HttpListener<KeyResultEntity> {
    private TabLayout mTabLayout;
    private BaseFragment fragment;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_home,-1,MODE_HOME);
    }

    @Override
    protected void setUpView() {
        mTabLayout = findViewById(R.id.mTabLayout);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        startMqttService();
        initTabs();
        EventBus.getDefault().register(this);
    }

    private void startMqttService() {
        if (!ServiceUtils.isServiceRunning(MQTTService.class.getName())){
            Intent serviceIntent = new Intent(this, MQTTService.class);
            startService(serviceIntent);
        }
    }

    private void initTabs() {
        ArrayList<TabLayout.Tab> tabs = new ArrayList<>();
        tabs.add(new TabLayout.Tab(R.drawable.selector_message,R.string.message,R.menu.menu_conversation,ConversationFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_contacts,R.string.contacts,-1,ContactsFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_call,R.string.call,-1,CallFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_mine,R.string.mine,-1,MineFragment.class));
        mTabLayout.setUpData(tabs,this);
        mTabLayout.setCurrentTab(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_ONLINE);
    }

    @Override
    public void onTabClick(TabLayout.Tab tab) {
        try {
            setUpTitle(tab.labelResId);
            fragment = tab.targetFragmentClz.newInstance();
            setUpMenu(tab.menuResId);
            getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayoutContainer, fragment).commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_BACK_TO_HOME);
        switch (action) {
            case ConstantValues.ACTION_KICK_OUT:
                reLogin();
                break;
            case ConstantValues.ACTION_LOGOUT:
                logout();
                break;
            case ConstantValues.ACTION_RESTART_APP:
                protectApp();
                break;
            case ConstantValues.ACTION_BACK_TO_HOME:
                break;
        }
    }

    private void logout() {
        AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_LOGOUT);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void reLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_KICK_OUT);
        AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_OFFLINE);
        startActivity(intent);
        finish();
    }


    @Override
    protected void protectApp() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        ToastUtils.showLong("获取密钥失败");
    }

    @Override
    public void onResponse(KeyResultEntity result, int id) {
        if (result.isFlag()){
            String value = result.getResData().getValue();
            String remainedKeys = SPUtils.getString(this, ConstantValues.KEYS, "");
            SPUtils.saveString(this, ConstantValues.KEYS,remainedKeys + value);
        }
    }

    @Override
    protected void kickOut() {
        reLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getUnreadnumber(UnreadnumberEvent event) {
        mTabLayout.onDataChanged(0,event.unreadnumber);
    }
}
