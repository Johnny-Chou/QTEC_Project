package com.im.qtec.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.qtec.R;
import com.im.qtec.core.BaseFragment;
import com.im.qtec.widget.PortraitView;

/**
 * Created by zhouyanglei on 2017/12/7.
 */

public class CallFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu,container,false);
        return view;
    }

    @Override
    public void setUpView(View view) {

    }

    @Override
    public void setUpData() {

    }
}
