package com.im.qtec.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.qtec.R;
import com.im.qtec.core.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Lenovo on 2018/1/25.
 */

public class RecentCallFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_call,container,false);
        return view;
    }

    @Override
    public void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setUpData() {
//        pv.setName("test");
    }
}
