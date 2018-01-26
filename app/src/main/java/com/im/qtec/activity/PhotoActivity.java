package com.im.qtec.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.entity.Info;
import com.im.qtec.entity.FileUploadResultEntity;
import com.im.qtec.utils.DividerGridItemDecoration;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by zhouyanglei on 2018/1/10.
 */

public class PhotoActivity extends BaseActivity{

    private RecyclerView mPhotoRv;
    private List<String> systemPhotoList;
    private List<String> selectedPhotoList = new ArrayList<>();
    private Button mSendPicBtn;
    private Info userInfo;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_photo, R.string.select_photo, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mPhotoRv = findViewById(R.id.mPhotoRv);
        mSendPicBtn = findViewById(R.id.mSendPicBtn);
        userInfo = new Gson().fromJson(SPUtils.getString(this, ConstantValues.USER_INFO, ""), Info.class);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        initPhotoList();
        final float[] uploadProgress = new float[selectedPhotoList.size()];
        final String url = UrlHelper.UPLOAD_FILE + "?id=" + userInfo.getResData().getId() + "&deviceId=" + DeviceUtils.getAndroidID();
        mSendPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhotoList.size() > 0){
                    for (int i = 0; i < selectedPhotoList.size(); i++) {
                        final int index = i;
                        HttpEngin.getInstance().postFile(url, new File(selectedPhotoList.get(i)), FileUploadResultEntity.class, new HttpEngin.FileLoadListener<FileUploadResultEntity>() {
                            @Override
                            public void inProgress(float progress, long total, int id) {
                                uploadProgress[index] = progress;
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showLong("第" + index + "张图片发送失败，请重试");
                            }

                            @Override
                            public void onResponse(FileUploadResultEntity entity, int id) {
                                if (entity.isFlag()){
                                    String picUrl = entity.getResData();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void initPhotoList() {
        systemPhotoList = getSystemPhotoList(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mPhotoRv.setLayoutManager(gridLayoutManager);
        mPhotoRv.setAdapter(new PhotoAdapter());
        mPhotoRv.addItemDecoration(new DividerGridItemDecoration(this));
    }

    public List<String> getSystemPhotoList(Context context) {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists()) {
                result.add(path);
            }
        }
        return result;
    }


    public class PhotoAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(PhotoActivity.this).inflate(R.layout.item_photo, parent, false);
            SupportMultipleScreensUtil.scale(view);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return systemPhotoList == null ? 0 : systemPhotoList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.mPhotoView)
        ImageView mPhotoView;
        @Bind(R.id.mSelectBtn)
        ImageButton mSelectBtn;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(int position) {
            final String photoPath = systemPhotoList.get(position);
            if (!photoPath.equals(mPhotoView.getTag(R.id.is_photo_tag))) {
                attachPhoto(photoPath);
            }
            mSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectBtn.isSelected()) {
                        mSelectBtn.setSelected(false);
                        selectedPhotoList.remove(photoPath);
                    } else {
                        mSelectBtn.setSelected(true);
                        selectedPhotoList.add(photoPath);
                    }
                    if (selectedPhotoList.size() == 0) {
                        mSendPicBtn.setText(R.string.send);
                    } else {
                        mSendPicBtn.setText("发送(" + selectedPhotoList.size() + ")");
                    }
                }
            });
        }

        private void attachPhoto(final String photoPath) {
            Glide.with(PhotoActivity.this).asBitmap()
                    //.thumbnail(Glide.with(this).asBitmap().load(R.mipmap.ic_launcher))
                    .load(photoPath)
                    //.apply(new RequestOptions().centerCrop().placeholder(new BitmapDrawable()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mPhotoView.setTag(R.id.is_photo_tag, photoPath);
                            mPhotoView.setImageBitmap(resource);
                        }
                    });
        }
    }
}
