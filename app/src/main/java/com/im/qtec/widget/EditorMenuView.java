package com.im.qtec.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.im.qtec.R;

/**
 * Created by zhouyanglei on 2018/1/5.
 */

public class EditorMenuView extends LinearLayout implements View.OnClickListener {
    public EditorMenuView(Context context) {
        super(context);
        initView();
    }

    public EditorMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EditorMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.pop_menu,this,true);
        setOrientation(VERTICAL);
        ImageButton mPhotoBtn = findViewById(R.id.mPhotoBtn);
        ImageButton mCameraBtn = findViewById(R.id.mCameraBtn);
        ImageButton mCallBtn = findViewById(R.id.mCallBtn);
        ImageButton mLocationBtn = findViewById(R.id.mLocationBtn);
        ImageButton mPersonalBtn = findViewById(R.id.mPersonalBtn);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        mCallBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
        mPersonalBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v.getId());
        }
    }

    public void setListener(EditorMenuListener listener) {
        this.listener = listener;
    }

    private EditorMenuListener listener;


    public interface EditorMenuListener{
        void onClick(int id);
    }
}
