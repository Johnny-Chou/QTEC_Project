package com.im.qtec.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.im.qtec.R;
import com.im.qtec.activity.ChatActivity;
import com.im.qtec.constants.ConstantValues;
import com.im.qtec.core.BaseFragment;
import com.im.qtec.db.Contact;
import com.im.qtec.db.Conversation;
import com.im.qtec.event.MessageEvent;
import com.im.qtec.utils.EmoParser;
import com.im.qtec.utils.MessageUtils;
import com.im.qtec.utils.RecycleViewDivider;
import com.im.qtec.utils.SupportMultipleScreensUtil;
import com.im.qtec.widget.PortraitView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouyanglei on 2017/12/5.
 */

public class ConversationFragment extends BaseFragment {


    @Bind(R.id.mEtSearch)
    EditText mEtSearch;
    @Bind(R.id.mConversationRv)
    RecyclerView mConversationRv;

    private List<Conversation> conversationList;
    private ConversationFragment.conversationAdapter conversationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        SupportMultipleScreensUtil.scale(view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setUpData() {
        initSearch();
        initConversationList();
    }

    private void initConversationList() {
        conversationList = DataSupport.findAll(Conversation.class);
        sortConversationList();
        mConversationRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mConversationRv.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        conversationAdapter = new conversationAdapter();
        mConversationRv.setAdapter(conversationAdapter);
    }

    private void sortConversationList() {
        Collections.sort(conversationList, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation c1, Conversation c2) {
                return MessageUtils.getTime(c2.getLastMessage()) - MessageUtils.getTime(c1.getLastMessage());
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class conversationAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
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
//            for (int i = 0; i < conversationList.size(); i++) {
//                List<Contact> contacts = DataSupport.where("uid = ?", conversationList.get(i).getUid() + "").find(Contact.class);
//                if (contacts.size() == 0) {
//                    conversationList.remove(i);
//                }
//            }
            return conversationList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.mPortraitView)
            PortraitView mPortraitView;
            @Bind(R.id.mConversationItem)
            LinearLayout mConversationItem;
            @Bind(R.id.mBadgeLabel)
            TextView mBadgeLabel;
            @Bind(R.id.mTvName)
            TextView mTvName;
            @Bind(R.id.mTvTime)
            TextView mTvTime;
            @Bind(R.id.mTvContent)
            TextView mTvContent;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindData(int position) {
                Conversation conversation = conversationList.get(position);
                int uid = conversation.getUid();
                List<Contact> contacts = DataSupport.where("uid = ?", uid + "").find(Contact.class);
                if (contacts.size() > 0) {
                    final Contact contact = contacts.get(0);
                    //mPortraitView.setTag(R.id.is_normal_tag, contact.getUid());
                    if (TextUtils.isEmpty(contact.getLogo())) {
                        mPortraitView.setImageDrawable(null);
                        mPortraitView.setName(contact.getUsername());
                    } else if (!contact.getLogo().equals(mPortraitView.getTag(R.id.is_normal_tag))) {
                        mPortraitView.setImageResource(R.mipmap.ic_launcher);
                        mPortraitView.setPortrait(getActivity(), contact.getLogo(), contact.getUsername());
                    }
                    mTvName.setText(contact.getUsername());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss", Locale.getDefault());
                    mTvTime.setText(TimeUtils.millis2String((long) MessageUtils.getTime(conversation.getLastMessage()) * 1000, simpleDateFormat));
                    if (MessageUtils.getMessageType(conversation.getLastMessage()) == 3) {
                        mTvContent.setText("[语音]");
                    } else {
                        mTvContent.setText(EmoParser.parseEmo(getActivity(), MessageUtils.getMessage(conversation.getLastMessage()), 20));
                    }
                    mConversationItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra(ConstantValues.CONTACT_INFO, contact);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMqttMessage(MessageEvent event) {
        conversationList = DataSupport.findAll(Conversation.class);
        sortConversationList();
        conversationAdapter.notifyDataSetChanged();
    }
}
