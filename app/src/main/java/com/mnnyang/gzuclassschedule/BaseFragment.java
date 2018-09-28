package com.mnnyang.gzuclassschedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

import java.util.Objects;

/**
 * Fragment 基类
 * Created by mnnyang on 17-4-11.
 */

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(this, "onCreateView");
        activity = getActivity();
        mRootView = inflater.inflate(getLayout(), container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.i(this, "onViewCreated");
        initData();
        initListener();
    }

    protected abstract int getLayout();

    public void initView() {

    }

    public void initListener() {

    }

    public void initData() {

    }

    public void toast(String s) {
        ToastUtils.show(s);
    }

    public void close() {
        if (isAdded()) {
            hideInput(Objects.requireNonNull(getContext()), mRootView);
        }
    }

    /**
     * 强制隐藏输入法键盘
     *
     * @param context Context
     * @param view    EditText
     */
    public static void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void backToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(this, "onDestroy");
    }
}