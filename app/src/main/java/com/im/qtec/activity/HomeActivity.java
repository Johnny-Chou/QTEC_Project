package com.im.qtec.activity;

import android.os.Bundle;

import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.AppStatusTracker;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.fragment.MessageFragment;
import com.im.qtec.widget.TabLayout;

import java.util.ArrayList;

/**
 * Created by zhouyanglei on 2017/11/24.
 */

public class HomeActivity extends BaseActivity implements TabLayout.OnTabClickListener {
    private TabLayout mTabLayout;
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
        ArrayList<TabLayout.Tab> tabs = new ArrayList<>();
        tabs.add(new TabLayout.Tab(R.drawable.selector_message,R.string.message,MessageFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_contacts,R.string.contacts,MessageFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_call,R.string.call,MessageFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_mine,R.string.mine,MessageFragment.class));
        mTabLayout.setUpData(tabs,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStatusTracker.getInstance().setAppStatus(ConstantValues.STATUS_ONLINE);
    }

    @Override
    public void onTabClick(TabLayout.Tab tab) {

    }
}
