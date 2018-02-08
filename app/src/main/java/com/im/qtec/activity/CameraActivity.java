package com.im.qtec.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.im.qtec.R;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.utils.ImageUtil;

/**
 * Created by zhouyanglei on 2018/2/1.
 */

public class CameraActivity extends BaseActivity {
    private JCameraView mJCameraView;
    private static final int REQUEST_CAMERA = 200;
    private static final String PICTURE_PATH = "picture_path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setNeedScaleView(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_camera);
    }

    @Override
    protected void setUpView() {
        mJCameraView = findViewById(R.id.mCameraview);
        //mJCameraView.setTip();
        //设置视频保存路径
        //mJCameraView.setSaveVideoPath(getFilesDir().getAbsolutePath() + File.separator + "JCamera");

//设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mJCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        //JCameraView监听
        mJCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError() {
//没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        mJCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
//获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                String imagePath = ImageUtil.saveImageToGallery(CameraActivity.this, bitmap);
                Intent data = new Intent();
                data.putExtra(PICTURE_PATH,imagePath);
                setResult(REQUEST_CAMERA,data);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
//获取视频路径
                Log.i("CJT", "url = " + url);
            }
        });
//左边按钮点击事件
        mJCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
//右边按钮点击事件
        mJCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mJCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJCameraView.onPause();
    }
}
