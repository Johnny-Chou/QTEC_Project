package com.im.qtec.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.im.qtec.R;
import com.im.qtec.fragment.MineFragment;
import com.im.qtec.utils.SupportMultipleScreensUtil;

/**
 * Created by zhouyanglei on 2017/12/13.
 */

public class PortraitView extends android.support.v7.widget.AppCompatImageView {
    public Paint mPaint;
    private int measuredHeight;
    private int measuredWidth;

    public void setName(String name) {
        if (!TextUtils.isEmpty(name) && name.length() >= 3){
            this.name = name.substring(name.length() - 2);
        }else {
            this.name = name;
        }
        postInvalidate();
    }

    private String name;

    public PortraitView(Context context) {
        super(context);
        init();
    }

    public PortraitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PortraitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//开启抗锯齿
        mPaint.setTextSize(SupportMultipleScreensUtil.getScaleValue(12));
        //设置文字被绘制的起点为底边的中心
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(name)  && getDrawable() == null) {
            int radius = measuredWidth < measuredHeight ? measuredWidth / 2 : measuredHeight / 2;
            int bgColor = getColor(name);
            mPaint.setColor(bgColor);
            canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, radius, mPaint);
            mPaint.setColor(getResources().getColor(R.color.tab_green));
            mPaint.setTextSize(SupportMultipleScreensUtil.getScaleValue(12));
            Rect bounds = new Rect();
            mPaint.getTextBounds(name, 0, name.length(), bounds);//代码执行完毕，bounds就有值了
            int textHeight = bounds.height();
            //int textWidth = bounds.width();
            canvas.drawText(name, measuredWidth / 2, measuredHeight / 2 + textHeight / 2, mPaint);
        }
    }

    private int getColor(String string) {
        String MD5 = EncryptUtils.encryptMD5ToString(string);
        byte[] bytes = ConvertUtils.hexString2Bytes(MD5);
        return Color.argb(bytes[0],bytes[1],bytes[2],bytes[3]);
    }

    public void setPortrait(Context context, final String url, final String username) {
        Glide.with(context).asBitmap()
                //.thumbnail(Glide.with(this).asBitmap().load(R.mipmap.ic_launcher))
                .load(url)
                //.apply(new RequestOptions().centerCrop().placeholder(new BitmapDrawable()))
                .into(new BitmapImageViewTarget(this) {
                    @Override
                    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        PortraitView.this.setTag(R.id.is_normal_tag,url);
                        setImageBitmap(resource);
                    }

                    @Override
                    protected void setResource(Bitmap resource) {
                        if (resource != null) {
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            if (roundedBitmapDrawable != null) {
                                roundedBitmapDrawable.setCircular(true);
                                setImageDrawable(roundedBitmapDrawable);
                            }else {
                                setName(username);
                            }
                        }else {
                            setName(username);
                        }
                    }
                });
    }

}
