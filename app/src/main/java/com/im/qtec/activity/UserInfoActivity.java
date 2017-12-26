package com.im.qtec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.im.qtec.R;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseActivity;
import com.im.qtec.db.Contact;
import com.im.qtec.widget.PortraitView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhouyanglei
 * @date 2017/12/13
 */

public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.mPortraitView)
    PortraitView mPortraitView;
    @Bind(R.id.mTvName)
    TextView mTvName;
    @Bind(R.id.mIvSex)
    ImageView mIvSex;
    @Bind(R.id.mMinumberTv)
    TextView mMinumberTv;
    @Bind(R.id.mFriendNameTv)
    TextView mFriendNameTv;
    @Bind(R.id.mFriendPhoneTv)
    TextView mFriendPhoneTv;
    @Bind(R.id.mFriendEmailTv)
    TextView mFriendEmailTv;
    @Bind(R.id.mFriendDepartmentTv)
    TextView mFriendDepartmentTv;
    @Bind(R.id.mFriendJobTv)
    TextView mFriendJobTv;
    @Bind(R.id.mSendBtn)
    Button mSendBtn;
    @Bind(R.id.mCallBtn)
    Button mCallBtn;
    private Contact contact;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_userinfo, R.string.userinfo, MODE_BACK);
        //enableBtn();
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        contact = (Contact) getIntent().getSerializableExtra(ConstantValues.CONTACT_INFO);
        if (TextUtils.isEmpty(contact.getLogo())) {
            mPortraitView.setName(contact.getUsername());
        } else {
            mPortraitView.setPortrait(this, contact.getLogo(), contact.getUsername());
        }
        mTvName.setText(contact.getUsername());
        mMinumberTv.append(contact.getUid() + "");
        mIvSex.setImageResource(contact.getSex() == 1 ? R.mipmap.book_men : R.mipmap.book_women);
        mFriendNameTv.setText(contact.getUsername());
        mFriendPhoneTv.setText(contact.getMobilephone());
        mFriendEmailTv.setText(contact.getEmail());
        mFriendDepartmentTv.setText(contact.getApartment());
        mFriendJobTv.setText(contact.getLevel());
//        toolbar_btn.setOnClickListener(this);
        setUpMenu(R.menu.menu_friend_detail);
    }


    @OnClick({R.id.mSendBtn, R.id.mCallBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSendBtn:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(ConstantValues.CONTACT_INFO, contact);
                startActivity(intent);
                break;
            case R.id.mCallBtn:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail:
                ToastUtils.showLong("详情");
                break;
        }
        return false;
    }
}
