package com.im.qtec.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.im.qtec.R;
import com.im.qtec.activity.UserInfoActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseFragment;
import com.im.qtec.db.Contact;
import com.im.qtec.entity.ContactsRequestEntity;
import com.im.qtec.entity.ContactsResultEntity;
import com.im.qtec.utils.HttpEngin;
import com.im.qtec.utils.PinYinUtil;
import com.im.qtec.utils.RecycleViewDivider;
import com.im.qtec.utils.SPUtils;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.utils.UrlHelper;
import com.im.qtec.widget.PortraitView;
import com.im.qtec.widget.QuickIndexBar;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhouyanglei on 2017/12/7.
 */

public class ContactsFragment extends BaseFragment implements HttpEngin.HttpListener<ContactsResultEntity> {
    @Bind(R.id.mEtSearch)
    EditText mEtSearch;
    @Bind(R.id.mFriendsView)
    RecyclerView mFriendsView;
    @Bind(R.id.mQuickIndex)
    QuickIndexBar mQuickIndex;

    private List<Contact> contacts;
    private String lastUpdateTime;
    private ContactsAdapter contactsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        SupportMultipleScreensUtil.scale(view);
        return view;
    }

    @Override
    public void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setUpData() {
        contacts = DataSupport.findAll(Contact.class);
        contactsAdapter = new ContactsAdapter();
        Collections.sort(contacts);
        mFriendsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mFriendsView.setAdapter(contactsAdapter);
        initSearch();
        initQuickIndex();
        lastUpdateTime = SPUtils.getString(getActivity(), ConstantValues.UPDATED_TIME, "");
        int id = SPUtils.getInt(getActivity(), ConstantValues.USER_ID, -1);
        HttpEngin.getInstance().post(UrlHelper.GET_CONTACTS, new ContactsRequestEntity(id, lastUpdateTime, DeviceUtils.getAndroidID()), ContactsResultEntity.class, this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void initQuickIndex() {
        mQuickIndex.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //根据letter找到列表中首字母和letter相同的条目，然后置顶
                for (int i = 0; i < contacts.size(); i++) {
                    String firstWord = PinYinUtil.getFirstLetter(contacts.get(i)) + "";
                    if (firstWord.equalsIgnoreCase(letter)) {
                        //说明找到了和letter同样字母的条目
                        //mFriendsView.smoothScrollToPosition();
                        //找到立即中断
                        break;
                    }
                }

            }
        });
    }

    private void initSearch() {
        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                if (hasFocus) {
                    et.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                } else if (!hasFocus && TextUtils.isEmpty(et.getText().toString().trim())) {
                    et.setGravity(Gravity.CENTER);
                }
            }
        });
        SpannableString spannableString = new SpannableString("表情 搜索");
        Drawable drawable = getResources().getDrawable(R.mipmap.search);
        drawable.setBounds(0, 0, SupportMultipleScreensUtil.getScaleValue(15), SupportMultipleScreensUtil.getScaleValue(15));
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mEtSearch.setHint(spannableString);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        ToastUtils.showLong("网络出现异常，无法更新好友列表");
    }

    @Override
    public void onResponse(ContactsResultEntity result, int id) {
        String updateTime = TimeUtils.millis2String(System.currentTimeMillis());
        SPUtils.saveString(getActivity(), ConstantValues.UPDATED_TIME, updateTime);
        if (result.getFlag()) {
            if (!contacts.contains(result.getResData())) {
                DataSupport.saveAll(result.getResData());
                contacts.addAll(result.getResData());
                Collections.sort(contacts);
                if (result.getResData() != null && result.getResData().size() != 0) {
                    contactsAdapter.notifyDataSetChanged();
                }
            }
//            mFriendsView.setAdapter(contactsAdapter);
        } else {
            ToastUtils.showLong("获取好友列表失败，请重试");
        }
    }

    public class ContactsAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_newfriend, parent, false);
            SupportMultipleScreensUtil.scale(view);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.dealWithPosition(position);
        }

        @Override
        public int getItemCount() {
            return contacts.size() + 1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.mFirstLetter)
            TextView mFirstLetter;
            @Bind(R.id.mPortraitView)
            PortraitView mPortraitView;
            @Bind(R.id.mTvName)
            TextView mTvName;
            @Bind(R.id.mContactItem)
            RelativeLayout mContactItem;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void dealWithPosition(int position) {
                if (position < 1) {
                    mPortraitView.setTag(R.id.is_normal_tag, "0");
                    mFirstLetter.setVisibility(View.GONE);
                    if (TextUtils.equals((String) mPortraitView.getTag(R.id.is_normal_tag), "0")) {
                        Glide.with(ContactsFragment.this).load(getActivity().getDrawable(R.mipmap.group_chat)).into(mPortraitView);
                        // mPortraitView.setImageDrawable(getActivity().getDrawable(R.mipmap.group_chat));
                    }
                    mTvName.setText(R.string.group_chat);
                    mContactItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //todo:group chat
                            ToastUtils.showLong("群聊");
                        }
                    });

                    // }
                } else {
                    final Contact contact = contacts.get(position - 1);
                    if (contact != null) {
                        //mPortraitView.setTag(R.id.is_normal_tag, contact.getLogo());
                        if (position == 1) {
                            mFirstLetter.setVisibility(View.VISIBLE);
                        } else {
                            if (PinYinUtil.getFirstLetter(contact) == PinYinUtil.getFirstLetter(contacts.get(position - 2))) {
                                mFirstLetter.setVisibility(View.GONE);
                            } else {
                                mFirstLetter.setVisibility(View.VISIBLE);
                            }
                        }
                        mFirstLetter.setText(PinYinUtil.getFirstLetter(contact) + "");
                        if (TextUtils.isEmpty(contact.getLogo())) {
                            mPortraitView.setImageDrawable(null);
                            mPortraitView.setName(contact.getUsername());
                        } else if (!contact.getLogo().equals(mPortraitView.getTag(R.id.is_normal_tag))) {
                            mPortraitView.setImageResource(R.mipmap.ic_launcher);
                            mPortraitView.setPortrait(getActivity(), contact.getLogo(), contact.getUsername());
                        }

                        mTvName.setText(contact.getUsername());
                        mContactItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //todo:好友信息
                                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                                intent.putExtra(ConstantValues.CONTACT_INFO, contact);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }

    }
}
