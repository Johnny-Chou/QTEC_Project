package com.im.qtec.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.im.qtec.R;


/**
 * @author zhouyanglei
 */
public class EmoView extends LinearLayout implements OnPageChangeListener {

    private ViewPager mEmoViewPager;
    private EmoDotView mEmoDotView;
    private EmoPagerAdapter adapter;
    private EmoNormalGrid.OnEmoClickListener listener;
    private Context context;
    private int groupIndex;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EmoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public EmoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public EmoView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_emo_container, this);
        mEmoDotView = findViewById(R.id.mEmoDotView);
        mEmoViewPager = findViewById(R.id.mEmoViewPager);
        adapter = new EmoPagerAdapter();
        mEmoViewPager.setAdapter(adapter);
        mEmoViewPager.setOnPageChangeListener(this);
    }

    public void initializeData(EmoNormalGrid.OnEmoClickListener listener) {
        this.listener = listener;
        onPageSelected(0);
    }

    class EmoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//			the count should be sum(group pages)
            return 5;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            EmoNormalGrid normal = new EmoNormalGrid(context);
            normal.initializeData(position, listener);
            container.addView(normal);
            return normal;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int pageIndex) {
        mEmoDotView.notifyDataChanged(pageIndex, 5);
    }

}
