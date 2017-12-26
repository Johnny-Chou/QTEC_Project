package com.im.qtec.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.im.qtec.R;
import com.im.qtec.activity.SettingActivity;
import com.im.qtec.activity.UserInfoActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseFragment;
import com.im.qtec.entity.Info;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;
import com.im.qtec.widget.PortraitView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouyanglei on 2017/12/7.
 */

public class MineFragment extends BaseFragment {
    private Info userInfo;
    @Bind(R.id.mUserInfoItem)
    RelativeLayout mUserInfoItem;
    @Bind(R.id.mTvName)
    TextView mTvName;
    @Bind(R.id.mTvCompany)
    TextView mTvCompany;
    @Bind(R.id.mTvDepartment)
    TextView mTvDepartment;
    @Bind(R.id.mTvLevel)
    TextView mTvLevel;
    @Bind(R.id.mIvPortrait)
    PortraitView mIvPortrait;
    @Bind(R.id.mIvKey)
    ImageView mIvKey;
    @Bind(R.id.mRlKeyManager)
    RelativeLayout mRlKeyManager;
    @Bind(R.id.mIvSetting)
    ImageView mIvSetting;
    @Bind(R.id.mRlSetting)
    RelativeLayout mRlSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        SupportMultipleScreensUtil.scale(view);
        return view;
    }

    @Override
    public void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setUpData() {
        userInfo = new Gson().fromJson(SPUtils.getString(getActivity(), ConstantValues.USER_INFO, ""), Info.class);
        final Info.ResDataBean userInfoResData = userInfo.getResData();
        mTvName.setText(userInfoResData.getUsername());
        mTvCompany.setText(userInfoResData.getCompany());
        mTvDepartment.setText(userInfoResData.getApartment());
        mTvLevel.setText(userInfoResData.getLevel());
        mIvPortrait.setPortrait(getActivity(), userInfoResData.getLogo(), userInfoResData.getUsername());
    }

    @Override
    public void onResume() {
        super.onResume();
        //HttpEngin.getInstance().post(UrlHelper.INFO_DETAIL,);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.mUserInfoItem,R.id.mIvPortrait, R.id.mRlKeyManager, R.id.mRlSetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mUserInfoItem:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.mIvPortrait:
                break;
            case R.id.mRlKeyManager:
                break;
            case R.id.mRlSetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}
