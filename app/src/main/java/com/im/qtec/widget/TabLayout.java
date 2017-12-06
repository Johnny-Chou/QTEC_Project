package com.im.qtec.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.im.qtec.R;
import com.im.qtec.core.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Stay on 27/3/16.
 * Powered by www.stay4it.com
 */
public class TabLayout extends LinearLayout implements View.OnClickListener {
    private ArrayList<Tab> tabs;
    private OnTabClickListener listener;
    private View selectView;
    private int tabCount;

    public TabLayout(Context context) {
        super(context);
        setUpView();
    }


    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpView();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView();
    }

    private void setUpView() {
        setOrientation(HORIZONTAL);

    }

    public void setUpData(ArrayList<Tab> tabs, OnTabClickListener listener) {
        this.tabs = tabs;
        this.listener = listener;

        if (tabs != null && tabs.size() > 0) {
            tabCount = tabs.size();
            TabView mTabView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1;
            for (int i = 0; i < tabs.size(); i++) {
                mTabView = new TabView(getContext());
                mTabView.setTag(tabs.get(i));
                mTabView.setUpData(tabs.get(i));
                mTabView.setOnClickListener(this);
                addView(mTabView, params);
            }
        } else {
            throw new IllegalArgumentException("tabs can't be empty");
        }
    }

    public void setCurrentTab(int i) {
        if (i < tabCount && i >= 0) {
            View view = getChildAt(i);
            onClick(view);
        }
    }

    public void onDataChanged(int i, int badgeCount) {
        if (i < tabCount && i >= 0) {
            TabView view = (TabView) getChildAt(i);
            view.onDataChanged(badgeCount);
        }
    }

    @Override
    public void onClick(View v) {
        if (selectView != v) {
            listener.onTabClick((Tab) v.getTag());
            v.setSelected(true);
            if (selectView != null) {
                selectView.setSelected(false);
            }
            selectView = v;
        }
    }

    public interface OnTabClickListener {
        void onTabClick(Tab tab);
    }

    public class TabView extends LinearLayout {
        private ImageView mTabImg;
        private TextView mTabLabel;
        private TextView mBadgeLabel;

        public TabView(Context context) {
            super(context);
            setUpView();
        }

        public TabView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setUpView();
        }

        public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setUpView();
        }


        private void setUpView() {
            LayoutInflater.from(getContext()).inflate(R.layout.widget_tab_view, this, true);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            mTabImg = findViewById(R.id.mTabImg);
            mTabLabel = findViewById(R.id.mTabLabel);
            mBadgeLabel = findViewById(R.id.mBadgeLabel);
        }

        public void setUpData(Tab tab) {
            mTabImg.setBackgroundResource(tab.imgResId);
            mTabLabel.setText(tab.labelResId);
        }


        public void onDataChanged(int badgeCount) {
            //  TODO notify new message, change the badgeView
            if (badgeCount == 0){
                mBadgeLabel.setVisibility(GONE);
            }else {
                mBadgeLabel.setVisibility(VISIBLE);
                mBadgeLabel.setText(String.format(getResources().getString(R.string.badge),badgeCount));
            }
        }
    }

    public static class Tab {
        public int imgResId;
        public int labelResId;
        public int badgeCount;
        public int menuResId;
        public Class<? extends BaseFragment> targetFragmentClz;

        public Tab(int imgResId, int labelResId) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
        }

        public Tab(int imgResId, int labelResId, int badgeCount) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.badgeCount = badgeCount;
        }

        public Tab(int imgResId, int labelResId, Class<? extends BaseFragment> targetFragmentClz) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.targetFragmentClz = targetFragmentClz;
        }

        public Tab(int imgResId, int labelResId, int menuResId, Class<? extends BaseFragment> targetFragmentClz) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.menuResId = menuResId;
            this.targetFragmentClz = targetFragmentClz;
        }
    }
}
